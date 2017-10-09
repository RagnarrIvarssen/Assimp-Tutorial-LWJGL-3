#version 440
#define MAX_BONES 200
in vec3 position;
in vec2 texCoord;
in vec3 normal;
in vec3 tangent;
in vec4 boneDataA;
in vec4 boneDataB;

out vec2 texCoord0;
out vec3 worldPos0;
out mat3 tbnMatrix;

uniform mat4 T_model;
uniform mat4 T_MVP;

uniform mat4 gBones[200];

void main()
{
	mat4 BoneTransform = gBones[uint(boneDataA.x)] * boneDataA.z +
						 gBones[uint(boneDataA.y)] * boneDataA.w +
						 gBones[uint(boneDataB.x)] * boneDataB.z +
						 gBones[uint(boneDataB.y)] * boneDataB.w ;
						 
	vec4 pos = BoneTransform * vec4(position, 1.0);

    gl_Position = T_MVP * pos;
    texCoord0 = texCoord; 
    worldPos0 = (T_model * pos).xyz;
    
    vec3 n = normalize((T_model * vec4(normal, 0.0)).xyz);
    vec3 t = normalize((T_model * vec4(tangent, 0.0)).xyz);
    t = normalize(t - dot(t, n) * n);
    
    vec3 biTangent = cross(t, n);
    tbnMatrix = mat3(t, biTangent, n);
}