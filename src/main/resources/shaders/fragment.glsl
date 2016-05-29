#version 330

in vec2 outputTexture;
out vec4 fragColor;

uniform sampler2D texture_sampler;

void main()
{
	fragColor = texture(texture_sampler, outputTexture);
}