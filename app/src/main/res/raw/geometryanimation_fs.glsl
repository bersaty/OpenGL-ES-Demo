precision mediump float;

varying vec4 v_Color;
uniform float u_GlobalTime;

uniform vec3 iResolution;

vec2 tile_num = vec2(5.0,5.0);

// 一元随机函数
float rand(float x)
{
   return fract(sin(x) * 4358.5453123);
}
// 二元随机函数
float rand(vec2 co)
{
   return fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 43758.5357);
}

void main() {
    vec4 result = vec4(v_Color.x,v_Color.y,v_Color.z,1.0);

    gl_FragColor =result * u_GlobalTime; //渲染颜色图
}