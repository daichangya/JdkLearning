/*
 * @(#)KeyPairGeneratorSpi.java	1.4 98/08/07
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
 
package java.security;

import java.security.spec.AlgorithmParameterSpec;

/**
 * <p> This class defines the <i>Service Provider Interface</i> (<b>SPI</b>)
 * for the <code>KeyPairGenerator</code> class, which is used to generate
 * pairs of public and private keys.
 *
 * <p> All the abstract methods in this class must be implemented by each
 * cryptographic service provider who wishes to supply the implementation
 * of a key pair generator for a particular algorithm.
 * 
 * <p> In case the client does not explicitly initialize the KeyPairGenerator
 * (via a call to an <code>initialize</code> method), each provider must
 * supply (and document) a default initialization.
 * For example, the <i>Sun</i> provider uses a default modulus size (keysize)
 * of 1024 bits.
 *
 * @author Benjamin Renaud
 *
 * @version 1.4, 00/05/10
 *
 * @see KeyPairGenerator
 * @see java.security.spec.AlgorithmParameterSpec
 */

public abstract class KeyPairGeneratorSpi {

    /**
     * Initializes the key pair generator for a certain keysize, using
     * the default parameter set.
     *
     * @param keysize the keysize. This is an
     * algorithm-specific metric, such as modulus length, specified in
     * number of bits.
     *
     * @param random the source of randomness for this generator.
     */
    public abstract void initialize(int keysize, SecureRandom random);

    /**
     * Initializes the key pair generator using the specified parameter
     * set and user-provided source of randomness.
     *
     * <p>This concrete method has been added to this previously-defined
     * abstract class. (For backwards compatibility, it cannot be abstract.)
     * It may be overridden by a provider to initialize the key pair 
     * generator. Such an override
     * is expected to throw an InvalidAlgorithmParameterException if
     * a parameter is inappropriate for this key pair generator.
     * If this method is not overridden, it always throws an
     * UnsupportedOperationException.
     *
     * @param params the parameter set used to generate the keys.
     *
     * @param random the source of randomness for this generator.
     *
     * @exception InvalidAlgorithmParameterException if the given parameters
     * are inappropriate for this key pair generator.
     *
     * @since JDK1.2
     */
    public void initialize(AlgorithmParameterSpec params,
		           SecureRandom random)
	throws InvalidAlgorithmParameterException {
	    throw new UnsupportedOperationException();
    }

    /**
     * Generates a key pair. Unless an initialization method is called
     * using a KeyPairGenerator interface, algorithm-specific defaults
     * will be used. This will generate a new key pair every time it
     * is called.
     */
    public abstract KeyPair generateKeyPair();
}
