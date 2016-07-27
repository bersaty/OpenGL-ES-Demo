#####OpenGL ES
OpenGL ES (OpenGL for Embedded Systems) 是 OpenGL三维图形 API 的子集，针对手机、PDA和游戏主机等嵌入式设备而设计。该API由Khronos集团定义推广，Khronos是一个图形软硬件行业协会，该协会主要关注图形和多媒体方面的开放标准。
OpenGL ES 是从 OpenGL 裁剪的定制而来的，去除了glBegin/glEnd，四边形（GL_QUADS）、多边形（GL_POLYGONS）等复杂图元等许多非绝对必要的特性。经过多年发展，现在主要有两个版本，OpenGL ES 1.x 针对固定管线硬件的，OpenGL ES 2.x 针对可编程管线硬件。OpenGL ES 1.0 是以 OpenGL 1.3 规范为基础的，OpenGL ES 1.1 是以 OpenGL 1.5 规范为基础的，它们分别又支持 common 和 common lite两种profile。lite profile只支持定点实数，而common profile既支持定点数又支持浮点数。 OpenGL ES 2.0 则是参照 OpenGL 2.0 规范定义的，common profile发布于2005-8，引入了对可编程管线的支持。

#####相关概念

矩阵变换：http://www.songho.ca/opengl/gl_transform.html

#### 坐标系


##### OpenGL Shader Language （GLSL）语言
OpenGL着色语言（OpenGL Shading Language）是用来在OpenGL中着色编程的语言，也即开发人员写的短小的自定义程序，他们是在图形卡的GPU （Graphic Processor Unit图形处理单元）上执行的，代替了固定的渲染管线的一部分，使渲染管线中不同层次具有可编程型。比如：视图转换、投影转换等。GLSL（GL Shading Language）的着色器代码分成2个部分：Vertex Shader（顶点着色器）和Fragment（片断着色器），有时还会有Geometry Shader（几何着色器）。负责运行顶点着色的是顶点着色器。它可以得到当前OpenGL 中的状态，GLSL内置变量进行传递。GLSL其使用C语言作为基础高阶着色语言，避免了使用汇编语言或硬件规格语言的复杂性。

http://my.oschina.net/sweetdark/blog/208024

#####着色器 shader

#####相关资料：
英文比较完整的教程：
http://www.learnopengles.com/

透视变换
http://tangzm.com/blog/?p=202
投影和相机视口：
http://blog.csdn.net/creativemobile/article/details/9236709
学习教程：
http://blog.piasy.com/2016/06/07/Open-gl-es-android-2-part-1/
http://so.csdn.net/so/search/s.do?ref=toolbar&q=opengl+es&ref=toolbar&q=opengl+es
API参数：
https://www.opengl.org/sdk/docs/man2/xhtml/
英文学习网址：
http://www.songho.ca/opengl/gl_projectionmatrix.html
http://www.songho.ca/opengl/gl_transform.html
https://www.khronos.org/opengles/sdk/docs/reference_cards/OpenGL-ES-2_0-Reference-card.pdf
透视投影&正交投影
http://blog.csdn.net/shulianghan/article/details/46680803
坐标转换
http://blog.csdn.net/zhongjling/article/details/8488844
OpenGL ES 2.0 规范
http://www.opengpu.org/forum.php?mod=viewthread&tid=2164
渲染线管：
http://www.w2bc.com/Article/50173
http://codingnow.cn/opengles/1504.html
着色器 shader：
http://www.ituring.com.cn/article/208521
http://www.tuicool.com/articles/VZVJra
GLSL 语言
https://www.opengl.org/registry/doc/GLSLangSpec.Full.1.20.8.pdf
http://my.oschina.net/sweetdark/blog/208024
http://blog.csdn.net/renai2008/article/details/7844495
MVP矩阵
https://4gamers.cn/blog/2014-06-15-opengl-es2-meet-mvp.html


