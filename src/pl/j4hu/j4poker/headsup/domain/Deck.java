package pl.j4hu.j4poker.headsup.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
	private List<Card> cards;

	public List<Card> getCards() {
		return cards;
	}
	
	public Deck(){
		cards = new ArrayList<Card>();
		for(Figure fig: Figure.values()){
			for(Suit suit: Suit.values()){
				cards.add(new Card(fig, suit));
			}
		}
		shuffleDeck();
	}
	
	private void shuffleDeck(){
		for(int i=0; i<50000; i++){
			int first = new Random().nextInt(cards.size());
			int second = new Random().nextInt(cards.size());
			Card tmp = cards.get(first);
			cards.set(first, cards.get(second));
			cards.set(second, tmp);
		}
	}
	
	public Card drawCard(){
		return cards.remove(0);
	}
}
