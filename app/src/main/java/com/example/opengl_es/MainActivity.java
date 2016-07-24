package com.example.opengl_es;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyGLsurfaceView gLsurfaceView = new MyGLsurfaceView(this);
        TriangleRenderer render = new TriangleRenderer();
        gLsurfaceView.setRenderer(render);
        setContentView(R.layout.activity_main);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        linearLayout.addView(gLsurfaceView);
    }

    class MyGLsurfaceView extends GLSurfaceView{

        public MyGLsurfaceView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyGLsurfaceView(Context context) {
            super(context);
        }
    }

}
