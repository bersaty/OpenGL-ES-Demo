package demo.myopengldemo.mydice;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import demo.myopengldemo.R;

/**
 * Created by wuchunhui on 17-2-21.
 */

public class Background {

    private  FloatBuffer mVerticesBuffer;
    private  FloatBuffer mVerticesTextureBuffer;

    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;
    private int mTextureUniformHandle;
    private int mTextureCoordinationHandle;

    private float[] mModelMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    private int mTextureId;

    private int mProgram;
    private Context mContext;

    public Background(Context context){
        mContext = context;
        initData();
        initShader();
    }

    //初始化数据
    private void initData(){
        // 平面顶点坐标，需要旋转一下，这些顶点z轴都为0
        final float[] VerticesData = {
                // X, Y, Z,
                -30.0f, -30.0f, 0.0f,

                30.0f, -30.0f, 0.0f,

                30.0f, 30.0f, 0.0f,

                -30.0f, -30.0f, 0.0f,

                30.0f, 30.0f, 0.0f,

                -30.0f, 30.0f, 0.0f,
        };

        final float[] TextureCoordinateData = {
                0,0,
                1,0,
                1,1,
                0,0,
                1,1,
                0,1
        };

        // Initialize the buffers.
        mVerticesBuffer = ByteBuffer.allocateDirect(VerticesData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVerticesBuffer.put(VerticesData).position(0);

        mVerticesTextureBuffer = ByteBuffer.allocateDirect(TextureCoordinateData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVerticesTextureBuffer.put(TextureCoordinateData).position(0);

    }

    private void initShader(){
        mProgram = MyGLUtils.buildProgram(mContext, R.raw.dice_bg_vertex, R.raw.dice_bg_fragment);

        mTextureId = MyGLUtils.loadTexture(mContext,R.drawable.container_wood,new int[2]);

        // Set program handles. These will later be used to pass in values to the program.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix");
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram,"u_Texture");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
//        mColorHandle = GLES20.glGetAttribLocation(mProgram, "a_Color");
        mTextureCoordinationHandle = GLES20.glGetAttribLocation(mProgram,"a_TexCoordinate");
//        mGlobalTimeHandle = GLES20.glGetUniformLocation(mProgram,"u_GlobalTime");

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
    }

    public void drawSelf() {

        GLES20.glUseProgram(mProgram);

        // Pass in the position information
        mVerticesBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                3*4, mVerticesBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        mVerticesTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinationHandle,2, GLES20.GL_FLOAT,false,2*4,mVerticesTextureBuffer);
        GLES20.glEnableVertexAttribArray(mTextureCoordinationHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
//        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

    }
    
}
