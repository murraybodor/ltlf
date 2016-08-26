/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ca.aeso.ltlf.client.date;

import com.google.gwt.user.client.ui.Label;

/**
 * Public class DatePickerCell is being used by DatePicker class to keep
 * the labels of controls and dates in the grid. It has the facility to attach
 * and retrieve integer type and value fields. The type and value fields
 * could be used by DatePicker to attach the type and specific nature of
 * actions on click. 
 * 
 */

public class DatePickerCell extends Label {

  private int type;
  private int value;

  /**
   * Default constructor.
   */
  public DatePickerCell() { }

  /**
   * Constructor of DatePickerCell.
   * 
   * @param text - string to be set to the label 
   */
  public DatePickerCell(String text) {
    this.setText(text);
  }

  /**
   * Constructor of DatePickerCell.
   * 
   * @param text  - string to be set to the label 
   * @param type  - type to be associated to the label
   * @param value - value to be associated to the label
   */
  public DatePickerCell(String text, int type, int value) {
    this.setText(text);
    this.type = type;
    this.value = value;
  }

  /**
   * Creates a clone of the DatePickerCell object.
   * 
   * @return Object - with the text, 
   */
  public Object clone() {

    DatePickerCell newCell =
      new DatePickerCell( this.getText(), this.type, this.value);

    return newCell;
  }

  /**
   * Sets type of the DatePickerCell.
   * 
   * @param type - int indicating the type of the object
   */
  public void setType(int type) {
    this.type = type;
  }

  /**
   * Sets value of the DatePickerCell.
   * 
   * @param value - int indicating the value of the object
   */
  public void setValue(int value) {
    this.value = value;
  }

  /**
   * Gets the type of the DatePickerCell.
   * 
   * @return value - int indicating the type of the object
   */
  public int type() {
    return this.type;
  }

  /**
   * Gets the value of the DatePickerCell.
   * 
   * @return value - int indicating the value of the object
   */
  public int value() {
    return this.value;
  }
}
