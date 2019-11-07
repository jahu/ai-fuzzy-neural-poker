package pl.j4hu.j4poker.headsup.view.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.j4hu.j4poker.headsup.domain.MoveType;
import pl.j4hu.j4poker.headsup.domain.Player;
import pl.j4hu.j4poker.headsup.domain.PlayerMove;

public class PlayerPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3654081142316501922L;
	
	private HandPanel handPanel = null;
	private Player player = null;
	private JLabel stackLabel = null;
	private JLabel lastMoveLabel = null;
	private JLabel playerName = null;
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setCards(){
		handPanel.setCards(player.getHand());
	}
	
	public void setReversedCards() {
		handPanel.setReversedCards();
	}
	
	public void refreshStack() {
		stackLabel.setText("Stack: " + Integer.toString(player.getStack()));
	}
	
	public void refreshLastMove(PlayerMove move) {
		if (move.getMove().getType().equals(MoveType.Raise)) {
			lastMoveLabel.setText(move.getMove().getType().toString() + " " + move.getMove().getAmount());
		} else {
			lastMoveLabel.setText(move.getMove().getType().toString());
		}
	}
	
	public void clearLastMoveLabel() {
		lastMoveLabel.setText("");
	}
	
	public PlayerPanel(Player player) {
		this.player = player;
		setForeground(Color.white);
		setLayout(new GridLayout(4,1));
		setOpaque(false);
		
		playerName = new JLabel(player.getName());
		playerName.setForeground(Color.white);
		handPanel = new HandPanel();
		
		add(playerName);
		add(handPanel);
		
		stackLabel = new JLabel("Stack: 0");
		stackLabel.setForeground(Color.white);
		add(stackLabel);
		
		lastMoveLabel = new JLabel("");
		lastMoveLabel.setForeground(Color.white);
		add(lastMoveLabel);
	}
	
	public void refreshName() {
		playerName.setText(player.getName());
	}

}
