package edu.tmc.uth.teo.interfaces;


/**
 * This is the Interface of TEOLoder component which loads ontology into main memory.
 * 
 * @author yluo
 *
 */
public interface TEOLoader {

	/**
	 * To load the ontology
	 */
	public boolean load();

	/**
	 * To return the loaded ontology.
	 */
	public Object getOntology();
}
