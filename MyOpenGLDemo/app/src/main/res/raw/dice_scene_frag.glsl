// Based on http://blog.shayanjaved.com/2011/03/13/shaders-android/
// from Shayan Javed
// And dEngine source from Fabien Sanglard

precision mediump float;

// The position of the light in eye space.
uniform vec3 uLightPos;

uniform vec3 uCamera;
  
// Texture variables: depth texture
uniform sampler2D uShadowTexture;

// This define the value to move one pixel left or right
uniform float uxPixelOffset;
// This define the value to move one pixel up or down
uniform float uyPixelOffset;		
  
// from vertex shader - values get interpolated
varying vec3 vPosition;
varying vec3 vNormal;
  
// shadow coordinates
varying vec4 vShadowCoord;
  
//Calculate variable bias - from http://www.opengl-tutorial.org/intermediate-tutorials/tutorial-16-shadow-mapping
float calcBias()
{
	float bias;

	vec3 n = normalize( vNormal );
	// Direction of the light (from the fragment to the light)
	vec3 l = normalize( uLightPos );
	
	// Cosine of the angle between the normal and the light direction, 
	// clamped above 0
	//  - light is at the vertical of the triangle -> 1
	//  - light is perpendiular to the triangle -> 0
	//  - light is behind the triangle -> 0
	float cosTheta = clamp( dot( n,l ), 0.0, 1.0 );
 		
 	bias = 0.0001*tan(acos(cosTheta));
	bias = clamp(bias, 0.0, 0.01);
 	
 	return bias;
}

float lookup( vec2 offSet) 
{ 
	vec4 shadowMapPosition = vShadowCoord / vShadowCoord.w;

	float distanceFromLight = texture2D(uShadowTexture, (shadowMapPosition + 
	                               vec4(offSet.x * uxPixelOffset, offSet.y * uyPixelOffset, 0.05, 0.0)).st ).z;
			
	//add bias to reduce shadow acne (error margin)
	float bias = calcBias();

	return float(distanceFromLight > shadowMapPosition.z - bias);
}

float shadowPCF()
{
	float shadow = 1.0;

	for (float y = -1.5; y <= 1.5; y = y + 1.0) {
		for (float x = -1.5; x <= 1.5; x = x + 1.0) {
			shadow += lookup(vec2(x,y));
		}
	}
		
	shadow /= 16.0;
	shadow += 0.1;
	
	return shadow;
}
  
void main()                    		
{        
	vec3 lightVec = uLightPos - vPosition;
	lightVec = normalize(lightVec);
   	
   	// Phong shading with diffuse and ambient component
	float diffuseComponent = max(0.0,dot(lightVec, vNormal) );
	float ambientComponent = 0.3;//环境光强度
	float lightSpecular = 0.5;//镜面光强度

//      vec3 newNormal=normalize(vNormal); 	//对法向量规格化
//      //计算从表面点到摄像机的向量
//      vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);
//      //计算从表面点到光源位置的向量vp
//      vec3 vp= normalize(uLightPos-vPosition);
//      vp=normalize(vp);//格式化vp
//      vec3 halfVector=normalize(vp+eye);	//求视线与光线的半向量
//      float shininess=50.0;				//粗糙度，越小越光滑
//      float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//求法向量与vp的点积与0的最大值
//      float nDotViewHalfVector=dot(newNormal,halfVector);	//法线与半向量的点积
//      float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//镜面反射光强度因子
//      float specular=lightSpecular*powerFactor;

 		
 	// Shadow
   	float shadow = 1.0;
	
	//if the fragment is not behind light view frustum
	if (vShadowCoord.w > 0.0) {
			
		shadow = shadowPCF();
		
		//scale 0.0-1.0 to 0.2-1.0
		//otherways everything in shadow would be black
		shadow = (shadow * 0.9) + 0.2;
	}

	// Final output color with shadow and lighting
	vec4 color = vec4(0.0,0.0,1.0,1.0);
    gl_FragColor = (color * (diffuseComponent + ambientComponent) * shadow);
}                                                                     	
