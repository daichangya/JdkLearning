/*
 * @(#)CompletionStatus.java	1.10 98/10/11
 *
 * Copyright 1996-1998 by Sun Microsystems, Inc.,
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
 * An object that indicates whether a method had completed running
 * when a <code>SystemException</code> was thrown.
 * <P>
 * The class <code>CompletionStatus</code>
 * contains three <code>CompletionStatus</code> instances, which are constants
 * representing each
 * possible completion status: <code>COMPLETED_MAYBE</code>,
 * <code>COMPLETED_NO</code>, and <code>COMPLETED_YES</code>.
 * It also contains
 * three <code>int</code> members, each a constant corresponding to one of
 * the <code>CompletionStatus</code> instances.  These <code>int</code>
 * members make it possible to use a <code>switch</code> statement.
 * <P>
 * The class also contains two methods:
 * <UL>
 * <LI><code>public int <bold>value</bold>()</code> -- which accesses the
 * <code>value</code> field of a <code>CompletionStatus</code> object
 * <LI><code>public static CompletionStatus
 * <bold>from_int</bold>(int i)</code> --
 * for creating an instance from one of the <code>int</code> members
 * </UL>
 * @version %I, %G
 * @see     org.omg.CORBA.SystemException
 * @since   JDK1.2
 */

public class CompletionStatus implements org.omg.CORBA.portable.IDLEntity
{
/**
 * The constant indicating that a method completed running
 * before a <code>SystemException</code> was thrown.
 */
    public static final int _COMPLETED_YES = 0,

/**
 * The constant indicating that a method had not completed running
 * when a <code>SystemException</code> was thrown.
 */
                            _COMPLETED_NO = 1,

/**
 * The constant indicating that it is unknown whether a method had
 * completed running when a <code>SystemException</code> was thrown.
 */
                            _COMPLETED_MAYBE = 2;


/**
 * An instance of <code>CompletionStatus</code> initialized with
 * the constant <code>_COMPLETED_YES</code>.
 */
    public static final CompletionStatus COMPLETED_YES   = new CompletionStatus(_COMPLETED_YES);

/**
 * An instance of <code>CompletionStatus</code> initialized with
 * the constant <code>_COMPLETED_NO</code>.
 */
    public static final CompletionStatus COMPLETED_NO    = new CompletionStatus(_COMPLETED_NO);

/**
 * An instance of <code>CompletionStatus</code> initialized with
 * the constant <code>_COMPLETED_MAYBE</code>.
 */
    public static final CompletionStatus COMPLETED_MAYBE = new CompletionStatus(_COMPLETED_MAYBE);

/**
 * Retrieves the value of this <code>CompletionStatus</code> object.
 *
 * @return  one of the possible <code>CompletionStatus</code> values:
 *          <code>_COMPLETED_YES</code>, <code>_COMPLETED_NO</code>, or
 *          <code>_COMPLETED_MAYBE</code>
 *
 */
    public int value() { return _value; }

/**
 * Creates a <code>CompletionStatus</code> object from the given <code>int</code>.
 *
 * @param i  one of <code>_COMPLETED_YES</code>, <code>_COMPLETED_NO</code>, or
 *          <code>_COMPLETED_MAYBE</code>
 *
 * @exception org.omg.CORBA.BAD_PARAM  if the argument given is not one of the
 *            <code>int</code> constants defined in <code>CompletionStatus</code>
 */
    public static CompletionStatus from_int(int i)  throws  org.omg.CORBA.BAD_PARAM {
	switch (i) {
	case _COMPLETED_YES:
	    return COMPLETED_YES;
	case _COMPLETED_NO:
	    return COMPLETED_NO;
	case _COMPLETED_MAYBE:
	    return COMPLETED_MAYBE;
	default:
	    throw new org.omg.CORBA.BAD_PARAM();
	}
    }


    protected CompletionStatus(int _value) {
	this._value = _value;
    }

    private int _value;
}
