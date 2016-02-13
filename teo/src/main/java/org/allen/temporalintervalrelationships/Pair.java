/**
 * This class is just a simple class for generating a pair of objects (e.g. Activity and ActivityState)
 * 
 * Author:
 * J�rn Franke <joern.franke@sap.com>
 * 
 * Baseline: init
 * 
 * Copyright 2010 J�rn Franke Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. 
 * 
 */
package org.allen.temporalintervalrelationships;

/**
 * @author J�rn Franke <jornfranke@gmail.com>
 * 
 */
public class Pair<P1,P2> {
	private P1 p1;
	private P2 p2;
	
	public Pair(P1 p1, P2 p2) {
		this.p1=p1;
		this.p2=p2;
	}
	
	public P1 getP1() {
		return this.p1;
	}
	
	public P2 getP2() {
		return this.p2;
	}
	
	public String toString() {
		return "(" + getP1() + ", " + getP2() + ")";
	}
}
