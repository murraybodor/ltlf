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

import java.util.Date;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * DatePicker widget displays a small Gregorian calendar dates to select
 * a date by the user.
 *  
 * <p>It has following features:
 * 
 * <ul>
 * <li>Fully internationalized by default locale</li>
 * <li>Optional display of dates form the adjacent dates months</li>
 * <li>Add a special formatting for a given day</li>
 * <li>Select any date as the start date and month.
 *  Today's date is the default selection and the default displayed month.</li>
 * </ul>
 * </p>
 * <p>CSS hooks:
  <table border="1" bordercolor="#000000" cellpadding="3" cellspacing="0">
    <tbody>
    <tr>
      <td style="FONT-WEIGHT:bold">
        Style name<br/>
      </td>
      <td style="FONT-WEIGHT:bold">
        Widget region affected<br/>
      </td>
    </tr>
    <tr>
      <td>
        .goog-date-picker<br/>
      </td>
      <td>
        Entire widget<br/>
      </td>
    </tr>
    <tr>
      <td>
        .goog-date-picker .grid<br/>
      </td>
      <td>
        Grid in the DatePicker.
        This includes week names and the date numbers.<br/>
      </td>
    </tr>
    <tr>
      <td>
        .goog-date-picker .title<br/>
      </td>
      <td>
        Month and Year titles on the top<br/>
      </td>
    </tr>
    <tr>
      <td>
        .goog-date-picker .control<br/>
      </td>
      <td>
        Month and year increment and decrement buttons<br/>
      </td>
    </tr>
    <tr>
      <td>
        .goog-date-picker .control-menu<br/>
      </td>
      <td>
        Month and year list available by clicking on them<br/>
      </td>
    </tr>
    <tr>
      <td>
        .goog-date-picker .control-block<br/>
      </td>
      <td>
        The block of month/year display with its
        increment decrement controls<br/>
      </td>
    </tr>
    <tr>
      <td>
        .goog-date-picker .control-pane<br/>
      </td>
      <td>
        Top area containing month and year controls<br/>
      </td>
    </tr>
    <tr>
      <td>
        .goog-date-picker .control-today<br/>
      </td>
      <td>
        Clickable today button<br/>
      </td>
    </tr>
    <tr>
      <td>
        .goog-date-picker .weekday<br/>
      </td>
      <td>
        Any date<br/>
      </td>
    </tr>
    <tr>
      <td>
        .goog-date-picker .week-names<br/>
      </td>
      <td>
        Weekday names<br/>
      </td>
    </tr>
    <tr>
      <td>
        .goog-date-picker .week-numbers<br/>
      </td>
      <td>
        Week of the year number<br/>
      </td>
    </tr>
    <tr>
      <td>
        .goog-date-picker .weekend-start<br/>
      </td>
      <td>
        Weekend startdate<br/>
      </td>
    </tr>
    <tr>
      <td>
        .goog-date-picker .weekend-end<br/>
      </td>
      <td>
        Weekend end date<br/>
      </td>
    </tr>
    <tr>
      <td>
        .goog-date-picker .today<br/>
      </td>
      <td>
        Special formatting for today
      </td>
    </tr>
    <tr>
      <td>
        .goog-date-picker .selected<br/>
      </td>
      <td>
        Special formatting for the selected date<br/>
      </td>
    </tr>
    <tr>
      <td>
        .goog-date-picker .other-month
      </td>
      <td>
        Opacity reducing formatting for the adjacent months<br/>
      </td>
    </tr>
    </tbody>
  </table> 
  </p>
 * <p>Following are the incomplete features.
 * Some components of these features exist.
 * However, they do not function completely.
 * <ul>
 * <li>Optional display week numbers</li>
 * </ul>
 * </p>
 * <p>Desired features
 * <ul>
 * <li>Disable calendar functions beyond an end date.
 * This is useful when document archives. Default today.</li>
 * <li>Disable calendar functions beyond an start date.
 * This is useful for reservation systems. Default today.</li>
 * <li>Disable calendar functions on a specific date.
 * This is useful for reservation systems.</li>
 * <li>Right to Left layout depending on the page layout.</li>
 * </ul>
 * </p>
 */

public class DatePicker extends DialogBox
implements ClickListener, ChangeListener, SourcesChangeEvents {

  private static final String STYLE_DATE_PICKER     = "goog-date-picker";
  private static final String STYLE_GRID            = "grid";

  private static final String STYLE_CONTROL         = "control";
  private static final String STYLE_CONTROL_BLOCK   = "control-block";
  private static final String STYLE_CONTROL_PANE    = "control-pane";
  private static final String STYLE_CONTROL_MENU    = "control-menu";
  private static final String STYLE_CONTROL_TODAY   = "control-today";
  private static final String STYLE_TITLE           = "title";

  private static final String STYLE_WEEK_NAMES      = "week-names";
  private static final String STYLE_WEEK_NUMBERS    = "numbers";

  private static final String STYLE_WEEKDAY         = "weekday";
  private static final String STYLE_WEEKEND_START   = "weekend-start";
  private static final String STYLE_WEEKEND_END     = "weekend-end";
  private static final String STYLE_OTHER_MONTHS    = "other-month";

  private static final String STYLE_TODAY           = "today";
  private static final String STYLE_SELECTED        = "selected";

  // Unique numbers representing various actions available in the controls.
  private static final int ACTION_PREV_MONTH = 0;
  private static final int ACTION_SET_MONTH  = 1;
  private static final int ACTION_NEXT_MONTH = 2;
  private static final int ACTION_PREV_YEAR  = 3;
  private static final int ACTION_SET_YEAR   = 4;
  private static final int ACTION_NEXT_YEAR  = 5;
  private static final int ACTION_TODAY      = 6;
  private static final int ACTION_CLOSE      = 7;

  // Unique numbers representing formating actions.
  private static final int FORMAT_ACTION_REMOVE = 0;
  private static final int FORMAT_ACTION_ADD    = 1;

  private LocaleCalendarUtils dateTable;

  private ChangeListenerCollection changeListeners
  = new ChangeListenerCollection();

  private HorizontalPanel control = new HorizontalPanel();
  private FlexTable grid;

  // menu based action

  private int weekOfYearOffset = 0;
  private boolean showYearMonthListing = true;
  private boolean showTodayButton = false;
  private int monthAction;
  private int yearAction;

  private DatePickerCell today;
  private final VerticalPanel panel = new VerticalPanel();

  private int prevMonthSize;
  private int prevMonthDays;
  private int currMonthSize;
  private int nextMonthDays;
  private int gridStart;

  /**
   * Constructor of the DatePicker class.
   * 
   */
  public DatePicker() {

    super(true); // hide dialogue box when clicked outside

    dateTable = new LocaleCalendarUtils(false);
    dateTable.specialDate(LocaleCalendarUtils.TODAY).setTag(STYLE_TODAY);
    dateTable.specialDate(LocaleCalendarUtils.SELECTED).setTag(STYLE_SELECTED);

    grid = new FlexTable();
    grid.setWidth("100%");
    grid.setStyleName(STYLE_GRID);

    panel.addStyleName(STYLE_DATE_PICKER);
    panel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);

    drawControlPane();
    panel.add(control);

    // grid operations
    addDaysOfWeek();
    initialGrid();
    gridUpdate();

    panel.add(grid);

    // today label
    today = dateTable.todayCell();
    today.setValue(ACTION_TODAY);
    today.addStyleName(STYLE_CONTROL_TODAY);
    today.addClickListener(this);
    enableTodayButton();

    super.setWidget(panel);
  }

  /**
   * Public method complete ClickListner interface. This adds a listener to the
   * listeners for Change events. Namely, a date clicked by the user.
   * 
   * @param listener - listener for events
   */
  public void addChangeListener(ChangeListener listener) {
    changeListeners.add(listener);
  }

  /**
   * Public method onChange which is fired when user clicks on the
   * list menu in the widget.
   * @param sender - widget on which user clicked.
   *
   */
  public void onChange(Widget sender) {

    if (sender instanceof ListBox) {

      ListBox list = (ListBox)sender;
      String val = list.getValue(list.getSelectedIndex());
      int ival = Integer.valueOf( val ).intValue();

      if (list == dateTable.monthNames()) {
        action(monthAction, ival);
      } else {
        action(yearAction, ival);       
      }

      changeListeners.fireChange(this);
    }
  }

  /**
   * Public method onClick which is fired when user clicks on the widget.
   * @param sender - widget on which user clicked.
   *
   */
  public void onClick(Widget sender) {

    if (sender instanceof DatePickerCell) {

      DatePickerCell cell = (DatePickerCell)sender;
      int type  = cell.type();
      int value = cell.value();

      if (type == LocaleCalendarUtils.TYPE_CONTROL) {
        action(value);
      } else {
        formatSpecialDates(FORMAT_ACTION_REMOVE);

        dateTable.selectedDate(type, value);

        if (type == LocaleCalendarUtils.TYPE_CURR_MONTH) {
          formatSpecialDates(FORMAT_ACTION_ADD);        
        } else {
          gridUpdate();       
        }
      }
      changeListeners.fireChange(sender);        
    }
  }

  /**
   * Public method removeChangeListener() removes a element from the list of 
   * listeners which will get fired on an widget change event. Example for 
   * listener change event is user click on a date.
   * 
   * @param listener - listener for widget change events
   * 
   */
  public void removeChangeListener(ChangeListener listener) {
    changeListeners.remove(listener);
  }

  /**
   * Public method to get the value of the user selected date for the
   * DatePicker object.
   * 
   * @return Date - value of the selected date
   */
  public Date selectedDate() {
    return dateTable;
  }

  /**
   * Public method to set the default day and date of the widget.
   * Date picker for the given month and year will be displayed.
   * The given date will be marked as the selected date.
   * 
   * @param date - Java Date value to be set
   */
  public void setFullDate(Date date) {
    formatSpecialDates(FORMAT_ACTION_REMOVE);
    dateTable.setFullDate(date);
    gridUpdate();
  }

  /**
   * Public method to set a given special formatting for the given date.
   * 
   * @param date - Date to be formatted specially
   * @param style - CSS style to be used
   */
  public void setSpecialDate(Date date, String style) {
    DatePickerDate specialDay = dateTable.addSpecialDay(date);
    specialDay.setTag(style);
    formatSpecialDates(FORMAT_ACTION_ADD);        
  }

  /**
   * Public method to enable or disable displaying the trailing
   * and leading dates from previous and next months.
   * 
   * @param show - A boolean indicating whether to show or not
   * 
   */
  public void showAdjacentMonths(boolean show) {
    dateTable.enableAdjacentMonths(show);
    gridUpdate();
  }

  /**
   * Public method to display the listing of adjacent months and years by
   * clicking on the month or year in the title.
   * 
   * @param show - A boolean indicating whether to show or not
   * 
   */
  public void showTodayButton(boolean show) {
    this.showTodayButton = show;

    enableTodayButton();
  }

  /**
   * Public method to enable or disable displaying the week number of the year.
   * (implementation not completed)
   * 
   * @param show - A boolean indicating whether to show or not
   * 
   */
  public void showWeekOfYear(boolean show) {
    this.weekOfYearOffset = show ? 1 : 0;
    gridUpdate();
  }

  /**
   * Public method to display the listing of adjacent months and years by
   * clicking on the month or year in the title.
   * 
   * @param show - A boolean indicating whether to show or not
   * 
   */
  public void showYearMonthListing(boolean show) {
    this.showYearMonthListing = show;
    drawControlPane();
  }

  private void action(int actionNum, int arg) {
    formatSpecialDates(FORMAT_ACTION_REMOVE);
    switch(actionNum) {
      case ACTION_SET_MONTH:
        dateTable.setMonth(arg);
        break;
      case ACTION_SET_YEAR:
        dateTable.setYear(arg);
        break;
    }
    gridUpdate();
  }

  private void action(int actionNum) {

    if (actionNum == ACTION_CLOSE) {
      this.hide();
    } else {
      formatSpecialDates(FORMAT_ACTION_REMOVE);
      switch(actionNum) {
        case ACTION_PREV_MONTH:
          dateTable.addMonths(-1);
          break;
        case ACTION_NEXT_MONTH:
          dateTable.addMonths(1);
          break;
        case ACTION_PREV_YEAR:
          dateTable.addMonths(-12);
          break;
        case ACTION_NEXT_YEAR:
          dateTable.addMonths(12);
          break;
        case ACTION_TODAY:
          dateTable.setToday();
          break;
      }
      gridUpdate();
    }
  }

  private void addDaysOfWeek() {

    String[] dayOfWeekNames = LocaleCalendarUtils.dayOfWeekNames();

    for (int col = 0; col < 7; col++) {
      int dayOfWeek = (col + dateTable.weekStart()) % 7;
      grid.setText(0, col + weekOfYearOffset, dayOfWeekNames[dayOfWeek]);
      grid.getCellFormatter().setStyleName(0, col + weekOfYearOffset,
          STYLE_WEEK_NAMES);
    }
  }

  private void addWeekOfYear() {

    String[] weekOfYear = dateTable.weekOfYear();

    for (int row = 0; row < 7; row++) {
      grid.setText(row + 1, 0, weekOfYear[row]);
      grid.getCellFormatter().addStyleName(row + 1, 0, STYLE_WEEK_NUMBERS);
    }
  }

  private void colFormat(int tableRow,
      int tableCol,
      int weekendStart,
      int weekendEnd,
      DatePickerCell w) {
    String style;

    if (tableCol == (weekendStart + weekOfYearOffset)) {
      style = STYLE_WEEKEND_START;
    } else if (tableCol == (weekendEnd + weekOfYearOffset)) {
      style = STYLE_WEEKEND_END;
    } else {
      style = STYLE_WEEKDAY;
    }
    w.setStyleName(style);
    grid.getCellFormatter().addStyleName(tableRow, tableCol, style);
  }

  private void dateFormat(int diffFromMonthStart, String style, int action) {

    if ( diffFromMonthStart < -prevMonthDays
        || diffFromMonthStart >= currMonthSize + nextMonthDays ) {
      return;
    }

    int i = diffFromMonthStart + prevMonthDays + gridStart;

    int row = i / 7;
    int col = i % 7;
    int tableRow = row + 1;
    int tableCol = col + weekOfYearOffset;

    switch(action) {
      case FORMAT_ACTION_REMOVE:
        grid.getCellFormatter().removeStyleName(tableRow, tableCol, style);
        grid.getWidget(tableRow, tableCol).removeStyleName(style);
        break;
      case FORMAT_ACTION_ADD:
        grid.getCellFormatter().addStyleName(tableRow, tableCol, style);
        grid.getWidget(tableRow, tableCol).addStyleName(style);
        break;
    }
  }

  private void drawControlPane() {
    control.clear();
    control.setWidth("100%");
    control.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
    control.addStyleName(STYLE_CONTROL_PANE);

    Widget yearControl  = drawControls(dateTable.yearNames(),
        dateTable.yearName(),
        ACTION_PREV_YEAR,
        ACTION_NEXT_YEAR,
        ACTION_SET_YEAR );
    Widget monthControl = drawControls(dateTable.monthNames(),
        dateTable.monthName(),
        ACTION_PREV_MONTH,
        ACTION_NEXT_MONTH,
        ACTION_SET_MONTH );

    if (dateTable.isYearBeforeMonth()) {
      control.add(yearControl);
      control.add(monthControl);      
    } else {
      control.add(monthControl);      
      control.add(yearControl);
    }
  }

  private Widget drawControls( ListBox names, Label name,
      int prev, int next, int set ) {

    HorizontalPanel hp = new HorizontalPanel();
    hp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
    hp.addStyleName(STYLE_CONTROL_BLOCK);

    if (names == dateTable.monthNames()) {
      monthAction = set;
    } else {
      yearAction = set;       
    }

    // move left
//    if (!showYearMonthListing || set == ACTION_SET_MONTH) {
//      DatePickerCell left = new DatePickerCell("\u00ab"); // \u00ab is <<
//      left.setType(LocaleCalendarUtils.TYPE_CONTROL);
//      left.setValue(prev);
//      left.addStyleName(STYLE_CONTROL);
//      left.addClickListener(this);
//      hp.add(left);
//    }

    // Need list box or not
    if (showYearMonthListing) {

      names.setVisibleItemCount(1);
      names.addStyleName(STYLE_CONTROL_MENU);
      names.addChangeListener(this);

      hp.add(names);

    } else {

      name.addStyleName(STYLE_TITLE);
      hp.add(name);
    }

    // move right
//    if (!showYearMonthListing || set == ACTION_SET_MONTH) {
//      DatePickerCell right = new DatePickerCell("\u00bb"); // \u00ab is >>
//      right.setType(LocaleCalendarUtils.TYPE_CONTROL);
//      right.setValue(next);
//      right.addStyleName(STYLE_CONTROL);
//      right.addClickListener(this);
//      hp.add(right);
//    }

    return hp;
  }

  private void enableTodayButton () {
    if (showTodayButton) {
      panel.add(today);
    } else {
      panel.remove(today);
    }
  }

  private void formatSpecialDates(int action) {
    int numSpecialDays = dateTable.numSpecialDays();

    for (int i = 0; i < numSpecialDays; i++) {
      DatePickerDate date = dateTable.specialDate(i);
      dateFormat(date.dayDiff(), date.tag(), action);
    }
  }

  private void gridUpdate() {

    DatePickerCell[] dayOfMonthNames     = dateTable.dayOfMonthNames();
    DatePickerCell[] dayOfMonthNamesPrev = dateTable.dayOfMonthNamesPrev();
    DatePickerCell[] dayOfMonthNamesNext = dateTable.dayOfMonthNamesNext();

    prevMonthSize = dateTable.prevMonthSize();
    prevMonthDays = dateTable.prevMonthDays();
    currMonthSize = dateTable.currMonthSize();
    nextMonthDays = dateTable.nextMonthDays();
    gridStart     = dateTable.gridStart();
    int weekendStart = dateTable.weekendStart();
    int weekendEnd   = dateTable.weekendEnd();

    if (weekOfYearOffset > 0) {
      addWeekOfYear();      
    }

    int rowCount = grid.getRowCount();
    for (int i = rowCount - 1; i > 0; i--) { // 0th row is the week names
      grid.removeRow(i);
    }

    for (int i = 0; i < prevMonthDays; i++) {
      int row = i / 7;
      int col = i % 7;
      int tableRow = row + 1;
      int tableCol = col + weekOfYearOffset;
      int dayOfMonth = prevMonthSize - prevMonthDays + i; // 0 based

      colFormat(tableRow, tableCol,
          weekendStart, weekendEnd,
          dayOfMonthNamesPrev[dayOfMonth]);
      grid.setWidget(tableRow, tableCol, dayOfMonthNamesPrev[dayOfMonth]);
      grid.getCellFormatter().addStyleName(tableRow, tableCol,
          STYLE_OTHER_MONTHS);
    }

    for (int i = 0; i < currMonthSize; i++) {
      int row = (gridStart + prevMonthDays + i) / 7;
      int col = (gridStart + prevMonthDays + i) % 7;
      int tableRow = row + 1;
      int tableCol = col + weekOfYearOffset;

      colFormat(tableRow, tableCol, weekendStart, weekendEnd,
          dayOfMonthNames[i]);
      grid.setWidget(tableRow, tableCol, dayOfMonthNames[i]);
    }

    for (int i = 0; i < nextMonthDays; i++) {
      int row = (currMonthSize + prevMonthDays + i) / 7;
      int col = (currMonthSize + prevMonthDays + i) % 7;
      int tableRow = row + 1;
      int tableCol = col + weekOfYearOffset;

      colFormat(tableRow, tableCol, weekendStart, weekendEnd,
          dayOfMonthNamesNext[i]);
      grid.setWidget(tableRow, tableCol, dayOfMonthNamesNext[i]);
      grid.getCellFormatter().addStyleName(tableRow, tableCol,
          STYLE_OTHER_MONTHS);
    }
    formatSpecialDates(FORMAT_ACTION_ADD);
  }

  private void initialGrid() {

    DatePickerCell[] dayOfMonthNamesPrev = dateTable.dayOfMonthNamesPrev();
    DatePickerCell[] dayOfMonthNamesNext = dateTable.dayOfMonthNamesNext();
    DatePickerCell[] dayOfMonthNames     = dateTable.dayOfMonthNames();

    for (int i = 0; i < 31; i++) {
      dayOfMonthNamesPrev[i].addClickListener(this);
      dayOfMonthNamesNext[i].addClickListener(this);
      dayOfMonthNames[i].addClickListener(this);
    }
  }
}
