package com.example.opengl_es;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.opengl_es.opengles10.BaseRenderer;
import com.example.opengl_es.opengles10.TriangleRenderer;

import java.util.ArrayList;
import java.util.List;

public class OpenGLES10TriangleActivity extends AppCompatActivity implements View.OnClickListener{
    BaseRenderer render;
    Spinner spinner;
    MyGLsurfaceView gLsurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gLsurfaceView = new MyGLsurfaceView(this);
        render = new TriangleRenderer();
        gLsurfaceView.setRenderer(render);
        setContentView(R.layout.activity_opengles10);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        linearLayout.addView(gLsurfaceView);
        //高性能，不持续，需要手动刷新
        gLsurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

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
    }

    @Override
    public void onClick(View view) {
        String selectedItem = (String) spinner.getSelectedItem();
        int d= 1;
        if(selectedItem.equals("顺时针"))
            d = -1;

        switch (view.getId()){
            case R.id.btnx:
                render.rotateDetaX +=(float) (Math.PI/2)*d;
                break;
            case R.id.btny:
                render.rotateDetaY +=(float) (Math.PI/2)*d;
                break;
            case R.id.btnz:
                render.rotateDetaZ +=(float) (Math.PI/2)*d;
                break;
            case R.id.btnreset:
                render.rotateDetaX = 0;
                render.rotateDetaZ = 0;
                render.rotateDetaY = 0;
                break;
        }
        gLsurfaceView.requestRender();
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
