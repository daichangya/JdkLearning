/*
 * @(#)AppletInitializer.java	1.3 98/09/21
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

package java.beans;

import java.applet.Applet;

import java.beans.beancontext.BeanContext;

/**
 * <p>
 * This interface is designed to work in collusion with java.beans.Beans.instantiate.
 * The interafce is intended to provide mechanism to allow the proper 
 * initialization of JavaBeans that are also Applets, during their
 * instantiation by java.beans.Beans.instantiate().
 * </p>
 *
 * @see java.beans.Bean.instantiate
 *
 * @version 1.3
 * @since JDK1.2
 *
 */


public interface AppletInitializer {

    /**
     * <p>
     * If passed to the appropriate variant of java.beans.Beans.instantiate
     * this method will be called in order to associate the newly instantiated
     * Applet (JavaBean) with its AppletContext, AppletStub, and Container.
     * </p>
     * <p>
     * Conformant implementations shall:
     * <ol>
     * <li> Associate the newly instantiated Applet with the appropriate
     * AppletContext.
     *
     * <li> Instantiate an AppletStub() and associate that AppletStub with
     * the Applet via an invocation of setStub().
     *
     * <li> If BeanContext parameter is null, then it shall associate the
     * Applet with its appropriate Container by adding that Applet to its
     * Container via an invocation of add(). If the BeanContext parameter is
     * non-null, then it is the responsibility of the BeanContext to associate
     * the Applet with its Container during the subsequent invocation of its
     * addChildren() method.
     * </ol>
     * </p>
     *
     * @param The newly instantiated JavaBean
     * @param The BeanContext intended for this Applet, or null.
     */

    void initialize(Applet newAppletBean, BeanContext bCtxt);

    /**
     * <p>
     * Activate, and/or mark Applet active. Implementors of this interface
     * shall mark this Applet as active, and optionally invoke its start()
     * method.
     * </p>
     *
     * @param The newly instantiated JavaBean
     */

    void activate(Applet newApplet);
}
