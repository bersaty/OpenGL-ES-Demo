### OpenGL ES
OpenGL ES (OpenGL for Embedded Systems) 是 OpenGL三维图形 API 的子集，针对手机、PDA和游戏主机等嵌入式设备而设计。该API由Khronos集团定义推广，Khronos是一个图形软硬件行业协会，该协会主要关注图形和多媒体方面的开放标准。
OpenGL ES 是从 OpenGL 裁剪的定制而来的，去除了glBegin/glEnd，四边形（GL_QUADS）、多边形（GL_POLYGONS）等复杂图元等许多非绝对必要的特性。经过多年发展，现在主要有两个版本，OpenGL ES 1.x 针对固定管线硬件的，OpenGL ES 2.x 针对可编程管线硬件。OpenGL ES 1.0 是以 OpenGL 1.3 规范为基础的，OpenGL ES 1.1 是以 OpenGL 1.5 规范为基础的，它们分别又支持 common 和 common lite两种profile。lite profile只支持定点实数，而common profile既支持定点数又支持浮点数。 OpenGL ES 2.0 则是参照 OpenGL 2.0 规范定义的，common profile发布于2005-8，引入了对可编程管线的支持。最新的OpenGL ES 3.X是兼容2.0版本的。

OpenGL ES 3.0主要新功能有：

1、渲染管线多重增强，实现先进视觉效果的加速，包括遮挡查询(Occlusion Query)、变缓反馈(Transform Feedback)、实例渲染(Instanced Rendering)、四个或更多渲染目标支持。

2、高质量ETC2/EAC纹理压缩格式成为一项标准功能，不同平台上不再需要需要不同的纹理集。

3、新版GLSL ES 3.0着色语言，全面支持整数和32位浮点操作。

4、纹理功能大幅增强，支持浮点纹理、3D纹理、深度纹理、顶点纹理、NPOT纹理、R/RG单双通道纹理、不可变纹理、2D阵列纹理、无二次幂限制纹理、阴影对比、调配(swizzle)、LOD与mip level clamps、无缝立方体贴图、采样对象、纹理MSAA抗锯齿渲染器。

5、一系列广泛的精确尺寸纹理和渲染缓冲格式，便携移动应用更简单。

### 相关概念

#### 1、坐标系以及重要的Matrix

OpenGL 里使用了很多矩阵，为了将坐标从一个坐标系转换到另一个坐标系，我们需要用到几个转换矩阵，最重要的几个分别是模型(Model)、视图(View)、投影(Projection)三个矩阵。

![](matrix_coordinate_systems.png)

局部空间(Local Space):是指对象所在的坐标空间，当创建一个物体时，它的中心点相对于物体自身的坐标可能是(0,0,0)。

世界空间(World Space)：指整个OpenGL 世界的坐标，从局部空间坐标转换到世界空间坐标需要使用模型矩阵(Model Matrix)来转化。模型矩阵是一种转换矩阵，它能通过对对象进行平移、缩放、旋转来将它置于它本应该在的位置或方向。

观察空间(View Space):OpenGL的摄像机(Camera)(有时也称为摄像机空间(Camera Space)或视觉空间(Eye Space))。将对象的世界空间的坐标转换为观察者视野前面的坐标。因此观察空间就是从摄像机的角度观察到的空间。而这通常是由一系列的平移和旋转的组合来平移和旋转场景从而使得特定的对象被转换到摄像机前面。这些组合在一起的转换通常存储在一个观察矩阵(View Matrix)里，用来将世界坐标转换到观察空间。

裁剪空间(Clip Space):在一个顶点着色器运行的最后，OpenGL期望所有的坐标都能落在一个给定的范围内，且任何在这个范围之外的点都应该被裁剪掉(Clipped)。被裁剪掉的坐标就被忽略了，所以剩下的坐标就将变为屏幕上可见的片段。为了将顶点坐标从观察空间转换到裁剪空间，我们需要定义一个投影矩阵(Projection Matrix)，它指定了坐标的范围。

我们为上述的每一个步骤都创建了一个转换矩阵：模型矩阵、观察矩阵和投影矩阵。一个顶点的坐标将会根据以下过程被转换到裁剪坐标：

```
Vclip=Mprojection⋅Mview⋅Mmodel⋅Vlocal
```

注意每个矩阵被运算的顺序是相反的(记住我们需要从右往左乘上每个矩阵)。最后的顶点应该被赋予顶点着色器中的gl_Position且OpenGL将会自动进行透视划分和裁剪。

#### 2、裁剪空间(Clip Space)的设置

![](setlookat.png)

通过这个函数设置相机位置，以及眼睛向上的方向。屏幕中间是一个参考点（0，0，0），用这个参考点来设置眼睛的位置。

```java
Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

```
通过上面设置了眼睛和观察位置之后，就需要根据上面的值设置frustum的相关值。

mProjectionMatrix:用于保存投影矩阵。
0：offset。
left：左边的距离（比例）。
right：右边的距离（比例）。
bottom：下边的距离（比例）。
top：上边的距离（比例）。
near:和近截面的距离。
far：和远截面的距离，far>near。

```java
Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
```

对应的图如下：

![](frustum.png)


### OpenGL Shader Language （GLSL）语言
OpenGL着色语言（OpenGL Shading Language）是用来在OpenGL中着色编程的语言，也即开发人员写的短小的自定义程序，他们是在图形卡的GPU （Graphic Processor Unit图形处理单元）上执行的，代替了固定的渲染管线的一部分，使渲染管线中不同层次具有可编程型。比如：视图转换、投影转换等。GLSL（GL Shading Language）的着色器代码分成2个部分：Vertex Shader（顶点着色器）和Fragment（片断着色器），有时还会有Geometry Shader（几何着色器）。负责运行顶点着色的是顶点着色器。它可以得到当前OpenGL 中的状态，GLSL内置变量进行传递。GLSL其使用C语言作为基础高阶着色语言，避免了使用汇编语言或硬件规格语言的复杂性。

### 着色器 shader
OpenGL 2.0 上我们想要绘制任何东西都需要一个vertexShader和fragment Shader。


