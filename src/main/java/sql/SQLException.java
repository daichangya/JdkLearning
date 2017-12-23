/*
 * @(#)SQLException.java	1.16 98/09/29
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

package java.sql;

/**
 * <P>An exception that provides information on a database access
 * error.
 *
 * <P>Each <code>SQLException</code> provides several kinds of information: 
 * <UL>
 *   <LI> a string describing the error.  This is used as the Java Exception
 *       message, available via the method <code>getMesage</code>.
 *   <LI> a "SQLstate" string, which follows the XOPEN SQLstate conventions.
 *       The values of the SQLState string are described in the XOPEN SQL spec.
 *   <LI> an integer error code that is specific to each vendor.  Normally this will
 *	 be the actual error code returned by the underlying database.
 *   <LI> a chain to a next Exception.  This can be used to provide additional
 * 	 error information.
 * </UL>
 */
public class SQLException extends java.lang.Exception {

    /**
     * Constructs a fully-specified <code>SQLException</code> object.  
     *
     * @param reason a description of the exception 
     * @param SQLState an XOPEN code identifying the exception
     * @param vendorCode a database vendor-specific exception code
     */
    public SQLException(String reason, String SQLState, int vendorCode) {
	super(reason);
	this.SQLState = SQLState;
	this.vendorCode = vendorCode;
	if (!(this instanceof SQLWarning)) {
	    if (DriverManager.getLogStream() != null) {
		DriverManager.println("SQLException: SQLState(" + SQLState + 
						") vendor code(" + vendorCode + ")");
		printStackTrace(DriverManager.getLogStream());
	    }
	}
    }


    /**
     * Constructs an <code>SQLException</code> object with a reason and SQLState;
     * vendorCode defaults to 0.
     *
     * @param reason a description of the exception 
     * @param SQLState an XOPEN code identifying the exception 
     */
    public SQLException(String reason, String SQLState) {
	super(reason);
	this.SQLState = SQLState;
	this.vendorCode = 0;
	if (!(this instanceof SQLWarning)) {
	    if (DriverManager.getLogStream() != null) {
		printStackTrace(DriverManager.getLogStream());
		DriverManager.println("SQLException: SQLState(" + SQLState + ")");
	    }
	}
    }

    /**
     * Constructs an <code>SQLException</code> object with a reason;
     * SQLState defaults to null, and vendorCode defaults to 0.
     *
     * @param reason a description of the exception 
     */
    public SQLException(String reason) {
	super(reason);
	this.SQLState = null;
	this.vendorCode = 0;
	if (!(this instanceof SQLWarning)) {
	    if (DriverManager.getLogStream() != null) {
		printStackTrace(DriverManager.getLogStream());
	    }
	}
    }

    /**
     * Constructs an <code>SQLException</code> object;
     * reason defaults to null, SQLState
     * defaults to null, and vendorCode defaults to 0.
     * */
    public SQLException() {
	super();
	this.SQLState = null;
	this.vendorCode = 0;
	if (!(this instanceof SQLWarning)) {
	    if (DriverManager.getLogStream() != null) {
		printStackTrace(DriverManager.getLogStream());
	    }
	}
    }

    /**
     * Retrieves the SQLState for this <code>SQLException</code> object.
     *
     * @return the SQLState value
     */
    public String getSQLState() {
	return (SQLState);
    }	

    /**
     * Retrieves the vendor-specific exception code
	 * for this <code>SQLException</code> object.
     *
     * @return the vendor's error code
     */
    public int getErrorCode() {
	return (vendorCode);
    }

    /**
     * Retrieves the exception chained to this 
	 * <code>SQLException</code> object.
     *
     * @return the next SQLException in the chain; null if none
     */
    public SQLException getNextException() {
	return (next);
    }

    /**
     * Adds an <code>SQLException</code> object to the end of the chain.
     *
     * @param ex the new exception that will be added to the end of
	 *            the SQLException chain
     */
    public synchronized void setNextException(SQLException ex) {
	SQLException theEnd = this;
	while (theEnd.next != null) {
	    theEnd = theEnd.next;
	}
	theEnd.next = ex;
    }

	/**
	 * @serial
	 */
    private String SQLState;

	/**
	 * @serial
	 */
    private int vendorCode;

	/**
	 * @serial
	 */
    private SQLException next;
}
