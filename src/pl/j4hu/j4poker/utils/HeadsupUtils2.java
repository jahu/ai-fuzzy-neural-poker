package pl.j4hu.j4poker.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.j4hu.j4poker.exceptions.NotEnoughCardsException;
import pl.j4hu.j4poker.headsup.domain.Card;
import pl.j4hu.j4poker.headsup.domain.Figure;
import pl.j4hu.j4poker.headsup.domain.Hand;
import pl.j4hu.j4poker.headsup.domain.Layout;
import pl.j4hu.j4poker.headsup.domain.Result;

public class HeadsupUtils2 {

	public static Result checkLayout(Hand hand, List<Card> table) throws Exception{
		Result result = null;
		if(table.size()<3){
			result = new Result();
			throw new NotEnoughCardsException();
		} else {
			List<Card> tmp = new ArrayList<Card>();
			for(Card card: hand.getHand()){
				tmp.add(card);
			}
			for(Card card: table){
				tmp.add(card);
			}
			Collections.sort(tmp, new CardComparatorDescending());
			System.out.println("Posortowane karty:");
			System.out.println(tmp);
			
			List<Card> resultCards = isOnePair(tmp);
			if (resultCards != null) {
				result = new Result();
				result.setCards(resultCards);
				result.setLayout(Layout.OnePair);
			}
		}
		
		return result;
	}
	
	private static List<Card> isOnePair(List<Card> cards) {
		Map<Figure, Integer> map = new HashMap<Figure, Integer>();
		List<Card> returnCards = null;
		for (Card card : cards) {
			Figure figure = card.getFigure();
			if (map.containsKey(figure)) {
				System.out.println("posiadam");
				Integer count = map.get(figure);
				count++;
				map.put(figure, count);
			} else {
				map.put(figure, 0);
			}
		}
		Figure onePairFigure = null;
		for (Figure figure: map.keySet()) {
			if (map.get(figure) >= 2) {
				if (onePairFigure == null) {
					onePairFigure = figure;
				} else {
					if (figure.compareTo(onePairFigure) > 0) {
						onePairFigure = figure;
					}
				}
			}
			System.out.println(map.get(figure));
		}
		if (onePairFigure != null) {
			returnCards = new ArrayList<Card>();
			for (Card card: cards) {
				if (card.getFigure().equals(onePairFigure)) {
					returnCards.add(card);
				}
			}
			while (returnCards.size() < 5) {
				for (Card card: cards) {
					if (!card.getFigure().equals(onePairFigure)) {
						returnCards.add(card);
						if (returnCards.size() >= 5) {
							break;
						}
					}
				}
			}
		}
		return returnCards;
	}
	
	public static int compareCards(List<Card> a, List<Card> b, Layout layout){
		return 0;
	}
	
	static class CardComparatorAscending implements Comparator<Card>{
		@Override
		public int compare(Card o1, Card o2) {
			return o1.getFigure().compareTo(o2.getFigure());
		}
		
	};
	static class CardComparatorDescending implements Comparator<Card>{
		@Override
		public int compare(Card o1, Card o2) {
			return -(o1.getFigure().compareTo(o2.getFigure()));
		}
		
	};
}
