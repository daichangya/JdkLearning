/*
 * @(#)ClassFormatError.java	1.15 98/09/21
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
 * Thrown when the Java Virtual Machine attempts to read a class 
 * file and determines that the file is malformed or otherwise cannot 
 * be interpreted as a class file. 
 *
 * @author  unascribed
 * @version 1.15, 09/21/98
 * @since   JDK1.0
 */
public
class ClassFormatError extends LinkageError {
    /**
     * Constructs a <code>ClassFormatError</code> with no detail message. 
     */
    public ClassFormatError() {
	super();
    }

    /**
     * Constructs a <code>ClassFormatError</code> with the specified 
     * detail message. 
     *
     * @param   s   the detail message.
     */
    public ClassFormatError(String s) {
	super(s);
    }
}
