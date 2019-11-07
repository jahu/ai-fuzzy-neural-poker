package pl.j4hu.ai.ann;

import java.util.ArrayList;
import java.util.List;

public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Perceptron perceptron = new Perceptron(3, new int[] {2,2,1}, new SigmoidFunction(0.6));
		perceptron.setBiasEnabled(true);
		perceptron.generateWeights();
		
		List<LearningVector> learningVectors = new ArrayList<LearningVector>();
		
		List<Double> input = new ArrayList<Double>();
		List<Double> output = new ArrayList<Double>();
		
		input.add(0.0);
		input.add(1.0);
		output.add(1.0);
		LearningVector vector = new LearningVector(input, output);
		learningVectors.add(vector);
		
		input = new ArrayList<Double>();
		output = new ArrayList<Double>();
		input.add(1.0);
		input.add(1.0);
		output.add(0.0);
		vector = new LearningVector(input, output);
		learningVectors.add(vector);
		
		input = new ArrayList<Double>();
		output = new ArrayList<Double>();
		input.add(0.0);
		input.add(0.0);
		output.add(0.0);
		vector = new LearningVector(input, output);
		learningVectors.add(vector);
		
		input = new ArrayList<Double>();
		output = new ArrayList<Double>();
		input.add(1.0);
		input.add(0.0);
		output.add(1.0);
		vector = new LearningVector(input, output);
		learningVectors.add(vector);
		
		perceptron.setRandomizeLearningVectors(true);
		System.out.println(perceptron.teach(learningVectors, 0.9, 0.6, 0.0001, 5000));

		
		System.out.println("1 = "+ perceptron.getOutput(new double[] {0.1, 0.9}));
		System.out.println("0 = "+ perceptron.getOutput(new double[] {0.9, 0.9}));
		System.out.println("0 = "+ perceptron.getOutput(new double[] {0.0, 0.1}));
		System.out.println("1 = "+ perceptron.getOutput(new double[] {0.9, 0.1}));
/*		
		perceptron.store("xor.ser");
		perceptron.saveLearningChart("XoR", "xor.jpg");
		//XMLSerializer.write(perceptron, "perceptron.xml");
		perceptron = null;
		
//		Perceptron perceptron2 = new Perceptron("xor2.ser");
//
//		System.out.println("\n");
//		System.out.println(perceptron2.getOutput(new double[] {0.1, 0.9}));
//		System.out.println(perceptron2.getOutput(new double[] {0.9, 0.9}));
//		System.out.println(perceptron2.getOutput(new double[] {0.0, 0.1}));
//		System.out.println(perceptron2.getOutput(new double[] {0.95, 0.01}));
*/	}

}
