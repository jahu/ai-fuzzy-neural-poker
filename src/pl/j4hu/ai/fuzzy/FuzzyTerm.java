package pl.j4hu.ai.fuzzy;

import java.io.Serializable;

public class FuzzyTerm implements FuzzySet, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2739752608591477624L;
	private double a;
	private String name;
	
	public FuzzyTerm(String name) {
		this.name = name;
	}
	
	@Override
	public double getValue(double x) {
		if (x == a) {
			return 1.0;
		} else {
			return 0.0;
		}
	}

	@Override
	public void setParameters(double[] p) throws FuzzyException {
		if (p.length != 1) {
			throw new FuzzyException("Wrong number (" + p.length + ") of parameters. Should be: 1");
		} else {
			a = p[0];
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	public double getTerm(){
		return a;
	}
	
	@Override
	public String toString(){
		return this.name;
	}
	
	@Override
	public int getNoParameters() {
		return 1;
	}
}
