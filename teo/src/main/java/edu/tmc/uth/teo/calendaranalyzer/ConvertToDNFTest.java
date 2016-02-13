package edu.tmc.uth.teo.calendaranalyzer;

import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.visitors.ConvertToDNF;

/**
 * To convert a common logic expression into the Disjunctive Normal Form.
 * 
 * @author Yi Luo
 * 
 */
public class ConvertToDNFTest {
	
	public static void main(String args[]) {
		PLParser parser = new PLParser();
		Sentence symbol = parser.parse("(((A & ((((B)))))) | (C & D)) & E");
		Sentence transformed = ConvertToDNF.convert(symbol);
		System.out.println(transformed.toString());
		//Assert.assertEquals("A", transformed.toString());
	}

}
