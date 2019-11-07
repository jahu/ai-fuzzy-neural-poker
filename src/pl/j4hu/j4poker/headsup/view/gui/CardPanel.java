package pl.j4hu.j4poker.headsup.view.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pl.j4hu.j4poker.headsup.domain.Card;

public class CardPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6558117485930652547L;
	BufferedImage cardImage = null;
	
	public CardPanel() {
		setLayout(null);
		setReversedCard();
		setOpaque(false);
	}
	
	public void setCard(Card card){
		cardImage = Utils.getCardImage(card);
		repaint();
	}
	
	public void setReversedCard(){
		cardImage = Utils.getReversedCardImage();
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int x = 0;
		int y = 0;
		g.drawImage(cardImage, x, y, this);
	}
}
