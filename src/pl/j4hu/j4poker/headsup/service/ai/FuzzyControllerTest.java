package pl.j4hu.j4poker.headsup.service.ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.j4hu.ai.fuzzy.DefuzzificatedValue;
import pl.j4hu.ai.fuzzy.FuzzyException;
import pl.j4hu.ai.fuzzy.FuzzyVariable;
import pl.j4hu.j4poker.headsup.domain.Deck;
import pl.j4hu.j4poker.headsup.domain.Hand;
import pl.j4hu.j4poker.utils.HandStrenght;

public class FuzzyControllerTest {

	public static void main(String [] args) throws FuzzyException, ClassNotFoundException, IOException {
		//PokerFuzzyController controller = new PokerFuzzyController();
		//controller.store("fuzzyPokerTest2.ser");
		
		PokerFuzzyController fuzzyController = new PokerFuzzyController("fuzzyPokerTest.ser");
		fuzzyController.setRaiseVariable(0, 1000);
		fuzzyController.getFuzzyController().getKnowledgeBase().refreshRules(fuzzyController.getFuzzyController().getVariables());
		
		Map<String, FuzzyVariable> varMap = new HashMap<String, FuzzyVariable>();
		for (FuzzyVariable variable: fuzzyController.getFuzzyController().getVariables()) {
			varMap.put(variable.getName(), variable);
		}
		
		Deck deck = new Deck();
		Hand hand = new Hand(deck);
		System.out.println(hand);
		
		FuzzyVariable handStrenght = varMap.get("HandStrenght");
		FuzzyVariable phase = varMap.get("Phase");
		FuzzyVariable stackSize = varMap.get("StackSize");
		FuzzyVariable amount = varMap.get("AmountToCall");
		
		DefuzzificatedValue handStr = new DefuzzificatedValue(handStrenght, HandStrenght.getHandStrenght(hand));
		DefuzzificatedValue phasePreFlop = new DefuzzificatedValue(phase, 0.0);
		DefuzzificatedValue stack = new DefuzzificatedValue(stackSize, 70.0);
		DefuzzificatedValue amountFuz = new DefuzzificatedValue(amount, 2.2);
		
		List<DefuzzificatedValue> input = new ArrayList<DefuzzificatedValue>();
		input.add(handStr);
		input.add(phasePreFlop);
		input.add(stack);
		input.add(amountFuz);
		
		List<DefuzzificatedValue> output = fuzzyController.getFuzzyController().getOutput(input);
		for(DefuzzificatedValue value: output){
			System.out.println(value.getVariable().getName()+": "+value.getValue());
		}
	}
	
}
