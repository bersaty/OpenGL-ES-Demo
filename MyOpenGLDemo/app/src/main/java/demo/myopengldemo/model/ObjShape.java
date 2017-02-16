package demo.myopengldemo.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import org.obj2openjl.v3.Obj2OpenJL;
import org.obj2openjl.v3.model.OpenGLModelData;
import org.obj2openjl.v3.model.RawOpenGLModel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import demo.myopengldemo.utils.MyGLUtils;

/**
 * Created by wuchunhui on 17-2-9.
 * 解析obj文件
 */

public class ObjShape {
    private FloatBuffer mVertexBuffer;
    private int mProgram;
    private int mPositionHandle;
    private int muMVPMatrixHandle;
    private int muMMatrixHandle;
    private int muLightLocationHandle;
    private int mTextureCoordHandle;
    private int textureId;
    private int muTextureHandle;

    private Context mContext;
    private FloatBuffer mTexureBuffer;
    private FloatBuffer mNormalBuffer;
    private int mNormalHandle;

    private int mVertexCount;
    private float vertices[];
    private float texures[];
    private float normals[];

    private String mObjFileName;
    private int mTexrureRes;
    public ObjShape(Context context, String objFileName, int texture) {
        this.mContext = context;
        this.mObjFileName = objFileName;
        this.mTexrureRes = texture;
        initVetexData();
    }

    public void initVetexData() {
        InputStream is = null;
        try {
            is = mContext.getAssets().open(mObjFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RawOpenGLModel openGLModel = new Obj2OpenJL().convert(is);
        OpenGLModelData openGLModelData = openGLModel.normalize().center().getDataForGLDrawArrays();

        vertices = openGLModelData.getVertices();//顶点坐标
        mVertexCount = vertices.length / 3;
        mVertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder
                .nativeOrder()).asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        texures = openGLModelData.getTextureCoordinates();//纹理坐标
        mTexureBuffer = ByteBuffer.allocateDirect(texures.length * 4).order(ByteOrder
                .nativeOrder()).asFloatBuffer();
        mTexureBuffer.put(texures);
        mTexureBuffer.position(0);

        normals = openGLModelData.getNormals();//法线
        mNormalBuffer = ByteBuffer.allocateDirect(normals.length * 4).order(ByteOrder
                .nativeOrder()).asFloatBuffer();
        mNormalBuffer.put(normals);
        mNormalBuffer.position(0);

        mProgram = MyGLUtils.buildProgram(vertexShaderCode,fragmentShaderCode,null);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");

        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
        muLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        muTextureHandle = GLES20.glGetUniformLocation(mProgram, "uTexture");

        initTexture(mTexrureRes);
    }

    // 初始化纹理
    public void initTexture(int res) {
        int [] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_MIRRORED_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_MIRRORED_REPEAT);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), res);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mVertexCount);
    }

    public void setValue(float[] mvpMatrix, float[] mMatrix) {
        GLES20.glUseProgram(mProgram);
        mVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        mTexureBuffer.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, mTexureBuffer);
        mTexureBuffer.position(0);
        GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mNormalBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
        GLES20.glEnableVertexAttribArray(mNormalHandle);

        GLES20.glUniform3f(muLightLocationHandle, 0, 0, 10);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, mMatrix, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(muTextureHandle, 0);
    }

    private String vertexShaderCode = "uniform mat4 uMVPMatrix;"
            + "attribute vec2 aTextureCoord;"
            + "varying vec2 vTextureCoord;"
            + "uniform mat4 uMMatrix;"
//            + "uniform vec3 uLightLocation;"
//            + "varying vec4 vDiffuse;"
            + "varying vec3 vFragPosition;"
            + "varying vec3 vNormal;"
            + "attribute vec3 aPosition;"
            + "attribute vec3 aNormal;"
            + "void main(){"
//            + "vec3 normalVectorOrigin = aNormal;"
//            + "vec3 normalVector = normalize((uMMatrix*vec4(normalVectorOrigin,1)).xyz);"
//            + "vec3 vectorLight = normalize(uLightLocation - (uMMatrix * vec4(aPosition,1)).xyz);"
//            + "float factor = max(0.0, dot(normalVector, vectorLight));"
//            + "vDiffuse = factor*vec4(1,1,1,1.0);"
            + "gl_Position = uMVPMatrix * vec4(aPosition,1);"
            + "vTextureCoord = aTextureCoord;"
            + "vNormal = aNormal;"
            + "vFragPosition = vec3(uMMatrix * vec4(aPosition,1.0));"
            + "}";

    private String fragmentShaderCode = "precision mediump float;"
            + "uniform sampler2D uTexture;"
            + "uniform vec3 uLightLocation;"
            + "varying vec2 vTextureCoord;"
            + "varying vec3 vFragPosition;"
//            + "varying  vec4 vColor;"
            + "varying vec3 vNormal;"
//            + "varying vec4 vDiffuse;"
            + "void main(){"
            + "vec3 normal = normalize(vNormal);"
            + "vec3 lightDir = normalize(uLightLocation-vFragPosition);"
            + "float factor = max(0.0, dot(normal, lightDir));"
            + "vec4 diffuse = factor * vec4(1.0,1.0,1.0,1.0);"

            + "gl_FragColor = (diffuse + vec4(0.6,0.6,0.6,1))*texture2D(uTexture, vec2(vTextureCoord.s,vTextureCoord.t));"
            + "}";

}