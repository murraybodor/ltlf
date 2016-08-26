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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

/**
 * LocaleCalendarUtils public class provides all the tables required to display
 * the calendar grid for a month with respect to a locale. It also provides
 * methods to obtain following data:
 * 
 * <ul>
 * <li>Names for days of month</li>
 * <li>Month and year names for the title of DatePicker </li>
 * <li>Sequence order of year and month </li>
 * <li>Weekend dates </li>
 * <li>Week number of the year for weeks </li>
 * <li>Starting day of the Week </li>
 * <li>List of month names of the year</li>
 * <li>List of years to be displayed</li>
 * <li>Long name for today</li>
 * <li>Position of the month start in the calendar grid</li>
 * </ul>
 * 
 * Following calendar manipulation methods:
 * <ul>
 * <li>Adding months to current date</li>
 * <li>Setting a specific date, month or year</li>
 * <li>Returning user specified date</li>
 * <li>Enabling display of trailing and leading dates from adjacent months</li>
 * </ul>
 * 
 * Following methods for styling specific dates:
 * <ul>
 * <li>Adding a date for styling with its CSS style name</li>
 * <li>Number of dates with special styles</li>
 * </ul>
 */
public class LocaleCalendarUtils extends DatePickerDate {

  /**
   * Constant SELECTED represents the user selected date in the grid.
   */
  public static final int SELECTED = 0;

  /**
   * Constant TODAY represents today's date in the grid.
   */
  public static final int TODAY = 1;

  /**
   * Constant TYPE_PREV_MONTH represents the grid cells for the month previous
   * to currently displayed one.
   */
  public static final int TYPE_PREV_MONTH = -1;

  /**
   * Constant TYPE_CURR_MONTH represents the grid cells for currently displayed
   * month.
   */
  public static final int TYPE_CURR_MONTH = 0;

  /**
   * Constant TYPE_NEXT_MONTH represents the grid cells for the month next to
   * currently displayed one.
   */
  public static final int TYPE_NEXT_MONTH = 1;

  /**
   * Constant TYPE_CONTROL represents the grid cell representing a control.
   */
  public static final int TYPE_CONTROL = 2;

  private static final DateTimeConstants intlConstants = (DateTimeConstants) GWT.create(DateTimeConstants.class);

  /**
   * dayOfWeekNames is kept as strings because used only once for initial
   * drawing.
   */
  private static final String[] dayOfWeekNames = new String[7];

  private static final DateTimeFormat dayOfMonthFormatter = DateTimeFormat.getFormat("d");
  private static final DateTimeFormat yearFormatter = DateTimeFormat.getFormat("yyyy");
  private static final DateTimeFormat monthFormatter = DateTimeFormat.getFormat("MMM");
  private static final DateTimeFormat dayOfWeekFormatter = DateTimeFormat.getFormat("ccccc");
  private static final DateTimeFormat weekOfYearFormatter = DateTimeFormat.getFormat("w");
  private static final DateTimeFormat fullDateFormatter = DateTimeFormat.getFullDateFormat();

  private static boolean isYearBeforeMonth;
  private static int weekendStart;
  private static int weekendEnd;

  /**
   * Public method dayOfWeekNames() returns an array of the names for days of a
   * week in the default locale.
   * 
   * @return array of size 7 with names for days of a week in the locale
   */
  public static String[] dayOfWeekNames() {
    return dayOfWeekNames;
  }

  private DatePickerCell monthName = new DatePickerCell();
  private DatePickerCell yearName = new DatePickerCell();

  private ListBox monthNames = new ListBox();
  private ListBox yearNames = new ListBox();
  private DatePickerCell todayCell;

  private DatePickerCell[] dayOfMonthNames;
  private DatePickerCell[] dayOfMonthNamesPrev;
  private DatePickerCell[] dayOfMonthNamesNext;

  private boolean adjacentMonths;

  private int currMonthSize;
  private int prevMonthDays;
  private int nextMonthDays;
  private int gridStart;

  // Hack used until the code can be refactored to prevent RPC madness.
  private transient ArrayList specialDates = new ArrayList();

  /**
   * Default constructor for {@link LocaleCalendarUtils}. By default, does not
   * display the dates in adjacent months.
   */
  public LocaleCalendarUtils() {
    this(false);
  }

  /**
   * Public constructor for LocaleCalendarUtils class. It takes in a boolean
   * parameter indicating whether to display some dates from adjacent months.
   * 
   */

  public LocaleCalendarUtils(boolean adjacentMonths) {

    // Finding day of month names
    dayOfMonthNames = new DatePickerCell[31];

    Date date = new Date();
    date.setMonth(0);

    for (int i = 0; i < 31; ++i) {
      date.setDate(i + 1);
      dayOfMonthNames[i] = new DatePickerCell(dayOfMonthFormatter.format(date),
          0, i + 1);
    }

    dayOfMonthNamesPrev = createShadow(dayOfMonthNames, TYPE_PREV_MONTH);
    dayOfMonthNamesNext = createShadow(dayOfMonthNames, TYPE_NEXT_MONTH);

    // Finding day of week names
    for (int i = 1; i <= 7; i++) {
      date.setDate(i);
      int dayOfWeek = date.getDay();
      dayOfWeekNames[dayOfWeek] = dayOfWeekFormatter.format(date);
    }

    // Finding whether year is before month
    String[] dateFormats = intlConstants.dateFormats();
    String dateLongFormat = dateFormats[3];

    int yIndex = dateLongFormat.indexOf("y");
    int mIndex = dateLongFormat.indexOf("M");

    isYearBeforeMonth = (yIndex < mIndex);

    // Finding the start and end of weekend
    weekendStart = Integer.parseInt(intlConstants.weekendRange()[0]) - 1;
    weekendEnd = Integer.parseInt(intlConstants.weekendRange()[1]) - 1;

    // Finding the list of year names and the name of the current year
    Date year = new Date();
    for (int y = 0; y < 120; y++) {
      year.setYear(y);
      yearNames.addItem(yearFormatter.format(year));
      yearNames.setValue(y, Integer.toString(y));
    }
    yearNames.setSelectedIndex(this.getYear());
    yearName.setText(yearFormatter.format(this));

    // Finding the list of month names and the name of the current month
    date = DatePickerDate.getDateAtMonthStart();

    for (int i = 0; i < 12; i++) {
      date.setMonth(i);
      String monthStr = monthFormatter.format(date);
      monthNames.addItem(monthStr);
      monthNames.setValue(i, Integer.toString(i));
    }

    monthNames.setSelectedIndex(this.getMonth());
    monthName.setText(monthFormatter.format(this));

    // Finding today's date string
    Date today = DatePickerDate.getDateAtDayStart();
    todayCell = new DatePickerCell(fullDateFormatter.format(today));
    todayCell.setType(TYPE_CONTROL);
    setSpecialDay(SELECTED, this);
    setSpecialDay(TODAY, today);

    this.adjacentMonths = adjacentMonths;

    populateDatePickerGrid();
  }

  /**
   * Public method addMonths() add a positive or negative number to the date.
   * The day of the month will be pinned to the original value as far as
   * possible.
   * 
   * @param delta - number of months to be added to the current date
   * @return boolean - indicate whether change in year value happened or not
   */
  public boolean addMonths(int delta) {
    if (delta == 0) {
      return false;
    }

    boolean yearChanged = super.addMonths(delta);
    changeMonthYearStr(yearChanged);
    populateDatePickerGrid();
    return yearChanged;
  }

  /**
   * Public method addSpecialDay() add a date to the list of special dates that
   * require special formatting.
   * 
   * @param date - date that require special formatting
   */
  public DatePickerDate addSpecialDay(Date date) {

    return setSpecialDay(specialDates.size(), date);
  }

  /**
   * Public method dayOfMonthNames() returns an array of labels for days of a
   * month in the default locale.
   * 
   * @return array of size 31 with names for days of month in default locale
   */
  public DatePickerCell[] dayOfMonthNames() {

    return dayOfMonthNames;
  }

  /**
   * Public method dayOfMonthNamesNext() returns an array of labels for days of
   * the next month in the default locale.
   * 
   * @return array of size 31 with names for days of the next month in the
   *         default locale
   */
  public DatePickerCell[] dayOfMonthNamesNext() {
    return dayOfMonthNamesNext;
  }

  /**
   * Public method dayOfMonthNamesPrev() returns an array of labels for days of
   * the previous month in the default locale.
   * 
   * @return array of size 31 with names for days of the previous month in the
   *         default locale
   */
  public DatePickerCell[] dayOfMonthNamesPrev() {
    return dayOfMonthNamesPrev;
  }

  /**
   * Public method enableAdjacentMonths() enables or disables the display of
   * trailing and leading dates from previous and next months.
   * 
   * @param adjacentMonths - A boolean indicating whether display of trailing
   *          and leading dates from previous and next months.
   * 
   */
  public void enableAdjacentMonths(boolean adjacentMonths) {
    this.adjacentMonths = adjacentMonths;
    populateConstants();
  }

  /**
   * Public method gridStart() returns the column number in the grid for the
   * month start.
   * 
   * @return returns the column number in the grid for the month start.
   */
  public int gridStart() {
    return gridStart;
  }

  /**
   * Public method isYearBeforeMonth() returns whether the year is before month
   * in the current locale or not.
   * 
   * @return returns whether the year is before month in the current locale or
   *         not.
   */
  public boolean isYearBeforeMonth() {
    return isYearBeforeMonth;
  }

  /**
   * Public method monthName() returns the name of the current month in the
   * default locale.
   * 
   * @return returns the name of the current month in the default locale.
   */
  public Label monthName() {
    return monthName;
  }

  /**
   * Public method monthNames() returns a ListBox containing the 12 month names
   * in the default locale. Current month would be the set as selected.
   * 
   * @return returns a ListBox containing the 12 month names in the default
   *         locale.
   */
  public ListBox monthNames() {
    return monthNames;
  }

  /**
   * Public method nextMonthDays() returns number of days in the next month.
   * 
   * @return number of days in the next month.
   */
  public int nextMonthDays() {
    return nextMonthDays;
  }

  /**
   * Public method numSpecialDays() returns number of dates for which special
   * formatting is set.
   * 
   * @return number of number of dates for which special formatting is set.
   */
  public int numSpecialDays() {
    return specialDates.size();
  }

  /**
   * Public method prevMonthDays() returns number of days in the previous month.
   * 
   * @return number of days in the previous month.
   */
  public int prevMonthDays() {
    return prevMonthDays;
  }

  /**
   * Public method selectedDate() sets the date user selected.
   * 
   * @param monthType - Month type of the cell in which user clicked. Type can
   *          be current, previous or next month.
   * @param dayOfMonth - Selected day of the month
   */
  public void selectedDate(int monthType, int dayOfMonth) {

    if (monthType != LocaleCalendarUtils.TYPE_CURR_MONTH) {
      super.setDate(1);
      this.addMonths(monthType);
      populateConstants();
    }

    super.setDate(dayOfMonth);
    updateSpecialDays();
  }

  /**
   * Public method selectedDate() sets to the given date.
   * 
   * @param date - Date to be set.
   */
  public void setFullDate(Date date) {
    super.setFullDate(date);
    changeMonthYearStr(true);
    populateDatePickerGrid();
  }

  /**
   * Public method setMonth() sets to the given month.
   * 
   * @param month - Month to be set.
   */
  public void setMonth(int month) {
    super.setMonth(month);
    populateDatePickerGrid();
    changeMonthYearStr(false);
  }

  /**
   * Public method setToday() sets date to today's date. The tables exported by
   * this class are changed accordingly.
   * 
   * @return Boolean reflecting whether year has been changed or not.
   */
  public boolean setToday() {
    boolean yearChanged = super.setToday();
    populateDatePickerGrid();
    changeMonthYearStr(yearChanged);
    return yearChanged;
  }

  /**
   * Public method setYear() sets to the given year.
   * 
   * @param year - Year to be set.
   */
  public void setYear(int year) {
    super.setYear(year);
    populateDatePickerGrid();
    changeMonthYearStr(true);
  }

  /**
   * Public method specialDate() returns a date from the list of dates that
   * require special formatting.
   * 
   * @param i - position of the date entry in the special date list.
   */
  public DatePickerDate specialDate(int i) {
    return (DatePickerDate) specialDates.get(i);
  }

  /**
   * Public method todayCell() returns the Label for the cell displaying today's
   * date.
   */
  public DatePickerCell todayCell() {
    return todayCell;
  }

  /**
   * Public method weekendEnd() returns the day of the week on which weekend
   * ends. The range between 0 for Sunday and 6 for Saturday.
   * 
   * @return the day of the week on which weekend ends.
   */
  public int weekendEnd() {

    return weekendEnd;
  }

  /**
   * Public method weekendStart() returns the day of the week on which weekend
   * starts. The range between 0 for Sunday and 6 for Saturday.
   * 
   * @return the day of the week on which weekend starts.
   */
  public int weekendStart() {
    return weekendStart;
  }

  /**
   * Public method weekOfYear() returns a list of strings for week number of the
   * year for the weeks displayed as per the locale set.
   * 
   * @return List of strings for week number of the year for the weeks displayed
   *         as per the locale set.
   */
  public String[] weekOfYear() {

    String[] weekOfYear = new String[7];
    Date date = (Date) this.clone();

    for (int i = 1 - prevMonthDays; i < currMonthSize + nextMonthDays; i += 7) {
      date.setDate(i);
      weekOfYear[i] = weekOfYearFormatter.format(date);
    }

    return weekOfYear;
  }

  /**
   * Public method weekStart() returns the day of the week on which week starts
   * as per the locale. The range between 0 for Sunday and 6 for Saturday.
   * 
   * @return the day of the week on which week starts as per the locale.
   */
  public int weekStart() {
    return Integer.parseInt(intlConstants.firstDayOfTheWeek()) - 1;
  }

  /**
   * Public method yearName() returns the name of the current year in the
   * default locale.
   * 
   * @return returns the name of the current year in the default locale.
   */
  public Label yearName() {
    return yearName;
  }

  /**
   * Public method yearNames() returns a ListBox containing the 120 year names
   * in the default locale. Current year would be the set as selected.
   * 
   * @return returns a ListBox containing the 120 year names in the default
   *         locale.
   */
  public ListBox yearNames() {

    return yearNames;
  }

  private void changeMonthYearStr(boolean yearChanged) {
    monthName.setText(monthFormatter.format(this));
    monthNames.setSelectedIndex(this.getMonth());

    if (yearChanged) {
      yearName.setText(yearFormatter.format(this));
      yearNames.setSelectedIndex(this.getYear());
    }
  }

  private DatePickerCell[] createShadow(DatePickerCell[] original, int monthType) {

    DatePickerCell[] shadow = new DatePickerCell[31];

    for (int i = 0; i < 31; ++i) {
      shadow[i] = (DatePickerCell) original[i].clone();
      shadow[i].setType(monthType);
    }

    return shadow;
  }

  private void populateConstants() {

    int weekStart = this.weekStart();
    int dayOfWeek = super.getDay();
    int date = super.getDate();
    int month = super.getMonth();
    int year = super.getYear();
    // offset from Sunday == 0; +70 to make number +ve
    int offset = (dayOfWeek - date + 1 - weekStart + 70) % 7;
    int monthCount = year * 12 + month;

    currMonthSize = super.currMonthSize();

    if (adjacentMonths) {
      prevMonthDays = (monthCount > 0) ? (offset + 7) : 0; // for Jan 1900
      nextMonthDays = (monthCount < 120 * 12 - 1)
          ? (7 * 7 - prevMonthDays - currMonthSize) : 0; // <= Dec 2019
      gridStart = 0;
    } else {
      prevMonthDays = 0;
      nextMonthDays = 0;
      gridStart = offset;
    }
  }

  private void populateDatePickerGrid() {

    populateConstants();
    updateSpecialDays();
  }

  private DatePickerDate setSpecialDay(int i, Date d) {
    DatePickerDate day;
    if (i >= specialDates.size()) {
      day = new DatePickerDate(d);
      specialDates.add(i, day);
    } else {
      day = (DatePickerDate) specialDates.get(i);
      day.setFullDate(d);
    }
    day.setDayDiff(this, this.getDate() - 1);

    return day;
  }

  private void updateSpecialDays() {

    int dayOfMonth = this.getDate();
    this.setSpecialDay(SELECTED, this);

    Iterator it = specialDates.iterator();
    while (it.hasNext()) {
      DatePickerDate d = (DatePickerDate) it.next();
      d.setDayDiff(this, dayOfMonth - 1);
    }
  }
}
