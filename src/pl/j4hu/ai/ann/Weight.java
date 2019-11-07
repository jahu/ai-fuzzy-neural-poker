package pl.j4hu.ai.ann;

import java.io.Serializable;

public class Weight implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7148944627942134075L;
	private double value;
	
	public Weight(double value) {
		this.value = value;
	}
	
	public Weight(){
		this.value = 1.0;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
}
