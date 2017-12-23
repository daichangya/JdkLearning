/*
 * @(#)BasicPanelUI.java	1.6 00/02/02
 *
 * Copyright 1998-2000 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */

package javax.swing.plaf.basic;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import java.awt.*;
import java.awt.event.*;


/**
 * BasicPanel implementation
 *
 * @version 1.6 02/02/00
 * @author Steve Wilson
 */
public class BasicPanelUI extends PanelUI {

    // Shared UI object
    private static PanelUI panelUI;

    public static ComponentUI createUI(JComponent c) {
	if(panelUI == null) {
            panelUI = new BasicPanelUI();
	}
        return panelUI;
    }

    public void installUI(JComponent c) {
        JPanel p = (JPanel)c;
        super.installUI(p);
        installDefaults(p);
    }

    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);

    }

    protected void installDefaults(JPanel p) {
        LookAndFeel.installColorsAndFont(p,
					 "Panel.background",
					 "Panel.foreground",
					 "Panel.font");
        LookAndFeel.installBorder(p,"Panel.border");
    }

    protected void uninstallDefaults(JPanel p) {
        LookAndFeel.uninstallBorder(p);
    }
}
