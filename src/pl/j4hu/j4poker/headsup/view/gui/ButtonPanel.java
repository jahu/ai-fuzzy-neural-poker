package pl.j4hu.j4poker.headsup.view.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import pl.j4hu.j4poker.headsup.domain.MoveType;

public class ButtonPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8100267152495098608L;
	private JButton btFold = null;
	private JButton btCheck = null;
	private JButton btCall = null;
	private JButton btRaise = null;
	private MoveType moveTypeClicked = null;
	
	private ButtonPanelListener listener = null;
	
	private class ButtonPanelListener implements ActionListener, Runnable {
		@Override
		public void actionPerformed(ActionEvent e) {
			String tmp = ((JButton) e.getSource()).getText();
			if (tmp.compareTo("Fold") == 0){
				moveTypeClicked = MoveType.Fold;
			} else if (tmp.compareTo("Check") == 0){
				moveTypeClicked = MoveType.Check;
			} else if(tmp.compareTo("Call") == 0) {
				moveTypeClicked = MoveType.Call;
			} else if(tmp.compareTo("Raise") == 0) {
				moveTypeClicked = MoveType.Raise;
			}
		}

		@Override
		public void run() {
			while (true) {		
			}
		}
	}
	
	public ButtonPanel() {
		listener = new ButtonPanelListener();
		Thread thread = new Thread(listener);
		thread.start();
		
		btFold = new JButton("Fold");
		btFold.setFont(new Font("sansserif",Font.BOLD,16));
		//btFold.setBackground(Color.orange);
		btFold.addActionListener(listener);
		
		btCheck = new JButton("Check");
		btCheck.setFont(new Font("sansserif",Font.BOLD,16));
		btCheck.setBackground(Color.green);
		btCheck.addActionListener(listener);
		
		btCall = new JButton("Call");
		btCall.setFont(new Font("sansserif",Font.BOLD,16));
		btCall.setBackground(Color.orange);
		btCall.addActionListener(listener);
		
		btRaise = new JButton("Raise");
		btRaise.setFont(new Font("sansserif",Font.BOLD,16));
		btRaise.setBackground(Color.red);
		btRaise.addActionListener(listener);
		
		setLayout(new GridLayout(1,4));
		add(btFold);
		add(btCheck);
		add(btCall);
		add(btRaise);
	}
	
	public void setPossibleMoves(List<MoveType> possibleMoves) {
		btFold.setEnabled(false);
		btCheck.setEnabled(false);
		btCall.setEnabled(false);
		btRaise.setEnabled(false);
		for (MoveType moveType: possibleMoves) {
			switch (moveType) {
			case Call:
				btCall.setEnabled(true);
				break;
			case Check:
				btCheck.setEnabled(true);
				break;
			case Fold:
				btFold.setEnabled(true);
				break;
			case Raise:
				btRaise.setEnabled(true);
				break;
			
			}
		}
	}
	
	public MoveType getClickedMove() {
		moveTypeClicked = null;
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
			}
		});
		thread.start();
		//while(thread.)
		while (moveTypeClicked == null){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//System.out.println(moveTypeClicked);
		return moveTypeClicked;
	}

}
