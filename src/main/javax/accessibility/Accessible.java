/*
 * @(#)Accessible.java	1.30 98/08/26
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

package javax.accessibility;

/**
 * Interface Accessible is the main interface for the accessibility package. 
 * All components that support
 * the accessibility package must implement this interface.  
 * It contains a single method, {@link #getAccessibleContext}, which  
 * returns an instance of the class {@link AccessibleContext}.
 *
 * @version     1.1 11/24/97 20:34:48
 * @author	Peter Korn
 * @author      Hans Muller
 * @author      Willie Walker
 */
public interface Accessible {

    /**
     * Returns the AccessibleContext associated with this object.  In most
     * cases, the return value should not be null if the object implements
     * interface Accessible.  If a component developer creates a subclass
     * of an object that implements Accessible, and that subclass
     * is not Accessible, the developer should override the 
     * getAccessibleContext method to return null.
     */
    public AccessibleContext getAccessibleContext();
}
