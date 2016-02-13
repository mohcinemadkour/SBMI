package edu.tmc.uth.teo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


/**
 * The is the class of Event. 
 * It has 3 fields: 1. event type; 2. valid time; 3. relationMap.
 * 
 * The relationMap is a hash map with the target event IRI as the key and the temporal relation as the value.
 * 
 * @author yluo
 *
 */
public class Event extends TEOClass {
	private TemporalType eventType; 
	private TemporalRegion validTime; // might be TimeInsant, TimeInterval, PeriodicTimeInterval
	private HashMap<String, ArrayList<TemporalRelationInShortCode>> relationMap; // HashMap<targetIRI, relation list>: 1. better for retrieval; 2. compress the used memory
	
	public Event() {
		this.eventType = TemporalType.UNKNOWN;
		this.validTime = null;
		this.relationMap = null;
	}
	
	public Event(TemporalType type) {
		this.eventType = type;
		this.validTime = null;
		this.relationMap = null;
	}
	
	public TemporalType getEventType() {
		return this.eventType;
	}
	
	public void setEventType(TemporalType eventType) {
		this.eventType = eventType;
	}
		
	public TemporalRegion getValidTime() {
		return this.validTime;
	}
	
	public void setValidTime(TemporalRegion validTime) {
		this.validTime = validTime;
	}
	
	public HashMap<String, ArrayList<TemporalRelationInShortCode>> getTemporalRelations() {
		return this.relationMap;
	}
	
	public void addTemporalRelation(String targetIRI, TemporalRelationInShortCode relation) {
		if (relationMap == null) {
			relationMap = new HashMap<String, ArrayList<TemporalRelationInShortCode>>();
		} 
		if (relationMap.get(targetIRI) == null) {
			relationMap.put(targetIRI, new ArrayList<TemporalRelationInShortCode>());
		}
		if (!relationMap.get(targetIRI).contains(relation)) {
			relationMap.get(targetIRI).add(relation);
		}
	}
	
	public String printRelations() {
		if (relationMap != null) {
			StringBuffer buf = null;
			buf = new StringBuffer("{\n");
			Iterator<Entry<String, ArrayList<TemporalRelationInShortCode>>> it = relationMap.entrySet().iterator();
			
			while (it.hasNext()) {
				Entry<String, ArrayList<TemporalRelationInShortCode>> entry = it.next();
				ArrayList<TemporalRelationInShortCode> relationList = entry.getValue();
				for (TemporalRelationInShortCode oneRelation : relationList) {
					buf.append(oneRelation + "->" + entry.getKey() + "\n");
				}
			}
			buf.append("}");
			return buf.toString();
		} else {
			return null;
		}
	}
	
	public String toString() {
		return "EventIRI: " + getIRIStr() + "\nEventType: " + getEventType() + "\nhasValidTime: " + getValidTime() +
				"\nhasTemporalRelations:" + printRelations();
	}
}
