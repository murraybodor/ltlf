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
/**
 * DatePickerDate public class is used by DatePicker UI class. This class
 * does the following extra calendar calculations required for displaying
 * dates in a  grid:
 * 
 * <ul>
 * <li>Size of the current and previous months</li>
 * <li>Time is kept at 00:00:00 for day difference calculation correctness</li>
 * <li>Pinnable date functionality. That is, when month is incremented, day of
 * month is kept close to the original value as far as possible. As an example,
 * if current date is May 31, incrementing the month return June 30.
 *  Incrementing it again will return July 31.</li>
 * <li>Adding months to current date</li>
 * <li>Set and get a string tag to the date</li>
 * <li>Reset the date to today's date</li>
 * </ul>
  */

public class DatePickerDate extends Date {

  private static int diffDays(Date a, Date b) {
    long aTime  = a.getTime();
    long bTime  = b.getTime();

    long adjust = 60 * 60 * 1000;
    adjust = (bTime > aTime) ? adjust : -adjust; 

    int diff  = (int)((bTime - aTime + adjust) / (24 * 60 * 60 * 1000));

    return diff;
  }

  /**
   * Public static method getDateAtDayStart() return the Date object with
   * time set to 00:00:00. Keeping a fixed the time of day intended to make
   * it easier to find day differences of dates that are
   * initiated to different times of the day.
   *  
   * @return new Date object
   */
  public static Date getDateAtDayStart() {
    Date date = new Date();
    DatePickerDate.resetTime(date);
    return date;
  }

  /**
   * Public static method getDateAtMonthStart() return the Date object with
   * date set to 1 and time set to 00:00:00.
   *  
   * @return new Date object
   */
  public static Date getDateAtMonthStart() {
    Date date = DatePickerDate.getDateAtDayStart();
    date.setDate(1);
    return date;
  }

  private static void resetTime(Date date) {
    long msec = date.getTime();
    msec = (msec / 1000) * 1000;
    date.setTime(msec);

    date.setHours(0);
    date.setMinutes(0);
    date.setSeconds(0);
  }

  private int prevMonthSize;
  private int currMonthSize;
  private int pinnedDate;
  private int dayDiff;
  private String tag;

  DatePickerDate() {
    DatePickerDate.resetTime(this);
    this.pinnedDate = super.getDate();
    updateConstants();
  }

  DatePickerDate (Date a) {
    this.setFullDate(a);
  }

  /**
   * Public method addMonths() add a positive or negative number to the date.
   * The day of the month will be pinned to the original value
   * as far as possible.
   * @param deltaMonths - number of months to be added to the current date
   * @return boolean - indicate whether change in year value happened or not
   */
  public boolean addMonths(int deltaMonths) {

    if (deltaMonths == 0) {
      return false;
    }

    int month = super.getMonth();
    int year = super.getYear();

    int resultMonthCount = year * 12 + month + deltaMonths;
    int resultYear = resultMonthCount / 12;
    int resultMonth = resultMonthCount - resultYear * 12;

    if (resultMonthCount < 0 || resultMonthCount >= 120 * 12) {
      return false;
    }

    super.setDate(1);
    super.setMonth(resultMonth);
    super.setYear(resultYear);
    updateConstants();

    super.setDate(this.getPinnedDate());

    return (year != resultYear);
  }

  /**
   * Public method currMonthSize() returns size of the current month
   * 
   * @return number of days in the current month
   */
  public int currMonthSize() {
    return currMonthSize;
  }

  /**
   * Public method dayDiff() returns difference in days from the date given
   * for setDayDiff().
   * 
   * @return difference in number of days.
   */
  public int dayDiff() {
    return this.dayDiff;
  }

  /**
   * Public method prevMonthSize() returns size of the month previous to current
   * one.
   * 
   * @return number of days in the previous month
   */
  public int prevMonthSize() {
    return prevMonthSize;
  }

  /**
   * Public method setDate() sets the day of the month to the given value.
   * It also sets that as the pinned value for day of the month.
   * 
   * @param date - day of the month to be set 
   */
  public void setDate(int date) {
    this.pinnedDate = date;
    super.setDate(getPinnedDate());
  }

  /**
   * Public method setDayDiff() stores the difference in days from a given date,
   * plus a given offset.
   * 
   * @param date - the date from which difference has to be computed
   * @param offset - the offset to be added to the set value 
   */
  public void setDayDiff(Date date, int offset) {
    this.dayDiff = DatePickerDate.diffDays(date, this) + offset;
  }

  /**
   * Public method setFullDate() sets the given date. However, the time of the
   * day is set to 00:00:00 so that it should not affect the computations.
   * Similarly, the pinned date is also set to the day of the month of the date
   * given.
   * 
   * @param date - date to be set 
   * 
   */
  public void setFullDate(Date date) {

    Date a = (Date)date.clone();
    DatePickerDate.resetTime(a);
    super.setTime(a.getTime());
    this.pinnedDate = a.getDate();
    updateConstants();
  }

  /**
   * Public method setMonth() sets the month to the given value.
   * 
   * @param month - month to be set 
   */
  public void setMonth(int month) {
    super.setMonth(month);
    updateConstants();
  }

  /**
   * Public method setTag() sets a string tag
   * 
   * @param tag - a string tag
   */
  public void setTag(String tag) {
    this.tag = tag;
  }

  /**
   * Public method setToday() resets the date to today's date. Pinned date value
   * would be set to day of the month for today.
   * 
   * @return boolean - indicate whether change in year value happened or not
   */
  public boolean setToday() {
    Date date = DatePickerDate.getDateAtDayStart();

    int currYear = this.getYear();
    int nextYear = date.getYear();

    this.setTime(date.getTime());
    this.pinnedDate = date.getDate();

    return currYear != nextYear;
  }

  /**
   * Public method setYear() sets the year to the given value.
   * 
   * @param year - year to be set 
   */
  public void setYear(int year) {
    super.setYear(year);
    updateConstants();
  }

  /**
   * Public method tag() gets the a string tag assigned
   * 
   * @return tag - assigned string tag
   */
  public String tag() {
    return this.tag;
  }

  /**
   * Private method getNumberOfDaysInMonth() gets number of days in the month
   * indicated.
   * 
   * @param deltaMonth - the position of the month with respect to current month 
   * 
   * @return number of days in the month
   */

  private int getNumberOfDaysInMonth(int deltaMonth) {

    int month = this.getMonth();
    int year  = this.getYear();

    Date a = DatePickerDate.getDateAtMonthStart();
    Date b = DatePickerDate.getDateAtMonthStart();

    int resultMonthCount = year * 12 + month + deltaMonth;

    int aYear  = resultMonthCount / 12;
    int aMonth = resultMonthCount - aYear * 12;

    resultMonthCount++;
    
    int bYear  = resultMonthCount / 12;
    int bMonth = resultMonthCount - bYear * 12;
    
    a.setMonth(aMonth);
    a.setYear(aYear);    

    b.setMonth(bMonth);
    b.setYear(bYear);    

    int diff = diffDays(a, b);

    return (diff >= 0) ? diff : -diff;
  }

  private int getPinnedDate() {
    int date = this.pinnedDate;

    return (date < currMonthSize ? date : currMonthSize);
  }

  private void updateConstants() {
    currMonthSize = getNumberOfDaysInMonth(0);
    prevMonthSize = getNumberOfDaysInMonth(-1);
  }
}
