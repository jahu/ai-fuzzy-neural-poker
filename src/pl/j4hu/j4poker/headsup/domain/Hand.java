package pl.j4hu.j4poker.headsup.domain;

import java.util.ArrayList;
import java.util.List;

public class Hand {
	private List<Card> hand;

	public List<Card> getHand() {
		return hand;
	}
	 
	public Hand(Deck deck){
		hand = new ArrayList<Card>();
		hand.add(deck.drawCard());
		hand.add(deck.drawCard());
	}
	
	public String toString(){
		return "("+hand.get(0) +" , "+hand.get(1)+")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hand == null) ? 0 : hand.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hand other = (Hand) obj;
		if (hand == null) {
			if (other.hand != null)
				return false;
		} else if (!hand.equals(other.hand))
			return false;
		return true;
	}

	public void setHand(List<Card> hand) {
		this.hand = hand;
	}
	
}
