package edu.tmc.uth.teo.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import edu.tmc.uth.teo.interfaces.TEOLoader;

/**
 * The implementation of the TEOLoader with OWL API. 
 * 
 * @author yluo
 *
 */
public class TEOOWLAPILoader implements TEOLoader {
	private Object ontology = null;
	private IRI iri = null;
	
	/**
	 * Constructor
	 */
	public TEOOWLAPILoader(IRI iri) {
		this.iri = iri;
	}
	
	/**
	 * Constructor
	 * @param localName
	 */
	public TEOOWLAPILoader(String localName) {
		File file = new File(localName);
		try {
			iri = IRI.create(file.toURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	 
	/**
	 * To load the ontolgoy with OWL API.
	 */
	public boolean load() {
		try {
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			ontology = manager.loadOntology(this.iri);
			if (ontology != null) {
				return true;
			}
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public Object getOntology() {
		return this.ontology;
	}
}
