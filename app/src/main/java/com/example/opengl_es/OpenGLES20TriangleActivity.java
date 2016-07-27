package com.example.opengl_es;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.opengl_es.opengles20.TriangleRenderer20;

import java.util.ArrayList;
import java.util.List;

public class OpenGLES20TriangleActivity extends AppCompatActivity implements View.OnClickListener{
    GLSurfaceView.Renderer render;
    Spinner spinner;
    GLSurfaceView mGLSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new GLSurfaceView(this);
        setContentView(R.layout.activity_opengles10);

        spinner = (Spinner) findViewById(R.id.spinner);
        //数据
        List<String> data_list = new ArrayList<String>();
        data_list.add("顺时针");
        data_list.add("逆时针");

        //适配器
        ArrayAdapter arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);

        Button btnX = (Button) findViewById(R.id.btnx);
        Button btnY = (Button) findViewById(R.id.btny);
        Button btnZ = (Button) findViewById(R.id.btnz);
        Button btnReset = (Button) findViewById(R.id.btnreset);
        btnReset.setOnClickListener(this);
        btnX.setOnClickListener(this);
        btnY.setOnClickListener(this);
        btnZ.setOnClickListener(this);

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2)
        {
            // Request an OpenGL ES 2.0 compatible context.
            mGLSurfaceView.setEGLContextClientVersion(2);

            // Set the renderer to our demo renderer, defined below.
            mGLSurfaceView.setRenderer(new TriangleRenderer20());
        }
        else
        {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            return;
        }

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        linearLayout.addView(mGLSurfaceView);
        //高性能，不持续，需要手动刷新
//        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onClick(View view) {
//        String selectedItem = (String) spinner.getSelectedItem();
//        int d= 1;
//        if(selectedItem.equals("顺时针"))
//            d = -1;
//
//        switch (view.getId()){
//            case R.id.btnx:
//                render.rotateDetaX +=(float) (Math.PI/2)*d;
//                break;
//            case R.id.btny:
//                render.rotateDetaY +=(float) (Math.PI/2)*d;
//                break;
//            case R.id.btnz:
//                render.rotateDetaZ +=(float) (Math.PI/2)*d;
//                break;
//            case R.id.btnreset:
//                render.rotateDetaX = 0;
//                render.rotateDetaZ = 0;
//                render.rotateDetaY = 0;
//                break;
//        }
        mGLSurfaceView.requestRender();
    }

    @Override
    protected void onResume()
    {
        // The activity must call the GL surface view's onResume() on activity onResume().
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause()
    {
        // The activity must call the GL surface view's onPause() on activity onPause().
        super.onPause();
        mGLSurfaceView.onPause();
    }

}
