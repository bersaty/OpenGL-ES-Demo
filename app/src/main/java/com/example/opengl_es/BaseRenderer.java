package com.example.opengl_es;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 蘑菇&罐头 on 2016/7/24.
 */
public class BaseRenderer implements GLSurfaceView.Renderer {

    //顺时针为负
    protected float rotateDeta;
    //x，y，z轴选择的方向
    protected float rotateXdir,rotateYdir,rotateZdir;
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glRotatef(rotateDeta,rotateXdir,rotateYdir,rotateZdir);
    }

    public void setRotate(float rotateDeta,float rotateXdir,float rotateYdir,float rotateZdir){
        this.rotateDeta += rotateDeta;
        this.rotateXdir = rotateXdir;
        this.rotateYdir = rotateYdir;
        this.rotateZdir = rotateZdir;

    }
}
