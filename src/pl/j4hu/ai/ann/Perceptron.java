package pl.j4hu.ai.ann;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Perceptron implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8439571951180475020L;
	private List<Layer> layers;
	private boolean randomizeLearningVectors;
	
	private Map<Integer, Double> learningProcess;
	private double learningFactor;
	private double momentum;
	
	public Perceptron(int noLayers, int [] noNeurons, ActivationFunction activationFunction) throws Exception{
		layers = new ArrayList<Layer>();
		learningProcess = new HashMap<Integer, Double>();
		if(noLayers<1) {
			throw new Exception("Perceptron must have at least 2 layers");
		} else if (noLayers != noNeurons.length){
			throw new Exception("Wrong number of layers in noNeurons array");
		} else {
			switch (noLayers) {
			case 2:
				layers.add(new InputLayer(noNeurons[0], activationFunction));
				layers.add(new OutputLayer(noNeurons[1], activationFunction));
				break;
			default:
				layers.add(new InputLayer(noNeurons[0], activationFunction));
				for(int i=1; i<noNeurons.length-1; i++){
					layers.add(new Layer(noNeurons[i], activationFunction));
				}
				layers.add(new OutputLayer(noNeurons[noNeurons.length-1], activationFunction));
				break;
			}
			
			for (int i=layers.size()-1; i>0; i--){
				layers.get(i).connectLayer(layers.get(i-1));
			}
		}	
	}
	
	public Perceptron(String path){
		try {
			FileInputStream fileIn = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Perceptron tmp = (Perceptron) in.readObject();
			this.layers = tmp.layers;
			this.randomizeLearningVectors = tmp.randomizeLearningVectors;
			this.learningProcess = new HashMap<Integer, Double>();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("Perceptron class not found in given file");
			c.printStackTrace();
			return;
		}
	}
	
	public void generateWeights(){
		Random rand = new Random();
		for(Layer layer: layers){
			if(!(layer instanceof InputLayer)){
				layer.generateWeights(rand);
			}
		}
	}
	
	
	public List<Double> getOutput(List<Double> input) throws Exception{
		if(input.size() != layers.get(0).getNeurons().size()){
			throw new Exception ("Wrong number of values in input. Input layer has "+layers.get(0).getNeurons().size()+" neurons.");
		}
		((InputLayer) layers.get(0)).setOutputs(input);
		for(Layer layer: layers){
			if(!(layer instanceof InputLayer)){
				layer.countOutput();
			}
		}
		return ((OutputLayer)layers.get(layers.size()-1)).getOutput();
	}
	
	public List<Double> getOutput(double[] input) throws Exception {
		if(input.length != layers.get(0).getNeurons().size()){
			throw new Exception ("Wrong number of values in input. Input layer has "+layers.get(0).getNeurons().size()+" neurons.");
		}
		List<Double> in = new ArrayList<Double>();
		for(int i=0;i<input.length;i++){
			in.add(input[i]);
		}
		((InputLayer) layers.get(0)).setOutputs(in);
		for(Layer layer: layers){
			if(!(layer instanceof InputLayer)){
				layer.countOutput();
			}
		}
		return ((OutputLayer)layers.get(layers.size()-1)).getOutput();
	}
	
	private OutputLayer getOutputLayer(){
		return (OutputLayer) layers.get(layers.size()-1);
	}
	
	public void setBiasEnabled(boolean biasEnabled){
		for (Layer layer: layers){
			layer.setBiasEnabled(biasEnabled);
		}
	}
	
	private void randomizeVectors(List<LearningVector> vectors){
		Random rand = new Random();
		for(int i=0;i<vectors.size()*2;i++){
			int index1 = rand.nextInt(vectors.size());
			int index2 = rand.nextInt(vectors.size());
			LearningVector tmp = vectors.get(index1);
			vectors.set(index1, vectors.get(index2));
			vectors.set(index2, tmp);
		}
	}
	
	public double teach(List<LearningVector> vectors, double learningFactor, double momentum, double maxError, int maxEpoch ) throws Exception{
		double RMS = 0.0;
		double ERMS = 1.0;
		int epoch = 0;
		
		this.learningFactor = learningFactor;
		this.momentum = momentum;
		
		learningProcess.clear();
		
		while (ERMS > maxError && epoch < maxEpoch) {
			if(isRandomizeLearningVectors()) {
				randomizeVectors(vectors);
			}
			for (LearningVector vector : vectors) {
				RMS = 0.0;
				for (int i = 1; i < layers.size(); i++) {
					layers.get(i).resetErrors();
				}
				getOutput(vector.getInput());
				getOutputLayer().countErrors(vector.getOutput());

				getOutputLayer().propagateErrorBackwards();
				for (int i = layers.size() - 2; i > 0; i--) {
					layers.get(i).propagateErrorBackwards();
				}

				getOutputLayer().correctWeights(learningFactor, momentum);
				for (int i = layers.size() - 2; i > 0; i--) {
					layers.get(i).correctWeights(learningFactor, momentum);
				}
				List<Double> output = getOutput(vector.getInput());
				for (int i = 0; i < output.size(); i++) {
					RMS += Math.pow(vector.getOutput().get(i) - output.get(i),
							2.0);
				}
			}
			ERMS = Math.sqrt(RMS / (vectors.size() * getOutputLayer().getNeurons().size()));
			//System.out.println(ERMS);
			epoch++;
			learningProcess.put(epoch, ERMS);
			//System.out.println("Epoch: " + epoch + " ; Error: " + ERMS);
		}
		//System.out.println(ERMS);
		return ERMS;
	}

	public boolean isRandomizeLearningVectors() {
		return randomizeLearningVectors;
	}

	public void setRandomizeLearningVectors(boolean randomizeLearningVectors) {
		this.randomizeLearningVectors = randomizeLearningVectors;
	}
	
	public void store(String path) {
		try {
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
	
	public void saveLearningChart(String name, String path){
		XYSeries series = new XYSeries("Learning process, \u03B7="+learningFactor+" , \u03B1=" + momentum);
		Iterator<Integer> it = learningProcess.keySet().iterator();
		while (it.hasNext()) {
			Integer key = it.next();
			series.add(key, learningProcess.get(key));
		}
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		JFreeChart chart = ChartFactory.createXYLineChart(
				name, // Title
				"Epoch", // x-axis Label
				"Network Error", // y-axis Label
				dataset, // Dataset
				PlotOrientation.VERTICAL, // Plot Orientation
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);
		try {
			ChartUtilities.saveChartAsJPEG(new File(path), chart, 1024, 960);
		} catch (IOException e) {
			System.err.println("Problem occurred creating chart.");
		}
	}

}
