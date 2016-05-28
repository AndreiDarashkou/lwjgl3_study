#version 330

in vec3 outputColour;

out vec4 fragColor;

void main()
{
	fragColor = vec4(outputColour, 1.0);
}