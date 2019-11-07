package pl.j4hu.ai.ann;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Neuron implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3547720722250191190L;

	private ActivationFunction activationFunction;
	
	private double output;
	
	private List<Connection> input;
	private boolean firstLayer;

	private double bias;
	private double lastBias;
	private boolean biasEnabled;

	
	private double error;
	
	public Neuron(ActivationFunction activationFunction){
		this.activationFunction = activationFunction;
		error = 0.0;
		output = 0.0;
		input = new ArrayList<Connection>();
		bias = 0.0;
		lastBias = 0.0;
		this.firstLayer = false;
	}
	
	public Neuron(ActivationFunction activationFunction, boolean isInputLayer){
		this.activationFunction = activationFunction;
		error = 0.0;
		output = 0.0;
		input = new ArrayList<Connection>();
		bias = 0.0;
		this.firstLayer = false;
		firstLayer = isInputLayer;
	}
	
	public void addInput(Neuron neuron){
		input.add(new Connection(neuron));
	}
	
	public void addInput(Neuron neuron, Weight weight){
		input.add(new Connection(neuron, weight));
	}
	
	public void addInput(Layer layer){
		for(Neuron neuron: layer.getNeurons()){
			addInput(neuron);
		}
	}

	public boolean isBiasEnabled() {
		return biasEnabled;
	}

	public void setBiasEnabled(boolean biasEnabled) {
		this.biasEnabled = biasEnabled;
	}
	
	public void genearateWeights(Random rand){
		for (Connection conn: input){
			conn.getWeight().setValue(rand.nextDouble());
		}
		bias = rand.nextDouble();
		lastBias = bias;
	}
	
	public void countOutput(){
		if (firstLayer) {
			return;
		} else {
			output = 0.0;
			for (Connection conn : input) {
				output += conn.getNeuron().getOutput() * conn.getWeight().getValue();
			}
			if (biasEnabled) {
				output += bias;
			}
			output = activationFunction.getValue(output);
		}
	}
	
	public double getOutput(){
		return output;
	}
	
	public void setOutput(double output) {
		this.output = output;
	}
	
	public boolean isFirstLayer() {
		return firstLayer;
	}

	public void setFirstLayer(boolean firstLayer) {
		this.firstLayer = firstLayer;
	}
	
	public void setError(double error) {
		this.error = error;
	}
	
	public void addError(double error) {
		this.error += error;
	}
	
	public void countError(double correctOutput){
		//error = correctOutput - output;
		error = (correctOutput - output) * activationFunction.getDerivative(output);
	}
	
	public void resetError(){
		error = 0.0;
	}
	
	public void correctWeights(double learningFactor, double momentum){
		for (Connection conn: input) {
			double tmp = conn.getWeight().getValue();
			conn.getWeight().setValue( conn.getWeight().getValue() + 
					learningFactor * error * conn.getNeuron().getOutput() + 
					momentum * (tmp - conn.getLastWeight().getValue())); 
			conn.getLastWeight().setValue(tmp);
		}
		if (isBiasEnabled()) {
			double tmp = bias;
			bias +=  learningFactor * error * output + momentum * (bias - lastBias);
			lastBias = tmp;
		}
	}
	
	public void propagateErrorBackwards(){
		for(Connection conn: input){
			conn.getNeuron().addError(activationFunction.getDerivative(conn.getNeuron().getOutput()) * error * conn.getWeight().getValue());
		}
	}
}
