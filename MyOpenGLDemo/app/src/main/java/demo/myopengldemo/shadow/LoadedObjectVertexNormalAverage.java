package demo.myopengldemo.shadow;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

//加载后的物体——携带顶点信息，自动计算面平均法向量
public class LoadedObjectVertexNormalAverage
{
    int mProgram;//自定义渲染管线着色器程序id
    int muMVPMatrixHandle;//总变换矩阵引用
    int muMMatrixHandle;//位置、旋转变换矩阵
    int maPositionHandle; //顶点位置属性引用
    int maNormalHandle; //顶点法向量属性引用
    int maLightLocationHandle;//光源位置属性引用
    int maCameraHandle; //摄像机位置属性引用
    int muIsShadow;//是否绘制阴影属性引用
    int muProjCameraMatrixHandle;//投影、摄像机组合矩阵引用

    String mVertexShader;//顶点着色器代码脚本
    String mFragmentShader;//片元着色器代码脚本

    FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    FloatBuffer mNormalBuffer;//顶点法向量数据缓冲
    int vCount=0;

    Context mContext;

    public LoadedObjectVertexNormalAverage(Context context, float[] vertices, float[] normals)
    {
        //初始化顶点坐标与着色数据
        initVertexData(vertices,normals);
        mContext = context;
        //初始化shader
    }

    //初始化顶点坐标与着色数据的方法
    public void initVertexData(float[] vertices,float[] normals)
    {
        //顶点坐标数据的初始化================begin============================
        vCount=vertices.length/3;

        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点坐标数据的初始化================end============================

        //顶点法向量数据的初始化================begin============================
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length*4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mNormalBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mNormalBuffer.put(normals);//向缓冲区中放入顶点法向量数据
        mNormalBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点着色数据的初始化================end============================
    }

    public void drawSelf(int isShadow)
    {
        //将顶点位置数据传入渲染管线
        GLES20.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3*4,
                        mVertexBuffer
                );
        //将顶点法向量数据传入渲染管线
        GLES20.glVertexAttribPointer
                (
                        maNormalHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3*4,
                        mNormalBuffer
                );
        //启用顶点位置、法向量数据
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maNormalHandle);
        //绘制加载的物体
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }

    public void render(int positionAttribute, int normalAttribute, int colorAttribute, boolean onlyPosition) {

        // Pass in the position information
        mVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(positionAttribute, 3, GLES20.GL_FLOAT, false,
                0, mVertexBuffer);

        GLES20.glEnableVertexAttribArray(positionAttribute);


        if (!onlyPosition)
        {
            // Pass in the normal information
            mNormalBuffer.position(0);
            GLES20.glVertexAttribPointer(normalAttribute, 3, GLES20.GL_FLOAT, false,
                    0, mNormalBuffer);

            GLES20.glEnableVertexAttribArray(normalAttribute);

            // Pass in the color information
//            cubeColor.position(0);
//            GLES20.glVertexAttribPointer(colorAttribute, 4, GLES20.GL_FLOAT, false,
//                    0, cubeColor);
//
//            GLES20.glEnableVertexAttribArray(colorAttribute);
        }

        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }
}
