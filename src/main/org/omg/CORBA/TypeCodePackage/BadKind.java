/*
 * @(#)BadKind.java	1.12 98/08/25
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

package org.omg.CORBA.TypeCodePackage;

/**
 * The exception <code>BadKind</code> is thrown when 
 * an inappropriate operation is invoked on a <code>TypeCode</code> object. For example,
 * invoking the method <code>discriminator_type()</code> on an instance of
 * <code>TypeCode</code> that does not represent an IDL union will cause the
 * exception <code>BadKind</code> to be thrown.
 *
 * @see org.omg.CORBA.TypeCode
 * @version 1.7, 03/18/98
 * @since   JDK1.2
 */

public final class BadKind extends org.omg.CORBA.UserException {
    /**
     * Constructs a <code>BadKind</code> exception with no reason message.
     */
    public BadKind() {
	super();
    }

    /**
     * Constructs a <code>BadKind</code> exception with the specified 
     * reason message.
     * @param reason the String containing a reason message
     */
    public BadKind(String reason) {
	super(reason);
    }
}
