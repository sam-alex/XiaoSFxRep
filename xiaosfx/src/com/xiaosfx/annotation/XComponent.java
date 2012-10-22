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

package com.xiaosfx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * All the GUI components that are going to be added to XLayout
 * should have this annotation.<br>
 * <b>Note: Containers are also considered as Components</b><br><br>
 * 
 * Visit: <a href="http://www.xiaosfx.com">http://www.xiaosfx.com</a>
 * for more informations.
 * @author Alex Sam Lou
 * @version 1.0 - October 11, 2012
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XComponent {
	public final static int NULL = -2147483648;
	
	/**
	 * @return The left constraint of the component.
	 */
	public int left() default NULL;

	/**
	 * @return The right constraint of the component.
	 */
	public int right() default NULL;

	/**
	 * @return The top constraint of the component.
	 */
	public int top() default NULL;

	/**
	 * @return The bottom constraint of the component.
	 */
	public int bottom() default NULL;

	/**
	 * This value may be invalidated if:<br>
	 * <b>( left != XComponent.NULL ) || ( right != XComponent.NULL )</b>
	 * 
	 * @return The horizontalCenter constraint of the component.
	 */
	public int horizontalCenter() default NULL;
	
	/**
	 * This value may be invalidated if:<br>
	 * <b>( top != XComponent.NULL ) || ( bottom != XComponent.NULL )</b>
	 * 
	 * @return The verticalCenter constraint of the component.
	 */
	public int verticalCenter() default NULL;

	/**
	 * This value may be invalidated if:<br>
	 * <b>( left != XComponent.NULL ) && ( right != XComponent.NULL )</b>
	 * 
	 * @return The width of the component.
	 */
	public int width() default NULL;

	/**
	 * This value may be invalidated if:<br>
	 * <b>( top != XComponent.NULL ) && ( bottom != XComponent.NULL )</b>
	 * 
	 * @return The height of the component.
	 */
	public int height() default NULL;
	
	/**
	 * Default value = "". If none of the containers has the same exact name,
	 * this component will not be added to the container, and therefore, it
	 * won't be displayed.<br><br>
	 * <b>Note: </b>these names are <b>Case-Sensitive</b>.
	 * @return The name of the container which will contain this component.<br>
	 */
	public String owner() default "";
	
}
