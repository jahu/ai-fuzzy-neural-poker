package pl.j4hu.j4poker.headsup.view;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import pl.j4hu.j4poker.headsup.domain.Card;
import pl.j4hu.j4poker.headsup.domain.CpuPlayer;
import pl.j4hu.j4poker.headsup.domain.MoveType;
import pl.j4hu.j4poker.headsup.domain.Player;
import pl.j4hu.j4poker.headsup.domain.PlayerMove;
import pl.j4hu.j4poker.headsup.view.gui.MainFrame;

public class GuiHeadsUpView implements HeadsUpView {

	private MainFrame mainFrame;
	
	public GuiHeadsUpView() throws Exception {
		
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					mainFrame = new MainFrame();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mainFrame.setVisible(true);
			}
		});

	}
	
	@Override
	public void log(String text) {
		mainFrame.getConsole().append(text + "\n");
		System.out.println(text);
		
	}

	@Override
	public MoveType getNextMoveType(Player player) {
		return mainFrame.getButtonPanel().getClickedMove();
	}

	@Override
	public int getRaiseAmount(Player player, int minimalRaise) {
		return mainFrame.getScrollbarPanel().getValue();
	}

	@Override
	public void displayCommunityCards(List<Card> communityCards) {
		mainFrame.getCommunityPanel().setUpCards(communityCards);
		mainFrame.getTablePanel().clearMoveLabels();
	}

	@Override
	public void newHand() {
		mainFrame.getTablePanel().setCards();
		mainFrame.getCommunityPanel().resetCommunityCards();
		mainFrame.getTablePanel().refreshStacks();
		mainFrame.getTablePanel().clearMoveLabels();
		mainFrame.getCenterPanel().setLeftPlayerContribution(0);
		mainFrame.getCenterPanel().setRightPlayerContribution(0);
	}

	@Override
	public void setPlayers(List<Player> players) {
		try {
			mainFrame.getTablePanel().setUpPlayers(players);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setPossibleMoves(List<MoveType> possibleMoves) {
		mainFrame.getButtonPanel().setPossibleMoves(possibleMoves);
	}

	@Override
	public void setMinimalRaiseAmount(int minRaise, Player player) {
		mainFrame.getScrollbarPanel().setMinimumValue(minRaise);
		mainFrame.getScrollbarPanel().setMaximumValue(player.getStack());
	}

	@Override
	public void showdown() {
		mainFrame.getTablePanel().showdown();
	}

	@Override
	public void refreshStacks() {
		mainFrame.getTablePanel().refreshStacks();
	}

	@Override
	public void refreshMove(Player player, PlayerMove move) {
		mainFrame.getTablePanel().refreshMove(player, move);
	}

	@Override
	public void alert(String text) {
		mainFrame.showDialogBox(text);
	}

	@Override
	public void setPlayerContribution(Player player, int contribution) {
		if (player instanceof CpuPlayer) {
			mainFrame.getCenterPanel().setRightPlayerContribution(contribution);
		} else {
			mainFrame.getCenterPanel().setLeftPlayerContribution(contribution);
		}
	}

}
