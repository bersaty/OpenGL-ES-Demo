package com.example.opengl_es.opengles20;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by wuchunhui on 16-8-1.
 */
public class BaseRenderer20 implements GLSurfaceView.Renderer {

    public float angleDegreeX = 0;
    public float angleDegreeY = 0;
    public float angleDegreeZ = 0;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

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
