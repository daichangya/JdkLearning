/*
 * @(#)ChangedCharSetException.java	1.5 00/02/02
 *
 * Copyright 1997-2000 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */
package javax.swing.text;

import java.io.IOException;

/**
 * ChangedCharSetException as the name indicates is an exception
 * thrown when the charset is changed.
 *
 * @author Sunita Mani
 * 1.5, 02/02/00
 */
public class ChangedCharSetException extends IOException {

    String charSetSpec;
    boolean charSetKey;

    public ChangedCharSetException(String charSetSpec, boolean charSetKey) {
	this.charSetSpec = charSetSpec;
	this.charSetKey = charSetKey;
    }

    public String getCharSetSpec() {
	return charSetSpec;
    }

    public boolean keyEqualsCharSet() {
	return charSetKey;
    }

}
