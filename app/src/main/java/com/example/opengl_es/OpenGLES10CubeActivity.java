package com.example.opengl_es;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.example.opengl_es.opengles10.CubeRenderer;

public class OpenGLES10CubeActivity extends OpenGLES10BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        render = new CubeRenderer();
        gLsurfaceView.setRenderer(render);
        //高性能，不持续，需要手动刷新
        gLsurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
