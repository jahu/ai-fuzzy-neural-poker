package pl.j4hu.ai.fuzzy;

import java.io.Serializable;
import java.util.List;

public class FuzzyConstraint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 397943660789172640L;

	private FuzzyVariable variable = null;
	private String value = null;
	
	public FuzzyConstraint(FuzzyVariable variable, String value) throws FuzzyException {
		this.variable = variable;
		List<String> possibleValues = variable.getPossibleValues();
		if(possibleValues.contains(value)) {
			this.value = value;
		} else {
			throw new FuzzyException("Variable '" +variable.getName() + "' does not contains value '" + value + "'");
		}
	}

	public FuzzyVariable getVariable() {
		return variable;
	}

	public void setVariable(FuzzyVariable variable) {
		this.variable = variable;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString(){
		return variable.getName() + "=" + value;
	}
	
}
