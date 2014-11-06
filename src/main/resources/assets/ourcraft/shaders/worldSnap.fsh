#version 120

uniform sampler2D diffuse;

varying vec2 texCoord0;
varying vec4 baseColor;

// RADIUS of our vignette, where 0.5 results in a circle fitting the screen
const float RADIUS = 0.75;

// softness of our vignette, between 0.0 and 1.0
const float SOFTNESS = 0.45;

void main()
{
	vec4 color = texture2D(diffuse, texCoord0);
	// Credits to Matt 'mattdesl' DesLauriers : https://github.com/mattdesl/lwjgl-basics/wiki/ShaderLesson3
    // determine center position
    vec2 position = texCoord0 - vec2(0.5);

    // determine the vector length of the center position
    float len = length(position);

    // use smoothstep to create a smooth vignette
    float vignette = smoothstep(RADIUS, RADIUS-SOFTNESS, len);

    // apply the vignette with 50% opacity
    color.rgb = mix(color.rgb, color.rgb * vignette, 0.5);

    gl_FragColor = color * baseColor;
}