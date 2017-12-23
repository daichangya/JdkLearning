/*
 * @(#)URLDecoder.java	1.2 98/06/29
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

package java.net;

import java.io.*;

/**
 * The class contains a utility method for converting from
 * a MIME format called "<code>x-www-form-urlencoded</code>"
 * to a <code>String</code>
 * <p>
 * To convert to a <code>String</code>, each character is examined in turn:
 * <ul>
 * <li>The ASCII characters '<code>a</code>' through '<code>z</code>',
 * '<code>A</code>' through '<code>Z</code>', and '<code>0</code>'
 * through '<code>9</code>' remain the same.
 * <li>The plus sign '<code>+</code>'is converted into a
 * space character '<code>&nbsp;</code>'.
 * <li>The remaining characters are represented by 3-character
 * strings which begin with the percent sign,
 * "<code>%<i>xy</i></code>", where <i>xy</i> is the two-digit
 * hexadecimal representation of the lower 8-bits of the character.
 * </ul>
 *
 * @author  Mark Chamness
 * @author  Michael McCloskey
 * @version 1.2 06/29/98
 * @since   JDK1.2
 */

public class URLDecoder {

    public static String decode(String s) throws Exception {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '+':
                    sb.append(' ');
                    break;
                case '%':
                    try {
                        sb.append((char)Integer.parseInt(
                                        s.substring(i+1,i+3),16));
                    }
                    catch (NumberFormatException e) {
                        throw new IllegalArgumentException();
                    }
                    i += 2;
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        // Undo conversion to external encoding
        String result = sb.toString();
        byte[] inputBytes = result.getBytes("8859_1");
        return new String(inputBytes);
    }
}


