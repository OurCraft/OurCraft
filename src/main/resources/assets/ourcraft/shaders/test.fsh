#version 120

uniform sampler2D diffuse;

varying vec2 texCoord0;
varying vec4 baseColor;

void main()
{
	float modValue = mod(texCoord0, 1.0/1.0); 
	vec4 sampleColor = texture2D(diffuse, texCoord0) * baseColor;
	gl_FragColor = vec4(sampleColor.rgb*modValue, sampleColor.w);
}