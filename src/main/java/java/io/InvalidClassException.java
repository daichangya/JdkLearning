/*
 * @(#)InvalidClassException.java	1.17 00/02/02
 *
 * Copyright 1996-2000 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */

package java.io;

/**
 * Thrown when the Serialization runtime detects one of the following
 * problems with a Class.
 * <UL>
 * <LI> The serial version of the class does not match that of the class
 *      descriptor read from the stream
 * <LI> The class contains unknown datatypes
 * <LI> The class does not have an accessible no-arg constructor
 * </UL>
 *
 * @author  unascribed
 * @version 1.17, 02/02/00
 * @since   JDK1.1
 */
public class InvalidClassException extends ObjectStreamException {
    /**
     * Name of the invalid class.
     *
     * @serial Name of the invalid class.
     */
    public String classname;

    /**
     * Report a InvalidClassException for the reason specified.
     *
     * @param reason  String describing the reason for the exception.
     */
    public InvalidClassException(String reason) {
	super(reason);
    }

    /**
     * Constructs an InvalidClassException object.
     *
     * @param cname   a String naming the invalid class.
     * @param reason  a String describing the reason for the exception.
     */
    public InvalidClassException(String cname, String reason) {
	super(reason);
	classname = cname;
    }

    /**
     * Produce the message and include the classname, if present.
     */
    public String getMessage() {
	if (classname == null)
	    return super.getMessage();
	else
	    return classname + "; " + super.getMessage();
    }
}
