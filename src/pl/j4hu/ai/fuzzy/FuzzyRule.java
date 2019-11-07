package pl.j4hu.ai.fuzzy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FuzzyRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7446913537709061096L;

	private List<FuzzyConstraint> constraints = null;
	private List<FuzzyConstraint> conclusions = null;
	
	public FuzzyRule() {
		constraints = new ArrayList<FuzzyConstraint>();
		conclusions = new ArrayList<FuzzyConstraint>();
	}
	
	public void addConstraint(FuzzyConstraint constraint) {
		constraints.add(constraint);
	}
	
	public void addConclusion(FuzzyConstraint conclusion) {
		conclusions.add(conclusion);
	}
	
	public void addConstraint(FuzzyVariable variable, String value) throws FuzzyException {
		constraints.add(new FuzzyConstraint(variable, value));
	}
	
	public void addConclusion(FuzzyVariable variable, String value) throws FuzzyException {
		conclusions.add(new FuzzyConstraint(variable, value));
	}
	
	public List<FuzzyConstraint> getConstraints() {
		return constraints;
	}

	public List<FuzzyConstraint> getConclusions() {
		return conclusions;
	}

	public String toString(){
		String value = "IF ";
		int count = 0;
		for (FuzzyConstraint con: constraints) {
			value += con;
			count++;
			if(count != constraints.size()) {
				value += " AND ";
			}
		}
		value += "\nTHEN ";
		count = 0;
		for (FuzzyConstraint con: conclusions) {
			value += con;
			count++;
			if(count != conclusions.size()) {
				value += " AND ";
			}
		}
		return value;
	}
	
	
	
}
