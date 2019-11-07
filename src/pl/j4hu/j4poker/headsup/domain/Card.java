package pl.j4hu.j4poker.headsup.domain;

public class Card {
	private Figure figure;
	private Suit suit;
	
	public Figure getFigure() {
		return figure;
	}
	public void setFigure(Figure figure) {
		this.figure = figure;
	}
	public Suit getSuit() {
		return suit;
	}
	public void setSuit(Suit suit) {
		this.suit = suit;
	}
	
	public Card(Figure figure, Suit suit){
		this.figure = figure;
		this.suit = suit;
	}
	
	public String toString(){
		return figure + " of " + suit;
	}
}
