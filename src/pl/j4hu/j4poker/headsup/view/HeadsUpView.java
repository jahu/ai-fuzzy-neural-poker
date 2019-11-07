package pl.j4hu.j4poker.headsup.view;

import java.util.List;

import pl.j4hu.j4poker.headsup.domain.Card;
import pl.j4hu.j4poker.headsup.domain.MoveType;
import pl.j4hu.j4poker.headsup.domain.Player;
import pl.j4hu.j4poker.headsup.domain.PlayerMove;

public interface HeadsUpView {

	void log(String text);
	void alert(String text);
	
	void setPossibleMoves(List<MoveType> possibleMoves);
	void setMinimalRaiseAmount(int minRaise, Player player);
	MoveType getNextMoveType(Player player);
	int getRaiseAmount(Player player, int minimalRaise);
	
	void displayCommunityCards(List<Card> communityCards);
	void refreshStacks();
	void refreshMove(Player player, PlayerMove move);
	void setPlayerContribution(Player player, int contribution);
	
	void setPlayers(List<Player> players);
	void newHand();
	void showdown();
	
}
