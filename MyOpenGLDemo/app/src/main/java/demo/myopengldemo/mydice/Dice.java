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

public class Dice {

    FloatBuffer mVerticesBuffer;//顶点坐标数据缓冲
    FloatBuffer mNormalBuffer;//顶点法向量数据缓冲
    FloatBuffer mVerticesTextureBuffer;

    private int mVertexCount;

    private int mProgram;
    private Context mContext;

    private int mModelMatrixHandle;
    private int mVPMatrixHandle;
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;
    private int mTextureId;
    private int mTextureCoordinationHandle;
    private int mIsShadowHandle;
    private int mLightPositionHandle;
    private int mCameraPositionHandle;
    private int mNormalHandle;

    public Dice(Context context,float[] verticesData,float[] verticesNormalData){
        mContext = context;
        initData(verticesData,verticesNormalData);
        initShader();
    }

    public void initData(float[] verticesData,float[] verticesNormalData){

        mVertexCount = verticesData.length/3;

        // Initialize the buffers.
        mVerticesBuffer = ByteBuffer.allocateDirect(verticesData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVerticesBuffer.put(verticesData).position(0);

        mNormalBuffer = ByteBuffer.allocateDirect(verticesNormalData.length*4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mNormalBuffer.put(verticesNormalData).position(0);

//        mVerticesTextureBuffer = ByteBuffer.allocateDirect(textureCoordinateData.length * 4)
//                .order(ByteOrder.nativeOrder()).asFloatBuffer();
//        mVerticesBuffer.put(textureCoordinateData).position(0);
    }

    private void initShader(){
        mProgram = MyGLUtils.buildProgram(mContext, R.raw.dice_scene_vertex, R.raw.dice_scene_frag);

        mTextureId = MyGLUtils.loadTexture(mContext,R.drawable.t1,new int[2]);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mModelMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
        mVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,"uMProjCameraMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mCameraPositionHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
        mLightPositionHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        mTextureCoordinationHandle = GLES20.glGetAttribLocation(mProgram,"aTextureCoord");
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
        mIsShadowHandle = GLES20.glGetUniformLocation(mProgram,"isShadow");

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
    }

    public void drawSelf(int isShadow) {
        //制定使用某套着色器程序
        GLES20.glUseProgram(mProgram);
        //将最终变换矩阵传入着色器程序
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //将位置、旋转变换矩阵传入着色器程序
        GLES20.glUniformMatrix4fv(mModelMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        //将光源位置传入着色器程序
        GLES20.glUniform3fv(mLightPositionHandle, 1, MatrixState.lightPositionFB);
        //将摄像机位置传入着色器程序
        GLES20.glUniform3fv(mCameraPositionHandle, 1, MatrixState.cameraFB);
        //将是否绘制阴影属性传入着色器程序
        GLES20.glUniform1i(mIsShadowHandle, isShadow);
        //将投影、摄像机组合矩阵传入着色器程序
        GLES20.glUniformMatrix4fv(mVPMatrixHandle, 1, false, MatrixState.getViewProjMatrix(), 0);

        //将顶点位置数据传入渲染管线
        GLES20.glVertexAttribPointer
                (
                        mPositionHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3*4,
                        mVerticesBuffer
                );
        //将顶点法向量数据传入渲染管线
        GLES20.glVertexAttribPointer
                (
                        mNormalHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3*4,
                        mNormalBuffer
                );
        //启用顶点位置、法向量数据
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mNormalHandle);

        //设置纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
//        GLES20.glUniform1i(mTextureHandle, 0);

        //绘制被加载的物体
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mVertexCount);
    }

}
