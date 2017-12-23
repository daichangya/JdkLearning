/*
 * @(#)WindowsTextFieldUI.java	1.10 98/08/28
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

import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.*;
import javax.swing.*;
import javax.swing.plaf.UIResource;



/**
 * Provides the Windows look and feel for a text field.  This 
 * is basically the following customizations to the default
 * look-and-feel.
 * <ul>
 * <li>The border is beveled (using the standard control color).
 * <li>The background is white by default.
 * <li>The highlight color is a dark color, blue by default.
 * <li>The foreground color is high contrast in the selected
 *  area, white by default.  The unselected foreground is black.
 * <li>The cursor blinks at about 1/2 second intervals.
 * <li>The entire value is selected when focus is gained.
 * <li>Shift-left-arrow and shift-right-arrow extend selection
 * <li>Cntrl-left-arrow and cntrl-right-arrow act like home and 
 *   end respectively.
 * </ul>
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases.  The current serialization support is appropriate
 * for short term storage or RMI between applications running the same
 * version of Swing.  A future release of Swing will provide support for
 * long term persistence.
 *
 * @author  Timothy Prinzing
 * @version 1.10 08/28/98
 */
public class WindowsTextFieldUI extends BasicTextFieldUI
{
    /**
     * Creates a UI for a JTextField.
     *
     * @param c the text field
     * @return the UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new WindowsTextFieldUI();
    }

    /**
     * Creates the caret for a field.
     *
     * @return the caret
     */
    protected Caret createCaret() {
	return new WindowsFieldCaret();
    }

    /**
     * WindowsFieldCaret has different scrolling behavior than
     * DefaultCaret, selects the field when focus enters it, and
     * deselects the field when focus leaves.
     */
    class WindowsFieldCaret extends DefaultCaret implements UIResource {

	public WindowsFieldCaret() {
	    super();
	}

	/**
	 * Called when the component containing the caret gains
	 * focus.  This is implemented to set the caret to visible
	 * if the component is editable, and sets the selection
	 * to visible.
	 *
	 * @param e the focus event
	 * @see FocusListener#focusGained
	 */
        public void focusGained(FocusEvent e) {
	    super.focusGained(e);
	    JTextComponent c = getComponent();
	    Document doc = c.getDocument();
	    setDot(0);
	    moveDot(doc.getLength());
	}

	/**
	 * Called when the component containing the caret loses
	 * focus.  This is implemented to set the caret to visibility
	 * to false, and to set the selection visibility to false.
	 *
	 * @param e the focus event
	 * @see FocusListener#focusLost
	 */
        public void focusLost(FocusEvent e) {
	    setDot(getDot());
	    setVisible(false);
	}

	/**
	 * Adjusts the visibility of the caret according to
	 * the windows feel which seems to be to move the
	 * caret out into the field by about a quarter of
	 * a field length if not visible.
	 */
	protected void adjustVisibility(Rectangle r) {
	    JTextField field = (JTextField) getComponent();
	    BoundedRangeModel vis = field.getHorizontalVisibility();
	    int x = r.x + vis.getValue();
	    int quarterSpan = vis.getExtent() / 4;
	    if (x < vis.getValue()) {
		vis.setValue(x - quarterSpan);
	    } else if (x > vis.getValue() + vis.getExtent()) {
		vis.setValue(x - (3 * quarterSpan));
	    }
	}
    }

}

