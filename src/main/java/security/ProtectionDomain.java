/*
 * @(#)ProtectionDomain.java	1.24 98/05/11
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

/** 
 *
 * <p> This ProtectionDomain class encapulates the characteristics of
 * a domain, which encloses a set of classes whose instances
 * are granted the same set of permissions.
 * 
 * <p>In addition to a set of permissions, a domain is comprised of a 
 * CodeSource, which is a set of PublicKeys together with a codebase (in 
 * the form of a URL). Thus, classes signed by the same keys and
 * from the same URL are placed in the same domain.
 * Classes that have the same permissions but are from different code
 * sources belong to different domains.
 *
 * <p> A class belongs to one and only one ProtectionDomain.
 * 
 * @version 	1.24, 05/11/98
 * @author Li Gong 
 * @author Roland Schemers
 */

public class ProtectionDomain {

    /* CodeSource */
    private CodeSource codesource ;

    /* the rights this protection domain is granted */
    private PermissionCollection permissions;

    /**
     * Creates a new ProtectionDomain with the given CodeSource and
     * Permissions. If the permissions object is not null, then
     * <code>setReadOnly()</code> will be called on the passed in 
     * Permissions object.
     *
     * @param codesource the codesource associated with this domain
     * @param permissions the permissions granted to this domain
     */
    public ProtectionDomain(CodeSource codesource,
			    PermissionCollection permissions) {
	this.codesource = codesource;
	if (permissions != null) {
	    this.permissions = permissions;
	    this.permissions.setReadOnly();
	}
    }

    /**
     * Returns the CodeSource of this domain.
     * @return the CodeSource of this domain.
     */
    public final CodeSource getCodeSource() {
	return this.codesource;
    }


    /** 
     * Returns the permissions of this domain.
     * @return the permissions of this domain.
     */
    public final PermissionCollection getPermissions() {
	return this.permissions;
    }

    /**
     * Check and see if this ProtectionDomain implies the permissions 
     * expressed in the Permission object.
     *
     * @param permission the Permission object to check.
     *
     * @return true if "permission" is a proper subset of a permission in 
     * this ProtectionDomain, false if not.
     */

    public boolean implies(Permission permission) {
	if (permissions != null) {
	    return permissions.implies(permission);
	} else {
	    return false;
	}
    }

    /**
     * Convert a ProtectionDomain to a String.
     */
    public String toString() {
	return "ProtectionDomain "+codesource+"\n"+permissions+"\n";
    }
}
