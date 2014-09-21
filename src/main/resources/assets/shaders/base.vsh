#version 120

attribute vec3 pos;
attribute vec2 texCoords;
attribute vec3 vertexColor;

varying vec2 texCoord0;
varying vec3 baseColor;

uniform mat4 modelview;
uniform mat4 projection;

void main()
{
	texCoord0 = texCoords;
	baseColor = vertexColor;
	gl_Position = projection * modelview * vec4(pos,1);
}