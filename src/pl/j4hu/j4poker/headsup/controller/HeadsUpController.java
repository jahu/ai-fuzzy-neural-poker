package pl.j4hu.j4poker.headsup.controller;

import java.util.List;

import pl.j4hu.j4poker.headsup.domain.Blinds;
import pl.j4hu.j4poker.headsup.domain.Card;
import pl.j4hu.j4poker.headsup.domain.Phase;
import pl.j4hu.j4poker.headsup.domain.Player;
import pl.j4hu.j4poker.headsup.domain.PlayerMove;

public interface HeadsUpController {

	void log(String text);
	void alert(String text);
	PlayerMove getMove(Player player, Player opponent, List<Card> communityCards, int pot, int currentBet, 
			int bigBlind, PlayerMove lastMove, Phase phase, int playerContribution, int opponentContribution) throws Exception;
	void showCommunityCards(List<Card> communityCards);
	void refreshStacks();
	int getMinimalRaise(int currentBet, PlayerMove lastMove);
	void refreshRaiseVariable(int minimalRaise, Player player, Player opponent);
	void setPlayerContribution(Player player, int contribution);
	
	void setBlinds(Blinds blinds);
	Blinds getBlinds();
	
	void setPlayersForView(List<Player> players);
	void newHand();
	void showdown();
	
	void addNewPlayerReaction(Player player, Player opponent,
			int playerContribution, int opponentContribution, int pot, Blinds blinds,
			int noPhase, List<Card> communityCards, PlayerMove playerMove,
			PlayerMove opponentReaction);
	
}
