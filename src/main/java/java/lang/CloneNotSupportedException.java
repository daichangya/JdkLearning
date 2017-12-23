/*
 * @(#)CloneNotSupportedException.java	1.8 00/02/02
 *
 * Copyright 1995-2000 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */

package java.lang;

/**
 * Thrown to indicate that the <code>clone</code> method in class 
 * <code>Object</code> has been called to clone an object, but that 
 * the object's class does not implement the <code>Cloneable</code> 
 * interface. 
 * <p>
 * Applications that override the <code>clone</code> method can also 
 * throw this exception to indicate that an object could not or 
 * should not be cloned.
 *
 * @author  unascribed
 * @version 1.8, 02/02/00
 * @see     java.lang.Cloneable
 * @see     java.lang.Object#clone()
 * @since   JDK1.0
 */

public
class CloneNotSupportedException extends Exception {
    /**
     * Constructs a <code>CloneNotSupportedException</code> with no 
     * detail message. 
     */
    public CloneNotSupportedException() {
	super();
    }

    /**
     * Constructs a <code>CloneNotSupportedException</code> with the 
     * specified detail message. 
     *
     * @param   s   the detail message.
     */
    public CloneNotSupportedException(String s) {
	super(s);
    }
}
