/*
 * @(#)WindowPeer.java	1.10 00/02/02
 *
 * Copyright 1995-2000 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */

package java.awt.peer;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * The peer interfaces are intended only for use in porting
 * the AWT. They are not intended for use by application
 * developers, and developers should not implement peers
 * nor invoke any of the peer methods directly on the peer
 * instances.
 */
public interface WindowPeer extends ContainerPeer {
    void toFront();
    void toBack();

    int handleFocusTraversalEvent(KeyEvent e);
    public final static int IGNORE_EVENT = 0;
    public final static int CONSUME_EVENT = 1;
    public final static int FOCUS_NEXT = 2;
    public final static int FOCUS_PREVIOUS = 3;
}

