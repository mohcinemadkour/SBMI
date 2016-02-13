package edu.tmc.uth.teo.utils;

import edu.tmc.uth.teo.model.DurationValue;
import edu.tmc.uth.teo.model.Unit;

public class DurationUtils {
	/**
	 * Note: "8 months" = "0 year", "14 months" = "1 year"
	 * @return
	 */
	public static DurationValue changeToUnit(DurationValue origDurValue, Unit unit) {
		DurationValue durValue = new DurationValue();
		
		if (unit.compareTo(Unit.YEAR) == 0) {
			int year = origDurValue.getYear() + origDurValue.getMonth() / 12 + (origDurValue.getWeek() * 7 + origDurValue.getDay()) / 365 +
						origDurValue.getHour() / 24 / 365 + origDurValue.getMinute() / 60 / 24 / 365 + origDurValue.getSecond() / 3600 / 24 / 365;
			durValue.setYear(year);
		}
		
		if (unit.compareTo(Unit.MONTH) == 0) {
			int month = origDurValue.getYear() * 12 + origDurValue.getMonth() + (origDurValue.getWeek() * 7 + origDurValue.getDay()) / 30 +
						origDurValue.getHour() / 24 / 30 + origDurValue.getMinute() / 60 / 24 / 30 + origDurValue.getSecond() / 3600 / 24 / 30;
			durValue.setMonth(month);
		}
		
		if (unit.compareTo(Unit.WEEK) == 0) {
			int week = origDurValue.getYear() * 365 / 7 + origDurValue.getMonth() * 30 / 7 + origDurValue.getWeek() + origDurValue.getDay() / 7 +
						origDurValue.getHour() / 24 / 7 + origDurValue.getMinute() / 60 / 24 / 7 + origDurValue.getSecond() / 3600 / 24 / 7;
			durValue.setWeek(week);
		}
		
		if (unit.compareTo(Unit.DAY) == 0) {
			int day = origDurValue.getYear() * 365 + origDurValue.getMonth() * 30 + origDurValue.getWeek() * 7 + origDurValue.getDay() +
						origDurValue.getHour() / 24 + origDurValue.getMinute() / 60 /24 + origDurValue.getSecond() / 3600 / 24;
			durValue.setDay(day);
		}
		
		if (unit.compareTo(Unit.HOUR) == 0) {
			int hour = origDurValue.getYear() * 365 * 24 + origDurValue.getMonth() * 30 * 24 + origDurValue.getWeek() *7 * 24 + origDurValue.getDay() * 24 +
						origDurValue.getHour() + origDurValue.getMinute() / 60 + origDurValue.getSecond() / 3600;
			durValue.setHour(hour);
		}
		
		if (unit.compareTo(Unit.MINUTE) == 0) {
			int minute = origDurValue.getYear() * 365 * 24 * 60 + origDurValue.getMonth() * 30 * 24 * 60 + origDurValue.getWeek() *7 * 24 * 60 + 
							origDurValue.getDay() * 24 * 60 + origDurValue.getHour() * 60 + origDurValue.getMinute() + origDurValue.getSecond() / 60;
			durValue.setMinute(minute);
		}
		
		if (unit.compareTo(Unit.SECOND) == 0) {
			int second = origDurValue.getYear() * 365 * 24 * 3600 + origDurValue.getMonth() * 30 * 24 * 3600 + origDurValue.getWeek() *7 * 24 * 3600 + 
							origDurValue.getDay() * 24 * 3600 + origDurValue.getHour() * 3600 + origDurValue.getMinute() + origDurValue.getSecond();
			durValue.setSecond(second);
		}
		return durValue;
	}
}
