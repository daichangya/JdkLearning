/*
 * @(#)MetalScrollBarUI.java	1.21 98/08/26
 *
 * Copyright 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package javax.swing.plaf.metal;
 
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.Adjustable;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Insets;
import java.awt.Color;
import java.awt.IllegalComponentStateException;

import java.beans.*;

import javax.swing.*;
import javax.swing.event.*;

import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicScrollBarUI;


/**
 * Implementation of ScrollBarUI for the Metal Look and Feel
 * <p>
 *
 * @version 1.21 08/26/98
 * @author Tom Santos
 * @author Steve Wilson
 */
public class MetalScrollBarUI extends BasicScrollBarUI
{
    private static Color shadowColor;
    private static Color highlightColor;
    private static Color darkShadowColor;
    private static Color thumbColor;
    private static Color thumbShadow;
    private static Color thumbHighlightColor; 


    protected MetalBumps bumps;

    protected MetalScrollButton increaseButton;
    protected MetalScrollButton decreaseButton;

    protected  int scrollBarWidth;

    public static final String FREE_STANDING_PROP = "JScrollBar.isFreeStanding";
    protected boolean isFreeStanding = true;

    public static ComponentUI createUI( JComponent c )
    {
        return new MetalScrollBarUI();
    }

    protected void installDefaults() {
	scrollBarWidth = ((Integer)(UIManager.get( "ScrollBar.width" ))).intValue();
	if (scrollbar.getBackground() == null || scrollbar.getBackground() instanceof UIResource)
	    scrollbar.setBackground(UIManager.getColor("ScrollBar.background"));
	super.installDefaults();
	bumps = new MetalBumps( 10, 10, thumbHighlightColor, thumbShadow, thumbColor );    
    }

    protected void installListeners(){
	super.installListeners();
	((ScrollBarListener)propertyChangeListener).handlePropertyChange( scrollbar.getClientProperty( FREE_STANDING_PROP ) );
    }	

    protected PropertyChangeListener createPropertyChangeListener(){
	return new ScrollBarListener();    
    }

    protected void configureScrollBarColors()
    {
        shadowColor         = UIManager.getColor("ScrollBar.shadow");
        highlightColor      = UIManager.getColor("ScrollBar.highlight");
        darkShadowColor     = UIManager.getColor("ScrollBar.darkShadow");
        thumbColor          = UIManager.getColor("ScrollBar.thumb");
        thumbShadow         = UIManager.getColor("ScrollBar.thumbShadow");
        thumbHighlightColor = UIManager.getColor("ScrollBar.thumbHighlight");


    } 

    public Dimension getPreferredSize( JComponent c )
    {
        if ( scrollbar.getOrientation() == JScrollBar.VERTICAL )
	{
	    return new Dimension( scrollBarWidth, scrollBarWidth * 3 + 10 );
        }
	else  // Horizontal
	{
            return new Dimension( scrollBarWidth * 3 + 10, scrollBarWidth );
        }

    }

    /** Returns the view that represents the decrease view. 
      */
    protected JButton createDecreaseButton( int orientation )
    {
        decreaseButton = new MetalScrollButton( orientation, scrollBarWidth, isFreeStanding );
	return decreaseButton;
    }

    /** Returns the view that represents the increase view. */
    protected JButton createIncreaseButton( int orientation )
    {
        increaseButton =  new MetalScrollButton( orientation, scrollBarWidth, isFreeStanding );
	return increaseButton;
    }

    protected void paintTrack( Graphics g, JComponent c, Rectangle trackBounds )
    {
        g.translate( trackBounds.x, trackBounds.y );

	if ( scrollbar.getOrientation() == JScrollBar.VERTICAL )
	{
	    if ( !isFreeStanding ) {
	        trackBounds.width += 2;
	    }

	    if ( c.isEnabled() ) {
	        g.setColor( darkShadowColor );
		g.drawLine( 0, 0, 0, trackBounds.height - 1 );
		g.drawLine( trackBounds.width - 2, 0, trackBounds.width - 2, trackBounds.height - 1 );
		g.drawLine( 2, trackBounds.height - 1, trackBounds.width - 1, trackBounds.height - 1);
		g.drawLine( 2, 0, trackBounds.width - 2, 0 );

		g.setColor( shadowColor );
		//	g.setColor( Color.red);
		g.drawLine( 1, 1, 1, trackBounds.height - 2 );
		g.drawLine( 1, 1, trackBounds.width - 3, 1 );
		if (scrollbar.getValue() != scrollbar.getMaximum()) {  // thumb shadow
		    int y = thumbRect.y + thumbRect.height - trackBounds.y;
		    g.drawLine( 1, y, trackBounds.width-1, y);
		}
		g.setColor(highlightColor);
		g.drawLine( trackBounds.width - 1, 0, trackBounds.width - 1, trackBounds.height - 1 );
	    } else {
		MetalUtils.drawDisabledBorder(g, 0, 0, trackBounds.width, trackBounds.height );
	    }

	    if ( !isFreeStanding ) {
	        trackBounds.width -= 2;
	    }
	}
	else  // HORIZONTAL
	{
	    if ( !isFreeStanding ) {
	        trackBounds.height += 2;
	    }

	    if ( c.isEnabled() ) {
	        g.setColor( darkShadowColor );
		g.drawLine( 0, 0, trackBounds.width - 1, 0 );  // top
		g.drawLine( 0, 2, 0, trackBounds.height - 2 ); // left
		g.drawLine( 0, trackBounds.height - 2, trackBounds.width - 1, trackBounds.height - 2 ); // bottom
		g.drawLine( trackBounds.width - 1, 2, trackBounds.width - 1, trackBounds.height - 1 ); // right

		g.setColor( shadowColor );
		//	g.setColor( Color.red);
		g.drawLine( 1, 1, trackBounds.width - 2, 1 );  // top
		g.drawLine( 1, 1, 1, trackBounds.height - 3 ); // left
		g.drawLine( 0, trackBounds.height - 1, trackBounds.width - 1, trackBounds.height - 1 ); // bottom
		if (scrollbar.getValue() != scrollbar.getMaximum()) {  // thumb shadow
		    int x = thumbRect.x + thumbRect.width - trackBounds.x;
		    g.drawLine( x, 1, x, trackBounds.height-1);
		}
	    } else {
	        MetalUtils.drawDisabledBorder(g, 0, 0, trackBounds.width, trackBounds.height );
	    }

	    if ( !isFreeStanding ) {
	        trackBounds.height -= 2;
	    }
	}

        g.translate( -trackBounds.x, -trackBounds.y );
    }

    protected void paintThumb( Graphics g, JComponent c, Rectangle thumbBounds )
    {
        if (!c.isEnabled()) {
	    return;
	}

        g.translate( thumbBounds.x, thumbBounds.y );

	if ( scrollbar.getOrientation() == JScrollBar.VERTICAL )
	{
	    if ( !isFreeStanding ) {
	        thumbBounds.width += 2;
	    }

	    g.setColor( thumbColor );
	    g.fillRect( 0, 0, thumbBounds.width - 2, thumbBounds.height - 1 );

	    g.setColor( thumbShadow );
	    g.drawRect( 0, 0, thumbBounds.width - 2, thumbBounds.height - 1 );
	
	    g.setColor( thumbHighlightColor );
	    g.drawLine( 1, 1, thumbBounds.width - 3, 1 );
	    g.drawLine( 1, 1, 1, thumbBounds.height - 2 );

	    bumps.setBumpArea( thumbBounds.width - 6, thumbBounds.height - 7 );
	    bumps.paintIcon( c, g, 3, 4 );

	    if ( !isFreeStanding ) {
	        thumbBounds.width -= 2;
	    }
	}
	else  // HORIZONTAL
	{
	    if ( !isFreeStanding ) {
	        thumbBounds.height += 2;
	    }

	    g.setColor( thumbColor );
	    g.fillRect( 0, 0, thumbBounds.width - 1, thumbBounds.height - 2 );

	    g.setColor( thumbShadow );
	    g.drawRect( 0, 0, thumbBounds.width - 1, thumbBounds.height - 2 );

	    g.setColor( thumbHighlightColor );
	    g.drawLine( 1, 1, thumbBounds.width - 3, 1 );
	    g.drawLine( 1, 1, 1, thumbBounds.height - 3 );

	    bumps.setBumpArea( thumbBounds.width - 7, thumbBounds.height - 6 );
	    bumps.paintIcon( c, g, 4, 3 );

	    if ( !isFreeStanding ) {
	        thumbBounds.height -= 2;
	    }
	}

        g.translate( -thumbBounds.x, -thumbBounds.y );
    }

    protected Dimension getMinimumThumbSize()
    {
        return new Dimension( scrollBarWidth, scrollBarWidth );
    }		

    /**
      * This is overridden only to increase the invalid area.  This
      * ensures that the "Shadow" below the thumb is invalidated
      */
    protected void setThumbBounds(int x, int y, int width, int height)
    {
	/* If the thumbs bounds haven't changed, we're done.
	 */
	if ((thumbRect.x == x) && 
	    (thumbRect.y == y) && 
	    (thumbRect.width == width) && 
	    (thumbRect.height == height)) {
	    return;
	}

	/* Update thumbRect, and repaint the union of x,y,w,h and 
	 * the old thumbRect.
	 */
	int minX = Math.min(x, thumbRect.x);
	int minY = Math.min(y, thumbRect.y);
	int maxX = Math.max(x + width, thumbRect.x + thumbRect.width);
	int maxY = Math.max(y + height, thumbRect.y + thumbRect.height);

	thumbRect.setBounds(x, y, width, height);
	scrollbar.repaint(minX, minY, (maxX - minX)+1, (maxY - minY)+1);
    }



    class ScrollBarListener extends BasicScrollBarUI.PropertyChangeHandler
    {
        public void propertyChange(PropertyChangeEvent e)
	{
	    String name = e.getPropertyName();
	    if ( name.equals( FREE_STANDING_PROP ) )
	    {
	        handlePropertyChange( e.getNewValue() );
	    }
	    else {
	        super.propertyChange( e );
	    }
	}

        public void handlePropertyChange( Object newValue )
	{
	    if ( newValue != null )
	    {
	        boolean temp = ((Boolean)newValue).booleanValue();
		boolean becameFlush = temp == false && isFreeStanding == true;
		boolean becameNormal = temp == true && isFreeStanding == false;
		
		isFreeStanding = temp;

		if ( becameFlush ) {
		    toFlush();
		}
		else if ( becameNormal ) {
		    toFreeStanding();
		}
	    }
	    else
	    {

	        if ( !isFreeStanding ) {
		    isFreeStanding = true;
		    toFreeStanding();
		}
		
		// This commented-out block is used for testing flush scrollbars.
/*
	        if ( isFreeStanding ) {
		    isFreeStanding = false;
		    toFlush();
		}
*/
	    }
	    
	    if ( increaseButton != null )
	    {
	        increaseButton.setFreeStanding( isFreeStanding );
	    }
	    if ( decreaseButton != null )
	    {
	        decreaseButton.setFreeStanding( isFreeStanding );
	    }	    
	}

        protected void toFlush() {
	    scrollBarWidth -= 2;
        }

        protected void toFreeStanding() {
	    scrollBarWidth += 2;
        }
    } // end class ScrollBarListener
}






































































