package demo.myopengldemo.shadow;

import java.util.Set;


//��ʾ���������࣬�����һ�������ʾһ��������
public class Normal 
{
   public static final float DIFF=0.0000001f;//�ж������������Ƿ���ͬ����ֵ
   //��������XYZ���ϵķ���
   float nx;
   float ny;
   float nz;
   
   public Normal(float nx, float ny, float nz)
   {
	   this.nx=nx;
	   this.ny=ny;
	   this.nz=nz;
   }
   
   @Override
   public boolean equals(Object o)
   {
	   if(o instanceof Normal)
	   {//������������XYZ���������ĲС��ָ������ֵ����Ϊ���������������
		   Normal tn=(Normal)o;
		   if(Math.abs(nx-tn.nx)<DIFF&&
			  Math.abs(ny-tn.ny)<DIFF&&
			  Math.abs(ny-tn.ny)<DIFF
             )
		   {
			   return true;
		   }
		   else
		   {
			   return false;
		   }
	   }
	   else
	   {
		   return false;
	   }
   }
   
   //����Ҫ�õ�HashSet�����һ��Ҫ��дhashCode����
   @Override
   public int hashCode()
   {
	   return 1;
   }
   
   //������ƽ��ֵ�Ĺ��߷���
   public static float[] getAverage(Set<Normal> sn)
   {
	   //��ŷ������͵�����
	   float[] result=new float[3];
	   //�Ѽ��������еķ��������
	   for(Normal n:sn)
	   {
		   result[0]+=n.nx;
		   result[1]+=n.ny;
		   result[2]+=n.nz;
	   }	   
	   //����ͺ�ķ��������
	   return LoadUtil.vectorNormal(result);
   }
}
