package demo.myopengldemo.utils;

import android.opengl.Matrix;

/**
 * Created by wuchunhui on 17-1-22.
 * opengl 矩阵的各种状态。
 * 计算MVP矩阵，设置相机位置，正交投影以及透视投影。
 */

public class MatrixStates {
    private static float[] mModelMatrix = new float[16];
    private static float[] mViewMatrix = new float[16];
    private static float[] mProjectionMatrix = new float[16];
    private static float[] mMVPMatrix = new float[16];

    /**
     * 设置相机位置，即眼睛位置：
     * 眼睛坐标，目标位置，眼睛向上的方向
     */
    public static void setCamera(float eyeX, float eyeY, float eyeZ,
                                 float targetX, float targetY, float targetZ,
                                 float upX, float upY, float upZ) {
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, targetX, targetY, targetZ, upX, upY, upZ);
    }

    /**
     * 获取最终的变换矩阵 (M*V*P)
     * @return
     */
    public static float[] getFinalMatrix(float[] modelMatrix){
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    /**
     * 设置透视投影
     */
    public static void setProjectFrustum(float left, float right, float bottom, float top, float near, float far){
        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    /**
     * 设置正交投影
     */
    public static void setProjectOrtho(float left, float right, float bottom, float top, float near, float far){
        Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }
}
