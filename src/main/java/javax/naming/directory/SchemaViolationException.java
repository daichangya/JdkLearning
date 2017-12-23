/*
 * @(#)SchemaViolationException.java	1.4 00/02/02
 *
 * Copyright 1999, 2000 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */


package javax.naming.directory;

import javax.naming.NamingException;

/**
  * This exception is thrown when a method
  * in some ways violates the schema. An example of schema violation
  * is modifying attributes of an object that violates the object's
  * schema definition. Another example is renaming or moving an object 
  * to a part of the namespace that violates the namespace's 
  * schema definition.
  * <p>
  * Synchronization and serialization issues that apply to NamingException
  * apply directly here.
  *
  * @author Rosanna Lee
  * @author Scott Seligman
  * @version 1.4 00/02/02
  * 
  * @see javax.naming.Context#bind
  * @see DirContext#bind
  * @see javax.naming.Context#rebind
  * @see DirContext#rebind
  * @see DirContext#createSubcontext
  * @see javax.naming.Context#createSubcontext
  * @see DirContext#modifyAttributes
  * @since 1.3
  */
public class SchemaViolationException extends NamingException {
    /**
     * Constructs a new instance of SchemaViolationException.
     * All fields are set to null.
     */
    public SchemaViolationException() {
	super();
    }

    /**
     * Constructs a new instance of SchemaViolationException
     * using the explanation supplied. All other fields are set to null. 
     * @param explanation Detail about this exception. Can be null.
     * @see java.lang.Throwable#getMessage
     */
    public SchemaViolationException(String explanation) {
	super(explanation);
    }

    /**
     * Use serialVersionUID from JNDI 1.1.1 for interoperability
     */
    private static final long serialVersionUID = -3041762429525049663L;
}
