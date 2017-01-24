package com.example.opengl_es.opengles10;

import android.opengl.GLU;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 蘑菇&罐头 on 2016/7/24.
 */
public class PointRenderer extends BaseRenderer {

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        gl10.glClearColor(0,0,0,1);
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        gl10.glViewport(0,0,i,i1);
        float ratio = (float)i/(float)i1;
        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        gl10.glFrustumf(-1,1,-ratio,ratio,3,7);

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl10.glColor4f(1,0,0,1);
        gl10.glMatrixMode(GL10.GL_MODELVIEW);
        gl10.glLoadIdentity();
        GLU.gluLookAt(gl10,0,0,5,0,0,0,0,1,0);

        super.onDrawFrame(gl10);

        float r= 0f;//半径
        //计算点
        List<Float> ponitList = new ArrayList<Float>();
        float zdeata = 0.01f;
        float x,y,z=1f;
        for (float dr = 0;dr < Math.PI *6;dr = (float) (dr+Math.PI/32)){
            if(dr <Math.PI*3)
                r+=0.5/96;
            else r-=0.5/96;
            x = (float) (r*Math.cos(dr));
            y = (float) (r*Math.sin(dr));
            z = z- zdeata;
            ponitList.add(x);
            ponitList.add(y);
            ponitList.add(z);
        }

        //转换点成缓冲区
        ByteBuffer bytebuffer = ByteBuffer.allocateDirect(ponitList.size()*4);
        bytebuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatbuffer = bytebuffer.asFloatBuffer();
        for (float f:ponitList)
            floatbuffer.put(f);
        bytebuffer.position(0);

        //顶点指针
        gl10.glVertexPointer(3,GL10.GL_FLOAT,0,bytebuffer);

        //画数组
        gl10.glDrawArrays(GL10.GL_POINTS,0,ponitList.size()/3);
    }
}
