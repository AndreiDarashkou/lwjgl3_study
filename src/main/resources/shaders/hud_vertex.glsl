#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec3 vertexNormal;

out vec2 outTexCoord;

struct TextureSet
{
    vec2 offset;
    vec2 visibleMin;
    vec2 visibleMax;
    bool isUsed;
};

uniform mat4 projModelMatrix;
uniform TextureSet textureSet;

void main()
{
    gl_Position = projModelMatrix * vec4(position, 1.0);
    outTexCoord = texCoord + textureSet.offset;

    if(textureSet.isUsed) {
        if (outTexCoord.x < textureSet.visibleMin.x) {
            outTexCoord.x = textureSet.visibleMin.x;
        }
        if (outTexCoord.y < textureSet.visibleMin.y) {
            outTexCoord.y = textureSet.visibleMin.y;
        }

        if (outTexCoord.x > textureSet.visibleMax.x) {
            outTexCoord.x = textureSet.visibleMax.x;
        }
        if (outTexCoord.y > textureSet.visibleMax.y) {
            outTexCoord.y = textureSet.visibleMax.y;
        }
    }
}