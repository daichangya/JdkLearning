/*
 * @(#)ChangeListener.java	1.8 00/02/02
 *
 * Copyright 1997-2000 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */
package javax.swing.event;


import java.util.EventListener;
 
 
/**
 * Defines an object which listens for ChangeEvents.
 *
 * @version 1.8 02/02/00
 * @author Jeff Dinkins
 */
public interface ChangeListener extends EventListener {
    /**
     * Invoked when the target of the listener has changed its state.
     *
     * @param e  a ChangeEvent object
     */
    void stateChanged(ChangeEvent e);
}

