precision mediump float;

varying vec4 v_Color;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;

uniform float u_GlobalTime;

//void mainImage( out vec4 fragColor, in vec2 fragCoord )
//{
//	float stongth = 0.3;
//	vec2 uv = fragCoord.xy;
//	float waveu = sin((uv.y + u_GlobalTime) * 20.0) * 0.5 * 0.03 * stongth;
//	fragColor = texture2D(u_Texture, uv + vec2(waveu, 0));
//}

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    vec2 uv = fragCoord.xy;

	vec3 tex = texture2D( u_Texture, uv ).rgb;
	float shade = dot(tex, vec3(0.333333));

	vec3 col = mix(vec3(0.1, 0.36, 0.8) * (1.0-2.0*abs(shade-0.5)), vec3(1.06, 0.8, 0.55), 1.0-shade);

    fragColor = vec4(col,1.0);
}

void main() {
	mainImage(gl_FragColor, v_TexCoordinate);
//    gl_FragColor =texture2D(u_Texture, v_TexCoordinate);
}