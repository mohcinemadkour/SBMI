package edu.tmc.uth.teo.datastruct;

import java.util.ArrayList;
import java.util.List;

/**
 * Implemented a DAG for the reasoning of the timeline feature
 * @author Yi
 *
 * @param <V>
 */
public class DirectedAcyclicGraph<V> {
	List<V> vertices = new ArrayList<V>();
	List<List<Edge>> neighbors = new ArrayList<List<Edge>>();
	
	List<Integer> inDegree = new ArrayList<Integer>();
	
	public DirectedAcyclicGraph() {
	}
	
	public DirectedAcyclicGraph(List<Edge> edges, List<V> vertices) {
		for (V v : vertices) {
			addVertex(v);
		}
		for (Edge e : edges) {
			addEdge(e.getSource(), e.getTarget());
		}
	}
	
	public DirectedAcyclicGraph(List<Edge> edges, V[] vertices) {
		for (V v : vertices) {
			addVertex(v);
		}
		for (Edge e : edges) {
			addEdge(e.getSource(), e.getTarget());
		}
	}
	
	public int getSize() {
		return vertices.size();
	}
	
	public List<V> getVertices() {
		return vertices;
	}
	
	public V getVertix(int index) {
		return vertices.get(index);
	}
	
	public int getIndex(V v) {
		return vertices.indexOf(v);
	}
	
	public List<Integer> getNeighbors(int index) {
		List<Integer> result = new ArrayList<Integer>();
		for (Edge e : neighbors.get(index)) {
			result.add(e.getTarget());
		}
		return result;
	}
	
	public int getOutDegree(int index) {
		return neighbors.get(index).size();
	}
	
	public int getInDegree(int index) {
		return inDegree.get(index);
	}

	public void clear() {
		this.neighbors.clear();
		this.vertices.clear();
	}
	
	public boolean addVertex(V vertex) {
		if (!this.vertices.contains(vertex)) {
			this.vertices.add(vertex);
			this.inDegree.add(0);
			this.neighbors.add(new ArrayList<Edge>());
			return true;
		} else {
			return false;
		}
	}
	
	public boolean addEdge(int u, int v) {
		if (u < 0 || u > getSize() - 1) {
			System.out.println("No such index " + u);
		}
		if (v < 0 || v > getSize() - 1) {
			System.out.println("No such index " + v);
		}
		Edge e = new Edge(u, v);
		if (!this.neighbors.get(u).contains(e)) {
			this.neighbors.get(u).add(e);
			// update the inDegree list
			this.inDegree.set(v, this.inDegree.get(v) + 1);
			return true;
		} else {
			return false;
		}
	}
	
	// never remove vertices in a harry, it would change the index 
	
	public boolean removeEdge(int u, int v) {
		if (u < 0 || u > getSize() - 1) {
			System.out.println("No such index " + u);
		}
		if (v < 0 || v > getSize() - 1) {
			System.out.println("No such index " + v);
		}
		Edge e = new Edge(u, v);
		if (this.neighbors.get(u).contains(e)) {
			// remove it from the neighbor list
			this.neighbors.get(u).remove(new Edge(u, v));
			// update the 
			this.inDegree.set(v, this.inDegree.get(v) - 1); 
			return true;
		} else {
			return false;
		}
	}
}



