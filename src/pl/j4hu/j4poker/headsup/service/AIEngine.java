package pl.j4hu.j4poker.headsup.service;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import pl.j4hu.ai.ann.LearningVector;
import pl.j4hu.ai.ann.Perceptron;
import pl.j4hu.ai.fuzzy.DefuzzificatedValue;
import pl.j4hu.ai.fuzzy.FuzzyException;
import pl.j4hu.ai.fuzzy.FuzzySet;
import pl.j4hu.ai.fuzzy.FuzzyVariable;
import pl.j4hu.j4poker.headsup.controller.HeadsUpController;
import pl.j4hu.j4poker.headsup.domain.Blinds;
import pl.j4hu.j4poker.headsup.domain.Card;
import pl.j4hu.j4poker.headsup.domain.Hand;
import pl.j4hu.j4poker.headsup.domain.Move;
import pl.j4hu.j4poker.headsup.domain.MoveType;
import pl.j4hu.j4poker.headsup.domain.Phase;
import pl.j4hu.j4poker.headsup.domain.Player;
import pl.j4hu.j4poker.headsup.domain.PlayerMove;
import pl.j4hu.j4poker.headsup.service.ai.PokerFuzzyController;
import pl.j4hu.j4poker.utils.FuzzyLoader;
import pl.j4hu.j4poker.utils.HandStrenght;
import pl.j4hu.j4poker.utils.HeadsupUtils;
import pl.j4hu.j4poker.utils.ReactionsLoader;

public class AIEngine {

	private static PokerFuzzyController fuzzyController = null;
	private static Perceptron perceptron = null;
	
	private static String reactionsFileName = "defaultReactions.txt";
	private static String perceptronFileName = "perceptron.ser";

	private static double learningFactor;
	private static double momentum;
	private static double maxError;
	private static int maxEpoch;
	
	public AIEngine() throws AIEngineException {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("resources/config.properties"));
			AIEngine.fuzzyController = new PokerFuzzyController(FuzzyLoader.getFuzzyControllerFromXML(properties.getProperty("fuzzyXMLFile")));
			AIEngine.perceptronFileName = properties.getProperty("perceptronFile");
			AIEngine.perceptron = new Perceptron(perceptronFileName);
			
			AIEngine.reactionsFileName = properties.getProperty("reactionFileName");
			
			learningFactor = Double.parseDouble(properties.getProperty("learningFactor"));
			momentum = Double.parseDouble(properties.getProperty("momentum"));
			maxError = Double.parseDouble(properties.getProperty("maxError"));
			maxEpoch = Integer.parseInt(properties.getProperty("maxEpoch"));
		} catch (Exception exception) {
			throw new AIEngineException(exception);
		}
	}
	
	public AIEngine(String fuzzyControllerFilename) throws AIEngineException {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("resources/config.properties"));
			AIEngine.fuzzyController = new PokerFuzzyController(FuzzyLoader.getFuzzyControllerFromXML(fuzzyControllerFilename));
			AIEngine.perceptronFileName = properties.getProperty("perceptronFile");
			AIEngine.perceptron = new Perceptron(perceptronFileName);
			
			AIEngine.reactionsFileName = properties.getProperty("reactionFileName");
			
			learningFactor = Double.parseDouble(properties.getProperty("learningFactor"));
			momentum = Double.parseDouble(properties.getProperty("momentum"));
			maxError = Double.parseDouble(properties.getProperty("maxError"));
			maxEpoch = Integer.parseInt(properties.getProperty("maxEpoch"));
		} catch (Exception exception) {
			throw new AIEngineException(exception);
		}
	}
	
	public Move getNextMove(HeadsUpController headsUpController, Hand hand, List<Card> communityCards,
			List<MoveType> possibleMoves, int pot, int currentBet,
			int bigBlind, PlayerMove lastMove, int stack, int opponentStack, Phase phase, int playerContribution, int opponentContribution) throws Exception {
		int minimalRaise = headsUpController.getMinimalRaise(currentBet, lastMove);
		
		Move myInitialMove = getMyPossibleNextMove(headsUpController, hand,
				communityCards, possibleMoves, pot, currentBet, minimalRaise, bigBlind,
				stack, phase, lastMove);
		PredictedMove possibleOpponentMove = getPossibleOpponentNextMove(myInitialMove, hand, communityCards, pot, currentBet, minimalRaise, bigBlind, stack, opponentStack, phase, playerContribution, opponentContribution);
		Move myFinalMove = getMyFinalMove(headsUpController, hand, possibleMoves, pot, currentBet, bigBlind, myInitialMove, possibleOpponentMove, stack, phase, minimalRaise, communityCards, lastMove);
		return myFinalMove;
		//return myInitialMove;
	}
	
	private Move getMyPossibleNextMove(HeadsUpController headsUpController, Hand hand, List<Card> communityCards,
			List<MoveType> possibleMoves, int pot, int currentBet, int minimalRaise,
			int bigBlind, int stack, Phase currentPhase, PlayerMove lastMove) throws FuzzyException {
		
		List<DefuzzificatedValue> input = getInputForFuzzyController(headsUpController, hand, communityCards, possibleMoves, pot, currentBet, minimalRaise, bigBlind, stack, currentPhase, lastMove);	
		List<DefuzzificatedValue> output = fuzzyController.getFuzzyController().getOutput(input);
		return getMoveFromOutputOfFuzzyController(output, possibleMoves, stack, minimalRaise);

	}
	
	private PredictedMove getPossibleOpponentNextMove(Move myMove, Hand hand, List<Card> communityCards, int pot, int currentBet, int minimalRaise,
			int bigBlind, int stack, int opponentStack, Phase currentPhase, int playerContribution, int opponentContribution) throws Exception {
		double tableStrenght = 0.0;
		String tableStrenghtStr = "NaN";
		if (communityCards != null && communityCards.size()>=3) {
			tableStrenght = HeadsupUtils.getTableStrenght(communityCards);
			tableStrenghtStr = Double.toString(tableStrenght);
		}
		
		
		List<Double> input = ReactionsLoader.getInputNeurons(fuzzyController, stack, opponentStack, playerContribution, 
				opponentContribution, pot, currentPhase.ordinal(), HandStrenght.getHandStrenght(hand), tableStrenghtStr , myMove.toString(), minimalRaise);

		List<Double> output = perceptron.getOutput(input);
		double max = 0.0;
		PredictedMove predictedMove = null;
		if (output.size() != PredictedMove.values().length) {
			throw new Exception("Number of output neurons must be the same as number of elements in PredictedMove enum!");
		}
		for (int i=0; i<output.size(); i++) {
			double value = output.get(i);
			if (value > max) {
				max = value;
				predictedMove = PredictedMove.values()[i];
			}
		}
		return predictedMove;
	}
	
	private Move getMyFinalMove(HeadsUpController headsUpController, Hand hand,
			List<MoveType> possibleMoves, int pot, int currentBet,
			int bigBlind, Move myInitialMove, PredictedMove possibleOpponentMove, int stack, Phase currentPhase, int minimalRaise, List<Card> communityCards, PlayerMove lastMove) {
		PredictedMove myMove = toPredictedMove(myInitialMove);

		FuzzyVariable myMoveVar = fuzzyController.getFuzzyController().getVariableByName("MyInitialMove");
		FuzzyVariable predictedOppMoveVar = fuzzyController.getFuzzyController().getVariableByName("PredictedOpponentMove");
		
		DefuzzificatedValue myMoveValue = null;
		switch (myMove) {
		case Call:
			myMoveValue = new DefuzzificatedValue(myMoveVar, 2.0);
			break;
		case Check:
			myMoveValue = new DefuzzificatedValue(myMoveVar, 1.0);
			break;
		case Fold:
			myMoveValue = new DefuzzificatedValue(myMoveVar, 0.0);
			break;
		case RaiseAllIn:
			myMoveValue = new DefuzzificatedValue(myMoveVar, 5.0);
			break;
		case RaiseBig:
			myMoveValue = new DefuzzificatedValue(myMoveVar, 4.0);
			break;
		case RaiseSmall:
			myMoveValue = new DefuzzificatedValue(myMoveVar, 3.0);
			break;
		default:
			break;
		}
		
		DefuzzificatedValue oppMoveValue = null;
		switch (possibleOpponentMove) {
		case Call:
			oppMoveValue = new DefuzzificatedValue(predictedOppMoveVar, 2.0);
			break;
		case Check:
			oppMoveValue = new DefuzzificatedValue(predictedOppMoveVar, 1.0);
			break;
		case Fold:
			oppMoveValue = new DefuzzificatedValue(predictedOppMoveVar, 0.0);
			break;
		case RaiseAllIn:
			oppMoveValue = new DefuzzificatedValue(predictedOppMoveVar, 5.0);
			break;
		case RaiseBig:
			oppMoveValue = new DefuzzificatedValue(predictedOppMoveVar, 4.0);
			break;
		case RaiseSmall:
			oppMoveValue = new DefuzzificatedValue(predictedOppMoveVar, 3.0);
			break;
		default:
			break;
		}
		
		List<DefuzzificatedValue> input = getInputForFuzzyController(headsUpController, hand, communityCards, possibleMoves, pot, currentBet, minimalRaise, bigBlind, stack, currentPhase, lastMove);
		input.add(myMoveValue);
		input.add(oppMoveValue);

		List<DefuzzificatedValue> output = fuzzyController.getFuzzyController().getOutput(input);
		return getMoveFromOutputOfFuzzyController(output, possibleMoves, stack, minimalRaise);
	
	}
	
	private List<DefuzzificatedValue> getInputForFuzzyController(HeadsUpController headsUpController, Hand hand, List<Card> communityCards,
			List<MoveType> possibleMoves, int pot, int currentBet, int minimalRaise,
			int bigBlind, int stack, Phase currentPhase, PlayerMove lastMove) {
		Map<String, FuzzyVariable> varMap = new HashMap<String, FuzzyVariable>();
		for (FuzzyVariable variable: fuzzyController.getFuzzyController().getVariables()) {
			varMap.put(variable.getName(), variable);
		}
		
		FuzzyVariable handStrenght = varMap.get("HandStrenght");
		FuzzyVariable phase = varMap.get("Phase");
		FuzzyVariable stackSize = varMap.get("StackSize");
		FuzzyVariable amount = varMap.get("AmountToCall");
		FuzzyVariable tableStrenght = varMap.get("TableStrenght");
		FuzzyVariable lastPlayerMove = varMap.get("OpponentLastMove");
		
		Blinds blinds = headsUpController.getBlinds();
		
		
		DefuzzificatedValue phaseVar = null;
		switch (currentPhase) {
		case Flop:
			phaseVar = new DefuzzificatedValue(phase, 1.0);
			break;
		case PreFlop:
			phaseVar = new DefuzzificatedValue(phase, 0.0);
			break;
		case River:
			phaseVar = new DefuzzificatedValue(phase, 3.0);
			break;
		case Turn:
			phaseVar = new DefuzzificatedValue(phase, 2.0);
			break;
		default:
			break;
		}
		
		DefuzzificatedValue lastPlayerMoveVal = null;
		if (lastMove != null) {
			switch (lastMove.getMove().getType()) {
			case Fold:
				lastPlayerMoveVal = new DefuzzificatedValue(phase, 0.0);
				break;
			case Check:
				lastPlayerMoveVal = new DefuzzificatedValue(phase, 1.0);
				break;
			case Call:
				lastPlayerMoveVal = new DefuzzificatedValue(phase, 2.0);
				break;
			case Raise:
				lastPlayerMoveVal = new DefuzzificatedValue(phase, 3.0);
				break;
			default:
				break;
			}
		}
		
		DefuzzificatedValue handStr = new DefuzzificatedValue(handStrenght, HandStrenght.getHandStrenght(hand));
		DefuzzificatedValue stackVal = new DefuzzificatedValue(stackSize, (double)(stack / blinds.getBigBlind()));
		DefuzzificatedValue amountFuz = new DefuzzificatedValue(amount, (double) (currentBet / pot));
		
		
		List<DefuzzificatedValue> input = new ArrayList<DefuzzificatedValue>();
		input.add(handStr);
		input.add(phaseVar);
		if (lastMove != null) {
			input.add(lastPlayerMoveVal);
		}
		input.add(stackVal);
		input.add(amountFuz);
		if (communityCards != null && communityCards.size()>0) {
			DefuzzificatedValue tableStrenghtVal = new DefuzzificatedValue(tableStrenght, HeadsupUtils.getTableStrenght(communityCards));
			input.add(tableStrenghtVal);
		}
		return input;
	}
	
	private PredictedMove toPredictedMove(Move move) {
		PredictedMove predictedMove = null;
		switch (move.getType()) {
		case Call:
			predictedMove = PredictedMove.Call;
			break;
		case Check:
			predictedMove = PredictedMove.Check;
			break;
		case Fold:
			predictedMove = PredictedMove.Fold;
			break;
		case Raise:
			int raiseAmount = move.getAmount();
			FuzzySet bestMatchedRaiseSet = fuzzyController.getFuzzyController().getVariableByName("Raise").getBestMatchedSet(raiseAmount);
			String raiseSet = bestMatchedRaiseSet.getName();
			if (raiseSet.equals("Small")) {
				predictedMove = PredictedMove.RaiseSmall;
			} else if (raiseSet.equals("Big")) {
				predictedMove = PredictedMove.RaiseBig;
			} else if (raiseSet.equals("AllIn")) {
				predictedMove = PredictedMove.RaiseAllIn;
			} 
			break;
		default:
			break;
		
		}
		return predictedMove;
	}
	
	private Move getMoveFromOutputOfFuzzyController(List<DefuzzificatedValue> output, List<MoveType> possibleMoves, int stack, int minimalRaise) {
		MoveType moveType = null;
		double raiseValue = 0.0;
		for (DefuzzificatedValue defuzzificatedValue: output) {
			String varName = defuzzificatedValue.getVariable().getName();
			if (varName.equals("MoveType")) {
				double moveVal = defuzzificatedValue.getValue();
				if (moveVal > 2.4) {
					moveType = MoveType.Raise;
				} else if (moveVal <= 2.4 && moveVal >= 1.6) {
					moveType = MoveType.Call;
				} else if (moveVal < 1.6) {
					moveType = MoveType.Fold;
				}
			} else if (varName.equals("Raise")) {
				raiseValue = defuzzificatedValue.getValue();
			}
		}
		
		Move move = null;
		switch (moveType) {
			case Call:
				if (possibleMoves.contains(moveType)) {
					move = new Move(MoveType.Call);
				} else if (possibleMoves.contains(MoveType.Check)) {
					move = new Move(MoveType.Check);
				} else {
					move = new Move(MoveType.Fold);
				}
				break;
			case Fold:
				if (possibleMoves.contains(MoveType.Check)) {
					move = new Move(MoveType.Check);
				} else {
					move = new Move(MoveType.Fold);
				}
				break;
			case Raise:
				if (possibleMoves.contains(moveType)) {
					if (raiseValue > stack) {
						raiseValue = stack;
					}
					if (raiseValue < minimalRaise) {
						raiseValue = minimalRaise;
					} 
					move = new Move(moveType, (int) raiseValue);
				} else if (possibleMoves.contains(MoveType.Call)) {
					move = new Move(MoveType.Call);
				} else if (possibleMoves.contains(MoveType.Check)) {
					move = new Move(MoveType.Check);
				} else {
					move = new Move(MoveType.Fold);
				}
				break;
		default:
			break;
		}
		return move;
	}
	
	public static void teachPerceptron(Player player, Player opponent,
			int playerContribution, int opponentContribution, int pot, Blinds blinds,
			int noPhase, List<Card> communityCards, PlayerMove playerMove,
			PlayerMove opponentReaction) throws Exception {
		List<LearningVector> learningVectors = ReactionsLoader.getLearningVectorsFromReactionsFile(reactionsFileName, fuzzyController);
		perceptron.setRandomizeLearningVectors(true);
		perceptron.teach(learningVectors, learningFactor, momentum, maxError, maxEpoch);
		perceptron.store(perceptronFileName);
	}
	
	public static void refreshRaiseVariable(int minimalRaise, int maximalRaise) throws FuzzyException {
		fuzzyController.setRaiseVariable(minimalRaise, maximalRaise);
		fuzzyController.getFuzzyController().getKnowledgeBase().refreshRules(fuzzyController.getFuzzyController().getVariables());
	}
	
	public static Perceptron getPerceptron() {
		return perceptron;
	}

	public static void setPerceptron(Perceptron perceptron) {
		AIEngine.perceptron = perceptron;
	}

	public static double getLearningFactor() {
		return learningFactor;
	}

	public static void setLearningFactor(double learningFactor) {
		AIEngine.learningFactor = learningFactor;
	}

	public static double getMomentum() {
		return momentum;
	}

	public static void setMomentum(double momentum) {
		AIEngine.momentum = momentum;
	}

	public static double getMaxError() {
		return maxError;
	}

	public static void setMaxError(double maxError) {
		AIEngine.maxError = maxError;
	}

	public static int getMaxEpoch() {
		return maxEpoch;
	}

	public static void setMaxEpoch(int maxEpoch) {
		AIEngine.maxEpoch = maxEpoch;
	}
	
}
