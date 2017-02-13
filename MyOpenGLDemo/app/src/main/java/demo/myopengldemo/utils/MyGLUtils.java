/*
 * Copyright 2016 nekocode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package demo.myopengldemo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * opengl 工具：
 * 1、加载纹理texture
 * 2、编译链接shader，返回对应的program句柄（int）
 * 3、从raw目录下读取glsl代码
 */
public class MyGLUtils {
    private static final String TAG = "MyGLUtils";

    public static int genTexture() {
        return genTexture(GLES20.GL_TEXTURE_2D);
    }

    public static int genTexture(int textureType) {
        int[] genBuf = new int[1];
        GLES20.glGenTextures(1, genBuf, 0);
        GLES20.glBindTexture(textureType, genBuf[0]);

        // Set texture default draw parameters
        if (textureType == GLES11Ext.GL_TEXTURE_EXTERNAL_OES) {
            GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        } else {
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
        }

        return genBuf[0];
    }

    /**
     * 加载纹理
     */
    public static int loadTexture(final Context context, final int resourceId, int[] size) {
        final int texId = genTexture();

        if (texId != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling
            options.inJustDecodeBounds = true;

            // Just decode bounds
            BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Set return size
            size[0] = options.outWidth;
            size[1] = options.outHeight;

            // Decode
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        return texId;
    }

    /**
     * 读取raw目录下的shader文件编译链接生成program,绑定对应的attributes
     */
    public static int buildProgram(Context context, int vertexSourceRawId, int fragmentSourceRawId,String[] attributes) {
        return buildProgram(getStringFromRaw(context, vertexSourceRawId),
                getStringFromRaw(context, fragmentSourceRawId),attributes);
    }

    /**
     * 编译fragmentshader和vertexshader，链接生成program
     */
    public static int buildProgram(String vertexSource, String fragmentSource, String[] attributes) {
        final int vertexShader = buildShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }

        final int fragmentShader = buildShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragmentShader == 0) {
            return 0;
        }

        final int program = GLES20.glCreateProgram();
        if (program == 0) {
            return 0;
        }

        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);

        if (attributes != null) {
            final int size = attributes.length;
            for (int i = 0; i < size; i++) {
                GLES20.glBindAttribLocation(program, i, attributes[i]);
            }
        }
        GLES20.glLinkProgram(program);

        return program;
    }

    /**
     * 编译shader代码
     */
    public static int buildShader(int type, String shaderSource) {
        final int shader = GLES20.glCreateShader(type);
        if (shader == 0) {
            return 0;
        }

        GLES20.glShaderSource(shader, shaderSource);
        GLES20.glCompileShader(shader);

        int[] status = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] == 0) {
            Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            return 0;
        }

        return shader;
    }

    /**
     * 从raw目录下读取glsl 代码，resourceID为（R.raw.xxx）
     */
    private static String getStringFromRaw(Context context, int resourceID) {
        String str;
        try {
            Resources r = context.getResources();
            InputStream is = r.openRawResource(resourceID);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                baos.write(i);
                i = is.read();
            }

            str = baos.toString();
            is.close();
        } catch (IOException e) {
            str = "";
        }

        return str;
    }

    /**
     * 通过inputStream转换乘BufferReader
     * @param inputStream
     * @return
     */
    public static BufferedReader getBufferedReaderFrom(InputStream inputStream) {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        InputStreamReader inputStreamReader = new InputStreamReader(dataInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        return bufferedReader;
    }

    /**
     * 加载obj文件至数组，包括顶点坐标、顶点法向量和纹理坐标
     * 返回的数组在使用glVertexAttribPointer函数时注意利用stride参数
     * @return
     */
    public static float[] loadObjFromAssets(Context context,String objFileName) {
        ArrayList<Float> vertexList = new ArrayList<Float>();
        ArrayList<Float> textureList = new ArrayList<Float>();
        ArrayList<Float> normalList = new ArrayList<Float>();
        ArrayList<Float> faceList = new ArrayList<Float>();
        try {
            InputStream is = context.getAssets().open(objFileName);
            BufferedReader br = getBufferedReaderFrom(is);
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] temp = line.split("[ ]+");
                if (temp[0].trim().equals("v")) {
                    vertexList.add(Float.parseFloat(temp[1]));
                    vertexList.add(Float.parseFloat(temp[2]));
                    vertexList.add(Float.parseFloat(temp[3]));
                } else if (temp[0].trim().equals("vn")) {
                    normalList.add(Float.parseFloat(temp[1]));
                    normalList.add(Float.parseFloat(temp[2]));
                    normalList.add(Float.parseFloat(temp[3]));
                } else if (temp[0].trim().equals("vt")) {
                    textureList.add(Float.parseFloat(temp[1]));
                    textureList.add(Float.parseFloat(temp[2]));
                } else if (temp[0].trim().equals("f")) {
                    for (int i = 1; i < temp.length; i++) {
                        String[] temp2 = temp[i].split("/");
                        int indexVetex = Integer.parseInt(temp2[0]) - 1;
                        faceList.add(vertexList.get(3 * indexVetex));
                        faceList.add(vertexList.get(3 * indexVetex + 1));
                        faceList.add(vertexList.get(3 * indexVetex + 2));
                        int indexNormal = Integer.parseInt(temp2[1]) - 1;
                        faceList.add(normalList.get(3 * indexNormal));
                        faceList.add(normalList.get(3 * indexNormal + 1));
                        faceList.add(normalList.get(3 * indexNormal + 2));
                        int indexTexture = Integer.parseInt(temp2[2]) - 1;
                        faceList.add(textureList.get(3 * indexTexture));
                        faceList.add(textureList.get(3 * indexTexture + 1));
                        faceList.add(textureList.get(3 * indexTexture + 2));
                    }
                }
            }
            float [] result = new float[faceList.size()];
            for (int i = 0; i < faceList.size(); i++) {
                result[i] = faceList.get(i);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
