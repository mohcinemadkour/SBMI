package edu.tmc.uth.teo.interfaces;

import java.util.ArrayList;
import java.util.List;

import edu.tmc.uth.teo.model.Duration;
import edu.tmc.uth.teo.model.Event;
import edu.tmc.uth.teo.model.Granularity;

/**
 * APIs that interact with users should be defined in this interface.
 * 
 * @author yluo
 *
 */
public interface TEOQuerier {

	/**
	 * To retrieve and event through a key word searching.
	 * 
	 * See the ISWC paper
	 */
//	public List<Event> findEvents(String searchText);	
	
	/**
	 * See the ISWC paper
	 */
//	public Date getEventFeature(Event event, EventFeature feature) throws CNTROException;

	/**
	 * To retrieve an event by its IRI String
	 */
	public Event getEventByIRIStr(String IRIStr);
	
	/**
	 * To get the duration of a TimeInterval event
	 */
	public Duration getDuration(Event intervalEvent); // for instantEvent we print out error messages

	/**
	 * To get the duration between two events
	 */
	public Duration getDurationBetweenEvents(Event event1, Event event2, Granularity granularity);

	/**
	 * To get the temporal relations between two events
	 */
	public ArrayList<Short> getTemporalRelationType(Event event1, Event event2, Granularity granularity);

	/**
	 * To get the event timeline with all events in order
	 */
	public List<Event> getEventsTimeline();
}
