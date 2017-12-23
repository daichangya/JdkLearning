/*
 * @(#)DomainManagerOperations.java	1.3 00/02/02
 *
 * Copyright 1998-2000 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */

package org.omg.CORBA;

/** The DomainManager has associated with it the policy objects for a
 *  particular domain. The domain manager also records the membership of
 *  the domain and provides the means to add and remove members. The domain
 *  manager is itself a member of a domain, possibly the domain it manages.
 *  The domain manager provides mechanisms for establishing and navigating
 *  relationships to superior and subordinate domains and
 *  creating and accessing policies.
 */

public interface DomainManagerOperations extends org.omg.CORBA.Object
{
    /** This returns the policy of the specified type for objects in
     *  this domain.
     */
    public org.omg.CORBA.Policy get_domain_policy(int policy_type);
}

