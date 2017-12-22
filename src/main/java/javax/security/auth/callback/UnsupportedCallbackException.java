/*
 * @(#)UnsupportedCallbackException.java	1.8 01/12/03
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.security.auth.callback;

/**
 * Signals that a <code>CallbackHandler</code> does not
 * recognize a particular <code>Callback</code>.
 *
 * @version 1.8, 12/03/01
 */
public class UnsupportedCallbackException extends Exception {

    /**
     * @serial
     */
    private Callback callback;

    /**
     * Constructs a <code>UnsupportedCallbackException</code>
     * with no detail message.
     *
     * <p>
     *
     * @param callback the unrecognized <code>Callback</code>.
     */
    public UnsupportedCallbackException(Callback callback) {
	super();
	this.callback = callback;
    }

    /**
     * Constructs a UnsupportedCallbackException with the specified detail
     * message.  A detail message is a String that describes this particular
     * exception.
     *
     * <p>
     *
     * @param callback the unrecognized <code>Callback</code>. <p>
     *
     * @param msg the detail message.
     */
    public UnsupportedCallbackException(Callback callback, String msg) {
	super(msg);
	this.callback = callback;
    }

    /**
     * Get the unrecognized <code>Callback</code>.
     *
     * <p>
     *
     * @return the unrecognized <code>Callback</code>.
     */
    public Callback getCallback() {
	return callback;
    }
}
