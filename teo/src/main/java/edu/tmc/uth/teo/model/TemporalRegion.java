package edu.tmc.uth.teo.model;

/**
 * This class is the temporal_region in TEO 
 * 
 * @author yluo
 *
 */
public abstract class TemporalRegion extends TEOClass{
	protected Granularity timeGranularity;
	
	TemporalRegion() {
		this.timeGranularity = new Granularity(Unit.UNKNOWN);
	}
	
	public Granularity getGranularity() {
		return this.timeGranularity;
	}
	
	public void setGranularity(Granularity gran) {
		this.timeGranularity = gran;
	}
	
	public void setUnit(Unit unit) {
		this.timeGranularity.setUnit(unit);
	}
}
