/*
 * @(#)DefaultEditorKit.java	1.28 98/08/28
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
package javax.swing.text;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.*;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

/**
 * This is the set of things needed by a text component
 * to be a reasonably functioning editor for some <em>type</em>
 * of text document.  This implementation provides a default
 * implementation which treats text as plain text and 
 * provides a minimal set of actions for a simple editor.
 *
 * @author  Timothy Prinzing
 * @version 1.28 08/28/98
 */
public class DefaultEditorKit extends EditorKit {
    
    /**
     * Creates a copy of the editor kit.  This
     * allows an implementation to serve as a prototype
     * for others, so that they can be quickly created.
     *
     * @return the copy
     */
    public Object clone() {
        return new DefaultEditorKit();
    }

    /**
     * Gets the MIME type of the data that this
     * kit represents support for.  The default
     * is <code>text/plain</code>.
     *
     * @return the type
     */
    public String getContentType() {
        return "text/plain";
    }

    /**
     * Fetches a factory that is suitable for producing 
     * views of any models that are produced by this
     * kit.  The default is to have the UI produce the
     * factory, so this method has no implementation.
     *
     * @return the view factory
     */
    public ViewFactory getViewFactory() {
        return null;
    }

    /**
     * Fetches the set of commands that can be used
     * on a text component that is using a model and
     * view produced by this kit.
     *
     * @return the command list
     */ 
    public Action[] getActions() {
        return defaultActions;
    }

    /**
     * Fetches a caret that can navigate through views
     * produced by the associated ViewFactory.
     *
     * @return the caret
     */
    public Caret createCaret() {
        return null;
    }

    /**
     * Creates an uninitialized text storage model (PlainDocument)
     * that is appropriate for this type of editor.
     *
     * @return the model
     */
    public Document createDefaultDocument() {
        return new PlainDocument();
    }

    /**
     * Inserts content from the given stream which is expected 
     * to be in a format appropriate for this kind of content
     * handler.
     * 
     * @param in  The stream to read from
     * @param doc The destination for the insertion.
     * @param pos The location in the document to place the
     *   content >= 0.
     * @exception IOException on any I/O error
     * @exception BadLocationException if pos represents an invalid
     *   location within the document.
     */
    public void read(InputStream in, Document doc, int pos) 
        throws IOException, BadLocationException {

        read(new InputStreamReader(in), doc, pos);
    }

    /**
     * Writes content from a document to the given stream
     * in a format appropriate for this kind of content handler.
     * 
     * @param out The stream to write to
     * @param doc The source for the write.
     * @param pos The location in the document to fetch the
     *   content >= 0.
     * @param len The amount to write out >= 0.
     * @exception IOException on any I/O error
     * @exception BadLocationException if pos represents an invalid
     *   location within the document.
     */
    public void write(OutputStream out, Document doc, int pos, int len) 
        throws IOException, BadLocationException {

        write(new OutputStreamWriter(out), doc, pos, len);
    }

    /**
     * Inserts content from the given stream, which will be 
     * treated as plain text.
     * 
     * @param in  The stream to read from
     * @param doc The destination for the insertion.
     * @param pos The location in the document to place the
     *   content >= 0.
     * @exception IOException on any I/O error
     * @exception BadLocationException if pos represents an invalid
     *   location within the document.
     */
    public void read(Reader in, Document doc, int pos) 
        throws IOException, BadLocationException {

        char[] buff = new char[4096];
        int nch;
	boolean lastWasCR = false;
	boolean isCRLF = false;
	int last;
	// Read in a block at a time, searching for \r\n and mapping
	// to a single \n.
        while ((nch = in.read(buff, 0, buff.length)) != -1) {
	    last = 0;
	    for(int counter = 0; counter < nch; counter++) {
		switch(buff[counter]) {
		case '\r':
		    if (lastWasCR) {
			if (counter == 0) {
			    doc.insertString(pos, "\r", null);
			    pos++;
			}
			else {
			    doc.insertString(pos, new String(buff, last,
							     counter - last),
					     null);
			    pos += (counter - last);
			    last = counter;
			}
		    }
		    else {
			lastWasCR = true;
		    }
		    break;
		case '\n':
		    if (lastWasCR) {
			if (counter > (last + 1)) {
			    doc.insertString(pos, new String(buff, last,
					    counter - last - 1), null);
			    pos += (counter - last - 1);
			}
			// else nothing to do, can skip \r, next write will
			// write \n
			lastWasCR = false;
			last = counter;
			isCRLF = true;
		    }
		    break;
		default:
		    if (lastWasCR) {
			if (counter == 0) {
			    doc.insertString(pos, "\r", null);
			    pos++;
			}
			else {
			    doc.insertString(pos, new String(buff, last,
					     counter - last), null);
			    pos += (counter - last);
			    last = counter;
			    lastWasCR = false;
			}
		    }
		    break;
		}
	    }
	    if (last < nch) {
		if(lastWasCR) {
		    if (last < (nch - 1)) {
			doc.insertString(pos, new String(buff, last,
					 nch - last - 1), null);
			pos += (nch - last - 1);
		    }
		}
		else {
		    doc.insertString(pos, new String(buff, last,
				     nch - last), null);
		    pos += (nch - last);
		}
	    }
        }
	if (lastWasCR) {
	    doc.insertString(pos, "\r", null);
	}
	if (isCRLF) {
	    doc.putProperty(EndOfLineStringProperty, "\r\n");
	}
	else {
	    doc.putProperty(EndOfLineStringProperty, null);
	}
    }

    /**
     * Writes content from a document to the given stream
     * as plain text.
     * 
     * @param out  The stream to write to
     * @param doc The source for the write.
     * @param pos The location in the document to fetch the
     *   content from >= 0.
     * @param len The amount to write out >= 0.
     * @exception IOException on any I/O error
     * @exception BadLocationException if pos is not within 0 and
     *   the length of the document.
     */
    public void write(Writer out, Document doc, int pos, int len) 
        throws IOException, BadLocationException {

	if ((pos < 0) || ((pos + len) > doc.getLength())) {
	    throw new BadLocationException("DefaultEditorKit.write", pos);
	}
        Segment data = new Segment();
        int nleft = len;
        int offs = pos;
	Object endOfLineProperty = doc.getProperty(EndOfLineStringProperty);
	if (endOfLineProperty != null &&
	    (endOfLineProperty instanceof String) &&
	    !(((String)endOfLineProperty).equals("\n"))) {
	    // There is an end of line string that isn't \n, have to iterate
	    // through and find all \n's and translate to end of line string.
	    String endOfLine = (String)endOfLineProperty;
	    while (nleft > 0) {
		int n = Math.min(nleft, 4096);
		doc.getText(offs, n, data);
		int last = data.offset;
		char[] array = data.array;
		int maxCounter = last + data.count;
		for (int counter = last; counter < maxCounter; counter++) {
		    if (array[counter] == '\n') {
			if (counter > last) {
			    out.write(array, last, counter - last);
			}
			out.write(endOfLine);
			last = counter + 1;
		    }
		}
		if (maxCounter > last) {
		    out.write(array, last, maxCounter - last);
		}
		offs += n;
		nleft -= n;
	    }
	}
	else {
	    // Just write out text, will already have \n, no mapping to
	    // do.
	    while (nleft > 0) {
		int n = Math.min(nleft, 4096);
		doc.getText(offs, n, data);
		out.write(data.array, data.offset, data.count);
		offs += n;
		nleft -= n;
	    }
	}
	out.flush();
    }


    /**
     * When reading a document if a CRLF is encountered a property
     * with this name is added and the value will be "\r\n".
     */
    public static final String EndOfLineStringProperty = "__EndOfLine__";

    // --- names of well-known actions ---------------------------

    /**
     * Name of the action to place content into the associated
     * document.  If there is a selection, it is removed before
     * the new content is added.
     * @see InsertContentAction
     * @see #getActions
     */
    public static final String insertContentAction = "insert-content";

    /**
     * Name of the action to place a line/paragraph break into
     * the document.  If there is a selection, it is removed before
     * the break is added.
     * @see InsertBreakAction
     * @see #getActions
     */
    public static final String insertBreakAction = "insert-break";

    /**
     * Name of the action to place a tab character into
     * the document.  If there is a selection, it is removed before
     * the tab is added.
     * @see InsertTabAction
     * @see #getActions
     */
    public static final String insertTabAction = "insert-tab";

    /**
     * Name of the action to delete the character of content that
     * precedes the current caret position.
     * @see DeletePrevCharAction
     * @see #getActions
     */
    public static final String deletePrevCharAction = "delete-previous";

    /**
     * Name of the action to delete the character of content that
     * follows the current caret position.
     * @see DeleteNextCharAction
     * @see #getActions
     */
    public static final String deleteNextCharAction = "delete-next";

    /**
     * Name of the action to set the editor into read-only
     * mode.
     * @see ReadOnlyAction
     * @see #getActions
     */
    public static final String readOnlyAction = "set-read-only";

    /**
     * Name of the action to set the editor into writeable
     * mode.
     * @see WritableAction
     * @see #getActions
     */
    public static final String writableAction = "set-writable";

    /**
     * Name of the action to cut the selected region
     * and place the contents into the system clipboard.
     * @see JTextComponent#cut
     * @see #getActions
     */
    public static final String cutAction = "cut-to-clipboard";

    /**
     * Name of the action to copy the selected region
     * and place the contents into the system clipboard.
     * @see JTextComponent#copy
     * @see #getActions
     */
    public static final String copyAction = "copy-to-clipboard";

    /**
     * Name of the action to paste the contents of the
     * system clipboard into the selected region, or before the
     * caret if nothing is selected.
     * @see JTextComponent#paste
     * @see #getActions
     */
    public static final String pasteAction = "paste-from-clipboard";

    /**
     * Name of the action to create a beep.
     * @see BeepAction
     * @see #getActions
     */
    public static final String beepAction = "beep";

    /**
     * Name of the action to page up vertically.
     * @see PageUpAction
     * @see #getActions
     */
    public static final String pageUpAction = "page-up";

    /**
     * Name of the action to page down vertically.
     * @see PageDownAction
     * @see #getActions
     */
    public static final String pageDownAction = "page-down";

    /**
     * Name of the Action for moving the caret 
     * logically forward one position.
     * @see ForwardAction
     * @see #getActions
     */
    public static final String forwardAction = "caret-forward";

    /**
     * Name of the Action for moving the caret 
     * logically backward one position.
     * @see BackwardAction
     * @see #getActions
     */
    public static final String backwardAction = "caret-backward";

    /**
     * Name of the Action for extending the selection
     * by moving the caret logically forward one position.
     * @see SelectionForwardAction
     * @see #getActions
     */
    public static final String selectionForwardAction = "selection-forward";

    /**
     * Name of the Action for extending the selection
     * by moving the caret logically backward one position.
     * @see SelectionBackwardAction
     * @see #getActions
     */
    public static final String selectionBackwardAction = "selection-backward";

    /**
     * Name of the Action for moving the caret 
     * logically upward one position.
     * @see UpAction
     * @see #getActions
     */
    public static final String upAction = "caret-up";

    /**
     * Name of the Action for moving the caret 
     * logically downward one position.
     * @see DownAction
     * @see #getActions
     */
    public static final String downAction = "caret-down";

    /**
     * Name of the Action for moving the caret 
     * logically upward one position, extending the selection.
     * @see UpAction
     * @see #getActions
     */
    public static final String selectionUpAction = "selection-up";

    /**
     * Name of the Action for moving the caret 
     * logically downward one position, extending the selection.
     * @see DownAction
     * @see #getActions
     */
    public static final String selectionDownAction = "selection-down";

    /**
     * Name of the Action for moving the caret 
     * to the begining of a word.
     * @see BeginAction
     * @see #getActions
     */
    public static final String beginWordAction = "caret-begin-word";

    /**
     * Name of the Action for moving the caret 
     * to the end of a word.
     * @see EndAction
     * @see #getActions
     */
    public static final String endWordAction = "caret-end-word";

    /**
     * Name of the Action for moving the caret 
     * to the begining of a word, extending the selection.
     * @see BeginWordAction
     * @see #getActions
     */
    public static final String selectionBeginWordAction = "selection-begin-word";

    /**
     * Name of the Action for moving the caret 
     * to the end of a word, extending the selection.
     * @see EndWordAction
     * @see #getActions
     */
    public static final String selectionEndWordAction = "selection-end-word";

    /**
     * Name of the Action for moving the caret to the begining of the
     * previous word.
     * @see PreviousWordAction
     * @see #getActions
     */
    public static final String previousWordAction = "caret-previous-word";

    /**
     * Name of the Action for moving the caret to the begining of the
     * next word.
     * to the next of the document.
     * @see NextWordAction
     * @see #getActions
     */
    public static final String nextWordAction = "caret-next-word";

    /**
     * Name of the Action for moving the selection to the begining of the
     * previous word, extending the selection.
     * @see PreviousWordAction
     * @see #getActions
     */
    public static final String selectionPreviousWordAction = "selection-previous-word";

    /**
     * Name of the Action for moving the selection to the begining of the
     * next word, extending the selection.
     * @see NextWordAction
     * @see #getActions
     */
    public static final String selectionNextWordAction = "selection-next-word";

    /**
     * Name of the Action for moving the caret 
     * to the begining of a line.
     * @see BeginAction
     * @see #getActions
     */
    public static final String beginLineAction = "caret-begin-line";

    /**
     * Name of the Action for moving the caret 
     * to the end of a line.
     * @see EndAction
     * @see #getActions
     */
    public static final String endLineAction = "caret-end-line";

    /**
     * Name of the Action for moving the caret 
     * to the begining of a line, extending the selection.
     * @see BeginLineAction
     * @see #getActions
     */
    public static final String selectionBeginLineAction = "selection-begin-line";

    /**
     * Name of the Action for moving the caret 
     * to the end of a line, extending the selection.
     * @see EndLineAction
     * @see #getActions
     */
    public static final String selectionEndLineAction = "selection-end-line";

    /**
     * Name of the Action for moving the caret 
     * to the begining of a paragraph.
     * @see BeginAction
     * @see #getActions
     */
    public static final String beginParagraphAction = "caret-begin-paragraph";

    /**
     * Name of the Action for moving the caret 
     * to the end of a paragraph.
     * @see EndAction
     * @see #getActions
     */
    public static final String endParagraphAction = "caret-end-paragraph";

    /**
     * Name of the Action for moving the caret 
     * to the begining of a paragraph, extending the selection.
     * @see BeginParagraphAction
     * @see #getActions
     */
    public static final String selectionBeginParagraphAction = "selection-begin-paragraph";

    /**
     * Name of the Action for moving the caret 
     * to the end of a paragraph, extending the selection.
     * @see EndParagraphAction
     * @see #getActions
     */
    public static final String selectionEndParagraphAction = "selection-end-paragraph";

    /**
     * Name of the Action for moving the caret 
     * to the begining of the document.
     * @see BeginAction
     * @see #getActions
     */
    public static final String beginAction = "caret-begin";

    /**
     * Name of the Action for moving the caret 
     * to the end of the document.
     * @see EndAction
     * @see #getActions
     */
    public static final String endAction = "caret-end";

    /**
     * Name of the Action for moving the caret 
     * to the begining of the document.
     * @see BeginAction
     * @see #getActions
     */
    public static final String selectionBeginAction = "selection-begin";

    /**
     * Name of the Action for moving the caret 
     * to the end of the document.
     * @see EndAction
     * @see #getActions
     */
    public static final String selectionEndAction = "selection-end";

    /**
     * Name of the Action for selecting a word around the caret.
     * @see SelectWordAction
     * @see #getActions
     */
    public static final String selectWordAction = "select-word";

    /**
     * Name of the Action for selecting a line around the caret.
     * @see SelectLineAction
     * @see #getActions
     */
    public static final String selectLineAction = "select-line";

    /**
     * Name of the Action for selecting a paragraph around the caret.
     * @see SelectParagraphAction
     * @see #getActions
     */
    public static final String selectParagraphAction = "select-paragraph";

    /**
     * Name of the Action for selecting the entire document
     * @see SelectAllAction
     * @see #getActions
     */
    public static final String selectAllAction = "select-all";

    /**
     * Name of the action that is executed by default if 
     * a <em>key typed event</em> is received and there
     * is no keymap entry.
     * @see DefaultKeyTypedAction
     * @see #getActions
     */
    public static final String defaultKeyTypedAction = "default-typed";

    // --- Action implementations ---------------------------------

    private static final Action[] defaultActions = {
        new InsertContentAction(), new DeletePrevCharAction(), 
        new DeleteNextCharAction(), new ReadOnlyAction(),
        new WritableAction(), new CutAction(), 
        new CopyAction(), new PasteAction(),
        new PageUpAction(), new PageDownAction(),
        new InsertBreakAction(), new BeepAction(),
        new NextVisualPositionAction(forwardAction, false,
				     SwingConstants.EAST),
        new NextVisualPositionAction(backwardAction, false,
				     SwingConstants.WEST),
        new NextVisualPositionAction(selectionForwardAction, true,
				     SwingConstants.EAST),
        new NextVisualPositionAction(selectionBackwardAction, true,
				     SwingConstants.WEST),
        new NextVisualPositionAction(upAction, false,
				     SwingConstants.NORTH),
        new NextVisualPositionAction(downAction, false,
				     SwingConstants.SOUTH),
        new NextVisualPositionAction(selectionUpAction, true,
				     SwingConstants.NORTH),
        new NextVisualPositionAction(selectionDownAction, true,
				     SwingConstants.SOUTH),
        // new ForwardAction(forwardAction, false),
        // new BackwardAction(backwardAction, false),
        // new ForwardAction(selectionForwardAction, true),
        // new BackwardAction(selectionBackwardAction, true),
        // new UpAction(upAction, false),  
        // new DownAction(downAction, false), 
        // new UpAction(selectionUpAction, true),  
        // new DownAction(selectionDownAction, true), 
        new BeginWordAction(beginWordAction, false),  
        new EndWordAction(endWordAction, false),
        new BeginWordAction(selectionBeginWordAction, true),  
        new EndWordAction(selectionEndWordAction, true),
        new PreviousWordAction(previousWordAction, false),  
        new NextWordAction(nextWordAction, false),
        new PreviousWordAction(selectionPreviousWordAction, true),  
        new NextWordAction(selectionNextWordAction, true),
        new BeginLineAction(beginLineAction, false),  
        new EndLineAction(endLineAction, false),
        new BeginLineAction(selectionBeginLineAction, true),  
        new EndLineAction(selectionEndLineAction, true),
        new BeginParagraphAction(beginParagraphAction, false),  
        new EndParagraphAction(endParagraphAction, false),
        new BeginParagraphAction(selectionBeginParagraphAction, true),  
        new EndParagraphAction(selectionEndParagraphAction, true),
        new BeginAction(beginAction, false), 
        new EndAction(endAction, false),
        new BeginAction(selectionBeginAction, true), 
        new EndAction(selectionEndAction, true),
        new DefaultKeyTypedAction(), new InsertTabAction(),
        new SelectWordAction(), new SelectLineAction(),
        new SelectParagraphAction(), new SelectAllAction(),
        new DumpModelAction()
    };

    /**
     * The action that is executed by default if 
     * a <em>key typed event</em> is received and there
     * is no keymap entry.  There is a variation across
     * different VM's in what gets sent as a <em>key typed</em>
     * event, and this action tries to filter out the undesired
     * events.  This filters the control characters and those
     * with the ALT modifier.
     * <p>
     * If the event doesn't get filtered, it will try to insert
     * content into the text editor.  The content is fetched
     * from the command string of the ActionEvent.  The text
     * entry is done through the <code>replaceSelection</code>
     * method on the target text component.  This is the
     * action that will be fired for most text entry tasks.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases.  The current serialization support is appropriate
     * for short term storage or RMI between applications running the same
     * version of Swing.  A future release of Swing will provide support for
     * long term persistence.
     *
     * @see DefaultEditorKit#defaultKeyTypedAction
     * @see DefaultEditorKit#getActions
     * @see Keymap#setDefaultAction
     * @see Keymap#getDefaultAction
     */
    public static class DefaultKeyTypedAction extends TextAction {

        /**
         * Creates this object with the appropriate identifier.
         */
        public DefaultKeyTypedAction() {
            super(defaultKeyTypedAction);
        }

        /**
         * The operation to perform when this action is triggered.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if ((target != null) && (e != null)) {
                String content = e.getActionCommand();
                int mod = e.getModifiers();
                if ((content != null) && (content.length() > 0) && 
                    ((mod & ActionEvent.ALT_MASK) == 0)) {
                    char c = content.charAt(0);
                    if ((c >= 0x20) && (c != 0x7F)) {
                        target.replaceSelection(content);
                    }
                }
            }
        }
    }

    /**
     * Places content into the associated document.
     * If there is a selection, it is removed before
     * the new content is added.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases.  The current serialization support is appropriate
     * for short term storage or RMI between applications running the same
     * version of Swing.  A future release of Swing will provide support for
     * long term persistence.
     *
     * @see DefaultEditorKit#insertContentAction
     * @see DefaultEditorKit#getActions
     */
    public static class InsertContentAction extends TextAction {

        /**
         * Creates this object with the appropriate identifier.
         */
        public InsertContentAction() {
            super(insertContentAction);
        }

        /**
         * The operation to perform when this action is triggered.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if ((target != null) && (e != null)) {
                String content = e.getActionCommand();
                if (content != null) {
                    target.replaceSelection(content);
                } else {
                    target.getToolkit().beep();
                }
            }
        }
    }

    /**
     * Places a line/paragraph break into the document.
     * If there is a selection, it is removed before
     * the break is added.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases.  The current serialization support is appropriate
     * for short term storage or RMI between applications running the same
     * version of Swing.  A future release of Swing will provide support for
     * long term persistence.
     *
     * @see DefaultEditorKit#insertBreakAction
     * @see DefaultEditorKit#getActions
     */
    public static class InsertBreakAction extends TextAction {

        /**
         * Creates this object with the appropriate identifier.
         */
        public InsertBreakAction() {
            super(insertBreakAction);
        }

        /**
         * The operation to perform when this action is triggered.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                target.replaceSelection("\n");
            }
        }
    }

    /**
     * Places a tab character into the document. If there
     * is a selection, it is removed before the tab is added.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases.  The current serialization support is appropriate
     * for short term storage or RMI between applications running the same
     * version of Swing.  A future release of Swing will provide support for
     * long term persistence.
     *
     * @see DefaultEditorKit#insertTabAction
     * @see DefaultEditorKit#getActions
     */
    public static class InsertTabAction extends TextAction {

        /**
         * Creates this object with the appropriate identifier.
         */
        public InsertTabAction() {
            super(insertTabAction);
        }

        /**
         * The operation to perform when this action is triggered.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                target.replaceSelection("\t");
            }
        }
    }

    /*
     * Deletes the character of content that precedes the
     * current caret position.
     * @see DefaultEditorKit#deletePrevCharAction
     * @see DefaultEditorKit#getActions
     */
    static class DeletePrevCharAction extends TextAction {

        /**
         * Creates this object with the appropriate identifier.
         */
        DeletePrevCharAction() {
            super(deletePrevCharAction);
        }

        /**
         * The operation to perform when this action is triggered.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            boolean beep = true;
            if ((target != null) && (target.isEditable())) {
                try {
                    Document doc = target.getDocument();
                    Caret caret = target.getCaret();
                    int dot = caret.getDot();
                    int mark = caret.getMark();
                    if (dot != mark) {
                        doc.remove(Math.min(dot, mark), Math.abs(dot - mark));
                        beep = false;
                    } else if (dot > 0) {
                        doc.remove(dot - 1, 1);
                        beep = false;
                    }
                } catch (BadLocationException bl) {
                }
            }
            if (beep) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    /*
     * Deletes the character of content that follows the
     * current caret position.
     * @see DefaultEditorKit#deleteNextCharAction
     * @see DefaultEditorKit#getActions
     */
    static class DeleteNextCharAction extends TextAction {

        /* Create this object with the appropriate identifier. */
        DeleteNextCharAction() {
            super(deleteNextCharAction);
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            boolean beep = true;
            if ((target != null) && (target.isEditable())) {
                try {
                    Document doc = target.getDocument();
                    Caret caret = target.getCaret();
                    int dot = caret.getDot();
                    int mark = caret.getMark();
                    if (dot != mark) {
                        doc.remove(Math.min(dot, mark), Math.abs(dot - mark));
                        beep = false;
                    } else if (dot < doc.getLength()) {
                        doc.remove(dot, 1);
                        beep = false;
                    }
                } catch (BadLocationException bl) {
                }
            }
            if (beep) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    /*
     * Sets the editor into read-only mode.
     * @see DefaultEditorKit#readOnlyAction
     * @see DefaultEditorKit#getActions
     */
    static class ReadOnlyAction extends TextAction {

        /* Create this object with the appropriate identifier. */
        ReadOnlyAction() {
            super(readOnlyAction);
        }

        /**
         * The operation to perform when this action is triggered.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                target.setEditable(false);
            }
        }
    }

    /*
     * Sets the editor into writeable mode.
     * @see DefaultEditorKit#writableAction
     * @see DefaultEditorKit#getActions
     */
    static class WritableAction extends TextAction {

        /* Create this object with the appropriate identifier. */
        WritableAction() {
            super(writableAction);
        }

        /**
         * The operation to perform when this action is triggered.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                target.setEditable(true);
            }
        }
    }

    /**
     * Cuts the selected region and place its contents
     * into the system clipboard.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases.  The current serialization support is appropriate
     * for short term storage or RMI between applications running the same
     * version of Swing.  A future release of Swing will provide support for
     * long term persistence.
     *
     * @see DefaultEditorKit#cutAction
     * @see DefaultEditorKit#getActions
     */
    public static class CutAction extends TextAction {

        /** Create this object with the appropriate identifier. */
        public CutAction() {
            super(cutAction);
        }

        /**
         * The operation to perform when this action is triggered.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                target.cut();
            }
        }
    }

    /**
     * Coies the selected region and place its contents
     * into the system clipboard.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases.  The current serialization support is appropriate
     * for short term storage or RMI between applications running the same
     * version of Swing.  A future release of Swing will provide support for
     * long term persistence.
     *
     * @see DefaultEditorKit#copyAction
     * @see DefaultEditorKit#getActions
     */
    public static class CopyAction extends TextAction {

        /** Create this object with the appropriate identifier. */
        public CopyAction() {
            super(copyAction);
        }

        /**
         * The operation to perform when this action is triggered.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                target.copy();
            }
        }
    }

    /**
     * Pastes the contents of the system clipboard into the
     * selected region, or before the caret if nothing is
     * selected.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases.  The current serialization support is appropriate
     * for short term storage or RMI between applications running the same
     * version of Swing.  A future release of Swing will provide support for
     * long term persistence.
     *
     * @see DefaultEditorKit#pasteAction
     * @see DefaultEditorKit#getActions
     */
    public static class PasteAction extends TextAction {

        /** Create this object with the appropriate identifier. */
        public PasteAction() {
            super(pasteAction);
        }

        /**
         * The operation to perform when this action is triggered.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                target.paste();
            }
        }
    }

    /**
     * Creates a beep.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases.  The current serialization support is appropriate
     * for short term storage or RMI between applications running the same
     * version of Swing.  A future release of Swing will provide support for
     * long term persistence.
     *
     * @see DefaultEditorKit#beepAction
     * @see DefaultEditorKit#getActions
     */
    public static class BeepAction extends TextAction {

        /** Create this object with the appropriate identifier. */
        public BeepAction() {
            super(beepAction);
        }

        /**
         * The operation to perform when this action is triggered.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /*
     * Pages up vertically.
     * @see DefaultEditorKit#pageUpAction
     * @see DefaultEditorKit#getActions
     */
    static class PageUpAction extends TextAction {

        /* Create this object with the appropriate identifier. */
        PageUpAction() {
            super(pageUpAction);
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
		int scrollOffset;
		int selectedIndex;
		Rectangle visible = new Rectangle();
		Rectangle r;
		target.computeVisibleRect(visible);
		scrollOffset = visible.y;
		visible.y -= visible.height;
		if(visible.y < 0)
		    visible.y = 0;
		scrollOffset = scrollOffset - visible.y;
		target.scrollRectToVisible(visible);
		
		selectedIndex = target.getCaretPosition();
		try {
		    if(selectedIndex != -1) {
			r = target.modelToView(selectedIndex);
			r.y -= scrollOffset;
			selectedIndex = target.viewToModel(new Point(r.x,r.y));
			if(selectedIndex  < 0) {
			    selectedIndex = 0;
			}
			target.setCaretPosition(selectedIndex);
		    }
		} catch(BadLocationException bl) {
		    target.getToolkit().beep();
		}
            }
        }
    }

    /*
     * Pages down vertically.
     * @see DefaultEditorKit#pageDownAction
     * @see DefaultEditorKit#getActions
     */
    static class PageDownAction extends TextAction {

        /* Create this object with the appropriate identifier. */
        PageDownAction() {
            super(pageDownAction);
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
		int scrollOffset;
		int selectedIndex;
		Rectangle visible = new Rectangle();
		Rectangle r;
		target.computeVisibleRect(visible);
		scrollOffset = visible.y;
		visible.y += visible.height;
		if((visible.y+visible.height) > target.getHeight())
		    visible.y = (target.getHeight() - visible.height);
		scrollOffset = visible.y - scrollOffset;
		target.scrollRectToVisible(visible);
		
		selectedIndex = target.getCaretPosition();
		try {
		    if(selectedIndex != -1) {
			r = target.modelToView(selectedIndex);
			r.y += scrollOffset;
			selectedIndex = target.viewToModel(new Point(r.x,r.y));
			Document doc = target.getDocument();
			if(selectedIndex  > (doc.getLength()-1))
			    selectedIndex = doc.getLength()-1;
			target.setCaretPosition(selectedIndex);
		    }
		} catch(BadLocationException bl) {
		    target.getToolkit().beep();
		}
            }
        }
    }

    static class DumpModelAction extends TextAction {

        DumpModelAction() {
            super("dump-model");
        }

        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                Document d = target.getDocument();
                if (d instanceof AbstractDocument) {
                    ((AbstractDocument) d).dump(System.err);
                }
            }
        }
    }

    /*
     * Move the caret logically forward one position.
     * @see DefaultEditorKit#forwardAction
     * @see DefaultEditorKit#getActions
     */
    static class ForwardAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        ForwardAction(String nm, boolean select) {
            super(nm);
            this.select = select;
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                Document doc = target.getDocument();
                int dot = target.getCaretPosition();
                if (dot < doc.getLength()) {
                    dot += 1;
                    if (select) {
                        target.moveCaretPosition(dot);
                    } else {
                        target.setCaretPosition(dot);
                    }
                } else {
                    target.getToolkit().beep();
                }
                target.getCaret().setMagicCaretPosition(null);
            }
        }

        private boolean select;
    }
            
    /*
     * Move the caret logically backward one position.
     * @see DefaultEditorKit#backwardAction
     * @see DefaultEditorKit#getActions
     */
    static class BackwardAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        BackwardAction(String nm, boolean select) {
            super(nm);
            this.select = select;
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                int dot = target.getCaretPosition();
                if (dot > 0) {
                    dot -= 1;
                    if (select) {
                        target.moveCaretPosition(dot);
                    } else {
                        target.setCaretPosition(dot);
                    }
                } else {
                    target.getToolkit().beep();
                }
                target.getCaret().setMagicCaretPosition(null);
            }
        }

        private boolean select;
    }
            
    /*
     * Action to move the selection by way of the
     * getNextVisualPositionFrom method. Constructor indicates direction
     * to use.
     */
    static class NextVisualPositionAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        NextVisualPositionAction(String nm, boolean select, int direction) {
            super(nm);
            this.select = select;
	    this.direction = direction;
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
		Caret caret = target.getCaret();
		DefaultCaret bidiCaret = (caret instanceof DefaultCaret) ?
			                      (DefaultCaret)caret : null;
		int dot = caret.getDot();
		Position.Bias[] bias = new Position.Bias[1];

		try {
		    if(caret != null &&
		       (direction == SwingConstants.NORTH ||
			direction == SwingConstants.SOUTH)) {
			Point p = caret.getMagicCaretPosition();
			if (p == null) {
			    Rectangle r = (bidiCaret != null) ?
				target.getUI().modelToView(target, dot,
						      bidiCaret.getDotBias()) :
				target.modelToView(dot);
			    p = new Point(r.x, r.y);
			    caret.setMagicCaretPosition(p);
			}
		    }

                    dot = target.getUI().getNextVisualPositionFrom(target, dot,
                            (bidiCaret != null) ? bidiCaret.getDotBias() :
			    Position.Bias.Forward, direction, bias);
		    if(bias[0] == null) {
			bias[0] = Position.Bias.Forward;
		    }
		    if(bidiCaret != null) {
			if (select) {
			    bidiCaret.moveDot(dot, bias[0]);
			} else {
			    bidiCaret.setDot(dot, bias[0]);
			}
		    }
		    else {
			if (select) {
			    caret.moveDot(dot);
			} else {
			    caret.setDot(dot);
			}
		    }
		    if(direction == SwingConstants.EAST ||
		       direction == SwingConstants.WEST) {
			target.getCaret().setMagicCaretPosition(null);
		    }
                } catch (BadLocationException ex) {
                }
            }
        }

        private boolean select;
	private int direction;
    }
            
    /*
     * Move the caret upward one row visually.
     * @see DefaultEditorKit#upAction
     * @see DefaultEditorKit#getActions
     */
    static class UpAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        UpAction(String nm, boolean select) {
            super(nm);
            this.select = select;
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                try {
                    Caret caret = target.getCaret();
                    int dot = caret.getDot();
                    Point p = caret.getMagicCaretPosition();
                    if (p == null) {
                        Rectangle r = target.modelToView(dot);
                        p = new Point(r.x, r.y);
                        caret.setMagicCaretPosition(p);
                    }
                    dot = Utilities.getPositionAbove(target, dot, p.x);
                    if (select) {
                        caret.moveDot(dot);
                    } else {
                        caret.setDot(dot);
                    }
                } catch (BadLocationException ex) {
                    target.getToolkit().beep();
                }
            }
        }

        private boolean select;
    }

    /*
     * Move the caret downward one row visually.
     * @see DefaultEditorKit#downAction
     * @see DefaultEditorKit#getActions
     */
    static class DownAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        DownAction(String nm, boolean select) {
            super(nm);
            this.select = select;
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                try {
                    Caret caret = target.getCaret();
                    int dot = caret.getDot();
                    Point p = caret.getMagicCaretPosition();
                    if (p == null) {
                        Rectangle r = target.modelToView(dot);
                        p = new Point(r.x, r.y);
                        caret.setMagicCaretPosition(p);
                    }
                    dot = Utilities.getPositionBelow(target, dot, p.x);
                    if (select) {
                        caret.moveDot(dot);
                    } else {
                        caret.setDot(dot);
                    }
                } catch (BadLocationException ex) {
                    target.getToolkit().beep();
                }
            }
        }

        private boolean select;
    }
            
    /*
     * Position the caret to the beginning of the word.
     * @see DefaultEditorKit#beginWordAction
     * @see DefaultEditorKit#selectBeginWordAction
     * @see DefaultEditorKit#getActions
     */
    static class BeginWordAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        BeginWordAction(String nm, boolean select) {
            super(nm);
            this.select = select;
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                try {
                    int offs = target.getCaretPosition();
                    int begOffs = Utilities.getWordStart(target, offs);
                    if (select) {
                        target.moveCaretPosition(begOffs);
                    } else {
                        target.setCaretPosition(begOffs);
                    }
                } catch (BadLocationException bl) {
                    target.getToolkit().beep();
                }
            }
        }

        private boolean select;
    }

    /*
     * Position the caret to the end of the word.
     * @see DefaultEditorKit#endWordAction
     * @see DefaultEditorKit#selectEndWordAction
     * @see DefaultEditorKit#getActions
     */
    static class EndWordAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        EndWordAction(String nm, boolean select) {
            super(nm);
            this.select = select;
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                try {
                    int offs = target.getCaretPosition();
                    int endOffs = Utilities.getWordEnd(target, offs);
                    if (select) {
                        target.moveCaretPosition(endOffs);
                    } else {
                        target.setCaretPosition(endOffs);
                    }
                } catch (BadLocationException bl) {
                    target.getToolkit().beep();
                }
            }
        }

        private boolean select;
    }

    /*
     * Position the caret to the previousning of the word.
     * @see DefaultEditorKit#previousWordAction
     * @see DefaultEditorKit#selectPreviousWordAction
     * @see DefaultEditorKit#getActions
     */
    static class PreviousWordAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        PreviousWordAction(String nm, boolean select) {
            super(nm);
            this.select = select;
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                try {
                    int offs = target.getCaretPosition();
                    offs = Utilities.getPreviousWord(target, offs);
                    if (select) {
                        target.moveCaretPosition(offs);
                    } else {
                        target.setCaretPosition(offs);
                    }
                } catch (BadLocationException bl) {
                    target.getToolkit().beep();
                }
            }
        }

        private boolean select;
    }

    /*
     * Position the caret to the next of the word.
     * @see DefaultEditorKit#nextWordAction
     * @see DefaultEditorKit#selectNextWordAction
     * @see DefaultEditorKit#getActions
     */
    static class NextWordAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        NextWordAction(String nm, boolean select) {
            super(nm);
            this.select = select;
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                try {
                    int offs = target.getCaretPosition();
                    offs = Utilities.getNextWord(target, offs);
                    if (select) {
                        target.moveCaretPosition(offs);
                    } else {
                        target.setCaretPosition(offs);
                    }
                } catch (BadLocationException bl) {
                    target.getToolkit().beep();
                }
            }
        }

        private boolean select;
    }

    /*
     * Position the caret to the beginning of the line.
     * @see DefaultEditorKit#beginLineAction
     * @see DefaultEditorKit#selectBeginLineAction
     * @see DefaultEditorKit#getActions
     */
    static class BeginLineAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        BeginLineAction(String nm, boolean select) {
            super(nm);
            this.select = select;
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                try {
                    int offs = target.getCaretPosition();
                    int begOffs = Utilities.getRowStart(target, offs);
                    if (select) {
                        target.moveCaretPosition(begOffs);
                    } else {
                        target.setCaretPosition(begOffs);
                    }
                } catch (BadLocationException bl) {
                    target.getToolkit().beep();
                }
            }
        }

        private boolean select;
    }

    /*
     * Position the caret to the end of the line.
     * @see DefaultEditorKit#endLineAction
     * @see DefaultEditorKit#selectEndLineAction
     * @see DefaultEditorKit#getActions
     */
    static class EndLineAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        EndLineAction(String nm, boolean select) {
            super(nm);
            this.select = select;
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                try {
                    int offs = target.getCaretPosition();
                    int endOffs = Utilities.getRowEnd(target, offs);
                    if (select) {
                        target.moveCaretPosition(endOffs);
                    } else {
                        target.setCaretPosition(endOffs);
                    }
                } catch (BadLocationException bl) {
                    target.getToolkit().beep();
                }
            }
        }

        private boolean select;
    }

    /*
     * Position the caret to the beginning of the paragraph.
     * @see DefaultEditorKit#beginParagraphAction
     * @see DefaultEditorKit#selectBeginParagraphAction
     * @see DefaultEditorKit#getActions
     */
    static class BeginParagraphAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        BeginParagraphAction(String nm, boolean select) {
            super(nm);
            this.select = select;
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                int offs = target.getCaretPosition();
                Element elem = Utilities.getParagraphElement(target, offs);
                offs = elem.getStartOffset();
                if (select) {
                    target.moveCaretPosition(offs);
                } else {
                    target.setCaretPosition(offs);
                }
            }
        }

        private boolean select;
    }

    /*
     * Position the caret to the end of the paragraph.
     * @see DefaultEditorKit#endParagraphAction
     * @see DefaultEditorKit#selectEndParagraphAction
     * @see DefaultEditorKit#getActions
     */
    static class EndParagraphAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        EndParagraphAction(String nm, boolean select) {
            super(nm);
            this.select = select;
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                int offs = target.getCaretPosition();
                Element elem = Utilities.getParagraphElement(target, offs);
                offs = elem.getEndOffset();
                if (select) {
                    target.moveCaretPosition(offs);
                } else {
                    target.setCaretPosition(offs);
                }
            }
        }

        private boolean select;
    }

    /*
     * Move the caret to the begining of the document.
     * @see DefaultEditorKit#beginAction
     * @see DefaultEditorKit#getActions
     */
    static class BeginAction extends TextAction {

        /* Create this object with the appropriate identifier. */
        BeginAction(String nm, boolean select) {
            super(nm);
            this.select = select;
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                if (select) {
                    target.moveCaretPosition(0);
                } else {
                    target.setCaretPosition(0);
                }
            }
        }

        private boolean select;
    }

    /*
     * Move the caret to the end of the document.
     * @see DefaultEditorKit#endAction
     * @see DefaultEditorKit#getActions
     */
    static class EndAction extends TextAction {

        /* Create this object with the appropriate identifier. */
        EndAction(String nm, boolean select) {
            super(nm);
            this.select = select;
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                Document doc = target.getDocument();
                int dot = doc.getLength();
                if (select) {
                    target.moveCaretPosition(dot);
                } else {
                    target.setCaretPosition(dot);
                }
            }
        }

        private boolean select;
    }

    /*
     * Select the word around the caret
     * @see DefaultEditorKit#endAction
     * @see DefaultEditorKit#getActions
     */
    static class SelectWordAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        SelectWordAction() {
            super(selectWordAction);
            start = new BeginWordAction("pigdog", false);
            end = new EndWordAction("pigdog", true);
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            start.actionPerformed(e);
            end.actionPerformed(e);
        }

        private Action start;
        private Action end;
    }

    /*
     * Select the line around the caret
     * @see DefaultEditorKit#endAction
     * @see DefaultEditorKit#getActions
     */
    static class SelectLineAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        SelectLineAction() {
            super(selectLineAction);
            start = new BeginLineAction("pigdog", false);
            end = new EndLineAction("pigdog", true);
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            start.actionPerformed(e);
            end.actionPerformed(e);
        }

        private Action start;
        private Action end;
    }

    /*
     * Select the paragraph around the caret
     * @see DefaultEditorKit#endAction
     * @see DefaultEditorKit#getActions
     */
    static class SelectParagraphAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        SelectParagraphAction() {
            super(selectParagraphAction);
            start = new BeginParagraphAction("pigdog", false);
            end = new EndParagraphAction("pigdog", true);
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            start.actionPerformed(e);
            end.actionPerformed(e);
        }

        private Action start;
        private Action end;
    }

    /*
     * Select the entire document
     * @see DefaultEditorKit#endAction
     * @see DefaultEditorKit#getActions
     */
    static class SelectAllAction extends TextAction {

        /** 
         * Create this action with the appropriate identifier. 
         * @param nm  the name of the action, Action.NAME.
         * @param select whether to extend the selection when
         *  changing the caret position.
         */
        SelectAllAction() {
            super(selectAllAction);
        }

        /** The operation to perform when this action is triggered. */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                Document doc = target.getDocument();
                target.setCaretPosition(0);
                target.moveCaretPosition(doc.getLength());
            }
        }

    }

}
