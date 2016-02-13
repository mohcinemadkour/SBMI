/**
 * Copyright 2010 J�rn Franke Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. 
 */
package edu.tmc.uth.teo.test;


import java.util.ArrayList;

import org.allen.temporalintervalrelationships.Constraint;
import org.allen.temporalintervalrelationships.ConstraintNetwork;
import org.allen.temporalintervalrelationships.Node;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.tmc.uth.teo.utils.TEOConstants;
import edu.tmc.uth.teo.utils.TemporalRelationUtils;

/**
 * @author J�rn Franke <jornfranke@gmail.com>
 *
 */
public class TestConstraintNetwork {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	/*
	 * TestOWAAPI by Yi: #1
	 * 
	 * A meet B
	 * B startBeforeStart C
	 * C before D
	 * D finish B
	 * 
	 */
//	@TestOWAAPI
//	public void testPathReasoning2() throws Exception {
//		ConstraintNetwork<String> myConstraintNetwork = new ConstraintNetwork<String>();
//		Node<String> nodeA = new Node<String>("A");
//		myConstraintNetwork.addNode(nodeA);
//		Node<String> nodeB = new Node<String>("B");
//		myConstraintNetwork.addNode(nodeB);
//		Node<String> nodeC = new Node<String>("C");
//		myConstraintNetwork.addNode(nodeC);
//		Node<String> nodeD= new Node<String>("D");
//		myConstraintNetwork.addNode(nodeD);
//		
//		Constraint<String> constraintAB = new Constraint<String> (nodeA,nodeB,TEOConstants.bin_finishedby);
//		myConstraintNetwork.addConstraint(constraintAB);
//		
//		Constraint<String> constraintBC = new Constraint<String> (nodeB,nodeC,TEOConstants.bin_SBS);
//		myConstraintNetwork.addConstraint(constraintBC);
//		
//		Constraint<String> constraintAD = new Constraint<String> (nodeA,nodeD,TEOConstants.bin_SES);
//		myConstraintNetwork.addConstraint(constraintAD);
//		
//		System.out.println(myConstraintNetwork.pathConsistency());		
//		
//		ArrayList<Node<String>> nodeList = myConstraintNetwork.getModeledNodes();
//		for (Node<String> node : nodeList) {
//			System.out.print(node.getIdentifier() + "(" + node.getAllenId() + "), ");
//		}
//		System.out.println();
//		
//		ArrayList<ArrayList<Short>> network = myConstraintNetwork.getConstraintNetwork();
//		for (ArrayList<Short> list : network) {
//			for (Short relation : list) {
//				System.out.print(TemporalRelationUtils.getTemporalRelationTypeListFromConstraintShort(relation) + ", ");
//			}
//			System.out.println();
//		}
//	}
	
	@Test
	public void testPathReasoning2() throws Exception {
		ConstraintNetwork<String> myConstraintNetwork = new ConstraintNetwork<String>();
		Node<String> node0= new Node<String>("0");
		myConstraintNetwork.addNode(node0);
		Node<String> node1 = new Node<String>("1");
		myConstraintNetwork.addNode(node1);
		Node<String> node2 = new Node<String>("2");
		myConstraintNetwork.addNode(node2);
		Node<String> node3 = new Node<String>("3");
		myConstraintNetwork.addNode(node3);
		Node<String> node4 = new Node<String>("4");
		myConstraintNetwork.addNode(node4);
//		Node<String> node5 = new Node<String>("5");
//		myConstraintNetwork.addNode(node5);
//		Node<String> node6 = new Node<String>("6");
//		myConstraintNetwork.addNode(node6);
		
		Constraint<String> constraint04 = new Constraint<String> (node0,node4,TEOConstants.bin_equals);
		myConstraintNetwork.addConstraint(constraint04);
		Constraint<String> constraint21 = new Constraint<String> (node2,node1,TEOConstants.bin_before);
		myConstraintNetwork.addConstraint(constraint21);	
		Constraint<String> constraint34 = new Constraint<String> (node3,node4,TEOConstants.bin_before);
		myConstraintNetwork.addConstraint(constraint34);
//		Constraint<String> constraint46 = new Constraint<String> (node4,node6,TEOConstants.bin_before);
//		myConstraintNetwork.addConstraint(constraint46);
		Constraint<String> constraint42 = new Constraint<String> (node4,node2,TEOConstants.bin_before);
		myConstraintNetwork.addConstraint(constraint42);
//		Constraint<String> constraint62 = new Constraint<String> (node6,node2,TEOConstants.bin_before);
//		myConstraintNetwork.addConstraint(constraint62);
//		Constraint<String> constraint51 = new Constraint<String> (node5,node1,TEOConstants.bin_starts);
//		myConstraintNetwork.addConstraint(constraint51);
		
		System.out.println(myConstraintNetwork.pathConsistency());		
		
		ArrayList<Node<String>> nodeList = myConstraintNetwork.getModeledNodes();
		for (Node<String> node : nodeList) {
			System.out.print(node.getIdentifier() + "(" + node.getAllenId() + "), ");
		}
		System.out.println();
		
		ArrayList<ArrayList<Short>> network = myConstraintNetwork.getConstraintNetwork();
		for (ArrayList<Short> list : network) {
			for (Short relation : list) {
				System.out.print(TemporalRelationUtils.getTemporalRelationTypeListFromConstraintShort(relation) + ", ");
			}
			System.out.println();
		}
	}
	
//	/*
//	 * TestOWAAPI Example of Consistent network
//	 * A STARTS B
//	 * A CONTAINS C
//	 *
//	 */
//	
//	@TestOWAAPI
//	public void testPathConsistency1() throws Exception {
//		ConstraintNetwork<String> myConstraintNetwork = new ConstraintNetwork<String>();
//		Node<String> nodeA = new Node<String>("A");
//		myConstraintNetwork.addNode(nodeA);
//		Node<String> nodeB = new Node<String>("B");
//		myConstraintNetwork.addNode(nodeB);
//		Node<String> nodeC = new Node<String>("C");
//		myConstraintNetwork.addNode(nodeC);
//		Constraint<String> constraintAB = new Constraint<String> (nodeA,nodeB,ConstraintNetwork.bin_starts);
//		myConstraintNetwork.addConstraint(constraintAB);
//		Constraint<String> constraintAC = new Constraint<String> (nodeA,nodeC,ConstraintNetwork.bin_contains);
//		myConstraintNetwork.addConstraint(constraintAC);
//		assertEquals(myConstraintNetwork.pathConsistency(),true);	
//	}
//	
//	/*
//	 * TestOWAAPI Example inconsistent network
//	 * A EQUALS B
//	 * B EQUALS C
//	 * C EQUALS D
//	 * A OVERLAPS D
//	 * 
//	 */
//	
//	@TestOWAAPI
//	public void testPathConsistency2() throws Exception {
//		ConstraintNetwork<String> myConstraintNetwork = new ConstraintNetwork<String>();
//		Node<String> nodeA = new Node<String>("A");
//		myConstraintNetwork.addNode(nodeA);
//		Node<String> nodeB = new Node<String>("B");
//		myConstraintNetwork.addNode(nodeB);
//		Node<String> nodeC = new Node<String>("C");
//		myConstraintNetwork.addNode(nodeC);
//		Node<String> nodeD = new Node<String>("D");
//		myConstraintNetwork.addNode(nodeD);
//		Constraint<String> constraintAB = new Constraint<String> (nodeA,nodeB,ConstraintNetwork.bin_equals);
//		myConstraintNetwork.addConstraint(constraintAB);
//		Constraint<String> constraintBC = new Constraint<String> (nodeB,nodeC,ConstraintNetwork.bin_equals);
//		myConstraintNetwork.addConstraint(constraintBC);
//		Constraint<String> constraintCD = new Constraint<String> (nodeC,nodeD,ConstraintNetwork.bin_equals);
//		myConstraintNetwork.addConstraint(constraintCD);
//		assertEquals(myConstraintNetwork.pathConsistency(),true);	
//		Constraint<String> constraintAD = new Constraint<String> (nodeA,nodeD,ConstraintNetwork.bin_overlaps);
//		myConstraintNetwork.addConstraint(constraintAD);
//		System.out.println(myConstraintNetwork.pathConsistency());		
//	}
	
//	/*
//	 * TestOWAAPI by Yi: #1
//	 * 
//	 * A overlappedBy B
//	 * B before C
//	 * C overlap D
//	 * 
//	 */
//	
//	@TestOWAAPI
//	public void testPathReasoning1() throws Exception {
//		ConstraintNetwork<String> myConstraintNetwork = new ConstraintNetwork<String>();
//		Node<String> nodeA = new Node<String>("A");
//		myConstraintNetwork.addNode(nodeA);
//		Node<String> nodeB = new Node<String>("B");
//		myConstraintNetwork.addNode(nodeB);
//		Node<String> nodeC = new Node<String>("C");
//		myConstraintNetwork.addNode(nodeC);
//		Node<String> nodeD = new Node<String>("D");
//		myConstraintNetwork.addNode(nodeD);
//		
//		Constraint<String> constraintAB = new Constraint<String> (nodeA,nodeB,ConstraintNetwork.bin_overlappedby);
//		myConstraintNetwork.addConstraint(constraintAB);
//		Constraint<String> constraintBC = new Constraint<String> (nodeB,nodeC,ConstraintNetwork.bin_before);
//		myConstraintNetwork.addConstraint(constraintBC);
//		Constraint<String> constraintCD = new Constraint<String> (nodeC,nodeD,ConstraintNetwork.bin_overlaps);
//		myConstraintNetwork.addConstraint(constraintCD);
//		
//		System.out.println(myConstraintNetwork.pathConsistency());		
//		
//		ArrayList<ArrayList<Short>> network = myConstraintNetwork.getConstraintNetwork();
//		for (ArrayList<Short> list : network) {
//			for (Short relation : list) {
//				System.out.print(relation + ",");
//			}
//			System.out.println();
//		}
//	}
}
