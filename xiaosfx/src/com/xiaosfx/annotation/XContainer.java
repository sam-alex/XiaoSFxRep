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
 * All the classes that are going to use XLayout and all the containers inside of it<br>
 * should have this annotation.<br><br>
 * When using on a container, you need to set it's layout to null, and also remember<br>
 * to set it's <b>name</b>, because by default it's value = "",<br>
 * this should be done to avoid "conflict" between the class container and this container,<br>
 * unless you've changed the class container's name.<br><br>
 * 
 * <b>Note: </b>The names are <b>Case-Sensitive</b>.<br><br>
 * Visit: <a href="http://www.xiaosfx.com">http://www.xiaosfx.com</a>
 * for more informations.
 * @author Alex Sam Lou
 * @version 1.0 - October 11, 2012
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XContainer {
	/**
	 * @return The left padding of this container.
	 */
	public int paddingLeft() default 0;
	
	/**
	 * @return The right padding of this container.
	 */
	public int paddingRight() default 0;
	
	/**
	 * @return The top padding of this container.
	 */
	public int paddingTop() default 0;
	
	/**
	 * @return The bottom padding of this container.
	 */
	public int paddingBottom() default 0;
	
	/**
	 * Each container has it's own name, which are used to identify
	 * which components belongs to which container.<br>
	 * If by chance two or more container holds the same name, it
	 * may cause an unexpected layout result and possible errors.
	 * <br><br>
	 * 
	 * <b>Note: </b>The names are <b>Case-Sensitive</b>.<br><br>
	 * @return The name of this container
	 */
	public String name() default "";
	
}
