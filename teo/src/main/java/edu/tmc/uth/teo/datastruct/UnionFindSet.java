package edu.tmc.uth.teo.datastruct;

import java.util.HashMap;

/**
 * UnionFindSet is also known as Disjoint Set.
 * 
 * It is used to find "equivalent" nodes.
 * 
 * @author yluo
 *
 * @param <T>
 */
public class UnionFindSet<T> {

	HashMap<T, UnionFindSetElement<T>> elements = null;
	
	public UnionFindSet() {
		elements = new HashMap<T, UnionFindSetElement<T>>();
	}
	
	public UnionFindSetElement<T> getElement(T key) {
		return elements.get(key);
	}
	
	public void MakeSet(T value) {
		UnionFindSetElement<T> e = new UnionFindSetElement<T>(value);
		if (!elements.containsKey(value)) elements.put(value, e);
	}
	
	public int size() {
		return elements.size();
	}
	 
	/**
	 * "Path Compression" is used to optimize the find() method.
	 * 
	 * @param e
	 * @return
	 */
	public UnionFindSetElement<T> find(UnionFindSetElement<T> e) {
		UnionFindSetElement<T> root = e;
		while (!root.equals(e.parent)) {
			root = e.parent;
		}
		// path compression
		UnionFindSetElement<T> x = e, y;
		while (!x.equals(root)) {
			y = x.parent;
			x.parent = root;
			x = y;
		}
		return root;
	}
	
	/**
	 * "Union by rank" is used to optimize the union() method.
	 * 
	 * @param x
	 * @param y
	 */
	public void union(UnionFindSetElement<T> x, UnionFindSetElement<T> y) {
		if (x != y) {
			UnionFindSetElement<T> xRoot = find(x);
			UnionFindSetElement<T> yRoot = find(y);
			
			if (xRoot.equals(yRoot)) {
				return;
			} 
			
			// union by rank
			if (xRoot.rank == yRoot.rank) { // same height
				xRoot.parent = yRoot;
				yRoot.rank ++;
			} else if (xRoot.rank < yRoot.rank) {
				xRoot.parent = yRoot;
			} else {
				yRoot.parent = xRoot;
			}
		}
	}
}
