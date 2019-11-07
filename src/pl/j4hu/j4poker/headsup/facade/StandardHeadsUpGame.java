package pl.j4hu.j4poker.headsup.facade;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import pl.j4hu.j4poker.headsup.controller.HeadsUpController;
import pl.j4hu.j4poker.headsup.domain.Blinds;
import pl.j4hu.j4poker.headsup.domain.Card;
import pl.j4hu.j4poker.headsup.domain.CpuPlayer;
import pl.j4hu.j4poker.headsup.domain.Deck;
import pl.j4hu.j4poker.headsup.domain.Hand;
import pl.j4hu.j4poker.headsup.domain.Phase;
import pl.j4hu.j4poker.headsup.domain.Player;
import pl.j4hu.j4poker.headsup.domain.PlayerMove;
import pl.j4hu.j4poker.headsup.domain.Position;
import pl.j4hu.j4poker.headsup.domain.Result;
import pl.j4hu.j4poker.utils.HeadsupUtils;

public class StandardHeadsUpGame implements HeadsUpGame, Runnable {

	private HeadsUpController controller;
	private Player humanPlayer;
	private Player cpuPlayer;
	private Map<Player, Position> position;
	private Map<Player, Integer> payinMap;
	private Map<Player, Integer> totalPayinMap;
	
	private boolean isPlayerAllIn = false;
	private Player allInPlayer = null;
	
	private List<Card> communityCards;
	private Blinds blinds;
	
	private int noHandsForBlindsChange;
	
	private int pot;
	private int bet;
	
	private Thread thread;
	
	private boolean learningEnabled = false;
	private boolean displayStatistics = false;
	
	int noGames = 0;
	int noGamesWinPlayer1 = 0;
	int noGamesWinPlayer2 = 0;
	
	@Override
	public void newGame(int initialStack, Player humanPlayer, Player cpuPlayer) {
		this.humanPlayer = humanPlayer;
		this.cpuPlayer = cpuPlayer;
		this.humanPlayer.setStack(initialStack);
		this.cpuPlayer.setStack(initialStack);
		position = new HashMap<Player, Position>();
		payinMap = new HashMap<Player, Integer>();
		payinMap.put(humanPlayer, 0);
		payinMap.put(cpuPlayer, 0);
		totalPayinMap = new HashMap<Player, Integer>();
		totalPayinMap.put(humanPlayer, 0);
		totalPayinMap.put(cpuPlayer, 0);
		position.put(this.humanPlayer, Position.BigBlind);
		position.put(this.cpuPlayer, Position.Dealer);
		communityCards = new ArrayList<Card>();
		blinds = new Blinds(initialStack / 100, (initialStack * 2) / 100);
		
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("resources/config.properties"));
			learningEnabled = Boolean.valueOf(properties.getProperty("learningEnable"));
			displayStatistics = Boolean.valueOf(properties.getProperty("displayStatistics"));
			noHandsForBlindsChange = Integer.parseInt(properties.getProperty("noHandsForBlindsChange"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void start(HeadsUpController controller) {
		this.controller = controller;
		controller.setBlinds(blinds);
		thread = new Thread(this);
		thread.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void stop() {
		thread.stop();
		controller.log("Game thread has been stopped");
	}

	@Override
	public void run() {	
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		List<Player> players = new ArrayList<Player>();
		players.add(humanPlayer);
		players.add(cpuPlayer);
		controller.setPlayersForView(players);
		controller.log("New game has been started. Good luck!");
		Deck deck = null;
		Player nextPlayer = null;
		PlayerMove lastMove = null;
		boolean isHandEnded = false;
		isPlayerAllIn = false;
		int noMoves = 0;
		int phase = 0;
		boolean opponentMadeMove = false;
		boolean firstMoveInPhase = false;
		PlayerMove opponentLastMove = null;
		while (true) {
			if (humanPlayer.getStack() <= 0 || cpuPlayer.getStack() <= 0) {
				break;
			}
			phase = 0;
			//controller.log("\nNEW HAND\n");
			communityCards = new ArrayList<Card>();
			cleanTotalPayInMap();
			isPlayerAllIn = false;
			allInPlayer = null;
			switchPositions();
			isHandEnded = false;
			lastMove = null;
			pot = 0;
			deck = new Deck();
			humanPlayer.setHand(new Hand(deck));
			cpuPlayer.setHand(new Hand(deck));
			controller.newHand();
			controller.refreshRaiseVariable(blinds.getBigBlind(), humanPlayer, cpuPlayer);
			noGames++;
			
			if (noGames % noHandsForBlindsChange == 0) {
				blinds.increaseBlinds();
			}
			
			//controller.log(humanPlayer.toString());
			//controller.log(cpuPlayer.toString());
			//controller.log(blinds.toString());
			//controller.log("Current pot: " + pot + "\n");
			
			if (position.get(humanPlayer) == Position.Dealer) {
				nextPlayer = humanPlayer;
			} else {
				nextPlayer = cpuPlayer;
			}
			getBlinds();
			
			if (displayStatistics) {
				System.out.println("Number of games: " + noGames);
				System.out.println("Number of " + humanPlayer.getName() + " wins: " + noGamesWinPlayer1);
				System.out.println("Number of " + cpuPlayer.getName() + " wins: " + noGamesWinPlayer2);
				System.out.println("Stack of " + humanPlayer.getName() + ": " + humanPlayer.getStack());
				System.out.println("Stack of " + cpuPlayer.getName() + ": " + cpuPlayer.getStack());
				if (humanPlayer.getStack() > cpuPlayer.getStack()) {
					System.out.println("Winner: " + humanPlayer.getName());
				} else {
					System.out.println("Winner: " + cpuPlayer.getName());
				}
			}
			
			// PRE-FLOP
			noMoves = 0;
			while (phase <= 3 && !isHandEnded) {
				opponentMadeMove = false;
				firstMoveInPhase = true;
				bet = blinds.getBigBlind();
				while((!isBetsBalanced() || noMoves<2) && !isHandEnded) {
					if (firstMoveInPhase) {
						lastMove = null;
						firstMoveInPhase = false;
					}
					if (isPlayerAllIn && allInPlayer.equals(nextPlayer)) {
						//controller.log(nextPlayer.getName() + " is All-In. Move skipped");
					} else {
						//controller.log("Next move: " + nextPlayer.getName());
						PlayerMove move = null;
						try {
							move = controller.getMove(nextPlayer, getOtherPlayer(nextPlayer), communityCards, pot, bet, 
									blinds.getBigBlind(), lastMove, toPhase(phase), totalPayinMap.get(nextPlayer).intValue(),
									totalPayinMap.get(getOtherPlayer(nextPlayer)).intValue());
						} catch (Exception exception) {
							exception.printStackTrace();
						}
						lastMove = move;
						if (nextPlayer.equals(cpuPlayer)) {
							opponentLastMove = move;
							opponentMadeMove = true;
						}
						if (nextPlayer.equals(humanPlayer)) {
							if (opponentMadeMove) {
								opponentMadeMove = false;
								if (learningEnabled) {
									controller.addNewPlayerReaction(cpuPlayer, humanPlayer, totalPayinMap.get(cpuPlayer),
										totalPayinMap.get(humanPlayer), pot, blinds, phase, communityCards, opponentLastMove, lastMove);
								}
							}
						}
						//controller.log(lastMove.toString());
						switch (lastMove.getMove().getType()) {
						case Call:
							addCashToPotFromPlayer(nextPlayer, bet - payinMap.get(nextPlayer));
							break;
						case Check:
							break;
						case Fold:
							isHandEnded = true;
							nextPlayer = switchPlayers(nextPlayer);
							playerWin(nextPlayer, null);
							break;
						case Raise:
							bet = lastMove.getMove().getAmount();
							addCashToPotFromPlayer(nextPlayer, lastMove.getMove().getAmount() - payinMap.get(nextPlayer));
							break;
						}
						
					}
					//controller.log("Current pot: " + pot + "\n");
					nextPlayer = switchPlayers(nextPlayer);
					noMoves += 1;
				}
				cleanPayInMap();
				noMoves = 0;
				if (phase < 3 && !isHandEnded) {
					switch(phase) {
					case 0:
						// FLOP
						deck.drawCard();
						for (int i=0; i<3; i++) {
							communityCards.add(deck.drawCard());
						}
						break;
					default:
						deck.drawCard();
						communityCards.add(deck.drawCard());
					}
					nextPlayer = getBigBlindPlayer();
					controller.showCommunityCards(communityCards);
				} else if (!isHandEnded){
					showdown();
				}
				phase++;
			}
		}
		controller.refreshStacks();
		controller.alert("END OF GAME");
		if (displayStatistics) {
			System.out.println("Number of games: " + noGames);
			System.out.println("Number of " + humanPlayer.getName() + " wins: " + noGamesWinPlayer1);
			System.out.println("Number of " + cpuPlayer.getName() + " wins: " + noGamesWinPlayer2);
			if (humanPlayer.getStack() > cpuPlayer.getStack()) {
				System.out.println("Winner: " + humanPlayer.getName());
			} else {
				System.out.println("Winner: " + cpuPlayer.getName());
			}
		}
	}
	
	private Phase toPhase(int phase) {
		switch (phase) {
		case 0: return Phase.PreFlop;
		case 1: return Phase.Flop;
		case 2: return Phase.Turn;
		case 3: return Phase.River;
		default: return null;
		}
	}

	private void switchPositions() {
		if (position.get(humanPlayer) == Position.Dealer) {
			position.put(humanPlayer, Position.BigBlind);
			position.put(cpuPlayer, Position.Dealer);
		} else {
			position.put(this.humanPlayer, Position.Dealer);
			position.put(this.cpuPlayer, Position.BigBlind);
		}
	}
	
	private Player switchPlayers(Player lastPlayer) {
		if (lastPlayer == humanPlayer) {
			return cpuPlayer;
		} else {
			return humanPlayer;
		}
	}
	
	private void getBlinds() {
		if (position.get(humanPlayer) == Position.Dealer) {
			addCashToPotFromPlayer(humanPlayer, blinds.getSmallBlind());
			addCashToPotFromPlayer(cpuPlayer, blinds.getBigBlind());
		} else {
			addCashToPotFromPlayer(humanPlayer, blinds.getBigBlind());
			addCashToPotFromPlayer(cpuPlayer, blinds.getSmallBlind());
		}
	}
	
	private void addCashToPotFromPlayer(Player player, int amount) {
		if (player.getStack() <= amount) {
			amount = player.getStack();
			isPlayerAllIn = true;
		}
		pot+=player.removeCash(amount);
		Integer currentAmount = payinMap.get(player);
		currentAmount+=amount;
		payinMap.put(player, currentAmount);
		Integer totalCurrentAmount = totalPayinMap.get(player);
		totalCurrentAmount += amount;
		totalPayinMap.put(player, totalCurrentAmount);
		controller.refreshStacks();
		controller.setPlayerContribution(player, totalCurrentAmount);
		if (player.getStack() <= 0) {
			isPlayerAllIn = true;
			allInPlayer = player;
		}
	}
	
	private boolean isBetsBalanced() {
		if (!isPlayerAllIn) {
			if (payinMap.get(humanPlayer).intValue() == payinMap.get(cpuPlayer).intValue()) {
				return true;
			} else {
				return false;
			}
		} else {
			Player otherPlayer = null;
			if (allInPlayer.equals(humanPlayer)) {
				otherPlayer = cpuPlayer;
			} else {
				otherPlayer = humanPlayer;
			}
			int allInPlayerPayedIn = payinMap.get(humanPlayer).intValue();
			int otherPlayerPayedIn = payinMap.get(otherPlayer).intValue();
			if (otherPlayerPayedIn >= allInPlayerPayedIn) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	private void playerWin(Player player, Result result) {
		player.addCash(pot);
/*		controller.log("Player " + player.getName() + " wins!");
		controller.log(humanPlayer.toString());
		controller.log(cpuPlayer.toString());
		controller.log("#############################################################");*/
		if (player.equals(humanPlayer)) {
			noGamesWinPlayer1++;
		} else {
			noGamesWinPlayer2++;
		}
		if (result != null) {
			controller.alert("Player " + player.getName() + " wins!\nWinner's layout: " + result.getLayout());
		} else {
			controller.alert("Player " + player.getName() + " wins!\nOpponent folded");
		}
	}
	
	private void split() {
		humanPlayer.addCash(pot/2);
		cpuPlayer.addCash(pot/2);
		controller.log("SPLIT");
		controller.log(humanPlayer.toString());
		controller.log(cpuPlayer.toString());
		controller.log("#############################################################");
		controller.alert("SPLIT");
	}
	
	private void showdown() {
		controller.showdown();
		try {
			Result result1 = HeadsupUtils.checkLayout(humanPlayer.getHand(), communityCards);
			controller.log(humanPlayer.getName() + ": " + result1.toString());
			Result result2 = HeadsupUtils.checkLayout(cpuPlayer.getHand(), communityCards);
			controller.log(cpuPlayer.getName() + ": " + result2.toString());
			int result = result1.compareTo(result2);
			if (result > 0) {
				playerWin(humanPlayer, result1);
			} else if (result < 0) {
				playerWin(cpuPlayer, result2);
			} else {
				split();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void cleanPayInMap() {
		payinMap.clear();
		payinMap.put(humanPlayer, 0);
		payinMap.put(cpuPlayer, 0);
	}
	
	private void cleanTotalPayInMap() {
		totalPayinMap.clear();
		totalPayinMap.put(humanPlayer, 0);
		totalPayinMap.put(cpuPlayer, 0);
	}
	
	private Player getBigBlindPlayer() {
		if (position.get(humanPlayer) == Position.Dealer) {
			return cpuPlayer;
		} else {
			return humanPlayer;
		}
	}
	
	private Player getOtherPlayer(Player player) {
		if (player.equals(humanPlayer)) {
			return cpuPlayer;
		} else {
			return humanPlayer;
		}
	}

}
