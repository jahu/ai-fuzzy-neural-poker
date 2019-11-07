package pl.j4hu.ai.fuzzy;

public class FuzzificatedValue {
	private FuzzySet set = null;
	private Double value = null;
	
	public FuzzificatedValue(FuzzySet set, Double value) {
		this.set = set;
		this.value = value;
	}

	public FuzzySet getSet() {
		return set;
	}

	public void setSet(FuzzySet set) {
		this.set = set;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	
}
