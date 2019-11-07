package pl.j4hu.ai.fuzzy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnowledgeBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -744468181982299251L;

	private List<FuzzyRule> rules = null;

	public KnowledgeBase(){
		rules = new ArrayList<FuzzyRule>();
	}
	
	public void addRule(FuzzyRule rule){
		rules.add(rule);
	}
	
	public List<FuzzyRule> getRules() {
		return rules;
	}
	
	public void removeRule(FuzzyRule rule) throws FuzzyException{
		if (rules.contains(rule)) {
			rules.remove(rule);
		} else {
			throw new FuzzyException("There is no such a rule to be removed");
		}
	}
	
	public String toString(){
		String value = "";
		for(FuzzyRule rule: rules) {
			value += rule + "\n";
		}
		return value;
	}
	

	/**
	 * Method for updating rules variable references in order to give ability of variables update
	 * 
	 * @param fuzzyVariables
	 */
	public void refreshRules(List<FuzzyVariable> fuzzyVariables) {
		Map<String, FuzzyVariable> varMap = new HashMap<String, FuzzyVariable>();
		for (FuzzyVariable variable: fuzzyVariables) {
			varMap.put(variable.getName(), variable);
		}
		
		for (FuzzyRule fuzzyRule: rules) {
			List<FuzzyConstraint> constraints = fuzzyRule.getConstraints();
			List<FuzzyConstraint> conclustions = fuzzyRule.getConclusions();
			
			for (FuzzyConstraint constraint: constraints) {
				String varName = constraint.getVariable().getName();
				constraint.setVariable(varMap.get(varName));
			}
			
			for (FuzzyConstraint conclusion: conclustions) {
				String varName = conclusion.getVariable().getName();
				conclusion.setVariable(varMap.get(varName));
			}
		}
	}
}
