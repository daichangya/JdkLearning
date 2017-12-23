/*
 * @(#)Dimension2D.java	1.5 98/05/05
 *
 * Copyright 1997, 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package java.awt.geom;

/**
 * The <code>Dimension2D</code> class is to encapsulate a width 
 * and a height dimension.
 * <p>
 * This class is only the abstract superclass for all objects that
 * store a 2D dimension.
 * The actual storage representation of the sizes is left to
 * the subclass.
 * 
 * @version 10 Feb 1997
 * @author	Jim Graham
 */
public abstract class Dimension2D implements Cloneable {
    /**
     * This is an abstract class that cannot be instantiated directly.
     * Type-specific implementation subclasses are available for
     * instantiation and provide a number of formats for storing
     * the information necessary to satisfy the various accessor
     * methods below.
     *
     * @see java.awt.Dimension
     */
    protected Dimension2D() {
    }

    /**
     * Returns the width of this <code>Dimension</code> in double 
     * precision.
     * @return the width of this <code>Dimension</code>.
     */
    public abstract double getWidth();

    /**
     * Returns the height of this <code>Dimension</code> in double 
     * precision.
     * @return the height of this <code>Dimension</code>.
     */
    public abstract double getHeight();

    /**
     * Sets the size of this <code>Dimension</code> object to the 
     * specified width and height.
     * This method is included for completeness, to parallel the
     * {@link java.awt.Component#getSize getSize} method of 
     * {@link java.awt.Component}.
     * @param width  the new width for the <code>Dimension</code>
     * object
     * @param height  the new height for the <code>Dimension</code> 
     * object
     */
    public abstract void setSize(double width, double height);

    /**
     * Sets the size of this <code>Dimension2D</code> object to 
     * match the specified size.
     * This method is included for completeness, to parallel the
     * <code>getSize</code> method of <code>Component</code>.
     * @param d  the new size for the <code>Dimension2D</code>
     * object
     */
    public void setSize(Dimension2D d) {
	setSize(d.getWidth(), d.getHeight());
    }

    /**
     * Creates a new object of the same class as this object.
     *
     * @return     a clone of this instance.
     * @exception  OutOfMemoryError            if there is not enough memory.
     * @see        java.lang.Cloneable
     * @since      JDK1.2
     */
    public Object clone() {
	try {
	    return super.clone();
	} catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
}
