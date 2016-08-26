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

renderer用于渲染，相当于画笔，主要的工作都在Renderer里面完成。Renderer继承GLSurfaceView.Renderer，需要实现三个接口，其中的GL10 参数是OpenGL 1.0遗留下来的，2.0之后不用使用了。
```java
    public class TriangleRenderer20 implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //surface创建的时候调用
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
        //surface发生变化的时候会调用，横竖屏切换或者创建后都会调用
        }

        @Override
        public void onDrawFrame(GL10 gl) {
        //绘制的时候调用
        }
    }
```

在Renderer里面所需要做的工作如下：

0、创建顶点数据,float[] buffer;

1、初始化顶点缓冲区对象,FloatBuffer;

2、清屏，相当于设置背景颜色，GLES20.glClearColor;

3、初始化相机位置，Matrix.setLookAtM;

4、编写着色器程序，vertexShader和fragmentShader;

5、加载着色器,GLES20.glShaderSource;

6、编译着色器,GLES20.glCompileShader;

7、创建程序,GLES20.glCreateProgram();

8、连接顶点着色器和片段着色器程序,GLES20.glLinkProgram();

9、将程序设置到GPU运行,GLES20.glUseProgram();

10、连接顶点属性，glVertexAttribPointer，glEnableVertexAttribArray；

11、计算最终的MVPMatrix并将矩阵传递到着色器程序里；

12、绘制数组内容；


```java

    /**
     * 0、创建顶点数据，包括顶点坐标和颜色
     */
    public TriangleRenderer20() {
        // 三角形的颜色红，绿，蓝
        final float[] triangle1VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.25f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f,

                0.5f, -0.25f, 0.0f,
                0.0f, 0.0f, 1.0f, 1.0f,

                0.0f, 0.559016994f, 0.0f,
                0.0f, 1.0f, 0.0f, 1.0f};

        // 1、将顶点数据转化成FloatBuffer，这样OpenGL 才能使用
        mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle1Vertices.put(triangle1VerticesData).position(0);
    }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //2、请屏颜色 4个参数分别对应R，G，B，A（红，绿，蓝，透明度）
        GLES20.glClearColor(0f, 0f, 0f, 0f);
        // 3、初始化相机位置，相当于眼睛的位置，以及观察的方向。
        // 在OpenGL 1 时使用的是模型矩阵和视图矩阵的组合。在OpenGL 2我们可以使用其中一种。
        // 设置相机的位置，mViewMatrix为返回的结果，rmOffset是矩阵开始的位置，（eyeX, eyeY, eyeZ）为相机的坐标，
        // （lookX, lookY, lookZ）为物体中心的坐标，（upX, upY, upZ）相机向上的坐标，用于固定相机的方向
        Matrix.setLookAtM(mViewMatrix, rmOffset, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
        
        //OpenGL Shader Language,简称GLSL，它是一种类似于C语言的专门为GPU设计的语言，它可以放在GPU里面被并行运行。
        //4、编写着色器程序。
        final String vertexShader =
                "uniform mat4 u_MVPMatrix;      \n"        // 这个是4x4的矩阵常量，表示model/view/projection矩阵

                        + "attribute vec4 a_Position;     \n"        // 每一个顶点的颜色数据，用一个4位的向量表示，需要外部设置
                        + "attribute vec4 a_Color;        \n"        // 每一个顶点的颜色数据，用一个4位的向量表示，需要外部设置

                        + "varying vec4 v_Color;          \n"        // 这个值会传递到 fragment shader.

                        + "void main()                    \n"        //  vertex shader的入口函数
                        + "{                              \n"
                        + "   v_Color = a_Color;          \n"        // 对 fragment shader设置Color.
                        // It will be interpolated across the triangle.
                        + "   gl_Position = u_MVPMatrix   \n"    // gl_Position 内部参数用于存储最终的位置参数
                        + "               * a_Position;   \n"     // 通过矩阵相乘计算出顶点在屏幕的最终坐标
                        + "}                              \n";    //

        final String fragmentShader =
                "precision mediump float;       \n"        // Set the default precision to medium. We don't need as high of a
                        // precision in the fragment shader.
                        + "varying vec4 v_Color;          \n"        // This is the color from the vertex shader interpolated across the
                        // triangle per fragment.
                        + "void main()                    \n"        // The entry point for our fragment shader.
                        + "{                              \n"
                        + "   gl_FragColor = v_Color;     \n"        // Pass the color directly through the pipeline.
                        + "}                              \n";

        // 5、加载着色器，加载 vertex shader 返回句柄
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        if (vertexShaderHandle != 0) {
            // 向顶点着色器中添加源码shader source.
            GLES20.glShaderSource(vertexShaderHandle, vertexShader);

            // 6、编译vertex shader
            GLES20.glCompileShader(vertexShaderHandle);

            // 获取编译结果
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // 如果编译失败，删除这个程序句柄
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }
        }

        if (vertexShaderHandle == 0) {
            throw new RuntimeException("Error creating vertex shader.");
        }
        }
        
        // 5、加载着色器，加载 fragment shader shader.
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        if (fragmentShaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);

            // 6、编译fragment shader
            GLES20.glCompileShader(fragmentShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
            }
        }

        if (fragmentShaderHandle == 0) {
            throw new RuntimeException("Error creating fragment shader.");
        }
        
        // 7、创建运行程序，加载上面的句柄
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");

            // 8、连接vertex shader 和 fragment shader
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }
        
        
        // 获取程序运行后返回的各个句柄
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");

        // 9、告诉OpenGL 使用这个程序渲染
        GLES20.glUseProgram(programHandle);
```

最后渲染绘制图形，调用GLES20.glDrawArrays绘制。
```java
    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        // Draw the triangle facing straight on.
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        drawTriangle(mTriangle1Vertices);

    }
    
    private void drawTriangle(final FloatBuffer aTriangleBuffer) {
        // 10、连接顶点属性，告诉着色器怎么解析顶点数据
        aTriangleBuffer.position(mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, aTriangleBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // 10、连接顶点属性，告诉OpenGL怎么解析颜色数据
        aTriangleBuffer.position(mColorOffset);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, aTriangleBuffer);

        GLES20.glEnableVertexAttribArray(mColorHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        
        //11、将最终的mvp矩阵传递到着色器中
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        //12、绘制数组
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
```
