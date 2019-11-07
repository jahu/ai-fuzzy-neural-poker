package pl.j4hu.ai.ann;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Layer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5320430613790053713L;

	private List<Neuron> neurons;

	private boolean inputLayer;
	
	public boolean isInputLayer() {
		return inputLayer;
	}

	public Layer(){
		neurons = new ArrayList<Neuron>();
	}
	
	public Layer(int noNeurons, ActivationFunction activationFunction) {
		neurons = new ArrayList<Neuron>();
		for (int i=0; i<noNeurons; i++) {
			addNeuron(new Neuron(activationFunction));
		}
	}
	
	public void addNeuron(Neuron neuron){
		neurons.add(neuron);
	}
	
	public List<Neuron> getNeurons() {
		return neurons;
	}
	
	public void connectLayer(Layer layer){
		for(Neuron neuron: neurons) {
			neuron.addInput(layer);
		}
	}
	
	public void generateWeights(Random rand) {
		for(Neuron neuron: neurons) {
			neuron.genearateWeights(rand);
		}
	}
	
	public void countOutput(){
		for(Neuron neuron: neurons){
			neuron.countOutput();
		}
	}
	
	public void resetErrors(){
		for(Neuron neuron: neurons){
			neuron.resetError();
		}
	}
	
	public void countErrors(List<Double> output) throws Exception{
		if (neurons.size() != output.size()) {
			throw new Exception("Wrong number of elements. There are "
					+ neurons.size() + " neurons; " + output.size()
					+ " values was provided");
		} else {
			for(int i=0; i<neurons.size(); i++) {
				neurons.get(i).countError(output.get(i));
			}
		}
	}
	
	public void correctWeights(double learningFactor, double momentum) throws Exception{
		if (learningFactor<0) {
			throw new Exception("Learning factor cannot be <0");
		} else {
			for (Neuron neuron: neurons){
				neuron.correctWeights(learningFactor, momentum);
			}
		}
	}
	
	public void propagateErrorBackwards(){
		for (Neuron neuron: neurons){
			neuron.propagateErrorBackwards();
		}
	}
	
	public void setBiasEnabled(boolean biasEnabled){
		for (Neuron neuron: neurons){
			neuron.setBiasEnabled(biasEnabled);
		}
	}
	
}
