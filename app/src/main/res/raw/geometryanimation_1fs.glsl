#ifdef GL_ES
precision highp float;
#endif

varying vec4 v_Color;
uniform float u_GlobalTime;

vec2 iResolution = vec2(1000.0,1000.0);
vec3 color = vec3(0.1, 0.3, 0.5); // 蓝色

// 一元随机生成函数
float rand(float x)
{
   return fract(sin(x) * 4358.5453123);
}

//二元随机生成函数
float rand(vec2 n)
{
  return fract(sin(dot(n.xy, vec2(12.9898, 78.233)))*43758.5453);
}

//正方形，条形等图形
void retangle(out vec4 fragColor){
	float aspect = iResolution.x / iResolution.y;
	vec2 unipos = ( gl_FragCoord.xy / iResolution );
// 	vec2 pos = unipos*1.5-1.0;//正方形
//    vec2 pos = vec2(unipos.x*2.0,(unipos.y)/10.0);//竖值线条
    vec2 pos = vec2(unipos.x/10.0,(unipos.y)*2.0);//横线条
//    vec2 pos = vec2((unipos.x*1.5),(unipos.y*1.5));
	pos.x *= aspect;

	float sint = sin(u_GlobalTime);
	float usint = sint*0.5+0.5;

	float size = 4.0;
	vec2 fa = floor(pos*size);
	float ra = rand(fa);
	vec2 fb = floor((pos+fa)*size);
	float rb = rand(fb);

	float shade = mix(ra,rb, usint);

  	fragColor = vec4(shade*color.x, shade*color.y, color.z, 1.0);
}

void main(void)
{
    retangle(gl_FragColor);
}