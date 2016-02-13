package edu.tmc.uth.teo.utils;

/**
 * Utilities for String processing
 * 
 * @author yluo
 *
 */
public class StringUtils {
	public static boolean isNull(String str) {
		if ((str == null)||("null".equalsIgnoreCase(str.trim()))||("".equalsIgnoreCase(str.trim())))
			return true;
		return false;
	}
	
	public static String getStringValueWithinQuotes(String str) {
		String value = str;
		
		if (value.startsWith("\""))
		{
			value = value.substring(1);
			if (!isNull(value))
			{
				if (value.indexOf("\"") != -1)
					value = value.substring(0, value.lastIndexOf("\""));
			}
		}
		return value;
	}
}
