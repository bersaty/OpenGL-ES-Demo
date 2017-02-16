package demo.myopengldemo.shadow;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class ShadowsActivity extends Activity {

    private ShadowsGLSurfaceView mGLView;
    private ShadowsRenderer renderer;
	/**
	 * Type of shadow bias to reduce unnecessary shadows
	 * 	- constant bias
	 * 	- bias value is variable according to slope
	 */
	private float mBiasType = 0.0f;
	/**
	 * Type of shadow algorithm
	 * 	- simple shadow (shadow value is only two state (yes/no) so aliasing is visible, no blur effect is possible)
	 *  - Percentage Closer Filtering (PCF)
	 */
	private float mShadowType = 0.0f;
	/**
	 * Shadow map size: 
	 * 	- displayWidth * SHADOW_MAP_RATIO
	 * 	- displayHeight * SHADOW_MAP_RATIO			
	 */
	private float mShadowMapRatio = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();


//		actionBar.show();

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new ShadowsGLSurfaceView(this);
        
        // Create an OpenGL ES 2.0 context.
        mGLView.setEGLContextClientVersion(2);
        
		renderer = new ShadowsRenderer(this);
		mGLView.setRenderer(renderer);
        
        setContentView(mGLView);
        
//        Toast.makeText(this, R.string.user_hint, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }
    
	public float getmBiasType() {
		return mBiasType;
	}

	private void setmBiasType(float mBiasType) {
		this.mBiasType = mBiasType;
	}

	public float getmShadowType() {
		return mShadowType;
	}

	private void setmShadowType(float mShadowType) {
		this.mShadowType = mShadowType;
	}

	public float getmShadowMapRatio() {
		return mShadowMapRatio;
	}

	private void setmShadowMapRatio(float mShadowMapRatio) {
		this.mShadowMapRatio = mShadowMapRatio;
	}
}
