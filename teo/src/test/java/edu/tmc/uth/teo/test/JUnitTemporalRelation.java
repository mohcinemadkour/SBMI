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

public class JUnitTemporalRelation {
	private TEOLoader loader = null;
	private TEOParser parser = null;
	private TEOQuerier querier = null;
	private TEOReasoner reasoner = null;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("\n\n>>>>>>>>>>>>>>>>Start Testing>>>>>>>>>>>>>>>>>>>");
		System.out.println("Loader: loading begins.");
		loader = new TEOOWLAPILoader("src//test//resources//TEO//TEOAnnotation_6.ttl");
		System.out.println("Status: " + loader.load());
		System.out.println("Loader: loading completes.");
		
		System.out.println("Parser: parsing begins.");
		parser = new TEOOWLAPIParser(loader.getOntology());
		System.out.println("Status: " + parser.parse());
		System.out.println("Parser: parsing completes.");
		
//		System.out.println("Querier: preparing the querier.");
//		querier = new TEOOWLAPIQuerier(parser.getEventMap());
//		System.out.println("Querier: preparing the querier completes.\n");
		
		System.out.println("Reasoner: reasoning begins.");
		reasoner = new TEOOWLAPIReasoner(parser.getEventMap());
		System.out.println("Status: " + reasoner.reasonTemporalRelations());
		System.out.println("Reasoner: reasoning completes.");		
		
		System.out.println("Querier: preparing the querier.");
		querier = new TEOOWLAPIQuerier(reasoner.getEventMap());
		System.out.println("Querier: preparing the querier completes.\n");

	}
	
//	@TestOWAAPI
//	public void testGetEventByIRIStr() {
//		System.out.println("######################## Testing GetEventByIRIStr #####################################");
//		Event event0 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_4.owl#Event0");
//		Event event1 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_4.owl#Event1");
//		Event event2 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_4.owl#Event2");
//		Event event3 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_4.owl#Event3");
//		Event event4 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_4.owl#Event4");
//		Event event5 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_4.owl#Event5");
//		Event event6 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_4.owl#Event6");
//		
//		System.out.println("Event0:\n" + event0);
//		System.out.println("\nEvent1:\n" + event1);
//		System.out.println("\nEvent2:\n" + event2);
//		System.out.println("\nEvent3:\n" + event3);
//		System.out.println("\nEvent4:\n" + event4);
//		System.out.println("\nEvent5:\n" + event5);
//		System.out.println("\nEvent6:\n" + event6);
//	}
	
//	@TestOWAAPI
//	public void testGetTemporalRelationsBetweenEvents() {
//		System.out.println("######################## Testing GetRelations #####################################");
//		Event event0 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_4.owl#Event0");
//		Event event1 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_4.owl#Event1");
//		Event event2 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_4.owl#Event2");
//		Event event3 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_4.owl#Event3");
//		Event event4 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_4.owl#Event4");
//		Event event5 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_4.owl#Event5");
//		Event event6 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_4.owl#Event6");
//		
//		System.out.println("event0 - event5:\n" + querier.getTemporalRelationType(event0, event5, null));
//	}
	
	//@TestOWAAPI
	public void testGetEventByIRIStr() {
		System.out.println("######################## Testing GetEventByIRIStr #####################################");
		Event event0 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_6.owl#ER_Admission1");
		Event event1 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_6.owl#StartAntiplateletTherapy2");
		Event event2 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_6.owl#StartAntiplateletTherapy1");
		Event event3 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_6.owl#Late_Stent_Thrombosis1");
		Event event4 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_6.owl#InitialDrugElustingStentImplantation1");
		Event event5 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_6.owl#EndAntiplateletTherapy1");
		Event event6 = querier.getEventByIRIStr("http://www.cse.lehigh.edu/~yil712/TEO/annotation_6.owl#InitialDrugElutingStentImplantation2");
		
		System.out.println("Event0:\n" + event0);
		System.out.println("\nEvent1:\n" + event1);
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
//		for (Event event : timeline) {
//			System.out.println(event.getIRIStr());
//		}
	}
}
