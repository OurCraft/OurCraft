#version 120

uniform sampler2D diffuse;

varying vec2 texCoord0;
varying vec4 baseColor;

void main()
{
	vec4 sampleColor = texture2D(diffuse, texCoord0) * baseColor;
	gl_FragColor = vec4(1.0-sampleColor.rgb, sampleColor.w);
}