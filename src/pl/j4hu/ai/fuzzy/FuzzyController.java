package pl.j4hu.ai.fuzzy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FuzzyController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8007824268625489465L;
	
	private KnowledgeBase knowledgeBase = null;
	private List<FuzzyVariable> variables = null;
	
	public FuzzyController(){
		knowledgeBase = new KnowledgeBase();
		variables = new ArrayList<FuzzyVariable>();
	}
	
	public FuzzyController(String path) throws IOException, ClassNotFoundException {
//		try {
			FileInputStream fileIn = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			FuzzyController tmp = (FuzzyController) in.readObject();
			this.knowledgeBase = tmp.knowledgeBase;
			this.variables = tmp.variables;
			in.close();
			fileIn.close();
//		} catch (IOException i) {
//			i.printStackTrace();
//			return;
//		} catch (ClassNotFoundException c) {
//			System.out.println("FuzzyController class not found in given file");
//			c.printStackTrace();
//			return;
//		}
	}
	
	public void addRule(FuzzyRule rule) {
		knowledgeBase.addRule(rule);
	}
	
	public void removeRule(FuzzyRule rule) throws FuzzyException{
		knowledgeBase.removeRule(rule);
	}
	
	public void addVariable(FuzzyVariable var) {
		variables.add(var);
	}
	
	public void removeVariable(FuzzyVariable var) throws FuzzyException {
		if (variables.contains(var)) {
			variables.remove(var);
		} else {
			throw new FuzzyException("There is no such a variable to be removed");
		}
	}
	
	public List<DefuzzificatedValue> getOutput(List<DefuzzificatedValue> variables) {
		Map<FuzzyVariable, List<FuzzificatedValue>> fuzzyValues = new HashMap<FuzzyVariable, List<FuzzificatedValue>>();
		
		for(DefuzzificatedValue var: variables) {
			fuzzyValues.put(var.getVariable(), fuzzification(var));
		}
		Map<FuzzyVariable, List<FuzzificatedValue>> conclusion = getConclusion(fuzzyValues);
		
//		for(FuzzyVariable var: conclusion.keySet()){
//			System.out.println(var.getName());
//			for(FuzzificatedValue value: conclusion.get(var)){
//				System.out.println("\t" + value.getSet().getName() +": " + value.getValue() );
//			}
//		}
		
		return defuzzification(conclusion);
	}
	
	private List<FuzzificatedValue> fuzzification(DefuzzificatedValue variable) {
		List<FuzzificatedValue> values = new ArrayList<FuzzificatedValue>();
		FuzzyVariable var = variable.getVariable();
		Double x = variable.getValue();
		for(FuzzySet set: var.getSets()) {
			values.add(new FuzzificatedValue(set, set.getValue(x)));
		}
		return values;
	}
	
	private Map<FuzzyVariable, List<FuzzificatedValue>> getConclusion(Map<FuzzyVariable, List<FuzzificatedValue>> fuzzyValues){
		List<FuzzyRule> rules = knowledgeBase.getRules();
		Set<FuzzyVariable> variables = fuzzyValues.keySet();
		Map<FuzzyVariable, List<FuzzificatedValue>> conclusionMap = new HashMap<FuzzyVariable, List<FuzzificatedValue>>();
		
		boolean skipRule = false;
		
		for(FuzzyRule rule: rules) {
			skipRule = false;
			List<FuzzyConstraint> constraints = rule.getConstraints();
			
			double minValue = 1.0;
			
			for(FuzzyConstraint constraint: constraints) {
				if (!skipRule) {
					if (variables.contains(constraint.getVariable())) {
						List<FuzzificatedValue> fuzzyVals = fuzzyValues.get(constraint.getVariable());
						for(FuzzificatedValue fuzzyVal: fuzzyVals) {
							if (fuzzyVal.getSet().getName().equals(constraint.getValue())) {
								double possibleMin = fuzzyVal.getValue();
								if(possibleMin < minValue) minValue = possibleMin;
							}
						}
						continue;
					} else {
						skipRule = true;
						break;
					}
				}
			}
			if(!skipRule) {
				List<FuzzyConstraint> conclusions = rule.getConclusions();
				for (FuzzyConstraint conclusion: conclusions){
					if (! conclusionMap.keySet().contains(conclusion.getVariable())) {
						List<FuzzificatedValue> temp = new ArrayList<FuzzificatedValue>();
						for(FuzzySet set: conclusion.getVariable().getSets()){
							temp.add(new FuzzificatedValue(set, 0.0));
						}
						conclusionMap.put(conclusion.getVariable(), temp);
					}
					
					List<FuzzificatedValue> conclusionValues = conclusionMap.get(conclusion.getVariable());
					for(FuzzificatedValue fuzzyValue: conclusionValues){
						if (fuzzyValue.getSet().getName().equals(conclusion.getValue())) {
							double max = fuzzyValue.getValue();
							if (minValue > max) {
								max = minValue;
								fuzzyValue.setValue(max);
								break;
							}
						}
					}
					conclusionMap.put(conclusion.getVariable(), conclusionValues);
				}
			} else {
				continue;
			}
		}
		return conclusionMap;
	}
	
	private List<DefuzzificatedValue> defuzzification(Map<FuzzyVariable, List<FuzzificatedValue>> conclusion){
		List<DefuzzificatedValue> result = new ArrayList<DefuzzificatedValue>();
		for(FuzzyVariable var: conclusion.keySet()) {
			Map<String, Double> tempValue = new HashMap<String, Double>();
			double x = 0.0;
			double y = 0.0;
			for(FuzzificatedValue val: conclusion.get(var)){
				y += val.getValue();
				tempValue.put(val.getSet().getName(), val.getValue());
			}
			for(FuzzySet set: var.getSets()) {
				FuzzyTerm term = (FuzzyTerm) set;
				x += term.getTerm() * tempValue.get(term.getName());
			}
			if(y != 0) {
				result.add(new DefuzzificatedValue(var, x/y));
			} else {
				result.add(new DefuzzificatedValue(var, 0.0));
			}
		}
		return result;
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

	public List<FuzzyVariable> getVariables() {
		return variables;
	}
	
	public FuzzyVariable getVariableByName(String name) {
		for (FuzzyVariable fuzzyVariable: getVariables()) {
			if (fuzzyVariable.getName().equals(name)) {
				return fuzzyVariable;
			}
		}
		return null;
	}

	public KnowledgeBase getKnowledgeBase() {
		return knowledgeBase;
	}
	
} 
