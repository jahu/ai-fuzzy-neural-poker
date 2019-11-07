package pl.j4hu.ai.ann;

import java.util.ArrayList;
import java.util.List;

public class OutputLayer extends Layer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3773442729091072066L;
	private List<Neuron> neurons;
	
	public OutputLayer(int noNeurons, ActivationFunction activationFunction) {
		super(noNeurons, activationFunction);
		neurons = super.getNeurons();
	}
	
	public List<Double> getOutput(){
		List<Double> output = new ArrayList<Double>();
		for (Neuron neuron: neurons){
			output.add(neuron.getOutput());
		}
		return output;
	}

}
