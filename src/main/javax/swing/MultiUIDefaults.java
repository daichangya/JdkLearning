/*
 * @(#)MultiUIDefaults.java	1.8 98/08/26
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

import java.util.Enumeration;



/**
 * 
 * @version 1.8 08/26/98
 * @author Hans Muller
 */
class MultiUIDefaults extends UIDefaults
{
    private UIDefaults[] tables;

    public MultiUIDefaults(UIDefaults[] defaults) {
	super();
	tables = defaults;
    }

    public MultiUIDefaults() {
	super();
	tables = new UIDefaults[0];
    }


    public Object get(Object key) 
    {
	Object value = super.get(key);
	if (value != null) {
	    return value;
	}

	for(int i = 0; i < tables.length; i++) {
	    UIDefaults table = tables[i];
	    value = (table != null) ? table.get(key) : null;
	    if (value != null) {
		return value;
	    }
	}

	return null;
    }


    public int size() {
	int n = super.size();
	for(int i = 0; i < tables.length; i++) {
	    UIDefaults table = tables[i];
	    n += (table != null) ? table.size() : 0;
	}
	return n;
    }


    public boolean isEmpty() {
	return size() == 0;
    }


    public Enumeration keys() 
    {
	Enumeration[] enums = new Enumeration[1 + tables.length];
	enums[0] = super.keys();
	for(int i = 0; i < tables.length; i++) {
	    UIDefaults table = tables[i];
	    if (table != null) {
		enums[i + 1] = table.keys();
	    }
	}
	return new MultiUIDefaultsEnumerator(enums);
    }


    public Enumeration elements() 
    {
	Enumeration[] enums = new Enumeration[1 + tables.length];
	enums[0] = super.elements();
	for(int i = 0; i < tables.length; i++) {
	    UIDefaults table = tables[i];
	    if (table != null) {
		enums[i + 1] = table.elements();
	    }
	}
	return new MultiUIDefaultsEnumerator(enums);
    }


    private static class MultiUIDefaultsEnumerator implements Enumeration
    {
	Enumeration[] enums;
	int n = 0;

	MultiUIDefaultsEnumerator(Enumeration[] enums) {
	    this.enums = enums;
	}

	public boolean hasMoreElements() {
	    for(int i = n; i < enums.length; i++) {
		Enumeration e = enums[i];
		if ((e != null) && (e.hasMoreElements())) {
		    return true;
		}
	    }
	    return false;
	}

	public Object nextElement() {
	    for(; n < enums.length; n++) {
		Enumeration e = enums[n];
		if ((e != null) && (e.hasMoreElements())) {
		    return e.nextElement();
		}
	    }
	    return null;
	}
    }


    public Object remove(Object key) 
    {
	Object value = super.remove(key);
	if (value != null) {
	    return value;
	}

	for(int i = 0; i < tables.length; i++) {
	    UIDefaults table = tables[i];
	    value = (table != null) ? table.remove(key) : null;
	    if (value != null) {
		return value;
	    }
	}

	return null;
    }


    public void clear() {
	super.clear();
	for(int i = 0; i < tables.length; i++) {
	    UIDefaults table = tables[i];
	    if (table != null) {
		table.clear();
	    }
	}
    }
}
