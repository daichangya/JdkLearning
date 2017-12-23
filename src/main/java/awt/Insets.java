/*
 * @(#)Insets.java	1.20 98/09/21
 *
 * Copyright 1995-1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package java.awt;

/**
 * An <code>Insets</code> object is a representation of the borders 
 * of a container. It specifies the space that a container must leave 
 * at each of its edges. The space can be a border, a blank space, or 
 * a title. 
 *
 * @version 	1.20, 09/21/98
 * @author 	Arthur van Hoff
 * @author 	Sami Shaio
 * @see         java.awt.LayoutManager
 * @see         java.awt.Container
 * @since       JDK1.0
 */
public class Insets implements Cloneable, java.io.Serializable {

    /**
     * The inset from the top.
     * This value is added to the Top of the rectangle
     * to yield a new location for the Top.
     *
     * @serial
     * @see clone()
     */
    public int top;

    /**
     * The inset from the left.
     * This value is added to the left of the rectangle
     * to yield a new location for the left edge.
     *
     * @serial
     * @see clone()
     */
    public int left;

    /**
     * The inset from the bottom.
     * This value is added to the Bottom of the rectangle
     * to yield a new location for the Bottom.
     *
     * @serial
     * @see clone()
     */
    public int bottom;

    /**
     * The inset from the right.
     * This value is added to the Right of the rectangle
     * to yield a new location for the Right edge.
     *
     * @serial
     * @see clone()
     */
    public int right;

    /*
     * JDK 1.1 serialVersionUID 
     */
    private static final long serialVersionUID = -2272572637695466749L;
  
    static {
        /* ensure that the necessary native libraries are loaded */
	Toolkit.loadLibraries();
	initIDs();
    }

    /**
     * Creates and initializes a new <code>Insets</code> object with the 
     * specified top, left, bottom, and right insets. 
     * @param       top   the inset from the top.
     * @param       left   the inset from the left.
     * @param       bottom   the inset from the bottom.
     * @param       right   the inset from the right.
     */
    public Insets(int top, int left, int bottom, int right) {
	this.top = top;
	this.left = left;
	this.bottom = bottom;
	this.right = right;
    }

    /**
     * Checks whether two insets objects are equal. Two instances 
     * of <code>Insets</code> are equal if the four integer values
     * of the fields <code>top</code>, <code>left</code>, 
     * <code>bottom</code>, and <code>right</code> are all equal.
     * @return      <code>true</code> if the two insets are equal;
     *                          otherwise <code>false</code>.
     * @since       JDK1.1
     */
    public boolean equals(Object obj) {
	if (obj instanceof Insets) {
	    Insets insets = (Insets)obj;
	    return ((top == insets.top) && (left == insets.left) &&
		    (bottom == insets.bottom) && (right == insets.right));
	}
	return false;
    }

    /**
     * Returns a string representation of this <code>Insets</code> object. 
     * This method is intended to be used only for debugging purposes, and 
     * the content and format of the returned string may vary between 
     * implementations. The returned string may be empty but may not be 
     * <code>null</code>.
     * 
     * @return  a string representation of this <code>Insets</code> object.
     */
    public String toString() {
	return getClass().getName() + "[top="  + top + ",left=" + left + ",bottom=" + bottom + ",right=" + right + "]";
    }

    /**
     * Create a copy of this object.
     * @return     a copy of this <code>Insets</code> object.
     */
    public Object clone() { 
	try { 
	    return super.clone();
	} catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
    /**
     * Initialize JNI field and method IDs
     */
    private static native void initIDs();

}
