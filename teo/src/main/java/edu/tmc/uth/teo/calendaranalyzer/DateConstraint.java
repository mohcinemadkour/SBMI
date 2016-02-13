package edu.tmc.uth.teo.calendaranalyzer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a rule/constraint of a special date.
 * 
 * @author yluo
 *
 */
public class DateConstraint {
	public static final int MAX_YEAR = 2020;
	public static final int MIN_YEAR = 1970;
	private int minYear, maxYear; // assumption: minYear = 1970, maxYear = 2020
	private Set<Integer> monthDay;
	private Set<Integer> weekDay;
	private Set<Integer> monthWeek;
	private Set<Integer> month;
	
	public DateConstraint() {
		this.maxYear = MAX_YEAR;
		this.minYear = MIN_YEAR;
		this.monthDay = null;; // Note: null means no constraint on this field , empty set means no elements
		this.monthWeek = null;
		this.weekDay = null;
		this.month = null;
	}
	
	public DateConstraint(int minYear, int maxYear, Set<Integer> monthDay,
			Set<Integer> weekDay, Set<Integer> monthWeek, Set<Integer> month) {
		super();
		this.minYear = minYear;
		this.maxYear = maxYear;
		this.monthDay = monthDay;
		this.weekDay = weekDay;
		this.monthWeek = monthWeek;
		this.month = month;
	}

	public int getMinYear() {
		return minYear;
	}
	public void setMinYear(int minYear) {
		if (minYear >= MIN_YEAR && minYear <= maxYear)
			this.minYear = minYear;
	}
	
	public int getMaxYear() {
		return maxYear;
	}
	public void setMaxYear(int maxYear) {
		if (maxYear <= MAX_YEAR && maxYear >= minYear)
			this.maxYear = maxYear;
	}
	
	public Set<Integer> getMonthDay() {
		return monthDay;
	}
	public void setMonthDay(Set<Integer> monthDay) {
		this.monthDay = monthDay;
	}
	public void addMonthDay(int i) {
		assert(i >= 1 && i <= 31);
		if (monthDay == null)
			monthDay = new HashSet<Integer>();
		monthDay.add(i);
	}
	

	public Set<Integer> getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(Set<Integer> weekDay) {
		this.weekDay = weekDay;
	}
	public void addWeekDay(int i) {
		assert(i >= Calendar.SUNDAY && i <= Calendar.SATURDAY);
		if (weekDay == null)
			weekDay = new HashSet<Integer>();
		weekDay.add(i);
	}
	

	public Set<Integer> getMonthWeek() {
		return monthWeek;
	}
	public void setMonthWeek(Set<Integer> monthWeek) {
		this.monthWeek = monthWeek;
	}
	public void addMonthWeek(int i) {
		assert(i >= 1 && i <= 5);
		if (monthWeek == null)
			monthWeek = new HashSet<Integer>();
		monthWeek.add(i);
	}
	
	public Set<Integer> getMonth() {
		return month;
	}
	public void setMonth(Set<Integer> month) {
		this.month = month;
	}
	public void addMonth(int i) {
		assert(i >= Calendar.JANUARY && i <= Calendar.DECEMBER);
		if (month == null)
			month = new HashSet<Integer>();
		month.add(i);
	}
	
	public String diplaySetElements(Set<Integer> set) {
		if (set == null) return "null";
		String result = "";
		if (set != null && !set.isEmpty()) {
			Integer[] setArray = set.toArray(new Integer[set.size()]);
			result += setArray[0];
			for (int i = 1; i < setArray.length; i ++) {
				result += (", " + setArray[i]);
			}
		}
		return result;
	}
	
	public String toString() {
		String result = "";
		result += "{[Year: (" + minYear + ", " + maxYear + ")]\n";
		result += "[Month: (" + diplaySetElements(month) + ")]\n";
		result += "[MonthDay: (" + diplaySetElements(monthDay) + ")]\n";
		result += "[Week: (" + diplaySetElements(monthWeek) + ")]\n";
		result += "[WeekDay: (" + diplaySetElements(weekDay) + ")]}";
		return result;
	}
	
	/**
	 * Return a new constraint which is the merged deep copy of two given constraints
	 * 
	 * constraint1 AND constraint2
	 * 
	 * @param constraint1
	 * @param constraint2
	 * @return
	 */
	public static DateConstraint intersectionDateConstraint(DateConstraint constraint1, DateConstraint constraint2) {
		int minYear = DateConstraint.MIN_YEAR, maxYear = DateConstraint.MAX_YEAR; // assumption: minYear = 1970, maxYear = 2020
		Set<Integer> monthDay = null;
		Set<Integer> weekDay = null;
		Set<Integer> monthWeek = null;
		Set<Integer> month = null;
		
		// intersect year
		if (constraint1.getMaxYear() < constraint2.getMinYear() || constraint1.getMinYear() >constraint2.getMaxYear()) {
			System.out.println("Error: no overlap on two year ranges!");
		} else {
			minYear = Math.max(constraint1.getMinYear(), constraint2.getMinYear());
			maxYear = Math.min(constraint1.getMaxYear(), constraint2.getMaxYear());
		}
		// intersect month
		if (constraint1.getMonth() != null) {
			month = new HashSet<Integer>(constraint1.getMonth());
			if (constraint2.getMonth() != null) { // set intersection
				month.retainAll(constraint2.getMonth());
			}
		} else if (constraint2.getMonth() != null) {
			month = new HashSet<Integer>(constraint2.getMonth());
		}
		// intersect monthDay
		if (constraint1.getMonthDay() != null) {
			monthDay = new HashSet<Integer>(constraint1.getMonthDay());
			if (constraint2.getMonthDay() != null) { // set intersection
				monthDay.retainAll(constraint2.getMonthDay());
			}
		} else if (constraint2.getMonthDay() != null) {
			monthDay = new HashSet<Integer>(constraint2.getMonthDay());
		}
		// intersect weekDay
		if (constraint1.getWeekDay() != null) {
			weekDay = new HashSet<Integer>(constraint1.getWeekDay());
			if (constraint2.getWeekDay() != null) { // set intersection
				weekDay.retainAll(constraint2.getWeekDay());
			}
		} else if (constraint2.getWeekDay() != null) {
			weekDay = new HashSet<Integer>(constraint2.getWeekDay());
		}
		// intersect monthWeek
		if (constraint1.getMonthWeek() != null) {
			monthWeek = new HashSet<Integer>(constraint1.getMonthWeek());
			if (constraint2.getMonthWeek() != null) { // set intersection
				monthWeek.retainAll(constraint2.getMonthWeek());
			}
		} else if (constraint2.getMonthWeek() != null) {
			monthWeek = new HashSet<Integer>(constraint2.getMonthWeek());
		}
		
		DateConstraint newConstraint = new DateConstraint(minYear, maxYear, monthDay, weekDay, monthWeek, month);
		
		return newConstraint;
	}
	
	/**
	 * This method enumerates all possible dates in the format of "MM/dd/yyyy" according to 
	 * the given constraints.
	 *  
	 * @param dateConstraint
	 * @return
	 */
	public static ArrayList<String> enumerateDates(DateConstraint dateConstraint) {
		// Note: to enumerate concrete dates, we need either: 
		// 0. maxYear - minYear <= 10
		// 1. year + month + monthDay
		// 2. year + month + monthWeek + weekDay
		// ...more options in the future...
		ArrayList<String> dates = new ArrayList<String>(); 
		
		if (dateConstraint != null && (dateConstraint.getMaxYear() - dateConstraint.getMinYear() <= 10) &&
			((dateConstraint.getMonth() != null && !dateConstraint.getMonth().isEmpty() && dateConstraint.getMonthDay() != null && !dateConstraint.getMonthDay().isEmpty()) || 
			 (dateConstraint.getMonth() != null && !dateConstraint.getMonth().isEmpty() && dateConstraint.getMonthWeek() != null && !dateConstraint.getMonthWeek().isEmpty() &&
					 dateConstraint.getWeekDay() != null && !dateConstraint.getWeekDay().isEmpty()))) {
			
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyy");
			Calendar newDate = Calendar.getInstance();
			
			for (int year = dateConstraint.getMinYear(); year <= dateConstraint.getMaxYear(); year ++) {
				newDate.set(Calendar.YEAR, year);
				for (Integer month : dateConstraint.getMonth()) {
					newDate.set(Calendar.MONTH, month);

					if (dateConstraint.getMonthDay() != null && !dateConstraint.getMonthDay().isEmpty()) {
						for (Integer monthDay : dateConstraint.getMonthDay()) {
							newDate.set(Calendar.DAY_OF_MONTH, monthDay);
							// check the consistency with week/weekday
							if ((dateConstraint.getMonthWeek() != null && dateConstraint.getMonthWeek().contains(newDate.get(Calendar.WEEK_OF_MONTH)) && 
									dateConstraint.getWeekDay() != null && dateConstraint.getWeekDay().contains(newDate.get(Calendar.DAY_OF_WEEK))) 
								|| ((dateConstraint.getMonthWeek() == null || dateConstraint.getMonthWeek().isEmpty()) && 
										(dateConstraint.getWeekDay() != null && dateConstraint.getWeekDay().contains(newDate.get(Calendar.DAY_OF_WEEK))))
								|| ((dateConstraint.getMonthWeek() != null && dateConstraint.getMonthWeek().contains(newDate.get(Calendar.WEEK_OF_MONTH))) && 
										(dateConstraint.getWeekDay() == null || dateConstraint.getWeekDay().isEmpty()))
								|| ((dateConstraint.getMonthWeek() == null || dateConstraint.getMonthWeek().isEmpty()) && 
										(dateConstraint.getWeekDay() == null || dateConstraint.getWeekDay().isEmpty()))){
								// eject the date
								dates.add(df.format(newDate.getTime()));
							}
						}
					} else { // then both .getMonthWeek() and .getWeekDay() should not be empty
						for (Integer monthWeek : dateConstraint.getMonthWeek()) {
							newDate.set(Calendar.WEEK_OF_MONTH, monthWeek);
							for (Integer weekDay : dateConstraint.getWeekDay()) {
								newDate.set(Calendar.DAY_OF_WEEK, weekDay);
								// eject the date
								dates.add(df.format(newDate.getTime()));
							}
						}
					}
				}
			}		
		} 
		
		return dates;
	}
}
