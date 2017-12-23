/*
 * @(#)SkeletonNotFoundException.java	1.7 98/09/21
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

package java.rmi.server;

import java.rmi.RemoteException;

/**
 * A <code>SkeletonNotFoundException</code> is thrown if the
 * <code>Skeleton</code> corresponding to the remote object being exported is
 * not found.  Skeletons are not required in JDK1.2, so this exception is
 * never thrown if stubs are generated with <code>rmic -v1.2</code>.  To
 * generate stubs and skeletons compatible with JDK1.1 and JDK1.2, use
 * <code>rmic -vcompat</code> which is the default in JDK1.2.
 * 
 * @version 1.7, 09/21/98
 * @since   JDK1.1
 * @deprecated no replacement.  Skeletons are no longer required for remote
 * method calls in JDK1.2 and greater.
 */
public class SkeletonNotFoundException extends RemoteException {

    /* indicate compatibility with JDK 1.1.x version of class */
    private static final long serialVersionUID = -7860299673822761231L;

    /**
     * Constructs a <code>SkeletonNotFoundException</code> with the specified
     * detail message.
     *
     * @param s the detail message.
     * @since JDK1.1
     */
    public SkeletonNotFoundException(String s) {
	super(s);
    }

    /**
     * Constructs a <code>SkeletonNotFoundException</code> with the specified
     * detail message and nested exception.
     *
     * @param s the detail message.
     * @param ex the nested exception
     * @since JDK1.1
     */
    public SkeletonNotFoundException(String s, Exception ex) {
	super(s, ex);
    }
}
