package edu.tmc.uth.teo.utils;

import java.util.Calendar;
import java.util.Date;

import edu.tmc.uth.teo.model.Duration;
import edu.tmc.uth.teo.model.DurationValue;
import edu.tmc.uth.teo.model.Granularity;
import edu.tmc.uth.teo.model.TimeInstant;
import edu.tmc.uth.teo.model.TimeInterval;
import edu.tmc.uth.teo.model.Unit;

public class TimeUtils {
	
	public static long getDeltaMilliseconds(TimeInstant t1, TimeInstant t2) {
		return Math.abs(t1.getNormalizedTime() - t2.getNormalizedTime());
	}
	
	public static Granularity getCoarserGranularity(TimeInstant t1, TimeInstant t2) {
		return (t1.getGranularity().compareTo(t2.getGranularity()) > 0) ? t1.getGranularity() : t2.getGranularity();
	}
	
	public static Granularity getFinerGranularity(TimeInstant t1, TimeInstant t2) {
		return (t1.getGranularity().compareTo(t2.getGranularity()) < 0) ? t1.getGranularity() : t2.getGranularity();
	}
	
	public static Unit getCoarserUnit(TimeInstant t, Duration dur) {
		return (t.getGranularity().getUnit().compareTo(dur.getUnit()) > 0) ? t.getGranularity().getUnit() : dur.getUnit();
	}
	
	public static Unit getFinerUnit(TimeInstant t, Duration dur) {
		return (t.getGranularity().getUnit().compareTo(dur.getUnit()) < 0) ? t.getGranularity().getUnit() : dur.getUnit();
	}
	
	
	public static Unit getUnitFromString(String str) {
		if (str.equals("year")) return Unit.YEAR;
		if (str.equals("month")) return Unit.MONTH;
		if (str.equals("week")) return Unit.WEEK;
		if (str.equals("day")) return Unit.DAY;
		if (str.equals("hour")) return Unit.HOUR;
		if (str.equals("minute")) return Unit.MINUTE;
		if (str.equals("second")) return Unit.SECOND;
		return Unit.UNKNOWN;
	}
	
	/**
	 * Given start time, end time and the desired granularity, this method calculates the duration.
	 * 
	 * Note: duration in a single unit, which means the result ought to be "14 months" rather than "1 year 2 months"
	 * 
	 * @param startTimeInstant
	 * @param endTimeInstant
	 * @param gran
	 * @return
	 */
	public static Duration getDurationFrom(TimeInstant startTimeInstant, TimeInstant endTimeInstant, Granularity gran) {
		if (startTimeInstant != null && endTimeInstant != null && startTimeInstant.compareTo(endTimeInstant) <= 0) {
			long delta = getDeltaMilliseconds(startTimeInstant, endTimeInstant);
			Granularity coarserGran = getCoarserGranularity(startTimeInstant, endTimeInstant);
				
			if (gran == null || gran.getUnit().compareTo(Unit.UNKNOWN) == 0) {
//				System.out.println("----------->Desired Unit: " + coarserGran);
				gran = coarserGran;
			} // if the desired granularity is null or unknown
			
			if (coarserGran.compareTo(gran) > 0) {
				System.out.println("Note:the available granularity is '" + coarserGran.getUnit() + "', the desired granularity is '" + gran.getUnit() + "'.");
				System.out.println("\tThe duration result may not be accurate.");
			}
			
			DurationValue durValue = new DurationValue();
			
			if (gran.getUnit().compareTo(Unit.YEAR) == 0) {
				int year = (int) (delta / 1000 / 3600 / 24 / 365);
				durValue.setYear(year);
			}
			if (gran.getUnit().compareTo(Unit.MONTH) == 0) {
				int month = (int) (delta / 1000 / 3600 / 24 / 30);
				durValue.setMonth(month);
			}
			if (gran.getUnit().compareTo(Unit.WEEK) == 0) {
				int week = (int) (delta / 1000 / 3600 / 24 / 7);
				durValue.setWeek(week);
			}
			if (gran.getUnit().compareTo(Unit.DAY) == 0) {
				int day = (int) (delta / 1000 / 3600 / 24);
				durValue.setDay(day);
			}
			if (gran.getUnit().compareTo(Unit.HOUR) == 0) {
				int hour = (int) (delta / 1000 / 3600);
				durValue.setHour(hour);
			}
			if (gran.getUnit().compareTo(Unit.MINUTE) == 0) {
				int minute = (int) (delta / 1000 / 30);
				durValue.setMinute(minute);
			}
			if (gran.getUnit().compareTo(Unit.SECOND) == 0 || gran.getUnit().compareTo(Unit.UNKNOWN) == 0) {
				int second = (int) (delta / 1000);
				durValue.setSecond(second);
			}
			
			Duration duration = new Duration(durValue); // an inferred duration (no source string)
			duration.setUnit(gran.getUnit());
			
			return duration;
		}
		return null;
	}
		
	/**
	 * Given start time instant and the duration, this method calculates the end time instant
	 * 
	 * @param startTimeInstant
	 * @param duration
	 * @param gran
	 * @return
	 */
	public static TimeInstant getEndTimeInstantFrom(TimeInstant startTimeInstant, Duration duration, Granularity gran) {
		if (startTimeInstant != null && duration != null) {
			if (startTimeInstant.getGranularity().getUnit().compareTo(duration.getUnit()) > 0) {
				System.out.println("Note:the time instant has granularity '" + startTimeInstant.getGranularity().getUnit() + "', the duration has '" + gran.getUnit() + "'.");
				System.out.println("\tThe duration result may not be accurate.");
			}
			
			Unit finerUnit = getFinerUnit(startTimeInstant, duration);
			
			if (gran == null || gran.getUnit().compareTo(Unit.UNKNOWN) == 0) {
//				System.out.println("----------->Desired Unit: " + finerUnit);
				gran = new Granularity(finerUnit);
			} // if the desired granularity is null or unknown
			
			if (finerUnit.compareTo(gran.getUnit()) > 0) {
				System.out.println("Note:the available granularity is '" + finerUnit + "', the desired granularity is '" + gran.getUnit() + "'.");
				System.out.println("\tThe duration result may not be accurate.");
			}
			
			Calendar endCal = Calendar.getInstance();
			endCal.setTimeInMillis(startTimeInstant.getNormalizedTime());
			
			DurationValue durValue = duration.getDurationValue();
			
			endCal.add(Calendar.YEAR, durValue.getYear());
			endCal.add(Calendar.MONTH, durValue.getMonth());
			endCal.add(Calendar.DAY_OF_YEAR, durValue.getWeek() * 7 + durValue.getDay()); // consider the weeks together
			endCal.add(Calendar.HOUR, durValue.getHour());
			endCal.add(Calendar.MINUTE, durValue.getMinute());
			endCal.add(Calendar.SECOND, durValue.getSecond());
			
			Date endDate = DateUtils.setGranularity(endCal.getTime(), gran.getUnit());
		
			TimeInstant endTimeInstant = new TimeInstant(endDate.getTime()); // an inferred time instant (no source string)
			endTimeInstant.setGranularity(gran);
			
			return endTimeInstant;
		}
		return null;
	}
	
	/**
	 * Given end time instant and the duration, this method calculates the start time instant
	 * 
	 * @param duration
	 * @param endTimeInstant
	 * @param gran
	 * @return
	 */
	public static TimeInstant getStartTimeInstantFrom(Duration duration, TimeInstant endTimeInstant, Granularity gran) {
		if (endTimeInstant != null && duration != null) {
			if (endTimeInstant.getGranularity().getUnit().compareTo(duration.getUnit()) > 0) {
				System.out.println("Note:the time instant has granularity '" + endTimeInstant.getGranularity().getUnit() + "', the duration has '" + gran.getUnit() + "'.");
				System.out.println("\tThe duration result may not be accurate.");
			}
			
			Unit finerUnit = getFinerUnit(endTimeInstant, duration);
			
			if (gran == null || gran.getUnit().compareTo(Unit.UNKNOWN) == 0) {
//				System.out.println("----------->Desired Unit: " + finerUnit);
				gran = new Granularity(finerUnit);
			} // if the desired granularity is null or unknown
			
			if (finerUnit.compareTo(gran.getUnit()) > 0) {
				System.out.println("Note:the available granularity is '" + finerUnit + "', the desired granularity is '" + gran.getUnit() + "'.");
				System.out.println("\tThe duration result may not be accurate.");
			}
			
			Calendar startCal = Calendar.getInstance();
			startCal.setTimeInMillis(endTimeInstant.getNormalizedTime());
			
			DurationValue durValue = duration.getDurationValue();
			
			startCal.add(Calendar.YEAR, - durValue.getYear());
			startCal.add(Calendar.MONTH, - durValue.getMonth());
			startCal.add(Calendar.DAY_OF_YEAR, - durValue.getWeek() * 7 - durValue.getDay()); // consider the weeks together
			startCal.add(Calendar.HOUR, - durValue.getHour());
			startCal.add(Calendar.MINUTE, - durValue.getMinute());
			startCal.add(Calendar.SECOND, - durValue.getSecond());
			
			Date endDate = DateUtils.setGranularity(startCal.getTime(), gran.getUnit());
		
			TimeInstant startTimeInstant = new TimeInstant(endDate.getTime()); // an inferred time instant (no source string)
			startTimeInstant.setGranularity(gran);
			
			return startTimeInstant;
		}
		return null;
	}
		
	public static void main(String args[]) {
//		TimeInstant t1 = new TimeInstant("10:30:45 5/15/2009", new Granularity(Unit.SECOND));
//		Duration d1 = new Duration("2Y", Unit.YEAR);
//		System.out.println(getEndTimeInstantFrom(t1, d1, null));
		
		TimeInstant t1 = new TimeInstant("Sept 1 2008", new Granularity(Unit.DAY));
		TimeInstant t2 = new TimeInstant("July 2012", new Granularity(Unit.MONTH));
		Duration d1 = new Duration("3Y", Unit.YEAR);
		System.out.println(TimeInterval.isValidTimeInterval(t1, t2, d1));
	}
}
