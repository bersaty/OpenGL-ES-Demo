package com.example.opengl_es.opengles10;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 蘑菇&罐头 on 2016/7/24.
 */
public class BaseRenderer implements GLSurfaceView.Renderer {

    //旋转角度，顺时针为负
    public float rotateDetaX;
    public float rotateDetaY;
    public float rotateDetaZ;
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glRotatef(rotateDetaX,1,0,0);
        gl10.glRotatef(rotateDetaY,0,1,0);
        gl10.glRotatef(rotateDetaZ,0,0,1);
    }

}
