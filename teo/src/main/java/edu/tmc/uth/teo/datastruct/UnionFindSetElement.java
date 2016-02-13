package edu.tmc.uth.teo.datastruct;

/**
 * A helper function for the UnionFindSet.java
 */
public class UnionFindSetElement<T> {
	UnionFindSetElement<T> parent = null;
	T value; // the key/index of the element
	int rank;

	public UnionFindSetElement(T value) {
		this.value = value;
		this.parent = this;
		this.rank = 1;
	}
	
	public UnionFindSetElement<T> getParent() {
		return this.parent;
	}
	
	public T getValue() {
		return this.value;
	}
	
	public boolean equals(Object o) {
		if (o instanceof UnionFindSetElement) {
			if (this.value.equals(((UnionFindSetElement<?>) o).value)) {
				return true;
			}
		}
		return false;
	}
}
