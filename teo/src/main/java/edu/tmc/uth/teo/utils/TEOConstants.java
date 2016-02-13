package edu.tmc.uth.teo.utils;

/**
 * This class defines all constants used in the teo-library.
 * 
 * @author yluo
 *
 */
public class TEOConstants {
	/**
	 * Name Spaces
	 */
	public static final String TEO_NS = "http://informatics.mayo.edu/TEO.owl#";
	

/*----------------------------------------------------Temporal Reasoning-----------------------------------------------*/	
	/**
	 * Class Names
	 */
	public static final String TEO_EVENT_CLS_NAME = "TEO_0000025"; // Event
	public static final String TEO_HOLIDAY_CLS_NAME = "TEO_0000011"; // Holiday
	public static final String TEO_TIMEGRANULARITY_CLS_NAME = "TEO_0000012"; // Time Granularity
	public static final String TEO_FREQUENCY_CLS_NAME = "TEO_0000023"; // Frequency
	public static final String TEO_TIMEPERIOD_CLS_NAME = "TEO_0000036"; // TimePeriod
	public static final String TEO_TIMEPHASE_CLS_NAME = "TEO_0000024"; // TimePhase
	public static final String TEO_DURATIONMEASUREMENT_CLS_NAME = "TEO_0000032"; // Duration Measurement
	public static final String TEO_PERIODICTIMEINTERVAL_CLS_NAME = "TEO_0000034"; // Periodic Time Interval
	public static final String TEO_TIMESEQUENCE_CLS_NAME = "TEO_0000044"; // TimeSequence	
	
	/**
	 * Object Property Names
	 */	
	// events
	public static final String TEO_HASVALIDTIME_PRP_NAME = "TEO_0000007"; // hasValidTime [domain: Event (Yi: + TimeInstant?)]
	// time interval
	public static final String TEO_HASSTARTTIME_PRP_NAME = "TEO_0000028"; // hasStartTime [domain: TimeInterval + Periodic TimeInterval]
	public static final String TEO_HASENDTIME_PRP_NAME = "TEO_0000006"; // hasEndTime [domain: TimeInterval + Periodic TimeInterval]
	public static final String TEO_HASDURATION_PRP_NAME = "TEO_0000022"; // hasDuration [domain: TimeInterval + Periodic TimeInterval, range: Duration Measurement]
	public static final String TEO_HASDURATIONUNIT_PRP_NAME = "TEO_0000040"; // domain: Duration Measurement
	public static final String TEO_HASGRANULARITY_PRP_NAME = "TEO_0000029"; // domain: temporal_region
	public static final String TEO_HASPHASE_PRP_NAME = "TEO_0000077"; // domain: Periodic TimeInterval, range: TimePhase
	public static final String TEO_HASPERIOD_PRP_NAME = "TEO_0000078"; // domain: Periodic TimeInterval, range: TimePeriod
	// frequency
	public static final String TEO_DENOMINATOR_PRP_NAME = "TEO_0000076"; // domain: Frequency
	public static final String TEO_HASFREQUENCY_PRP_NAME = "TEO_0000079"; // range: Frequency
	// temporal relations
	public static final String TEO_TEMPORALRELATION_PRP_NAME = "TEO_0000039";
	public static final String TEO_TR_AFTER_PRP_NAME = "TEO_0000016"; // after
	public static final String TEO_TR_BEFORE_PRP_NAME = "TEO_0000002"; // before
	public static final String TEO_TR_MEET_PRP_NAME = "TEO_0000020"; // meet
	public static final String TEO_TR_OVERLAP_PRP_NAME = "TEO_0000038"; // overlap
	public static final String TEO_TR_CONTAIN_PRP_NAME = "TEO_0000010"; // contain
	public static final String TEO_TR_DURING_PRP_NAME = "TEO_0000003"; // during
	public static final String TEO_TR_EQUAL_PRP_NAME = "TEO_0000018"; // equal
	public static final String TEO_TR_FINISH_PRP_NAME = "TEO_0000037"; // finish
	public static final String TEO_TR_START_PRP_NAME = "TEO_0000014"; // start
	public static final String TEO_TR_METBY_PRP_NAME = "TEO_0000146"; // met by
	public static final String TEO_TR_OVERLAPPEDBY_PRP_NAME = "TEO_0000145"; // overlapped by
	public static final String TEO_TR_FINISHEDBY_PRP_NAME = "TEO_0000148"; // finished by
	public static final String TEO_TR_STARTEDBY_PRP_NAME = "TEO_0000147"; // started by
	
	public static final String TEO_TR_SBS_PRP_NAME = "TEO_0000150"; // startBeforeStart
	public static final String TEO_TR_SBE_PRP_NAME = "TEO_0000151"; // startBeforeEnd
	public static final String TEO_TR_EBS_PRP_NAME = "TEO_0000154"; // endBeforeStart
	public static final String TEO_TR_EBE_PRP_NAME = "TEO_0000155"; // endBeforeEnd
	public static final String TEO_TR_SAS_PRP_NAME = "TEO_0000152"; // startAfterStart
	public static final String TEO_TR_SAE_PRP_NAME = "TEO_0000153"; // startAfterEnd
	public static final String TEO_TR_EAS_PRP_NAME = "TEO_0000156"; // endAfterStart
	public static final String TEO_TR_EAE_PRP_NAME = "TEO_0000157"; // endAfterEnd
	public static final String TEO_TR_SES_PRP_NAME = "TEO_0000149"; // startEqualStart
	public static final String TEO_TR_SEE_PRP_NAME = "TEO_0000158"; // startEqualEnd
	public static final String TEO_TR_EES_PRP_NAME = "TEO_0000159"; // endEqualStart
	public static final String TEO_TR_EEE_PRP_NAME = "TEO_0000160"; // endEqualEnd
	

	// time sequence
	public static final String TEO_HASTIMESEQUENCE_PRP_NAME = "TEO_0000096";// domain: ConnectedTemporalRegion + ScatteredTemporalRegion

	
	/**
	 * Data Property Names
	 */
	public static final String TEO_HASMODALITY_PRP_NAME = "TEO_0000004";
	public static final String TEO_HASNORMALIZEDTIME_PRP_NAME = "TEO_0000005";
	public static final String TEO_HASORIGTIME_PRP_NAME = "TEO_0000015";
	public static final String TEO_HASDURATIONVALUE_PRP_NAME = "TEO_0000041"; // hasDurationValue (should go with hasUnit)
	public static final String TEO_HASDURATIONPATTERN_PRP_NAME = "TEO_0000144"; // hasDurationValue
	public static final String TEO_HASCALENDARPATTERNFORM_PRP_NAME = "TEO_0000064"; // hasCalendarPatternForm
	public static final String TEO_NUMERATOR_PRP_NAME = "TEO_0000076"; // domain: Frequency

	/**
	 * Annotation Property Names
	 */
	public static final String TEO_HASTIMEOFFSET_PRP_NAME = "hasTimeOffset";
	
	/**
	 *  Classes
	 */
	public static final String TEMPORAL_INSTANT_CLS = "http://www.ifomis.org/bfo/1.1/span#TemporalInstant";
	public static final String TEMPORAL_INTERVAL_CLS = "http://www.ifomis.org/bfo/1.1/span#TemporalInterval";
	public static final String PERIODIC_TIME_INTERVAL_CLS = getWithNS(TEO_PERIODICTIMEINTERVAL_CLS_NAME);
	
	public static final String TEO_EVENT_CLS = getWithNS(TEO_EVENT_CLS_NAME);
	public static final String TEO_HOLIDAY_CLS = getWithNS(TEO_HOLIDAY_CLS_NAME);
	public static final String TEO_TIMEGRANULARITY_CLS = getWithNS(TEO_TIMEGRANULARITY_CLS_NAME);
	public static final String TEO_FREQUENCY_CLS = getWithNS(TEO_FREQUENCY_CLS_NAME);
	public static final String TEO_TIMEPERIOD_CLS = getWithNS(TEO_TIMEPERIOD_CLS_NAME);
	public static final String TEO_TIMEPHASE_CLS = getWithNS(TEO_TIMEPHASE_CLS_NAME);
	public static final String TEO_DURATIONMEASUREMENT_CLS = getWithNS(TEO_DURATIONMEASUREMENT_CLS_NAME);
	public static final String TEO_TIMESEQUENCE_CLS = getWithNS(TEO_TIMESEQUENCE_CLS_NAME);

	
	
	/**
	 *  Properties
	 */
	public static final String TEO_HASVALIDTIME_PRP = getWithNS(TEO_HASVALIDTIME_PRP_NAME);
	public static final String TEO_HASSTARTTIME_PRP = getWithNS(TEO_HASSTARTTIME_PRP_NAME);
	public static final String TEO_HASENDTIME_PRP = getWithNS(TEO_HASENDTIME_PRP_NAME);
	public static final String TEO_HASDURATION_PRP = getWithNS(TEO_HASDURATION_PRP_NAME);
	public static final String TEO_HASDURATIONUNIT_PRP = getWithNS(TEO_HASDURATIONUNIT_PRP_NAME);
	public static final String TEO_HASGRANULARITY_PRP = getWithNS(TEO_HASGRANULARITY_PRP_NAME);
	public static final String TEO_HASPHASE_PRP = getWithNS(TEO_HASPHASE_PRP_NAME);
	public static final String TEO_HASPERIOD_PRP = getWithNS(TEO_HASPERIOD_PRP_NAME);
	public static final String TEO_DENOMINATOR_PRP = getWithNS(TEO_DENOMINATOR_PRP_NAME);
	public static final String TEO_HASFREQUENCY_PRP = getWithNS(TEO_HASFREQUENCY_PRP_NAME);
	public static final String TEO_TEMPORALRELATION_PRP = getWithNS(TEO_TEMPORALRELATION_PRP_NAME);
	public static final String TEO_TR_AFTER_PRP = getWithNS(TEO_TR_AFTER_PRP_NAME);
	public static final String TEO_TR_BEFORE_PRP = getWithNS(TEO_TR_BEFORE_PRP_NAME);
	public static final String TEO_TR_MEET_PRP = getWithNS(TEO_TR_MEET_PRP_NAME);
	public static final String TEO_TR_OVERLAP_PRP = getWithNS(TEO_TR_OVERLAP_PRP_NAME);
	public static final String TEO_TR_CONTAIN_PRP = getWithNS(TEO_TR_CONTAIN_PRP_NAME);
	public static final String TEO_TR_DURING_PRP = getWithNS(TEO_TR_DURING_PRP_NAME);
	public static final String TEO_TR_EQUAL_PRP = getWithNS(TEO_TR_EQUAL_PRP_NAME);
	public static final String TEO_TR_FINISH_PRP = getWithNS(TEO_TR_FINISH_PRP_NAME);
	public static final String TEO_TR_START_PRP = getWithNS(TEO_TR_START_PRP_NAME);
		
	public static final String TEO_TR_METBY_PRP = getWithNS(TEO_TR_METBY_PRP_NAME);
	public static final String TEO_TR_OVERLAPPEDBY_PRP = getWithNS(TEO_TR_OVERLAPPEDBY_PRP_NAME);
	public static final String TEO_TR_FINISHEDBY_PRP = getWithNS(TEO_TR_FINISHEDBY_PRP_NAME);
	public static final String TEO_TR_STARTEDBY_PRP = getWithNS(TEO_TR_STARTEDBY_PRP_NAME);
	
	public static final String TEO_TR_SBS_PRP = getWithNS(TEO_TR_SBS_PRP_NAME);
	public static final String TEO_TR_SBE_PRP = getWithNS(TEO_TR_SBE_PRP_NAME);
	public static final String TEO_TR_EBS_PRP = getWithNS(TEO_TR_EBS_PRP_NAME);
	public static final String TEO_TR_EBE_PRP = getWithNS(TEO_TR_EBE_PRP_NAME);
	public static final String TEO_TR_SAS_PRP = getWithNS(TEO_TR_SAS_PRP_NAME);
	public static final String TEO_TR_SAE_PRP = getWithNS(TEO_TR_SAE_PRP_NAME);
	public static final String TEO_TR_EAS_PRP = getWithNS(TEO_TR_EAS_PRP_NAME);
	public static final String TEO_TR_EAE_PRP = getWithNS(TEO_TR_EAE_PRP_NAME);	
	public static final String TEO_TR_SES_PRP = getWithNS(TEO_TR_SES_PRP_NAME);
	public static final String TEO_TR_SEE_PRP = getWithNS(TEO_TR_SEE_PRP_NAME);
	public static final String TEO_TR_EES_PRP = getWithNS(TEO_TR_EES_PRP_NAME);
	public static final String TEO_TR_EEE_PRP = getWithNS(TEO_TR_EEE_PRP_NAME);
	
	public static final String TEO_HASTIMESEQUENCE_PRP = getWithNS(TEO_HASTIMESEQUENCE_PRP_NAME);
	public static final String TEO_HASMODALITY_PRP = getWithNS(TEO_HASMODALITY_PRP_NAME);
	public static final String TEO_HASNORMALIZEDTIME_PRP = getWithNS(TEO_HASNORMALIZEDTIME_PRP_NAME);
	public static final String TEO_HASORIGTIME_PRP = getWithNS(TEO_HASORIGTIME_PRP_NAME);
	public static final String TEO_HASDURATIONVALUE_PRP = getWithNS(TEO_HASDURATIONVALUE_PRP_NAME);
	public static final String TEO_HASCALENDARPATTERNFORM_PRP = getWithNS(TEO_HASCALENDARPATTERNFORM_PRP_NAME);
	public static final String TEO_NUMERATOR_PRP = getWithNS(TEO_NUMERATOR_PRP_NAME);
	public static final String TEO_HASDURATIONPATTERN_PRP = getWithNS(TEO_HASDURATIONPATTERN_PRP_NAME);
	
	public static final String TEO_HASTIMEOFFSET_PRP = getWithNS(TEO_HASTIMEOFFSET_PRP_NAME);
	
	
	// ------------------------------------Temporal Relation Short Code-----------------------------------------------
	// representation of the constraints in binary format
		public final static short bin_before = 1; // 0000000000000001 - p
		public final static short bin_after = 2;  // 0000000000000010 - P
		public final static short bin_during = 4; // 0000000000000100 - d
		public final static short bin_contains = 8; // 0000000000001000 - D
		public final static short bin_overlaps = 16; // 0000000000010000 - o
		public final static short bin_overlappedby = 32; // 0000000000100000 - O
		public final static short bin_meets = 64; // 0000000001000000 - m
		public final static short bin_metby = 128; // 0000000010000000 - M
		public final static short bin_starts = 256;// 0000000100000000 - s
		public final static short bin_startedby = 512; // 0000001000000000 - S
		public final static short bin_finishes = 1024;  // 0000010000000000 - f
		public final static short bin_finishedby = 2048;// 0000100000000000 - F
		public final static short bin_equals = 4096;    // 0001000000000000 - e
		public final static short bin_full = (short) (bin_before | bin_after | bin_during | bin_contains | bin_overlaps | bin_overlappedby | bin_meets | bin_metby | bin_starts | bin_startedby | bin_finishes | bin_finishedby | bin_equals);		  // 0001111111111111

		// Yi: point relations
		public final static short bin_SBS = (short) (bin_before | bin_meets | bin_overlaps | bin_finishedby | bin_contains); // 0000100001011001 - startBeforeStart (pmoFD)
		public final static short bin_SAS = (short) (bin_during | bin_finishes | bin_overlappedby | bin_metby | bin_after); // 0000010010100110 - startAfterStart (dfOMP)
		public final static short bin_SES = (short) (bin_starts | bin_equals | bin_startedby); // 0001001100000000 - startEqualStart (seS)
		public final static short bin_SBE = (short) (bin_before | bin_meets | bin_overlaps | bin_finishedby | 
														bin_contains | bin_starts | bin_equals | bin_startedby |
														bin_during | bin_finishes | bin_overlappedby); // 0001111101111101 - startBeforeEnd (pmoFDseSdfO)
		public final static short bin_SAE = (short) (bin_after); // 0000000000000010 - startAfterEnd (P)
		public final static short bin_SEE = (short) (bin_metby); // 0000000010000000 - startEqualEnd (M)
		public final static short bin_EBE = (short) (bin_before | bin_meets | bin_overlaps | bin_starts | bin_during); // 0000000101010101 - endBeforeEnd (pmosd)
		public final static short bin_EAE = (short) (bin_contains | bin_startedby | bin_overlappedby | bin_metby | bin_after); // 0000001010101010 - endAfterEnd (DSOMP)
		public final static short bin_EEE = (short) (bin_finishes | bin_equals | bin_finishedby); // 0001110000000000 - endEqualEnd (feF)
		public final static short bin_EBS = (short) (bin_before); // 0000100001011001 - endBeforeStart (p)
		public final static short bin_EAS = (short) (bin_overlaps | bin_finishedby | bin_contains | bin_starts | 
														bin_equals | bin_startedby | bin_during | bin_finishes |
														bin_overlappedby | bin_metby | bin_after); // 0000000000010000 - endAfterStart (oFDseSdfOMP)
		public final static short bin_EES = (short) (bin_meets); // 0000000001000000 - endEqualStart (m)	
		// last bit is used as a sign
	
		// A corrected version of the transivity matrix described by Allen
		public final static short[][] transitivematrixshort = {
			// first row before
			{bin_before,bin_full,bin_before | bin_overlaps | bin_meets | bin_during | bin_starts, bin_before, bin_before, bin_before | bin_overlaps | bin_meets | bin_during | bin_starts, bin_before, bin_before| bin_overlaps | bin_meets | bin_during | bin_starts,  bin_before, bin_before, bin_before | bin_overlaps | bin_meets | bin_during | bin_starts, bin_before, bin_before},
		//	{"<","< > d di o oi m mi s si f fi e","< o m d s","<","<","< o m d s","<","< o m d s","<","<","< o m d s","<","<"}
			
			// second row after
			{bin_full,bin_after,bin_after | bin_overlappedby | bin_metby | bin_during | bin_finishes, bin_after, bin_after | bin_overlappedby | bin_metby | bin_during | bin_finishes,bin_after,bin_after | bin_overlappedby | bin_metby | bin_during | bin_finishes,bin_after,bin_after | bin_overlappedby | bin_metby | bin_during | bin_finishes,bin_after,bin_after,bin_after,bin_after },
		//	{"< > d di o oi m mi s si f fi e",">","> oi mi d f", ">", "> oi mi d f", ">", "> oi mi d f", ">", "> oi mi d f", ">", ">", ">", ">"}
			// third row during
			{bin_before, bin_after,bin_during,bin_full,bin_before | bin_overlaps | bin_meets | bin_during | bin_starts, bin_after | bin_overlappedby | bin_metby | bin_during | bin_finishes, bin_before, bin_after, bin_during, bin_after | bin_overlappedby | bin_metby | bin_during | bin_finishes, bin_during, bin_before | bin_overlaps | bin_meets | bin_during | bin_starts, bin_during},
//			{"<",">","d", "< > d di o oi m mi s si f fi e", "< o m d s", "> oi mi d f", "<", ">", "d", "> oi mi d f", "d", "< o m d s","d"},
			// fourth row contains
			// note: there seems to be a confusion in allens original table
			// bin_contains -> bin_during is in the original: o, oi, dur, con, e
			// it should be: o oi dur con e s si f fi
			{bin_before | bin_overlaps | bin_meets | bin_contains | bin_finishedby, bin_after | bin_overlappedby | bin_contains | bin_metby | bin_startedby, bin_overlaps | bin_overlappedby | bin_during | bin_contains | bin_equals | bin_starts| bin_startedby | bin_finishes | bin_finishedby, bin_contains, bin_overlaps | bin_contains | bin_finishedby,bin_overlappedby | bin_contains | bin_startedby,bin_overlaps | bin_contains | bin_finishedby,bin_overlappedby | bin_contains | bin_startedby,bin_contains | bin_finishedby | bin_overlaps, bin_contains, bin_contains | bin_startedby | bin_overlappedby, bin_contains,bin_contains},
//			{"< o m di fi","> oi mi di si","o oi dur con e s si f fi","di","o di fi","oi di si","o di fi","oi di si","di fi o","di","di si oi","di","di"}
			// fifth row overlaps 
			// note: there seems to be a confusion in allens original table
			// bin_overlaps -> bin_overlappedby is in the original: o oi dur con e
			// it should be: o oi dur con e s si f fi
			{bin_before,bin_after | bin_overlappedby | bin_contains | bin_metby | bin_startedby,bin_overlaps | bin_during | bin_starts, bin_before | bin_overlaps | bin_meets | bin_contains | bin_finishedby, bin_before | bin_overlaps | bin_meets, bin_overlaps | bin_overlappedby | bin_during | bin_contains | bin_equals | bin_starts| bin_startedby | bin_finishes | bin_finishedby, bin_before, bin_overlappedby | bin_contains | bin_startedby, bin_overlaps, bin_contains | bin_finishedby | bin_overlaps, bin_during | bin_starts | bin_overlaps, bin_before | bin_overlaps | bin_meets, bin_overlaps},
//			{"<","> oi di mi si","o d s","< o m di fi","< o m", "o oi dur con e s si f fi","<","oi di si","o","di fi o","d s o","< o m","o"},
			// six row overlapped by
			// note: there seems to be a mistake in allens original table
			// bin_overlappedby -> bin_overlaps is in the original: o oi dur con e
			// it should be: o oi dur con e s si f fi
			{bin_before | bin_overlaps | bin_meets | bin_contains | bin_finishedby, bin_after, bin_overlappedby | bin_during | bin_finishes, bin_after | bin_overlappedby | bin_metby | bin_contains | bin_startedby, bin_overlaps | bin_overlappedby | bin_during | bin_contains | bin_equals | bin_starts | bin_startedby | bin_finishes| bin_finishedby, bin_after | bin_overlappedby | bin_metby, bin_overlaps | bin_contains | bin_finishedby, bin_after, bin_overlappedby | bin_during | bin_finishes, bin_overlappedby | bin_after | bin_metby, bin_overlappedby, bin_overlappedby | bin_contains | bin_startedby, bin_overlappedby},
//			{"< o m di fi",">","oi d f","> oi mi di si","o oi d di e"[fixed: "o oi dur con e s si f fi"],"> oi mi","o di fi",">","oi d f","oi > mi","oi","oi di si","oi"},
			// seventh row meets    
			{bin_before, bin_after | bin_overlappedby | bin_metby | bin_contains | bin_startedby, bin_overlaps | bin_during | bin_starts, bin_before, bin_before, bin_overlaps | bin_during | bin_starts, bin_before, bin_finishes | bin_finishedby | bin_equals, bin_meets, bin_meets, bin_during | bin_starts | bin_overlaps, bin_before, bin_meets},
//			{"<","> oi mi di si","o d s","<","<","o d s","<","f fi e","m","m","d s o","<","m"},
			// eights row metby 
			{bin_before | bin_overlaps | bin_meets | bin_contains | bin_finishedby, bin_after, bin_overlappedby | bin_during | bin_finishes, bin_after,bin_overlappedby | bin_during | bin_finishes, bin_after, bin_starts | bin_startedby | bin_equals, bin_after, bin_during | bin_finishes | bin_overlappedby, bin_after, bin_metby, bin_metby, bin_metby},
//			{"< o m di fi",">","oi d f",">","oi d f",">","s si e",">","d f oiX",">","mi","mi","mi"},
			// ninth row starts 
			{bin_before, bin_after, bin_during, bin_before | bin_overlaps | bin_meets | bin_contains | bin_finishedby,bin_before | bin_overlaps | bin_meets, bin_overlappedby | bin_during | bin_finishes, bin_before, bin_metby, bin_starts, bin_starts | bin_startedby | bin_equals, bin_during, bin_before | bin_meets | bin_overlaps, bin_starts},
//			{"<",">","d","< o m di fi","< o m","oi d f","<","mi","s","s si e","d","< m o","s"},
			// tenth row startedby 
			{bin_before | bin_overlaps | bin_meets | bin_contains | bin_finishedby, bin_after, bin_overlappedby | bin_during | bin_finishes, bin_contains, bin_overlaps | bin_contains | bin_finishedby, bin_overlappedby, bin_overlaps | bin_contains | bin_finishedby, bin_metby, bin_starts | bin_startedby | bin_equals, bin_startedby, bin_overlappedby, bin_contains, bin_startedby},
//			{"< o m di fi",">","oi d f","di","o di fi","oi","o di fi","mi","s si eX","si","oi","di","si"},
			// eleventh row finishes   
			{bin_before, bin_after, bin_during, bin_after | bin_overlappedby | bin_metby | bin_contains | bin_startedby,bin_overlaps | bin_during | bin_starts, bin_after | bin_overlappedby | bin_metby , bin_meets, bin_after, bin_during, bin_after | bin_overlappedby | bin_metby, bin_finishes, bin_finishes | bin_finishedby | bin_equals, bin_finishes},
//			{"<",">","d","> oi mi di si","o d s","> oi mi di","m",">","d","> oi mX","f","f fi e","f"},
			// twelfth row finishedby 
			{bin_before, bin_after | bin_overlappedby | bin_metby | bin_contains | bin_startedby, bin_overlaps | bin_during | bin_starts, bin_contains, bin_overlaps, bin_overlappedby | bin_contains | bin_startedby, bin_meets, bin_startedby | bin_overlappedby | bin_contains, bin_overlaps, bin_contains, bin_finishes | bin_finishedby | bin_equals, bin_finishedby, bin_finishedby},
//			{"<","> oi mi di si","o d s","di","o","oi di si","m","si oi di","o","di","f fi eX","fi","fi"},
			// thirteenth row equals 
			{bin_before,bin_after,bin_during,bin_contains,bin_overlaps,bin_overlappedby,bin_meets,bin_metby,bin_starts,bin_startedby,bin_finishes,bin_finishedby,bin_equals},
//			{"<",">","d","di","o","oi","m","mi","s","si","f","fi","e"}
				
		};
	/**
	 *  Data Properties
	 */
	public static String getWithNS(String name) {
		return TEO_NS + name;
	}

}
