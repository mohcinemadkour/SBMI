package edu.tmc.uth.teo.calendaranalyzer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.visitors.ConvertToDNF;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

import edu.tmc.uth.teo.exceptions.TEOException;

/**
 * This class helps enumerate possible date for a given speical date.
 * For example, if given "Independence Day" and constraint the year = 2014,
 * it is able to answer the date "07/04/2014".
 * 
 * 
 * @author yluo
 *
 */
public class TEOJENACalendarAnalyzer {
	public HashMap<String, String> restrictionMap = new HashMap<String, String>(); // R? --> occurYear some (http://informatics.mayo.edu/TEO.owl#TEO_0000042)

	private OntModel model = null;
	
	public OntModel getModel() {
		return model;
	}

	public void setModel(OntModel model) {
		this.model = model;
	}

	public TEOJENACalendarAnalyzer() {
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	}
	
	public void load(String fileName) {
		model.read(fileName);
	}
	
	public void printRestrictMap() {
		if (restrictionMap != null) {
			Set<String> keySet = restrictionMap.keySet();
			for (String key: keySet) {
				System.out.println("key=" + key + ", value=" + restrictionMap.get(key));
			}
		}
	}
	
	public String getObjectPropSomeValueFrom(Restriction restrict) {
		String key = "";
		if (restrict.isSomeValuesFromRestriction()) {
			String some = "";
			SomeValuesFromRestriction someValueFrom = restrict.asSomeValuesFromRestriction();

			OntProperty prop = someValueFrom.getOnProperty();
			some += prop;
			
			OntClass fromCls = (OntClass) someValueFrom.getSomeValuesFrom();	
			some += " some (";
			
			if (fromCls.isUnionClass()) { // or
				ExtendedIterator<? extends OntClass> operandsItor = fromCls.asUnionClass().listOperands();
				OntClass disjClass = null;
				int count = 0;
				while (operandsItor.hasNext()) {
					disjClass = operandsItor.next();
					if (count == 0) 
						some += disjClass;
					else
						some += (" or " + disjClass);
					count ++;
				}
			} else if (fromCls.hasProperty( RDF.type, RDFS.Datatype )) { // the type is rdfs:datatype
				Property owlWithRestrictions = ResourceFactory.createProperty(OWL.getURI(), "withRestrictions");
				Property minInclusive = ResourceFactory.createProperty(XSD.getURI(), "minInclusive");
				Property maxInclusive = ResourceFactory.createProperty(XSD.getURI(), "maxInclusive");
				
				Resource wr = fromCls.getProperty(owlWithRestrictions).getResource();
				RDFList wrl = wr.as(RDFList.class);

				int min = Integer.MIN_VALUE, max = Integer.MAX_VALUE;
				// Note: a simple assumption here, only elements w/ min or max in the list!
				for (Iterator<RDFNode> k = wrl.iterator(); k.hasNext();) {
					Resource wrClause = (Resource) k.next();
					if (wrClause.getProperty(minInclusive) != null) {
						Literal minValue = wrClause.getProperty(minInclusive).getLiteral();
						if (minValue != null) min = minValue.getInt();
					}
					if (wrClause.getProperty(maxInclusive) != null) {
						Literal maxValue = wrClause.getProperty(maxInclusive).getLiteral();
						if (maxValue != null) max = maxValue.getInt();
					}
				}	
				
				some += "[" + min + ", " + max + "]";
			} else {
				some += fromCls;
			}
			
			some += ")";
			
			int index = restrictionMap.size() + 1;
			key = "R" + index;
			restrictionMap.put(key, some);
		}
		return key;
	}
	
	public String getLogicAlgebra(OntClass clsExpression) {
		if (!clsExpression.isIntersectionClass() && !clsExpression.isUnionClass()) {
			if (clsExpression.isRestriction()) { // then it should be someValueFrom
				Restriction restrict = clsExpression.asRestriction();
				if (restrict.isSomeValuesFromRestriction()) {		
					return getObjectPropSomeValueFrom(restrict.asSomeValuesFromRestriction());
				} else {
					return "unknown_restrict";
				}
			} else {
				return "unknown_class";
			}
		} else { 
			String result = " ( ";
			if (clsExpression.isIntersectionClass()) {
				ExtendedIterator<? extends OntClass> operandsItor = clsExpression.asIntersectionClass().listOperands();
				OntClass conjClass = null;
				int count = 0;
				while (operandsItor.hasNext()) {
					conjClass = operandsItor.next();
					if (count == 0) 
						result += getLogicAlgebra(conjClass);
					else
						result += (" & " + getLogicAlgebra(conjClass));
					count ++;
				}	
			} else if (clsExpression.isUnionClass()) {				
				ExtendedIterator<? extends OntClass> operandsItor = clsExpression.asUnionClass().listOperands();
				OntClass disjClass = null;
				int count = 0;
				while (operandsItor.hasNext()) {
					disjClass = operandsItor.next();
					if (count == 0) 
						result += getLogicAlgebra(disjClass);
					else
						result += (" | " + getLogicAlgebra(disjClass));
					count ++;
				}
			}
			result += " ) ";
			return result;
		}
	}
	
	public DateConstraint translateSimpleSentenceIntoRules(String simpleSentence) {		
		DateConstraint dateConstraint = new DateConstraint();
		
		String[] conKeys = simpleSentence.split("&");
		String condition = null;
		for (int i = 0; i < conKeys.length; i ++) {
			condition = restrictionMap.get(conKeys[i].trim());
			if (condition != null) {
				addConstraintCondition(dateConstraint, condition);
			}
		}
		
		return dateConstraint;
	}
	
	public void addConstraintCondition(DateConstraint dateConstraint, String condition) {
		assert(dateConstraint != null && condition != null);
		
		// parse the condition first
		String[] parts = condition.split(" some ");
		String classes = parts[1].substring(1, parts[1].length() - 1).trim(); // trim '(' and ')'
		String prop = getProp(parts[0].trim());
		OntClass cls = null;
		
		if (prop.equals("occurDay")) {
			if (classes.contains("or")) { // Assumption: only or
				String[] clsArray = classes.split(" or ");
				for (int i = 0; i < clsArray.length; i ++) {
					classes = clsArray[i].trim();
					cls = model.getOntClass(classes);
					if (cls.getSuperClass().toString().contains(CalendarConstants.MONTHDAY_CLS)) { // month day
						dateConstraint.addMonthDay(getMonthDay(classes));
					} else { // week day
						dateConstraint.addWeekDay(getWeekDay(classes));
					}
				}
			} else { // only one class
				cls = model.getOntClass(classes);
				if (cls.getSuperClass().toString().contains(CalendarConstants.MONTHDAY_CLS)) { // month day
					dateConstraint.addMonthDay(getMonthDay(classes));
				} else { // week day
					dateConstraint.addWeekDay(getWeekDay(classes));
				}
			}
		} else if (prop.equals("occurMonth")) {
			if (classes.contains("or")) { // Assumption: only or
				String[] clsArray = classes.split(" or ");
				for (int i = 0; i < clsArray.length; i ++) {
					classes = clsArray[i].trim();
					dateConstraint.addMonth(getMonth(classes));
				}
			} else { // only one class
				dateConstraint.addMonth(getMonth(classes));
			}
		} else if (prop.equals("occurWeek")) {
			if (classes.contains("or")) { // Assumption: only or
				String[] clsArray = classes.split(" or ");
				for (int i = 0; i < clsArray.length; i ++) {
					classes = clsArray[i].trim();
					dateConstraint.addMonthWeek(getMonthWeek(classes));
				}
			} else { // only one class
				dateConstraint.addMonthWeek(getMonthWeek(classes));
			}
		} else if (prop.equals("occurYear")) {
			String[] ranges = classes.substring(1, classes.length() - 1).trim().split(", "); // trim '[' and ']'
			int minYear = Integer.parseInt(ranges[0]);
			int maxYear = Integer.parseInt(ranges[1]);
			dateConstraint.setMinYear(minYear);
			dateConstraint.setMaxYear(maxYear);
		}
	}
	
	/**
	 * This method outputs a list of dates for a given special date class (e.g. holiday) from the TEO and an extra constraint (e.g. the year).
	 * 
	 * @param classIRIStr
	 * @param extraConstraint
	 * @return
	 * @throws TEOException 
	 */
	public ArrayList<String> getSpecialDateInstances(String classIRIStr, DateConstraint extraConstraint) throws TEOException{
		OntClass testClass = model.getOntClass(classIRIStr);
		if (testClass == null) throw new TEOException("Exception: Cannot find class \"" + classIRIStr + "\" in the ontology.");
		
		ExtendedIterator<OntClass> eqClassItor = testClass.listEquivalentClasses(); // constraints
		ArrayList<DateConstraint> dateClsConstraints = new ArrayList<DateConstraint>();
		ArrayList<String> results = new ArrayList<String>();
		
		String logicExpression = null;
		OntClass eqClass = null;
		
		while (eqClassItor.hasNext()) {
			eqClass = eqClassItor.next();
			logicExpression = getLogicAlgebra(eqClass);
			System.out.println("Original:\t" + logicExpression);	
			
			PLParser parser = new PLParser();
			Sentence symbol = parser.parse(logicExpression);
			Sentence transformed = ConvertToDNF.convert(symbol);
			System.out.println("Transformed:\t" + transformed.toString());
			
			// parse all constraints
			String[] disjSentences = transformed.toString().split("\\|");
			for (int i = 0; i < disjSentences.length; i ++) {
				dateClsConstraints.add(translateSimpleSentenceIntoRules(disjSentences[i].trim()));
//				System.out.println(dateClsConstraints.get(dateClsConstraints.size() - 1));
			}
		}
		
		if (!dateClsConstraints.isEmpty()) {
			DateConstraint mergedConstraint = null;
			for (DateConstraint eachConstraint : dateClsConstraints) {
				mergedConstraint = DateConstraint.intersectionDateConstraint(eachConstraint, extraConstraint);
				System.out.println(mergedConstraint);
				results.addAll(DateConstraint.enumerateDates(mergedConstraint));
			}
		}
		
		return results;
	}
	
	public static void main(String args[]) {
		TEOJENACalendarAnalyzer analyzer = new TEOJENACalendarAnalyzer();
		String fileName = "src/test/resources/TEO/TEO_1.1.0.owl";
		analyzer.load(fileName);
		
		// test case - all defined Federal Holidays
		String classStr = CalendarConstants.getWithNS("TEO_0000017"); // federal holidays
		OntClass rootClass = analyzer.getModel().getOntClass(classStr);
		ExtendedIterator<OntClass> subClsItor = rootClass.listSubClasses(true);
		
		int testYear = 2019;
		DateConstraint yearConstraint = new DateConstraint();
		yearConstraint.setMaxYear(testYear);
		yearConstraint.setMinYear(testYear);
		
		ArrayList<String> dates = null;
		OntClass subCls = null;
		
		while (subClsItor.hasNext()) {
			try {
				subCls = subClsItor.next();
				dates = analyzer.getSpecialDateInstances(subCls.toString(), yearConstraint);
				for (String date : dates) {
					System.out.println("Possible dates of \"" + subCls.getLabel(null) + "\" in year = " + testYear + " are: " + date);
					System.out.println();
				}
			} catch (TEOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getProp(String propIRIStr) {
		if (propIRIStr.equals(CalendarConstants.OCCURDAY_PRP)) {
			return "occurDay";
		} else if (propIRIStr.equals(CalendarConstants.OCCURMONTH_PRP)) {
			return "occurMonth";
		} else if (propIRIStr.equals(CalendarConstants.OCCURWEEK_PRP)) {
			return "occurWeek";
		} else if (propIRIStr.equals(CalendarConstants.OCCURYEAR_PRP)) {
			return "occurYear";
		} else {
			return "unknown";
		}
	}
	
	public int getMonth(String clsIRIStr) {
		if (clsIRIStr.equals(CalendarConstants.MONTH_JAN_CLS)) {
			return Calendar.JANUARY;
		} else if (clsIRIStr.equals(CalendarConstants.MONTH_FEB_CLS)) {
			return Calendar.FEBRUARY;
		} else if (clsIRIStr.equals(CalendarConstants.MONTH_MAR_CLS)) {
			return Calendar.MARCH;
		} else if (clsIRIStr.equals(CalendarConstants.MONTH_APR_CLS)) {
			return Calendar.APRIL;
		} else if (clsIRIStr.equals(CalendarConstants.MONTH_MAY_CLS)) {
			return Calendar.MAY;
		} else if (clsIRIStr.equals(CalendarConstants.MONTH_JUN_CLS)) {
			return Calendar.JUNE;
		} else if (clsIRIStr.equals(CalendarConstants.MONTH_JUL_CLS)) {
			return Calendar.JULY;
		} else if (clsIRIStr.equals(CalendarConstants.MONTH_AUG_CLS)) {
			return Calendar.AUGUST;
		} else if (clsIRIStr.equals(CalendarConstants.MONTH_SEP_CLS)) {
			return Calendar.SEPTEMBER;
		} else if (clsIRIStr.equals(CalendarConstants.MONTH_OCT_CLS)) {
			return Calendar.OCTOBER;
		} else if (clsIRIStr.equals(CalendarConstants.MONTH_NOV_CLS)) {
			return Calendar.NOVEMBER;
		} else if (clsIRIStr.equals(CalendarConstants.MONTH_DEC_CLS)) {
			return Calendar.DECEMBER;
		} else {
			return -1;
		}
	}
	
	public int getMonthDay(String clsIRIStr) {
		if (clsIRIStr.equals(CalendarConstants.MONTHDAY_1_CLS)) {
			return 1;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_2_CLS)) {
			return 2;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_3_CLS)) {
			return 3;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_4_CLS)) {
			return 4;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_5_CLS)) {
			return 5;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_6_CLS)) {
			return 6;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_7_CLS)) {
			return 7;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_8_CLS)) {
			return 8;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_9_CLS)) {
			return 9;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_10_CLS)) {
			return 10;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_11_CLS)) {
			return 11;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_12_CLS)) {
			return 12;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_13_CLS)) {
			return 13;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_14_CLS)) {
			return 14;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_15_CLS)) {
			return 15;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_16_CLS)) {
			return 16;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_17_CLS)) {
			return 17;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_18_CLS)) {
			return 18;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_19_CLS)) {
			return 19;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_20_CLS)) {
			return 20;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_21_CLS)) {
			return 21;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_23_CLS)) {
			return 22;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_23_CLS)) {
			return 23;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_24_CLS)) {
			return 24;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_25_CLS)) {
			return 25;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_26_CLS)) {
			return 26;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_27_CLS)) {
			return 27;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_28_CLS)) {
			return 28;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_29_CLS)) {
			return 29;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_30_CLS)) {
			return 30;
		} else if (clsIRIStr.equals(CalendarConstants.MONTHDAY_31_CLS)) {
			return 31;
		} else {
			return -1;
		}
	}
	
	public int getWeekDay(String clsIRIStr) {
		if (clsIRIStr.equals(CalendarConstants.WEEKDAY_SUN_CLS)) {
			return Calendar.SUNDAY;
		} else if (clsIRIStr.equals(CalendarConstants.WEEKDAY_MON_CLS)) {
			return Calendar.MONDAY;
		} else if (clsIRIStr.equals(CalendarConstants.WEEKDAY_TUE_CLS)) {
			return Calendar.TUESDAY;
		} else if (clsIRIStr.equals(CalendarConstants.WEEKDAY_WED_CLS)) {
			return Calendar.WEDNESDAY;
		} else if (clsIRIStr.equals(CalendarConstants.WEEKDAY_THU_CLS)) {
			return Calendar.THURSDAY;
		} else if (clsIRIStr.equals(CalendarConstants.WEEKDAY_FRI_CLS)) {
			return Calendar.FRIDAY;
		} else if (clsIRIStr.equals(CalendarConstants.WEEKDAY_SAT_CLS)) {
			return Calendar.SATURDAY;
		} else {
			return -1;
		}
	}
	
	public int getMonthWeek(String clsIRIStr) {
		if (clsIRIStr.equals(CalendarConstants.WEEK_1_CLS)) {
			return 1;
		} else if (clsIRIStr.equals(CalendarConstants.WEEK_2_CLS)) {
			return 2;
		} else if (clsIRIStr.equals(CalendarConstants.WEEK_3_CLS)) {
			return 3;
		} else if (clsIRIStr.equals(CalendarConstants.WEEK_4_CLS)) {
			return 4;
		} else if (clsIRIStr.equals(CalendarConstants.WEEK_5_CLS) || clsIRIStr.equals(CalendarConstants.WEEK_LAST_CLS)) {
			return 5;
		} else {
			return -1;
		}
	}
}
