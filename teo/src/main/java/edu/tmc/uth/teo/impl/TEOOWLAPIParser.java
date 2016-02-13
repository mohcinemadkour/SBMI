package edu.tmc.uth.teo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;

import edu.tmc.uth.teo.interfaces.TEOParser;
import edu.tmc.uth.teo.model.AssemblyMethod;
import edu.tmc.uth.teo.model.Duration;
import edu.tmc.uth.teo.model.Event;
import edu.tmc.uth.teo.model.Granularity;
import edu.tmc.uth.teo.model.TemporalRelation;
import edu.tmc.uth.teo.model.TemporalRelationInShortCode;
import edu.tmc.uth.teo.model.TemporalRelationType;
import edu.tmc.uth.teo.model.TemporalType;
import edu.tmc.uth.teo.model.TimeInstant;
import edu.tmc.uth.teo.model.TimeInterval;
import edu.tmc.uth.teo.model.Unit;
import edu.tmc.uth.teo.utils.StringUtils;
import edu.tmc.uth.teo.utils.TEOConstants;
import edu.tmc.uth.teo.utils.TemporalRelationUtils;
import edu.tmc.uth.teo.utils.TemporalTypeUtils;
import edu.tmc.uth.teo.utils.TimeUtils;

/**
 * This is the implementation of TEOParser with OWL API.
 * 
 * This implementation parses the loaded OWLOntology into an event HashMap which can be regarded as a
 * graph with events as the nodes and temporal relations between events as the directed edges.
 *  
 * Note: in this version of implementation, we did Pellet reasoning and Timeoffset reasoning as the 
 * pre-reasoning step in the parser (as the last step of parsing). 
 * 
 * @author yluo
 *
 */
public class TEOOWLAPIParser implements TEOParser {
	/**
	 * the HashMap representing the ontology as a graph
	 */
	private HashMap<String, Event> eventMap = null;

	private OWLOntology ontology = null;
	private OWLDataFactory df = null;
	private PelletReasoner reasoner = null;
	
	// inner helper data structures
	private HashMap<String, TemporalRelationInShortCode> relationMap = null;
	private HashMap<OWLObjectProperty, TemporalRelationType> relationIntervalRoaster = null;
	private HashMap<OWLObjectProperty, TemporalRelationType> relationPointRoaster = null;
	private HashMap<String, String> timeOffsetMap = null;
	private Vector<String> iriList = null; // used to compress the space for processing IRI strings, see getRelationMapKey()

	// Properties
	private OWLAnnotationProperty rdfLabel = null;
	private OWLAnnotationProperty hasTimeOffset = null;

	private OWLObjectProperty hasValidTime = null;
	private OWLObjectProperty hasStartTime = null;
	private OWLObjectProperty hasEndTime = null;
	private OWLObjectProperty hasDuration = null;
	
	private OWLObjectProperty before = null;
	private OWLObjectProperty after = null;
	private OWLObjectProperty start = null;
	private OWLObjectProperty startedBy = null;
	private OWLObjectProperty finish = null;
	private OWLObjectProperty finishedBy = null;
	private OWLObjectProperty meet = null;
	private OWLObjectProperty metBy = null;
	private OWLObjectProperty overlap = null;
	private OWLObjectProperty overlappedBy = null;
	private OWLObjectProperty contain = null;
	private OWLObjectProperty during = null;
	private OWLObjectProperty equal = null;
	
	private OWLObjectProperty SBS = null; // start before start
	private OWLObjectProperty SAS = null; // start after start
	private OWLObjectProperty SES = null; // start equal start
	private OWLObjectProperty SBE = null; // start before end
	private OWLObjectProperty SAE = null; // start after end
	private OWLObjectProperty SEE = null; // start equal end
	private OWLObjectProperty EBS = null; // end before start
	private OWLObjectProperty EAS = null; // end after start
	private OWLObjectProperty EES = null; // end equal start
	private OWLObjectProperty EBE = null; // end before end
	private OWLObjectProperty EAE = null; // end after end
	private OWLObjectProperty EEE = null; // end equal end
	
	private OWLDataProperty hasDurationPattern = null;
	private OWLDataProperty hasDurationUnit = null;
	private OWLDataProperty hasGranularity = null;
	
	public int getEventCount() {
		return eventMap.size();
	}
	
	public HashMap<String, Event> getEventMap() {
		return this.eventMap;
	}
	
	/**
	 * Constructor
	 * @param ont
	 */
	public TEOOWLAPIParser(Object ont) {
		if (ont == null) {
			System.out.println("!!!!!!!!!! Initialization Error!! ontology is null. Nothing will work !!!!!!!!!!!!!");
			return;
		}
		
		this.eventMap = new HashMap<String, Event>();
		this.relationIntervalRoaster = new HashMap<OWLObjectProperty, TemporalRelationType>();
		this.relationPointRoaster = new HashMap<OWLObjectProperty, TemporalRelationType>();
		this.ontology = (OWLOntology) ont;
		this.df = this.ontology.getOWLOntologyManager().getOWLDataFactory();
		this.reasoner = com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory.getInstance().createReasoner(this.ontology);
		this.relationMap = new HashMap<String, TemporalRelationInShortCode>();
		this.timeOffsetMap = new HashMap<String, String>();
		
		if ((df == null) || (reasoner == null)) {
			System.out.println("!!!!!!!!!! Initialization Error!! df/reasoner is null. Nothing will work !!!!!!!!!!!!!");
			return;
		}
		
		// Properties
		rdfLabel = df.getRDFSLabel();
		hasTimeOffset = df.getOWLAnnotationProperty(IRI.create(TEOConstants.TEO_HASTIMEOFFSET_PRP));
		
		hasValidTime = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_HASVALIDTIME_PRP));
		hasStartTime = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_HASSTARTTIME_PRP));
		hasEndTime = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_HASENDTIME_PRP));
		hasDuration = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_HASDURATION_PRP));
		
		// allen's interval algebra
		before = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_BEFORE_PRP));
		after = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_AFTER_PRP));
		start = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_START_PRP));
		startedBy = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_STARTEDBY_PRP));
		finish = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_FINISH_PRP));
		finishedBy = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_FINISHEDBY_PRP));
		meet = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_MEET_PRP));
		metBy = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_METBY_PRP));
		overlap = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_OVERLAP_PRP));
		overlappedBy = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_OVERLAPPEDBY_PRP));
		contain = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_CONTAIN_PRP));
		during = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_DURING_PRP));
		equal = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_EQUAL_PRP));
		
		// temporal relation for timeOffset
		SBS = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_SBS_PRP));
		SBE = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_SBE_PRP));
		EBS = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_EBS_PRP));
		EBE = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_EBE_PRP));
		SAS = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_SAS_PRP));
		SAE = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_SAE_PRP));
		EAS = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_EAS_PRP));
		EAE = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_EAE_PRP));
		SES = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_SES_PRP));
		SEE = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_SEE_PRP));
		EES = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_EES_PRP));
		EEE = df.getOWLObjectProperty(IRI.create(TEOConstants.TEO_TR_EEE_PRP));
				
		if (before != null) relationIntervalRoaster.put(before, TemporalRelationType.BEFORE);
		if (after != null) relationIntervalRoaster.put(after, TemporalRelationType.AFTER);
		if (start != null) relationIntervalRoaster.put(start, TemporalRelationType.START);
		if (startedBy != null) relationIntervalRoaster.put(startedBy, TemporalRelationType.STARTEDBY);
		if (finish != null) relationIntervalRoaster.put(finish, TemporalRelationType.FINISH);
		if (finishedBy != null) relationIntervalRoaster.put(finishedBy, TemporalRelationType.FINISHEDBY);
		if (meet != null) relationIntervalRoaster.put(meet, TemporalRelationType.MEET);
		if (metBy != null) relationIntervalRoaster.put(metBy, TemporalRelationType.METBY);
		if (overlap != null) relationIntervalRoaster.put(overlap, TemporalRelationType.OVERLAP);
		if (overlappedBy != null) relationIntervalRoaster.put(overlappedBy, TemporalRelationType.OVERLAPPEDBY);
		if (contain != null) relationIntervalRoaster.put(contain, TemporalRelationType.CONTAIN);
		if (during != null) relationIntervalRoaster.put(during, TemporalRelationType.DURING);
		if (equal != null) relationIntervalRoaster.put(equal, TemporalRelationType.EQUAL);
		
		if (SBS != null) relationPointRoaster.put(SBS, TemporalRelationType.START_BEFORE_START);
		if (SBE != null) relationPointRoaster.put(SBE, TemporalRelationType.START_BEFORE_END);
		if (EBS != null) relationPointRoaster.put(EBS, TemporalRelationType.END_BEFORE_START);
		if (EBE != null) relationPointRoaster.put(EBE, TemporalRelationType.END_BEFORE_END);
		if (SAS != null) relationPointRoaster.put(SAS, TemporalRelationType.START_AFTER_START);
		if (SAE != null) relationPointRoaster.put(SAE, TemporalRelationType.START_AFTER_END);
		if (EAS != null) relationPointRoaster.put(EAS, TemporalRelationType.END_AFTER_START);
		if (EAE != null) relationPointRoaster.put(EAE, TemporalRelationType.END_AFTER_END);
		if (SES != null) relationPointRoaster.put(SES, TemporalRelationType.START_EQUAL_START);
		if (SEE != null) relationPointRoaster.put(SEE, TemporalRelationType.START_EQUAL_END);
		if (EES != null) relationPointRoaster.put(EES, TemporalRelationType.END_EQUAL_START);
		if (EEE != null) relationPointRoaster.put(EEE, TemporalRelationType.END_EQUAL_END);
		
		hasDurationPattern = df.getOWLDataProperty(IRI.create(TEOConstants.TEO_HASDURATIONPATTERN_PRP));
		hasDurationUnit = df.getOWLDataProperty(IRI.create(TEOConstants.TEO_HASDURATIONUNIT_PRP));
		hasGranularity = df.getOWLDataProperty(IRI.create(TEOConstants.TEO_HASGRANULARITY_PRP));
	}
	
	/**
	 * The main parser to parse the given file
	 */
	public boolean parse() {
		boolean noError = true;
		OWLClass c = null;
		Set<OWLNamedIndividual> individuals = null;
		
		/*
		 * To find out all individuals of "Event" class
		 */
		c = df.getOWLClass(IRI.create(TEOConstants.TEO_EVENT_CLS));
		individuals = reasoner.getInstances(c, false).getFlattened(); // from the reasoner
		
		for (OWLNamedIndividual eventIndividual : individuals) {
			if (eventIndividual != null) {
//				System.out.println("[####################################]Processing Events....--> " + eventIndividual.getIRI().toString());
				Event event = parseEvent(eventIndividual); // call the parseEvent() method - 1.parseValidTime; 2.parseTemporalRelations
				eventMap.put(eventIndividual.getIRI().toString(), event);
			}
		}
		
		// double check the consistency for each event, between EventTemporalType (instant/interval) and its relations
		// can only print out error messages if detected inconsistent situations...
		for (Event eachEvent : eventMap.values()) {
			HashMap<String, ArrayList<TemporalRelationInShortCode>> relationMap = eachEvent.getTemporalRelations();
			if (relationMap != null) {
				Set<String> targetStrSet = relationMap.keySet(); // might be revised in the following steps
				for (String targetStr : targetStrSet) {
					Event targetEvent = eventMap.get(targetStr);
					ArrayList<TemporalRelationInShortCode> relations = relationMap.get(targetStr); // might be revised in the following steps
					for (TemporalRelationInShortCode relation : relations) {
						// check basic interval relations here; for point relations like "start?start, start?end...", they will be checked in the reasoner...
						if (!TemporalTypeUtils.checkTemporalTypeConsistency(eachEvent, relation, targetEvent)) {
							// eachEvent.remove(String targetIRI, TemporalRelationInShortCode relation); 
							// converse(relation) will be removed by targetEvent in the following certain loops...
							System.out.println("Relations should be removed.");
						} else {
							// decides the event type (if unknown) with second priority
							TemporalTypeUtils.assignTemporalType(eachEvent, relation, targetEvent);
						}
					}
				}
			}
		}
		
		// assign the timeOffset to related relations
		if (timeOffsetMap != null && !timeOffsetMap.isEmpty()) {
			Set<String> keySet = timeOffsetMap.keySet();
			for (String keyStr : keySet) {
				String[] parts = timeOffsetMap.get(keyStr).split("-");
				OWLNamedIndividual duration = df.getOWLNamedIndividual(IRI.create(iriList.get(Integer.parseInt(parts[1]))));
				Duration dur = this.parseDuration(duration);
				if (parts[0].equals("A")) {
					dur.setAssemblyMethod(AssemblyMethod.ASSERTED);
				} else {
					dur.setAssemblyMethod(AssemblyMethod.INFERRED);
				}		
				relationMap.get(keyStr).setTimeOffset(dur);
			}
//			System.out.println(iriList);
		}
		
		return noError;
	}
	
	/**
	 * Event parser
	 * @param eventIndividual
	 * @return
	 */
	public Event parseEvent(OWLNamedIndividual eventIndividual) {
		Event event = new Event();
		String sourceIRIStr = eventIndividual.getIRI().toString();
		event.setIRIStr(sourceIRIStr);

		Set<OWLNamedIndividual> valueList = null;		
		
		// 1. parse the valid time
		valueList = getObjectPropertyValue(eventIndividual, hasValidTime); // hasValidTime (decides the event type with first priority)
		for (OWLNamedIndividual validTime : valueList) {
			TemporalType parsedType = getTemporalType(validTime);
			if (parsedType.equals(TemporalType.TIMEINSTANT)) {
				event.setEventType(TemporalType.TIMEINSTANT);
				TimeInstant parsedInstant = parseTimeInstant(validTime);
				event.setValidTime(parsedInstant);
			}
			else if (parsedType.equals(TemporalType.TIMEINTERVAL)) {
				event.setEventType(TemporalType.TIMEINTERVAL);
				TimeInterval parsedInterval = parseTimeInterval(validTime);
				event.setValidTime(parsedInterval);
			}
		}
		
		// 2. parse the temporal relations
		
		// auxiliary variables prepared for parsing Temporal Relations
		TemporalRelationType relationType = null;
		String targetIRIStr = null;
		TemporalRelationInShortCode relation = null;
		String relationMapKey = null;
		
		// 1). collect all "asserted" relations (for later relation merge) first and record timeOffsets for point relations
		Set<OWLObjectPropertyAssertionAxiom> axiomSet = ontology.getObjectPropertyAssertionAxioms(eventIndividual);
		for (OWLObjectPropertyAssertionAxiom axiom : axiomSet) {
			if (relationIntervalRoaster.containsKey(axiom.getProperty())) {
				relationType = relationIntervalRoaster.get(axiom.getProperty());
			} else {
				relationType = relationPointRoaster.get(axiom.getProperty()); 
			}
			if (relationType != null && axiom.getObject().isNamed()) {
				targetIRIStr = axiom.getObject().asOWLNamedIndividual().getIRI().toString();
				
				// may contain timeOffset info
				// Note: both timeInstant and timeInterval can have start/end time points, will be determined in the reasoning process
				if (TemporalRelation.TemporalPointRelationSet.contains(relationType)) {
					if (getConversePointRelation(relationType) != null) { // means it is possible to have timeOffset
						String key1 = getRelationMapKey(sourceIRIStr, relationType, targetIRIStr); // Asserted
						String key2 = getRelationMapKey(targetIRIStr, getConversePointRelation(relationType), sourceIRIStr); // Inferred
						if (!this.timeOffsetMap.containsKey(key1) && !this.timeOffsetMap.containsKey(key2)) {
							Set<OWLAnnotation> annotSet = axiom.getAnnotations(hasTimeOffset);
							if (annotSet != null) {
								for (OWLAnnotation annot : annotSet) {	
									//annot.getValue().toString() - The IRI String of the duration individual 
									timeOffsetMap.put(key1, "A-" + this.getIRIIndex(annot.getValue().toString()));
									timeOffsetMap.put(key2, "I-" + this.getIRIIndex(annot.getValue().toString()));
								}
							}
						} else {
							System.out.println("Error: Duplicate point relation (with duplicate timeOffset) information detected!");
						}
					}
				}

				relation = new TemporalRelationInShortCode(TemporalRelationUtils.getTemporalRelationCode(relationType));
				relation.setAssemblyMethod(AssemblyMethod.ASSERTED); // Asserted axioms
				event.addTemporalRelation(targetIRIStr, relation); 
				// duplicate relations cannot be added, it detects duplicate by "target" AND "type" only, (no asserted/granularity info involved)
				relationMapKey = getRelationMapKey(sourceIRIStr, relationType, targetIRIStr);
				relationMap.put(relationMapKey, relation);
			}
		}
				
/**
 * Yi: consider to give rid of Pellet's reasoning here????
 * 	 	then we have to totally rely on the Allen's Interval Reasoning algorithm.
 * 
 * Experiments on Annotation_6.owl showed that there are different results between integrating and Not integrating Pellet's results, we can merge 
 * their results later??
 */
		// 2). collect all "inferred" relation (by Pellet only)
		Iterator<Entry<OWLObjectProperty, TemporalRelationType>> it = null;
		OWLObjectProperty relationPro = null;
		// 2-1). parse inferred Interval temporal relations
		it = relationIntervalRoaster.entrySet().iterator();	
		while (it.hasNext()) {
			Entry<OWLObjectProperty, TemporalRelationType> pair = it.next();
			relationType = pair.getValue();
			relationPro = pair.getKey();
			if (relationPro != null) {
				valueList = getObjectPropertyValue(eventIndividual, relationPro);
				for (OWLNamedIndividual target : valueList) {
					targetIRIStr = target.getIRI().toString();
					relation = new TemporalRelationInShortCode(TemporalRelationUtils.getTemporalRelationCode(relationType));
					relation.setAssemblyMethod(AssemblyMethod.INFERRED); // Inferred axioms
					event.addTemporalRelation(targetIRIStr, relation);
					relationMapKey = getRelationMapKey(sourceIRIStr, relationType, targetIRIStr);
					if (!relationMap.containsKey(relationMapKey)) {
						relationMap.put(relationMapKey, relation);
					}
				}
			}
		}
		// 2-2). parse inferred Point temporal relations
		it = relationPointRoaster.entrySet().iterator();	
		while (it.hasNext()) {
			Entry<OWLObjectProperty, TemporalRelationType> pair = it.next();
			relationType = pair.getValue();
			relationPro = pair.getKey();
			if (relationPro != null) {
				valueList = getObjectPropertyValue(eventIndividual, relationPro);
				for (OWLNamedIndividual target : valueList) {
					targetIRIStr = target.getIRI().toString();
					relation = new TemporalRelationInShortCode(TemporalRelationUtils.getTemporalRelationCode(relationType));
					relation.setAssemblyMethod(AssemblyMethod.INFERRED); // Inferred axioms
					event.addTemporalRelation(targetIRIStr, relation);
					relationMapKey = getRelationMapKey(sourceIRIStr, relationType, targetIRIStr);
					if (!relationMap.containsKey(relationMapKey)) {
						relationMap.put(relationMapKey, relation);
					}
				}
			}
		}
				
		return event;
	}

	/**
	 * Check the type of a given time individual
	 * @param timeIndividual
	 * @return
	 */
	public TemporalType getTemporalType(OWLNamedIndividual timeIndividual) {
		Set<OWLClass> typeList = reasoner.getTypes(timeIndividual, true).getFlattened(); // direct rdf:type
		
		for (OWLClass type : typeList) {
			if (type.getIRI().toString().equals(TEOConstants.TEMPORAL_INSTANT_CLS)) {
				return TemporalType.TIMEINSTANT;
			} else if (type.getIRI().toString().equals(TEOConstants.TEMPORAL_INTERVAL_CLS)) {
				return TemporalType.TIMEINTERVAL;
			} else if (type.getIRI().toString().equals(TEOConstants.PERIODIC_TIME_INTERVAL_CLS)) {
				return TemporalType.PERIODICTIMEINTERVAL;
			}
		}
		
		return null;
	}
	
	/**
	 * TimeInstant parser
	 * @param timeIndividual
	 * @return
	 */
	public TimeInstant parseTimeInstant(OWLNamedIndividual timeIndividual) {
		TimeInstant timeInstant = null;
		
		String labelAsTime = getAnnotationPropertyValue(timeIndividual, rdfLabel);
		if (labelAsTime != null) {
			Granularity gran = new Granularity(Unit.SECOND); // Note: granularity should be parsed from the property "hasGranularity"
			
			Set<OWLLiteral> valueList = getDataPropertyValue(timeIndividual, hasGranularity);
			if (valueList != null) {
				for (OWLLiteral granValue : valueList) {
					if (granValue != null) {
						gran = new Granularity(TimeUtils.getUnitFromString(granValue.getLiteral()));
					}
				}
			}
			
			timeInstant = new TimeInstant(labelAsTime, gran);
		}
		
		return timeInstant;
	}
	
	/**
	 * TimeInterval parser
	 * @param timeIndividual
	 * @return
	 */
	public TimeInterval parseTimeInterval(OWLNamedIndividual timeIndividual) {
		TimeInterval timeInterval = null;
		
		TimeInstant startTimeInstant = null;
		TimeInstant endTimeInstant = null;
		Duration duration = null;
		
		// parse the start time
		Set<OWLNamedIndividual> valueList = getObjectPropertyValue(timeIndividual, hasStartTime); // hasStartTime
		for (OWLNamedIndividual startTime : valueList) {
			if (startTime != null) {
				startTimeInstant = parseTimeInstant(startTime);
			}
		}
		// parse the end time
		valueList = getObjectPropertyValue(timeIndividual, hasEndTime); // hasStartTime
		for (OWLNamedIndividual endTime : valueList) {
			if (endTime != null) {
				endTimeInstant = parseTimeInstant(endTime);
			}
		}
		// parse the duration
		valueList = getObjectPropertyValue(timeIndividual, hasDuration); // hasDuration
		for (OWLNamedIndividual dur : valueList) {
			if (dur != null) {
				duration = parseDuration(dur);
			}
		}
		if (startTimeInstant != null && endTimeInstant != null && duration != null) { // we have to check the validation first
			if (TimeInterval.isValidTimeInterval(startTimeInstant, endTimeInstant, duration)) {
				// Assumption: if start, end and duration are valid and given, we use start and end to populate the time interval.
				timeInterval = new TimeInterval(startTimeInstant, endTimeInstant); 
			} else {
				System.out.println("Error: Inconsistent startTime, endTime and duration for TimeInterval: " + timeIndividual.getIRI());
			}
		} else if (startTimeInstant != null && endTimeInstant != null) {
			timeInterval = new TimeInterval(startTimeInstant, endTimeInstant);
		} else if (startTimeInstant != null && duration != null) {
			timeInterval = new TimeInterval(startTimeInstant, duration);
		} else if (endTimeInstant != null && duration != null) {
			timeInterval = new TimeInterval(duration, endTimeInstant);
		} else {
			System.out.println("Warning: Given information is not sufficient to parse the TimeInterval: " + timeIndividual.getIRI());			
			timeInterval = new TimeInterval();
			if (startTimeInstant != null) timeInterval.setStartTime(startTimeInstant);
			if (endTimeInstant != null) timeInterval.setEndTime(endTimeInstant);
			if (duration != null) timeInterval.setDuration(duration);
			// we still return the (incomplete) timeInterval for reasoning process (they might convey knowledge for inference)
		}
		
		return timeInterval;
	}
	
	/**
	 * Duration parser from durationPattern "0Y0M0W0D0H0m0s"
	 */
	public Duration parseDuration(OWLNamedIndividual durIndividual) {
		Duration duration = null;
		
		Set<OWLLiteral> valueList = getDataPropertyValue(durIndividual, hasDurationPattern);
		for (OWLLiteral durValue : valueList) {
			if (durValue != null) {
				String durValueStr = durValue.getLiteral();
				Unit unit = Unit.SECOND; // Note: granularity should be parsed from the property "hasDurationUnit"
				
				Set<OWLLiteral> unitList = getDataPropertyValue(durIndividual, hasDurationUnit);
				if (unitList != null) {
					for (OWLLiteral unitValue : unitList) {
						if (unitValue != null) {
							unit = TimeUtils.getUnitFromString(unitValue.getLiteral());
						}
					}
				}
				
				duration = new Duration(durValueStr, unit);
			}
		}
		return duration;
	}
	
	/**
	 * To get AnnotationPropertyValue
	 * @param pI
	 * @param annProperty
	 * @return
	 */
	public String getAnnotationPropertyValue(OWLNamedIndividual pI,
			OWLAnnotationProperty annProperty) {
		if ((pI == null) || (ontology == null) || (annProperty == null))
			return null;

		Set<OWLAnnotation> annotations = pI.getAnnotations(ontology, annProperty);

		// return the first label annotation
		for (OWLAnnotation ann : annotations) {
			if ((ann.getValue() != null) && (!StringUtils.isNull(ann.getValue().toString()))) {
				String value = ann.getValue().toString();
				return StringUtils.getStringValueWithinQuotes(value);
			}
		}
		return null;
	}
	
	/**
	 * To get ObjectPropertyValue
	 * @param pI
	 * @param objProperty
	 * @return
	 */
	public Set<OWLNamedIndividual> getObjectPropertyValue(OWLNamedIndividual pI, OWLObjectProperty objProperty)
	{
		if ((pI == null)||(objProperty == null))
			return null;

		Set<OWLNamedIndividual> propList = reasoner.getObjectPropertyValues(pI, objProperty).getFlattened();
		
		return propList;
	}
	
	/**
	 * To get DataPropertyValue
	 * @param pI
	 * @param dataProperty
	 * @return
	 */
	public Set<OWLLiteral> getDataPropertyValue(OWLNamedIndividual pI,
			OWLDataProperty dataProperty) {
		if ((pI == null) || (dataProperty == null))
			return null;

		Map<OWLDataPropertyExpression, Set<OWLLiteral>> dataProperties = pI
				.getDataPropertyValues(ontology);

		if (dataProperties.containsKey(dataProperty)) {
			return dataProperties.get(dataProperty);
		}

		return null;
	}
	
	public TemporalRelationType getConversePointRelation(TemporalRelationType pointRelation) {
		switch (pointRelation) {
			case START_BEFORE_START: return TemporalRelationType.START_AFTER_START;
			case START_AFTER_START: return TemporalRelationType.START_BEFORE_START;
			//case START_EQUAL_START: return TemporalRelationType.START_EQUAL_START;
			
			case START_BEFORE_END: return TemporalRelationType.END_AFTER_START;
			case START_AFTER_END: return TemporalRelationType.END_BEFORE_START;
			//case START_EQUAL_END: return TemporalRelationType.END_EQUAL_START;
			
			case END_AFTER_START: return TemporalRelationType.START_BEFORE_END;
			case END_BEFORE_START: return TemporalRelationType.START_AFTER_END;
			//case END_EQUAL_START: return TemporalRelationType.START_EQUAL_END;
			
			case END_BEFORE_END: return TemporalRelationType.END_AFTER_END;
			case END_AFTER_END: return TemporalRelationType.END_BEFORE_END;
			//case END_EQUAL_END: return TemporalRelationType.END_EQUAL_END;
			
			default: return null;
		}
	}
	
	/**
	 * Compress the String into things like "1- 2".
	 */
	public String getRelationMapKey(String sourceStr, TemporalRelationType relation, String targetStr) {
		return this.getIRIIndex(sourceStr) + "-" + relation + "-" + this.getIRIIndex(targetStr);
	}
	
	private void addIRIStr(String iriStr) {
		if (this.iriList == null) {
			iriList = new Vector<String>();
		}
		if (!iriList.contains(iriStr)) {
			iriList.add(iriStr);
		}
	}
	
	private int getIRIIndex(String iriStr) {
		if (iriList == null) {
			addIRIStr(iriStr);
		}
		int index = iriList.indexOf(iriStr);
		if (index < 0) {
			addIRIStr(iriStr);
			return iriList.size() - 1;
		} else {
			return index;
		}
	}
}
