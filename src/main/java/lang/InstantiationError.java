/*
 * @(#)InstantiationError.java	1.7 98/09/21
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

package java.lang;

/**
 * Thrown when an application tries to use the Java <code>new</code> 
 * construct to instantiate an abstract class or an interface. 
 * <p>
 * Normally, this error is caught by the compiler; this error can 
 * only occur at run time if the definition of a class has 
 * incompatibly changed. 
 *
 * @author  unascribed
 * @version 1.7, 09/21/98
 * @since   JDK1.0
 */


public
class InstantiationError extends IncompatibleClassChangeError {
    /**
     * Constructs an <code>InstantiationError</code> with no detail  message.
     */
    public InstantiationError() {
	super();
    }

    /**
     * Constructs an <code>InstantiationError</code> with the specified 
     * detail message. 
     *
     * @param   s   the detail message.
     */
    public InstantiationError(String s) {
	super(s);
    }
}
