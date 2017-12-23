/*
 * @(#)ComboBoxModel.java	1.9 00/02/02
 *
 * Copyright 1997-2000 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */
package javax.swing;

/**
 * A data model for JComboBox
 *
 * @version 1.9 02/02/00
 * @author Arnaud Weber
 */

/** ComboBoxDataModel is a ListDataModel with a selected item
  * This selected item is in the model since it is not
  * always in the item list.
  */
public interface ComboBoxModel extends ListModel {
  /** Set the selected item **/
  void setSelectedItem(Object anItem);

  /** Return the selected item **/
  Object getSelectedItem();
}

