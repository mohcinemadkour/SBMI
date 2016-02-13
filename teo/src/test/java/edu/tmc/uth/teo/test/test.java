package edu.tmc.uth.teo.test;

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

public class test {
	private TEOLoader loader = null;
	private TEOParser parser = null;
	private TEOQuerier querier = null;
	private TEOReasoner reasoner = null;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("\n\n>>>>>>>>>>>>>>>>Start Testing>>>>>>>>>>>>>>>>>>>");
		System.out.println("%Loader: loading begins.");
		loader = new TEOOWLAPILoader("/Users/jdu2/Documents/workspace/teo/demo/demo.ttl");
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
		Event event1 = querier.getEventByIRIStr("http://informatics.mayo.edu/demo/semantatorannotation#TEO_00000252");
		Event event2 = querier.getEventByIRIStr("http://informatics.mayo.edu/demo/semantatorannotation#TEO_00000253");
		Event event3 = querier.getEventByIRIStr("http://informatics.mayo.edu/demo/semantatorannotation#TEO_00000254");

		
	
		System.out.println("Event1:\n" + event1);
		System.out.println("\nEvent2:\n" + event2);
		System.out.println("\nEvent3:\n" + event3);
		System.out.println("######################## Testing GetEventsTimeline #####################################");
//		List<Event> timeline = querier.getEventsTimeline();
//		for (Event event : timeline) {
//			System.out.println(event.getIRIStr());
//		}
		querier.getEventsTimeline();

	}
	

}
