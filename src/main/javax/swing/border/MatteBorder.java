/*
 * @(#)MatteBorder.java	1.13 98/08/28
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
package javax.swing.border;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Component;
import java.awt.Color;

import javax.swing.Icon;

/**
 * A class which provides a matte-like border of either a solid color 
 * or a tiled icon.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases.  The current serialization support is appropriate
 * for short term storage or RMI between applications running the same
 * version of Swing.  A future release of Swing will provide support for
 * long term persistence.
 *
 * @version 1.13 08/28/98
 * @author Amy Fowler
 */
public class MatteBorder extends EmptyBorder
{
    protected Color color;
    protected Icon tileIcon;

    /**
     * Creates a matte border with the specified insets and color.
     * @param top the top inset of the border
     * @param left the left inset of the border
     * @param bottom the bottom inset of the border
     * @param right the right inset of the border
     */
    public MatteBorder(int top, int left, int bottom, int right, Color color)   {
        super(top, left, bottom, right);
        this.color = color;
    }

    /**
     * Creates a matte border with the specified insets and tile icon.
     * @param top the top inset of the border
     * @param left the left inset of the border
     * @param bottom the bottom inset of the border
     * @param right the right inset of the border
     * @param tileIcon the icon to be used for tiling the border
     */
    public MatteBorder(int top, int left, int bottom, int right, Icon tileIcon)   {
        super(top, left, bottom, right);
        this.tileIcon = tileIcon;
    }

    /**
     * Creates a matte border with the specified tile icon.  The
     * insets will be calculated dynamically based on the size of
     * the tile icon, where the top and bottom will be equal to the
     * tile icon's height, and the left and right will be equal to
     * the tile icon's width.
     * @param tileIcon the icon to be used for tiling the border
     */
    public MatteBorder(Icon tileIcon)   {
        this(-1,-1,-1,-1, tileIcon);
    }

    /**
     * Paints the matte border.
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Insets insets = getBorderInsets(c);
        Color oldColor = g.getColor();
        g.translate(x, y);

        // If the tileIcon failed loading, paint as gray.
        if (tileIcon != null) {
            color = (tileIcon.getIconWidth() == -1) ? Color.gray : null;
        }

        if (color != null) {
            g.setColor(color);
            g.fillRect(0, 0, width - insets.right, insets.top);
            g.fillRect(0, insets.top, insets.left, height - insets.top);
            g.fillRect(insets.left, height - insets.bottom, width - insets.left, insets.bottom);
            g.fillRect(width - insets.right, 0, insets.right, height - insets.bottom);

        } else if (tileIcon != null) {

            int tileW = tileIcon.getIconWidth();
            int tileH = tileIcon.getIconHeight();
            int xpos, ypos, startx, starty;
            Graphics cg;

            // Paint top matte edge
            cg = g.create();
            cg.setClip(0, 0, width, insets.top);
            for (ypos = 0; insets.top - ypos > 0; ypos += tileH) {
                for (xpos = 0; width - xpos > 0; xpos += tileW) {
                    tileIcon.paintIcon(c, cg, xpos, ypos);
                }
            }
            cg.dispose();

            // Paint left matte edge
            cg = g.create();
            cg.setClip(0, insets.top, insets.left, height - insets.top);
            starty = insets.top - (insets.top%tileH);
            startx = 0;
            for (ypos = starty; height - ypos > 0; ypos += tileH) {
                for (xpos = startx; insets.left - xpos > 0; xpos += tileW) {
                    tileIcon.paintIcon(c, cg, xpos, ypos);
                }
            }
            cg.dispose();

            // Paint bottom matte edge
            cg = g.create();
            cg.setClip(insets.left, height - insets.bottom, width - insets.left, insets.bottom);
            starty = (height - insets.bottom) - ((height - insets.bottom)%tileH);
            startx = insets.left - (insets.left%tileW);
            for (ypos = starty; height - ypos > 0; ypos += tileH) {
                for (xpos = startx; width - xpos > 0; xpos += tileW) {
                    tileIcon.paintIcon(c, cg, xpos, ypos);
                }
            }
            cg.dispose();

            // Paint right matte edge
            cg = g.create();
            cg.setClip(width - insets.right, insets.top, insets.right, height - insets.top - insets.bottom);
            starty = insets.top - (insets.top%tileH);
            startx = width - insets.right - ((width - insets.right)%tileW);
            for (ypos = starty; height - ypos > 0; ypos += tileH) {
                for (xpos = startx; width - xpos > 0; xpos += tileW) {
                    tileIcon.paintIcon(c, cg, xpos, ypos);
                }
            }
            cg.dispose();
        }
        g.translate(-x, -y);
        g.setColor(oldColor);

    }

    /**
     * Returns the insets of the border.
     * @param c the component for which this border insets value applies
     */
    public Insets getBorderInsets(Component c) {
        if (tileIcon != null && top == -1 && bottom == -1 && left == -1 && right == -1) {
            int w = tileIcon.getIconWidth();
            int h = tileIcon.getIconHeight();
            return new Insets(h,w,h,w);
        }
        return new Insets(top, left, bottom, right);
    }

    /**
     * Returns whether or not the border is opaque.
     */
    public boolean isBorderOpaque() { 
        // If a tileIcon is set, then it may contain transparent bits
        return color != null; 
    }

}
