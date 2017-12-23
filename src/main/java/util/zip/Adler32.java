/*
 * @(#)Adler32.java	1.19 98/07/24
 *
 * Copyright 1996-1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package java.util.zip;

/**
 * A class that can be used to compute the Adler-32 checksum of a data
 * stream. An Adler-32 checksum is almost as reliable as a CRC-32 but
 * can be computed much faster.
 *
 * @see		Checksum
 * @version 	1.19, 07/24/98
 * @author 	David Connelly
 */
public
class Adler32 implements Checksum {
    private int adler = 1;

    /*
     * Loads the ZLIB library.
     */
    static {
	java.security.AccessController.doPrivileged(
		  new sun.security.action.LoadLibraryAction("zip"));
    }

    /**
     * Creates a new Adler32 class.
     */
    public Adler32() {
    }
   

    /**
     * Updates checksum with specified byte.
     */
    public void update(int b) {
	adler = update(adler, b);
    }

    /**
     * Updates checksum with specified array of bytes.
     */
    public void update(byte[] b, int off, int len) {
	if (b == null) {
	    throw new NullPointerException();
	}
	if (off < 0 || len < 0 || off + len > b.length) {
	    throw new ArrayIndexOutOfBoundsException();
	}
	adler = updateBytes(adler, b, off, len);
    }

    /**
     * Updates checksum with specified array of bytes.
     */
    public void update(byte[] b) {
	adler = updateBytes(adler, b, 0, b.length);
    }

    /**
     * Resets checksum to initial value.
     */
    public void reset() {
	adler = 1;
    }

    /**
     * Returns checksum value.
     */
    public long getValue() {
	return (long)adler & 0xffffffffL;
    }

    private native static int update(int adler, int b);
    private native static int updateBytes(int adler, byte[] b, int off,
					  int len);
}
