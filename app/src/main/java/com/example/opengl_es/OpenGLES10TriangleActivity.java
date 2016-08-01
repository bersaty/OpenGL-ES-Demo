package com.example.opengl_es;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

import com.example.opengl_es.opengles10.TriangleRenderer;

public class OpenGLES10TriangleActivity extends OpenGLES10BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        render = new TriangleRenderer();
        gLsurfaceView.setRenderer(render);
        //高性能，不持续，需要手动刷新
        gLsurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
