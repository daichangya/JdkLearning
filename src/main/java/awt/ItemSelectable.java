/*
 * @(#)ItemSelectable.java	1.9 98/09/21
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

package java.awt;

import java.awt.event.*;

/**
 * The interface for objects which contain a set of items for
 * which zero or more can be selected.
 *
 * @version 1.9 09/21/98
 * @author Amy Fowler
 */

public interface ItemSelectable {

    /**
     * Returns the selected items or null if no items are selected.
     */
    public Object[] getSelectedObjects();

    /**
     * Add a listener to recieve item events when the state of
     * an item changes.
     * If l is null, no exception is thrown and no action is performed.
     *
     * @param    l the listener to recieve events
     * @see ItemEvent
     */    
    public void addItemListener(ItemListener l);

    /**
     * Removes an item listener.
     * If l is null, no exception is thrown and no action is performed.
     *
     * @param 	l the listener being removed
     * @see ItemEvent
     */ 
    public void removeItemListener(ItemListener l);
}
