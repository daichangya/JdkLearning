/*
 * @(#)BasicLabelUI.java	1.59 98/08/26
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

package javax.swing.plaf.basic;

import javax.swing.*;
import javax.swing.plaf.*;

import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;

/**
 * A Windows L&F implementation of LabelUI.  This implementation 
 * is completely static, i.e. there's only one UIView implementation 
 * that's shared by all JLabel objects.
 *
 * @version 1.59 08/26/98
 * @author Hans Muller
 */
public class BasicLabelUI extends LabelUI implements  PropertyChangeListener
{
    protected static BasicLabelUI labelUI = new BasicLabelUI();

    /**
     * Forwards the call to SwingUtilities.layoutCompoundLabel().
     * This method is here so that a subclass could do Label specific
     * layout and to shorten the method name a little.
     * 
     * @see SwingUtilities#layoutCompoundLabel
     */
    protected String layoutCL(
        JLabel label,                  
        FontMetrics fontMetrics, 
        String text, 
        Icon icon, 
        Rectangle viewR, 
        Rectangle iconR, 
        Rectangle textR)
    {
        return SwingUtilities.layoutCompoundLabel(
            (JComponent) label,
            fontMetrics,
            text,
            icon,
            label.getVerticalAlignment(),
            label.getHorizontalAlignment(),
            label.getVerticalTextPosition(),
            label.getHorizontalTextPosition(),
            viewR,
            iconR,
            textR,
            label.getIconTextGap());
    }

    /**
     * Paint clippedText at textX, textY with the labels foreground color.
     * 
     * @see #paint
     * @see #paintDisabledText
     */
    protected void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY)
    {
        int accChar = l.getDisplayedMnemonic();
        g.setColor(l.getForeground());
        BasicGraphicsUtils.drawString(g, s, accChar, textX, textY);
    }


    /**
     * Paint clippedText at textX, textY with background.lighter() and then 
     * shifted down and to the right by one pixel with background.darker().
     * 
     * @see #paint
     * @see #paintEnabledText
     */
    protected void paintDisabledText(JLabel l, Graphics g, String s, int textX, int textY)
    {
        int accChar = l.getDisplayedMnemonic();
        Color background = l.getBackground();
        g.setColor(background.brighter());
        BasicGraphicsUtils.drawString(g, s, accChar, textX, textY);
        g.setColor(background.darker());
        BasicGraphicsUtils.drawString(g, s, accChar, textX + 1, textY + 1);
    }


    /* These rectangles/insets are allocated once for this shared LabelUI
     * implementation.  Re-using rectangles rather than allocating
     * them in each paint call halved the time it took paint to run.
     */
    private static Rectangle paintIconR = new Rectangle();
    private static Rectangle paintTextR = new Rectangle();
    private static Rectangle paintViewR = new Rectangle();
    private static Insets paintViewInsets = new Insets(0, 0, 0, 0);
    

    /** 
     * Paint the label text in the foreground color, if the label
     * is opaque then paint the entire background with the background
     * color.  The Label text is drawn by paintEnabledText() or
     * paintDisabledText().  The locations of the label parts are computed
     * by layoutCL.
     * 
     * @see #paintEnabledText
     * @see #paintDisabledText
     * @see #layoutCL
     */
    public void paint(Graphics g, JComponent c) 
    {
        JLabel label = (JLabel)c;
        String text = label.getText();
        Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

        if ((icon == null) && (text == null)) {
            return;
        }

        FontMetrics fm = g.getFontMetrics();
        paintViewInsets = c.getInsets(paintViewInsets);

        paintViewR.x = paintViewInsets.left;
        paintViewR.y = paintViewInsets.top;
        paintViewR.width = c.getWidth() - (paintViewInsets.left + paintViewInsets.right);
        paintViewR.height = c.getHeight() - (paintViewInsets.top + paintViewInsets.bottom);

        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

        String clippedText = 
            layoutCL(label, fm, text, icon, paintViewR, paintIconR, paintTextR);

        if (icon != null) {
            icon.paintIcon(c, g, paintIconR.x, paintIconR.y);
        }

        if (text != null) {
            int textX = paintTextR.x;
            int textY = paintTextR.y + fm.getAscent();

            if (label.isEnabled()) {
                paintEnabledText(label, g, clippedText, textX, textY);
            }
            else {
                paintDisabledText(label, g, clippedText, textX, textY);
            }
        }
    }


    /* These rectangles/insets are allocated once for this shared LabelUI
     * implementation.  Re-using rectangles rather than allocating
     * them in each getPreferredSize call sped up the method substantially.
     */
    private static Rectangle iconR = new Rectangle();
    private static Rectangle textR = new Rectangle();
    private static Rectangle viewR = new Rectangle();
    private static Insets viewInsets = new Insets(0, 0, 0, 0);


    public Dimension getPreferredSize(JComponent c) 
    {
        JLabel label = (JLabel)c;
        String text = label.getText();
        Icon icon = label.getIcon();
        Insets insets = label.getInsets(viewInsets);
        Font font = label.getFont();

        int dx = insets.left + insets.right;
        int dy = insets.top + insets.bottom;

        if ((icon == null) && 
            ((text == null) || 
             ((text != null) && (font == null)))) {
            return new Dimension(dx, dy);
        }
        else if ((text == null) || ((icon != null) && (font == null))) {
            return new Dimension(icon.getIconWidth() + dx, 
                                 icon.getIconHeight() + dy);
        }
        else {
            FontMetrics fm = label.getToolkit().getFontMetrics(font);

            iconR.x = iconR.y = iconR.width = iconR.height = 0;
            textR.x = textR.y = textR.width = textR.height = 0;
            viewR.x = dx;
            viewR.y = dy;
            viewR.width = viewR.height = Short.MAX_VALUE;

            layoutCL(label, fm, text, icon, viewR, iconR, textR);
            int x1 = Math.min(iconR.x, textR.x);
            int x2 = Math.max(iconR.x + iconR.width, textR.x + textR.width);
            int y1 = Math.min(iconR.y, textR.y);
            int y2 = Math.max(iconR.y + iconR.height, textR.y + textR.height);
            Dimension rv = new Dimension(x2 - x1, y2 - y1);

            rv.width += dx;
            rv.height += dy;
            return rv;
        }
    }


    /**
     * @return getPreferredSize(c)
     */
    public Dimension getMinimumSize(JComponent c) {
        return getPreferredSize(c);
    }

    /**
     * @return getPreferredSize(c)
     */
    public Dimension getMaximumSize(JComponent c) {
        return getPreferredSize(c);
    }


    public void installUI(JComponent c) { 
        installDefaults((JLabel)c);
        installComponents((JLabel)c);
        installListeners((JLabel)c);
        installKeyboardActions((JLabel)c);
    }

    
    public void uninstallUI(JComponent c) { 
        uninstallDefaults((JLabel)c);
        uninstallComponents((JLabel)c);
        uninstallListeners((JLabel)c);
        uninstallKeyboardActions((JLabel)c);
    }

    protected void installDefaults(JLabel c){
        LookAndFeel.installColorsAndFont(c, "Label.background", "Label.foreground", "Label.font");
    }

    protected void installListeners(JLabel c){
        c.addPropertyChangeListener(this);      
    }

    protected void installComponents(JLabel c){
    }

    protected void installKeyboardActions(JLabel l) {
        int dka = l.getDisplayedMnemonic();
        Component lf = l.getLabelFor();
        l.resetKeyboardActions();
        if ((dka != 0) && (lf != null)) {
            l.registerKeyboardAction(
                    new PressAction(l,lf),
                    KeyStroke.getKeyStroke(dka,ActionEvent.ALT_MASK,false),
                    JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
    }

    protected void uninstallDefaults(JLabel c){
    }

    protected void uninstallListeners(JLabel c){
        c.removePropertyChangeListener(this);
    }

    protected void uninstallComponents(JLabel c){
    }

    protected void uninstallKeyboardActions(JLabel c) {
        c.resetKeyboardActions();
    }

    public static ComponentUI createUI(JComponent c) {
        return labelUI;
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("labelFor") ||
            e.getPropertyName().equals("displayedMnemonic")) {
            installKeyboardActions((JLabel) e.getSource());
        }
    }

    // When the accelerator is pressed, temporarily make the JLabel 
    // focusTraversable by registering a WHEN_FOCUSED action for the
    // release of the accelerator.  Then give it focus so it can 
    // prevent unwanted keyTyped events from getting to other components.
    static class PressAction extends AbstractAction {
        JLabel    owner;
        Component labelFor;

        PressAction(JLabel l, Component c) {
            super("nothing");
            owner = l;
            labelFor = c;
        }

        public void actionPerformed(ActionEvent e) {
            owner.registerKeyboardAction(
                    new ReleaseAction(owner,labelFor),
                    KeyStroke.getKeyStroke(owner.getDisplayedMnemonic(),
                                           ActionEvent.ALT_MASK,true),
                    JComponent.WHEN_FOCUSED);

            // Need this if the accelerator is released before the ALT key
            //
            owner.registerKeyboardAction(
                    new ReleaseAction(owner,labelFor),
                    KeyStroke.getKeyStroke(0,ActionEvent.ALT_MASK,true),
                    JComponent.WHEN_FOCUSED);

            owner.requestFocus();
        }

        public boolean isEnabled() {
            return owner.isEnabled();
        }
    }

    // On the release of the accelerator, remove the keyboard action
    // that allows the label to take focus and then give focus to the
    // labelFor component.
    static class ReleaseAction extends AbstractAction {
        JLabel    owner;
        Component labelFor;

        ReleaseAction(JLabel l, Component c) {
            super("giveFocusToLabelFor");
            owner = l;
            labelFor = c;
        }
        
        public void actionPerformed(ActionEvent e) {
            owner.unregisterKeyboardAction(
                    KeyStroke.getKeyStroke(owner.getDisplayedMnemonic(),
                                           ActionEvent.ALT_MASK,true));
            owner.unregisterKeyboardAction(
                    KeyStroke.getKeyStroke(0,ActionEvent.ALT_MASK,true));
            labelFor.requestFocus();
        }

        public boolean isEnabled() {
            return owner.isEnabled();
        }
    }
}
