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


class DirectShortBufferS

    extends ShortBuffer



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




































































    DirectShortBufferS(DirectByteBuffer bb) { 	// package-private

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



    DirectShortBufferS(DirectBuffer db,	        // package-private
			       int mark, int pos, int lim, int cap,
			       int off)
    {

	super(mark, pos, lim, cap);
	address = db.address() + off;
	allocated = false;
	viewedBuffer = db;



    }

    public ShortBuffer slice() {
	int pos = this.position();
	int lim = this.limit();
	assert (pos <= lim);
	int rem = (pos <= lim ? lim - pos : 0);
	int off = (pos << 1);
	return new DirectShortBufferS(this, -1, 0, rem, rem, off);
    }

    public ShortBuffer duplicate() {
	return new DirectShortBufferS(this,
					      this.markValue(),
					      this.position(),
					      this.limit(),
					      this.capacity(),
					      0);
    }

    public ShortBuffer asReadOnlyBuffer() {

	return new DirectShortBufferRS(this,
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

    public short get() {
	return Bits.swap(unsafe.getShort(ix(nextGetIndex())));
    }

    public short get(int i) {
	return Bits.swap(unsafe.getShort(ix(checkIndex(i))));
    }



    public ShortBuffer put(short x) {

	unsafe.putShort(ix(nextPutIndex()), Bits.swap(x));
	return this;



    }

    public ShortBuffer put(int i, short x) {

	unsafe.putShort(ix(checkIndex(i)), Bits.swap(x));
	return this;



    }

    public ShortBuffer put(ShortBuffer src) {

	if (src instanceof DirectShortBufferS) {
	    if (src == this)
		throw new IllegalArgumentException();
	    DirectShortBufferS sb = (DirectShortBufferS)src;

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

    public ShortBuffer compact() {

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











































    public ByteOrder order() {

	return ((ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN)
		? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);





    }











































}
