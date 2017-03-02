package demo.myopengldemo.Sample6_1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.vecmath.Vector3f;

import demo.myopengldemo.R;

import static demo.myopengldemo.Sample6_1.Constant.MAX_SUB_STEPS;
import static demo.myopengldemo.Sample6_1.Constant.TIME_STEP;

class MySurfaceView extends GLSurfaceView
{
	private SceneRenderer mRenderer;//������Ⱦ��	
	DiscreteDynamicsWorld dynamicsWorld;//�������
	ArrayList<TexCube> tca=new ArrayList<TexCube>();
	ArrayList<TexCube> tcaForAdd=new ArrayList<TexCube>();
	CollisionShape boxShape;//���õ�������
	CollisionShape planeShape;//���õ�ƽ����״
	Sample6_1_Activity activity;
	
	public MySurfaceView(Context context)
	{
        super(context);
        this.activity=(Sample6_1_Activity) context;
        this.setEGLContextClientVersion(2);
        //��ʼ����������
        initWorld();        
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ
    }
	
	//��ʼ����������ķ���
	public void initWorld()
	{
		//������ײ���������Ϣ����
		CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();		
		//������ײ����㷨�����߶����书��Ϊɨ�����е���ײ���ԣ���ȷ�����õļ����Զ�Ӧ���㷨
		CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);		
		//����������������ı߽���Ϣ
		Vector3f worldAabbMin = new Vector3f(-1, -1, -1);
		Vector3f worldAabbMax = new Vector3f(1, 1, 1);
		int maxProxies = 1024;
		//������ײ���ֲ�׶εļ����㷨����
		AxisSweep3 overlappingPairCache =new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
		//�����ƶ�Լ������߶���
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
		//���������������
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver,collisionConfiguration);
		//�����������ٶ�
		dynamicsWorld.setGravity(new Vector3f(0, -10, 0));
		//�������õ�������
		boxShape=new BoxShape(new Vector3f(Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE));
		//�������õ�ƽ����״
		planeShape=new StaticPlaneShape(new Vector3f(0, 1, 0), 0);
	}

	private class SceneRenderer implements GLSurfaceView.Renderer
    {
		int[] cubeTextureId=new int[2];//����������
		int floorTextureId;//��������
		TexFloor floor;//�������1		
		
        public void onDrawFrame(GL10 gl) {
        	//�����ɫ��������Ȼ���
        	GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            //��������
            synchronized(tca)
			{
	            for(TexCube tc:tca)
	            {
	            	MatrixState.pushMatrix();
	                tc.drawSelf(cubeTextureId); 
	                MatrixState.popMatrix();         
	            }            
			}
            
            //���Ƶذ�
            MatrixState.pushMatrix();
            floor.drawSelf( floorTextureId);
            MatrixState.popMatrix();         
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES20.glViewport(0, 0, width, height);
            //����͸��ͶӰ�ı���
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
            
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫ��ɫRGBA
            GLES20.glClearColor(0,0,0,0);
            //������Ȳ���
            GLES20.glEnable(GL10.GL_DEPTH_TEST);
            //����Ϊ�򿪱������
            GLES20.glEnable(GL10.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();
            MatrixState.setCamera( 
            		3,   //����λ�õ�X
            		30, 	//����λ�õ�Y
            		3,   //����λ�õ�Z
            		1, 	//�����򿴵ĵ�X
            		1,   //�����򿴵ĵ�Y
            		-3,   //�����򿴵ĵ�Z
            		0,
            		1,
            		0);
            //��ʼ�����õ���shader����
            ShaderManager.loadCodeFromFile(activity.getResources());
            ShaderManager.compileShader();
            //��ʼ������
            cubeTextureId[0]=initTexture(R.drawable.wood_bin2);
            cubeTextureId[1]=initTexture(R.drawable.wood_bin1);
            floorTextureId=initTextureRepeat(R.drawable.f6);            
            
            //�����������
            floor=new TexFloor(ShaderManager.getTextureShaderProgram(),80*Constant.UNIT_SIZE,-Constant.UNIT_SIZE,planeShape,dynamicsWorld);
           
            //����������       
            int size=2;   //������ߴ�
            float xStart=(-size/2.0f+0.5f)*(2+0.4f)*Constant.UNIT_SIZE;//x������ʼֵ
            float yStart=0.02f;//y������ʼֵ
            float zStart=(-size/2.0f+0.5f)*(2+0.4f)*Constant.UNIT_SIZE-4f;//z������ʼֵ
            for(int i=0;i<size;i++)
            {
            	for(int j=0;j<size;j++)
            	{
            		for(int k=0;k<size;k++)
            		{
            			TexCube tcTemp=new TexCube       //��������������
            			(
            					MySurfaceView.this,		//MySurfaceView������
                				Constant.UNIT_SIZE,		//�ߴ�
                				boxShape,				//��ײ��״
                				dynamicsWorld,			//��������
                				1,						//��������		
                				xStart+i*(2+0.4f)*Constant.UNIT_SIZE,//��ʼx����
                				yStart+j*(2.02f)*Constant.UNIT_SIZE, //��ʼy����        
                				zStart+k*(2+0.4f)*Constant.UNIT_SIZE,//��ʼz����
                				ShaderManager.getTextureShaderProgram()//��ɫ����������
                		);            			
            			tca.add(tcTemp);
            			//ʹ��������һ��ʼ�ǲ������
            			tcTemp.body.forceActivationState(RigidBody.WANTS_DEACTIVATION);
            		}
            	}
            }
            
            new Thread()
            {
            	public void run()
            	{
            		while(true)
            		{
            			try
            			{
            				synchronized(tcaForAdd)//�������������ڼ���
            	            {
            					synchronized(tca)//������ǰ���ӵļ���
            					{
            						for(TexCube tc:tcaForAdd)
                	                {
                	            		tca.add(tc);  //�����Ӽ������������
                	                }
            					}
            	            	tcaForAdd.clear();		//�������ӵļ������
            	            }
            				//��ʼģ��
                			dynamicsWorld.stepSimulation(TIME_STEP, MAX_SUB_STEPS);
							Thread.sleep(20);	//��ǰ�߳�˯��20����
						} catch (Exception e)
						{
							e.printStackTrace();
						}
            		}
            	}
            }.start();					//�����߳�
        }
    }
	
	//�����¼��ص�����
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        switch (e.getAction()) 
        {
           case MotionEvent.ACTION_DOWN:			//������Ļ�����µ��¼�
        	TexCube tcTemp=new TexCube				//����һ������������
   			(
   					this,							//MySurfaceView������
       				Constant.UNIT_SIZE,				//�ߴ�
       				boxShape,						//��ײ��״
       				dynamicsWorld,					//��������
       				1,								//��������
       				0,								//��ʼx����
       				2,         						//��ʼy���� 
       				4,								//��ʼz����
       				ShaderManager.getTextureShaderProgram()//��ɫ����������
       		);        
        	//�������ӵĳ�ʼ�ٶ�
        	tcTemp.body.setLinearVelocity(new Vector3f(0,2,-12));//����ֱ���˶����ٶ�--Vx,Vy,Vz��������
        	tcTemp.body.setAngularVelocity(new Vector3f(0,0,0)); //����������ת���ٶ�--�����������x,y,x������ת���ٶ�
        	//������������뵽�б���
        	synchronized(tcaForAdd)//��������
            {
        	   tcaForAdd.add(tcTemp);//�������
            }
           break;
        }
        return true;
    }   
	public int initTexture(int drawableId)//textureId
	{
		//��������ID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //����������id������
				textures,   //����id������
				0           //ƫ����
		);    
		int textureId=textures[0];    
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        
        //ͨ������������ͼƬ===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        //ͨ������������ͼƬ===============end=====================  
        
        //ʵ�ʼ�������
        GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
        		0, 					  //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        		bitmapTmp, 			  //����ͼ��
        		0					  //����߿�ߴ�
        );
        bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ
        
        return textureId;
	}
	public int initTextureRepeat(int drawableId)//textureId
	{
		//��������ID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //����������id������
				textures,   //����id������
				0           //ƫ����
		);    
		int textureId=textures[0];    
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        
        //ͨ������������ͼƬ===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        //ͨ������������ͼƬ===============end=====================  
        
        //ʵ�ʼ�������
        GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
        		0, 					  //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        		bitmapTmp, 			  //����ͼ��
        		0					  //����߿�ߴ�
        );
        bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ
        
        return textureId;
	}
}
