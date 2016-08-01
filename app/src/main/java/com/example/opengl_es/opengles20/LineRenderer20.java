package com.example.opengl_es.opengles20;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by wuchunhui on 16-7-26.
 */
public class LineRenderer20 implements GLSurfaceView.Renderer {
    //4x4矩阵表示显示的物体
    private float[] mModelMatrix = new float[16];

    //4x4矩阵表示眼睛
    private float[] mViewMatrix = new float[16];

    //存储投影矩阵
    private float[] mProjectionMatrix = new float[16];

    //存放最终的矩阵，这个矩阵会传递到shader程序里
    private float[] mMVPMatrix = new float[16];

    //FloatBuffer
    private FloatBuffer mLinesVerticesFloatBuffer;

    private int mMVPMatrixHandle;

    //位置信息
    private int mPositionHandle;

    //颜色信息
    private int mColorHandle;

    //每个float占用字节数
    private final int mBytesPerFloat = 4;

    //每个顶点占用的字节数，顶点（X，Y，Z，R，G，B，A）
    private final int mStrideBytes = 7 * mBytesPerFloat;

    //顶点坐标起始位置
    private int mPositionOffset = 0;

    //坐标数据大小
    private int mPositionDataSize = 3;

    //顶点颜色起始位置
    private int mColorOffset = 3;

    //坐标数据大小
    private int mColorDataSize = 3;

    public LineRenderer20() {
        final float[] linesVerticesData = new float[]{
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.5f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f,

                -0.5f, 0.5f, 0.0f,
                0.0f, 0.0f, 1.0f, 1.0f,

                0.5f, 0.5f, 0.0f,
                0.0f, 1.0f, 0.0f, 1.0f,

                0.5f, -0.5f, 0.0f,
                1.0f, .0f, 0.0f, 1.0f
        };

        mLinesVerticesFloatBuffer = ByteBuffer.allocateDirect(linesVerticesData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mLinesVerticesFloatBuffer.put(linesVerticesData).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //背景颜色黑色
        GLES20.glClearColor(0, 0, 0, 0);
        // 眼睛的位置
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;

        // 观察的位置
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // 向上的方向，用于固定眼睛的位置
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        final String vertex_shader =
                "uniform mat4 u_MVPMatrix;\n" +
                "attribute vec4 a_Position;\n" +
                "attribute vec4 a_Color;\n" +
                "varying vec4 v_Color;\n" +
                "void main() {\n" +
                "    gl_Position = u_MVPMatrix * a_Position;\n" +
                "    v_Color = a_Color;\n" +
                "    gl_PointSize = 5.0;\n" +
                "}\n";

        final String fragment_shader = "" +
                "precision mediump float;  \n" +
                "varying vec4 v_Color;\n" +
                "void main() {\n" +
                "    gl_FragColor = v_Color;\n" +
                "}\n";

        //加载shader vertex
        int vertextShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        if(vertextShaderHandle != 0){
            GLES20.glShaderSource(vertextShaderHandle,vertex_shader);
            GLES20.glCompileShader(vertextShaderHandle);
            final int[] compileState = new int[1];
            GLES20.glGetShaderiv(vertextShaderHandle,GLES20.GL_COMPILE_STATUS,compileState,0);
            if(compileState[0] == 0){
                GLES20.glDeleteShader(vertextShaderHandle);
                vertextShaderHandle = 0;
            }
        }
        if(vertextShaderHandle == 0)
            throw new RuntimeException("Error creating vertex shader.");

        //加载shader fragment
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        if(fragmentShaderHandle != 0){
            GLES20.glShaderSource(fragmentShaderHandle,fragment_shader);
            GLES20.glCompileShader(fragmentShaderHandle);
            final int[] compileState = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle,GLES20.GL_COMPILE_STATUS,compileState,0);
            if(compileState[0] == 0){
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
            }
        }
        if(fragmentShaderHandle ==0)
            throw new RuntimeException("Error creating fragment shader.");

        //创建程序
        int shaderProgram = GLES20.glCreateProgram();
        if(shaderProgram != 0){
            //绑定shader程序
            GLES20.glAttachShader(shaderProgram,vertextShaderHandle);
            GLES20.glAttachShader(shaderProgram,fragmentShaderHandle);

            //绑定属性索引，需要我们赋值
            GLES20.glBindAttribLocation(shaderProgram,0,"a_Position");
            GLES20.glBindAttribLocation(shaderProgram,1,"a_Color");

            GLES20.glLinkProgram(shaderProgram);

            final int[] linkState = new int[1];

            GLES20.glGetProgramiv(shaderProgram,GLES20.GL_LINK_STATUS,linkState,0);
            if(linkState[0] == 0) {
                GLES20.glDeleteProgram(shaderProgram);
                shaderProgram = 0;
            }
        }
        if(shaderProgram == 0){
            throw new RuntimeException("Error creating shader program.");
        }

        mMVPMatrixHandle = GLES20.glGetUniformLocation(shaderProgram,"u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(shaderProgram,"a_Position");
        mColorHandle = GLES20.glGetAttribLocation(shaderProgram,"a_Color");

        GLES20.glUseProgram(shaderProgram);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_ALIASED_POINT_SIZE_RANGE);

        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        // Draw the triangle facing straight on.
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        drawLines(mLinesVerticesFloatBuffer);

    }

    private  void drawLines(FloatBuffer linesBuffer){
        linesBuffer.position(mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle,mPositionDataSize,GLES20.GL_FLOAT,false,mStrideBytes,linesBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        linesBuffer.position(mColorOffset);
        GLES20.glVertexAttribPointer(mColorHandle,mColorDataSize,GLES20.GL_FLOAT,false,mStrideBytes,linesBuffer);

        GLES20.glEnableVertexAttribArray(mColorHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, 4);
    }
}
