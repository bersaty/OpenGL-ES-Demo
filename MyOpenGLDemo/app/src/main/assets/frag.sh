precision mediump float;				//����Ĭ�ϵĸ��㾫��
uniform vec4 aColor;
varying vec4 ambient;  				//�Ӷ�����ɫ�����ݹ����Ļ���������ǿ��
varying vec4 diffuse;					//�Ӷ�����ɫ�����ݹ�����ɢ�������ǿ��
varying vec4 specular;				//�Ӷ�����ɫ�����ݹ����ľ��������ǿ��
void main() {
	vec4 finalColor=aColor;		//���屾�����ɫ
	//�ۺ�����ͨ���������ǿ�ȼ�ƬԪ����ɫ���������ƬԪ����ɫ�����ݸ�����
	gl_FragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
}     
