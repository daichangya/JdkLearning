/*
 * @(#)SystemException.java	1.55 01/12/03
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import java.util.*;
import org.omg.CORBA.OMGVMCID;
import com.sun.corba.se.internal.util.SUNVMCID;

/**
 * The root class for all CORBA standard exceptions. These exceptions
 * may be thrown as a result of any CORBA operation invocation and may
 * also be returned by many standard CORBA API methods. The standard
 * exceptions contain a minor code, allowing more detailed specification, and a
 * completion status. This class is subclassed to
 * generate each one of the set of standard ORB exceptions.
 * <code>SystemException</code> extends
 * <code>java.lang.RuntimeException</code>; thus none of the
 * <code>SystemException</code> exceptions need to be
 * declared in signatures of the Java methods mapped from operations in
 * IDL interfaces.
 *
 * @see <A href="../../../../guide/idl/jidlExceptions.html">documentation on
 * Java&nbsp;IDL exceptions</A>
 */

public abstract class SystemException extends java.lang.RuntimeException {

    /**
     * The CORBA Exception minor code.
     * @serial
     */
    public int minor;

    /**
     * The status of the operation that threw this exception.
     * @serial
     */
    public CompletionStatus completed;

    /**
     * Constructs a <code>SystemException</code> exception with the specified detail
     * message, minor code, and completion status.
     * A detail message is a String that describes this particular exception.
     * @param reason the String containing a detail message
     * @param minor the minor code
     * @param completed the completion status
     */
    protected SystemException(String reason, int minor, CompletionStatus completed) {
	super(reason);
	this.minor = minor;
	this.completed = completed;
    }

    /**
     * Converts this exception to a representative string.
     */
    public String toString() {
        // The fully qualified exception class name
	String result = super.toString();

        // The vmcid part
        int vmcid = minor & 0xFFFFF000;
        switch (vmcid) {
            case OMGVMCID.value:
                result += "  vmcid: OMG";
                break;
            case SUNVMCID.value:
                result += "  vmcid: SUN";
                break;
            default:
                result += "  vmcid: 0x" + Integer.toHexString(vmcid);
                break;
        }

        // The minor code part
        int mc = minor & 0x00000FFF;
        result += "  minor code: " + mc;

        // The completion status part
	switch (completed.value()) {
            case CompletionStatus._COMPLETED_YES:
                result += "  completed: Yes";
                break;
            case CompletionStatus._COMPLETED_NO:
                result += "  completed: No";
                break;
            case CompletionStatus._COMPLETED_MAYBE:
            default:
                result += " completed: Maybe";
                break;
	}
        return result;
    }
}
