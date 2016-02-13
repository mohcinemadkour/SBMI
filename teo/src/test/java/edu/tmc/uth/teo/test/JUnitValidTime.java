package edu.tmc.uth.teo.test;

import org.junit.Before;
import org.junit.Test;

import edu.tmc.uth.teo.impl.TEOOWLAPILoader;
import edu.tmc.uth.teo.impl.TEOOWLAPIParser;
import edu.tmc.uth.teo.impl.TEOOWLAPIQuerier;
import edu.tmc.uth.teo.interfaces.TEOLoader;
import edu.tmc.uth.teo.interfaces.TEOParser;
import edu.tmc.uth.teo.interfaces.TEOQuerier;
import edu.tmc.uth.teo.model.Event;
import edu.tmc.uth.teo.model.Granularity;
import edu.tmc.uth.teo.model.Unit;

public class JUnitValidTime {

	private TEOLoader loader = null;
	private TEOParser parser = null;
	private TEOQuerier querier = null;

	@Before
	public void setUp() throws Exception {
		System.out.println("Loader: loading begins.");
		loader = new TEOOWLAPILoader("src//test//resources//TEO//TEOAnnotation_2.ttl");
		System.out.println("Status: " + loader.load());
		System.out.println("Loader: loading completes.\n");
		
		System.out.println("Parser: parsing begins.");
		parser = new TEOOWLAPIParser(loader.getOntology());
		System.out.println("Status: " + parser.parse());
		System.out.println("Parser: parsing completes.\n");
		
		System.out.println("Querier: preparing the querier.");
		querier = new TEOOWLAPIQuerier(parser.getEventMap());
		System.out.println("Querier: preparing the querier completes.\n");

	}
	
	@Test
	public void testGetEventByIRIStr() {
		System.out.println("######################## Testing GetEventByIRIStr #####################################");
		Event event1 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_2.owl#Event1");
		Event event2 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_2.owl#Event2");
		Event event3 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_2.owl#Event3");
		Event event4 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_2.owl#Event4");
		Event event5 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_2.owl#Event5");
		
		System.out.println("Event1:\n" + event1);
		System.out.println("\nEvent2:\n" + event2);
		System.out.println("\nEvent3:\n" + event3);
		System.out.println("\nEvent4:\n" + event4);
		System.out.println("\nEvent4:\n" + event5);
	}
	
	@Test
	public void testGetEventDuration() {
		System.out.println("######################## Testing GetEventDuration #####################################");
		Event event1 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_2.owl#Event1");
		Event event2 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_2.owl#Event2");
		Event event3 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_2.owl#Event3");
		Event event4 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_2.owl#Event4");
		Event event5 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_2.owl#Event5");
		
		System.out.println("Event1 duration:" + querier.getDuration(event1) + "\n");
		System.out.println("Event2 duration:" + querier.getDuration(event2) + "\n");
		System.out.println("Event3 duration:" + querier.getDuration(event3) + "\n");
		System.out.println("Event4 duration:" + querier.getDuration(event4) + "\n");
		System.out.println("Event5 duration:" + querier.getDuration(event5) + "\n");
	}
	
	@Test
	public void testGetDurationBetweenEvents() {
		System.out.println("######################## Testing GetDurationBetweenEvents #####################################");
		Event event1 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_2.owl#Event1");
		Event event2 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_2.owl#Event2");
		Event event3 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_2.owl#Event3");
		Event event4 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_2.owl#Event4");

		System.out.println("Duration between event1 and event4:" + querier.getDurationBetweenEvents(event1, event4, new Granularity(Unit.MONTH)) + "\n");
		System.out.println("Duration between event4 and event2:" + querier.getDurationBetweenEvents(event4, event2, new Granularity(Unit.YEAR)) + "\n");
		System.out.println("Duration between event2 and event3:" + querier.getDurationBetweenEvents(event2, event3, new Granularity(Unit.SECOND)) + "\n");
		System.out.println("Duration between event1 and event2:" + querier.getDurationBetweenEvents(event1, event2, new Granularity(Unit.DAY)) + "\n");
	}
}
