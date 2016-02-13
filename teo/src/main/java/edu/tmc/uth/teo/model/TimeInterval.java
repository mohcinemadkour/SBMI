package edu.tmc.uth.teo.model;

import edu.tmc.uth.teo.utils.DurationUtils;
import edu.tmc.uth.teo.utils.TimeUtils;


/**
 * Note: startTime, endTime and duration should be unified to the same granularity when initializing the TimeInterval.
 * 
 * Any 2 conditions can automatically lead to the 3rd condition.
 * 
 * @author yluo
 *
 */
public class TimeInterval extends ConnectedTemporalRegion {
	private TimeInstant startTime; // startTime has its own granularity
	private TimeInstant endTime; // endTime has its own granularity
	private Duration duration; // duration has its own duration unit (similar to granularity)
	
	// hidden super class filed: Granularity timeGranularity;
	
	public TimeInstant getStartTime() {
		return this.startTime;
	}
	
	public void setStartTime(TimeInstant startTime) {
		this.startTime = startTime;
	}
	
	public TimeInstant getEndTime() {
		return this.endTime;
	}
	
	public void setEndTime(TimeInstant endTime) {
		this.endTime = endTime;
	}
	
	public Duration getDuration() {
		return this.duration;
	}
	
	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	
	/**
	 * Constructor 0
	 * @param startTime
	 * @param endTime
	 */
	public TimeInterval() {
		this.startTime = null;
		this.endTime = null;
		this.setGranularity(new Granularity());
		this.duration = null;
	}
	
	/**
	 * Constructor 1
	 * @param startTime
	 * @param endTime
	 */
	public TimeInterval(TimeInstant startTime, TimeInstant endTime) {
		if (startTime != null && endTime != null) {
			this.startTime = startTime;
			this.endTime = endTime;
			this.setGranularity(TimeUtils.getCoarserGranularity(startTime, endTime));
			this.duration = TimeUtils.getDurationFrom(startTime, endTime, this.getGranularity()); // get the duration according to the current granularity
		}
	}
	
	/**
	 * Constructor 2
	 * @param startTime
	 * @param duration
	 */
	public TimeInterval(TimeInstant startTime, Duration duration) {
		if (startTime != null && duration != null) {
			this.startTime = startTime;
			this.duration = duration;
			this.setUnit(TimeUtils.getFinerUnit(startTime, duration));
			this.endTime = TimeUtils.getEndTimeInstantFrom(startTime, duration, this.getGranularity());
		}
	}
	
	/**
	 * Constructor 3
	 * @param duration
	 * @param endTime
	 */
	public TimeInterval(Duration duration, TimeInstant endTime) {
		if (startTime != null && duration != null) {
			this.endTime = endTime;
			this.duration = duration;
			this.setUnit(TimeUtils.getFinerUnit(startTime, duration));
			this.startTime = TimeUtils.getStartTimeInstantFrom(duration, endTime, this.getGranularity());
		}
	}
	
	
	public String toString() {
		return "{startTime:" + this.startTime + "\n"  + "EndTime:" + this.endTime + "\n" + "Duration:" + this.duration + "}";
	}
	
	/**
	 * This method checks if three given conditions (startTime, endTime, and duration) could lead to
	 * a valid time interval or not.
	 * 
	 * 1. start <= end; 
	 * 2. duration = end - start.
	 * 
	 * 3. always trust the two w/ finer granularities.
	 *     e.g.
	 *         startTime = Sept 2nd 2008, endTime = Oct 2012, duration = 4Y;
     *         we select startTime and endTime to calculate new duration.
	 * 
	 * @param startTime
	 * @param endTime
	 * @param duration
	 * @return
	 */
	public static boolean isValidTimeInterval(TimeInstant startTime, TimeInstant endTime, Duration duration) {
		if (startTime != null && endTime != null && duration != null) {
			if (startTime.compareTo(endTime) <= 0) { // start <= end??
				Granularity corserGran = TimeUtils.getCoarserGranularity(startTime, endTime);
				Unit maxUnit = corserGran.getUnit().compareTo(duration.getUnit()) > 0 ? corserGran.getUnit() : duration.getUnit();
				
				Duration computedDur = TimeUtils.getDurationFrom(startTime, endTime, new Granularity(maxUnit));// would be as accurate as possible
				DurationValue givenDurValue = DurationUtils.changeToUnit(duration.getDurationValue(), maxUnit);
					
			    if (computedDur.getDurationValue().compareTo(givenDurValue, maxUnit) == 0) { // duration = end - start.?? 
					return true;
				}
			}
		}
		return false;
	}
}
