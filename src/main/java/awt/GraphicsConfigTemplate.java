/*
 * @(#)GraphicsConfigTemplate.java	1.8 98/10/19
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

package java.awt;

import java.awt.Component;
import java.io.*;

/**
 * The <code>GraphicsConfigTemplate</code> class is used to obtain a valid
 * {@link GraphicsConfiguration}.  A user instantiates one of these
 * objects and then sets all non-default attributes as desired.  The
 * {@link GraphicsDevice#getBestConfiguration} method found in the
 * {@link GraphicsDevice} class is then called with this
 * <code>GraphicsConfigTemplate</code>.  A valid 
 * <code>GraphicsConfiguration</code> is returned that meets or exceeds
 * what was requested in the <code>GraphicsConfigTemplate</code>.
 * @see GraphicsDevice
 * @see GraphicsConfiguration
 *
 * @version 	1.2 03/18/98
 * @since       JDK1.2
 */
public abstract class GraphicsConfigTemplate implements Serializable {

    /**
     * This class is an abstract class so only subclasses can be
     * instantiated.
     */
    public GraphicsConfigTemplate() {
    }	 

    /**
     * Value used for "Enum" (Integer) type.  States that this
     * feature is required for the <code>GraphicsConfiguration</code> 
     * object.  If this feature is not available, do not select the
     * <code>GraphicsConfiguration</code> object.
     */
    public static final int REQUIRED	= 1;

    /**
     * Value used for "Enum" (Integer) type.  States that this
     * feature is desired for the <code>GraphicsConfiguration</code> 
     * object.  A selection with this feature is preferred over a
     * selection that does not include this feature, although both
     * selections can be considered valid matches.
     */
    public static final int PREFERRED	= 2;

    /**
     * Value used for "Enum" (Integer) type.  States that this
     * feature is not necessary for the selection of the
     * <code>GraphicsConfiguration</code> object.  A selection
     * without this feature is preferred over a selection that
     * includes this feature since it is not used. 
     */
    public static final int UNNECESSARY	= 3;

    /**
     * Returns the "best" configuration possible that passes the
     * criteria defined in the <code>GraphicsConfigTemplate</code>.
     * @param gc the array of <code>GraphicsConfiguration</code>
     * objects to choose from.
     * @return a <code>GraphicsConfiguration</code> object that is
     * the best configuration possible.
     * @see GraphicsConfiguration
     */
    public abstract GraphicsConfiguration
      getBestConfiguration(GraphicsConfiguration[] gc); 

    /**
     * Returns a <code>boolean</code> indicating whether or 
     * not the specified <code>GraphicsConfiguration</code> can be 
     * used to create a drawing surface that supports the indicated
     * features.
     * @param gc the <code>GraphicsConfiguration</code> object to test
     * @return <code>true</code> if this 
     * <code>GraphicsConfiguration</code> object can be used to create
     * surfaces that support the indicated features; 
     * <code>false</code> if the <code>GraphicsConfiguration</code> can
     * not be used to create a drawing surface usable by this Java(tm)
     * API.
     */
    public abstract boolean
      isGraphicsConfigSupported(GraphicsConfiguration gc);

}

