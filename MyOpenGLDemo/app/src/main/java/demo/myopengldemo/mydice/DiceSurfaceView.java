package demo.myopengldemo.mydice;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by wuchunhui on 17-2-21.
 */

public class DiceSurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
    private DiceRenderer mRenderer;//场景渲染器

    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标
    //关于摄像机的变量
    float cx=0;//摄像机x位置
    float cy=0;//摄像机y位置
    float cz=-50;//摄像机z位置

    float tx=0;//目标点x位置
    float ty=0;//目标点y位置
    float tz=0;//目标点z位置
    public float currSightDis=60;//摄像机和目标的距离
    float angdegElevation=90;//仰角
    public float angdegAzimuth=180;//方位角

    //关于灯光的变量
    float lx=0;//x位置
    float ly=0;//y位置
    float lz=0;//z位置
    float lightDis=100;
    float lightElevation=50;//灯光仰角
    public float lightAzimuth=-30;//灯光的方位角
    public DiceSurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2); //设置使用OPENGL ES2.0
        mRenderer = new DiceRenderer();	//创建场景渲染器
        setRenderer(mRenderer);				//设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染
    }

    //触摸事件回调方法
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;//计算触控笔Y位移
                float dx = x - mPreviousX;//计算触控笔X位移
                angdegAzimuth += dx * TOUCH_SCALE_FACTOR;//设置沿x轴旋转角度
                angdegElevation += dy * TOUCH_SCALE_FACTOR;//设置沿z轴旋转角度
                //将仰角限制在5～90度范围内
                angdegElevation = Math.max(angdegElevation, 5);
                angdegElevation = Math.min(angdegElevation, 90);
                //设置摄像机的位置
                setCameraPostion();
        }
        mPreviousY = y;//记录触控笔位置
        mPreviousX = x;//记录触控笔位置
        return true;
    }

    // 位置灯光位置的方法
    public void setLightPostion() {
        //计算灯光的位置
        double angradElevation = Math.toRadians(lightElevation);// 仰角（弧度）
        double angradAzimuth = Math.toRadians(lightAzimuth);// 方位角
        lx = (float) (- lightDis * Math.cos(angradElevation)	* Math.sin(angradAzimuth));
        ly = (float) (+ lightDis * Math.sin(angradElevation));
        lz = (float) (- lightDis * Math.cos(angradElevation) * Math.cos(angradAzimuth));
    }

    // 设置摄像机位置的方法
    public void setCameraPostion() {
        //计算摄像机的位置
        double angradElevation = Math.toRadians(angdegElevation);// 仰角（弧度）
        double angradAzimuth = Math.toRadians(angdegAzimuth);// 方位角
        cx = (float) (tx - currSightDis * Math.cos(angradElevation)	* Math.sin(angradAzimuth));
        cy = (float) (ty + currSightDis * Math.sin(angradElevation));
        cz = (float) (tz - currSightDis * Math.cos(angradElevation) * Math.cos(angradAzimuth));
    }

    private class DiceRenderer implements GLSurfaceView.Renderer
    {

        Background mBackground;//地板
        Dice mDice;//骰子

        public void onDrawFrame(GL10 gl)
        {
            //清除深度缓冲与颜色缓冲
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            //设置camera位置,在上面往下面看
            MatrixState.setCamera
                    (
                            cx,   //人眼位置的X
                            cy, 	//人眼位置的Y
                            cz,   //人眼位置的Z
                            tx, 	//人眼球看的点X
                            ty,   //人眼球看的点Y
                            tz,   //人眼球看的点Z
                            0, 	//up位置
                            1,
                            0
                    );
            //初始化光源位置
            MatrixState.setLightLocation(lx, ly, lz);
            //若加载的物体部位空则绘制物体

            GLES20.glCullFace(GLES20.GL_FRONT);

            //绘制地板
            MatrixState.pushMatrix();
            MatrixState.rotate(-90,1,0,0);
            mBackground.drawSelf();
            MatrixState.popMatrix();

            //绘制骰子
            MatrixState.pushMatrix();
            MatrixState.translate(0,0,0);
//            MatrixState.rotate(-30,1,1,0);
            mDice.drawSelf(0);
            mDice.drawSelf(1);
            MatrixState.popMatrix();

            //绘制骰子
            MatrixState.pushMatrix();
            MatrixState.translate(15,5,10);
            MatrixState.rotate(-30,1,1,0);
            mDice.drawSelf(0);
            mDice.drawSelf(1);
            MatrixState.popMatrix();

            //绘制骰子
            MatrixState.pushMatrix();
            MatrixState.translate(-15,5,-10);
            MatrixState.rotate(-60,1,1,0);
            mDice.drawSelf(0);
            mDice.drawSelf(1);
            MatrixState.popMatrix();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视窗大小及位置
            GLES20.glViewport(0, 0, width, height);
            //计算GLSurfaceView的宽高比
            float ratio = (float) width / height;
            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);

            //计算摄像机的位置
            setCameraPostion();
            //计算灯光的位置
            setLightPostion();
            new Thread(){
                @Override
                public void run(){
                    while(true){
                        lightAzimuth +=1;
                        lightAzimuth %= 360;
                        //改变灯光的位置
//                        setLightPostion();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES20.glClearColor(0.3f,0.3f,0.3f,1.0f);
            //打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //关闭背面剪裁
            GLES20.glDisable(GLES20.GL_CULL_FACE);
            //初始化变换矩阵
            MatrixState.setInitStack();

            mBackground = new Background(getContext());
            mDice = LoadUtil.loadDiceObj("boxes.wvo",getResources(),getContext());
        }

    }
}
