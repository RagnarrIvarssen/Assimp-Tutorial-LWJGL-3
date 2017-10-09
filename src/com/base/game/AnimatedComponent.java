package com.base.game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AINodeAnim;

import com.base.engine.components.GameComponent;
import com.base.engine.core.Matrix4f;
import com.base.engine.core.Quaternion;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.resourceManagement.MeshResource;

public class AnimatedComponent extends GameComponent
{
	public Matrix4f 	globalInverseTransform;
	public Bone			bones[];
	public Matrix4f 	boneTransforms[];
	public AINode		root;
	public AIAnimation	animation;
	public MeshResource resource;
	public Shader		shader;
	public Material		material;
	
	long timer = System.currentTimeMillis();
	
	public void AddVertices(FloatBuffer vertices, IntBuffer indices)
	{
		shader = new Shader("forward-ambient2");
		material = new Material(new Texture("bricks.jpg"), 1, 8,
				new Texture("bricks_normal.jpg"), new Texture("bricks_disp.png"), 0.03f, -0.5f);
		
		resource = new MeshResource(indices.capacity());
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.GetVbo());
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.GetIbo());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
	}
	
	@Override
	public void Update(float delta) {
		super.Update(delta);
		
		boneTransforms((float)(((double)System.currentTimeMillis() - (double)timer) / 1000.0));
	}
	
	AINodeAnim FindNodeAnim(AIAnimation pAnimation, String NodeName)
    {
        for (int i = 0 ; i < pAnimation.mNumChannels(); i++) {
            AINodeAnim pNodeAnim = AINodeAnim.create(pAnimation.mChannels().get(i));

            if (pNodeAnim.mNodeName().dataString().equals(NodeName)) return pNodeAnim;
        }

        return null;
    }

    void CalcInterpolatedPosition(Vector3f Out, float AnimationTime, AINodeAnim pNodeAnim)
    {
        if (pNodeAnim.mNumPositionKeys() == 1) {
            Out.Set(Vector3f.fromAssimp(pNodeAnim.mPositionKeys().get(0).mValue()));
            return;
        }

        int PositionIndex = FindPosition(AnimationTime, pNodeAnim);
        int NextPositionIndex = (PositionIndex + 1);
        assert(NextPositionIndex < pNodeAnim.mNumPositionKeys());
        float DeltaTime = (float)(pNodeAnim.mPositionKeys().get(NextPositionIndex).mTime() - pNodeAnim.mPositionKeys().get(PositionIndex).mTime());
        float Factor = (AnimationTime - (float)pNodeAnim.mPositionKeys().get(PositionIndex).mTime()) / DeltaTime;
        assert(Factor >= 0.0f && Factor <= 1.0f);
        Vector3f Start = Vector3f.fromAssimp(pNodeAnim.mPositionKeys().get(PositionIndex).mValue());
        Vector3f End = Vector3f.fromAssimp(pNodeAnim.mPositionKeys().get(NextPositionIndex).mValue());
        Vector3f Delta = End.Sub(Start);
        Out.Set(Start.Add(Delta.Mul(Factor)));// + Factor * Delta;
    }


    void CalcInterpolatedRotation(Quaternion Out, float AnimationTime, AINodeAnim pNodeAnim)
    {
        // we need at least two values to interpolate...
        if (pNodeAnim.mNumRotationKeys() == 1) {
            Out.Set(Quaternion.fromAssimp(pNodeAnim.mRotationKeys().get(0).mValue()));
            return;
        }

        int RotationIndex = FindRotation(AnimationTime, pNodeAnim);
        int NextRotationIndex = (RotationIndex + 1);
        assert(NextRotationIndex < pNodeAnim.mNumRotationKeys());
        float DeltaTime = (float)(pNodeAnim.mRotationKeys().get(NextRotationIndex).mTime() - pNodeAnim.mRotationKeys().get(RotationIndex).mTime());
        float Factor = (AnimationTime - (float)pNodeAnim.mRotationKeys().get(RotationIndex).mTime()) / DeltaTime;
        assert(Factor >= 0.0f && Factor <= 1.0f);
        Quaternion StartRotationQ = Quaternion.fromAssimp(pNodeAnim.mRotationKeys().get(RotationIndex).mValue());
        Quaternion EndRotationQ   = Quaternion.fromAssimp(pNodeAnim.mRotationKeys().get(NextRotationIndex).mValue());
        Out.Set(StartRotationQ.SLerp(EndRotationQ, Factor, false));// = AIQuaternion.Interpolate(Out, StartRotationQ, EndRotationQ, Factor);
//        Out = Out.Normalize();
    }


    void CalcInterpolatedScaling(Vector3f Out, float AnimationTime, AINodeAnim pNodeAnim)
    {
        if (pNodeAnim.mNumScalingKeys() == 1) {
            Out = Vector3f.fromAssimp(pNodeAnim.mScalingKeys().get(0).mValue());
            return;
        }

        int ScalingIndex = FindScaling(AnimationTime, pNodeAnim);
        int NextScalingIndex = (ScalingIndex + 1);
        assert(NextScalingIndex < pNodeAnim.mNumScalingKeys());
        float DeltaTime = (float)(pNodeAnim.mScalingKeys().get(NextScalingIndex).mTime() - pNodeAnim.mScalingKeys().get(ScalingIndex).mTime());
        float Factor = (AnimationTime - (float)pNodeAnim.mScalingKeys().get(ScalingIndex).mTime()) / DeltaTime;
        assert(Factor >= 0.0f && Factor <= 1.0f);
        Vector3f Start = Vector3f.fromAssimp(pNodeAnim.mScalingKeys().get(ScalingIndex).mValue());
        Vector3f End   = Vector3f.fromAssimp(pNodeAnim.mScalingKeys().get(NextScalingIndex).mValue());
        Vector3f Delta = End.Sub(Start);
        Out.Set(Start.Add(Delta.Mul(Factor)));
    }

    int FindPosition(float AnimationTime, AINodeAnim pNodeAnim)
    {
        for (int i = 0 ; i < pNodeAnim.mNumPositionKeys() - 1 ; i++) {
            if (AnimationTime < (float)pNodeAnim.mPositionKeys().get(i + 1).mTime()) {
                return i;
            }
        }

        return 0;
    }


    int FindRotation(float AnimationTime, AINodeAnim pNodeAnim)
    {
        assert(pNodeAnim.mNumRotationKeys() > 0);

        for (int i = 0 ; i < pNodeAnim.mNumRotationKeys() - 1 ; i++) {
            if (AnimationTime < (float)pNodeAnim.mRotationKeys().get(i + 1).mTime()) {
                return i;
            }
        }

        return 0;
    }


    int FindScaling(float AnimationTime, AINodeAnim pNodeAnim)
    {
        assert(pNodeAnim.mNumScalingKeys() > 0);

        for (int i = 0 ; i < pNodeAnim.mNumScalingKeys() - 1 ; i++) {
            if (AnimationTime < (float)pNodeAnim.mScalingKeys().get(i + 1).mTime()) {
                return i;
            }
        }

        return 0;
    }

    protected void ReadNodeHeirarchy(float AnimationTime, AINode pNode, Matrix4f ParentTransform)
    {
        String NodeName = pNode.mName().dataString();

//        engine.animation pAnimation = null;//m_pScene.mAnimations[0];

        Matrix4f NodeTransformation = new Matrix4f().fromAssimp(pNode.mTransformation());//(pNode.mTransformation);

        AINodeAnim pNodeAnim = FindNodeAnim(animation, NodeName);

        if (pNodeAnim != null)
        {
            // Interpolate scaling and generate scaling transformation matrix
            Vector3f Scaling = new Vector3f(0, 0, 0);
            CalcInterpolatedScaling(Scaling, AnimationTime, pNodeAnim);
            Matrix4f ScalingM = new Matrix4f().InitScale(Scaling.x(), Scaling.y(), Scaling.z());

            // Interpolate rotation and generate rotation transformation matrix
            Quaternion RotationQ = new Quaternion(0, 0, 0, 0);
            CalcInterpolatedRotation(RotationQ, AnimationTime, pNodeAnim);
            Matrix4f RotationM = RotationQ.ToRotationMatrix();

            // Interpolate translation and generate translation transformation matrix
            Vector3f Translation = new Vector3f(0, 0, 0);
            CalcInterpolatedPosition(Translation, AnimationTime, pNodeAnim);
            Matrix4f TranslationM = new Matrix4f().InitTranslation(Translation.x(), Translation.y(), Translation.z());

            // Combine the above transformations
            NodeTransformation = TranslationM.Mul(RotationM).Mul(ScalingM);
        }

        Matrix4f GlobalTransformation = ParentTransform.Mul(NodeTransformation);

        Bone bone = null;

        if ((bone = findBone(NodeName)) != null)
        {
            bone.finalTransformation = globalInverseTransform.Mul(GlobalTransformation).Mul(bone.offsetMatrix);
        }

        for (int i = 0 ; i < pNode.mNumChildren(); i++) {
            ReadNodeHeirarchy(AnimationTime, AINode.create(pNode.mChildren().get(i)), GlobalTransformation);
        }
    }

    private final Bone findBone(String name)
    {
        for(Bone bone : bones) if(bone.name.equals(name)) return bone;

        return null;
    }

    public void boneTransforms(float timeInSeconds)
    {
        Matrix4f Identity = new Matrix4f().InitIdentity();

        float TicksPerSecond = (float)(animation.mTicksPerSecond() != 0 ? animation.mTicksPerSecond() : 25.0f);
        float TimeInTicks = timeInSeconds * TicksPerSecond;
        float AnimationTime = (TimeInTicks % (float)animation.mDuration());

        ReadNodeHeirarchy(AnimationTime, root, Identity);

//        boneTransforms.resize(m_NumBones);

        for (short i = 0 ; i < bones.length ; i++) {
            boneTransforms[i] = bones[i].finalTransformation;
        }
    }
	
	@Override
	public void Render(Shader notshader, RenderingEngine renderingEngine) {
		shader.Bind();
		GetTransform().SetRot(new Quaternion(new Matrix4f().InitRotation(-90, 0, 0)));
		
		for(int i = 0; i < boneTransforms.length; i++) shader.SetUniform("gBones[" + i + "]", boneTransforms[i]);
		shader.UpdateUniforms(GetTransform(), material, renderingEngine);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);
		glEnableVertexAttribArray(5);
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.GetVbo());
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 19 * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 19 * 4, 12);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 19 * 4, 20);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, 19 * 4, 32);
		glVertexAttribPointer(4, 4, GL_FLOAT, false, 19 * 4, 44);
		glVertexAttribPointer(5, 4, GL_FLOAT, false, 19 * 4, 60);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.GetIbo());
		glDrawElements(GL_TRIANGLES, resource.GetSize(), GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glDisableVertexAttribArray(5);
	}
}