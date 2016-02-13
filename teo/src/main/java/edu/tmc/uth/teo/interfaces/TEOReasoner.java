package edu.tmc.uth.teo.interfaces;

import java.util.HashMap;

import edu.tmc.uth.teo.model.Event;

/**
 * This is the interface of the TEOReasoner component.
 * 
 * It has two steps to go:
 * 1. reasonValidTime, which focuses on the absolute time information reasoning.
 * 2. reasonTemporalRelation, which focuses on the temporal relation reasoning.; 
 * 
 * @author yluo
 *
 */
public interface TEOReasoner {
	/**
	 * to complete the valid time of events
	 */
	public boolean reasonValidTime();
	
	/**
	 * To complete the relation graph
	 */
	public boolean reasonTemporalRelations();

	/**
	 * To get the eventh HashMap after the reasoning
	 */
	public HashMap<String, Event> getEventMap();
}
