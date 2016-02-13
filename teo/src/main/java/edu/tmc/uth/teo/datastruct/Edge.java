package edu.tmc.uth.teo.datastruct;

/**
 * A helper class for the DAG class: DirectedAcyclicGraph.java
 * 
 * 
 * @author yluo
 *
 */
public class Edge {
	private int u; // source
	private int v; // target
	
	public Edge(int u, int v) {
		this.u = u;
		this.v = v;
	}
	
	public int getSource() {
		return u;
	}
	
	public int getTarget() {
		return v;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Edge) {
			if (this.getSource() == ((Edge) o).getSource() && this.getTarget() == ((Edge) o).getTarget()) {
				return true;
			}
		}
		return false;
	}
}
