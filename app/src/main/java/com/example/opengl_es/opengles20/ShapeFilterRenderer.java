package com.example.opengl_es.opengles20;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.example.opengl_es.R;
import com.example.opengl_es.utils.MyGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by wuchunhui on 16-7-25.
 */

/**
 * This class implements our custom renderer. Note that the GL10 parameter passed in is unused for OpenGL ES 2.0
 * renderers -- the static class GLES20 is used instead.
 */
public class ShapeFilterRenderer extends BaseRenderer20 {
    private float[] mModelMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private final FloatBuffer mTriangle1Vertices;
    private final FloatBuffer mTriangle1VerticesColor;
    private final FloatBuffer mTriangle1VerticesTexture;
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;
    private int mTextureUniformHandle;
    private int mTextureCoordinationHandle;

    private int mGlobalTimeHandle;
    private final int mBytesPerFloat = 4;
    private final int mStrideBytes = 7 * mBytesPerFloat;
    private final int mPositionOffset = 0;
    private final int mPositionDataSize = 3;
    private final int mColorOffset = 3;
    private final int mColorDataSize = 4;

    private Context mContext;
    public ShapeFilterRenderer(Context context) {

        mContext = context;

        // This triangle is red, green, and blue.
        final float[] triangle1VerticesData = {
                // X, Y, Z,
                -0.5f, -0.5f, 0.0f,

                0.5f, -0.5f, 0.0f,

                0.5f, 0.5f, 0.0f,

                0.5f, 0.5f, 0.0f,

                -0.5f, -0.5f, 0.0f,

                -0.5f, 0.5f, 0.0f,
        };

        // This triangle is red, green, and blue.
        final float[] triangle1VerticesColorData = {
                // R, G, B, A
                1.0f, 0.0f, 0.0f, 1.0f,

                0.0f, 0.0f, 1.0f, 1.0f,

                0.0f, 1.0f, 0.0f, 1.0f,

                0.0f, 1.0f, 0.0f, 1.0f,

                1.0f, 0.0f, 0.0f, 1.0f,

                0.0f, 0.0f, 1.0f, 1.0f};

        final float[] triangle1TextureCoordinateData = {
                0,0,
                -1,0,
                -1,-1,
                -1,-1,
                0,0,
                0,-1

        };

        // Initialize the buffers.
        mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle1Vertices.put(triangle1VerticesData).position(0);

        mTriangle1VerticesColor = ByteBuffer.allocateDirect(triangle1VerticesColorData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle1VerticesColor.put(triangle1VerticesColorData).position(0);

        mTriangle1VerticesTexture = ByteBuffer.allocateDirect(triangle1TextureCoordinateData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle1VerticesTexture.put(triangle1TextureCoordinateData).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        // Set the background clear color to gray.
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.1f;

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

        int programHandle = MyGLUtils.buildProgram(mContext, R.raw.waverender_vs,R.raw.triangles_mosaic);

        int textureHandle = MyGLUtils.loadTexture(mContext,R.drawable.foxgirl,new int[2]);

        // Set program handles. These will later be used to pass in values to the program.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mTextureUniformHandle = GLES20.glGetUniformLocation(programHandle,"u_Texture");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
        mTextureCoordinationHandle = GLES20.glGetAttribLocation(programHandle,"a_TexCoordinate");
        mGlobalTimeHandle = GLES20.glGetUniformLocation(programHandle,"u_GlobalTime");


        // Set the active texture unit to texture unit 0.
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
//        GLES20.glUniform1i(mTextureUniformHandle, 0);

        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
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
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        // Draw the triangle facing straight on.
        Matrix.setIdentityM(mModelMatrix, 0);
//        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        rotate(mModelMatrix);
        Random random = new Random();
        GLES20.glUniform1f(mGlobalTimeHandle, angleInDegrees/360);
        drawShape(mTriangle1Vertices,mTriangle1VerticesColor,mTriangle1VerticesTexture);

    }

    /**
     * Draws a triangle from the given vertex data.
     *
     * @param aTriangleBuffer The buffer containing the vertex data.
     */
    private void drawShape(final FloatBuffer aTriangleBuffer, final FloatBuffer aTriangleBufferClolr,
                           final FloatBuffer aTriangleBufferTexture) {
        // Pass in the position information
        aTriangleBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                3*4, aTriangleBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        aTriangleBufferClolr.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false,
                4*4, aTriangleBufferClolr);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        aTriangleBufferTexture.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinationHandle,2,GLES20.GL_FLOAT,false,2*4,aTriangleBufferTexture);
        GLES20.glEnableVertexAttribArray(mTextureCoordinationHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }
}
