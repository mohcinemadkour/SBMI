package edu.tmc.uth.teo.utils;

import edu.tmc.uth.teo.model.Event;
import edu.tmc.uth.teo.model.TemporalRelationInShortCode;
import edu.tmc.uth.teo.model.TemporalType;

public class TemporalTypeUtils {

	/**
	 * If the given subEvent is a TimeInstant event, its relations cannot be the subset of "overlap, overlappedBy, startedBy, finishedBy, contain".
	 * If the given objEvent is a TimeInstant event, its relations cannot be the subset of "overlap, overlappedBy, start, finish, during".
	 * 
	 * @param event
	 * @param relation
	 * @return
	 */
	public static boolean checkTemporalTypeConsistency(Event subEvent, TemporalRelationInShortCode relation, Event objEvent) {
		boolean flag = true;
		
		if (subEvent != null && relation != null && objEvent != null) {
			short relationCode = relation.getRelationCode();
			if (subEvent.getEventType().equals(TemporalType.TIMEINSTANT)) {
				short intersection = (short) (relationCode & (TEOConstants.bin_overlaps | TEOConstants.bin_overlappedby | TEOConstants.bin_startedby | TEOConstants.bin_finishedby | TEOConstants.bin_contains));
				if (intersection != 0 && intersection == relationCode) {
					flag = false;
					System.out.println("SubEvent: \"" + subEvent.getIRIStr() + "\" is a TimeInstant event, followed by error relation - [" + relation.printRelations() + "]");
				}
			}
			
			if (objEvent.getEventType().equals(TemporalType.TIMEINSTANT)) {
				short intersection = (short) (relationCode & (TEOConstants.bin_overlaps | TEOConstants.bin_overlappedby | TEOConstants.bin_starts | TEOConstants.bin_finishes | TEOConstants.bin_during));
				if (intersection != 0 && intersection == relationCode) {
					flag = false;
					System.out.println("ObjEvent: \"" + objEvent.getIRIStr() + "\" is a TimeInstant event, follow error relation - [" + relation.printRelations() + "]");
				}
			}
		}
		return flag;
	}
	
	/**
	 * Assign temporal type to events if unknown.
	 * @param subEvent
	 * @param relation
	 * @param objEvent
	 * @return
	 */
	public static boolean assignTemporalType(Event subEvent, TemporalRelationInShortCode relation, Event objEvent) {
		if (subEvent != null && relation != null && objEvent != null) {
			short relationCode = relation.getRelationCode();
			if (subEvent.getEventType().equals(TemporalType.UNKNOWN)) {
				short intersection = (short) (relationCode & (TEOConstants.bin_overlaps | TEOConstants.bin_overlappedby | TEOConstants.bin_startedby | TEOConstants.bin_finishedby | TEOConstants.bin_contains));
				if (intersection != 0 && intersection == relationCode) {
					subEvent.setEventType(TemporalType.TIMEINTERVAL);
				}
			}
			
			if (objEvent.getEventType().equals(TemporalType.UNKNOWN)) {
				short intersection = (short) (relationCode & (TEOConstants.bin_overlaps | TEOConstants.bin_overlappedby | TEOConstants.bin_starts | TEOConstants.bin_finishes | TEOConstants.bin_during));
				if (intersection != 0 && intersection == relationCode) {
					objEvent.setEventType(TemporalType.TIMEINTERVAL);
				}
			}
			
			return true;
		}
		return false;
	}
}
