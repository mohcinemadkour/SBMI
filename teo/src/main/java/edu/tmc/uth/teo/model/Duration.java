package edu.tmc.uth.teo.model;

import edu.tmc.uth.teo.utils.DurationParser;
import edu.tmc.uth.teo.utils.DurationUtils;

/**
 * This is the class of Duration corresponding to the Duration class in TEO.
 *  
 * @author yluo
 *
 */
public class Duration extends TEOClass implements Comparable<Duration> {
	private AssemblyMethod assemblyMethod;
	
	private Unit durUnit; // display unit
	private String durStr; // display string
	
	private DurationValue durValue; // in milliseconds, should be computed.
	
	
	/**
	 * Constructor 1
	 */
	public Duration() {
		this.durStr = null;
		this.durUnit = Unit.UNKNOWN;
		this.durValue = null;
		this.assemblyMethod = AssemblyMethod.UNKNOWN;
	}
	
	/**
	 * Constructor 2, durValue is computed from durStr and durUnit
	 */
	public Duration(String durStr, Unit durUnit) {
		this.durStr = durStr;
		this.durUnit = durUnit;
		this.durValue = getDurValueFromStr(durStr, durUnit);
		this.assemblyMethod = AssemblyMethod.ASSERTED;
	}
	
	/**
	 * Constructor 3
	 */
	public Duration(DurationValue durValue) {
		this.durStr = null;
		this.durUnit = Unit.UNKNOWN;
		this.durValue = durValue;
		this.assemblyMethod = AssemblyMethod.INFERRED;
	}
	
	public void reset(String durStr, Unit durUnit) {
		this.durStr = durStr;
		this.durUnit = durUnit;
		this.durValue = getDurValueFromStr(durStr, durUnit);
	}
	
	public void setUnit(Unit durUnit) {
		this.durUnit = durUnit;
	}
	
	public Unit getUnit() {
		return this.durUnit;
	}
	
	public String getDurString() {
		return this.durStr;
	}
	
	public DurationValue getDurationValue() {
		return this.durValue;
	}
	
	public AssemblyMethod getAssemblyMethod() {
		return this.assemblyMethod;
	}
	
	public void setAssemblyMethod(AssemblyMethod method) {
		this.assemblyMethod = method;
	}
	
	public String toString() {
//		return "{[durUnit:" + this.durUnit + "]" + "[durStr:" + this.durStr + "]" + 
//				"[durValue:" + this.durValue + "]" + "[AssemblyMethod:" + this.assemblyMethod + "]" + "}";
		return "{[durStr:" + this.durStr + "]" +  "[durValue:" + this.durValue + "]" + 
				"[Granularity:" + this.getUnit() + "]" + "[AssemblyMethod:" + this.assemblyMethod + "]" + "}";
	}
	
//	public String toString(Unit unit) {
//		return ""  + ((this.durUnit != null)? ("{durUnit:" + this.durUnit + "}"):"") +
//				"{durStr:" + this.durStr + "}" + "{durValue:" + getDurValueInDifferentUnit(unit) + "}";
//	}
	
	public int compareTo(Duration other) {
		return this.durValue.compareTo(other.durValue);
	}
	
	public DurationValue getDurValueInDifferentUnit(Unit unit) {
		return DurationUtils.changeToUnit(this.getDurationValue(), unit);
	}

	/**
	 * 
	 * @param durStr
	 * @param durUnit
	 * @return
	 */
	public static DurationValue getDurValueFromStr(String durStr, Unit durUnit) {
		DurationValue durValue = DurationParser.parseDuration(durStr);
		if (durValue != null) {
			durValue.supressUnitsLowerthan(durUnit);
		}
		return durValue; // might be null
	}
}
