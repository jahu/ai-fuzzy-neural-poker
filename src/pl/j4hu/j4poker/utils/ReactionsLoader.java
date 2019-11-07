package pl.j4hu.j4poker.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import pl.j4hu.ai.ann.LearningVector;
import pl.j4hu.ai.fuzzy.FuzzyController;
import pl.j4hu.ai.fuzzy.FuzzySet;
import pl.j4hu.ai.fuzzy.FuzzyTerm;
import pl.j4hu.ai.fuzzy.FuzzyVariable;
import pl.j4hu.j4poker.headsup.domain.MoveType;
import pl.j4hu.j4poker.headsup.service.ai.PokerFuzzyController;

public class ReactionsLoader {

	public static List<Double> getInputNeurons(PokerFuzzyController pokerFuzzyController, int playerStack, int opponentStack, int playerContribution, int opponentContribution, 
			int pot, int noPhase, double playerHandStrenght, String tableStrenghtStr, String playerMove, int minimalRaise) throws Exception {
		FuzzyController fuzzyController = pokerFuzzyController.getFuzzyController();
		List<Double> input = new ArrayList<Double>();
		
		double tableStrenght = 0.0;
		if (!tableStrenghtStr.equals("NaN")) {
			tableStrenght = Double.parseDouble(tableStrenghtStr);
		}
		
		FuzzyVariable stackVariable = fuzzyController.getVariableByName("StackSize");
		input.addAll(stackVariable.getSetValues(playerStack));
		input.addAll(stackVariable.getSetValues(opponentStack));
		
		FuzzyVariable contributionVariable = fuzzyController.getVariableByName("MyContribution");
		input.addAll(contributionVariable.getSetValues(playerContribution));
		input.addAll(contributionVariable.getSetValues(opponentContribution));
		
		FuzzyVariable potVariable = fuzzyController.getVariableByName("PotSize");
		input.addAll(potVariable.getSetValues( (pot * 100) / (playerStack + playerContribution + opponentStack + opponentContribution) ));
		
		switch (noPhase) {
		case 0:
			input.add(1.0);
			input.add(0.0);
			input.add(0.0);
			input.add(0.0);
			break;
		case 1:
			input.add(0.0);
			input.add(1.0);
			input.add(0.0);
			input.add(0.0);
			break;
		case 2:
			input.add(0.0);
			input.add(0.0);
			input.add(1.0);
			input.add(0.0);
			break;
		case 3:
			input.add(0.0);
			input.add(0.0);
			input.add(0.0);
			input.add(1.0);
			break;
		default:
			input.add(1.0);
			input.add(0.0);
			input.add(0.0);
			input.add(0.0);
		}
		
		FuzzyVariable handStrenghtVariable = fuzzyController.getVariableByName("HandStrenght");
		input.addAll(handStrenghtVariable.getSetValues(playerHandStrenght));
		
		FuzzyVariable tableStrenghtVariable = fuzzyController.getVariableByName("TableStrenght");
		if (tableStrenghtStr.equals("NaN")) {
			input.add(0.0);
			input.add(0.0);
			input.add(0.0);
		} else {
			input.addAll(tableStrenghtVariable.getSetValues(tableStrenght));
		}
		
		//pokerFuzzyController.setRaiseVariable(minimalRaise, Math.max(playerStack, opponentStack));
		pokerFuzzyController.getFuzzyController().getKnowledgeBase().refreshRules(pokerFuzzyController.getFuzzyController().getVariables());
		
		FuzzyVariable raiseVariable = fuzzyController.getVariableByName("Raise");
		input.addAll(getPlayerMove(playerMove, raiseVariable));
		
		return input;
	}
	
	public static List<LearningVector> getLearningVectorsFromReactionsFile(String fileName, PokerFuzzyController pokerFuzzyController) {
		List<LearningVector> learningVectors = new ArrayList<LearningVector>();
		FuzzyController fuzzyController = pokerFuzzyController.getFuzzyController();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String strLine;
			while ((strLine = reader.readLine()) != null) {
				List<Double> output = new ArrayList<Double>();
				
				StringTokenizer tokenizer = new StringTokenizer(strLine);
				int playerStack = Integer.parseInt(tokenizer.nextToken(";"));
				int opponentStack = Integer.parseInt(tokenizer.nextToken());
				int playerContribution = Integer.parseInt(tokenizer.nextToken());
				int opponentContribution = Integer.parseInt(tokenizer.nextToken());
				int pot = Integer.parseInt(tokenizer.nextToken());
				int noPhase = Integer.parseInt(tokenizer.nextToken());
				double playerHandStrenght = Double.parseDouble(tokenizer.nextToken());
				String tableStrenghtStr = tokenizer.nextToken();
				String playerMove = tokenizer.nextToken();
				String opponentReaction = tokenizer.nextToken();
				int minimalRaise = Integer.parseInt(tokenizer.nextToken());
				
				//pokerFuzzyController.setRaiseVariable(minimalRaise, Math.max(playerStack, opponentStack));
				pokerFuzzyController.getFuzzyController().getKnowledgeBase().refreshRules(pokerFuzzyController.getFuzzyController().getVariables());
				
				FuzzyVariable raiseVariable = fuzzyController.getVariableByName("Raise");
				List<Double> input = getInputNeurons(pokerFuzzyController, playerStack, opponentStack, playerContribution, opponentContribution, pot, noPhase, 
						playerHandStrenght, tableStrenghtStr, playerMove, minimalRaise);
				output.addAll(getOpponentReaction(opponentReaction, raiseVariable));
				
				LearningVector learningVector = new LearningVector(input, output);
				learningVectors.add(learningVector);

			}
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return learningVectors;
	}
	
	// PlayerMove [player=Gracz 1, move=Move=Raise (285)]     Player Move (check/call/raise small/raise big/allin) : 5
	private static List<Double> getPlayerMove(String playerMove, FuzzyVariable raiseVariable) throws Exception {
		List<Double> values = new ArrayList<Double>();
		
		int idx = playerMove.indexOf("Move=");
		playerMove = playerMove.substring(idx);
		idx = playerMove.indexOf("=");
		playerMove = playerMove.substring(idx+1);
		String moveTypeStr = playerMove.substring(0, playerMove.indexOf(" "));
		String valueStr = playerMove.substring(playerMove.indexOf("(") + 1, playerMove.indexOf(")"));
		double valueOfMove = Double.parseDouble(valueStr);
		MoveType moveType = MoveType.toMoveType(moveTypeStr);
		switch (moveType){
		case Call:
			values.add(0.0);
			values.add(1.0);
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			break;
		case Check:
			values.add(1.0);
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			break;
		case Fold:
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			break;
		case Raise:
			values.add(0.0);
			values.add(0.0);
			values.addAll(getRaiseNeurons(raiseVariable, valueOfMove));
			break;
		default:
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			break;
		}
		return values;
	}
	// Opponent reaction (fold/check/call/raise small/raise big/all in) : 6
	private static List<Double> getOpponentReaction(String opponentReaction, FuzzyVariable raiseVariable) throws Exception {
		List<Double> values = new ArrayList<Double>();
		
		int idx = opponentReaction.indexOf("Move=");
		opponentReaction = opponentReaction.substring(idx);
		idx = opponentReaction.indexOf("=");
		opponentReaction = opponentReaction.substring(idx+1);
		String moveTypeStr = opponentReaction.substring(0, opponentReaction.indexOf(" "));
		String valueStr = opponentReaction.substring(opponentReaction.indexOf("(") + 1, opponentReaction.indexOf(")"));
		double valueOfMove = Double.parseDouble(valueStr);
		MoveType moveType = MoveType.toMoveType(moveTypeStr);
		switch (moveType){
		case Call:
			values.add(0.0);
			values.add(0.0);
			values.add(1.0);
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			break;
		case Check:
			values.add(0.0);
			values.add(1.0);
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			break;
		case Fold:
			values.add(1.0);
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			break;
		case Raise:
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			values.addAll(getRaiseNeurons(raiseVariable, valueOfMove));
			break;
		default:
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			values.add(0.0);
			break;
		}
		return values;
	}
	
	private static List<Double> getRaiseNeurons(FuzzyVariable raiseVariable, double value) throws Exception {
		List<Double> values = new ArrayList<Double>();
		List<FuzzySet> sets = raiseVariable.getSets();
		double smallThreshold = 0;
		double bigThreshold = 0;
		double allInThreshold = 0;
		for (FuzzySet set: sets) {
			if (set instanceof FuzzyTerm) {
				if (set.getName().equals("Small")) {
					smallThreshold = ((FuzzyTerm) set).getTerm();
				} else 	if (set.getName().equals("Big")) {
					bigThreshold = ((FuzzyTerm) set).getTerm();
				} else 	if (set.getName().equals("AllIn")) {
					allInThreshold = ((FuzzyTerm) set).getTerm();
				}
			} else {
				throw new Exception("Given variable is propably not raise variable (because it contains non-term fuzzy sets");
			}
		}
		double small = Math.abs(value - smallThreshold);
		double big = Math.abs(value - bigThreshold);
		double allIn = Math.abs(value - allInThreshold);
		
		if (small < big) {
			if (small < allIn) {
				// small
				values.add(1.0);
				values.add(0.0);
				values.add(0.0);
			} else {
				// allIn
				values.add(0.0);
				values.add(0.0);
				values.add(1.0);
			}
		} else {
			if (big < allIn) {
				//big
				values.add(0.0);
				values.add(1.0);
				values.add(0.0);
			} else {
				// allIn
				values.add(0.0);
				values.add(0.0);
				values.add(1.0);
			}
		}
		
		return values;
	}
	
}
