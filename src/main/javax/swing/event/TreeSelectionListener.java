/*
 * @(#)TreeSelectionListener.java	1.8 98/09/21
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

package javax.swing.event;

import java.util.EventListener;

/**
 * The listener that's notified when the selection in a TreeSelectionModel
 * changes.
 *
 * @see javax.swing.tree.TreeSelectionModel
 * @see javax.swing.JTree
 *
 * @version 1.8 09/21/98
 * @author Scott Violet
 */
public interface TreeSelectionListener extends EventListener
{
    /** 
      * Called whenever the value of the selection changes.
      * @param e the event that characterizes the change.
      */
    void valueChanged(TreeSelectionEvent e);
}
