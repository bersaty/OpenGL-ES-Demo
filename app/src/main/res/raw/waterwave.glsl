
varying vec4 v_Color;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;

precision mediump float;
uniform float u_GlobalTime;
const float PI = 3.1415926535897932;

const float speed = 0.2;  
const float speed_x = 0.3;  
const float speed_y = 0.3;  
 
const float intensity = 3.0;  
const int steps = 8;  
const float frequency = 4.0;  
const int angle = 7; 
 
const float delta = 20.0;  
const float intence = 400.0;  
const float emboss = 0.3;
 
float col(vec2 coord)  
{  
    float delta_theta = 2.0 * PI / float(angle);  
    float col = 0.0;  
    float theta = 0.0;  
    for (int i = 0; i < steps; i++)  
    {
        vec2 adjc = coord;
    theta = delta_theta * float(i);
    adjc.x += cos(theta)*u_GlobalTime*speed + u_GlobalTime * speed_x;
        adjc.y -= sin(theta)*u_GlobalTime*speed - u_GlobalTime * speed_y;
        col = col + cos((adjc.x*cos(theta) - adjc.y*sin(theta))  
            *frequency)*intensity;  
    }
    return cos(col);
}  

void main(void)  
{  
    vec2 p = (gl_FragCoord.xy) / v_TexCoordinate.xy, c1 = p, c2 = p;
    float cc1 = col(c1);  
 
    c2.x += v_TexCoordinate.x/delta;
    float dx = emboss*(cc1-col(c2))/delta;  
 
    c2.x = p.x;  
    c2.y += v_TexCoordinate.y/delta;
    float dy = emboss*(cc1-col(c2))/delta;  
 
    c1.x += dx;  
    c1.y += dy;  
    float alpha = 1.+dot(dx,dy)*intence;  
    gl_FragColor = texture2D(u_Texture,c1)*(alpha) *v_Color*(alpha);
}