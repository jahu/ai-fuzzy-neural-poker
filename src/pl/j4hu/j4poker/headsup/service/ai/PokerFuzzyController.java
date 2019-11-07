package pl.j4hu.j4poker.headsup.service.ai;

import java.io.IOException;

import pl.j4hu.ai.fuzzy.FuzzyController;
import pl.j4hu.ai.fuzzy.FuzzyException;
import pl.j4hu.ai.fuzzy.FuzzySet;
import pl.j4hu.ai.fuzzy.FuzzyTerm;
import pl.j4hu.ai.fuzzy.FuzzyVariable;

public class PokerFuzzyController {

	private FuzzyController fuzzyController = null;
	
	private static final String VAR_RAISE = "Raise";
	private static final String SET_SMALL_RAISE = "Small";
	private static final String SET_BIG_RAISE = "Big";
	private static final String SET_ALL_IN = "AllIn";
	
	public PokerFuzzyController(String path) throws ClassNotFoundException, IOException {
		load(path);
	}
	
	public PokerFuzzyController(FuzzyController fuzzyController) {
		this.fuzzyController = fuzzyController;
	}
	
	public FuzzyController getFuzzyController() {
		return fuzzyController;
	}

	public void store(String path) {
		fuzzyController.store(path);
	}
	
	public void load(String path) throws ClassNotFoundException, IOException {
		fuzzyController = new FuzzyController(path);
	}
	
	public void setRaiseVariable(int min, int max) throws FuzzyException {
		for (FuzzyVariable fuzzyVariable: fuzzyController.getVariables()) {
			if (fuzzyVariable.getName().equals(VAR_RAISE)) {
				fuzzyController.removeVariable(fuzzyVariable);
				break;
			}
		}
		FuzzyVariable raiseVariable = new FuzzyVariable(VAR_RAISE);
		if (min >= max) {
			max = min+1;
		}
		raiseVariable.setSpace(min, max);
		FuzzySet small = new FuzzyTerm(SET_SMALL_RAISE);
		small.setParameters(new double[] {min});
		raiseVariable.addFuzzySet(small);
		FuzzySet big = new FuzzyTerm(SET_BIG_RAISE);
		big.setParameters(new double[] { min + ((max-min)/12) });
		raiseVariable.addFuzzySet(big);
		FuzzySet allIn = new FuzzyTerm(SET_ALL_IN);
		allIn.setParameters(new double[] {max});
		raiseVariable.addFuzzySet(allIn);
		fuzzyController.addVariable(raiseVariable);
	}
	
}
