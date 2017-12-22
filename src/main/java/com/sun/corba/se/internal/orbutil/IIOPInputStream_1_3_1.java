/** 
 * @(#)IIOPInputStream_1_3_1.java	1.1 01/06/26
 *
 * Copyright 2001 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of Sun Microsystems, Inc.
 * Use is subject to license terms.
 *
 */
package com.sun.corba.se.internal.orbutil;

import java.io.*;
import java.util.Hashtable;

/**
 * Implements legacy behavior from Ladybird to maintain
 * backwards compatibility.
 */
public class IIOPInputStream_1_3_1 extends com.sun.corba.se.internal.io.IIOPInputStream
{
    public IIOPInputStream_1_3_1()
    	throws java.io.IOException {
        super();
    }

    /**
     * Before JDK 1.3.1_01, the PutField/GetField implementation
     * actually sent a Hashtable.
     */
    public ObjectInputStream.GetField readFields()
    	throws IOException, ClassNotFoundException, NotActiveException {

	Hashtable fields = (Hashtable)readObject();
	return new LegacyHookGetFields(fields);
    }
}
