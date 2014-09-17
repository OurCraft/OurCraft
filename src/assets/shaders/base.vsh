#version 120

attribute vec3 pos;
attribute vec2 texCoords;

varying vec2 texCoord0;

uniform mat4 modelview;
uniform mat4 projection;

void main()
{
	texCoord0 = texCoords;
	gl_Position = projection * modelview * vec4(pos,1);
}