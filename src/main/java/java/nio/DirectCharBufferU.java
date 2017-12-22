/*
 * @(#)Direct-X-Buffer.java	1.37 01/12/03
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// -- This file was mechanically generated: Do not edit! -- //

package java.nio;

import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;
import sun.nio.ch.FileChannelImpl;


class DirectCharBufferU

    extends CharBuffer



    implements DirectBuffer
{



    // Cached unsafe-access object
    protected static final Unsafe unsafe = Bits.unsafe();

    // Cached unaligned-access capability
    protected static final boolean unaligned = Bits.unaligned();

    // Base address, used in all indexing calculations
    // NOTE: moved up to Buffer.java for speed in JNI GetDirectBufferAddress
    //    protected long address;

    // True iff this buffer should be freed
    protected boolean allocated;

    // If this buffer is a view of another buffer then we keep a reference to
    // that buffer so that its memory isn't freed before we're done with it
    protected Object viewedBuffer = null;

    public Object viewedBuffer() {
        return viewedBuffer;
    }




































































    DirectCharBufferU(DirectByteBuffer bb) { 	// package-private

	super(-1, 0,
	      bb.remaining() >> 1,
	      bb.remaining() >> 1);
	// enforce limit == capacity
	int cap = this.capacity();
	this.limit(cap);
	int pos = this.position();
	assert (pos <= cap);
 	address = bb.address() + pos;
	allocated = false;
	viewedBuffer = bb;



    }



    DirectCharBufferU(DirectBuffer db,	        // package-private
			       int mark, int pos, int lim, int cap,
			       int off)
    {

	super(mark, pos, lim, cap);
	address = db.address() + off;
	allocated = false;
	viewedBuffer = db;



    }

    public CharBuffer slice() {
	int pos = this.position();
	int lim = this.limit();
	assert (pos <= lim);
	int rem = (pos <= lim ? lim - pos : 0);
	int off = (pos << 1);
	return new DirectCharBufferU(this, -1, 0, rem, rem, off);
    }

    public CharBuffer duplicate() {
	return new DirectCharBufferU(this,
					      this.markValue(),
					      this.position(),
					      this.limit(),
					      this.capacity(),
					      0);
    }

    public CharBuffer asReadOnlyBuffer() {

	return new DirectCharBufferRU(this,
					   this.markValue(),
					   this.position(),
					   this.limit(),
					   this.capacity(),
					   0);



    }



    public long address() {
	return address;
    }

    private long ix(int i) {
	return address + (i << 1);
    }

    public char get() {
	return (unsafe.getChar(ix(nextGetIndex())));
    }

    public char get(int i) {
	return (unsafe.getChar(ix(checkIndex(i))));
    }



    public CharBuffer put(char x) {

	unsafe.putChar(ix(nextPutIndex()), (x));
	return this;



    }

    public CharBuffer put(int i, char x) {

	unsafe.putChar(ix(checkIndex(i)), (x));
	return this;



    }

    public CharBuffer put(CharBuffer src) {

	if (src instanceof DirectCharBufferU) {
	    if (src == this)
		throw new IllegalArgumentException();
	    DirectCharBufferU sb = (DirectCharBufferU)src;

	    int spos = sb.position();
	    int slim = sb.limit();
	    assert (spos <= slim);
	    int srem = (spos <= slim ? slim - spos : 0);

	    int pos = position();
	    int lim = limit();
	    assert (pos <= lim);
	    int rem = (pos <= lim ? lim - pos : 0);

	    if (srem > rem)
		throw new BufferOverflowException();
 	    unsafe.copyMemory(sb.ix(spos), ix(pos), srem << 1);
 	    sb.position(spos + srem);
 	    position(pos + srem);
	} else {
	    super.put(src);
	}
	return this;



    }

    public CharBuffer compact() {

	int pos = position();
	int lim = limit();
	assert (pos <= lim);
	int rem = (pos <= lim ? lim - pos : 0);

 	unsafe.copyMemory(ix(pos), ix(0), rem << 1);
 	position(rem);
	limit(capacity());
	return this;



    }

    public boolean isDirect() {
	return true;
    }

    public boolean isReadOnly() {
	return false;
    }




    public String toString(int start, int end) {
	if ((end > limit()) || (start > end))
	    throw new IndexOutOfBoundsException();
	try {
	    int len = end - start;
	    char[] ca = new char[len];
	    CharBuffer cb = CharBuffer.wrap(ca);
	    CharBuffer db = this.duplicate();
	    db.position(start);
	    db.limit(end);
	    cb.put(db);
	    return new String(ca);
	} catch (StringIndexOutOfBoundsException x) {
	    throw new IndexOutOfBoundsException();
	}
    }


    // --- Methods to support CharSequence ---

    public CharSequence subSequence(int start, int end) {
	int len = length();
	int pos = position();
	assert (pos <= len);
	pos = (pos <= len ? pos : len);

	if ((start < 0) || (end > len) || (start > end))
	    throw new IndexOutOfBoundsException();
	int sublen = end - start;
 	int off = (pos + start) << 1;
	return new DirectCharBufferU(this, -1, 0, sublen, sublen, off);
    }







    public ByteOrder order() {





	return ((ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN)
		? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);

    }











































}
