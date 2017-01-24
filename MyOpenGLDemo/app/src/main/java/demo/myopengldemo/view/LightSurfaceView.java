package demo.myopengldemo.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import demo.myopengldemo.renderer.LightRenderer;

/**
 * Created by wuchunhui on 17-1-22.
 */

public class LightSurfaceView extends GLSurfaceView {
    private double nLenStart;

    float lastX, lastY;
    LightRenderer mLightRenderer;//对应的渲染

    public LightSurfaceView(Context context) {
        super(context);
    }

    public LightSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int nCnt = event.getPointerCount();

        //两个手指岸按下的时候记录当前位置数据
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN && 2 == nCnt)
        {

            int xlen = Math.abs((int) event.getX(0) - (int) event.getX(1));
            int ylen = Math.abs((int) event.getY(0) - (int) event.getY(1));

            nLenStart = Math.sqrt((double) xlen * xlen + (double) ylen * ylen);

        } else if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE && 2 <= nCnt) {//移动的时候计算缩放大小

            int xlen = Math.abs((int) event.getX(0) - (int) event.getX(1));
            int ylen = Math.abs((int) event.getY(0) - (int) event.getY(1));

            double nLenEnd = Math.sqrt((double) xlen * xlen + (double) ylen * ylen);

            //两个手指放大预览区
            double delta = nLenEnd - nLenStart;//放大缩小移动距离
            Log.i("wch delta = ",delta + "~~~~` ");
            nLenStart = nLenEnd;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            return false;
        }

        return true;
    }
}
