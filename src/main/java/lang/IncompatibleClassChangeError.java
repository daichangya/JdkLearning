/*
 * @(#)IncompatibleClassChangeError.java	1.13 98/09/21
 *
 * Copyright 1994-1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package java.lang;

/**
 * Thrown when an incompatible class change has occurred to some class 
 * definition. The definition of some class, on which the currently 
 * executing method depends, has since changed. 
 *
 * @author  unascribed
 * @version 1.13, 09/21/98
 * @since   JDK1.0
 */
public
class IncompatibleClassChangeError extends LinkageError {
    /**
     * Constructs an <code>IncompatibleClassChangeError</code> with no 
     * detail message. 
     */
    public IncompatibleClassChangeError () {
	super();
    }

    /**
     * Constructs an <code>IncompatibleClassChangeError</code> with the 
     * specified detail message. 
     *
     * @param   s   the detail message.
     */
    public IncompatibleClassChangeError(String s) {
	super(s);
    }
}
