package com.example.opengl_es;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    BaseRenderer render;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyGLsurfaceView gLsurfaceView = new MyGLsurfaceView(this);
        render = new TriangleRenderer();
        gLsurfaceView.setRenderer(render);
        setContentView(R.layout.activity_main);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        linearLayout.addView(gLsurfaceView);

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
                render.setRotate((float) (Math.PI/4)*d,1,0,0);
                break;
            case R.id.btny:
                render.setRotate((float) (Math.PI/4)*d,0,1,0);
                break;
            case R.id.btnz:
                render.setRotate((float) (Math.PI/4)*d,0,0,1);
                break;
        }
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
