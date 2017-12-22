/*
 * @(#)PrintServiceAttributeEvent.java	1.2 01/12/03
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.print.event;

import java.util.List;
import javax.print.PrintService;
import javax.print.attribute.AttributeSetUtilities;
import javax.print.attribute.PrintServiceAttributeSet;

/**
 *
 * Class PrintServiceAttributeEvent encapsulates an event a
 * Print Service instance reports to let the client know of
 * changes in the print service state.
 */

public class PrintServiceAttributeEvent extends PrintEvent {

    private PrintServiceAttributeSet attributes;

    /**
     * Constructs a PrintServiceAttributeEvent object.
     *
     * @param source the print job generating  this event
     * @param attributes the attribute changes being reported
     */
    public PrintServiceAttributeEvent(PrintService source,
                                      PrintServiceAttributeSet attributes) {

        super(source);
        this.attributes = AttributeSetUtilities.unmodifiableView(attributes);
    }


    /**
     * Returns the print service.

     * @return  Print Service object.
     */
    public PrintService getPrintService() {
 
    	return (PrintService) getSource();
    }


    /**
     * Determine the printing service attributes that changed and their new
     * values.
     *
     * @return  Attributes containing the new values for the service
     * attributes that changed. The returned set may be unmodifiable.
     */
    public PrintServiceAttributeSet getAttributes() {

	return attributes;
    }

}
