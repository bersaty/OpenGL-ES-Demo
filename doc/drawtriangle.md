####绘制三角形
#### 1、创建一个GLSurfaceView
GLSurfaceView是一个载体，相当于画布，需要绘制的内容都在这个画布上绘制。
```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new GLSurfaceView(this);
        setContentView(mGLSurfaceView);
        }
```
处理好onPause，onResume等。

```java
    @Override
    protected void onResume()
    {
        // The activity must call the GL surface view's onResume() on activity onResume().
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause()
    {
        // The activity must call the GL surface view's onPause() on activity onPause().
        super.onPause();
        mGLSurfaceView.onPause();
    }
```

#### 2、创建Renderer

用于渲染，相当于画笔，主要的工作都在Renderer里面完成。Renderer继承GLSurfaceView.Renderer，需要实现三个接口，
```java
    public class TriangleRenderer20 implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //请屏颜色 4个参数分别对应R，G，B，A（红，绿，蓝，透明度）
        GLES20.glClearColor(0f, 0f, 0f, 0f);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
        }

        @Override
        public void onDrawFrame(GL10 gl) {
        }
    }
```
