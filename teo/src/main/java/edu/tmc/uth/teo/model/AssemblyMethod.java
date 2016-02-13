package edu.tmc.uth.teo.model;

/**
 * The enumeration of assembly methods.
 * 
 * @author yluo
 *
 */
public enum AssemblyMethod 
{
	ASSERTED, 	// User specified the time 
	INFERRED, 	// Parser or program computed it or inferred it
	UNKNOWN		// Default
}
