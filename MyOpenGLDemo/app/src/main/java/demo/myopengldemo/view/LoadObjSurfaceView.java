package demo.myopengldemo.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import demo.myopengldemo.R;
import demo.myopengldemo.model.ObjShape;

public class LoadObjSurfaceView extends GLSurfaceView{

	private float mPreviousY;
	private float mPreviousX;
	MyRender mMyRender;
	public LoadObjSurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		mMyRender = new MyRender(context);
		setRenderer(mMyRender);
	}
	
	public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dy = y - mPreviousY;
            float dx = x - mPreviousX;
            mMyRender.yAngle += dx;
            mMyRender.xAngle+= dy;
            requestRender();
        }
        mPreviousY = y;
        mPreviousX = x;
        return true;
    }
	
	class MyRender implements Renderer {
		private ObjShape mShap1;
		private ObjShape mShap2;
		float yAngle;
    	float xAngle;
		private Context mContext;
    	public MyRender(Context context) {
    		mContext = context;
    	}
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1);
//			mShap1 = new Shape(mContext,"obj1.obj", R.drawable.t3);
			mShap2 = new ObjShape(mContext,"poly_hay_obj.obj", R.drawable.t1);
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			GLES20.glViewport(0, 0, width, height);
			Matrix.perspectiveM(mProjectionMatrix, 0, 45, (float)width/height, 2, 5);
			Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3,  0, 0, 0, 0, 1, 0);
		}
	
	    private final float[] mProjectionMatrix = new float[16];
	    private final float[] mViewMatrix = new float[16];
	    private final float[] mModuleMatrix = new float[16];
	    private final float[] mViewProjectionMatrix = new float[16];
	    private final float[] mMVPMatrix = new float[16];
		@Override
		public void onDrawFrame(GL10 gl) {
			GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
			// draw shape1
//			Matrix.setIdentityM(mModuleMatrix, 0);
//			Matrix.translateM(mModuleMatrix, 0, 0, 0.0f, 0);
//			Matrix.rotateM(mModuleMatrix, 0, xAngle, 1, 0, 0);
//			Matrix.rotateM(mModuleMatrix, 0, yAngle, 0, 1, 0);
//			Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
//			Matrix.multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mModuleMatrix, 0);
//			mShap1.setValue(mMVPMatrix, mModuleMatrix);
//			mShap1.draw();
			
			// draw shape2
			Matrix.setIdentityM(mModuleMatrix, 0);
			Matrix.translateM(mModuleMatrix, 0, 0, 0.0f, 0);
			Matrix.rotateM(mModuleMatrix, 0, xAngle, 1, 0, 0);
			Matrix.rotateM(mModuleMatrix, 0, yAngle, 0, 1, 0);
			Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
			Matrix.multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mModuleMatrix, 0);
			mShap2.setValue(mMVPMatrix, mModuleMatrix);
			mShap2.draw();

		}
	}
}