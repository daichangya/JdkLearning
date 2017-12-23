/*
 * @(#)Scrollable.java	1.4 98/08/26
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

package javax.swing;

import java.awt.Dimension;
import java.awt.Rectangle;


/** 
 * An interface that provides information to a scrolling container
 * like JScrollPane.  A complex component that's likely to be used 
 * as a viewin a JScrollPane viewport (or other scrolling container) 
 * should implement this interface.
 * 
 * @see JViewport
 * @see JScrollPane
 * @see JScrollBar
 * @version 1.4 08/26/98
 * @author Hans Muller
 */
public interface Scrollable  
{
    /**
     * Returns the preferred size of the viewport for a view component.
     * For example the preferredSize of a JList component is the size
     * required to acommodate all of the cells in its list however the
     * value of preferredScrollableViewportSize is the size required for
     * JList.getVisibleRowCount() rows.   A component without any properties
     * that would effect the viewport size should just return 
     * getPreferredSize() here.
     * 
     * @return The preferredSize of a JViewport whose view is this Scrollable.
     * @see JViewport#getPreferredSize
     */
    Dimension getPreferredScrollableViewportSize();


    /**
     * Components that display logical rows or columns should compute
     * the scroll increment that will completely expose one new row
     * or column, depending on the value of orientation.  Ideally, 
     * components should handle a partially exposed row or column by 
     * returning the distance required to completely expose the item.
     * <p>
     * Scrolling containers, like JScrollPane, will use this method
     * each time the user requests a unit scroll.
     * 
     * @param visibleRect The view area visible within the viewport
     * @param orientation Either SwingConstants.VERTICAL or SwingConstants.HORIZONTAL.
     * @param direction Less than zero to scroll up/left, greater than zero for down/right.
     * @return The "unit" increment for scrolling in the specified direction
     * @see JScrollBar#setUnitIncrement
     */
    int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction);


    /**
     * Components that display logical rows or columns should compute
     * the scroll increment that will completely expose one block
     * of rows or columns, depending on the value of orientation. 
     * <p>
     * Scrolling containers, like JScrollPane, will use this method
     * each time the user requests a block scroll.
     * 
     * @param visibleRect The view area visible within the viewport
     * @param orientation Either SwingConstants.VERTICAL or SwingConstants.HORIZONTAL.
     * @param direction Less than zero to scroll up/left, greater than zero for down/right.
     * @return The "block" increment for scrolling in the specified direction.
     * @see JScrollBar#setBlockIncrement
     */
    int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction);  
    

    /**
     * Return true if a viewport should always force the width of this 
     * Scrollable to match the width of the viewport.  For example a noraml 
     * text view that supported line wrapping would return true here, since it
     * would be undesirable for wrapped lines to disappear beyond the right
     * edge of the viewport.  Note that returning true for a Scrollable
     * whose ancestor is a JScrollPane effectively disables horizontal
     * scrolling.
     * <p>
     * Scrolling containers, like JViewport, will use this method each 
     * time they are validated.  
     * 
     * @return True if a viewport should force the Scrollables width to match its own.
     */
    boolean getScrollableTracksViewportWidth();

    /**
     * Return true if a viewport should always force the height of this 
     * Scrollable to match the height of the viewport.  For example a 
     * columnar text view that flowed text in left to right columns 
     * could effectively disable vertical scrolling by returning
     * true here.
     * <p>
     * Scrolling containers, like JViewport, will use this method each 
     * time they are validated.  
     * 
     * @return True if a viewport should force the Scrollables height to match its own.
     */
    boolean getScrollableTracksViewportHeight();
}
