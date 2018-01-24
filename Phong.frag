#version 330 core

//in vec4 color;
in vec3 P;
in vec3 N;
out vec4 fColor;

uniform vec4 LightPosition;
uniform vec4 AmbientProduct, DiffuseProduct, SpecularProduct;
uniform float Shininess;

// The following uniform is used in case light position 
// is defined in wordl coordinates
//uniform mat4 ModelView;  

void main()
{
   // Here light position is defined in eye coordinates
    vec3 L = normalize( LightPosition.xyz - P );
    
    // If Light position is defined in world coordinates,
    // the next line is used instead of the above
    //vec3 L = normalize( (ModelView * (LightPosition-gl_FragCoord)).xyz);

	// View direction
    vec3 E = normalize( -P );

 	// Original Phong illumination model uses perfect reflection direction
     vec3 R = normalize( -reflect(L,N) );	 
    //Modified Phong or Blinn-Phong illumination model uses the direction
    //half-way between light and eye directions 
//	vec3 H = normalize( L + E );

    // Compute terms in the illumination equation
    vec4 ambient = AmbientProduct;

    float Kd = max( dot(L, N), 0.0 );
    vec4  diffuse = Kd*DiffuseProduct;

	//For original Phong model 
	float Ks = pow( max(dot(E, R), 0.0), Shininess );
	// For Blinn-Phong model
//	float Ks = pow( max(dot(N, H), 0.0), Shininess );

    vec4  specular = Ks * SpecularProduct;
    specular = clamp(specular, 0.0, 1.0); 
    //if( dot(L, N) < 0.0 ) {
	//	specular = vec4(0.0, 0.0, 0.0, 1.0);
    //} 

    fColor = ambient + diffuse + specular;
    fColor.a = 1.0;
}