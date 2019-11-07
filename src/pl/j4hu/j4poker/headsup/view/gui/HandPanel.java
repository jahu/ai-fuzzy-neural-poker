package pl.j4hu.j4poker.headsup.view.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pl.j4hu.j4poker.headsup.domain.Hand;

public class HandPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2706390600757815109L;
	BufferedImage cardImage = null;
	boolean upPanel = false;
	
	public HandPanel() {
		setLayout(null);
		setReversedCards();
	}

	public void setCards(Hand hand) {
		cardImage = Utils.getHandImage(hand);
		this.repaint();
	}
	
	public void setReversedCards() {
		cardImage = Utils.getTwoReversedCardsImage();
		setOpaque(false);
		this.repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int x = 0;
		int y = 0;
		g.drawImage(cardImage, x, y, this);
	}
}
