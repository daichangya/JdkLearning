/*
 * @(#)FileDialogPeer.java	1.10 00/02/02
 *
 * Copyright 1995-2000 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */

package java.awt.peer;

import java.awt.*;
import java.io.FilenameFilter;

/**
 * The peer interfaces are intended only for use in porting
 * the AWT. They are not intended for use by application
 * developers, and developers should not implement peers
 * nor invoke any of the peer methods directly on the peer
 * instances.
 */
public interface FileDialogPeer extends DialogPeer {
    void setFile(String file);
    void setDirectory(String dir);
    void setFilenameFilter(FilenameFilter filter);
}
