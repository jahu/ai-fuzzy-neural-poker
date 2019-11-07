package pl.j4hu.j4poker.headsup.view.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import pl.j4hu.j4poker.headsup.domain.CpuPlayer;
import pl.j4hu.j4poker.headsup.domain.Deck;
import pl.j4hu.j4poker.headsup.domain.Hand;
import pl.j4hu.j4poker.headsup.domain.HumanPlayer;
import pl.j4hu.j4poker.headsup.domain.Player;
import pl.j4hu.j4poker.headsup.service.AIEngineException;

public class Test {

	static MainFrame frame = null;
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
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
					frame = new MainFrame();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.setVisible(true);
				}
		});
		setUp();
	}
	
	public static void setUp() throws Exception {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.getConsole().append("START");
		List<Player> p1 = new ArrayList<Player>();
		p1.add(new HumanPlayer("Jacek"));
		p1.add(new HumanPlayer("CPU"));
		frame.getTablePanel().setUpPlayers(p1);
		Thread.sleep(1000);
		Deck deck = new Deck();
		p1.get(0).setHand(new Hand(deck));
		p1.get(1).setHand(new Hand(deck));
		frame.getTablePanel().setCards();
	}
}
