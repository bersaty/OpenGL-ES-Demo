#ifdef GL_ES
precision highp float;
#endif
varying vec4 v_Color;
uniform float u_GlobalTime;

vec2 iResolution = vec2(1000.0,1000.0);
vec4 color = vec4(0.1, 0.3, 0.5,1.0); // 蓝色

	vec4 kBlack = vec4(0,0,0,1.0);
	vec4 kWhite = vec4(1,1,1,1.0);

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
 	vec2 pos = unipos*1.5-1.0;//正方形
//    vec2 pos = vec2(unipos.x*2.0,(unipos.y)/10.0);//竖值线条
//    vec2 pos = vec2(unipos.x/10.0,(unipos.y)*2.0);//横线条
//    vec2 pos = vec2((unipos.x*1.5),(unipos.y*1.5));
	pos.x *= aspect;

	float sint = sin(u_GlobalTime);
	float usint = sint*0.5+0.5;

	float size = 4.0;
	vec2 fa = floor(pos*size);
	float ra = rand(fa);
	vec2 fb = floor((pos+fa)*size);
	float rb = rand(fb);

	float shade = mix(ra,rb, usint);//ra*(1-usint)+rb*usint

  	fragColor = vec4(shade*color.x, shade*color.y, color.z, 1.0);
}


vec2 rotate(vec2 point, float rads) {
	float cs = cos(rads);
	float sn = sin(rads);
	return point * mat2(cs, -sn, sn, cs);
}
//三角形
void trangle(out vec4 fragColor,in vec4 fragCoord){
    float rads = radians(90.0);

	vec2 pos=fragCoord.xy;
	pos = rotate(pos, rads);

	float size2 = 500.0;
	float size1 = size2 * 0.5;

	vec2 p0 = rotate(pos, radians(0.0));
	vec2 p1 = rotate(pos, radians(120.0));
	vec2 p2 = rotate(pos, radians(240.0));
	p0 = mod(p0, size2);
	p1 = mod(p1, size2);
	p2 = mod(p2, size2);

	int i=1;
	if(p0.x > size1) { i*=-1; }
	if(p1.x > size1) { i*=-1; }
	if(p2.x > size1) { i*=-1; }

	float a = 1.0;
	if(i < 0) a = 0.0;

	float sint = sin(u_GlobalTime);
	float usint = sint*0.5+0.5;
		float size = 4.0;
    	vec2 fa = floor(p2*size);
    	float ra = rand(fa);
    	vec2 fb = floor((p2+fa)*size);
    	float rb = rand(fb);

    	float shade = mix(ra,rb, usint);

    		vec4 result = mix(color, kWhite, a);

	fragColor=vec4(result.r,result.g,result.b,1.0);
}

float circle(vec2 p , vec2 o ){
	p-=o;
	return length(p);

}

void mainImage(out vec4 fragColor, in vec4 fragCoord)
{
    float degree = 90.0;
	vec2 p = fragCoord.xy / iResolution.y;
	p.x-= iResolution.x / iResolution.y*0.5;
	p.y-=0.5;

	float v = 1.0;

		float sint = sin(u_GlobalTime / 20.0);

	float c = circle(p*sint*0.5  , vec2(cos(degree*v)/5.0, sin(degree*v)/5.0));
	c*= circle(p , vec2(cos(degree*v/3.)/8.,sin(degree*v/3.)/8.));

	fragColor = vec4( (1.0-vec3( c*50. )*vec3(0.5,0.2,0.8))*50.0, 1.0 );
}

void main(void)
{
//    retangle(gl_FragColor);
//    trangle(gl_FragColor,gl_FragCoord);

mainImage(gl_FragColor,gl_FragCoord);
}