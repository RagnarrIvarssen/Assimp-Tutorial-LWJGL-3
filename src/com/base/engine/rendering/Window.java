/*
 * Copyright (C) 2014 Benny Bobaganoosh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.base.engine.rendering;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWDropCallback;
import org.lwjgl.glfw.GLFWDropCallbackI;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;

import com.base.engine.core.Vector2f;

public class Window 
{
	private static final ArrayList<ScreenDraggable> s_behaviours = new ArrayList<>();
	private static final Window window = new Window();
	public static int BOUND_FBO_ID = 0;
	public static int BOUND_FBO_TYPE = 0;
	public static int BOUND_TEX_ID = 0;
	public static int BOUND_TEX_TYPE = 0;
	public static int BOUND_TEX_VX = 0;
	public static int BOUND_TEX_VY = 0;
	public static int BOUND_TEX_VW = 0;
	public static int BOUND_TEX_VH = 0;
	public static int WIDTH = 0;
	public static int HEIGHT = 0;
	protected static long m_window;
	protected static GLFWErrorCallback m_errorCallback;
	protected static GLFWDropCallbackI m_dropCallBackI;
	protected static GLFWDropCallback m_dropCallBack;
	protected static GLFWWindowSizeCallback m_resizeCallBack;
	public static Mouse Mouse;
	
	public static void CreateWindow(int width, int height, String title)
	{
		try 
		{
			if(!glfwInit()) throw new Exception("GLFW Initialization failed.");
			glfwSetErrorCallback(m_errorCallback = GLFWErrorCallback.createPrint(System.err));
			glfwDefaultWindowHints();
			glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
			glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
			glfwWindowHint(GLFW_REFRESH_RATE , GLFW_DONT_CARE);
			glfwWindowHint(GLFW_DOUBLEBUFFER , GL_TRUE);
			Window.WIDTH = width;
			Window.HEIGHT = height;
			m_window = glfwCreateWindow(width, height, title, NULL, NULL);
			if(m_window == 0) throw new Exception("GLFW Window creation failed.");
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			// Center our window
			glfwSetWindowPos(
				m_window,
				(vidmode.width() - WIDTH) / 2,
				(vidmode.height() - HEIGHT) / 2
			);
			
			Mouse = window.new Mouse();
			Mouse.Create(m_window);

			glfwSetWindowSizeCallback(m_window, m_resizeCallBack = new GLFWWindowSizeCallback()
			{
				@Override
				public void invoke(long arg0, int arg1, int arg2)
				{
					Window.WIDTH = arg1;
					Window.HEIGHT = arg2;
				}
			});
			glfwMakeContextCurrent(m_window);
			glfwSwapInterval(0);
			glfwShowWindow(m_window);
			GL.createCapabilities();
			int vao = GL30.glGenVertexArrays ();
			GL30.glBindVertexArray (vao);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void BindAsRenderTarget()
	{
		BindAsRenderTarget(GL_TEXTURE_2D, 0, GL_FRAMEBUFFER, 0, 0, 0, GetWidth(), GetHeight());
	}
	
	public static void BindAsRenderTarget(int viewPortX,
			int viewPortY, int width, int height)
	{
		BindAsRenderTarget(GL_TEXTURE_2D, 0, GL_FRAMEBUFFER, 0, viewPortX, viewPortY, width, height);
	}

	public static void BindAsRenderTarget(int frameBufferType, int fboId, int viewPortX, int viewPortY, int width,
			int height)
	{
		glViewport(viewPortX, viewPortY, width, height);
		glBindFramebuffer(frameBufferType, fboId);
		BOUND_FBO_ID = fboId;
		BOUND_FBO_TYPE = frameBufferType;

		BOUND_TEX_VX = viewPortX;
		BOUND_TEX_VY = viewPortY;
		BOUND_TEX_VW = width;
		BOUND_TEX_VH = height;
	}

	public static void BindAsRenderTarget(int texTureType, int id, int frameBufferType, int fboId, int viewPortX,
			int viewPortY, int width, int height)
	{
		glBindTexture(texTureType, id);
		BindAsRenderTarget(frameBufferType, fboId, viewPortX, viewPortY, width, height);
		BOUND_TEX_ID = id;
		BOUND_TEX_TYPE = texTureType;
	}
	
	public static void BindLastAsRenderTarget(WindowRenderTargetInfo info)
	{
		BindAsRenderTarget(info.BOUND_FBO_TYPE, info.BOUND_FBO_ID, info.BOUND_TEX_VX, info.BOUND_TEX_VY, info.BOUND_TEX_VW, info.BOUND_TEX_VH);
	}
	
	public static WindowRenderTargetInfo SaveLastRenderTarget()
	{
		WindowRenderTargetInfo info = new WindowRenderTargetInfo();
		info.BOUND_FBO_ID = BOUND_FBO_ID;
		info.BOUND_FBO_TYPE = BOUND_FBO_TYPE;
		info.BOUND_TEX_ID = BOUND_TEX_ID;
		info.BOUND_TEX_TYPE = BOUND_TEX_TYPE;
		info.BOUND_TEX_VX = BOUND_TEX_VX;
		info.BOUND_TEX_VY = BOUND_TEX_VY;
		info.BOUND_TEX_VW = BOUND_TEX_VW;
		info.BOUND_TEX_VH = BOUND_TEX_VH;
		return info;
	}
	
	public static class WindowRenderTargetInfo
	{
		public int BOUND_FBO_ID = 0;
		public int BOUND_FBO_TYPE = 0;
		public int BOUND_TEX_ID = 0;
		public int BOUND_TEX_TYPE = 0;
		public int BOUND_TEX_VX = 0;
		public int BOUND_TEX_VY = 0;
		public int BOUND_TEX_VW = 0;
		public int BOUND_TEX_VH = 0;
	}
	
	public static void Update()
	{
		glfwPollEvents();
	}
	
	public static void Render()
	{
//		Display.update();
		glfwSwapBuffers(m_window);
//		glfwPollEvents();
	}
	
	public static void Dispose()
	{
		glfwDestroyWindow(m_window);
//		Display.destroy();
//		Keyboard.destroy();
//		Mouse.destroy();
	}
	
	public static boolean IsCloseRequested()
	{
		return glfwWindowShouldClose(m_window);//Display.isCloseRequested();
	}
	
	public static void SetCloseRequested(boolean value)
	{
		glfwSetWindowShouldClose(m_window, value);
	}
	
	public static void SetTitle(String title)
	{
		glfwSetWindowTitle(m_window, title);
	}
	
	static final IntBuffer w = BufferUtils.createIntBuffer(1);
	static final IntBuffer h = BufferUtils.createIntBuffer(1);
	
	public static int GetWidth()
	{
		glfwGetWindowSize(m_window, w, h);
		WIDTH = w.get(0);
		HEIGHT = h.get(0);
		return w.get(0);//Display.getDisplayMode().getWidth();
	}
	
	public static int GetHeight()
	{
		glfwGetWindowSize(m_window, w, h);
		WIDTH = w.get(0);
		HEIGHT = h.get(0);
		return h.get(0);//Display.getDisplayMode().getHeight();
	}
	
	public static float Aspect()
	{
		return (float)GetWidth() / (float)GetHeight();
	}
	
//	public static String GetTitle()
//	{
//		return glfwgetwindow//Display.getTitle();
//	}

	public Vector2f GetCenter()
	{
		return new Vector2f(GetWidth()/2, GetHeight()/2);
	}
	
	public static long CurrentWindow()
	{
		return m_window;
	}
	
	public class Mouse
	{
		public GLFWCursorPosCallback callback;
		public GLFWScrollCallback scallback;
		public double x;
		public double y;
//		
		public double dx;
		public double dy;
		
		public double wheel;
		
//		double[] x;
//		double[] y;
//		Vector2f MousePos;
		
		public void Create(long window)
		{
//			MousePos = new Vector2f(0, 0);
//			x = new double[1];
//			y = new double[1];
//			glfwGetCursorPos(CurrentWindow(), x, y);
//			MousePos.Set((float)x[0], (float)y[0]);
			 glfwSetCursorPosCallback(window, callback = new GLFWCursorPosCallback(){
				 
	                @Override
	                public void invoke(long window, double xpos, double ypos) {
	                    // Add delta of x and y mouse coordinates
	                	dx += (int)xpos - x;
	                	dy += (int)xpos - y;
	                    // Set new positions of x and y
	                    x = (int) xpos;
	                    y = (int) ypos;
	                }
	            });
			 glfwSetScrollCallback(m_window, scallback = new GLFWScrollCallback() {
				
				@Override
				public void invoke(long arg0, double arg1, double arg2)
				{
					wheel += arg2;
				}
			});
		}
		
		public void Update()
		{
			double xx[] = new double[1];
			double yy[] = new double[1];
			glfwGetCursorPos(CurrentWindow(), xx, yy);
			x = xx[0];
			y = yy[0];
		}
		
		public void setCursorPosition(float x, float y)
		{
			glfwSetCursorPos(m_window, x, y);
			Update();
		}
		
		public void setGrabbed(boolean grabbed)
		{
			if(grabbed)
			{
				glfwSetInputMode(m_window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
			}
			else
			{
				glfwSetInputMode(m_window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
			}
		}
		
		public double getWheel()
		{
			return wheel;
		}
		
		public double getX()
		{
			return x;
		}
		
		public double getY()
		{
			//TODO: This is a fucking bug, remove this temp fix and fix it permanently.
			int height = (int) Math.ceil((HEIGHT/1080.0)-1.0);
			return (HEIGHT - height) - y;
		}
		
		public double getOGLX()
		{
			return x;
		}
		
		public double getOGLY()
		{
			return y;
		}
	}

	public static void SetResizeable(boolean b)
	{
		int bool = GL_FALSE;
		if(b) bool = GL_TRUE;
		glfwWindowHint(GLFW_RESIZABLE, bool);
	}

	public static void SetSize(int i, int j)
	{
		glfwSetWindowSize(m_window, i, j);
	}
	
	public static void AddDragAndDropBehaviour(ScreenDraggable draggable)
	{
		s_behaviours.add(draggable);
	}
	
	public static interface ScreenDraggable
	{
		public void CheckDropped(String string);
	}
}
