package edu.tmc.uth.teo.utils;

import edu.tmc.uth.teo.model.DurationValue;

/**
 * This class might be replaced by the Duration class in Java 8.
 * 
 * @author yluo
 * 
 */
public class DurationParser {

	/**
	 * Should be in the form of "4Y1M0W1D2H3m6s" (each attribute value must be an Integer)
	 * @param duration
	 * @return
	 */
	public static DurationValue parseDuration(String duration) {
		if (duration.contains("Y") || duration.contains("M") || duration.contains("W") || duration.contains("D") 
				|| duration.contains("H") || duration.contains("m") || duration.contains("s")) {
			DurationValue durValue = new DurationValue();
			int year = 0;
			int month = 0;
			int week = 0;
			int day = 0;
			int hour = 0;
			int minute = 0;
			int second = 0;
			
			if (duration.contains("Y")) {
				year = getIntValueBefore("Y", duration);
			}
			
			if (duration.contains("M")) {
				month = getIntValueBefore("M", duration);
			}
			
			if (duration.contains("W")) {
				week = getIntValueBefore("W", duration);
			}
			
			if (duration.contains("D")) {
				day = getIntValueBefore("D", duration);
			}
			
			if (duration.contains("H")) {
				hour = getIntValueBefore("H", duration);
			}
			
			if (duration.contains("m")) {
				minute = getIntValueBefore("m", duration);
			}
			
			if (duration.contains("s")) {
				second = getIntValueBefore("s", duration);
			}
			
			durValue.setYear(year);
			durValue.setMonth(month);
			durValue.setWeek(week);
			durValue.setDay(day);
			durValue.setHour(hour);
			durValue.setMinute(minute);
			durValue.setSecond(second);
			
			return durValue;
		} else {
			return null;
		}
	}
	
	public static int getIntValueBefore(String subStr, String origStr) {
		int index = origStr.indexOf(subStr);
		if (index != -1) {
			int start = 0;
			for (start = index - 1; start >= 0; start --) {
				if (origStr.charAt(start) < '0' || origStr.charAt(start) > '9') {
					break;
				}
			}
			start ++;
			int value = Integer.parseInt(origStr.substring(start, index));
			return value; 
		}
		return -1;
	}
	
	public static void main(String args[]) {
		System.out.println(DurationParser.parseDuration("01Y 0M 2W 0D 0H 2m 0s"));
	}	
}