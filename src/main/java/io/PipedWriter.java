/*
 * @(#)PipedWriter.java	1.10 98/07/07
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

package java.io;


/**
 * Piped character-output streams.
 *
 * @version 	1.10, 98/07/07
 * @author	Mark Reinhold
 * @since	JDK1.1
 */

public class PipedWriter extends Writer {

    /* REMIND: identification of the read and write sides needs to be
       more sophisticated.  Either using thread groups (but what about
       pipes within a thread?) or using finalization (but it may be a
       long time until the next GC). */
    private PipedReader sink;

    /**
     * Creates a piped writer connected to the specified piped 
     * reader. Data characters written to this stream will then be 
     * available as input from <code>snk</code>.
     *
     * @param      snk   The piped reader to connect to.
     * @exception  IOException  if an I/O error occurs.
     */
    public PipedWriter(PipedReader snk)  throws IOException {
	connect(snk);
    }
    
    /**
     * Creates a piped writer that is not yet connected to a 
     * piped reader. It must be connected to a piped reader, 
     * either by the receiver or the sender, before being used. 
     *
     * @see     java.io.PipedReader#connect(java.io.PipedWriter)
     * @see     java.io.PipedWriter#connect(java.io.PipedReader)
     */
    public PipedWriter() {
    }
    
    /**
     * Connects this piped writer to a receiver. If this object
     * is already connected to some other piped reader, an 
     * <code>IOException</code> is thrown.
     * <p>
     * If <code>snk</code> is an unconnected piped reader and 
     * <code>src</code> is an unconnected piped writer, they may 
     * be connected by either the call:
     * <blockquote><pre>
     * src.connect(snk)</pre></blockquote>
     * or the call:
     * <blockquote><pre>
     * snk.connect(src)</pre></blockquote>
     * The two calls have the same effect.
     *
     * @param      snk   the piped reader to connect to.
     * @exception  IOException  if an I/O error occurs.
     */
    public synchronized void connect(PipedReader snk) throws IOException {
        if (snk == null) {
            throw new NullPointerException();
        } else if (sink != null || snk.connected) {
	    throw new IOException("Already connected");
	}
	sink = snk;
	snk.in = -1;
	snk.out = 0;
        snk.connected = true;
    }

    /**
     * Writes the specified <code>char</code> to the piped output stream. 
     * If a thread was reading data characters from the connected piped input 
     * stream, but the thread is no longer alive, then an 
     * <code>IOException</code> is thrown.
     * <p>
     * Implements the <code>write</code> method of <code>Writer</code>.
     *
     * @param      c   the <code>char</code> to be written.
     * @exception  IOException  if an I/O error occurs.
     */
    public void write(int c)  throws IOException {
        if (sink == null) {
            throw new IOException("Pipe not connected");
        }
	sink.receive(c);
    }

    /**
     * Writes <code>len</code> characters from the specified character array 
     * starting at offset <code>off</code> to this piped output stream. 
     * If a thread was reading data characters from the connected piped input 
     * stream, but the thread is no longer alive, then an 
     * <code>IOException</code> is thrown.
     *
     * @param      cbuf  the data.
     * @param      off   the start offset in the data.
     * @param      len   the number of characters to write.
     * @exception  IOException  if an I/O error occurs.
     */
    public void write(char cbuf[], int off, int len) throws IOException {
        if (sink == null) {
            throw new IOException("Pipe not connected");
        } else if ((off < 0) || (off > cbuf.length) || (len < 0) ||
		   ((off + len) > cbuf.length) || ((off + len) < 0)) {
	    throw new IndexOutOfBoundsException();
	}
	sink.receive(cbuf, off, len);
    }

    /**
     * Flushes this output stream and forces any buffered output characters 
     * to be written out. 
     * This will notify any readers that characters are waiting in the pipe.
     *
     * @exception IOException if an I/O error occurs.
     */
    public synchronized void flush() throws IOException {
	if (sink != null) {
            synchronized (sink) {
                sink.notifyAll();
            }
	}
    }

    /**
     * Closes this piped output stream and releases any system resources 
     * associated with this stream. This stream may no longer be used for 
     * writing characters.
     *
     * @exception  IOException  if an I/O error occurs.
     */
    public void close()  throws IOException {
	if (sink != null) {
	    sink.receivedLast();
	}
    }
}
