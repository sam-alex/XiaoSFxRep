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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.lang.reflect.Field;

import com.xiaosfx.annotation.XComponent;
import com.xiaosfx.annotation.XContainer;
import com.xiaosfx.reflect.Reflection;

/**
 * The XLayout lays out a custom container class by arranging and resizing
 * its components, which are identified by the @XComponent Annotation,
 * based on the components annotation values (left, right, top, bottom,
 * horizontalCenter and VerticalCenter constraints, or width and height
 * values).
 * 
 * Only set this layout to a container which <b>class</b> contains the
 * <b>XContainer</b> annotation.<br><br>
 * 
 * Visit: <a href="http://www.xiaosfx.com">http://www.xiaosfx.com</a>
 * for more informations.
 * @author Alex Sam Lou
 * @version 1.0 - October 11, 2012
 */
public class XLayout implements LayoutManager {
	/*---------------------------------------
	 * ATTRIBUTES
	 */
	private boolean firstTime = true;
	
	private Container rootParent = null;
	private Field containers[] = null;
	private Field components[] = null;
	private boolean keepMiniumSize = false;

	/*---------------------------------------
	 * CONSTRUCTORS
	 */
	public XLayout() {
	}
	
	/**
	 * Setting the keepMinimumSize to True will prevent the
	 * components being resized under it's minimumSize.<br><br>
	 * <b>Note: </b> This means, that you won't be able to set 
	 * a custom size that goes under it's minimumSize.
	 * @param keepMinimumSize
	 */
	public XLayout(boolean keepMinimumSize) {
		this.keepMiniumSize = keepMinimumSize;
	}

	/*---------------------------------------
	 * METHODS
	 */
	/*----------------
	 * Methods required by the LayoutManager
	 */
	/**
	 * Method required by the LayoutManager.<br>
	 * currently, does nothing.
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	/**
	 * Method required by the LayoutManager.<br>
	 * @return the minimum size for this layout to hold it's
	 * components properly
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return this.getCustomLayoutSize( parent, true );
	}

	/**
	 * Method required by the LayoutManager.<br>
	 * @return the preferred size for this layout to hold it's
	 * components properly
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return this.getCustomLayoutSize( parent, false );
	}

	/**
	 * Method required by the LayoutManager.<br>
	 * currently, does nothing.
	 */
	@Override
	public void removeLayoutComponent(Component comp) {
	}

	/**
	 * Method required by the LayoutManager.<br>
	 * This method will layout the components.<br>
	 * The first time being executed, it will make a scan on the
	 * "root" class which holds this layout by using Reflection.
	 * This will extract all the components and containers on it.
	 * Check it's constraints and give them proper size and location.
	 */
	@Override
	public void layoutContainer(Container parent) {
		// Verify if it's the first time being shown
		if ( firstTime ) {
			try {
				// if it is, then it should construct the GUI
				this.validateRootParent( parent );
				
				this.constructCanvas();
				
				// After populating rootParent, components and containers,
				// we're ready to layout it
				firstTime = false;
			}
			catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		
    	Insets insets = null;
    	
		Dimension cSize = new Dimension();
		
		// Loops through all the components and set it's bounds accordingly.
		for ( int compCount = 0; compCount < this.components.length; compCount++ ) {
	    	int maxWidth = 0;
	    	int maxHeight = 0;
	    	
			XComponent xcomponent = this.components[compCount].getAnnotation( XComponent.class );
			this.components[compCount].setAccessible( true );
			
			Component objComponent = (Component) Reflection.getFieldInstanceFromObject( this.components[compCount], this.rootParent );
			
			if ( objComponent == null ) break;
			
			XContainer xcontainer = this.rootParent.getClass().getAnnotation( XContainer.class );
			
			if ( xcomponent.owner().equals(xcontainer.name()) ) {
				// Get its bounds based on Top-Container
				insets = parent.getInsets();
		    	maxWidth = parent.getWidth() - insets.left - insets.right;
		    	maxHeight = parent.getHeight() - insets.top - insets.bottom;
		    	
				cSize.width = maxWidth - xcontainer.paddingLeft() - xcontainer.paddingRight();
				cSize.height = maxHeight - xcontainer.paddingTop() - xcontainer.paddingBottom();
				Rectangle bounds = this.getXBounds( xcontainer, cSize, objComponent, xcomponent );
				objComponent.setBounds( bounds );
			}
			else {
				// Get its bounds based on proper its custom container
				for ( int contCount = 0; contCount < this.containers.length; contCount++ ) {
					xcontainer = this.containers[contCount].getAnnotation( XContainer.class );
					if ( xcomponent.owner().equals(xcontainer.name()) ) {
						this.containers[contCount].setAccessible( true );
						
						Container objContainer = (Container) Reflection.getFieldInstanceFromObject( this.containers[contCount], this.rootParent );
						if ( objContainer == null ) break;
						
						insets = objContainer.getInsets();
						maxWidth = objContainer.getWidth() - insets.left - insets.right;
						maxHeight = objContainer.getHeight() - insets.top - insets.bottom;
						
						cSize.width = maxWidth - xcontainer.paddingLeft() - xcontainer.paddingRight();
						cSize.height = maxHeight - xcontainer.paddingTop() - xcontainer.paddingBottom();
						
						Rectangle bounds = this.getXBounds( xcontainer, cSize, objComponent, xcomponent );
						objComponent.setBounds( bounds );
						
						this.containers[contCount].setAccessible( false );
						break;
					}
					
				}
			}
			components[compCount].setAccessible( false );
		}
		
	}

	/*----------------
	 * Custom Methods
	 */
	/**
	 * This will seek for the Root Parent and populate this.rootParent if is valid.
	 * @param parent
	 * @throws Exception If the RootParent doesn't contain the XContainer Annotation
	 */
	private void validateRootParent(Container parent) throws Exception {
    	while ( (parent != null) &&
    			(!parent.getClass().isAnnotationPresent(XContainer.class)) )
    		parent = parent.getParent();
    	if ( parent != null )
    		this.rootParent = parent;
    	else
    		throw new Exception("Annotation XContainer is not present.");
	}
	
	/**
	 * This method will get all the components and containers, and join them.<br>
	 * Call this method only once and only after your GUI components have been all instantiated<br>
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void constructCanvas() throws IllegalArgumentException, IllegalAccessException {
		this.containers = Reflection.getContainers( this.rootParent.getClass() );
		this.components = Reflection.getComponents( this.rootParent.getClass() );
		
		// Sort the components, the containers should be added before components.
		// The top-level-containers should be added before other's containers.
		XUtilities.sortComponents( this.components );
		XUtilities.sortContainersInComponents( this.components );
		
		// Loops through all the components, check which container it belongs to,
		// and insert it.
		for ( int compCount = 0; compCount < this.components.length; compCount++ ) {
			XComponent xcomponent = this.components[compCount].getAnnotation( XComponent.class );
			this.components[compCount].setAccessible( true );
			Component objComponent = (Component) this.components[compCount].get( this.rootParent );
			if ( objComponent.getParent() != null ) {
				// If the objComponent.getParent() != null, it means that this component
				// has already been inserted into a container.
				// So to avoid further problems, such as, a component been added to two
				// or more containers. We'll simply make its parent remove this component
				Container objComponentsContainer = objComponent.getParent();
				objComponentsContainer.remove( objComponent );
			}
			if ( xcomponent.owner().equals("") ) {
				// Belongs to the Root Parent Container
				this.rootParent.add( objComponent );
			}
			else {
				// Belongs to other container
				for ( int contCount = 0; contCount < this.containers.length; contCount++ ) {
					XContainer xcontainer = this.containers[contCount].getAnnotation( XContainer.class );
					if ( xcomponent.owner().equals(xcontainer.name()) ) {
						this.containers[contCount].setAccessible( true );
						Container objContainer = (Container) this.containers[contCount].get( this.rootParent );
						objContainer.add( objComponent );
						this.containers[contCount].setAccessible( false );
						break;
					}
					
				}
			}
			this.components[compCount].setAccessible( false );
		}
		
	}

    /**
     * Returns the calculated bounds with valid sizes for the component.<br>
     * This method will process the XComponent annotation properly.
     */
    private Rectangle getXBounds(XContainer xcontainer, Dimension cSize,
    						Component objComponent, XComponent xcomponent) {
		boolean bHorizCenter = true;
		boolean bVertCenter = true;
		
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		
		// Calculating Horizontally - x  - width -----------------------------------------------
		if ( (xcomponent.left() == XComponent.NULL) || (xcomponent.right() == XComponent.NULL) ) {
			if ( xcomponent.width() != XComponent.NULL )
				width = xcomponent.width();
			else
				width = objComponent.getPreferredSize().width;
		}
		
		// Gets the Left() and Right()
		if ( xcomponent.left() != XComponent.NULL ) {
			x = xcomponent.left();
			bHorizCenter = false;
			if ( xcomponent.right() != XComponent.NULL )
				width = cSize.width - x - xcomponent.right();
			else if ( (x + width) > cSize.width )
				width = cSize.width - x;
		}
		else if ( xcomponent.right() != XComponent.NULL ) {
			if ( (width + xcomponent.right()) > cSize.width )
				width = cSize.width - xcomponent.right();
			else
				x = cSize.width - xcomponent.right() - width;
			bHorizCenter = false;
		}
		
		// Calculating Vertically - y  - height -----------------------------------------------
		if ( (xcomponent.top() == XComponent.NULL) || (xcomponent.bottom() == XComponent.NULL) ) {
			if ( xcomponent.height() != XComponent.NULL )
				height = xcomponent.height();
			else
				height = objComponent.getPreferredSize().height;
		}
		
		// Gets the Top() and Bottom()
		if ( xcomponent.top() != XComponent.NULL ) {
			y = xcomponent.top();
			bVertCenter = false;
			if ( xcomponent.bottom() != XComponent.NULL )
				height = cSize.height - y - xcomponent.bottom();
			else if ( (y + height) > cSize.height )
				height = cSize.height - y;
		}
		else if ( xcomponent.bottom() != XComponent.NULL ) {
			if ( (height + xcomponent.bottom()) > cSize.height )
				height = cSize.height - xcomponent.bottom();
			else
				y = cSize.height - xcomponent.bottom() - height;
			bVertCenter = false;
		}
		
		// Gets the Horizontal Center
		if ( bHorizCenter ) {
			if ( width > cSize.width )
				width = cSize.width;
			else if ( xcomponent.horizontalCenter() != XComponent.NULL )
				x = (cSize.width / 2) - (width / 2) + xcomponent.horizontalCenter();
		}
		
		// Gets the Vertical Center
		if ( bVertCenter ) {
			if ( height > cSize.height )
				height = cSize.height;
			else if ( xcomponent.verticalCenter() != XComponent.NULL )
				y = (cSize.height / 2) - (height / 2) + xcomponent.verticalCenter();
		}
    	
		// Validates X and Y
		x = (x < 0) || (x > cSize.width) ? 0 : x;
		y = (y < 0) || (y > cSize.height) ? 0 : y;
		
		// Correct Paddings if necessary
		x += xcontainer.paddingLeft();
		y += xcontainer.paddingTop();
		
		if ( this.keepMiniumSize ) {
			// Correct The size - do not allow the component be smaller than it's minimum size
			width = width < objComponent.getMinimumSize().width ?
							objComponent.getMinimumSize().width : width;
			
			height = height < objComponent.getMinimumSize().height ?
									objComponent.getMinimumSize().height : height;
		}
		
    	return new Rectangle( x, y, width, height );
    }
	
	private Dimension getPreferredComponentSize(Component component, XComponent xcomponent) {
		return this.getCustomComponentSize( component, xcomponent, false );
	}
	
	private Dimension getMinimumComponentSize(Component component, XComponent xcomponent) {
		return this.getCustomComponentSize( component, xcomponent, true );
	}
	
	private Dimension getCustomComponentSize(Component component,
						XComponent xcomponent, boolean minimumSize) {
		Dimension dimension = new Dimension( 0, 0 );
		
		// check if it should get the HorizontalCenter value
		boolean bHorizCenter = true;
		// check if it should get the VerticalCenter value
		boolean bVertCenter = true;
		
		if ( minimumSize )
			dimension.width = component.getMinimumSize().width;
		else
			dimension.width = component.getPreferredSize().width;
		
		if ( xcomponent.left() != XComponent.NULL ) {
			dimension.width += xcomponent.left();
			bHorizCenter = false;
		}
		if ( xcomponent.right() != XComponent.NULL ) {
			dimension.width += xcomponent.right();
			bHorizCenter = false;
		}
		if ( (bHorizCenter) && (xcomponent.horizontalCenter() != XComponent.NULL) ) {
			dimension.width += xcomponent.horizontalCenter() > 0 ?
					xcomponent.horizontalCenter() : xcomponent.horizontalCenter() * (-1);
		}
		
		if ( minimumSize )
			dimension.height = component.getMinimumSize().height;
		else
			dimension.height = component.getPreferredSize().height;
		
		if ( xcomponent.top() != XComponent.NULL ) {
			dimension.height += xcomponent.top();
			bVertCenter = false;
		}
		if ( xcomponent.bottom() != XComponent.NULL ) {
			dimension.height += xcomponent.bottom();
			bVertCenter = false;
		}
		if ( (bVertCenter) && (xcomponent.verticalCenter() != XComponent.NULL) ) {
			dimension.height += xcomponent.verticalCenter() > 0 ?
					xcomponent.verticalCenter() : xcomponent.verticalCenter() * (-1);
		}
		
		return dimension;
	}
	
	private Dimension getCustomLayoutSize(Container parent, boolean minimumSize) {
		Dimension minLayoutSize = new Dimension( 0, 0 );
		
		for ( int i = 0; i < this.components.length; i++ ) {
			Component objComponent = (Component) Reflection.getFieldInstanceFromObject( this.components[i], this.rootParent );
			XComponent xcomponent = this.components[i].getAnnotation( XComponent.class );
			
			Dimension minCompSize = null;
			
			if ( minimumSize )
				minCompSize = this.getMinimumComponentSize( objComponent, xcomponent );
			else
				minCompSize = this.getPreferredComponentSize( objComponent, xcomponent );
			
			if ( minCompSize.width > minLayoutSize.width )
				minLayoutSize.width = minCompSize.width;
			
			if ( minCompSize.height > minLayoutSize.height )
				minLayoutSize.height = minCompSize.height;
		}
		
		XContainer xcontainer = rootParent.getClass().getAnnotation( XContainer.class );
		
		minLayoutSize.width += xcontainer.paddingLeft() + xcontainer.paddingRight();
		minLayoutSize.height += xcontainer.paddingTop() + xcontainer.paddingBottom();
		return minLayoutSize;
	}
    
}
