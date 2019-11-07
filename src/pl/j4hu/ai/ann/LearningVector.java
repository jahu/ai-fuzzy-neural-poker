package pl.j4hu.ai.ann;

import java.io.Serializable;
import java.util.List;

public class LearningVector implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2883675927674958593L;
	private List<Double> input;
	private List<Double> output;

	public LearningVector(List<Double> input, List<Double> output) {
		this.input = input;
		this.output = output;
	}
	
	public List<Double> getInput() {
		return input;
	}

	public List<Double> getOutput() {
		return output;
	}
	
	public String toString(){
		return "Input: "+input +"\nOutput: "+output;
	}
	
}
