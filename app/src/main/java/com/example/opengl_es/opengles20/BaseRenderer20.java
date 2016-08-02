package com.example.opengl_es.opengles20;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by wuchunhui on 16-8-1.
 */
public class BaseRenderer20 implements GLSurfaceView.Renderer {

    public float angleDegreeX = 0;
    public float angleDegreeY = 0;
    public float angleDegreeZ = 0;

    private float[] mBaseViewMatrix = new float[16];
    private float[] mBaseModelMatrix = new float[16];
    private float[] mBaseProjectionMatrix = new float[16];
    private float[] mBaseMVPMatrix = new float[16];

    private FloatBuffer mBaseVertexFloatBuffer;
    private FloatBuffer mBaseVertexColorFloatBuffer;

    private float mBaseMVPMatrixHandle;
    private float mBasePositionHandle;
    private float mBaseColorHandle;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//        GLES20.glClearColor(0,0,0,0);
        float[] vertextData = new float[]{
                0,1,0,
                0,0,0,
                0,0,1,
                0,0,0,
                1,0,0

        };
        mBaseVertexFloatBuffer = ByteBuffer.allocateDirect(vertextData.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mBaseVertexFloatBuffer.put(vertextData).position(0);

//        Matrix.setLookAtM(mBaseViewMatrix,0,0,0,1.5f,0,0,-5,0,1,0);

        final String vertex_shader =
                "uniform mat4 u_MVPMatrix;\n" +
                        "attribute vec4 a_Position;\n" +
                        "attribute vec4 a_Color;\n" +
                        "varying vec4 v_Color;\n" +
                        "void main() {\n" +
                        "    gl_Position = u_MVPMatrix * a_Position;\n" +
                        "    v_Color = a_Color;\n" +
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

        mBaseMVPMatrixHandle = GLES20.glGetUniformLocation(shaderProgram,"u_MVPMatrix");
        mBasePositionHandle = GLES20.glGetAttribLocation(shaderProgram,"a_Position");
        mBaseColorHandle = GLES20.glGetAttribLocation(shaderProgram,"a_Color");

        GLES20.glUseProgram(shaderProgram);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
    }

    public void rotate(float[] matrix){
        Matrix.rotateM(matrix,0,angleDegreeX,1,0,0);
        Matrix.rotateM(matrix,0,angleDegreeY,0,1,0);
        Matrix.rotateM(matrix,0,angleDegreeZ,0,0,1);
    }


}
