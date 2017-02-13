package demo.myopengldemo.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import demo.myopengldemo.R;
import demo.myopengldemo.view.LoadObjSurfaceView;

public class LoadObjActivity extends AppCompatActivity {

    LoadObjSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lightdemo_activity);

        mGLSurfaceView = new LoadObjSurfaceView(this);
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        linearLayout.addView(mGLSurfaceView);
        //高性能，不持续，需要手动刷新
//        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

//        LightRenderer render = new LightRenderer();
//        if (supportsEs2)
//        {
//            // Request an OpenGL ES 2.0 compatible context.
//            mGLSurfaceView.setEGLContextClientVersion(2);
//
//            // Set the renderer to our demo renderer, defined below.
//            mGLSurfaceView.setRenderer(render);
//        }
//        else
//        {
//            // This is where you could create an OpenGL ES 1.x compatible
//            // renderer if you wanted to support both ES 1 and ES 2.
//            return;
//        }
    }
}
