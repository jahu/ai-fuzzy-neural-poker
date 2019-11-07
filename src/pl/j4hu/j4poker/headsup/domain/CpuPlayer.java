package pl.j4hu.j4poker.headsup.domain;

import java.util.List;

import pl.j4hu.ai.fuzzy.FuzzyException;
import pl.j4hu.j4poker.headsup.controller.HeadsUpController;
import pl.j4hu.j4poker.headsup.service.AIEngine;
import pl.j4hu.j4poker.headsup.service.AIEngineException;

public class CpuPlayer extends HumanPlayer {

	private AIEngine aiEngine = null;
	
	public CpuPlayer(String name) throws AIEngineException {
		super(name);
		this.aiEngine = new AIEngine();
	}
	
	public CpuPlayer(String name, String fuzzyControllerFilename) throws AIEngineException {
		super(name);
		this.aiEngine = new AIEngine(fuzzyControllerFilename);
	}
	
	public Move getMove(HeadsUpController headsUpController,
			List<Card> communityCards, List<MoveType> possibleMoves, int pot,
			int currentBet, int bigBlind, PlayerMove lastMove, Phase currentPhase, int opponentStack, int playerContribution, int opponentContribution) throws Exception {
		try {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return aiEngine.getNextMove(headsUpController, this.getHand(),
					communityCards, possibleMoves, pot, currentBet, bigBlind,
					lastMove, getStack(), opponentStack, currentPhase, playerContribution, opponentContribution);
		} catch (FuzzyException e) {
			e.printStackTrace();
			return new Move(MoveType.Fold);
		}
	}

	@Override
	public String toString() {
		return "CpuPlayer [name=" + getName() + ", hand=" + getHand() + ", stack="
				+ getStack() + "]";
	}

	
}
