package edu.tmc.uth.teo.model;

/**
 * Granularity is the attribute of a temporal_region, such as the event, the time_instant...
 * Unit is the attribute of a duration.
 * 
 * Event though "granularity" and "unit" have the same value in string: year, month, week, day, hour, minute and second, they
 * are just different meanings and are associated with different classes.
 * 
 * @author yluo
 *
 */
public class Granularity implements Comparable<Granularity> {
	private Unit unit;
	
	public Granularity() {
		this.unit = Unit.UNKNOWN;
	}
	
	public Granularity(Unit unit) {
		this.unit = unit;
	}
	
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	public Unit getUnit() {
		return this.unit;
	}

	public int compareTo(Granularity o) {
		return getUnit().compareTo(o.getUnit());
	}
}