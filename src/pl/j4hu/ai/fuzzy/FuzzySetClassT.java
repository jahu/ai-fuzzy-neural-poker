package pl.j4hu.ai.fuzzy;

import java.io.Serializable;

public class FuzzySetClassT implements FuzzySet, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8253372420816612807L;

	private double a;
	private double b;
	private double c;
	
	private String name;
	
	public FuzzySetClassT(String name) {
		this.name = name;
	}
	
	@Override
	public double getValue(double x) {
		if (x <= a) {
			return 0.0;
		}
		else if ((x > a) && (x <= b)) {
			return (x-a) / (b-a);
		} else if ((x > b) && (x <= c)){
			return (c-x) / (c-b);
		} else {
			return 0.0;
		}
	}

	@Override
	public void setParameters(double[] p) throws FuzzyException {
		if (p.length != 3) {
			throw new FuzzyException("Wrong number (" + p.length + ") of parameters. Should be: 3");
		} else {
			a = p[0];
			b = p[1];
			c = p[2];
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
		return 3;
	}


}