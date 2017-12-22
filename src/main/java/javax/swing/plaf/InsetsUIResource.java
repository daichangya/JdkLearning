/*
 * @(#)InsetsUIResource.java	1.10 01/12/03
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.swing.plaf;

import java.awt.Insets;
import javax.swing.plaf.UIResource;


/*
 * A subclass of Insets that implements UIResource.  UI
 * classes that use Insets values for default properties
 * should use this class.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 * 
 * @see javax.swing.plaf.UIResource
 * @version 1.10 12/03/01
 * @author Amy Fowler
 * 
 */
public class InsetsUIResource extends Insets implements UIResource
{
    public InsetsUIResource(int top, int left, int bottom, int right) {
        super(top, left, bottom, right);
    }
}
