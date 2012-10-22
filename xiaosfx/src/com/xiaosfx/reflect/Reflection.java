/*
 * Copyright (c) 2012, Alex Sam Lou
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 	1.	Redistributions of source code must retain the above copyright notice, this
 * 		list of conditions and the following disclaimer.
 * 
 * 	2.	Redistributions in binary form must reproduce the above copyright notice,
 * 		this list of conditions and the following disclaimer in the documentation
 * 		and/or other materials provided with the distribution.
 * 
 * 	3.	Neither the name of the XiaoS!Fx nor the names of its contributors may
 * 		be used to endorse or promote products derived from this software without
 * 		specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.xiaosfx.reflect;

import java.lang.reflect.Field;
import java.util.Vector;

import com.xiaosfx.annotation.XComponent;
import com.xiaosfx.annotation.XContainer;

/**
 * This class provides specific reflection methods for XLayout class<br>
 * Do not use it, if you're only building a GUI<br><br>
 * 
 * Visit: <a href="http://www.xiaosfx.com">http://www.xiaosfx.com</a>
 * for more informations.
 * @author Alex Sam Lou
 * @version 1.0 - October 11, 2012
 */
public final class Reflection {
	/**
	 * Returns all the fields with XComponent annotation found in Class c.
	 */
	public static Field[] getComponents(Class<?> c) {
		Field declaredFields[] = c.getDeclaredFields();
		Vector<Field> components = new Vector<Field>();
		for ( int i = 0; i < declaredFields.length; i++ )
			if ( declaredFields[i].isAnnotationPresent(XComponent.class) )
				components.add( declaredFields[i] );
		Field fields[] = (Field[]) components.toArray(new Field[components.size()]);
		return fields;
	}

	/**
	 * Returns all the fields with XContainer annotation found in Class c.
	 */
	public static Field[] getContainers(Class<?> c) {
		Field declaredFields[] = c.getDeclaredFields();
		Vector<Field> components = new Vector<Field>();
		for ( int i = 0; i < declaredFields.length; i++ )
			if ( declaredFields[i].isAnnotationPresent(XContainer.class) )
				components.add( declaredFields[i] );
		return (Field[]) components.toArray(new Field[components.size()]);
	}
	
	/**
	 * Returns the field instance from object
	 */
	public static Object getFieldInstanceFromObject(Field field, Object object) {
		try {
			return field.get( object );
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
