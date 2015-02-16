#version 120

uniform sampler2D diffuse;
uniform vec2 screenSize;

varying vec2 texCoord0;
varying vec4 baseColor;

void main()
{
	float pixelWidth = 1.0/screenSize.x;
	float pixelHeight = 1.0/screenSize.y;
	vec4 tl = texture2D(diffuse, texCoord0 - vec2(pixelWidth, pixelHeight));
	vec4 tr = texture2D(diffuse, texCoord0 + vec2(pixelWidth, pixelHeight));
	vec4 bl = texture2D(diffuse, texCoord0 - vec2(pixelWidth, -pixelHeight));
	vec4 br = texture2D(diffuse, texCoord0 + vec2(pixelWidth, -pixelHeight));
	vec4 sample = texture2D(diffuse, texCoord0);
	vec4 finalColor = (tl+tr+bl+br+sample)/5.0;
	float cap = 64.0/255.0;
	if(length(finalColor - sample) > cap)
		gl_FragColor = finalColor * baseColor;
	else
		gl_FragColor = sample * baseColor;
}