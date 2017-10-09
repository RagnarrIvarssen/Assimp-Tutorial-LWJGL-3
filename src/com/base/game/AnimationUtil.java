package com.base.game;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.AIVertexWeight;
import org.lwjgl.assimp.Assimp;

import com.base.engine.core.Matrix4f;
import com.base.engine.core.Util;

public class AnimationUtil {
	public static final AnimatedComponent loadAnimatedFile(File file)
	{
		AIScene scene = Assimp.aiImportFile(file.toString(),
			Assimp.aiProcess_Triangulate |	
			Assimp.aiProcess_GenSmoothNormals |	
			Assimp.aiProcess_FlipUVs |	
			Assimp.aiProcess_CalcTangentSpace |	
			Assimp.aiProcess_LimitBoneWeights	
		);
		
		if(scene == null | scene.mNumAnimations() == 0)
		{
			System.err.println("the imported file does not contain any animations.");
			System.exit(0);
		}
		
		AIMesh mesh = AIMesh.create(scene.mMeshes().get(0));
		
		int sizeOfVertex = 19;
		int sizeOfVertexUnrigged = 11;
		/**
		 * position data 3f
		 * normal   data 3f
		 * tangent  data 3f
		 * texcoord data 2f
		 * 
		 * bone		info 4f
		 * bone		info 4f
		 */
		float array[]	 = new float[mesh.mNumVertices() * sizeOfVertex];
		int index 		 = 0;
		
		for(int v = 0; v < mesh.mNumVertices(); v++)
		{
			AIVector3D position = mesh.mVertices().get(v);
			AIVector3D normal   = mesh.mNormals().get(v);
			AIVector3D tangent  = mesh.mTangents().get(v);
			AIVector3D texCoord = mesh.mTextureCoords(0).get(v);
			/**
			 * The above assumes that the program has texture coordinates, if it doesn't the program will throw a null pointer exception.
			 */
			
			array[index++] = position.x();
			array[index++] = position.y();
			array[index++] = position.z();
			
			array[index++] = texCoord.x();
			array[index++] = texCoord.y();
			
			array[index++] = normal.x();
			array[index++] = normal.y();
			array[index++] = normal.z();
			
			array[index++] = tangent.x();
			array[index++] = tangent.y();
			array[index++] = tangent.z();
			
			array[index++] = 0;
			array[index++] = 0;
			array[index++] = 0;
			array[index++] = 0;
			
			array[index++] = 0;
			array[index++] = 0;
			array[index++] = 0;
			array[index++] = 0;
		}

		index = 0;
		/**
		 * ^
		 * this has to be here
		 */
		IntBuffer indices = Util.CreateIntBuffer(mesh.mNumFaces() * mesh.mFaces().get(0).mNumIndices());
		
		for(int f = 0; f < mesh.mNumFaces(); f++)
		{
			AIFace face = mesh.mFaces().get(f);
			for(int ind = 0; ind < face.mNumIndices(); ind++)
				indices.put(face.mIndices().get(ind));
		}
		
		HashMap<String, Integer> boneMap = new HashMap<>();
		HashMap<Integer, Integer> bone_index_map0 = new HashMap<>();
		HashMap<Integer, Integer> bone_index_map1 = new HashMap<>();
		
		for(int b = 0; b < mesh.mNumBones(); b++)
		{
			AIBone bone = AIBone.create(mesh.mBones().get(b));
			boneMap.put(bone.mName().dataString(), b);
			
			for(int w = 0; w < bone.mNumWeights(); w++)
			{
				AIVertexWeight weight = bone.mWeights().get(w);
				int vertexIndex = weight.mVertexId();
				int findex		= vertexIndex * sizeOfVertex;
				
				if(!bone_index_map0.containsKey(vertexIndex))
				{
					array[(findex + sizeOfVertexUnrigged) + 0] = b;
					array[(findex + sizeOfVertexUnrigged) + 2] = weight.mWeight();
					bone_index_map0.put(vertexIndex, 0);
				} else if(bone_index_map0.get(vertexIndex) == 0)
				{
					array[(findex + sizeOfVertexUnrigged) + 1] = b;
					array[(findex + sizeOfVertexUnrigged) + 3] = weight.mWeight();
					bone_index_map0.put(vertexIndex, 1);
				} else if(!bone_index_map1.containsKey(vertexIndex))
				{
					array[(findex + sizeOfVertexUnrigged) + 4] = b;
					array[(findex + sizeOfVertexUnrigged) + 6] = weight.mWeight();
					bone_index_map1.put(vertexIndex, 0);
				} else if(bone_index_map1.get(vertexIndex) == 0)
				{
					array[(findex + sizeOfVertexUnrigged) + 5] = b;
					array[(findex + sizeOfVertexUnrigged) + 7] = weight.mWeight();
					bone_index_map1.put(vertexIndex, 1);
				} else {
					System.err.println("max 4 bones per vertex.");
					System.exit(0);
				}
			}
		}
		
		AIMatrix4x4 inverseRootTransform = scene.mRootNode().mTransformation();
		Matrix4f	inverseRootTransformation = new Matrix4f().fromAssimp(inverseRootTransform);
		
		Bone bones[] = new Bone[boneMap.size()];
		
		for(int b = 0; b < mesh.mNumBones(); b++)
		{
			AIBone bone = AIBone.create(mesh.mBones().get(b));
			bones[b] = new Bone();
			
			bones[b].name		  = bone.mName().dataString();
			bones[b].offsetMatrix = new Matrix4f().fromAssimp(bone.mOffsetMatrix());
		}
		
		AnimatedComponent component = new AnimatedComponent();
		FloatBuffer vertices = Util.CreateFloatBuffer(array.length);
		
		for(int i = 0; i < array.length; i++) vertices.put(array[i]);
		vertices.flip();
		indices.flip();
		
		component.AddVertices(vertices, indices);
		
		component.animation = AIAnimation.create(scene.mAnimations().get(0));
		component.bones		= bones;
		component.boneTransforms = new Matrix4f[bones.length];
		component.root			 = scene.mRootNode();
		component.globalInverseTransform = inverseRootTransformation;
		
		return component;
	}
}
