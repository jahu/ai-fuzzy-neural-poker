package pl.j4hu.j4poker.headsup.test;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;

import pl.j4hu.ai.ann.LearningVector;
import pl.j4hu.ai.ann.Perceptron;
import pl.j4hu.ai.ann.SigmoidFunction;
import pl.j4hu.j4poker.headsup.service.PredictedMove;
import pl.j4hu.j4poker.headsup.service.ai.PokerFuzzyController;
import pl.j4hu.j4poker.utils.FuzzyLoader;
import pl.j4hu.j4poker.utils.ReactionsLoader;

public class PerceptronLearningTester {
	
	private static final String fuzzyControllerFileName = "resources/pokerFuzzyController.xml";
	private static final String reactionsFileName = "reactions.txt";
	
	public static void main(String [] args) throws Exception {
		PokerFuzzyController pokerFuzzyController = new PokerFuzzyController(FuzzyLoader.getFuzzyControllerFromXML(fuzzyControllerFileName));
		DecimalFormat df = new DecimalFormat("#.######");
		DecimalFormat df2 = new DecimalFormat("#.#");
		double beta = 0.5;
		//for (int j=0; j<9; j++) {
			//for (int i=0; i<3; i++) {
				Perceptron perceptron = new Perceptron(3, new int[] {30,21,6}, new SigmoidFunction(beta));
				perceptron.setBiasEnabled(true);
				perceptron.generateWeights();
				List<LearningVector> learningVectors = ReactionsLoader.getLearningVectorsFromReactionsFile(reactionsFileName, pokerFuzzyController);
		
				Properties properties = new Properties();
				properties.load(new FileInputStream("resources/config.properties"));
				double learningFactor = Double.parseDouble(properties.getProperty("learningFactor"));
				double momentum = Double.parseDouble(properties.getProperty("momentum"));
				double maxError = Double.parseDouble(properties.getProperty("maxError"));
				int maxEpoch = Integer.parseInt(properties.getProperty("maxEpoch"));
				
				maxEpoch = 1000;
				perceptron.setRandomizeLearningVectors(true);
			
			
				momentum = 0.3;
				learningFactor = 0.9;
				perceptron.generateWeights();
				double ERMS = perceptron.teach(learningVectors, learningFactor, momentum, maxError, maxEpoch);
				//System.out.println("Eta: " + learningFactor +", alpha: " + momentum +": Error: " + ERMS);
				System.out.println( df2.format(learningFactor) + " & " + df2.format(momentum) + " & " + df2.format(beta) + " & " + df.format(ERMS) + " \\\\ \\hline");
			
				int count = 0;
				
				for (int i=0; i<learningVectors.size(); i++) {
					LearningVector learningVector = learningVectors.get(i);
					List<Double> input = learningVector.getInput();
					List<Double> output = learningVector.getOutput();
					if (PredictedMove.toPredictedMove(getOutput(output)) != PredictedMove.toPredictedMove(getOutput(perceptron.getOutput(input)))) {
						count++;
					}
					System.out.println((i+1) + " & " + PredictedMove.toPredictedMove(getOutput(output)) + " & " + PredictedMove.toPredictedMove(getOutput(perceptron.getOutput(input))) + " & \\\\ \\hline");
				}
				System.out.println((count * 100) / learningVectors.size());
			//}
		//}
		
	}
	
	public static int getOutput(List<Double> output) {
		int idx = 0;
		double max = Double.MIN_VALUE;
		int count = 0;
		for (Double val: output) {
			if (val > max) {
				max = val;
				idx = count;
			}
			count++;
		}
		return idx;
	}
}
