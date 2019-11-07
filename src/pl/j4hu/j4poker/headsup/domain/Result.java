package pl.j4hu.j4poker.headsup.domain;

import java.util.List;

import pl.j4hu.j4poker.utils.HeadsupUtils;

public class Result implements Comparable<Result> {
	private List<Card> cards;
	private Layout layout;
	public List<Card> getCards() {
		return cards;
	}
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	public Layout getLayout() {
		return layout;
	}
	public void setLayout(Layout layout) {
		this.layout = layout;
	}
	public String toString(){
		return layout + " : "+cards;
	}
	
	@Override
	public int compareTo(Result o) {
		int layoutComp = this.layout.compareTo(o.layout);
		if(layoutComp<0){
			return -1;
		} else if(layoutComp>0){
			return 1;
		} else {
			int cardsComp = HeadsupUtils.compareCards(this.cards, o.cards, this.layout);
			//System.out.println(cardsComp);
			return cardsComp;
		}
	}
}
