package pl.j4hu.j4poker.headsup.view.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class CenterPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3793071500900362275L;
	
	private JLabel leftPlayerContribution;
	private JLabel rightPlayerContribution;
	private JLabel pot;
	
	public CenterPanel() {
		setLayout(new GridLayout(1,3));
		setOpaque(false);
		leftPlayerContribution = new JLabel("0");
		pot = new JLabel("Pot: 0");
		rightPlayerContribution = new JLabel("0");
		leftPlayerContribution.setForeground(Color.white);
		rightPlayerContribution.setForeground(Color.white);
		pot.setForeground(Color.white);
		this.add(leftPlayerContribution);
		this.add(pot);
		this.add(rightPlayerContribution);
	}
	
	public void setLeftPlayerContribution(int contribution) {
		leftPlayerContribution.setText(Integer.toString(contribution));
		refreshPot();
	}
	
	public void setRightPlayerContribution(int contribution) {
		rightPlayerContribution.setText(Integer.toString(contribution));
		refreshPot();
	}
	
	private void refreshPot() {
		int contribution1 = Integer.parseInt(leftPlayerContribution.getText());
		int contribution2 = Integer.parseInt(rightPlayerContribution.getText());
		this.pot.setText("Pot: " + (contribution1 + contribution2));
	}

}
