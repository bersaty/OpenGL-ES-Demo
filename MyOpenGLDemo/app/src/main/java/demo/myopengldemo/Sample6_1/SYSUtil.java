package demo.myopengldemo.Sample6_1;

import javax.vecmath.Quat4f;

public class SYSUtil
{
	//����Ԫ��ת��Ϊ�Ƕȼ�ת������
	public static float[] fromSYStoAXYZ(Quat4f q4)
	{	
		double sitaHalf= Math.acos(q4.w);
		float nx=(float) (q4.x/ Math.sin(sitaHalf));
		float ny=(float) (q4.y/ Math.sin(sitaHalf));
		float nz=(float) (q4.z/ Math.sin(sitaHalf));
		
		return new float[]{(float) Math.toDegrees(sitaHalf*2),nx,ny,nz};
	}
}
