#version 120

uniform sampler2D diffuse;

varying vec2 texCoord0;
varying vec4 baseColor;

void main()
{
	gl_FragColor = texture2D(diffuse, texCoord0) * baseColor;
}