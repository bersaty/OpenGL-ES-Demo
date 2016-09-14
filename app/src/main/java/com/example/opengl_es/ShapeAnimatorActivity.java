package com.example.opengl_es;

import android.os.Bundle;
import android.view.View;

import com.example.opengl_es.opengles20.ShapeAnimatorRenderer;

public class ShapeAnimatorActivity extends OpenGLES20BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        render = new ShapeAnimatorRenderer(this);
        if (supportsEs2)
        {
            // Request an OpenGL ES 2.0 compatible context.
            mGLSurfaceView.setEGLContextClientVersion(2);

            // Set the renderer to our demo renderer, defined below.
            mGLSurfaceView.setRenderer(render);
        }
        else
        {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            return;
        }
    }
}
