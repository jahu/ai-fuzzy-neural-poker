package pl.j4hu.ai.ann;

import java.io.Serializable;

public class Connection implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6498677976052364184L;
	private Neuron neuron;
	private Weight weight;
	private Weight lastWeight;
	
	public Neuron getNeuron() {
		return neuron;
	}
	public void setNeuron(Neuron neuron) {
		this.neuron = neuron;
	}
	public Weight getWeight() {
		return weight;
	}
	public void setWeight(Weight weight) {
		//this.lastWeight.setValue(this.weight.ge)
		this.weight = weight;
	}
	
	public Connection(Neuron neuron, Weight weight){
		this.neuron = neuron;
		this.weight = weight;
		this.lastWeight = new Weight();
	}
	
	public Connection(Neuron neuron){
		this.neuron = neuron;
		this.weight = new Weight();
		this.lastWeight = new Weight();
	}
	
	public Weight getLastWeight() {
		return lastWeight;
	}
	
}
