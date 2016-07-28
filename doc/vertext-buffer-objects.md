####Vertex Buffer Objects (VBOS) 顶点缓冲区对象
目前我们绘制所需要的数据都是保存在客户端里，当需要渲染的时候才传到GPU里。
OpenGL ES是一个底层库，数据存储对应native堆，而Android应用是跑在dalvik虚拟机里面的，数据对应dalvik堆，因此我们需要分配一个缓冲区以便OpenGL可以访问。

```java
// Java array.
float[] cubePositions;
...
// Floating-point buffer
final FloatBuffer cubePositionsBuffer;
...
 
// Allocate a direct block of memory on the native heap,
// size in bytes is equal to cubePositions.length * BYTES_PER_FLOAT.
// BYTES_PER_FLOAT is equal to 4, since a float is 32-bits, or 4 bytes.
cubePositionsBuffer = ByteBuffer.allocateDirect(cubePositions.length * BYTES_PER_FLOAT)
 
// Floats can be in big-endian or little-endian order.
// We want the same as the native platform.
.order(ByteOrder.nativeOrder())
 
// Give us a floating-point view on this byte buffer.
.asFloatBuffer();
Transferring data from the Java heap to the native heap is then a matter of a couple calls:

// Copy data from the Java heap to the native heap.
cubePositionsBuffer.put(cubePositions)
 
// Reset the buffer position to the beginning of the buffer.
.position(0);
```

设置缓冲区的位置相当于改变内存的指针位置，但是java没有对应的方法来改变这个指针，只能让OpenGL 来处理。
一旦数据存储到native堆里面，java对应的数据已经不需要了，可以让垃圾回收机制处理掉。

```java
// Pass in the position information
GLES20.glEnableVertexAttribArray(mPositionHandle);
GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE,
GLES20.GL_FLOAT, false, 0, mCubePositions);
```
| 参数 | 解释 |
|--------|--------|
| mPositionHandle | The OpenGL index of the position attribute of our shader program. |
|POSITION_DATA_SIZE|How many elements (floats) define this attribute.|
|GL_FLOAT|The type of each element.|
|false|Should fixed-point data be normalized? Not applicable since we are using floating-point data.|
|0|The stride. Set to 0 to mean that the positions should be read sequentially.|
|mCubePositions|The pointer to our buffer, containing all of the positional data.|

数据缓冲区可以分开写，也可以一起写：
```
positions = X,Y,Z, X, Y, Z, X, Y, Z, …
colors = R, G, B, A, R, G, B, A, …
textureCoordinates = S, T, S, T, S, T, …
```
```
buffer = X, Y, Z, R, G, B, A, S, T, …
```

如果把信息都写在一起渲染速度会更快，因为都在一块i内存里面，但是更新会比较困难，如果我们的数据变化大，非常难维护。我们需要告诉OpenGL顶点需要多少字节，每一个顶点的数据的偏移量。

偏移量计算公式，传递给glVertexAttribPointer的偏移量的单位是字节。
```
final int stride = (POSITION_DATA_SIZE + NORMAL_DATA_SIZE + TEXTURE_COORDINATE_DATA_SIZE) * BYTES_PER_FLOAT;
```
```
// Pass in the position information
mCubeBuffer.position(0);
GLES20.glEnableVertexAttribArray(mPositionHandle);
GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE,
GLES20.GL_FLOAT, false, stride, mCubeBuffer);
 
// Pass in the normal information
mCubeBuffer.position(POSITION_DATA_SIZE);
GLES20.glEnableVertexAttribArray(mNormalHandle);
GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE,
GLES20.GL_FLOAT, false, stride, mCubeBuffer);
...
```
OpenGL通过偏移量计算下一个顶点的数据。


如果大量使用分配native堆的内存和释放，迟早会出现OOM的情况。

native堆释放后不能完全释放，需要一些而外的GC过程才可以完全释放。Dalvik发现内存不足或者native的内存没有释放，就会跑异常。
native的内存块会变得很破碎，导致有时候内存明显够用的，但是调用allocateDirect()却无法申请内存块。这个时候可以申请比较小的内存块，再释放，然后再申请比较大的。
