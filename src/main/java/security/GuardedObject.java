/*
 * @(#)GuardedObject.java	1.8 98/10/27
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

package java.security;

/**
 * A GuardedObject is an object that is used to protect access to
 * another object.
 *
 * <p>A GuardedObject encapsulates a target object and a Guard object,
 * such that access to the target object is possible
 * only if the Guard object allows it.
 * Once an object is encapsulated by a GuardedObject,
 * access to that object is controlled by the <code>getObject</code>
 * method, which invokes the
 * <code>checkGuard</code> method on the Guard object that is
 * guarding access. If access is not allowed,
 * an exception is thrown.
 *
 * @see Guard
 * @see Permission
 *
 * @version 1.8 00/05/10
 * @author Roland Schemers
 * @author Li Gong
 */

public class GuardedObject implements java.io.Serializable {

    private Object object; // the object we are guarding
    private Guard guard;   // the guard

    /**
     * Constructs a GuardedObject using the specified object and guard.
     * If the Guard object is null, then no restrictions will
     * be placed on who can access the object.
     *
     * @param object the object to be guarded.
     *
     * @param guard the Guard object that guards access to the object.
     */

    public GuardedObject(Object object, Guard guard)
    {
        this.guard = guard;
        this.object = object;
    }

    /**
     * Retrieves the guarded object, or throws an exception if access
     * to the guarded object is denied by the guard.
     *
     * @return the guarded object.
     *
     * @exception SecurityException if access to the guarded object is
     * denied.
     */
    public Object getObject()
        throws SecurityException
    {
        if (guard != null)
            guard.checkGuard(object);

        return object;
    }

    /**
     * Writes this object out to a stream (i.e., serializes it).
     * We check the guard if there is one.
     */
    private synchronized void writeObject(java.io.ObjectOutputStream oos)
        throws java.io.IOException
    {
        if (guard != null)
            guard.checkGuard(object);
	
	oos.defaultWriteObject();
    }
}
