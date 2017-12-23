/*
 * @(#)Remote.java	1.7 98/09/21
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

package java.rmi;

/** 
 * The <code>Remote</code> interface serves to identify interfaces whose
 * methods may be invoked from a non-local virtual machine.  Any object that
 * is a remote object must directly or indirectly implement this interface.
 * Only those methods specified in a "remote interface", an interface that
 * extends <code>java.rmi.Remote</code> are available remotely.
 *
 * <p>Implementation classes can implement any number of remote interfaces and
 * can extend other remote implementation classes.  RMI provides some
 * convenience classes that remote object implementations can extend which
 * facilitate remote object creation.  These classes are
 * <code>java.rmi.server.UnicastRemoteObject</code> and
 * <code>java.rmi.activation.Activatable</code>.
 *
 * <p>For complete details on RMI, see the <a
 href=http://java.sun.com/products/jdk/1.2/docs/guide/rmi/spec/rmiTOC.doc.html>RMI Specification</a> which describes the RMI API and system</a>.
 *
 * @version 1.7, 09/21/98
 * @since   JDK1.1
 * @author  Ann Wollrath
 * @see	    java.rmi.server.UnicastRemoteObject
 * @see     java.rmi.activation.Activatable
 */
public interface Remote {}
