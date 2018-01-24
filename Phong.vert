#version 330 core

layout(location = 0) in vec4 vPosition;
layout(location = 1) in vec3 vNormal;

out vec3 P;
out vec3 N;

uniform mat4 ModelView;
uniform mat4 NormalTransform;
uniform mat4 Projection;

void main()
{
   // Transform vertex position into eye coordinates
    P = (ModelView * vPosition).xyz;
    
    // Transform vertex normal into eye coordinates
    N = normalize(mat3(NormalTransform) * vNormal);
    
    // Vertex position afte projection
    gl_Position = Projection * ModelView * vPosition;
}
