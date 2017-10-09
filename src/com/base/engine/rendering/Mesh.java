package com.base.engine.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;
import java.util.HashMap;

import com.base.engine.core.Util;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.meshLoading.IndexedModel;
import com.base.engine.rendering.meshLoading.OBJModel;
import com.base.engine.rendering.resourceManagement.MeshResource;

public class Mesh
{
	private static HashMap<String, MeshResource> s_loadedModels = new HashMap<String, MeshResource>();
	private MeshResource m_resource;
	private String       m_fileName;
	
	public Mesh(String fileName)
	{
		this.m_fileName = fileName;
		MeshResource oldResource = s_loadedModels.get(fileName);

		if(oldResource != null)
		{
			m_resource = oldResource;
			m_resource.AddReference();
		}
		else
		{
			LoadMesh(fileName);
			s_loadedModels.put(fileName, m_resource);
		}
	}
	
	public Mesh(Vertex[] vertices, int[] indices)
	{
		this(vertices, indices, false);
	}
	
	public Mesh(Vertex[] vertices, int[] indices, boolean calcNormals)
	{
		m_fileName = "";
		AddVertices(vertices, indices, calcNormals);
	}

	@Override
	protected void finalize()
	{
		if(m_resource.RemoveReference() && !m_fileName.isEmpty())
		{
			s_loadedModels.remove(m_fileName);
		}
	}
	
	private void AddVertices(Vertex[] vertices, int[] indices, boolean calcNormals)
	{
		if(calcNormals)
		{
			CalcNormals(vertices, indices);
		}

		m_resource = new MeshResource(indices.length);
		
		glBindBuffer(GL_ARRAY_BUFFER, m_resource.GetVbo());
		glBufferData(GL_ARRAY_BUFFER, Util.CreateFlippedBuffer(vertices), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_resource.GetIbo());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.CreateFlippedBuffer(indices), GL_STATIC_DRAW);
	}
	
	public void Draw()
	{
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		
		glBindBuffer(GL_ARRAY_BUFFER, m_resource.GetVbo());
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_resource.GetIbo());
		glDrawElements(GL_TRIANGLES, m_resource.GetSize(), GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
	}
	
	private void CalcNormals(Vertex[] vertices, int[] indices)
	{
		for(int i = 0; i < indices.length; i += 3)
		{
			int i0 = indices[i];
			int i1 = indices[i + 1];
			int i2 = indices[i + 2];
			
			Vector3f v1 = vertices[i1].GetPos().Sub(vertices[i0].GetPos());
			Vector3f v2 = vertices[i2].GetPos().Sub(vertices[i0].GetPos());
			
			Vector3f normal = v1.Cross(v2).Normalized();
			
			vertices[i0].SetNormal(vertices[i0].GetNormal().Add(normal));
			vertices[i1].SetNormal(vertices[i1].GetNormal().Add(normal));
			vertices[i2].SetNormal(vertices[i2].GetNormal().Add(normal));
		}
		
		for(int i = 0; i < vertices.length; i++)
			vertices[i].SetNormal(vertices[i].GetNormal().Normalized());
	}
	
	private Mesh LoadMesh(String fileName)
	{
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];

		if(!ext.equals("obj"))
		{
			System.err.println("Error: '" + ext + "' file format not supported for mesh data.");
			new Exception().printStackTrace();
			System.exit(1);
		}

		OBJModel test = new OBJModel("./res/models/" + fileName);
		IndexedModel model = test.ToIndexedModel();

		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		for(int i = 0; i < model.GetPositions().size(); i++)
		{
			vertices.add(new Vertex(model.GetPositions().get(i),
					model.GetTexCoords().get(i),
					model.GetNormals().get(i),
					model.GetTangents().get(i)));
		}

		Vertex[] vertexData = new Vertex[vertices.size()];
		vertices.toArray(vertexData);

		Integer[] indexData = new Integer[model.GetIndices().size()];
		model.GetIndices().toArray(indexData);

		AddVertices(vertexData, Util.ToIntArray(indexData), false);
		
		return this;
	}
}


//private Mesh Mesh(String string)
//{
//	return new Mesh(string);
////	IndexedModel model = LoadMeshData(new File(string));
////	
////	ArrayList<Vertex> vertices = new ArrayList<Vertex>();
////
////	for(int i = 0; i < model.GetPositions().size(); i++)
////	{
////		vertices.add(new Vertex(model.GetPositions().get(i),
////				model.GetTexCoords().get(i),
////				model.GetNormals().get(i),
////				model.GetTangents().get(i)));
////	}
////	
////	Vertex[] vertexData = new Vertex[vertices.size()];
////	vertices.toArray(vertexData);
////
////	Integer[] indexData = new Integer[model.GetIndices().size()];
////	model.GetIndices().toArray(indexData);
//	
////	return new Mesh(Util.CreateFlippedBuffer(vertexData), Util.CreateFlippedBuffer(Util.ToIntArray(indexData)));
//}

//public ByteBuffer loadTexture(String fileName) throws IOException
//{
//	File file = new File(fileName);
//	
//	FileInputStream fileInputStream = new FileInputStream(file);
//    int byteLength=(int) file.length(); //bytecount of the file-content
//    byte[] filecontent = new byte[byteLength];
//    fileInputStream.read(filecontent,0,byteLength);
//    
//    return (ByteBuffer) Util.CreateByteBuffer(byteLength).put(filecontent).flip();
//}
//
//private static IndexedModel LoadMeshData(File file)
//{
//	AIScene scene =  Assimp.aiImportFile(file.toString(), 
//			Assimp.aiProcess_Triangulate |
//			Assimp.aiProcess_GenSmoothNormals | 
//			Assimp.aiProcess_FlipUVs |
//			Assimp.aiProcess_CalcTangentSpace);
//	
//		AIMesh mesh = AIMesh.create(scene.mMeshes().get(0));
//		ArrayList<Vector3f> vertices = new ArrayList<>();
//		ArrayList<Vector3f> normals = new ArrayList<>();
//		ArrayList<Vector3f> tangents = new ArrayList<>();
//		ArrayList<Vector2f> texcoords = new ArrayList<>();
//		ArrayList<Integer> indices = new ArrayList<>();
//		for(int v = 0; v < mesh.mNumVertices(); v++)
//		{
//			AIVector3D pos = mesh.mVertices().get(v);
//			AIVector3D nor = mesh.mNormals().get(v);
//			if(mesh.mTangents() != null)
//			{
//				AIVector3D tan = mesh.mTangents().get(v);
//				tangents.add(new Vector3f(tan.x(), tan.y(), tan.z()));
//			} else tangents.add(new Vector3f(0,0,0));
//			AIVector3D tex = mesh.mTextureCoords(0).get(v);
//			
//			vertices.add(new Vector3f(pos.x(), pos.y(), pos.z()));
//			normals.add(new Vector3f(nor.x(), nor.y(), nor.z()));
//			texcoords.add(new Vector2f(tex.x(), tex.y()));
//		}
//		
//		for(int f = 0; f < mesh.mNumFaces(); f++)
//		{
//			AIFace face = mesh.mFaces().get(f);
////			OBJIndex index = new OBJIndex();
//			for(int IND = 0; IND < face.mNumIndices(); IND++)
//			{
//				indices.add(face.mIndices().get(IND));
//			}
//		}
//		
//	return (OBJModel.ToIndexedModel(vertices, normals, tangents, texcoords, indices, true, true));
//}