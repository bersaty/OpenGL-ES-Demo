package com.example.opengl_es.opengles10;

import android.opengl.GLU;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 蘑菇&罐头 on 2016/7/24.
 */
public class CubeRenderer extends BaseRenderer {
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        gl10.glClearColor(0,0,0,1);
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        gl10.glViewport(0,0,i,i1);
        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        float ratio = (float)i/(float)i1;
        gl10.glFrustumf(-1f,1f,-ratio,ratio,3f,7f);

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl10.glMatrixMode(GL10.GL_MODELVIEW);
        gl10.glLoadIdentity();
        GLU.gluLookAt(gl10,0,0,5,0,0,0,0,1,0);
        super.onDrawFrame(gl10);
        float[] coords = {
                -0.5f,0.5f,-0.5f,
                0.5f,0.5f,-0.5f,
                0.5f,0.5f,0.5f,
                -0.5f,0.5f,0.5f,
                -0.5f,-0.5f,0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f,
        };
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(coords.length *4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(coords);
        byteBuffer.position(0);
        gl10.glColor4f(1f,0,0,1f);
        gl10.glVertexPointer(3,GL10.GL_FLOAT,0,byteBuffer);
        gl10.glDrawArrays(GL10.GL_TRIANGLE_FAN,0,coords.length/3);
    }
}
