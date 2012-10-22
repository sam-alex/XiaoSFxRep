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

package com.xiaosfx;

import java.lang.reflect.Field;

import com.xiaosfx.annotation.XComponent;
import com.xiaosfx.annotation.XContainer;

/**
 * Provides specific sorting methods for the XLayout class.<br>
 * Do not use it, if you're only building a GUI<br><br>
 * 
 * Visit: <a href="http://www.xiaosfx.com">http://www.xiaosfx.com</a>
 * for more informations.
 * @author Alex Sam Lou
 * @version 1.0 - October 11, 2012
 */
public final class XUtilities {
	
	/**
	 * Sorts the Fields, so that the containers comes first.
	 * This is made so that the XCanvasShaper can build it correctly.
	 */
	public static void sortComponents(Field fields[]) {
		int k = 0;
		for ( int i = 0; i < fields.length; i++ ) {
			if ( fields[i].isAnnotationPresent(XContainer.class) ) {
				Field fieldAux = fields[i];
				fields[i] = fields[k];
				fields[k] = fieldAux;
				k++;
			}
		}
	}
	
	/**
	 * Sorts the Fields (again), this time, sorting between containers.
	 * The top-most level containers should come first.
	 */
	public static void sortContainersInComponents(Field fields[]) {
		boolean ready = false;
		while ( !ready ) {
			ready = true;
			for ( int i = 1; i < fields.length; i++ ) {
				XComponent xcomponent = fields[i].getAnnotation( XComponent.class );
				XContainer xcontainer = null;
				if ( fields[i - 1].isAnnotationPresent(XContainer.class) )
					xcontainer = fields[i - 1].getAnnotation( XContainer.class );
				if ( xcontainer != null ) {
					if ( !xcomponent.owner().equals(xcontainer.name()) ) {
						Field fieldAux = fields[i];
						fields[i] = fields[i - 1];
						fields[i - 1] = fieldAux;
						ready = false;
					}
				}
				else
					break;
				
			}
		}
	}

}
