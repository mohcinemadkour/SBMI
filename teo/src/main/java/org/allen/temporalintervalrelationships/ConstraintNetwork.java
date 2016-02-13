/**
 * This class implements allen's path consistency algorithm for reasoning on temporal interval relationships. Find more information in his paper:
 * Allen, J. F. Maintaining Knowledge about Temporal Intervals Communications of the ACM, 1983, 26, 832-843

 * As Allen points out: the path consistency algorithm is not complete. However, this is not so important in practice.
 * A paper analyzing the completeness of the path consistency algorithm can be found here: 
 * Nebel, B. & B�rckert, H.-J. Reasoning about Temporal Relations: A Maximal Tractable Subclass of Allen's Interval Algebra Journal of the ACM, 1995, 42, 43-66
 * A complete version for all temporal relationships (which is significantly slower than the path consistency algorithm) will be part of a future version.
 * 
 * Please note that the paper contains some minor mistakes which have been corrected in this source code (see also comments). 
 *
 * Copyright 2010 J�rn Franke Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. 
 *
 */
package org.allen.temporalintervalrelationships;

import java.util.ArrayList;
import java.util.Iterator;

import edu.tmc.uth.teo.utils.TEOConstants;


/**
 * @author J�rn Franke <jornfranke@gmail.com>
 *
 * Adapted by Yi Luo for Point Relations.
 * BUG fixed by Yi Luo on 7/23/2014.
 * 
 */
public class ConstraintNetwork<E> {
	// store the constraints separately 
	ArrayList<Constraint<E>> modeledConstraints;
	
	// store the nodes separately
	ArrayList<Node<E>> modeledNodes;
	
	// store the constraint network as arraylist (most performant)
	private ArrayList<ArrayList<Short>> constraintnetwork;
	
	
	// representation of the constraints in binary format
	short bin_before = TEOConstants.bin_before;
	short bin_after = TEOConstants.bin_after;
	short bin_during = TEOConstants.bin_during;
	short bin_contains = TEOConstants.bin_contains;
	short bin_overlaps = TEOConstants.bin_overlaps;
	short bin_overlappedby = TEOConstants.bin_overlappedby; 
	short bin_meets = TEOConstants.bin_meets;
	short bin_metby = TEOConstants.bin_metby; 
	short bin_starts = TEOConstants.bin_starts;
	short bin_startedby = TEOConstants.bin_startedby;
	short bin_finishes = TEOConstants.bin_finishes;
	short bin_finishedby = TEOConstants.bin_finishedby;
	short bin_equals = TEOConstants.bin_equals;
	short bin_all = TEOConstants.bin_full;
	
	// Yi: point relations
	short bin_SBS = TEOConstants.bin_SBS;
	short bin_SAS = TEOConstants.bin_SAS;
	short bin_SES = TEOConstants.bin_SES;
	short bin_SBE = TEOConstants.bin_SBE;
	short bin_SAE = TEOConstants.bin_SAE;
	short bin_SEE = TEOConstants.bin_SEE;
	short bin_EBE = TEOConstants.bin_EBE;
	short bin_EAE = TEOConstants.bin_EAE;
	short bin_EEE = TEOConstants.bin_EEE;
	short bin_EBS = TEOConstants.bin_EBS;
	short bin_EAS = TEOConstants.bin_EAS;
	short bin_EES = TEOConstants.bin_EES;
	
	short[][] transitivematrixshort = TEOConstants.transitivematrixshort;
	
	/*
	 * Constructor creates an empty constraint network
	 * 
	 * 
	 */
	
	public ConstraintNetwork() {
		this.modeledNodes=new ArrayList<Node<E>>();
		this.modeledConstraints=new ArrayList<Constraint<E>>();
		this.constraintnetwork=new ArrayList<ArrayList<Short>>();
	}
	
	/*
	 * Adds a node to the list of modeled nodes constraint network
	 * 
	 * @param nodeAdd Node to be added to the list of modeled nodes and the constraint network
	 * 
	 * @return true if node has been successfully added, false, if not
	 * 
	 */
	
	public boolean addNode(Node<E> nodeAdd)  {
		// add to the list of modeled nodes
		if (!this.modeledNodes.contains(nodeAdd)) {
			this.modeledNodes.add(nodeAdd);
		} else {
			return false;
		}
		this.addNodeToConstraintNetwork(nodeAdd);
		return true;
	}
	
	
	/*
	 * Adds a node to the constraint network. By default all interval relationships are possible from this node to the others and the other
	 * way around. This has to be verified/corrected by the path consistency algorithm (@see #pathConsistency)
	 * 
	 * @param nodeAdd Node to be added to the constraint network
	 * 
	 */
	
	private void addNodeToConstraintNetwork(Node<E> nodeAdd) {
		// add to constraint network
		// create default relationship to all the other nodes
		Iterator<ArrayList<Short>> constraintnetworkIterator = this.constraintnetwork.iterator();
		while (constraintnetworkIterator.hasNext()==true) {
			ArrayList<Short> currentALShort = constraintnetworkIterator.next();
			currentALShort.add(bin_all); // default there can be any relationship between the old nodes and the newly added node
		}
		// add node to constraint network
		this.constraintnetwork.add(new ArrayList<Short>());
		nodeAdd.setAllenId(this.constraintnetwork.size()-1);
		// copy from previous (if exists)
		if (this.constraintnetwork.size()>1) {
			ArrayList<Short> previousALShort = this.constraintnetwork.get(0); // copy reference
			for (int i=0;i<previousALShort.size();i++)
				// add for all previous a shadow constraint
				this.constraintnetwork.get(this.constraintnetwork.size()-1).add(bin_all); // we do not know the references yet
			
		} else  this.constraintnetwork.get(this.constraintnetwork.size()-1).add(bin_equals); // to oneself it is always equals
		// add the previous...
		this.constraintnetwork.get(this.constraintnetwork.size()-1).set(this.constraintnetwork.size()-1, bin_equals); // to oneself is always equals

	}
	
	/*
	 * Removes a node from the list of modeled nodes and the constraint network
	 * 
	 * @param reovedNode Node to be removed from the list of modeled nodes and the constraint network
	 * 
	 * @return true if the Node has been successfully removed, false if not
	 * 
	 */
	
	public boolean removeNode(Node<E> removedNode) {
		if (this.modeledNodes.contains(removedNode)) {
				this.modeledNodes.remove(removedNode);
		} else {
			return false;
		}
		// remove all the constraint defined to that Node
		Iterator<Constraint<E>> modeledConstraintIterator = this.modeledConstraints.iterator();
		while (modeledConstraintIterator.hasNext()==true) {
			Constraint<E> currentConstraint = modeledConstraintIterator.next();
			if ((currentConstraint.getSourceNode().equals(removedNode)) || (currentConstraint.getDestinationNode().equals(removedNode))) {
				modeledConstraintIterator.remove();
			}
		}
		this.rebuild();
		return true;
	}
	
	/*
	 * This method adds a constraint to the list of modeled constraints and the constraint network
	 * 
	 *  @param constraintAdd Constraint to be added to the list of modeled constraints and the constraint network
	 * 
	 *  @return true if constraint has been added, false, if not
	 * 
	 */
	
	public boolean addConstraint(Constraint<E> constraintAdd) {
		// add to the list of modeled constraints
		if (!this.modeledConstraints.contains(constraintAdd)) {
			// check if it contains the same nodes
			Iterator<Constraint<E>> modeledConstraintsIterator = this.modeledConstraints.iterator();
			while (modeledConstraintsIterator.hasNext()) {
				Constraint<E> currentConstraint = modeledConstraintsIterator.next();
				if (currentConstraint.getSourceNode().equals(constraintAdd.getSourceNode()) & (currentConstraint.getDestinationNode().equals(constraintAdd.getDestinationNode()))) {
					return false;
				}
				if (currentConstraint.getSourceNode().equals(constraintAdd.getDestinationNode()) & (currentConstraint.getDestinationNode().equals(constraintAdd.getSourceNode()))) {
					return false;
				}
			}
			// if the nodes defined in the constraint are not part of the network then do not add the constraint
			if (!this.modeledNodes.contains(constraintAdd.getSourceNode()) || (!this.modeledNodes.contains(constraintAdd.getDestinationNode()))) {
				return false;
			}
			this.modeledConstraints.add(constraintAdd);
		} else {
			return false;
		}
		
		// this.addConstraintToConstraintNetwork(constraintAdd);
		
		return true;
	}
	
	/*
	 * Adds a constraint to the constraint network
	 * 
	 * @param constraintAdd constraint to be added to the constraint network
	 * 
	 */
	
	private void addConstraintToConstraintNetwork(Constraint<E> constraintAdd) {
		// add in constraint network
		
		int i = constraintAdd.getSourceNode().getAllenId();
		int j = constraintAdd.getDestinationNode().getAllenId();

		// set constraint
		this.constraintnetwork.get(i).set(j, constraintAdd.getConstraints());
		// set inverse of constraint
		this.constraintnetwork.get(j).set(i, this.inverseConstraintsShort(constraintAdd.getConstraints()));

	}
	
	
	/*
	 * Remove a constraint from the network. Please note that this function can be costly, because it requires a full
	 * execution of the path consistency algorithm. It rebuilds the constraint network afterwards (to gain correct results
	 * when using the path consistency method)
	 * 
	 * @constraintRemove the constraint to be removed 
	 * 
	 * @return true, constraint has been successfully removed, false if not
	 * 
	 */
	
	public boolean removeConstraint(Constraint<E> constraintRemove) {
		if (this.modeledConstraints.contains((constraintRemove))) {
			this.modeledConstraints.remove(constraintRemove);
		} else {
			return false;
		}
		this.rebuild();
		return true;
	}
	
	
	/*
	 * Rebuilds the constraint network (only for the case that constraints have been removed)
	 * 
	 */
	
	private void rebuild() {
		constraintnetwork.clear();
		// add all nodes
		Iterator<Node<E>> modeledNodesIterator = this.modeledNodes.iterator();
		while (modeledNodesIterator.hasNext()) {
			Node<E> currentNode = modeledNodesIterator.next();
			this.addNodeToConstraintNetwork(currentNode);
		}
		// add all constraints
		Iterator<Constraint<E>> modeledConstraintsIterator = this.modeledConstraints.iterator();
		while (modeledConstraintsIterator.hasNext()) {
			Constraint<E> currentConstraint = modeledConstraintsIterator.next();
			this.addConstraintToConstraintNetwork(currentConstraint);
		}
		
	}
	
	/*
	 * Implements allen's path consistency algorithm. Please note that the algorithm may not be able to detect all inconsistent networks
	 * (see references above). Please note that another output of this algorithm is an updated constraint network (@see #getConstraintNetwork), with
	 * all possible constraints between nodes given the defined constraints
	 * 
	 * @return true, network is consistent and false, network is inconsistent
	 * 
	 * 
	 * O(n^2)
	 */
	
	public boolean pathConsistency() {
		if (this.modeledConstraints.size()==0) return true; // no constraint => nothing todo		
		ArrayList<Pair<Integer,Integer>> batchStack = new ArrayList<Pair<Integer,Integer>>(); 
		
		// Yi: to add constraint one by one
		// This is a BUG in the original package, we have to add each constraint one by one. So that all previous graphs are complete connected graphs.
		
		for (Constraint<E> startConstraint : modeledConstraints) {
			// Add constraint to batchStack
			batchStack.add(new Pair<Integer,Integer>(startConstraint.getSourceNode().getAllenId(),startConstraint.getDestinationNode().getAllenId()));

			while (batchStack.size()>0) {
				Pair<Integer,Integer> currentEdge = batchStack.get(0); // Yi: this is used as a queue
				batchStack.remove(0);
				
				int i = currentEdge.getP1().intValue();
				int j = currentEdge.getP2().intValue();
				
				this.addConstraintToConstraintNetwork(startConstraint); // N(i, j) <- R(i, j)
				
				// Browse through all nodes
				for (int k=0;k<this.constraintnetwork.size();k++) {
					// Preliminaries get the constraints 
					short ckj = this.constraintnetwork.get(k).get(j);
					short cki = this.constraintnetwork.get(k).get(i);
					short cij = this.constraintnetwork.get(i).get(j);
					/////////////////////////////////////////First Part
					// get the constraints for k -> j
					// lookup in the transivity matrix (k,i) and (i,j)
					short ckiij = collectConstraintsShort(cki, cij);
					// the following line intersects the set of contraints in ckj and ckiij
					ckj = (short) (ckj & ckiij);
					// if no valid constraint is possible this means the network is inconsistent 
	    			if (ckj==0) {
						return false;
					}
					// Please note a change of allens original algorithm here:
						// there seems to be a mistake in Allen's paper with respect to the algorithm
						// Original: cki subset of getConstraints(eki)
						// Proposed Modification: ckj subset of getConstraints(ekj)
						// Rationale: If the constraints have changed then we need to revisit this dependency again
						// note: bit operation detrect if it is a real subset or not
						//if (subsetConstraintsShort(ckj,this.staticmodelshort.get(k).get(j)==true) {
					short ckjtemp = this.constraintnetwork.get(k).get(j);
					if ((ckj!=ckjtemp && ((short)ckjtemp&ckj)==ckj)) {
							Pair<Integer,Integer> updatePair =new Pair<Integer,Integer>(k,j); 
							batchStack.add(updatePair);
					
							// update constraint network
							this.constraintnetwork.get(k).set(j, ckj);			
							// we also update directly the inverse constraint between the nodes: ejk
							short iCon = this.inverseConstraintsShort(ckj);
							this.constraintnetwork.get(j).set(k, iCon);						
						}
				}
				
				// Yi: adapted according to the original paper (2 separate for loops...)
				for (int k=0;k<this.constraintnetwork.size();k++) {
					// Preliminaries get the constraints 
					short cik = this.constraintnetwork.get(i).get(k);
					short cjk = this.constraintnetwork.get(j).get(k);
					short cij = this.constraintnetwork.get(i).get(j);
					////////////////////////////////////////Second part
					// get the constraints for i -> k
					short cijjk = collectConstraintsShort(cij,cjk);
					// the following line is equivalent to an intersection of the set of constraints defined in cik and cijjk
					cik = (short) (cik & cijjk);
					// if no valid constraint is possible this means the network is inconsistent
					if (cik==0) {
						return false;
					}
						// there seems to be a mistake in Allen's paper with respect to the algorithm
						// Original: cik subset of getConstraints(eki)
						// Proposed Modification: cik subset of getConstraints(eik)
						// Rationale: If the constraints have changed then we need to revisit them again
						
					short ciktemp = this.constraintnetwork.get(i).get(k);
					if ((cik!=ciktemp && ((short)ciktemp&cik)==cik)) {
							Pair<Integer,Integer> updatePair =new Pair<Integer,Integer>(i,k); 
							batchStack.add(updatePair);	
							// update constraint network
							this.constraintnetwork.get(i).set(k, cik);
							// And also the inverse
							Short iCon = this.inverseConstraintsShort(cik);
							this.constraintnetwork.get(k).set(i, iCon);
					}				
				
				}
			}
		}
		return true;

	}
	
	/*
	 * This is one of the most costly functions in Allen's path consistency algorithm. For each constraint (between A and B) given the first parameter it looks 
	 * up for each constraint (between B and C) given in the second parameter in the transivity table the relationship between A and C.
	 * 
	 * @param c1 constraints between A and B
	 * @param c2 constraints between B and C
	 * 
	 * @return constraints between A and C
	 * 
	 * 
	 */
	
	public Short collectConstraintsShort(Short c1, Short c2) {
		short result=0;
		// for each entry (max 13)
		for (int i=0;i<14;i++) {
			// determine for c1 the position
			short c1select = (short)(1<<i); //new Double(Math.pow(2, i)).shortValue();
			// Check if c1 has a constraint
			if ((short) (c1 & c1select)==c1select) { // there is a constraint at this position
			for (int j=0;j<14;j++) {
				// determine for c2 the position
				short c2select = (short)(1<<j);//new Double(Math.pow(2, j)).shortValue();
				// check if c2 has a constraint
				if ((short) (c2 & c2select)==c2select) { // c2 has a constraint at this position
					// look up in transitivitymatrix
					short constraints = transitivematrixshort[i][j];
					// the following line means a union of the constraint set in result and constraints
					result = (short) (result |constraints);
					if (((short)result&bin_all)==bin_all) {
						return result; // all constraints are already in there we do not need further unions
					}
				}
			}
			}
		}
		return result;
	}
	
	/*
	 * This method inverts the constraints given in the parameter (e.g. the constraint "overlaps" becomes the constraint "overlapped by" and vice versa)
	 * 
	 *  @param c constraints (represented as  a short) to invert
	 * 
	 * @return inverted constraints
	 * 
	 */
	
	public Short inverseConstraintsShort(Short c){
		// Probably there is one clever bit operation which can do this in one line...
		short result=0;
		// test before
		if ((short)(c & bin_before)==bin_before) result = (short) (result | bin_after);
		// test after
		if ((short)(c & bin_after)==bin_after) result = (short) (result | bin_before);
		// test during
		if ((short)(c & bin_during)==bin_during) result = (short) (result | bin_contains);
		// test contains
		if ((short)(c & bin_contains)==bin_contains) result = (short) (result | bin_during);
		// test overlaps
		if ((short)(c & bin_overlaps)==bin_overlaps) result = (short) (result | bin_overlappedby);
		// test overlappedby
		if ((short)(c & bin_overlappedby)==bin_overlappedby) result = (short) (result | bin_overlaps);
		// test meets
		if ((short)(c & bin_meets)==bin_meets) result = (short) (result | bin_metby);
		// test metby
		if ((short)(c & bin_metby)==bin_metby) result = (short) (result | bin_meets);
		// test starts
		if ((short)(c & bin_starts)==bin_starts) result = (short) (result | bin_startedby);
		// test startedby
		if ((short)(c & bin_startedby)==bin_startedby) result = (short) (result | bin_starts);
		// test finishes
		if ((short)(c & bin_finishes)==bin_finishes) result = (short) (result | bin_finishedby);
		// test finished by
		if ((short)(c & bin_finishedby)==bin_finishedby) result = (short) (result | bin_finishes);
		// test equals 
		if ((short)(c & bin_equals)==bin_equals) result = (short) (result | bin_equals);
	
		return new Short(result);
	}
	
	/*
	 * This method returns the current constraint network (useful after it has been processed by @see #pathConsistency)
	 * 
	 * @return the current constraint network
	 * 
	 */
	
	public ArrayList<ArrayList<Short>> getConstraintNetwork () {
		return this.constraintnetwork;
	}
	
	/*
	 * This method returns the list of modeled constraints
	 * 
	 * @return list of modeled constraints
	 * 
	 */
	public ArrayList<Constraint<E>> getModeledConstraints() {
		return this.modeledConstraints;
	}
	
	/*
	 * This method returns the lsit of modeled nodes
	 * 
	 *  @return list of modeled nodes
	 * 
	 */
	
	public ArrayList<Node<E>> getModeledNodes() {
		return this.modeledNodes;
	}
	
}
