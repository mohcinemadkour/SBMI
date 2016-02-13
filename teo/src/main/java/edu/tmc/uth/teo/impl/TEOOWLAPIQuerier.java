package edu.tmc.uth.teo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.semanticweb.owlapi.util.MultiMap;

import edu.tmc.uth.teo.datastruct.DirectedAcyclicGraph;
import edu.tmc.uth.teo.datastruct.Edge;
import edu.tmc.uth.teo.datastruct.UnionFindSet;
import edu.tmc.uth.teo.interfaces.TEOQuerier;
import edu.tmc.uth.teo.model.Duration;
import edu.tmc.uth.teo.model.Event;
import edu.tmc.uth.teo.model.Granularity;
import edu.tmc.uth.teo.model.TemporalRelationInShortCode;
import edu.tmc.uth.teo.model.TemporalType;
import edu.tmc.uth.teo.model.TimeInstant;
import edu.tmc.uth.teo.model.TimeInterval;
import edu.tmc.uth.teo.utils.TEOConstants;
import edu.tmc.uth.teo.utils.TemporalRelationUtils;
import edu.tmc.uth.teo.utils.TimeUtils;

/**
 * This is the implementation of the TEOQuerier component which realizes APIs that can be called by users.
 * 
 * @author yluo
 *
 */
public class TEOOWLAPIQuerier implements TEOQuerier {
	public HashMap<String, Event> eventMap = null;
	
	public TEOOWLAPIQuerier(HashMap<String, Event> eventMap) {
		this.eventMap = eventMap;
	}
	
	/**
	 * To retrieve an event by its IRI String
	 */
	public Event getEventByIRIStr(String IRIStr) {
		if (IRIStr != null && this.eventMap != null) {
			return this.eventMap.get(IRIStr);
		}
		return null;
	}

	/**
	 * To get the duration of a TimeInterval event
	 * 
	 * If the given event is a TimeInstant event, the program should report error messages.
	 */
	public Duration getDuration(Event intervalEvent) {
		if (intervalEvent != null) {
			if (intervalEvent.getEventType().compareTo(TemporalType.TIMEINTERVAL) == 0) {
				TimeInterval timeInterval = (TimeInterval) intervalEvent.getValidTime();
				if (timeInterval != null) {
					return timeInterval.getDuration();
				}
			} else {
				System.out.println("Error: Only TimeInterval Event has duration.");		
			}
		}
		return null;
	}

	/**
	 * To get the duration between two events
	 */
	public Duration getDurationBetweenEvents(Event event1, Event event2, Granularity gran) {
		if (event1 != null && event2 != null) {
			TimeInstant begin = null;
			TimeInstant end = null;
			
			if (event1.getEventType().compareTo(TemporalType.TIMEINSTANT) == 0) {
				begin = (TimeInstant) event1.getValidTime();
			} else if (event1.getEventType().compareTo(TemporalType.TIMEINTERVAL) == 0) {
				begin = ((TimeInterval) event1.getValidTime()).getStartTime();
			}
			
			if (event2.getEventType().compareTo(TemporalType.TIMEINSTANT) == 0) {
				end = (TimeInstant) event2.getValidTime();
			} else if (event2.getEventType().compareTo(TemporalType.TIMEINTERVAL) == 0) {
				end = ((TimeInterval) event2.getValidTime()).getStartTime();
			}
			
			if (begin != null && end != null && begin.compareTo(end) > 0) {
				TimeInstant temp = begin;
				begin = end;
				end = temp;
			}
			
			return TimeUtils.getDurationFrom(begin, end, gran);
		}
		return null;
	}
	
	/**
	 * Here we return the temporalRelationCode list due to possible relation combinations (e.g. "[before, contain]" as a relation), need to be interpreted further.
	 * 
	 * Note: need to consider the granularity for temporal relations (TODO)
	 */
	public ArrayList<Short> getTemporalRelationType(Event event1, Event event2, Granularity granularity) {
		ArrayList<Short> relations= new ArrayList<Short>();
		if (event1 != null && event2 != null) {
			String targetIRIStr = event2.getIRIStr();
			HashMap<String, ArrayList<TemporalRelationInShortCode>> relationMap = event1.getTemporalRelations();
			ArrayList<TemporalRelationInShortCode> relationList = relationMap.get(targetIRIStr);
			for (TemporalRelationInShortCode relation : relationList) {	
				relations.add(relation.getRelationCode());
			}
		} else {
			relations.add(TEOConstants.bin_full); // use FULL as the unknown relation
		}
		return relations;
	}

	/**
	 * The default configuration is to compare the startTime between a pair of events
	 * 
	 * To generate the timeline, we try to find the topology order of a DAG.
	 */
	public List<Event> getEventsTimeline() {
		Set<String> strSet = eventMap.keySet();
		
		// attempt to detect equivalent sets w/ UnionFindSet
		UnionFindSet<String> eqSet = new UnionFindSet<String>();
		for (String sourceIRI : strSet) {
			eqSet.MakeSet(sourceIRI);
			
			Event sourceEvent = eventMap.get(sourceIRI);
			HashMap<String, ArrayList<TemporalRelationInShortCode>> relationMap = sourceEvent.getTemporalRelations();
			Iterator<Entry<String, ArrayList<TemporalRelationInShortCode>>> itor = relationMap.entrySet().iterator();
			while (itor.hasNext()) {
				Entry<String, ArrayList<TemporalRelationInShortCode>> pair = itor.next();
				String targetIRI = pair.getKey();
				if (sourceIRI.equals(targetIRI)) continue;// jump this situation
				eqSet.MakeSet(targetIRI);
				ArrayList<TemporalRelationInShortCode> relationList = pair.getValue(); // relations should be merged first (intersection)
				short minRelations = TemporalRelationUtils.getMergedTemporalRelationCode(relationList);
				if ((minRelations & TEOConstants.bin_SES) == minRelations) { // if startEqualStart
//					System.out.println("Equal: " + sourceIRI + ", " + targetIRI + " <-" + TemporalRelationUtils.getTemporalRelationTypeListFromConstraintShort(minRelations));
					eqSet.union(eqSet.getElement(sourceIRI), eqSet.getElement(targetIRI));
				}
			}
		}
						
		ArrayList<String> vertexStr = new ArrayList<String>();
		MultiMap <String, String> eqMap = new MultiMap<String, String>(); // used for visulization
		for (String sourceIRI : strSet) {
			if (eqSet.getElement(sourceIRI).equals(eqSet.find(eqSet.getElement(sourceIRI)))) {
//				System.out.println("Remaining: " + sourceIRI);
				vertexStr.add(sourceIRI);
			} else {
				eqMap.put(eqSet.find(eqSet.getElement(sourceIRI)).getValue(), sourceIRI);
			}
		}
		
		ArrayList<Edge> edges = new ArrayList<Edge>();
		HashMap<String, Integer> viMap = new HashMap<String, Integer>();
		
		for (int i = 0; i < vertexStr.size(); i ++) {
			viMap.put(vertexStr.get(i), i);
		}
		
		for (String sourceIRI : strSet) { // traverse ALL nodes
			Event sourceEvent = eventMap.get(sourceIRI);
			HashMap<String, ArrayList<TemporalRelationInShortCode>> relationMap = sourceEvent.getTemporalRelations();
			Iterator<Entry<String, ArrayList<TemporalRelationInShortCode>>> itor = relationMap.entrySet().iterator();
			while (itor.hasNext()) {
				Entry<String, ArrayList<TemporalRelationInShortCode>> pair = itor.next();
				String targetIRI = pair.getKey();
				ArrayList<TemporalRelationInShortCode> relationList = pair.getValue(); // relations should be merged first (intersection)
				short minRelations = TemporalRelationUtils.getMergedTemporalRelationCode(relationList);
				if ((minRelations & TEOConstants.bin_SBS) == minRelations) { // if startBeforeStart, we add an Edge. It is implemented in bit operations
//					System.out.println("Edge: " + sourceIRI + ", " + targetIRI + " <-" + TemporalRelationUtils.getTemporalRelationTypeListFromConstraintShort(minRelations));
//					System.out.println("Replace: " + eqSet.find(eqSet.getElement(sourceIRI)).getValue() + ", " + eqSet.find(eqSet.getElement(targetIRI)).getValue() + " <-" + TemporalRelationUtils.getTemporalRelationTypeListFromConstraintShort(minRelations));
					Edge newEdge = new Edge(viMap.get(eqSet.find(eqSet.getElement(sourceIRI)).getValue()), viMap.get(eqSet.find(eqSet.getElement(targetIRI)).getValue())); // use representatives
					edges.add(newEdge);
				} 
			}
		}
		
		DirectedAcyclicGraph<String> graph = new DirectedAcyclicGraph<String>(edges, vertexStr);
		List<String> vList = TemporalRelationUtils.<String>topologySort(graph);
		List<Event> eventList = new ArrayList<Event>();
		
		for (String vStr : vList) {
			System.out.println(vStr);
			eventList.add(eventMap.get(vStr));
			if (eqMap.containsKey(vStr)) {
				for (String eqStr : eqMap.get(vStr)) {
					System.out.println("\t" + eqStr);
					eventList.add(eventMap.get(eqStr));
				}
			}
		}
		
		return eventList; // the output list loses Equivalent information...
	}
}
