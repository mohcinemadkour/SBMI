package edu.tmc.uth.teo.interfaces;

import java.util.HashMap;

import edu.tmc.uth.teo.model.Event;

/**
 * This is the interface of the TEOParser component which parses the ontology and generates
 * an event HashMap representing the graph (event node and relations between events).
 * 
 */
public interface TEOParser {

	/**
	 * To parse the ontology.
	 */
	public boolean parse();
	
	/**
	 * Get the count of events.
	 */
	public int getEventCount();
	
	/**
	 * Get the generated HashMap which represents the graph
	 */
	public HashMap<String, Event> getEventMap();
}
