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

package com.base.engine.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.glfw.GLFW;

import com.base.engine.rendering.Window;

public class Input 
{
	public static ArrayList<Integer> keys = new ArrayList<>();
	public static final int NUM_MOUSEBUTTONS = 5;
	
	//All these constants come from LWJGL's Keyboard class
	public static final int KEY_ESCAPE        = Key(GLFW.GLFW_KEY_ESCAPE);
	public static final int KEY_1             = Key(GLFW.GLFW_KEY_1);
	public static final int KEY_2             = Key(GLFW.GLFW_KEY_2);
	public static final int KEY_3             = Key(GLFW.GLFW_KEY_3);
	public static final int KEY_4             = Key(GLFW.GLFW_KEY_4);
	public static final int KEY_5             = Key(GLFW.GLFW_KEY_5);
	public static final int KEY_6             = Key(GLFW.GLFW_KEY_6);
	public static final int KEY_7             = Key(GLFW.GLFW_KEY_7);
	public static final int KEY_8             = Key(GLFW.GLFW_KEY_8);
	public static final int KEY_9             = Key(GLFW.GLFW_KEY_9);
	public static final int KEY_0             = Key(GLFW.GLFW_KEY_0);
	public static final int KEY_MINUS         = Key(GLFW.GLFW_KEY_MINUS); /* - on main keyboard */
	public static final int KEY_EQUALS        = Key(GLFW.GLFW_KEY_EQUAL);
	public static final int KEY_BACKSPACE          = Key(GLFW.GLFW_KEY_BACKSPACE); /* backspace */
	public static final int KEY_TAB           = Key(GLFW.GLFW_KEY_TAB);
	public static final int KEY_Q             = Key(GLFW.GLFW_KEY_Q);
	public static final int KEY_W             = Key(GLFW.GLFW_KEY_W);
	public static final int KEY_E             = Key(GLFW.GLFW_KEY_E);
	public static final int KEY_R             = Key(GLFW.GLFW_KEY_R);
	public static final int KEY_T             = Key(GLFW.GLFW_KEY_T);
	public static final int KEY_Y             = Key(GLFW.GLFW_KEY_Y);
	public static final int KEY_U             = Key(GLFW.GLFW_KEY_U);
	public static final int KEY_I             = Key(GLFW.GLFW_KEY_I);
	public static final int KEY_O             = Key(GLFW.GLFW_KEY_O);
	public static final int KEY_P             = Key(GLFW.GLFW_KEY_P);
	public static final int KEY_LBRACKET      = Key(GLFW.GLFW_KEY_LEFT_BRACKET);
	public static final int KEY_RBRACKET      = Key(GLFW.GLFW_KEY_RIGHT_BRACKET);
	public static final int KEY_RETURN        = Key(GLFW.GLFW_KEY_ENTER); /* Enter on main keyboard */
	public static final int KEY_ENTER         = Key(GLFW.GLFW_KEY_ENTER); /* Enter on main keyboard */
	public static final int KEY_LCONTROL      = Key(GLFW.GLFW_KEY_LEFT_CONTROL);
	public static final int KEY_A             = Key(GLFW.GLFW_KEY_A);
	public static final int KEY_S             = Key(GLFW.GLFW_KEY_S);
	public static final int KEY_D             = Key(GLFW.GLFW_KEY_D);
	public static final int KEY_F             = Key(GLFW.GLFW_KEY_F);
	public static final int KEY_G             = Key(GLFW.GLFW_KEY_G);
	public static final int KEY_H             = Key(GLFW.GLFW_KEY_H);
	public static final int KEY_J             = Key(GLFW.GLFW_KEY_J);
	public static final int KEY_K             = Key(GLFW.GLFW_KEY_K);
	public static final int KEY_L             = Key(GLFW.GLFW_KEY_L);
	public static final int KEY_SEMICOLON     = Key(GLFW.GLFW_KEY_SEMICOLON);
	public static final int KEY_APOSTROPHE    = Key(GLFW.GLFW_KEY_APOSTROPHE);
	public static final int KEY_GRAVE         = Key(GLFW.GLFW_KEY_GRAVE_ACCENT); /* accent grave */
	public static final int KEY_LSHIFT        = Key(GLFW.GLFW_KEY_LEFT_SHIFT);
	public static final int KEY_BACKSLASH     = Key(GLFW.GLFW_KEY_BACKSLASH);
	public static final int KEY_Z             = Key(GLFW.GLFW_KEY_Z);
	public static final int KEY_X             = Key(GLFW.GLFW_KEY_X);
	public static final int KEY_C             = Key(GLFW.GLFW_KEY_C);
	public static final int KEY_V             = Key(GLFW.GLFW_KEY_V);
	public static final int KEY_B             = Key(GLFW.GLFW_KEY_B);
	public static final int KEY_N             = Key(GLFW.GLFW_KEY_N);
	public static final int KEY_M             = Key(GLFW.GLFW_KEY_M);
	public static final int KEY_COMMA         = Key(GLFW.GLFW_KEY_COMMA);
	public static final int KEY_PERIOD        = Key(GLFW.GLFW_KEY_PERIOD); /* . on main keyboard */
	public static final int KEY_SLASH         = Key(GLFW.GLFW_KEY_SLASH); /* / on main keyboard */
	public static final int KEY_RSHIFT        = Key(GLFW.GLFW_KEY_RIGHT_SHIFT);
	public static final int KEY_MULTIPLY      = Key(GLFW.GLFW_KEY_KP_MULTIPLY); /* * on numeric keypad */
	public static final int KEY_LMENU         = Key(GLFW.GLFW_KEY_LEFT_ALT); /* left Alt */
	public static final int KEY_LALT          = Key(GLFW.GLFW_KEY_LEFT_ALT); /* left Alt */
	public static final int KEY_SPACE         = Key(GLFW.GLFW_KEY_SPACE);
	public static final int KEY_CAPS_LOCK       = Key(GLFW.GLFW_KEY_CAPS_LOCK);
	public static final int KEY_F1            = Key(GLFW.GLFW_KEY_F1);
	public static final int KEY_F2            = Key(GLFW.GLFW_KEY_F2);
	public static final int KEY_F3            = Key(GLFW.GLFW_KEY_F3);
	public static final int KEY_F4            = Key(GLFW.GLFW_KEY_F4);
	public static final int KEY_F5            = Key(GLFW.GLFW_KEY_F5);
	public static final int KEY_F6            = Key(GLFW.GLFW_KEY_F6);
	public static final int KEY_F7            = Key(GLFW.GLFW_KEY_F7);
	public static final int KEY_F8            = Key(GLFW.GLFW_KEY_F8);
	public static final int KEY_F9            = Key(GLFW.GLFW_KEY_F9);
	public static final int KEY_F10           = Key(GLFW.GLFW_KEY_F10);
	public static final int KEY_NUMLOCK       = Key(GLFW.GLFW_KEY_NUM_LOCK);
	public static final int KEY_SCROLL        = Key(GLFW.GLFW_KEY_SCROLL_LOCK); /* Scroll Lock */
	public static final int KEY_NUMPAD7       = Key(GLFW.GLFW_KEY_KP_7);
	public static final int KEY_NUMPAD8       = Key(GLFW.GLFW_KEY_KP_8);
	public static final int KEY_NUMPAD9       = Key(GLFW.GLFW_KEY_KP_9);
	public static final int KEY_SUBTRACT      = Key(GLFW.GLFW_KEY_KP_SUBTRACT); /* - on numeric keypad */
	public static final int KEY_NUMPAD4       = Key(GLFW.GLFW_KEY_KP_4);
	public static final int KEY_NUMPAD5       = Key(GLFW.GLFW_KEY_KP_5);
	public static final int KEY_NUMPAD6       = Key(GLFW.GLFW_KEY_KP_6);
	public static final int KEY_ADD           = Key(GLFW.GLFW_KEY_KP_ADD); /* + on numeric keypad */
	public static final int KEY_NUMPAD1       = Key(GLFW.GLFW_KEY_KP_1);
	public static final int KEY_NUMPAD2       = Key(GLFW.GLFW_KEY_KP_2);
	public static final int KEY_NUMPAD3       = Key(GLFW.GLFW_KEY_KP_3);
	public static final int KEY_NUMPAD0       = Key(GLFW.GLFW_KEY_KP_0);
	public static final int KEY_DECIMAL       = Key(GLFW.GLFW_KEY_KP_DECIMAL); /* . on numeric keypad */
	public static final int KEY_F11           = Key(GLFW.GLFW_KEY_F11);
	public static final int KEY_F12           = Key(GLFW.GLFW_KEY_F12);
	public static final int KEY_F13           = Key(GLFW.GLFW_KEY_F13); /*                     (NEC PC98) */
	public static final int KEY_F14           = Key(GLFW.GLFW_KEY_F14); /*                     (NEC PC98) */
	public static final int KEY_F15           = Key(GLFW.GLFW_KEY_F15); /*                     (NEC PC98) */
//	public static final int KEY_KANA          = Key(GLFW.GLFW_KEY_ka; /* (Japanese keyboard)            */
//	public static final int KEY_CONVERT       = Key(GLFW.GLFW_KEY_; /* (Japanese keyboard)            */
//	public static final int KEY_NOCONVERT     = Key(GLFW.GLFW_KEY_; /* (Japanese keyboard)            */
//	public static final int KEY_YEN           = Key(GLFW.GLFW_KEY_; /* (Japanese keyboard)            */
//	public static final int KEY_NUMPADEQUALS Key(GLFW.GLFW_KEY_; /* = on numeric keypad (NEC PC98) */
//	public static final int KEY_CIRCUMFLEX    = Key(GLFW.GLFW_KEY_; /* (Japanese keyboard)            */
//	public static final int KEY_AT            = Key(GLFW.GLFW_KEY_at; /*                     (NEC PC98) */
//	public static final int KEY_COLON         = Key(GLFW.GLFW_KEY_sem; /*                     (NEC PC98) */
//	public static final int KEY_UNDERLINE     = Key(GLFW.GLFW_KEY_; /*                     (NEC PC98) */
//	public static final int KEY_KANJI         = Key(GLFW.GLFW_KEY_; /* (Japanese keyboard)            */
//	public static final int KEY_STOP          = Key(GLFW.GLFW_KEY_; /*                     (NEC PC98) */
//	public static final int KEY_AX            = Key(GLFW.GLFW_KEY_; /*                     (Japan AX) */
//	public static final int KEY_UNLABELED     = Key(GLFW.GLFW_KEY_; /*                        (J3100) */
	public static final int KEY_NUMPADENTER   = Key(GLFW.GLFW_KEY_KP_ENTER); /* Enter on numeric keypad */
	public static final int KEY_RCONTROL      = Key(GLFW.GLFW_KEY_RIGHT_CONTROL);
//	public static final int KEY_NUMPADCOMMA   = Key(GLFW.GLFW_KEY_; /* , on numeric keypad (NEC PC98) */
//	public static final int KEY_DIVIDE        = Key(GLFW.GLFW_KEY_kp; /* / on numeric keypad */
//	public static final int KEY_SYSRQ         = Key(GLFW.GLFW_KEY_;
	public static final int KEY_RMENU         = Key(GLFW.GLFW_KEY_RIGHT_ALT); /* right Alt */
	public static final int KEY_RALT          = Key(KEY_RMENU); /* right Alt */
	public static final int KEY_PAUSE         = Key(GLFW.GLFW_KEY_PAUSE); /* Pause */
	public static final int KEY_HOME          = Key(GLFW.GLFW_KEY_HOME); /* Home on arrow keypad */
	public static final int KEY_UP            = Key(GLFW.GLFW_KEY_UP); /* UpArrow on arrow keypad */
	public static final int KEY_PRIOR         = Key(GLFW.GLFW_KEY_PAGE_UP); /* PgUp on arrow keypad */
	public static final int KEY_LEFT          = Key(GLFW.GLFW_KEY_LEFT); /* LeftArrow on arrow keypad */
	public static final int KEY_RIGHT         = Key(GLFW.GLFW_KEY_RIGHT); /* RightArrow on arrow keypad */
	public static final int KEY_END           = Key(GLFW.GLFW_KEY_END); /* End on arrow keypad */
	public static final int KEY_DOWN          = Key(GLFW.GLFW_KEY_DOWN); /* DownArrow on arrow keypad */
	public static final int KEY_NEXT          = Key(GLFW.GLFW_KEY_PAGE_DOWN); /* PgDn on arrow keypad */
	public static final int KEY_INSERT        = Key(GLFW.GLFW_KEY_INSERT); /* Insert on arrow keypad */
	public static final int KEY_DELETE        = Key(GLFW.GLFW_KEY_DELETE); /* Delete on arrow keypad */
//	public static final int KEY_LMETA         = Key(GLFW.GLFW_KEY_; /* Left Windows/Option key */
//	public static final int KEY_LWIN          = Key(GLFW.GLFW_KEY_; /* Left Windows key */
//	public static final int KEY_RMETA         = Key(GLFW.GLFW_KEY_; /* Right Windows/Option key */
//	public static final int KEY_RWIN          = Key(GLFW.GLFW_KEY_; /* Right Windows key */
//	public static final int KEY_APPS          = Key(GLFW.GLFW_KEY_; /* AppMenu key */
//	public static final int KEY_POWER         = Key(GLFW.GLFW_KEY_;
//	public static final int KEY_SLEEP         = Key(GLFW.GLFW_KEY_;
	
	
//	public class Key
//	{
//		int code;
//		public Key(int keyCode) {code=keyCode;}
//	}
	
	public static int Key(int code)
	{
		keys.add(code);
		return code;
	}
	
	public static final int NUM_KEYCODES = keys.size();
//	private static boolean[] m_lastKeys = new boolean[keys.size()];
	private static HashMap<Integer, Boolean> m_lastKeys = new HashMap<>();
	private static boolean[] m_lastMouse = new boolean[NUM_MOUSEBUTTONS];
	
	public static void Update()
	{
//		for(int i = 0; i < NUM_KEYCODES; i++)
//			m_lastKeys[i] = GetKey(i);
		
//		for(int i = 0; i < NUM_KEYCODES; i++)
		for(int key : keys)
			m_lastKeys.put(key, GetKey(key));
		
		
		for(int i = 0; i < NUM_MOUSEBUTTONS; i++)
			m_lastMouse[i] = GetMouse(i);
		Window.Mouse.Update();
	}
	
	public static boolean GetKey(int keyCode)
	{
		try{
			return GLFW.glfwGetKey(Window.CurrentWindow(), keyCode) == GLFW.GLFW_PRESS;//Keyboard.isKeyDown(keyCode);
		} catch(Exception e)
		{
			return false;
		}
	}
	
	public static boolean GetKeyDown(int keyCode)
	{
		return GetKey(keyCode) && !m_lastKeys.get(keyCode);
	}
	
	public static boolean GetKeyUp(int keyCode)
	{
		return !GetKey(keyCode) && m_lastKeys.get(keyCode);
	}
	
	public static boolean Next()
	{
		for(boolean bool : m_lastKeys.values())
			if(bool) return true;
		return false;
	}
	
	public static boolean GetMouse(int mouseButton)
	{
		return GLFW.glfwGetMouseButton(Window.CurrentWindow(), mouseButton) == GLFW.GLFW_PRESS;//Mouse.isButtonDown(mouseButton);
	}
	
	public static boolean GetMouseDown(int mouseButton)
	{
		return GetMouse(mouseButton) && !m_lastMouse[mouseButton];
	}
	
	public static boolean GetMouseUp(int mouseButton)
	{
		return !GetMouse(mouseButton) && m_lastMouse[mouseButton];
	}
	
	public static Vector2f GetMousePosition()
	{
		return new Vector2f((float)Window.Mouse.getX(), (float)Window.Mouse.getY());
	}
	
	public static void SetMousePosition(Vector2f pos)
	{
		Window.Mouse.setCursorPosition(pos.GetX(), pos.GetY());
	}
	
	public static void SetCursor(boolean enabled)
	{
		Window.Mouse.setGrabbed(!enabled);
	}
}
