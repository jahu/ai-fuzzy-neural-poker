package pl.j4hu.j4poker.headsup.view.gui;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JPanel;

import pl.j4hu.j4poker.headsup.domain.CpuPlayer;
import pl.j4hu.j4poker.headsup.domain.HumanPlayer;
import pl.j4hu.j4poker.headsup.domain.Player;
import pl.j4hu.j4poker.headsup.domain.PlayerMove;

public class TablePanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2634471879596436195L;
	
	private List<PlayerPanel> players = null;
	private boolean displayCpuCards = false;
	
	public TablePanel() throws Exception {
		setSize(800, 400);
		setLayout(new GridLayout(1, 2, 500, 10));
		players = new ArrayList<PlayerPanel>();
		
		List<Player> temp = new ArrayList<Player>();
		temp.add(new HumanPlayer("Gracz 1"));
		temp.add(new HumanPlayer("Gracz 2"));
		for(Player player: temp){
			PlayerPanel panel = null;
			panel = new PlayerPanel(player);
			add(panel);
			this.players.add(panel);
		}
		
		Properties properties = new Properties();
		properties.load(new FileInputStream("resources/config.properties"));
		displayCpuCards = Boolean.valueOf(properties.getProperty("showCpuCards"));
		
	}

	public void setUpPlayers(List<Player> players) throws Exception{
		if (players.size() != 2) {
			throw new Exception("Number of players must be 2!");
		}
		this.players.get(0).setPlayer(players.get(0));
		this.players.get(1).setPlayer(players.get(1));
		this.players.get(0).refreshName();
		this.players.get(1).refreshName();
	}
	
	public void setCards(){
		for(PlayerPanel panel: players){
			if (!(panel.getPlayer() instanceof CpuPlayer)){
				panel.setCards();
			} else {
				if (!displayCpuCards) { 
					panel.setReversedCards();
				} else {
					panel.setCards();
				}
			}
		}
	}
	
	public void refreshStacks() {
		for(PlayerPanel panel: players){
			panel.refreshStack();
		}
	}
	
	public void refreshMove(Player player, PlayerMove move) {
		PlayerPanel panel = null;
		for (PlayerPanel playerPanel: players) {
			if (playerPanel.getPlayer().equals(player)) {
				panel = playerPanel;
				break;
			}
		}
		panel.refreshLastMove(move);
	}
	
	public void clearMoveLabels() {
		for (PlayerPanel playerPanel: players) {
			playerPanel.clearLastMoveLabel();
		}
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	public void showdown() {
		for(PlayerPanel panel: players){
			panel.setCards();
		}
	}
}
