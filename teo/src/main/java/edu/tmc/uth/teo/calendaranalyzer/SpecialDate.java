package edu.tmc.uth.teo.calendaranalyzer;

import java.util.Calendar;
import java.util.Date;

/**
 * This class represents a special date.
 * 
 * @author yluo
 *
 */
public class SpecialDate {
	int monthDay;
	int weekDay;
	int monthWeek;
	int month;
	int year;
	
	public int getMonthDay() {
		return monthDay;
	}

	public void setMonthDay(int monthDay) {
		this.monthDay = monthDay;
	}

	public int getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}

	public int getMonthWeek() {
		return monthWeek;
	}

	public void setMonthWeek(int monthWeek) {
		this.monthWeek = monthWeek;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public SpecialDate() {
		this.monthDay = 0;
		this.month = 0;
		this.monthWeek = 0;
		this.year = 0;
		this.weekDay = 0;
	}
	
	public void parseDate(Date date) {
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			this.monthDay = cal.get(Calendar.DAY_OF_MONTH);
			this.month = cal.get(Calendar.MONTH);
			this.monthWeek = cal.get(Calendar.WEEK_OF_MONTH);
			this.year = cal.get(Calendar.YEAR);
			this.weekDay = cal.get(Calendar.DAY_OF_WEEK);
		}
	}
	
	public String toString() {
		return "month: " + this.month + ", year: " + this.year + ", monthDay: " + this.monthDay + ", weekDay: " + this.weekDay + ", monthWeek: " + this.monthWeek; 
	}
}
