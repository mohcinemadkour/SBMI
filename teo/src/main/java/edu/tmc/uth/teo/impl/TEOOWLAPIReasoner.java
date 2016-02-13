package edu.tmc.uth.teo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.allen.temporalintervalrelationships.Constraint;
import org.allen.temporalintervalrelationships.ConstraintNetwork;
import org.allen.temporalintervalrelationships.Node;

import edu.tmc.uth.teo.interfaces.TEOReasoner;
import edu.tmc.uth.teo.model.AssemblyMethod;
import edu.tmc.uth.teo.model.Duration;
import edu.tmc.uth.teo.model.Event;
import edu.tmc.uth.teo.model.TemporalRegion;
import edu.tmc.uth.teo.model.TemporalRelation;
import edu.tmc.uth.teo.model.TemporalRelationInShortCode;
import edu.tmc.uth.teo.model.TemporalRelationType;
import edu.tmc.uth.teo.model.TemporalType;
import edu.tmc.uth.teo.model.TimeInstant;
import edu.tmc.uth.teo.model.TimeInterval;
import edu.tmc.uth.teo.utils.TemporalRelationUtils;
import edu.tmc.uth.teo.utils.TimeUtils;

/**
 * This is the implementation of the TEOReasoner which consist of two major processes:
 * 1. reasonValidTime()
 * 2. reasonTemporalRelations()
 * 
 * @author yluo
 *
 */
public class TEOOWLAPIReasoner implements TEOReasoner {
	private HashMap<String, Event> eventMap = null;
	
	public TEOOWLAPIReasoner(HashMap<String, Event> eventMap) {
		this.eventMap = eventMap;
	}
	
	public HashMap<String, Event> getEventMap() {
		return this.eventMap;
	}
	
	/**
	 * Note: currently, we assume temporal relations only happen between Events.
	 */
	public boolean reasonValidTime() {
		if (eventMap != null) {
			Vector<Event> validTimeEvent = null; // events that contain validTime and can be used for inference
			
			// 1. pin all events which have valid time onto the timeline (put them in a Set "validTimeEvent")
			Iterator<Entry<String, Event>> it = eventMap.entrySet().iterator();
			if (it != null && it.hasNext()) validTimeEvent = new Vector<Event>();
			while (it.hasNext()) {
				Event crtEvent = it.next().getValue();
				if (crtEvent.getValidTime() != null) // validTime can be instant/interval
					validTimeEvent.add(crtEvent);
			}
			
			// 2. iteration: reason validTimes for other events
			while (validTimeEvent != null && !validTimeEvent.isEmpty()) {
				Event startEvent = validTimeEvent.firstElement();
//				System.out.println(startEvent.getIRIStr());
				TimeInstant startTime = null;
				TimeInstant endTime = null;
				if (startEvent.getEventType().equals(TemporalType.TIMEINSTANT)) {
					startTime = (TimeInstant) startEvent.getValidTime();
					endTime = (TimeInstant) startEvent.getValidTime(); // endTime = startTime for a TimeInstant
				} else if (startEvent.getEventType().equals(TemporalType.TIMEINTERVAL)) {
					startTime = ((TimeInterval) startEvent.getValidTime()).getStartTime();
					endTime = ((TimeInterval) startEvent.getValidTime()).getEndTime();
				}
				if (startTime != null || endTime != null) { // to infer other events, it must contain start or end time
					HashMap<String, ArrayList<TemporalRelationInShortCode>> relations = startEvent.getTemporalRelations();
					Set<String> targetStrSet = relations.keySet();
					
					for (String targetIRIStr : targetStrSet) { // for each target event
						Event targetEvent = eventMap.get(targetIRIStr); // Assumption: the target IRI must be an Event
						if (!validTimeIsComplete(targetEvent)) { // if incomplete, then we can investigate new validTime info
							// infer the start/end timeInstant for the target event
							TimeInstant[] instants = inferStartEndTimeForTargetEvent(relations, startTime, endTime, targetIRIStr);
							TimeInstant tStartTime = instants[0];
							TimeInstant tEndTime = instants[1];
		
							// Note: only when the targetEvent is updated with NEW info can we add it into the validTimeEvent queue, otherwise it triggers infinite recursion
							if (updateTargetValidTimeForTargetEvent(startTime, endTime, tStartTime, tEndTime, targetEvent)) {
								validTimeEvent.add(targetEvent);
							}
						}
					}
				}
				validTimeEvent.remove(startEvent);
			}
			
			// 3. add extra Temporal Relations back to the eventMap, according to newly updated validTime information.
			//    Note, here we only update FULL relations (to reduce computing time).
			//it = eventMap.entrySet().iterator();
			Set<String> eventStrSet = eventMap.keySet();
			Event crtEvent, targetEvent;
			HashMap<String, ArrayList<TemporalRelationInShortCode>> relations;
			ArrayList<TemporalRelationInShortCode> relationList;
			
			for (String startIRIStr : eventStrSet) {
				crtEvent = eventMap.get(startIRIStr);
				relations = crtEvent.getTemporalRelations();

				for (String targetIRIStr : eventStrSet) { // for each target event
					targetEvent = eventMap.get(targetIRIStr); // Assumption: the target IRI must be an Event
					relationList = relations.get(targetIRIStr);
					if (relationList == null) { // no relation == FULL relation, we should check whether there are new relations to add
						ArrayList<TemporalRelationType> inferredRelations= TemporalRelationUtils.getTemporalPointRelationsByValidTime(crtEvent, targetEvent);
						if (!inferredRelations.isEmpty()) {
							TemporalRelationInShortCode newRelation = null;
							for (TemporalRelationType inferredRelation : inferredRelations) {
								newRelation = new TemporalRelationInShortCode(TemporalRelationUtils.getTemporalRelationCode(inferredRelation));
								newRelation.setAssemblyMethod(AssemblyMethod.INFERRED);
								crtEvent.addTemporalRelation(targetIRIStr, newRelation);
							}
						}
					} 						
				}
			}
			return true;
		}
		return false;
	}
	
	

	/**
	 * Note: currently, we assume temporal relations only happen between Events.
	 */
	public boolean reasonTemporalRelations() {
		if (eventMap != null) {
			HashMap<String, Node<String>> nodeMap = new HashMap<String, Node<String>>();
			ConstraintNetwork<String> constraintNetwork = new ConstraintNetwork<String>();
			for (String nodeStr : eventMap.keySet()) {
				Node<String> node = new Node<String>(nodeStr);
				constraintNetwork.addNode(node);
				nodeMap.put(nodeStr, node);
			}
			
			Iterator<Event> it = eventMap.values().iterator();
			while (it != null && it.hasNext()) {
				Event event = it.next();
				String sourceStr = event.getIRIStr();
				HashMap<String, ArrayList<TemporalRelationInShortCode>> relationMap = event.getTemporalRelations();
				
				if (relationMap != null) {
					Iterator<Entry<String, ArrayList<TemporalRelationInShortCode>>> itor = relationMap.entrySet().iterator();
					while (itor.hasNext()) { // for each targetIRI
						Entry<String, ArrayList<TemporalRelationInShortCode>> pair = itor.next();
						String targetStr = pair.getKey();
						
						if (sourceStr.equals(targetStr)) continue; // relations between the same node cannot be added as a constraint, it can only be inferred although it is "equal"
						
						ArrayList<TemporalRelationInShortCode> relationList = pair.getValue();
						short typeCode = TemporalRelationUtils.getMergedTemporalRelationCode(relationList); // should merge them and get the minimum labeling set
						Constraint<String> constraint = new Constraint<String>(nodeMap.get(sourceStr), nodeMap.get(targetStr), typeCode);
						constraintNetwork.addConstraint(constraint);			
//						System.out.println("Constraint: (" + nodeMap.get(sourceStr).getAllenId() + ", " + nodeMap.get(targetStr).getAllenId() + ", " 
//											+ "merged: "+ TemporalRelationUtils.getTemporalRelationTypeListFromConstraintShort(typeCode) + ")");									
					}
				}
			}
			
			if (constraintNetwork.pathConsistency()) { // if the network is consistent, then populate the Matrix (eventMap)
				// a quick-and-dirty implementation here for the consistency of querier's API
				ArrayList<Node<String>> matrixNodes = constraintNetwork.getModeledNodes();
				ArrayList<ArrayList<Short>> matrix = constraintNetwork.getConstraintNetwork();
				
//				//---------------------------
//				ArrayList<Node<String>> nodeList = constraintNetwork.getModeledNodes();
//				for (Node<String> node : nodeList) {
//					System.out.print(node.getIdentifier() + "(" + node.getAllenId() + "), ");
//				}
//				System.out.println();
//				
//				ArrayList<ArrayList<Short>> network = constraintNetwork.getConstraintNetwork();
//				for (ArrayList<Short> list : network) {
//					for (Short relation : list) {
//						System.out.print(TemporalRelationUtils.getTemporalRelationTypeListFromConstraintShort(relation) + ", ");
//					}
//					System.out.println();
//				}
//				//---------------------------
				
				for (int i = 0; i < matrixNodes.size(); i ++) {
					String sourceStr = matrixNodes.get(i).getIdentifier();
					Event event = eventMap.get(sourceStr);

					for (int j = 0; j < matrixNodes.size(); j ++) {
						String targetStr = matrixNodes.get(j).getIdentifier();
						// Yi: the short code "matrix.get(i).get(j)" represents a single relation (relation combination), so we should not split them
						TemporalRelationInShortCode relation = new TemporalRelationInShortCode(matrix.get(i).get(j));
						relation.setAssemblyMethod(AssemblyMethod.INFERRED); // inferred relations
						event.addTemporalRelation(targetStr, relation);
					}
				}
				return true;
			} else {
				System.err.println("Error: This network is inconsistent when reasonTemporalRelations().");
			}
		}
		return false;
	}
	
	// helper functions for reasonValidTime().
		private boolean validTimeIsComplete(Event event) {
			TemporalRegion validTime = event.getValidTime();
			if (validTime != null) {
				if (event.getEventType().equals(TemporalType.TIMEINSTANT)) {
					TimeInstant timeInstant = (TimeInstant) validTime;
					if (timeInstant.getNormalizedTime() > -1) return true;
				} else if (event.getEventType().equals(TemporalType.TIMEINTERVAL)) {
					TimeInterval timeInterval = (TimeInterval) validTime;
					if (TimeInterval.isValidTimeInterval(timeInterval.getStartTime(), timeInterval.getEndTime(), timeInterval.getDuration())) {
						return true;
					}
				}
			}
			return false;
		}
		
		// instants[0] - startTime; instants[1] - endTime
		private TimeInstant[] inferStartEndTimeForTargetEvent(HashMap<String, ArrayList<TemporalRelationInShortCode>> relations, TimeInstant startTime, TimeInstant endTime, String targetIRIStr) {
			 TimeInstant[] instants = new TimeInstant[2];
			 ArrayList<TemporalRelationInShortCode> relationList = relations.get(targetIRIStr);
				for (TemporalRelationInShortCode relation : relationList) {
					TemporalRelationType relationType = TemporalRelationUtils.getTemporalRelationTypeListFromConstraintShort(relation.getRelationCode()).get(0); // should be only one Point relation
					if (TemporalRelation.TemporalPointRelationSet.contains(relationType)) {
						switch (relationType) {
							case START_BEFORE_START:
								if (instants[0] == null && startTime != null)
									instants[0] = TimeUtils.getEndTimeInstantFrom(startTime, relation.getTimeOffset(), null);
								break;
							case START_AFTER_START:
								if (instants[0] == null && startTime != null)
									instants[0] = TimeUtils.getStartTimeInstantFrom(relation.getTimeOffset(), startTime, null);
								break;
							case START_BEFORE_END:
								if (instants[1] == null && startTime != null)
									instants[1] = TimeUtils.getEndTimeInstantFrom(startTime, relation.getTimeOffset(), null);
								break;
							case START_AFTER_END:
								if (instants[1] == null && startTime != null)
									instants[1] = TimeUtils.getStartTimeInstantFrom(relation.getTimeOffset(), startTime, null);
								break;
							case END_BEFORE_START:
								if (instants[0] == null && endTime != null)
									instants[0] = TimeUtils.getEndTimeInstantFrom(endTime, relation.getTimeOffset(), null);
								break;
							case END_AFTER_START:
								if (instants[0] == null && endTime != null)
									instants[0] = TimeUtils.getStartTimeInstantFrom(relation.getTimeOffset(), endTime, null);
								break;
							case END_BEFORE_END:
								if (instants[1] == null && endTime != null)
									instants[1] = TimeUtils.getEndTimeInstantFrom(endTime, relation.getTimeOffset(), null);
								break;
							case END_AFTER_END:
								if (instants[1] == null && endTime != null)
									instants[1] = TimeUtils.getStartTimeInstantFrom(relation.getTimeOffset(), endTime, null);
								break;
							case START_EQUAL_START:
								if (instants[0] == null && startTime != null)
									instants[0] = new TimeInstant(startTime.getNormalizedTime(), startTime.getGranularity());
								break;
							case START_EQUAL_END:
								if (instants[1] == null && startTime != null)
									instants[1] = new TimeInstant(startTime.getNormalizedTime(), startTime.getGranularity());
								break;
							case END_EQUAL_START:
								if (instants[0] == null && endTime != null)
									instants[0] = new TimeInstant(endTime.getNormalizedTime(), endTime.getGranularity());
								break;
							case END_EQUAL_END:
								if (instants[1] == null && endTime != null)
									instants[1] = new TimeInstant(endTime.getNormalizedTime(), endTime.getGranularity());
								break;
							default:
								break;
						}
					}
				}
				
			 return instants;
		}
				
		// Note: only when the targetEvent is updated with NEW info can we add it into the validTimeEvent queue, otherwise it triggers infinite recursion
		private boolean updateTargetValidTimeForTargetEvent(TimeInstant sStartTime, TimeInstant sEndTime, TimeInstant tStartTime, TimeInstant tEndTime, Event targetEvent) {
			boolean addToValidTimeEventSet = false;
			TemporalType targetEventType = targetEvent.getEventType();
			TemporalRegion validTime = null;
//			System.out.println("Now Reasoning: " + targetEvent);
			switch (targetEventType) {
				case TIMEINSTANT:  // then the target event must have a null validTime
					if (tStartTime != null && tEndTime != null) {
						if (tStartTime.compareTo(tEndTime) == 0) {
							validTime = tStartTime;
							targetEvent.setValidTime(validTime);
							addToValidTimeEventSet = true;
						} else {
							System.out.println("Error: inferred startTime conflicts with inferred endTime for TimeInstant event: " + targetEvent.getIRIStr());
						}
					} else if (tStartTime != null) {
						validTime = tStartTime;
						targetEvent.setValidTime(validTime);
						addToValidTimeEventSet = true;
					} else if (tEndTime != null) {
						validTime = tEndTime;
						targetEvent.setValidTime(validTime);
						addToValidTimeEventSet = true;
					} else {
						// cannot infer validTime for the target TimeInstant event
					}
					break;
				case TIMEINTERVAL:
					// it may: 1. missing startTime, duration and endTime;
					//         2. missing startTime and duration;
					//         3. missing duration and endTime;
					//         4. missing startTime and endTime.
													
					// 1. missing startTime, duration and endTime;
					if (targetEvent.getValidTime() == null) {
						if (tStartTime != null && tEndTime != null) {
							if (tStartTime.compareTo(tEndTime) <= 0) {
								validTime = new TimeInterval(tStartTime, tEndTime);
								targetEvent.setValidTime(validTime);
								addToValidTimeEventSet = true;
							} else {
								System.out.println("Error: inferred startTime is larger than inferred endTime for TimeInterval event: " + targetEvent.getIRIStr());
							}
						} else if (tStartTime != null) {
							validTime = new TimeInterval();
							((TimeInterval) validTime).setStartTime(tStartTime);
							targetEvent.setValidTime(validTime);
							addToValidTimeEventSet = true; 
							// we still all this (incomplete) timeInterval for reasoning process (they might convey knowledge for inference)
						} else if (tEndTime != null) {
							validTime = new TimeInterval();
							((TimeInterval) validTime).setEndTime(tEndTime);
							targetEvent.setValidTime(validTime);
							addToValidTimeEventSet = true; 
							// we still all this (incomplete) timeInterval for reasoning process (they might convey knowledge for inference)
						} else {
							// cannot infer validTime for the target TimeInstant event
						}
					} else {// then validTime is not null, we cannot create a new one, just add some extra information 
						TimeInstant origStartTime = ((TimeInterval) targetEvent.getValidTime()).getStartTime();
						TimeInstant origEndTime = ((TimeInterval) targetEvent.getValidTime()).getEndTime();
						Duration origDur = ((TimeInterval) targetEvent.getValidTime()).getDuration();
						
						// 2. missing startTime and duration;
					 	if (origStartTime == null && origDur == null && origEndTime != null) {
							if (tEndTime != null) {
								if (tEndTime.compareTo(origEndTime) != 0) {
									System.out.println("Error: inferred endTime conflicts with existed endTime for TimeInterval event: " + targetEvent.getIRIStr());
								} else if (tStartTime != null) {
										if (tStartTime.compareTo(tEndTime) <= 0) {
											validTime = new TimeInterval(tStartTime, tEndTime);
											targetEvent.setValidTime(validTime);
											addToValidTimeEventSet = true; 
										} else {
											System.out.println("Error: inferred startTime is larger than existed endTime for TimeInterval event: " + targetEvent.getIRIStr());
										}
								} 
								// else if (tStartTime == null), no new info added, drop it
							} else if (tStartTime != null) {
								validTime = new TimeInterval(tStartTime, origEndTime);
								targetEvent.setValidTime(validTime);
								addToValidTimeEventSet = true;
							} 
							// else if (tStartTime == null && tEndTime == null), drop it
						}
						// 3. missing duration and endTime;
						else if (origStartTime != null && origDur == null && origEndTime == null) {
							if (tStartTime != null) {
								if (tStartTime.compareTo(origStartTime) != 0) {
									System.out.println("Error: inferred startTime conflicts with existed startTime for TimeInterval event: " + targetEvent.getIRIStr());
								} else if (tEndTime != null) {
										if (tStartTime.compareTo(tEndTime) <= 0) {
											validTime = new TimeInterval(tStartTime, tEndTime);
											targetEvent.setValidTime(validTime);
											addToValidTimeEventSet = true;
										} else {
											System.out.println("Error: existed startTime is larger than inferred endTime for TimeInterval event: " + targetEvent.getIRIStr());
										}
								} 
								// else if (tEndTime == null), no new info added, drop it
							} else if (tEndTime != null) {
								validTime = new TimeInterval(origStartTime, tEndTime);
								targetEvent.setValidTime(validTime);
								addToValidTimeEventSet = true;
							}
							// else if (tStartTime == null && tEndTime == null), drop it
						}
						// 4. missing startTime and endTime.
						else if (origStartTime == null && origDur != null && origEndTime == null) {
							if (tStartTime != null && tEndTime != null) {
								if (TimeInterval.isValidTimeInterval(tStartTime, tEndTime, origDur)) {
									validTime = new TimeInterval(tStartTime, tEndTime);
									targetEvent.setValidTime(validTime);
									addToValidTimeEventSet = true; 
								} else {
									System.out.println("Error: inferred startTime and endTime conflict with original duration for TimeInterval event: " + targetEvent.getIRIStr());
								}
							} else if (tStartTime != null) {
								validTime = new TimeInterval(tStartTime, origDur);
								targetEvent.setValidTime(validTime);
								addToValidTimeEventSet = true;
							} else if (tEndTime != null) {
								validTime = new TimeInterval(origDur, tEndTime);
								targetEvent.setValidTime(validTime);
								addToValidTimeEventSet = true;
							} else {
								// cannot infer validTime for the target TimeInstant event
							}
						}
					}
					break;
				case PERIODICTIMEINTERVAL: // TODO
					break;
					
				default: // UNKNOWN
					if (tStartTime != null && tEndTime != null) { 
						if (tStartTime.compareTo(tEndTime) < 0) { // should be an interval
							validTime = new TimeInterval(tStartTime, tEndTime);
							targetEvent.setValidTime(validTime);
							targetEvent.setEventType(TemporalType.TIMEINTERVAL); // assign the event to be an interval
							addToValidTimeEventSet = true;
						} else if (tStartTime.compareTo(tEndTime) > 0) {
							System.out.println("Error: inferred startTime is larger than inferred endTime for UNKNOWN event: " + targetEvent.getIRIStr());
						} else { // should be an instant
							validTime = tStartTime;
							targetEvent.setValidTime(validTime);
							targetEvent.setEventType(TemporalType.TIMEINSTANT); // assign the event to be an interval
							addToValidTimeEventSet = true;
						}
					} else if (tStartTime != null) { // Note: we treat TimeInterval event as the Default event form!
						validTime = new TimeInterval();
						((TimeInterval) validTime).setStartTime(tStartTime);
						targetEvent.setValidTime(validTime);
						targetEvent.setEventType(TemporalType.TIMEINTERVAL); // assign the event to be an interval (default)
						addToValidTimeEventSet = true;
						// we still all this (incomplete) timeInterval for reasoning process (they might convey knowledge for inference)
					} else if (tEndTime != null) {
						validTime = new TimeInterval();
						((TimeInterval) validTime).setEndTime(tEndTime);
						targetEvent.setValidTime(validTime);
						targetEvent.setEventType(TemporalType.TIMEINTERVAL); // assign the event to be an interval (default)
						addToValidTimeEventSet = true; 
						// we still all this (incomplete) timeInterval for reasoning process (they might convey knowledge for inference)
					} else {
						// cannot infer validTime for the target TimeInstant event
					} 
					break;
			}
			return addToValidTimeEventSet;
		}
}
