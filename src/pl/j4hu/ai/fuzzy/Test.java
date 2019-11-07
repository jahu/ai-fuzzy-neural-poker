package pl.j4hu.ai.fuzzy;

import java.util.ArrayList;
import java.util.List;

public class Test {

	/**
	 * @param args
	 * @throws FuzzyException 
	 */
	public static void main(String[] args) throws FuzzyException {
		FuzzyVariable velocity = new FuzzyVariable("Velocity");
		FuzzyVariable breaking = new FuzzyVariable("Break");
		
		FuzzySet slow = new FuzzySetClassL("Slow");
		FuzzySet medium = new FuzzySetClassT("Medium");
		FuzzySet fast = new FuzzySetClassGamma("Fast");
		
		FuzzySet soft = new FuzzyTerm("Soft");
		FuzzySet hard = new FuzzyTerm("Hard");
		
		velocity.setSpace(0, 200);
		breaking.setSpace(0, 20);
		
		slow.setParameters(new double[] {40, 60});
		medium.setParameters(new double[] {40, 80, 120});
		fast.setParameters(new double[] {80, 120});
		
		soft.setParameters(new double[] {0});
		hard.setParameters(new double[] {20});
	
		breaking.addFuzzySet(soft);
		breaking.addFuzzySet(hard);
		
		velocity.addFuzzySet(slow);
		velocity.addFuzzySet(medium);
		velocity.addFuzzySet(fast);
		
		
		FuzzyRule rule1 = new FuzzyRule();
		rule1.addConstraint(velocity, "Medium");
		rule1.addConclusion(breaking, "Soft");
		
		FuzzyRule rule2 = new FuzzyRule();
		rule2.addConstraint(velocity, "Fast");
		rule2.addConclusion(breaking, "Hard");
		
		FuzzyController controller = new FuzzyController();
		controller.addVariable(velocity);
		controller.addVariable(breaking);
		controller.addRule(rule1);
		controller.addRule(rule2);

		
		DefuzzificatedValue val1 = new DefuzzificatedValue(velocity, 86.0);
		List<DefuzzificatedValue> vars = new ArrayList<DefuzzificatedValue>();
		vars.add(val1);
		List<DefuzzificatedValue> output = controller.getOutput(vars);
		for(DefuzzificatedValue value: output){
			System.out.println(value.getVariable().getName()+": "+value.getValue());
		}
	}

}
