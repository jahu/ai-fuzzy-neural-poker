package pl.j4hu.j4poker.headsup.service.ai;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import pl.j4hu.ai.ann.LearningVector;
import pl.j4hu.ai.ann.Perceptron;
import pl.j4hu.ai.ann.SigmoidFunction;
import pl.j4hu.j4poker.utils.FuzzyLoader;
import pl.j4hu.j4poker.utils.ReactionsLoader;

public class PerceptronTeacher {

	private static final String perceptronFileName = "perceptron.ser";
	private static final String fuzzyControllerFileName = "resources/pokerFuzzyController.xml";
	private static final String reactionsFileName = "reactions.txt";
	
	public static void main(String [] args) throws Exception {
		
		Properties properties = new Properties();
		properties.load(new FileInputStream("resources/config.properties"));
		double learningFactor = Double.parseDouble(properties.getProperty("learningFactor"));
		double momentum = Double.parseDouble(properties.getProperty("momentum"));
		double beta = Double.parseDouble(properties.getProperty("beta"));
		double maxError = Double.parseDouble(properties.getProperty("maxError"));
		int maxEpoch = Integer.parseInt(properties.getProperty("maxEpoch"));
		int noNeurons = Integer.parseInt(properties.getProperty("noNeurons"));
		
		PokerFuzzyController pokerFuzzyController = new PokerFuzzyController(FuzzyLoader.getFuzzyControllerFromXML(fuzzyControllerFileName));
		Perceptron perceptron = new Perceptron(3, new int[] {30,noNeurons,6}, new SigmoidFunction(beta));
		perceptron.setBiasEnabled(true);
		perceptron.generateWeights();
		List<LearningVector> learningVectors = ReactionsLoader.getLearningVectorsFromReactionsFile(reactionsFileName, pokerFuzzyController);
		
		perceptron.setRandomizeLearningVectors(true);
		perceptron.teach(learningVectors, learningFactor, momentum, maxError, maxEpoch);
		
		perceptron.store(perceptronFileName);
		//perceptron.saveLearningChart("j4Poker", "learning.jpg");
	}
	
}
