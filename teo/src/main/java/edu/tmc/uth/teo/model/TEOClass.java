package edu.tmc.uth.teo.model;

/**
 * The top TEOClass
 * 
 * @author yluo
 *
 */
public abstract class TEOClass {
	private String iriStr = null;
	private String label = null; // as unique id
	
	public String getIRIStr() {
		return this.iriStr;
	}

	public void setIRIStr(String iriStr) {
		this.iriStr = iriStr;
	}
	
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
