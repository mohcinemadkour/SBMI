package edu.tmc.uth.teo.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.tmc.uth.teo.impl.TEOOWLAPILoader;
import edu.tmc.uth.teo.impl.TEOOWLAPIParser;
import edu.tmc.uth.teo.impl.TEOOWLAPIQuerier;
import edu.tmc.uth.teo.impl.TEOOWLAPIReasoner;
import edu.tmc.uth.teo.interfaces.TEOLoader;
import edu.tmc.uth.teo.interfaces.TEOParser;
import edu.tmc.uth.teo.interfaces.TEOQuerier;
import edu.tmc.uth.teo.interfaces.TEOReasoner;
import edu.tmc.uth.teo.model.Event;

public class JUnitValidTimeAndTemporalRelation {
	private TEOLoader loader = null;
	private TEOParser parser = null;
	private TEOQuerier querier = null;
	private TEOReasoner reasoner = null;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("\n\n>>>>>>>>>>>>>>>>Start Testing>>>>>>>>>>>>>>>>>>>");
		System.out.println("%Loader: loading begins.");
		loader = new TEOOWLAPILoader("src//test//resources//TEO//TEOAnnotation_8.ttl");
		System.out.println("Status: " + loader.load());
		System.out.println("%Loader: loading completes.");
		
		System.out.println("#Parser: parsing begins.");
		parser = new TEOOWLAPIParser(loader.getOntology());
		System.out.println("Status: " + parser.parse());
		System.out.println("#Parser: parsing completes.");
		
//		System.out.println("Querier: preparing the querier.");
//		querier = new TEOOWLAPIQuerier(parser.getEventMap());
//		System.out.println("Querier: preparing the querier completes.\n");
		
		System.out.println("@Reasoner: reasoning begins.");
		reasoner = new TEOOWLAPIReasoner(parser.getEventMap());
		System.out.println("reasonValidTime Status: " + reasoner.reasonValidTime());
		System.out.println("reasonTemporalRelations Status: " + reasoner.reasonTemporalRelations());
		System.out.println("@Reasoner: reasoning completes.");		
		
		System.out.println("&Querier: preparing the querier.");
		querier = new TEOOWLAPIQuerier(reasoner.getEventMap());
		System.out.println("&Querier: preparing the querier completes.\n");

	}
	
	@Test
	public void testGetEventByIRIStr() {
		System.out.println("######################## Testing GetEventByIRIStr #####################################");
		Event event1 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_8.owl#Event1");
		Event event2 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_8.owl#Event2");
		Event event3 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_8.owl#Event3");
		Event event4 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_8.owl#Event4");
		Event event5 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_8.owl#Event5");
		Event event6 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_8.owl#Event6");
		
	
		System.out.println("Event1:\n" + event1);
		System.out.println("\nEvent2:\n" + event2);
		System.out.println("\nEvent3:\n" + event3);
		System.out.println("\nEvent4:\n" + event4);
		System.out.println("\nEvent5:\n" + event5);
		System.out.println("\nEvent6:\n" + event6);
	}
	
	
	@Test
	public void testGetEventsTimeline() {
		System.out.println("######################## Testing GetEventsTimeline #####################################");
		List<Event> timeline = querier.getEventsTimeline();
		for (Event event : timeline) {
			System.out.println(event.getIRIStr());
		}
//		querier.getEventsTimeline();
	}
}
