/*
 * @(#)BAD_TYPECODE.java	1.22 98/08/27
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

package org.omg.CORBA;

/**
 * The CORBA <code>BAD_TYPECODE</code> exception, which is thrown
 * when the specified typecode is invalid.
 * It contains a minor code, which gives more detailed information about
 * what caused the exception, and a completion status. It may also contain
 * a string describing the exception.
 *
 * @see <A href="../../../../guide/idl/jidlExceptions.html">documentation on
 * Java&nbsp;IDL exceptions</A>
 * @version     1.17, 09/09/97
 * @since       JDK1.2
 */

public final class BAD_TYPECODE extends SystemException {

    /**
     * Constructs a <code>BAD_TYPECODE</code> exception with a default 
	 * minor code of 0 and a completion state of COMPLETED_NO.
     */
    public BAD_TYPECODE() {
        this("");
    }

    /**
     * Constructs a <code>BAD_TYPECODE</code> exception with the specified detail,
     * a minor code of 0, and a completion state of COMPLETED_NO.
	 *
     * @param s the String containing a detail message
     */
    public BAD_TYPECODE(String s) {
        this(s, 0, CompletionStatus.COMPLETED_NO);
    }

    /**
     * Constructs a <code>BAD_TYPECODE</code> exception with the specified
     * minor code and completion status.
     * @param minor the minor code
     * @param completed an instance of <code>CompletionStatus</code> indicating
	 *                  the completion status
     */
    public BAD_TYPECODE(int minor, CompletionStatus completed) {
        this("", minor, completed);
    }

    /**
     * Constructs a <code>BAD_TYPECODE</code> exception with the specified detail
     * message, minor code, and completion status.
     * A detail message is a String that describes this particular exception.
     * @param s the String containing a detail message
     * @param minor the minor code
     * @param completed an instance of <code>CompletionStatus</code> indicating
	 *                  the completion status
     */
    public BAD_TYPECODE(String s, int minor, CompletionStatus completed) {
        super(s, minor, completed);
    }
}
