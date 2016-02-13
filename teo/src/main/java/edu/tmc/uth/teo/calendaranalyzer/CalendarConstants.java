package edu.tmc.uth.teo.calendaranalyzer;

/**
 * This class defines all constants used in the calendar elements analysis. 
 * 
 * @author yluo
 *
 */
public class CalendarConstants {
	/**
	 * Name Spaces
	 */
	public static final String TEO_NS = "http://informatics.mayo.edu/TEO.owl#";
	/**
	 *  Data Properties
	 */
	public static String getWithNS(String name) {
		return TEO_NS + name;
	}
	
	/*----------------------------------------------------Calendar analysis-----------------------------------------------*/	

	/**
	 * Classes
	 */
	public static final String DAY_CLS_NAME = "TEO_0000095"; // Day
	public static final String WEEK_CLS_NAME = "TEO_0000030"; // Week
	public static final String MONTH_CLS_NAME = "TEO_0000050"; // Month
	public static final String YEAR_CLS_NAME = "TEO_0000051"; // Year
	public static final String MONTHDAY_CLS_NAME = "TEO_0000091"; // MonthDay
	public static final String WEEKDAY_CLS_NAME = "TEO_0000084"; // WeekDay
	
//	public static final String HOLIDAY_CLS_NAME = "TEO_0000011"; // Holiday
	public static final String MONTH_JAN_CLS_NAME = "TEO_0000052";
	public static final String MONTH_FEB_CLS_NAME = "TEO_0000053";
	public static final String MONTH_MAR_CLS_NAME = "TEO_0000054";
	public static final String MONTH_APR_CLS_NAME = "TEO_0000055";
	public static final String MONTH_MAY_CLS_NAME = "TEO_0000056";
	public static final String MONTH_JUN_CLS_NAME = "TEO_0000057";
	public static final String MONTH_JUL_CLS_NAME = "TEO_0000058";
	public static final String MONTH_AUG_CLS_NAME = "TEO_0000059";
	public static final String MONTH_SEP_CLS_NAME = "TEO_0000060";
	public static final String MONTH_OCT_CLS_NAME = "TEO_0000061";
	public static final String MONTH_NOV_CLS_NAME = "TEO_0000062";
	public static final String MONTH_DEC_CLS_NAME = "TEO_0000063";
	public static final String WEEK_1_CLS_NAME = "TEO_0000019";
	public static final String WEEK_2_CLS_NAME = "TEO_0000031";
	public static final String WEEK_3_CLS_NAME = "TEO_0000035";
	public static final String WEEK_4_CLS_NAME = "TEO_0000088";
	public static final String WEEK_5_CLS_NAME = "TEO_0000089";
	public static final String WEEK_LAST_CLS_NAME = "TEO_0000127";
	public static final String MONTHDAY_1_CLS_NAME = "TEO_0000098";
	public static final String MONTHDAY_2_CLS_NAME = "TEO_0000099";
	public static final String MONTHDAY_3_CLS_NAME = "TEO_0000100";
	public static final String MONTHDAY_4_CLS_NAME = "TEO_0000101";
	public static final String MONTHDAY_5_CLS_NAME = "TEO_0000102";
	public static final String MONTHDAY_6_CLS_NAME = "TEO_0000103";
	public static final String MONTHDAY_7_CLS_NAME = "TEO_0000104";
	public static final String MONTHDAY_8_CLS_NAME = "TEO_0000105";
	public static final String MONTHDAY_9_CLS_NAME = "TEO_0000106";
	public static final String MONTHDAY_10_CLS_NAME = "TEO_0000107";
	public static final String MONTHDAY_11_CLS_NAME = "TEO_0000108";
	public static final String MONTHDAY_12_CLS_NAME = "TEO_0000109";
	public static final String MONTHDAY_13_CLS_NAME = "TEO_0000110";
	public static final String MONTHDAY_14_CLS_NAME = "TEO_0000111";
	public static final String MONTHDAY_15_CLS_NAME = "TEO_0000112";
	public static final String MONTHDAY_16_CLS_NAME = "TEO_0000113";
	public static final String MONTHDAY_17_CLS_NAME = "TEO_0000114";
	public static final String MONTHDAY_18_CLS_NAME = "TEO_0000115";
	public static final String MONTHDAY_19_CLS_NAME = "TEO_0000116";
	public static final String MONTHDAY_20_CLS_NAME = "TEO_0000117";
	public static final String MONTHDAY_21_CLS_NAME = "TEO_0000118";
	public static final String MONTHDAY_22_CLS_NAME = "TEO_0000119";
	public static final String MONTHDAY_23_CLS_NAME = "TEO_0000120";
	public static final String MONTHDAY_24_CLS_NAME = "TEO_0000121";
	public static final String MONTHDAY_25_CLS_NAME = "TEO_0000122";
	public static final String MONTHDAY_26_CLS_NAME = "TEO_0000123";
	public static final String MONTHDAY_27_CLS_NAME = "TEO_0000124";
	public static final String MONTHDAY_28_CLS_NAME = "TEO_0000125";
	public static final String MONTHDAY_29_CLS_NAME = "TEO_0000126";
	public static final String MONTHDAY_30_CLS_NAME = "TEO_0000127";
	public static final String MONTHDAY_31_CLS_NAME = "TEO_0000128";
	public static final String WEEKDAY_1_CLS_NAME = "TEO_0000049"; // sunday
	public static final String WEEKDAY_2_CLS_NAME = "TEO_0000042"; // monday
	public static final String WEEKDAY_3_CLS_NAME = "TEO_0000043"; // tuesday
	public static final String WEEKDAY_4_CLS_NAME = "TEO_0000045"; // wednesday
	public static final String WEEKDAY_5_CLS_NAME = "TEO_0000046"; // thursday
	public static final String WEEKDAY_6_CLS_NAME = "TEO_0000047"; // friday
	public static final String WEEKDAY_7_CLS_NAME = "TEO_0000048"; // saturday
	
	public static final String DAY_CLS = getWithNS(DAY_CLS_NAME);
	public static final String WEEK_CLS = getWithNS(WEEK_CLS_NAME);
	public static final String MONTH_CLS = getWithNS(MONTH_CLS_NAME);
	public static final String YEAR_CLS = getWithNS(YEAR_CLS_NAME);
	public static final String WEEKDAY_CLS = getWithNS(WEEKDAY_CLS_NAME);
	public static final String MONTHDAY_CLS = getWithNS(MONTHDAY_CLS_NAME);
	
//	public static final String HOLIDAY_CLS = getWithNS(HOLIDAY_CLS_NAME);
	public static final String MONTH_JAN_CLS = getWithNS(MONTH_JAN_CLS_NAME);
	public static final String MONTH_FEB_CLS = getWithNS(MONTH_FEB_CLS_NAME);
	public static final String MONTH_MAR_CLS = getWithNS(MONTH_MAR_CLS_NAME);
	public static final String MONTH_APR_CLS = getWithNS(MONTH_APR_CLS_NAME);
	public static final String MONTH_MAY_CLS = getWithNS(MONTH_MAY_CLS_NAME);
	public static final String MONTH_JUN_CLS = getWithNS(MONTH_JUN_CLS_NAME);
	public static final String MONTH_JUL_CLS = getWithNS(MONTH_JUL_CLS_NAME);
	public static final String MONTH_AUG_CLS = getWithNS(MONTH_AUG_CLS_NAME);
	public static final String MONTH_SEP_CLS = getWithNS(MONTH_SEP_CLS_NAME);
	public static final String MONTH_OCT_CLS = getWithNS(MONTH_OCT_CLS_NAME);
	public static final String MONTH_NOV_CLS = getWithNS(MONTH_NOV_CLS_NAME);
	public static final String MONTH_DEC_CLS = getWithNS(MONTH_DEC_CLS_NAME);
	public static final String WEEK_1_CLS = getWithNS(WEEK_1_CLS_NAME);
	public static final String WEEK_2_CLS = getWithNS(WEEK_2_CLS_NAME);
	public static final String WEEK_3_CLS = getWithNS(WEEK_3_CLS_NAME);
	public static final String WEEK_4_CLS = getWithNS(WEEK_4_CLS_NAME);
	public static final String WEEK_5_CLS = getWithNS(WEEK_5_CLS_NAME);
	public static final String WEEK_LAST_CLS = getWithNS(WEEK_LAST_CLS_NAME);
	public static final String MONTHDAY_1_CLS = getWithNS(MONTHDAY_1_CLS_NAME);
	public static final String MONTHDAY_2_CLS = getWithNS(MONTHDAY_2_CLS_NAME);
	public static final String MONTHDAY_3_CLS = getWithNS(MONTHDAY_3_CLS_NAME);
	public static final String MONTHDAY_4_CLS = getWithNS(MONTHDAY_4_CLS_NAME);
	public static final String MONTHDAY_5_CLS = getWithNS(MONTHDAY_5_CLS_NAME);
	public static final String MONTHDAY_6_CLS = getWithNS(MONTHDAY_6_CLS_NAME);
	public static final String MONTHDAY_7_CLS = getWithNS(MONTHDAY_7_CLS_NAME);
	public static final String MONTHDAY_8_CLS = getWithNS(MONTHDAY_8_CLS_NAME);
	public static final String MONTHDAY_9_CLS = getWithNS(MONTHDAY_9_CLS_NAME);
	public static final String MONTHDAY_10_CLS = getWithNS(MONTHDAY_10_CLS_NAME);
	public static final String MONTHDAY_11_CLS = getWithNS(MONTHDAY_11_CLS_NAME);
	public static final String MONTHDAY_12_CLS = getWithNS(MONTHDAY_12_CLS_NAME);
	public static final String MONTHDAY_13_CLS = getWithNS(MONTHDAY_13_CLS_NAME);
	public static final String MONTHDAY_14_CLS = getWithNS(MONTHDAY_14_CLS_NAME);
	public static final String MONTHDAY_15_CLS = getWithNS(MONTHDAY_15_CLS_NAME);
	public static final String MONTHDAY_16_CLS = getWithNS(MONTHDAY_16_CLS_NAME);
	public static final String MONTHDAY_17_CLS = getWithNS(MONTHDAY_17_CLS_NAME);
	public static final String MONTHDAY_18_CLS = getWithNS(MONTHDAY_18_CLS_NAME);
	public static final String MONTHDAY_19_CLS = getWithNS(MONTHDAY_19_CLS_NAME);
	public static final String MONTHDAY_20_CLS = getWithNS(MONTHDAY_20_CLS_NAME);
	public static final String MONTHDAY_21_CLS = getWithNS(MONTHDAY_21_CLS_NAME);
	public static final String MONTHDAY_22_CLS = getWithNS(MONTHDAY_22_CLS_NAME);
	public static final String MONTHDAY_23_CLS = getWithNS(MONTHDAY_23_CLS_NAME);
	public static final String MONTHDAY_24_CLS = getWithNS(MONTHDAY_24_CLS_NAME);
	public static final String MONTHDAY_25_CLS = getWithNS(MONTHDAY_25_CLS_NAME);
	public static final String MONTHDAY_26_CLS = getWithNS(MONTHDAY_26_CLS_NAME);
	public static final String MONTHDAY_27_CLS = getWithNS(MONTHDAY_27_CLS_NAME);
	public static final String MONTHDAY_28_CLS = getWithNS(MONTHDAY_28_CLS_NAME);
	public static final String MONTHDAY_29_CLS = getWithNS(MONTHDAY_29_CLS_NAME);
	public static final String MONTHDAY_30_CLS = getWithNS(MONTHDAY_30_CLS_NAME);
	public static final String MONTHDAY_31_CLS = getWithNS(MONTHDAY_31_CLS_NAME);
	public static final String WEEKDAY_SUN_CLS = getWithNS(WEEKDAY_1_CLS_NAME);
	public static final String WEEKDAY_MON_CLS = getWithNS(WEEKDAY_2_CLS_NAME);
	public static final String WEEKDAY_TUE_CLS = getWithNS(WEEKDAY_3_CLS_NAME);
	public static final String WEEKDAY_WED_CLS = getWithNS(WEEKDAY_4_CLS_NAME);
	public static final String WEEKDAY_THU_CLS = getWithNS(WEEKDAY_5_CLS_NAME);
	public static final String WEEKDAY_FRI_CLS = getWithNS(WEEKDAY_6_CLS_NAME);
	public static final String WEEKDAY_SAT_CLS = getWithNS(WEEKDAY_7_CLS_NAME);
	/**
	 * Properties
	 */
	// occur
	public static final String OCCUR_PRP_NAME = "TEO_0000085"; // domain: ConnectedTemporalRegion (Yi: + Event?)
	public static final String OCCURYEAR_PRP_NAME = "TEO_0000132";
	public static final String OCCURMONTH_PRP_NAME = "TEO_0000086";
	public static final String OCCURWEEK_PRP_NAME = "TEO_0000090";
	public static final String OCCURDAY_PRP_NAME = "TEO_0000087";
		
	public static final String OCCUR_PRP = getWithNS(OCCUR_PRP_NAME);
	public static final String OCCURYEAR_PRP = getWithNS(OCCURYEAR_PRP_NAME);
	public static final String OCCURMONTH_PRP = getWithNS(OCCURMONTH_PRP_NAME);
	public static final String OCCURWEEK_PRP = getWithNS(OCCURWEEK_PRP_NAME);
	public static final String OCCURDAY_PRP = getWithNS(OCCURDAY_PRP_NAME);
}
