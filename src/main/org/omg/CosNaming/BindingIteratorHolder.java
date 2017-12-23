/*
 * @(#)BindingIteratorHolder.java	1.5 98/09/21
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
/*
 * File: ./org/omg/CosNaming/BindingIteratorHolder.java
 * From: nameservice.idl
 * Date: Tue Aug 11 03:12:09 1998
 *   By: idltojava Java IDL 1.2 Aug 11 1998 02:00:18
 */

package org.omg.CosNaming;
public final class BindingIteratorHolder
     implements org.omg.CORBA.portable.Streamable{
    //	instance variable 
    public org.omg.CosNaming.BindingIterator value;
    //	constructors 
    public BindingIteratorHolder() {
	this(null);
    }
    public BindingIteratorHolder(org.omg.CosNaming.BindingIterator __arg) {
	value = __arg;
    }

    public void _write(org.omg.CORBA.portable.OutputStream out) {
        org.omg.CosNaming.BindingIteratorHelper.write(out, value);
    }

    public void _read(org.omg.CORBA.portable.InputStream in) {
        value = org.omg.CosNaming.BindingIteratorHelper.read(in);
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.omg.CosNaming.BindingIteratorHelper.type();
    }
}
