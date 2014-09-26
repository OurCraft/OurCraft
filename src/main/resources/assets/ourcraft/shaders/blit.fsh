#version 120

uniform sampler2D diffuse;

varying vec2 texCoord0;
varying vec3 baseColor;

void main()
{
	vec4 finalColor = texture2D(diffuse, texCoord0);
	finalColor = vec4(finalColor.rgb, finalColor.a);
	gl_FragColor = finalColor * vec4(baseColor,1);
}