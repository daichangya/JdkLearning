/*
 * @(#)DigestException.java	1.9 98/09/21
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

package java.security;

/**
 * This is the generic Message Digest exception. 
 * 
 * @version 1.9, 00/05/10
 * @author Benjamin Renaud 
 */
public class DigestException extends GeneralSecurityException {

    /** 
     * Constructs a DigestException with no detail message.  (A
     * detail message is a String that describes this particular
     * exception.)  
     */
    public DigestException() {
	super();
    }

    /** 
     * Constructs a DigestException with the specified detail
     * message.  (A detail message is a String that describes this
     * particular exception.)
     *
     * @param msg the detail message.  
     */
   public DigestException(String msg) {
       super(msg);
    }
}
