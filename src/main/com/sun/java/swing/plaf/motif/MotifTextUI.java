/*
 * @(#)MotifTextUI.java	1.14 98/09/11
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
package com.sun.java.swing.plaf.motif;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.plaf.*;

/**
 * Provides the look and feel features that are common across
 * the Motif/CDE text LAF implementations.  
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases.  The current serialization support is appropriate
 * for short term storage or RMI between applications running the same
 * version of Swing.  A future release of Swing will provide support for
 * long term persistence.
 *
 * @author  Timothy Prinzing
 * @version 1.14 09/11/98
 */
public class MotifTextUI {

    /**
     * Creates the object to use for a caret for all of the Motif
     * text components.  The caret is rendered as an I-beam on Motif.
     *
     * @return the caret object
     */
    public static Caret createCaret() {
	return new MotifCaret();
    }

    /**
     * The motif caret is rendered as an I beam.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases.  The current serialization support is appropriate
     * for short term storage or RMI between applications running the same
     * version of Swing.  A future release of Swing will provide support for
     * long term persistence.
     */
    public static class MotifCaret extends DefaultCaret implements UIResource {

	/**
	 * Damages the area surrounding the caret to cause
	 * it to be repainted.  If paint() is reimplemented,
	 * this method should also be reimplemented.
	 *
	 * @param r  the current location of the caret, does nothing if null
	 * @see #paint
	 */
        protected void damage(Rectangle r) {
	    if (r != null) {
		x = r.x - IBeamOverhang - 1;
		y = r.y;
		width = r.width + (2 * IBeamOverhang) + 3;
		height = r.height;
		repaint();
	    }
	}

	/**
	 * Renders the caret as a vertical line.  If this is reimplemented
	 * the damage method should also be reimplemented as it assumes the
	 * shape of the caret is a vertical line.  Does nothing if isVisible()
         * is false.  The caret color is derived from getCaretColor() if
         * the component has focus, else from getDisabledTextColor().
	 *
	 * @param g the graphics context
	 * @see #damage
	 */
        public void paint(Graphics g) {
	    if(isVisible()) {
		try {
		    JTextComponent c = getComponent();
		    Color fg = c.hasFocus() ? c.getCaretColor() : 
			c.getDisabledTextColor();
		    TextUI mapper = c.getUI();
		    int dot = getDot();
		    Rectangle r = mapper.modelToView(c, dot);
		    int x0 = r.x - IBeamOverhang;
		    int x1 = r.x + IBeamOverhang;
		    int y0 = r.y + 1;
		    int y1 = r.y + r.height - 2;
		    g.setColor(fg);
		    g.drawLine(r.x, y0, r.x, y1);
		    g.drawLine(x0, y0, x1, y0);
		    g.drawLine(x0, y1, x1, y1);
		} catch (BadLocationException e) {
		    // can't render I guess
		    //System.err.println("Can't render caret");
		}
	    }
	}
	
	static final int IBeamOverhang = 2;
    }

    /**
     * Default bindings all keymaps implementing the Motif feel.
     */
    static final JTextComponent.KeyBinding[] defaultBindings = {
	new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 
								    InputEvent.CTRL_MASK),
					     DefaultEditorKit.copyAction),
	new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 
								    InputEvent.SHIFT_MASK),
					     DefaultEditorKit.pasteAction),
	new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 
								    InputEvent.SHIFT_MASK),
					     DefaultEditorKit.cutAction),
	new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 
								    InputEvent.SHIFT_MASK),
					     DefaultEditorKit.selectionBackwardAction),
	new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 
								    InputEvent.SHIFT_MASK),
					     DefaultEditorKit.selectionForwardAction),
    };


}
