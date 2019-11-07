package pl.j4hu.ai.fuzzy;

public class DefuzzificatedValue {
	private FuzzyVariable variable = null;
	private Double value = null;
	
	public DefuzzificatedValue(FuzzyVariable variable, Double value) {
		this.variable = variable;
		this.value = value;
	}

	public FuzzyVariable getVariable() {
		return variable;
	}

	public void setVariable(FuzzyVariable variable) {
		this.variable = variable;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	
}
