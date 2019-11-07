package pl.j4hu.j4poker.headsup.view;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import pl.j4hu.j4poker.headsup.domain.Card;
import pl.j4hu.j4poker.headsup.domain.MoveType;
import pl.j4hu.j4poker.headsup.domain.Player;
import pl.j4hu.j4poker.headsup.domain.PlayerMove;

public class ConsoleHeadsUpView implements HeadsUpView {

	private Map<String, MoveType> moveMap;
	
	private static final String LABEL_GET_MOVE = "What is your next move?";
	private static final String LABEL_RAISE = "How much to raise?";
	private static final String ERROR_RAISE_MISMATCH = "You have to enter integer value!";
	
	public ConsoleHeadsUpView() {
		moveMap = new HashMap<String, MoveType>();
		moveMap.put("fold", MoveType.Fold);
		moveMap.put("call", MoveType.Call);
		moveMap.put("raise", MoveType.Raise);
		moveMap.put("check", MoveType.Check);
	}
	
	@Override
	public void log(String text) {
		//System.out.println(text);
	}

	@Override
	public MoveType getNextMoveType(Player player) {
		Scanner scanner = new Scanner(System.in);
		String line = "";
		while (!moveMap.containsKey(line)) {
			System.out.print(LABEL_GET_MOVE + " ");
			line = scanner.nextLine();
		}
		MoveType moveType = moveMap.get(line);
		return moveType;
	}

	@Override
	public int getRaiseAmount(Player player, int minimalRaise) {
		Scanner scanner = new Scanner(System.in);
		int raise = -1;
		while (raise < minimalRaise) {
			System.out.print(LABEL_RAISE + " [min: " + minimalRaise + "] ");
			try {
				raise = scanner.nextInt();
			} catch (InputMismatchException exception) {
				System.out.print(ERROR_RAISE_MISMATCH);
				scanner = new Scanner(System.in);
			}
			System.out.println();
		}
		if (raise > player.getStack()) {
			raise = player.getStack();
		}
		return raise;
	}

	@Override
	public void displayCommunityCards(List<Card> communityCards) {
		//System.out.println(communityCards);
	}

	@Override
	public void newHand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlayers(List<Player> players) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setPossibleMoves(List<MoveType> possibleMoves) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMinimalRaiseAmount(int minRaise, Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshStacks() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshMove(Player player, PlayerMove move) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void alert(String text) {
		System.out.println(text);
	}

	@Override
	public void setPlayerContribution(Player player, int contribution) {
		// TODO Auto-generated method stub
		
	}

}
