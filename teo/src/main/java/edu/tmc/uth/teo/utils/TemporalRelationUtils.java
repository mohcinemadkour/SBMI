package edu.tmc.uth.teo.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.tmc.uth.teo.datastruct.DirectedAcyclicGraph;
import edu.tmc.uth.teo.model.Event;
import edu.tmc.uth.teo.model.TemporalRelationInShortCode;
import edu.tmc.uth.teo.model.TemporalRelationType;
import edu.tmc.uth.teo.model.TemporalType;
import edu.tmc.uth.teo.model.TimeInstant;
import edu.tmc.uth.teo.model.TimeInterval;

/**
 * We implemented the Kahn Algorithm to find the topology order
 * 
 * @author yluo
 * 
 */
public class TemporalRelationUtils {

	/**
	 * Kahn Algorithm, O(N+E)
	 * 
	 * @param graph
	 * @return
	 */
	public static <T> List<T> topologySort(DirectedAcyclicGraph<T> graph) {
		List<T> vList = new ArrayList<T>(); // result
		Queue<T> queue = new LinkedList<T>(); // temp queue

		List<T> vertices = graph.getVertices();

		for (T v : vertices) {
			if (graph.getInDegree(graph.getIndex(v)) == 0) {
				queue.add(v);
			}
		}

		while (!queue.isEmpty()) {
			T crtV = queue.poll();
			int u = graph.getIndex(crtV);
			vList.add(crtV);
			List<Integer> neighbors = graph.getNeighbors(u);

			for (Integer v : neighbors) {
				graph.removeEdge(u, v);
				if (graph.getInDegree(v) == 0) {
					queue.add(graph.getVertix(v));
				}
			}
		}

		if (vList.size() != graph.getSize()) {
			System.err.println("The DAG contains cycles!");
		}

		return vList;
	}

	/**
	 * The helper function to get TemporalRelationType from the relation string.
	 */
	public static TemporalRelationType getTemporalRelationType(
			String relationStr) {
		// interval relations
		if (relationStr.equals("before"))
			return TemporalRelationType.BEFORE;
		if (relationStr.equals("after"))
			return TemporalRelationType.AFTER;
		if (relationStr.equals("meet"))
			return TemporalRelationType.MEET;
		if (relationStr.equals("metBy"))
			return TemporalRelationType.METBY;
		if (relationStr.equals("during"))
			return TemporalRelationType.DURING;
		if (relationStr.equals("contain"))
			return TemporalRelationType.CONTAIN;
		if (relationStr.equals("overlap"))
			return TemporalRelationType.OVERLAP;
		if (relationStr.equals("overlappedBy"))
			return TemporalRelationType.OVERLAPPEDBY;
		if (relationStr.equals("start"))
			return TemporalRelationType.START;
		if (relationStr.equals("startedBy"))
			return TemporalRelationType.STARTEDBY;
		if (relationStr.equals("finish"))
			return TemporalRelationType.FINISH;
		if (relationStr.equals("finishedBy"))
			return TemporalRelationType.FINISHEDBY;
		if (relationStr.equals("equal"))
			return TemporalRelationType.EQUAL;
		if (relationStr.equals("before"))
			return TemporalRelationType.BEFORE;
		// point relations
		if (relationStr.equals("startBeforeStart"))
			return TemporalRelationType.START_BEFORE_START;
		if (relationStr.equals("startAfterStart"))
			return TemporalRelationType.START_AFTER_START;
		if (relationStr.equals("startEqualStart"))
			return TemporalRelationType.START_EQUAL_START;
		if (relationStr.equals("endBeforeEnd"))
			return TemporalRelationType.END_BEFORE_END;
		if (relationStr.equals("endAfterEnd"))
			return TemporalRelationType.END_AFTER_END;
		if (relationStr.equals("endEqualEnd"))
			return TemporalRelationType.END_EQUAL_END;
		if (relationStr.equals("startBeforeEnd"))
			return TemporalRelationType.START_BEFORE_END;
		if (relationStr.equals("startAfterEnd"))
			return TemporalRelationType.START_AFTER_END;
		if (relationStr.equals("startEqualEnd"))
			return TemporalRelationType.START_EQUAL_END;
		if (relationStr.equals("endBeforeStart"))
			return TemporalRelationType.END_BEFORE_START;
		if (relationStr.equals("endAfterStart"))
			return TemporalRelationType.END_AFTER_START;
		if (relationStr.equals("endEqualStart"))
			return TemporalRelationType.END_EQUAL_START;
		// full
		return TemporalRelationType.FULL;
	}

	/**
	 * The helper function to get TemporalRelationCode from the relation type.
	 */
	public static short getTemporalRelationCode(
			TemporalRelationType relationType) {
		switch (relationType) {
		// interval relations
		case AFTER:
			return TEOConstants.bin_after;
		case BEFORE:
			return TEOConstants.bin_before;
		case MEET:
			return TEOConstants.bin_meets;
		case METBY:
			return TEOConstants.bin_metby;
		case FINISH:
			return TEOConstants.bin_finishes;
		case FINISHEDBY:
			return TEOConstants.bin_finishedby;
		case START:
			return TEOConstants.bin_starts;
		case STARTEDBY:
			return TEOConstants.bin_startedby;
		case OVERLAP:
			return TEOConstants.bin_overlaps;
		case OVERLAPPEDBY:
			return TEOConstants.bin_overlappedby;
		case EQUAL:
			return TEOConstants.bin_equals;
		case CONTAIN:
			return TEOConstants.bin_contains;
		case DURING:
			return TEOConstants.bin_during;
			// point relations
		case START_BEFORE_START:
			return TEOConstants.bin_SBS;
		case START_AFTER_START:
			return TEOConstants.bin_SAS;
		case START_EQUAL_START:
			return TEOConstants.bin_SES;
		case START_BEFORE_END:
			return TEOConstants.bin_SBE;
		case START_AFTER_END:
			return TEOConstants.bin_SAE;
		case START_EQUAL_END:
			return TEOConstants.bin_SEE;
		case END_BEFORE_START:
			return TEOConstants.bin_EBS;
		case END_AFTER_START:
			return TEOConstants.bin_EAS;
		case END_EQUAL_START:
			return TEOConstants.bin_EES;
		case END_BEFORE_END:
			return TEOConstants.bin_EBE;
		case END_AFTER_END:
			return TEOConstants.bin_EAE;
		case END_EQUAL_END:
			return TEOConstants.bin_EEE;
			// full
		default:
			return TEOConstants.bin_full;
		}
	}

	/**
	 * To merge relations between a pair of nodes to minimize the label
	 * 
	 * @param relationList
	 * @return
	 */
	public static short getMergedTemporalRelationCode(
			ArrayList<TemporalRelationInShortCode> relationList) {
		short result = TEOConstants.bin_full;
		if (relationList != null && !relationList.isEmpty()) {
			for (TemporalRelationInShortCode relation : relationList) {
				result &= relation.getRelationCode(); // intersection for
														// "Inferred Relations"
			}
		}
		return result;
	}

	/**
	 * Returns a list of names of the constraints given in the set of
	 * constraints
	 * 
	 * @param set
	 *            of constraints c
	 * @return list of names of the constraints given in c
	 */
	public static ArrayList<TemporalRelationType> getTemporalRelationTypeListFromConstraintShort(
			short c) {
		ArrayList<TemporalRelationType> result = new ArrayList<TemporalRelationType>();
		// if the result matches a higher level point relation, it should only
		// return this higher one
		// test full
		if ((short) (c & TEOConstants.bin_full) == TEOConstants.bin_full) {
			result.add(TemporalRelationType.FULL);
			return result;
		}

		// point relations
		// test SBE
		if ((short) (c & TEOConstants.bin_SBE) == TEOConstants.bin_SBE) {
			result.add(TemporalRelationType.START_BEFORE_END);
			return result;
		}
		// test EAS
		if ((short) (c & TEOConstants.bin_EAS) == TEOConstants.bin_EAS) {
			result.add(TemporalRelationType.END_AFTER_START);
			return result;
		}
		// test SBS
		if ((short) (c & TEOConstants.bin_SBS) == TEOConstants.bin_SBS) {
			result.add(TemporalRelationType.START_BEFORE_START);
			return result;
		}
		// test EBE
		if ((short) (c & TEOConstants.bin_EBE) == TEOConstants.bin_EBE) {
			result.add(TemporalRelationType.END_AFTER_START);
			return result;
		}
		// test SES
		if ((short) (c & TEOConstants.bin_SES) == TEOConstants.bin_SES) {
			result.add(TemporalRelationType.START_EQUAL_START);
			return result;
		}
		// test EEE
		if ((short) (c & TEOConstants.bin_EEE) == TEOConstants.bin_EEE) {
			result.add(TemporalRelationType.END_EQUAL_END);
			return result;
		}
		// test SAS
		if ((short) (c & TEOConstants.bin_SAS) == TEOConstants.bin_SAS) {
			result.add(TemporalRelationType.START_AFTER_START);
			return result;
		}
		// test EAE
		if ((short) (c & TEOConstants.bin_EAE) == TEOConstants.bin_EAE) {
			result.add(TemporalRelationType.END_AFTER_END);
			return result;
		}
		// test EAS
		if ((short) (c & TEOConstants.bin_EAS) == TEOConstants.bin_EAS) {
			result.add(TemporalRelationType.END_AFTER_START);
			return result;
		}

		// test EAS
		if ((short) (c & TEOConstants.bin_EBS) == TEOConstants.bin_EBS) {
			result.add(TemporalRelationType.END_BEFORE_START);
			return result;
		}

		if ((short) (c & TEOConstants.bin_SAE) == TEOConstants.bin_SAE) {
			result.add(TemporalRelationType.START_AFTER_END);
			return result;
		}

		if ((short) (c & TEOConstants.bin_EES) == TEOConstants.bin_EES) {
			result.add(TemporalRelationType.END_EQUAL_START);
			return result;
		}

		if ((short) (c & TEOConstants.bin_SEE) == TEOConstants.bin_SEE) {
			result.add(TemporalRelationType.START_EQUAL_END);
			return result;
		}

		// test before
		if ((short) (c & TEOConstants.bin_before) == TEOConstants.bin_before)
			result.add(TemporalRelationType.BEFORE);
		// test after
		if ((short) (c & TEOConstants.bin_after) == TEOConstants.bin_after)
			result.add(TemporalRelationType.AFTER);
		// test during
		if ((short) (c & TEOConstants.bin_during) == TEOConstants.bin_during)
			result.add(TemporalRelationType.DURING);
		// test contains
		if ((short) (c & TEOConstants.bin_contains) == TEOConstants.bin_contains)
			result.add(TemporalRelationType.CONTAIN);
		// test overlaps
		if ((short) (c & TEOConstants.bin_overlaps) == TEOConstants.bin_overlaps)
			result.add(TemporalRelationType.OVERLAP);
		// test overlappedby
		if ((short) (c & TEOConstants.bin_overlappedby) == TEOConstants.bin_overlappedby)
			result.add(TemporalRelationType.OVERLAPPEDBY);
		// test meets
		if ((short) (c & TEOConstants.bin_meets) == TEOConstants.bin_meets)
			result.add(TemporalRelationType.MEET);
		// test metby
		if ((short) (c & TEOConstants.bin_metby) == TEOConstants.bin_metby)
			result.add(TemporalRelationType.METBY);
		// test starts
		if ((short) (c & TEOConstants.bin_starts) == TEOConstants.bin_starts)
			result.add(TemporalRelationType.START);
		// test startedby
		if ((short) (c & TEOConstants.bin_startedby) == TEOConstants.bin_startedby)
			result.add(TemporalRelationType.STARTEDBY);
		// test finishes
		if ((short) (c & TEOConstants.bin_finishes) == TEOConstants.bin_finishes)
			result.add(TemporalRelationType.FINISH);
		// test finished by
		if ((short) (c & TEOConstants.bin_finishedby) == TEOConstants.bin_finishedby)
			result.add(TemporalRelationType.FINISHEDBY);
		// test equals
		if ((short) (c & TEOConstants.bin_equals) == TEOConstants.bin_equals)
			result.add(TemporalRelationType.EQUAL);
		return result;
	}

	// The returned relation list should be valid if given events are valid.
	public static ArrayList<TemporalRelationType> getTemporalPointRelationsByValidTime(
			Event event1, Event event2) {
		ArrayList<TemporalRelationType> relationList = new ArrayList<TemporalRelationType>();
		TimeInstant start1 = null, end1 = null, start2 = null, end2 = null;
		if (event1.getEventType().equals(TemporalType.TIMEINSTANT)) {
			start1 = (TimeInstant) event1.getValidTime();
			end1 = start1;
		} else if (event1.getEventType().equals(TemporalType.TIMEINTERVAL)) {
			start1 = ((TimeInterval) event1.getValidTime()).getStartTime();
			end1 = ((TimeInterval) event1.getValidTime()).getEndTime();
		}

		if (event2.getEventType().equals(TemporalType.TIMEINSTANT)) {
			start2 = (TimeInstant) event2.getValidTime();
			end2 = start2;
		} else if (event2.getEventType().equals(TemporalType.TIMEINTERVAL)) {
			start2 = ((TimeInterval) event2.getValidTime()).getStartTime();
			end2 = ((TimeInterval) event2.getValidTime()).getEndTime();
		}

		// 1. start1 vs. start2
		if (start1 != null && start2 != null) {
			if (start1.compareTo(start2) < 0) {
				relationList.add(TemporalRelationType.START_BEFORE_START);
			} else if (start1.compareTo(start2) == 0) {
				relationList.add(TemporalRelationType.START_EQUAL_START);
			} else {
				relationList.add(TemporalRelationType.START_AFTER_START);
			}
		}
		// 2. start1 vs. end2
		if (start1 != null && end2 != null) {
			if (start1.compareTo(end2) < 0) {
				relationList.add(TemporalRelationType.START_BEFORE_END);
			} else if (start1.compareTo(end2) == 0) {
				relationList.add(TemporalRelationType.START_EQUAL_END);
			} else {
				relationList.add(TemporalRelationType.START_AFTER_END);
			}
		}
		// 3. end1 vs. start2
		if (end1 != null && start2 != null) {
			if (end1.compareTo(start2) < 0) {
				relationList.add(TemporalRelationType.END_BEFORE_START);
			} else if (end1.compareTo(start2) == 0) {
				relationList.add(TemporalRelationType.END_EQUAL_START);
			} else {
				relationList.add(TemporalRelationType.END_AFTER_START);
			}
		}
		// 4. end1 vs. end2
		if (end1 != null && end2 != null) {
			if (end1.compareTo(end2) < 0) {
				relationList.add(TemporalRelationType.END_BEFORE_END);
			} else if (end1.compareTo(end2) == 0) {
				relationList.add(TemporalRelationType.END_EQUAL_END);
			} else {
				relationList.add(TemporalRelationType.END_AFTER_END);
			}
		}
		return relationList;
	}
}
