package pl.j4hu.j4poker.headsup.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import pl.j4hu.ai.fuzzy.FuzzyException;
import pl.j4hu.j4poker.headsup.domain.Blinds;
import pl.j4hu.j4poker.headsup.domain.Card;
import pl.j4hu.j4poker.headsup.domain.CpuPlayer;
import pl.j4hu.j4poker.headsup.domain.Move;
import pl.j4hu.j4poker.headsup.domain.MoveType;
import pl.j4hu.j4poker.headsup.domain.Phase;
import pl.j4hu.j4poker.headsup.domain.Player;
import pl.j4hu.j4poker.headsup.domain.PlayerMove;
import pl.j4hu.j4poker.headsup.service.AIEngine;
import pl.j4hu.j4poker.headsup.view.HeadsUpView;
import pl.j4hu.j4poker.utils.HandStrenght;
import pl.j4hu.j4poker.utils.HeadsupUtils;

public class StandardHeadsUpController implements HeadsUpController {
	
	private HeadsUpView view;
	private Blinds blinds;
	private int currentMinimalRaise;
	
	private String reactionFileName = "defaultReactions.txt";
	
	public StandardHeadsUpController(HeadsUpView headsUpView) {
		//view = new ConsoleHeadsUpView();
		view = headsUpView;
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("resources/config.properties"));
			this.reactionFileName = properties.getProperty("reactionFileName");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		

	}
	
	@Override
	public void log(String text) {
		view.log(text);
	}

	@Override
	public PlayerMove getMove(Player player, Player opponent, List<Card> communityCards,
			int pot, int currentBet, int bigBlind, PlayerMove lastMove, Phase phase, int playerContribution, int opponentContribution) throws Exception {
		List<MoveType> possibleMoves = getPossibleMoves(player, currentBet, bigBlind, lastMove, phase);
		PlayerMove playerMove = new PlayerMove(player, new Move(MoveType.Fold));
		
		if (player instanceof CpuPlayer) {
			Move move = ((CpuPlayer) player).getMove(this, communityCards, possibleMoves, pot, currentBet, bigBlind, lastMove, phase, opponent.getStack(), playerContribution, opponentContribution);
			playerMove = new PlayerMove(player, move);
		} else {
			view.setPossibleMoves(possibleMoves);
			currentMinimalRaise = getMinimalRaise(currentBet, lastMove);
			view.setMinimalRaiseAmount(currentMinimalRaise, player);
			//log("Possible moves: " + possibleMoves);
			//log("Current bet: " + currentBet);

			MoveType moveType = view.getNextMoveType(player);
			switch (moveType) {
			case Fold:
				playerMove = new PlayerMove(player, new Move(MoveType.Fold));
			case Check:
				playerMove = new PlayerMove(player, new Move(MoveType.Check));
			case Call:
				playerMove = new PlayerMove(player, new Move(MoveType.Call));
			case Raise:
				int raise = view.getRaiseAmount(player, getMinimalRaise(currentBet, lastMove));
				playerMove = new PlayerMove(player, new Move(moveType, raise));
			}
		}
		view.refreshStacks();
		view.refreshMove(player, playerMove);
		return playerMove;
	}

	@Override
	public void showCommunityCards(List<Card> communityCards) {
		view.displayCommunityCards(communityCards);
	}
	
	private List<MoveType> getPossibleMoves(Player player, int currentBet, int bigBlind, PlayerMove lastMove, Phase phase) {
		List<MoveType> possibleMoves = new ArrayList<MoveType>();
		possibleMoves.add(MoveType.Fold);
		if (lastMove != null) {
			switch(lastMove.getMove().getType()) {
			case Call:
				possibleMoves.add(MoveType.Check);
				possibleMoves.add(MoveType.Raise);
				break;
			case Check:
				possibleMoves.add(MoveType.Check);
				possibleMoves.add(MoveType.Raise);
				break;
			case Raise:
				possibleMoves.add(MoveType.Call);
				possibleMoves.add(MoveType.Raise);
				break;
			default:
				break;
			}
		} else {
			if (phase.equals(Phase.PreFlop)) {
				possibleMoves.add(MoveType.Call);
				possibleMoves.add(MoveType.Raise);
			} else {
				possibleMoves.add(MoveType.Check);
				possibleMoves.add(MoveType.Raise);
			}
		}
		return possibleMoves;
	}
	
	@Override
	public int getMinimalRaise(int currentBet, PlayerMove lastMove) {
		int toRaise = currentBet*2;
		if (lastMove != null) {
			if (lastMove.getMove().getType() == MoveType.Check || lastMove.getMove().getType() == MoveType.Call) {
				toRaise = currentBet;
			}
		}
		return toRaise;
	}

	@Override
	public void setBlinds(Blinds blinds) {
		this.blinds = blinds;
		
	}

	@Override
	public Blinds getBlinds() {
		return blinds;
	}

	@Override
	public void newHand() {
		view.newHand();
	}

	@Override
	public void setPlayersForView(List<Player> players) {
		view.setPlayers(players);
	}

	@Override
	public void showdown() {
		view.showdown();
	}

	@Override
	public void refreshStacks() {
		view.refreshStacks();
	}

	@Override
	public void alert(String text) {
		view.alert(text);
	}

	@Override
	public void addNewPlayerReaction(Player player, Player opponent,
			int playerContribution, int opponentContribution, int pot, Blinds blinds,
			int noPhase, List<Card> communityCards, PlayerMove playerMove,
			PlayerMove opponentReaction) {
		writeToReactionFile(player, opponent, playerContribution, opponentContribution, pot, blinds, noPhase, communityCards, playerMove, opponentReaction);
		try {
			AIEngine.teachPerceptron(player, opponent, playerContribution, opponentContribution, pot, blinds, noPhase, communityCards, playerMove, opponentReaction);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void writeToReactionFile(Player player, Player opponent,
			int playerContribution, int opponentContribution, int pot, Blinds blinds,
			int noPhase, List<Card> communityCards, PlayerMove playerMove,
			PlayerMove opponentReaction) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(new File(
					reactionFileName), true));
			out.write(((player.getStack() + playerContribution) / blinds.getBigBlind()) + ";" 
					+ ((opponent.getStack() + opponentContribution) / blinds.getBigBlind()) + ";"
					+ (playerContribution * 100)
					/ (player.getStack() + playerContribution) + ";"
					+ (opponentContribution * 100)
					/ (opponent.getStack() + opponentContribution) + ";" + pot
					+ ";" + noPhase + ";"
					+ HandStrenght.getHandStrenght(player.getHand()) + ";"
					+ HeadsupUtils.getTableStrenght(communityCards) + ";"
					+ playerMove + ";" + opponentReaction + ";" + currentMinimalRaise + "\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void refreshRaiseVariable(int minimalRaise, Player player,
			Player opponent) {
		try {
			AIEngine.refreshRaiseVariable(minimalRaise, Math.max(player.getStack(), opponent.getStack()));
		} catch (FuzzyException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setPlayerContribution(Player player, int contribution) {
		view.setPlayerContribution(player, contribution);
	}

}
