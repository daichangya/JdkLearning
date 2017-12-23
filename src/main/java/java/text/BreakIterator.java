/*
 * @(#)BreakIterator.java	1.13 97/02/06
 *
 * (C) Copyright Taligent, Inc. 1996 - All Rights Reserved
 * (C) Copyright IBM Corp. 1996 - All Rights Reserved
 *
 * Portions copyright (c) 1996 Sun Microsystems, Inc. All Rights Reserved.
 *
 *   The original version of this source code and documentation is copyrighted
 * and owned by Taligent, Inc., a wholly-owned subsidiary of IBM. These
 * materials are provided under terms of a License Agreement between Taligent
 * and Sun. This technology is protected by multiple US and International
 * patents. This notice and attribution to Taligent may not be removed.
 *   Taligent is a registered trademark of Taligent, Inc.
 *
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for NON-COMMERCIAL purposes and without
 * fee is hereby granted provided that this copyright notice
 * appears in all copies. Please refer to the file "copyright.html"
 * for further important copyright and licensing information.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */

package java.text;
import java.util.Vector;
import java.util.Locale;
import java.text.resources.*;

/**
 * The <code>BreakIterator</code> class implements methods for finding
 * the location of boundaries in text. Instances of <code>BreakIterator</code>
 * maintain a current position and scan over text
 * returning the index of characters where boundaries occur.
 * Internally, <code>BreakIterator</code> scans text using a
 * <code>CharacterIterator</code>, and is thus able to scan text held
 * by any object implementing that protocol. A <code>StringCharacterIterator</code>
 * is used to scan <code>String</code> objects passed to <code>setText</code>.
 *
 * <p>
 * You use the factory methods provided by this class to create
 * instances of various types of break iterators. In particular,
 * use <code>getWordIterator</code>, <code>getLineIterator</code>,
 * <code>getSentenceIterator</code>, and <code>getCharacterIterator</code>
 * to create <code>BreakIterator</code>s that perform
 * word, line, sentence, and character boundary analysis respectively.
 * A single <code>BreakIterator</code> can work only on one unit
 * (word, line, sentence, and so on). You must use a different iterator
 * for each unit boundary analysis you wish to perform.
 *
 * <p>
 * Line boundary analysis determines where a text string can be
 * broken when line-wrapping. The mechanism correctly handles
 * punctuation and hyphenated words.
 *
 * <p>
 * Sentence boundary analysis allows selection with correct interpretation
 * of periods within numbers and abbreviations, and trailing punctuation
 * marks such as quotation marks and parentheses.
 *
 * <p>
 * Word boundary analysis is used by search and replace functions, as
 * well as within text editing applications that allow the user to
 * select words with a double click. Word selection provides correct
 * interpretation of punctuation marks within and following
 * words. Characters that are not part of a word, such as symbols
 * or punctuation marks, have word-breaks on both sides.
 *
 * <p>
 * Character boundary analysis allows users to interact with characters
 * as they expect to, for example, when moving the cursor through a text
 * string. Character boundary analysis provides correct navigation of
 * through character strings, regardless of how the character is stored.
 * For example, an accented character might be stored as a base character
 * and a diacritical mark. What users consider to be a character can
 * differ between languages.
 *
 * <p>
 * <code>BreakIterator</code> is intended for use with natural
 * languages only. Do not use this class to tokenize a programming language.
 *
 * <P>
 * <strong>Examples</strong>:<P>
 * Creating and using text boundaries
 * <blockquote>
 * <pre>
 * public static void main(String args[]) {
 *      if (args.length == 1) {
 *          String stringToExamine = args[0];
 *          //print each word in order
 *          BreakIterator boundary = BreakIterator.getWordInstance();
 *          boundary.setText(stringToExamine);
 *          printEachForward(boundary, stringToExamine);
 *          //print each sentence in reverse order
 *          boundary = BreakIterator.getSentenceInstance(Locale.US);
 *          boundary.setText(stringToExamine);
 *          printEachBackward(boundary, stringToExamine);
 *          printFirst(boundary, stringToExamine);
 *          printLast(boundary, stringToExamine);
 *      }
 * }
 * </pre>
 * </blockquote>
 *
 * Print each element in order
 * <blockquote>
 * <pre>
 * public static void printEachForward(BreakIterator boundary, String source) {
 *     int start = boundary.first();
 *     for (int end = boundary.next();
 *          end != BreakIterator.DONE;
 *          start = end, end = boundary.next()) {
 *          System.out.println(source.substring(start,end));
 *     }
 * }
 * </pre>
 * </blockquote>
 *
 * Print each element in reverse order
 * <blockquote>
 * <pre>
 * public static void printEachBackward(BreakIterator boundary, String source) {
 *     int end = boundary.last();
 *     for (int start = boundary.previous();
 *          start != BreakIterator.DONE;
 *          end = start, start = boundary.previous()) {
 *         System.out.println(source.substring(start,end));
 *     }
 * }
 * </pre>
 * </blockquote>
 *
 * Print first element
 * <blockquote>
 * <pre>
 * public static void printFirst(BreakIterator boundary, String source) {
 *     int start = boundary.first();
 *     int end = boundary.next();
 *     System.out.println(source.substring(start,end));
 * }
 * </pre>
 * </blockquote>
 *
 * Print last element
 * <blockquote>
 * <pre>
 * public static void printLast(BreakIterator boundary, String source) {
 *     int end = boundary.last();
 *     int start = boundary.previous();
 *     System.out.println(source.substring(start,end));
 * }
 * </pre>
 * </blockquote>
 *
 * Print the element at a specified position
 * <blockquote>
 * <pre>
 * public static void printAt(BreakIterator boundary, int pos, String source) {
 *     int end = boundary.following(pos);
 *     int start = boundary.previous();
 *     System.out.println(source.substring(start,end));
 * }
 * </pre>
 * </blockquote>
 *
 * @see CharacterIterator
 *
 */

public abstract class BreakIterator implements Cloneable, java.io.Serializable
{
    /**
     * Constructor. BreakIterator is stateless and has no default behavior.
     */
    protected BreakIterator()
    {
    }

    /**
     * Create a copy of this boundary
     * @return A copy of this
     */
    public Object clone()
    {
    	try {
        	return super.clone();
        }
        catch (CloneNotSupportedException e) {
        	throw new InternalError();
        }
    }

    /**
     * DONE is returned by previous() and next() after all valid
     * boundaries have been returned.
     */
    public static final int DONE = -1;

    /**
     * Return the first boundary. The iterator's current position is set
     * to the first boundary.
     * @return The character index of the first text boundary.
     */
    public abstract int first()
        ;
    /**
     * Return the last boundary. The iterator's current position is set
     * to the last boundary.
     * @return The character index of the last text boundary.
     */
    public abstract int last();
    
    /**
     * Return the nth boundary from the current boundary
     * @param n which boundary to return.  A value of 0
     * does nothing.  Negative values move to previous boundaries
     * and positive values move to later boundaries.
     * @return The index of the nth boundary from the current position.
     */
    public abstract int next(int n);

    /**
     * Return the boundary following the current boundary.
     * @return The character index of the next text boundary or DONE if all
     * boundaries have been returned.  Equivalent to next(1).
     */
    public abstract int next();

    /**
     * Return the boundary preceding the current boundary.
     * @return The character index of the previous text boundary or DONE if all
     * boundaries have been returned.
     */
    public abstract int previous();

    /**
     * Return the first boundary following the specified offset.
     * The value returned is always greater than the offset or
     * the value BreakIterator.DONE
     * @param offset the offset to begin scanning. Valid values
     * are determined by the CharacterIterator passed to
     * setText().  Invalid values cause
     * an IllegalArgumentException to be thrown.
     * @return The first boundary after the specified offset.
     */
    public abstract int following(int offset);

    /**
     * Return character index of the text boundary that was most recently
     * returned by next(), previous(), first(), or last()
     * @return The boundary most recently returned.
     */
    public abstract int current();

    /**
     * Get the text being scanned
     * @return the text being scanned
     */
    public abstract CharacterIterator getText();

    /**
     * Set a new text string to be scanned.  The current scan
     * position is reset to first().
     * @param newText new text to scan.
     */
    public void setText(String newText)
    {
        setText(new StringCharacterIterator(newText));
    }

    /**
     * Set a new text for scanning.  The current scan
     * position is reset to first().
     * @param newText new text to scan.
     */
    public abstract void setText(CharacterIterator newText);

    /**
     * Create BreakIterator for word-breaks using default locale.
     * Returns an instance of a BreakIterator implementing word breaks.
     * WordBreak  is usefull for word selection (ex. double click)
     * @return A BreakIterator for word-breaks
     * @see java.util.Locale#getDefault
     */
    public static BreakIterator getWordInstance()
    {
        return getWordInstance(Locale.getDefault());
    }

    /**
     * Create BreakIterator for word-breaks using default locale.
     * Returns an instance of a BreakIterator implementing word breaks.
     * WordBreak is usefull for word selection (ex. double click)
     * @param where the local.  If a specific WordBreak is not
     * avaliable for the specified local, a default WordBreak is returned.
     * @return A BreakIterator for word-breaks
     */
    public static BreakIterator getWordInstance(Locale where)
    {
        return new SimpleTextBoundary(new WordBreakData());
    }

    /**
     * Create BreakIterator for line-breaks using default locale.
     * Returns an instance of a BreakIterator implementing line breaks. Line
     * breaks are logically possible line breaks, actual line breaks are
     * usually determined based on display width.
     * LineBreak is usefull for word wrapping text.
     * @return A BreakIterator for line-breaks
     * @see java.util.Locale#getDefault
     */
    public static BreakIterator getLineInstance()
    {
        return getLineInstance(Locale.getDefault());
    }

    /**
     * Create BreakIterator for line-breaks using default locale.
     * Returns an instance of a BreakIterator implementing line breaks. Line
     * breaks are logically possible line breaks, actual line breaks are
     * usually determined based on display width.
     * LineBreak is usefull for word wrapping text.
     * @param where the local.  If a specific LineBreak is not
     * avaliable for the specified local, a default LineBreak is returned.
     * @return A BreakIterator for line-breaks
     */
    public static BreakIterator getLineInstance(Locale where)
    {
        return new SimpleTextBoundary(new LineBreakData());
    }

    /**
     * Create BreakIterator for character-breaks using default locale
     * Returns an instance of a BreakIterator implementing character breaks.
     * Character breaks are boundaries of combining character sequences.
     * @return A BreakIterator for character-breaks
     * @see Locale#getDefault
     */
    public static BreakIterator getCharacterInstance()
    {
        return getCharacterInstance(Locale.getDefault());
    }

    /**
     * Create BreakIterator for character-breaks using specified locale
     * Returns an instance of a BreakIterator implementing character breaks.
     * Character breaks are boundaries of combining character sequences.
     * @param where the local.  If a specific character break is not
     * avaliable for the specified local, a default character break is returned.
     * @return A BreakIterator for character-breaks
     */
    public static BreakIterator getCharacterInstance(Locale where)
    {
        return new SimpleTextBoundary(new CharacterBreakData());
    }

    /**
     * Create BreakIterator for sentence-breaks using default locale
     * Returns an instance of a BreakIterator implementing sentence breaks.
     * @return A BreakIterator for sentence-breaks
     * @see java.util.Locale#getDefault
     */
    public static BreakIterator getSentenceInstance()
    {
        return getSentenceInstance(Locale.getDefault());
    }

    /**
     * Create BreakIterator for sentence-breaks using specified locale
     * Returns an instance of a BreakIterator implementing sentence breaks.
     * @param where the local.  If a specific SentenceBreak is not
     * avaliable for the specified local, a default SentenceBreak is returned.
     * @return A BreakIterator for sentence-breaks
     */
    public static BreakIterator getSentenceInstance(Locale where)
    {
        return new SimpleTextBoundary(new SentenceBreakData());
    }

    /**
     * Get the set of Locales for which TextBoundaries are installed
     * @return available locales
     */
    public static synchronized Locale[] getAvailableLocales()
    {
        //FIX ME - this is a known bug.  It should return
        //all locales.
        return LocaleData.getAvailableLocales("NumberPatterns");
    }

}
