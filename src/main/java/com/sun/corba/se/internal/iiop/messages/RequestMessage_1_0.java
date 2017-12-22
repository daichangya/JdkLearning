/*
 * @(#)RequestMessage_1_0.java	1.9 01/12/03
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.corba.se.internal.iiop.messages;

import org.omg.CORBA.Principal;
import com.sun.corba.se.internal.core.ServiceContexts;
import com.sun.corba.se.internal.core.GIOPVersion;
import com.sun.corba.se.internal.iiop.ORB;
import com.sun.corba.se.internal.ior.ObjectKey;

/**
 * This implements the GIOP 1.0 Request header.
 *
 * @author Ram Jeyaraman 05/14/2000
 * @version 1.0
 */

public final class RequestMessage_1_0 extends Message_1_0
        implements RequestMessage {

    // Instance variables

    private ORB orb = null;
    private ServiceContexts service_contexts = null;
    private int request_id = (int) 0;
    private boolean response_expected = false;
    private byte[] object_key = null;
    private String operation = null;
    private Principal requesting_principal = null;
    private ObjectKey objectKey = null;

    // Constructor

    RequestMessage_1_0(ORB orb) {
        this.orb = orb;
    }

    RequestMessage_1_0(ORB orb, ServiceContexts _service_contexts,
            int _request_id, boolean _response_expected, byte[] _object_key,
            String _operation, Principal _requesting_principal) {
        super(Message.GIOPBigMagic, false, Message.GIOPRequest, 0);
        this.orb = orb;
        service_contexts = _service_contexts;
        request_id = _request_id;
        response_expected = _response_expected;
        object_key = _object_key;
        operation = _operation;
        requesting_principal = _requesting_principal;
    }

    // Accessor methods (RequestMessage interface)

    public ServiceContexts getServiceContexts() {
        return this.service_contexts;
    }

    public int getRequestId() {
        return this.request_id;
    }

    public boolean isResponseExpected() {
        return this.response_expected;
    }

    public byte[] getReserved() {
        // REVISIT Should we throw an exception or return null ?
        return null;
    }

    public ObjectKey getObjectKey() {
        if (this.objectKey == null) {
	    // this will raise a MARSHAL exception upon errors.
	    this.objectKey = MessageBase.extractObjectKey(object_key, orb);
        }

	return this.objectKey;
    }

    public String getOperation() {
        return this.operation;
    }

    public Principal getPrincipal() {
        return this.requesting_principal;
    }

    // IO methods

    public void read(org.omg.CORBA.portable.InputStream istream) {
        super.read(istream);
        this.service_contexts = new ServiceContexts((org.omg.CORBA_2_3.portable.InputStream) istream, GIOPVersion.V1_0);
        this.request_id = istream.read_ulong();
        this.response_expected = istream.read_boolean();
        int _len0 = istream.read_long();
        this.object_key = new byte[_len0];
        istream.read_octet_array(this.object_key, 0, _len0);
        this.operation = istream.read_string();
        this.requesting_principal = istream.read_Principal();
    }

    public void write(org.omg.CORBA.portable.OutputStream ostream) {
        super.write(ostream);
    	if (this.service_contexts != null) {
	        service_contexts.write(
                (org.omg.CORBA_2_3.portable.OutputStream) ostream,
                GIOPVersion.V1_0);
	    } else {
	        ServiceContexts.writeNullServiceContext(
                (org.omg.CORBA_2_3.portable.OutputStream) ostream);
        }
        ostream.write_ulong(this.request_id);
        ostream.write_boolean(this.response_expected);
        nullCheck(this.object_key);
        ostream.write_long(this.object_key.length);
        ostream.write_octet_array(this.object_key, 0, this.object_key.length);
        ostream.write_string(this.operation);
        if (this.requesting_principal != null) {
            ostream.write_Principal(this.requesting_principal);
        } else {
            ostream.write_long(0);
        }
    }

    public final void callback(com.sun.corba.se.internal.iiop.MessageMediator m)
        throws java.io.IOException
    {
        m.handleInput(this);
    }
} // class RequestMessage_1_0
