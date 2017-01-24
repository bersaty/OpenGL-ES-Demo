package com.example.opengl_es.opengles20;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.example.opengl_es.R;
import com.example.opengl_es.utils.MyGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by wuchunhui on 16-7-25.
 */

/**
 * This class implements our custom renderer. Note that the GL10 parameter passed in is unused for OpenGL ES 2.0
 * renderers -- the static class GLES20 is used instead.
 */
public class GeometryAnimationRenderer implements GLSurfaceView.Renderer {
//    private float[] mModelMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
//    private float[] mMVPMatrix = new float[16];
    private final FloatBuffer mTriangle1Vertices;
    private final FloatBuffer mTriangle1VerticesColor;
//    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;
    private int mGlobalTimeHandle;
    private int mResolutionHandle;

    private Context mContext;
    public GeometryAnimationRenderer(Context context) {

        mContext = context;

        // This triangle is red, green, and blue.
        final float[] triangle1VerticesData = {
                // X, Y, Z,
                -10f, -10f, 0.0f,

                10f, -10f, 0.0f,

                10f, 10f, 0.0f,

                10f, 10f, 0.0f,

                -10f, -10f, 0.0f,

                -10f, 10f, 0.0f,
        };

        // This triangle is red, green, and blue.
        final float[] triangle1VerticesColorData = {
                // R, G, B, A
                0.32f, 0.48f, 0.98f, 1.0f,

                0.32f, 0.48f, 0.98f, 1.0f,

                0.32f, 0.48f, 0.98f, 1.0f,

                0.32f, 0.48f, 0.98f, 1.0f,

                0.32f, 0.48f, 0.98f, 1.0f,

                0.32f, 0.48f, 0.98f, 1.0f};

        // Initialize the buffers.
        mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle1Vertices.put(triangle1VerticesData).position(0);

        mTriangle1VerticesColor = ByteBuffer.allocateDirect(triangle1VerticesColorData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle1VerticesColor.put(triangle1VerticesColorData).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        // Set the background clear color to gray.
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 10.0f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = 0.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        int programHandle = MyGLUtils.buildProgram(mContext, R.raw.geometryanimation_vs,R.raw.geometryanimation_fs);

        // Set program handles. These will later be used to pass in values to the program.
//        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
//        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
        mGlobalTimeHandle = GLES20.glGetUniformLocation(programHandle,"u_GlobalTime");
        mResolutionHandle = GLES20.glGetUniformLocation(programHandle,"iResolution");

        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle);

//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

//        GLES20.glStencilMask(0);
//        GLES20.glStencilFunc(GLES20.GL_EQUAL,1,0Xff);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 3.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_STENCIL_BUFFER_BIT);

        //角度变化时间
        long time = SystemClock.uptimeMillis() % 150000L;
        float angleInDegrees = (360.0f / 150000.0f) * ((int) time);

        // Draw the triangle facing straight on.
//        Matrix.setIdentityM(mModelMatrix, 0);
        //传入的GlobalTime这里转化为角度后再传入，直接传时间会不断闪烁
        GLES20.glUniform1f(mGlobalTimeHandle, angleInDegrees);
//        GLES20.glUniform3fv(mResolutionHandle,1,FloatBuffer.wrap(new float[]{1080f,1920f,1.0f}));
        drawShape(mTriangle1Vertices,mTriangle1VerticesColor);
    }

    /**
     * Draws a triangle from the given vertex data.
     *
     * @param aTriangleBuffer The buffer containing the vertex data.
     */
    private void drawShape(final FloatBuffer aTriangleBuffer, final FloatBuffer aTriangleBufferClolr) {
        // Pass in the position information
        aTriangleBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                3*4, aTriangleBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
//        aTriangleBufferClolr.position(0);
//        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false,
//                4*4, aTriangleBufferClolr);
//        GLES20.glEnableVertexAttribArray(mColorHandle);

//        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
//        // (which currently contains model * view).
//        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
//
//        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
//        // (which now contains model * view * projection).
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
//
//        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }
}
