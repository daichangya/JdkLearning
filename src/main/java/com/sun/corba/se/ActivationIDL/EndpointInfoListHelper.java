package com.sun.corba.se.ActivationIDL;


/**
* com/sun/corba/se/ActivationIDL/EndpointInfoListHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.1"
* from ../../../../../../src/share/classes/com/sun/corba/se/ActivationIDL/activation.idl
* Wednesday, January 30, 2002 8:23:27 AM PST
*/

abstract public class EndpointInfoListHelper
{
  private static String  _id = "IDL:ActivationIDL/EndpointInfoList:1.0";

  public static void insert (org.omg.CORBA.Any a, com.sun.corba.se.ActivationIDL.EndPointInfo[] that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static com.sun.corba.se.ActivationIDL.EndPointInfo[] extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = com.sun.corba.se.ActivationIDL.EndPointInfoHelper.type ();
      __typeCode = org.omg.CORBA.ORB.init ().create_sequence_tc (0, __typeCode);
      __typeCode = org.omg.CORBA.ORB.init ().create_alias_tc (com.sun.corba.se.ActivationIDL.EndpointInfoListHelper.id (), "EndpointInfoList", __typeCode);
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static com.sun.corba.se.ActivationIDL.EndPointInfo[] read (org.omg.CORBA.portable.InputStream istream)
  {
    com.sun.corba.se.ActivationIDL.EndPointInfo value[] = null;
    int _len0 = istream.read_long ();
    value = new com.sun.corba.se.ActivationIDL.EndPointInfo[_len0];
    for (int _o1 = 0;_o1 < value.length; ++_o1)
      value[_o1] = com.sun.corba.se.ActivationIDL.EndPointInfoHelper.read (istream);
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, com.sun.corba.se.ActivationIDL.EndPointInfo[] value)
  {
    ostream.write_long (value.length);
    for (int _i0 = 0;_i0 < value.length; ++_i0)
      com.sun.corba.se.ActivationIDL.EndPointInfoHelper.write (ostream, value[_i0]);
  }

}
