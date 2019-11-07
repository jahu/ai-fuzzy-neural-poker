package pl.j4hu.j4poker.headsup.view.gui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import pl.j4hu.j4poker.headsup.domain.Card;

public class CommunityPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3793071500900362275L;
	
	private List<CardPanel> cards = null;
	
	public CommunityPanel() {
		setLayout(new GridLayout(1,5));
		cards = new ArrayList<CardPanel>();
		for(int i=0;i<5;i++){
			cards.add(new CardPanel());
			this.add(cards.get(i));
		}
	}
	
	public void setUpCards(List<Card> tableCards){
		for(int i=0; i<tableCards.size(); i++){
			cards.get(i).setCard(tableCards.get(i));
		}
	}
	
	public void resetCommunityCards(){
		for(CardPanel panel: cards){
			panel.setReversedCard();
		}
	}
}
