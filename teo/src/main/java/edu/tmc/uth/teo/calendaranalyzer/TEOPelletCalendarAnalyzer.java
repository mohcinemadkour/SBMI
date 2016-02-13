package edu.tmc.uth.teo.calendaranalyzer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import edu.tmc.uth.teo.impl.TEOOWLAPILoader;
import edu.tmc.uth.teo.utils.DateUtils;

/**
 * This class helps to classify a date into a special date it belongs to.
 * 
 * For example, if given a date "07-04-2014", this analyzer will annotate the 
 * date as "Independence Day" according to the definition in TEO.
 * 
 * This class can be merged with TEOJENAClalendarAnalyzer.java.
 * 
 * @author yluo
 *
 */
public class TEOPelletCalendarAnalyzer {
	private OWLOntology ontology = null;
	private OWLOntologyManager manager = null;
	private OWLDataFactory df = null;
	private PelletReasoner reasoner = null;
	private int quriedDateNum = 0;
	
	public boolean loadOntology(String fileName) {
		TEOOWLAPILoader loader = new TEOOWLAPILoader(fileName);
		loader.load();
		ontology = (OWLOntology) loader.getOntology();
		manager = ontology.getOWLOntologyManager();
		df = manager.getOWLDataFactory();
	
		if (ontology != null && manager != null && df != null) 
			return true;
		else 
			return false;
	}
		
	public Set<OWLClass> getSpecialDateClasses(Date date) {
		Set<OWLClass> dateClass = null;
		
		if (date != null) {
			SpecialDate specialDate = new SpecialDate();
			specialDate.parseDate(date);
			
			System.out.println(specialDate);
			
			OWLNamedIndividual tempDate = df.getOWLNamedIndividual(IRI.create(CalendarConstants.getWithNS("TempDate" + "_" + quriedDateNum)));
			
			ArrayList<OWLAxiom> axioms = getInstanceAxioms(tempDate, specialDate);
			if (axioms != null) {
				for (OWLAxiom axiom : axioms) {
					manager.addAxiom(ontology, axiom);
				}
			}
			
			// after adding the date into the ontology, we launch the reasoner
			reasoner = PelletReasonerFactory.getInstance().createReasoner(this.ontology);
			dateClass = reasoner.getTypes(tempDate, true).getFlattened(); // direct rdf:type
		}
		return dateClass;
	}

	private ArrayList<OWLAxiom> getInstanceAxioms(OWLNamedIndividual tempDate, SpecialDate spcDate) {
		ArrayList<OWLAxiom> axioms = new ArrayList<OWLAxiom>();
		if (spcDate != null) {
			OWLAxiom tempAxiom = null;
			OWLNamedIndividual tempWeekDay = null;
			OWLNamedIndividual tempMonthDay = null;
			OWLNamedIndividual tempMonth = null;
			OWLNamedIndividual tempMonthWeek = null;
			OWLObjectProperty occurMonth = df.getOWLObjectProperty(IRI.create(CalendarConstants.OCCURMONTH_PRP));
			OWLObjectProperty occurDay = df.getOWLObjectProperty(IRI.create(CalendarConstants.OCCURDAY_PRP));
			OWLObjectProperty occurWeek = df.getOWLObjectProperty(IRI.create(CalendarConstants.OCCURWEEK_PRP));
			OWLDataProperty occurYear = df.getOWLDataProperty(IRI.create(CalendarConstants.OCCURYEAR_PRP));
			
			// occurDay - weekDay
			if (spcDate.getWeekDay() != 0) {
				tempWeekDay = df.getOWLNamedIndividual(IRI.create(CalendarConstants.getWithNS("TempWeekDay" + "_" + quriedDateNum))); // mon, tue, wed...
				tempAxiom = df.getOWLClassAssertionAxiom(getWeekDayClass(spcDate.getWeekDay()), tempWeekDay);
				axioms.add(tempAxiom);
			}
			if (tempWeekDay != null) {
				tempAxiom = df.getOWLObjectPropertyAssertionAxiom(occurDay, tempDate, tempWeekDay);
				axioms.add(tempAxiom);
			}
			// occurDay - monthDay
			if (spcDate.getMonthDay() != 0) {
				tempMonthDay = df.getOWLNamedIndividual(IRI.create(CalendarConstants.getWithNS("TempMonthDay" + "_" + quriedDateNum)));
				tempAxiom = df.getOWLClassAssertionAxiom(getMonthDayClass(spcDate.getMonthDay()), tempMonthDay);
				axioms.add(tempAxiom);
			}
			if (tempMonthDay != null) {
				tempAxiom = df.getOWLObjectPropertyAssertionAxiom(occurDay, tempDate, tempMonthDay);
				axioms.add(tempAxiom);
			}
			// occurMonth - month
			if (spcDate.getMonth() != 0) {
				tempMonth = df.getOWLNamedIndividual(IRI.create(CalendarConstants.getWithNS("TempMonth" + "_" + quriedDateNum)));
				tempAxiom = df.getOWLClassAssertionAxiom(getMonthClass(spcDate.getMonth()), tempMonth);
				axioms.add(tempAxiom);
			}
			if (tempMonth != null) {
				tempAxiom = df.getOWLObjectPropertyAssertionAxiom(occurMonth, tempDate, tempMonth);
				axioms.add(tempAxiom);
			}
			// occurWeek - monthWeek
			if (spcDate.getMonthWeek() != 0) {
				tempMonthWeek = df.getOWLNamedIndividual(IRI.create(CalendarConstants.getWithNS("TempMonthWeek" + "_" + quriedDateNum)));
				tempAxiom = df.getOWLClassAssertionAxiom(getMonthWeekClass(spcDate.getMonthWeek()), tempMonthWeek);
				axioms.add(tempAxiom);
			}
			if (tempMonthWeek != null) {
				tempAxiom = df.getOWLObjectPropertyAssertionAxiom(occurWeek, tempDate, tempMonthWeek);
				axioms.add(tempAxiom);
			}
			// occurYear - year
			if (spcDate.getYear() != 0) {
				tempAxiom = df.getOWLDataPropertyAssertionAxiom(occurYear, tempDate, spcDate.getYear());
				axioms.add(tempAxiom);
			}			
			quriedDateNum ++;
		}
		return axioms;
	}
	
	public static void main(String args[]) {
		String fileName = "src/test/resources/TEO/TEO_1.1.0.owl";
		TEOPelletCalendarAnalyzer analyzer = new TEOPelletCalendarAnalyzer();
		
		Date testDate1 = DateUtils.parse("07-04-2014");
		Date testDate2 = DateUtils.parse("12-25-2014");
		Date testDate3 = DateUtils.parse("11-22-2012");
		
		if (analyzer.loadOntology(fileName)) {
			Set<OWLClass> dateClasses = null;
			
			dateClasses = analyzer.getSpecialDateClasses(testDate1);
			for (OWLClass dateClass : dateClasses) {
				// Get the annotations on the class that use the label property (rdfs:label)
				for (OWLAnnotation annotation : dateClass.getAnnotations(analyzer.ontology, analyzer.df.getRDFSLabel())) {
				  if (annotation.getValue() instanceof OWLLiteral) {
				    OWLLiteral val = (OWLLiteral) annotation.getValue();
				    	System.out.println(dateClass + " : \"" + val.getLiteral() + "\"");
				   }
				}
			}
			
			dateClasses = analyzer.getSpecialDateClasses(testDate2);
			for (OWLClass dateClass : dateClasses) {
				// Get the annotations on the class that use the label property (rdfs:label)
				for (OWLAnnotation annotation : dateClass.getAnnotations(analyzer.ontology, analyzer.df.getRDFSLabel())) {
				  if (annotation.getValue() instanceof OWLLiteral) {
				    OWLLiteral val = (OWLLiteral) annotation.getValue();
				    	System.out.println(dateClass + " : \"" + val.getLiteral() + "\"");
				   }
				}
			}
			
			dateClasses = analyzer.getSpecialDateClasses(testDate3);
			for (OWLClass dateClass : dateClasses) {
				// Get the annotations on the class that use the label property (rdfs:label)
				for (OWLAnnotation annotation : dateClass.getAnnotations(analyzer.ontology, analyzer.df.getRDFSLabel())) {
				  if (annotation.getValue() instanceof OWLLiteral) {
				    OWLLiteral val = (OWLLiteral) annotation.getValue();
				    	System.out.println(dateClass + " : \"" + val.getLiteral() + "\"");
				   }
				}
			}
		}
	}
	
	/*
	 * TODO: what about the last week?
	 */
	private OWLClass getMonthWeekClass(int i) {
		switch (i) {
			case 1: return df.getOWLClass(IRI.create(CalendarConstants.WEEK_1_CLS));
			case 2: return df.getOWLClass(IRI.create(CalendarConstants.WEEK_2_CLS));
			case 3: return df.getOWLClass(IRI.create(CalendarConstants.WEEK_3_CLS));
			case 4: return df.getOWLClass(IRI.create(CalendarConstants.WEEK_4_CLS));
			case 5: return df.getOWLClass(IRI.create(CalendarConstants.WEEK_5_CLS));
			default: return null;
		}
	}
	
	private OWLClass getMonthClass(int i) {
		switch (i) {
			case 0: return df.getOWLClass(IRI.create(CalendarConstants.MONTH_JAN_CLS));
			case 1: return df.getOWLClass(IRI.create(CalendarConstants.MONTH_FEB_CLS));
			case 2: return df.getOWLClass(IRI.create(CalendarConstants.MONTH_MAR_CLS));
			case 3: return df.getOWLClass(IRI.create(CalendarConstants.MONTH_APR_CLS));
			case 4: return df.getOWLClass(IRI.create(CalendarConstants.MONTH_MAY_CLS));
			case 5: return df.getOWLClass(IRI.create(CalendarConstants.MONTH_JAN_CLS));
			case 6: return df.getOWLClass(IRI.create(CalendarConstants.MONTH_JUL_CLS));
			case 7: return df.getOWLClass(IRI.create(CalendarConstants.MONTH_AUG_CLS));
			case 8: return df.getOWLClass(IRI.create(CalendarConstants.MONTH_SEP_CLS));
			case 9: return df.getOWLClass(IRI.create(CalendarConstants.MONTH_OCT_CLS));
			case 10: return df.getOWLClass(IRI.create(CalendarConstants.MONTH_NOV_CLS));
			case 11: return df.getOWLClass(IRI.create(CalendarConstants.MONTH_DEC_CLS));
			default: return null;
		}
	}
	
	private OWLClass getWeekDayClass(int i) {
		switch (i) {
			case 1: return df.getOWLClass(IRI.create(CalendarConstants.WEEKDAY_SUN_CLS));
			case 2: return df.getOWLClass(IRI.create(CalendarConstants.WEEKDAY_MON_CLS));
			case 3: return df.getOWLClass(IRI.create(CalendarConstants.WEEKDAY_TUE_CLS));
			case 4: return df.getOWLClass(IRI.create(CalendarConstants.WEEKDAY_WED_CLS));
			case 5: return df.getOWLClass(IRI.create(CalendarConstants.WEEKDAY_THU_CLS));
			case 6: return df.getOWLClass(IRI.create(CalendarConstants.WEEKDAY_FRI_CLS));
			case 7: return df.getOWLClass(IRI.create(CalendarConstants.WEEKDAY_SAT_CLS));
			default: return null;
		}
	}
	
	private OWLClass getMonthDayClass(int i) {
		switch (i) {
			case 1: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_1_CLS));
			case 2: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_2_CLS));
			case 3: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_3_CLS));
			case 4: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_4_CLS));
			case 5: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_5_CLS));
			case 6: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_6_CLS));
			case 7: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_7_CLS));
			case 8: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_8_CLS));
			case 9: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_9_CLS));
			case 10: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_10_CLS));
			case 11: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_11_CLS));
			case 12: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_12_CLS));
			case 13: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_13_CLS));
			case 14: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_14_CLS));
			case 15: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_15_CLS));
			case 16: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_16_CLS));
			case 17: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_17_CLS));
			case 18: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_18_CLS));
			case 19: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_19_CLS));
			case 20: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_20_CLS));
			case 21: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_21_CLS));
			case 22: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_22_CLS));
			case 23: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_23_CLS));
			case 24: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_24_CLS));
			case 25: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_25_CLS));
			case 26: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_26_CLS));
			case 27: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_27_CLS));
			case 28: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_28_CLS));
			case 29: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_29_CLS));
			case 30: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_30_CLS));
			case 31: return df.getOWLClass(IRI.create(CalendarConstants.MONTHDAY_31_CLS));
			default: return null;
		}
	}

}
