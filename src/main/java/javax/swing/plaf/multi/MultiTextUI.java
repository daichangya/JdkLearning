/*
 * @(#)MultiTextUI.java	1.32 01/12/03
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javax.swing.plaf.multi;

import java.util.Vector;
import javax.swing.plaf.TextUI;
import java.lang.String;
import javax.swing.text.JTextComponent;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import javax.swing.text.EditorKit;
import javax.swing.text.View;
import javax.swing.plaf.ComponentUI;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.accessibility.Accessible;

/**
 * A multiplexing UI used to combine <code>TextUI</code>s.
 * 
 * <p>This file was automatically generated by AutoMulti.
 *
 * @version 1.32 12/03/01 20:38:43
 * @author  Otto Multey
 */
public class MultiTextUI extends TextUI {

    /**
     * The vector containing the real UIs.  This is populated 
     * in the call to <code>createUI</code>, and can be obtained by calling
     * the <code>getUIs</code> method.  The first element is guaranteed to be the real UI 
     * obtained from the default look and feel.
     */
    protected Vector uis = new Vector();

////////////////////
// Common UI methods
////////////////////

    /**
     * Returns the list of UIs associated with this multiplexing UI.  This 
     * allows processing of the UIs by an application aware of multiplexing 
     * UIs on components.
     */
    public ComponentUI[] getUIs() {
        return MultiLookAndFeel.uisToArray(uis);
    }

////////////////////
// TextUI methods
////////////////////

    /**
     * Invokes the <code>getToolTipText</code> method on each UI handled by this object.
     * 
     * @return the value obtained from the first UI, which is
     * the UI obtained from the default <code>LookAndFeel</code>
     */
    public String getToolTipText(JTextComponent a, Point b) {
        String returnValue = 
            ((TextUI) (uis.elementAt(0))).getToolTipText(a,b);
        for (int i = 1; i < uis.size(); i++) {
            ((TextUI) (uis.elementAt(i))).getToolTipText(a,b);
        }
        return returnValue;
    }

    /**
     * Invokes the <code>modelToView</code> method on each UI handled by this object.
     * 
     * @return the value obtained from the first UI, which is
     * the UI obtained from the default <code>LookAndFeel</code>
     */
    public Rectangle modelToView(JTextComponent a, int b)
            throws BadLocationException {
        Rectangle returnValue = 
            ((TextUI) (uis.elementAt(0))).modelToView(a,b);
        for (int i = 1; i < uis.size(); i++) {
            ((TextUI) (uis.elementAt(i))).modelToView(a,b);
        }
        return returnValue;
    }

    /**
     * Invokes the <code>modelToView</code> method on each UI handled by this object.
     * 
     * @return the value obtained from the first UI, which is
     * the UI obtained from the default <code>LookAndFeel</code>
     */
    public Rectangle modelToView(JTextComponent a, int b, Position.Bias c)
            throws BadLocationException {
        Rectangle returnValue = 
            ((TextUI) (uis.elementAt(0))).modelToView(a,b,c);
        for (int i = 1; i < uis.size(); i++) {
            ((TextUI) (uis.elementAt(i))).modelToView(a,b,c);
        }
        return returnValue;
    }

    /**
     * Invokes the <code>viewToModel</code> method on each UI handled by this object.
     * 
     * @return the value obtained from the first UI, which is
     * the UI obtained from the default <code>LookAndFeel</code>
     */
    public int viewToModel(JTextComponent a, Point b) {
        int returnValue = 
            ((TextUI) (uis.elementAt(0))).viewToModel(a,b);
        for (int i = 1; i < uis.size(); i++) {
            ((TextUI) (uis.elementAt(i))).viewToModel(a,b);
        }
        return returnValue;
    }

    /**
     * Invokes the <code>viewToModel</code> method on each UI handled by this object.
     * 
     * @return the value obtained from the first UI, which is
     * the UI obtained from the default <code>LookAndFeel</code>
     */
    public int viewToModel(JTextComponent a, Point b, Position.Bias[] c) {
        int returnValue = 
            ((TextUI) (uis.elementAt(0))).viewToModel(a,b,c);
        for (int i = 1; i < uis.size(); i++) {
            ((TextUI) (uis.elementAt(i))).viewToModel(a,b,c);
        }
        return returnValue;
    }

    /**
     * Invokes the <code>getNextVisualPositionFrom</code> method on each UI handled by this object.
     * 
     * @return the value obtained from the first UI, which is
     * the UI obtained from the default <code>LookAndFeel</code>
     */
    public int getNextVisualPositionFrom(JTextComponent a, int b, Position.Bias c, int d, Position.Bias[] e)
            throws BadLocationException {
        int returnValue = 
            ((TextUI) (uis.elementAt(0))).getNextVisualPositionFrom(a,b,c,d,e);
        for (int i = 1; i < uis.size(); i++) {
            ((TextUI) (uis.elementAt(i))).getNextVisualPositionFrom(a,b,c,d,e);
        }
        return returnValue;
    }

    /**
     * Invokes the <code>damageRange</code> method on each UI handled by this object.
     */
    public void damageRange(JTextComponent a, int b, int c) {
        for (int i = 0; i < uis.size(); i++) {
            ((TextUI) (uis.elementAt(i))).damageRange(a,b,c);
        }
    }

    /**
     * Invokes the <code>damageRange</code> method on each UI handled by this object.
     */
    public void damageRange(JTextComponent a, int b, int c, Position.Bias d, Position.Bias e) {
        for (int i = 0; i < uis.size(); i++) {
            ((TextUI) (uis.elementAt(i))).damageRange(a,b,c,d,e);
        }
    }

    /**
     * Invokes the <code>getEditorKit</code> method on each UI handled by this object.
     * 
     * @return the value obtained from the first UI, which is
     * the UI obtained from the default <code>LookAndFeel</code>
     */
    public EditorKit getEditorKit(JTextComponent a) {
        EditorKit returnValue = 
            ((TextUI) (uis.elementAt(0))).getEditorKit(a);
        for (int i = 1; i < uis.size(); i++) {
            ((TextUI) (uis.elementAt(i))).getEditorKit(a);
        }
        return returnValue;
    }

    /**
     * Invokes the <code>getRootView</code> method on each UI handled by this object.
     * 
     * @return the value obtained from the first UI, which is
     * the UI obtained from the default <code>LookAndFeel</code>
     */
    public View getRootView(JTextComponent a) {
        View returnValue = 
            ((TextUI) (uis.elementAt(0))).getRootView(a);
        for (int i = 1; i < uis.size(); i++) {
            ((TextUI) (uis.elementAt(i))).getRootView(a);
        }
        return returnValue;
    }

////////////////////
// ComponentUI methods
////////////////////

    /**
     * Invokes the <code>contains</code> method on each UI handled by this object.
     * 
     * @return the value obtained from the first UI, which is
     * the UI obtained from the default <code>LookAndFeel</code>
     */
    public boolean contains(JComponent a, int b, int c) {
        boolean returnValue = 
            ((ComponentUI) (uis.elementAt(0))).contains(a,b,c);
        for (int i = 1; i < uis.size(); i++) {
            ((ComponentUI) (uis.elementAt(i))).contains(a,b,c);
        }
        return returnValue;
    }

    /**
     * Invokes the <code>update</code> method on each UI handled by this object.
     */
    public void update(Graphics a, JComponent b) {
        for (int i = 0; i < uis.size(); i++) {
            ((ComponentUI) (uis.elementAt(i))).update(a,b);
        }
    }

    /**
     * Returns a multiplexing UI instance if any of the auxiliary
     * <code>LookAndFeel</code>s supports this UI.  Otherwise, just returns the 
     * UI object obtained from the default <code>LookAndFeel</code>.
     */
    public static ComponentUI createUI(JComponent a) {
        ComponentUI mui = new MultiTextUI();
        return MultiLookAndFeel.createUIs(mui,
                                          ((MultiTextUI) mui).uis,
                                          a);
    }

    /**
     * Invokes the <code>installUI</code> method on each UI handled by this object.
     */
    public void installUI(JComponent a) {
        for (int i = 0; i < uis.size(); i++) {
            ((ComponentUI) (uis.elementAt(i))).installUI(a);
        }
    }

    /**
     * Invokes the <code>uninstallUI</code> method on each UI handled by this object.
     */
    public void uninstallUI(JComponent a) {
        for (int i = 0; i < uis.size(); i++) {
            ((ComponentUI) (uis.elementAt(i))).uninstallUI(a);
        }
    }

    /**
     * Invokes the <code>paint</code> method on each UI handled by this object.
     */
    public void paint(Graphics a, JComponent b) {
        for (int i = 0; i < uis.size(); i++) {
            ((ComponentUI) (uis.elementAt(i))).paint(a,b);
        }
    }

    /**
     * Invokes the <code>getPreferredSize</code> method on each UI handled by this object.
     * 
     * @return the value obtained from the first UI, which is
     * the UI obtained from the default <code>LookAndFeel</code>
     */
    public Dimension getPreferredSize(JComponent a) {
        Dimension returnValue = 
            ((ComponentUI) (uis.elementAt(0))).getPreferredSize(a);
        for (int i = 1; i < uis.size(); i++) {
            ((ComponentUI) (uis.elementAt(i))).getPreferredSize(a);
        }
        return returnValue;
    }

    /**
     * Invokes the <code>getMinimumSize</code> method on each UI handled by this object.
     * 
     * @return the value obtained from the first UI, which is
     * the UI obtained from the default <code>LookAndFeel</code>
     */
    public Dimension getMinimumSize(JComponent a) {
        Dimension returnValue = 
            ((ComponentUI) (uis.elementAt(0))).getMinimumSize(a);
        for (int i = 1; i < uis.size(); i++) {
            ((ComponentUI) (uis.elementAt(i))).getMinimumSize(a);
        }
        return returnValue;
    }

    /**
     * Invokes the <code>getMaximumSize</code> method on each UI handled by this object.
     * 
     * @return the value obtained from the first UI, which is
     * the UI obtained from the default <code>LookAndFeel</code>
     */
    public Dimension getMaximumSize(JComponent a) {
        Dimension returnValue = 
            ((ComponentUI) (uis.elementAt(0))).getMaximumSize(a);
        for (int i = 1; i < uis.size(); i++) {
            ((ComponentUI) (uis.elementAt(i))).getMaximumSize(a);
        }
        return returnValue;
    }

    /**
     * Invokes the <code>getAccessibleChildrenCount</code> method on each UI handled by this object.
     * 
     * @return the value obtained from the first UI, which is
     * the UI obtained from the default <code>LookAndFeel</code>
     */
    public int getAccessibleChildrenCount(JComponent a) {
        int returnValue = 
            ((ComponentUI) (uis.elementAt(0))).getAccessibleChildrenCount(a);
        for (int i = 1; i < uis.size(); i++) {
            ((ComponentUI) (uis.elementAt(i))).getAccessibleChildrenCount(a);
        }
        return returnValue;
    }

    /**
     * Invokes the <code>getAccessibleChild</code> method on each UI handled by this object.
     * 
     * @return the value obtained from the first UI, which is
     * the UI obtained from the default <code>LookAndFeel</code>
     */
    public Accessible getAccessibleChild(JComponent a, int b) {
        Accessible returnValue = 
            ((ComponentUI) (uis.elementAt(0))).getAccessibleChild(a,b);
        for (int i = 1; i < uis.size(); i++) {
            ((ComponentUI) (uis.elementAt(i))).getAccessibleChild(a,b);
        }
        return returnValue;
    }
}
