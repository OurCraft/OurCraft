#version 120
#define M_PI 3.1415926535897932384626433832795
varying vec4 baseColor;
varying vec2 texCoord0;
uniform sampler2D texture;

const vec3 #palette#;

vec3 toHSB(vec3 c)
{
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = c.g < c.b ? vec4(c.bg, K.wz) : vec4(c.gb, K.xy);
    vec4 q = c.r < p.x ? vec4(p.xyw, c.r) : vec4(c.r, p.yzx);

    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

float vnormalize(float val, float minVal, float maxVal)
{
	return (val-minVal)/(maxVal-minVal);
}

vec3 getCoords(vec3 color)
{
	float r = color.x;
    float g = color.y;
    float blue = color.z;
    float chroma = max(max(r,g),blue) - min(min(r,g),blue);
    vec3 hsb = toHSB(color);
    float hue = hsb.x;
    float lightness = hsb.z;
    hue = vnormalize(hue, 0, 2);
    lightness = vnormalize(lightness, -1, 1);
    float x = chroma * cos(hue*M_PI);
    float y = chroma * sin(hue*M_PI);
    float z = lightness;
    return vec3(x,y,z);
}

void main()
{
	vec3 color = texture2D(texture, texCoord0).rgb;
	vec3 coords = getCoords(color);
    float x = coords.x;
    float y = coords.y;
    float z = coords.z;
    float minDist = 1./0.;
    vec3 closestColor = color;
    for(int i = 0;i<palette.length();i++)
    {
    	vec3 color1 = palette[i];
        vec3 coords1 = getCoords(color1);
        float x1 = coords1.x;
        float y1 = coords1.y;
        float z1 = coords1.z;
        if(length(color1-color) < minDist)
        {
            minDist = length(color1-color);
            closestColor = color1;
        }
    }
	gl_FragColor = vec4(closestColor, 1);
}
