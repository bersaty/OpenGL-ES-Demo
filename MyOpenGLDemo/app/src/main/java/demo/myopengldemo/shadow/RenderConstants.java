package demo.myopengldemo.shadow;

public class RenderConstants {

	/** Identifiers for our uniforms and attributes inside the shaders. */
	public static final String MVP_MATRIX_UNIFORM = "uMVPMatrix";
	public static final String MV_MATRIX_UNIFORM = "uMVMatrix";
	public static final String NORMAL_MATRIX_UNIFORM = "uNormalMatrix";
	public static final String LIGHT_POSITION_UNIFORM = "uLightPos";
	public static final String CAMERA_POSITION_UNIFORM = "uCamera";
	public static final String POSITION_ATTRIBUTE = "aPosition";
	public static final String NORMAL_ATTRIBUTE = "aNormal";
	public static final String COLOR_ATTRIBUTE = "aColor";
	public static final String TEX_COORDINATE = "aTexCoordinate";
	
	public static final String SHADOW_TEXTURE = "uShadowTexture";
	public static final String SHADOW_PROJ_MATRIX = "uShadowProjMatrix";
	public static final String SHADOW_X_PIXEL_OFFSET = "uxPixelOffset";
	public static final String SHADOW_Y_PIXEL_OFFSET = "uyPixelOffset";
	
	public static final String SHADOW_POSITION_ATTRIBUTE = "aShadowPosition";
	
	public static final String TEXTURE_UNIFORM = "uTexture";



	/** Additional constants. */
	public static final int FLOAT_SIZE_IN_BYTES = 4;
	public static final int SHORT_SIZE_IN_BYTES = 2;
}
