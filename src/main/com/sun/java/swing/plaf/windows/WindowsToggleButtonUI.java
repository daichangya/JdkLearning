/*
 * @(#)WindowsToggleButtonUI.java	1.15 98/08/28
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

package com.sun.java.swing.plaf.windows;

import javax.swing.plaf.basic.*;
import javax.swing.plaf.*;
import javax.swing.*;

import java.awt.*;



/**
 * A Windows toggle button.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases.  The current serialization support is appropriate
 * for short term storage or RMI between applications running the same
 * version of Swing.  A future release of Swing will provide support for
 * long term persistence.
 *
 * @version 1.15 08/28/98
 * @author Jeff Dinkins
 */
public class WindowsToggleButtonUI extends BasicToggleButtonUI
{
    protected static int dashedRectGapX;
    protected static int dashedRectGapY;
    protected static int dashedRectGapWidth;
    protected static int dashedRectGapHeight;

    protected Color focusColor;
    
    private final static WindowsToggleButtonUI windowsToggleButtonUI = new WindowsToggleButtonUI();

    private boolean defaults_initialized = false;
    
    public static ComponentUI createUI(JComponent b) {
	return windowsToggleButtonUI;
    }

    // ********************************
    //            Defaults
    // ********************************
    protected void installDefaults(AbstractButton b) {
	super.installDefaults(b);
	if(!defaults_initialized) {
	    String pp = getPropertyPrefix();
	    dashedRectGapX = ((Integer)UIManager.get("Button.dashedRectGapX")).intValue();
	    dashedRectGapY = ((Integer)UIManager.get("Button.dashedRectGapY")).intValue();
	    dashedRectGapWidth = ((Integer)UIManager.get("Button.dashedRectGapWidth")).intValue();
	    dashedRectGapHeight = ((Integer)UIManager.get("Button.dashedRectGapHeight")).intValue();
	    focusColor = UIManager.getColor(pp + "focus");
	    defaults_initialized = true;
	}
    }

    protected void uninstallDefaults(AbstractButton b) {
	super.uninstallDefaults(b);
	defaults_initialized = false;
    }
    

    protected Color getFocusColor() {
	return focusColor;
    }
    
    
    // ********************************
    //         Paint Methods
    // ********************************
    protected void paintFocus(Graphics g, AbstractButton b,
			      Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
	g.setColor(getFocusColor());
	BasicGraphicsUtils.drawDashedRect(g, dashedRectGapX, dashedRectGapY,
					  b.getWidth() - dashedRectGapWidth,
					  b.getHeight() - dashedRectGapHeight);
    }

}

