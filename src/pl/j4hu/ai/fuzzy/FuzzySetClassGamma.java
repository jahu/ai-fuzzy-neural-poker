package pl.j4hu.ai.fuzzy;

import java.io.Serializable;

public class FuzzySetClassGamma implements FuzzySet, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2815668909215466552L;

	private double a;
	private double b;
	
	private String name;
	
	public FuzzySetClassGamma(String name) {
		this.name = name;
	}
	
	@Override
	public double getValue(double x) {
		if (x <= a) {
			return 0.0;
		}
		else if ((x > a) && (x <= b)) {
			return (x-a) / (b-a);
		} else {
			return 1.0;
		}
	}

	@Override
	public void setParameters(double[] p) throws FuzzyException {
		if (p.length != 2) {
			throw new FuzzyException("Wrong number (" + p.length + ") of parameters. Should be: 2");
		} else {
			a = p[0];
			b = p[1];
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
	
	@Override
	public String toString(){
		return this.name;
	}
	
	@Override
	public int getNoParameters() {
		return 2;
	}


}