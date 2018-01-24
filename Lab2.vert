#version 330 core

layout(location = 0) in vec4 vPosition;
layout(location = 1) in vec4 vColor;
out vec4 color;

uniform mat4 ModelView;
uniform mat4 Projection;

void main()
{
	gl_Position = Projection*ModelView*vPosition;
	color = vColor; //vec4( vColor, 1.0 );
}