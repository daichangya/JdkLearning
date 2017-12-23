/*
 * @(#)Clob.java	1.8 98/05/19
 * 
 * Copyright 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package java.sql;
 
/**
 * JDBC 2.0
 * The mapping in the Java<sup><font size=-2>TM</font></sup> programming language 
 * for the SQL <code>CLOB</code> type.
 * An SQL <code>CLOB</code> is a built-in type
 * that stores a Character Large Object as a column value in a row of
 * a database table.
 * The driver implements a <code>Clob</code> object using an SQL
 * <code>locator(CLOB)</code>, which means that a <code>Clob</code> object 
 * contains a logical pointer to the SQL <code>CLOB</code> data rather than
 * the data itself. A <code>Clob</code> object is valid for the duration
 * of the transaction in which it was created.
 * <P>The <code>Clob</code> interface provides methods for getting the
 * length of an SQL <code>CLOB</code> (Character Large Object) value,
 * for materializing a <code>CLOB</code> value on the client, and for
 * searching for a substring or <code>CLOB</code> object within a
 * <code>CLOB</code> value.
 * Methods in the interfaces {@link ResultSet},
 * {@link CallableStatement}, and {@link PreparedStatement}, such as
 * <code>getClob</code> and <code>setClob</code> allow a programmer to
 * access the SQL <code>CLOB</code>.
 */

public interface Clob {

  /**
   * Returns the number of characters 
   * in the <code>CLOB</code> value
   * designated by this <code>Clob</code> object.
   * @return length of the <code>CLOB</code> in characters
   * @exception SQLException if there is an error accessing the
   * length of the <code>CLOB</code>
   */
  long length() throws SQLException;

  /**
   * Returns a copy of the specified substring 
   * in the <code>CLOB</code> value
   * designated by this <code>Clob</code> object.
   * The substring begins at position
   * <code>pos</code> and has up to <code>length</code> consecutive
   * characters.
   * @param pos the first character of the substring to be extracted.
   *            The first character is at position 1.
   * @param length the number of consecutive characters to be copied
   * @return a <code>String</code> that is the specified substring in
   *         the <code>CLOB</code> value designated by this <code>Clob</code> object
   * @exception SQLException if there is an error accessing the
   * <code>CLOB</code> 
   */
  String getSubString(long pos, int length) throws SQLException;

  /**
   * Gets the <code>Clob</code> contents as a Unicode stream.
   * @return a Unicode stream containing the <code>CLOB</code> data
   * @exception SQLException if there is an error accessing the 
   * <code>CLOB</code>
   */
  java.io.Reader getCharacterStream() throws SQLException;

  /**
   * Gets the <code>CLOB</code> value designated by this <code>Clob</code>
   * object as a stream of Ascii bytes.
   * @return an ascii stream containing the <code>CLOB</code> data
   * @exception SQLException if there is an error accessing the 
   * <code>CLOB</code> value
   */
  java.io.InputStream getAsciiStream() throws SQLException;

  /** 
   * Determines the character position at which the specified substring 
   * <code>searchstr</code> appears in the <code>CLOB</code>.  The search 
   * begins at position <code>start</code>.
   * @param searchstr the substring for which to search 
   * @param start the position at which to begin searching; the first position
   *              is 1
   * @return the position at which the substring appears, else -1; the first
   *         position is 1
   * @exception SQLException if there is an error accessing the       
   * <code>CLOB</code> value
   */
  long position(String searchstr, long start) throws SQLException;

  /** 
   * Determines the character position at which the specified  
   * <code>Clob</code> object <code>searchstr</code> appears in this 
   * <code>Clob</code> object.  The search begins at position 
   * <code>start</code>.
   * @param searchstr the <code>Clob</code> object for which to search
   * @param start the position at which to begin searching; the first
   *              position is 1
   * @return the position at which the <code>Clob</code> object appears, 
   * else -1; the first position is 1
   * @exception SQLException if there is an error accessing the 
   * <code>CLOB</code> value
   */
  long position(Clob searchstr, long start) throws SQLException;
}
