#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec3 vertexNormal;

out vec2 outTexCoord;
out vec3 mvVertexNormal;
out vec3 mvVertexPos;
out mat4 outModelViewMatrix;
out vec4 mLightviewVertexPos;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 modelLightViewMatrix;
uniform mat4 orthoProjectionMatrix;

void main()
{
    vec4 modelViewPosition = modelViewMatrix * vec4(position, 1.0); //position in camera* (eyeCoords)
    gl_Position = projectionMatrix * modelViewPosition;             //position on display
    outTexCoord = texCoord;                                         //texture coodinates

    mvVertexNormal = normalize(modelViewMatrix * vec4(vertexNormal, 0.0)).xyz;  //normal vertex in camera*
    mvVertexPos = modelViewPosition.xyz;                                        //only vertex model view position

    mLightviewVertexPos = orthoProjectionMatrix * modelLightViewMatrix * vec4(position, 1.0);
    outModelViewMatrix = modelViewMatrix;
}