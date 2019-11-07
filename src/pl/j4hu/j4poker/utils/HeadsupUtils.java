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
import pl.j4hu.j4poker.headsup.domain.Suit;

public class HeadsupUtils {
	
	private static Map<Figure, Double> figureStrenght;
	
	static {
		figureStrenght = new HashMap<Figure, Double>();
		for (Figure figure: Figure.values()) {
			switch (figure) {
			case Ace:
				figureStrenght.put(figure, 14.0);
				break;
			case Eight:
				figureStrenght.put(figure, 8.0);
				break;
			case Five:
				figureStrenght.put(figure, 5.0);
				break;
			case Four:
				figureStrenght.put(figure, 4.0);
				break;
			case Jack:
				figureStrenght.put(figure, 11.0);
				break;
			case King:
				figureStrenght.put(figure, 13.0);
				break;
			case Nine:
				figureStrenght.put(figure, 9.0);
				break;
			case Queen:
				figureStrenght.put(figure, 12.0);
				break;
			case Seven:
				figureStrenght.put(figure, 7.0);
				break;
			case Six:
				figureStrenght.put(figure, 6.0);
				break;
			case Ten:
				figureStrenght.put(figure, 10.0);
				break;
			case Three:
				figureStrenght.put(figure, 3.0);
				break;
			case Two:
				figureStrenght.put(figure, 2.0);
				break;
			default:
				break;
			}
		}
	}
	
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
			Collections.sort(tmp, new CardComparatorAscending());
//			System.out.println("High card: "+checkHighCard(tmp));
//			System.out.println("One pair: "+checkOnePair(tmp));
//			System.out.println("Two pairs: "+checkTwoPairs(tmp));
//			System.out.println("Set: "+checkSet(tmp));
//			System.out.println("Straight: "+checkStraight(tmp));
//			System.out.println("Flush: "+checkFlush(tmp));
//			System.out.println("Full house: "+checkFullHouse(tmp));
//			System.out.println("Four of a kind: "+checkFourOfaKind(tmp));
//			System.out.println("Straight flush: "+checkStraightFlush(tmp));
//			System.out.println("Royal straight flush: "+checkRoyalStraightFlush(tmp));
			
			List<Card> cards = null;
			result = new Result();
			if((cards=checkRoyalStraightFlush(tmp))!=null){
				cards = extendToFiveCards(cards, tmp);
				result.setCards(cards);
				result.setLayout(Layout.RoyalStraightFlush);
			} else if((cards=checkStraightFlush(tmp))!=null) {
				cards = extendToFiveCards(cards, tmp);
				result.setCards(cards);
				result.setLayout(Layout.StraightFlush);
			} else if((cards=checkFourOfaKind(tmp))!=null){
				cards = extendToFiveCards(cards, tmp);
				result.setCards(cards);
				result.setLayout(Layout.FourOfaKind);
			} else if((cards=checkFullHouse(tmp))!=null){
				cards = extendToFiveCards(cards, tmp);
				result.setCards(cards);
				result.setLayout(Layout.FullHouse);
			} else if((cards=checkFlush(tmp))!=null){
				cards = extendToFiveCards(cards, tmp);
				result.setCards(cards);
				result.setLayout(Layout.Flush);
			} else if((cards=checkStraight(tmp))!=null){
				cards = extendToFiveCards(cards, tmp);
				result.setCards(cards);
				result.setLayout(Layout.Straight);
			} else if((cards=checkSet(tmp))!=null){
				cards = extendToFiveCards(cards, tmp);
				result.setCards(cards);
				result.setLayout(Layout.ThreeOfaKind);
			} else if((cards=checkTwoPairs(tmp))!=null){
				cards = extendToFiveCards(cards, tmp);
				result.setCards(cards);
				result.setLayout(Layout.TwoPairs);
			} else if((cards=checkOnePair(tmp))!=null){
				cards = extendToFiveCards(cards, tmp);
				result.setCards(cards);
				result.setLayout(Layout.OnePair);
			} else {
				cards = checkHighCard(tmp);
				cards = extendToFiveCards(cards, tmp);
				result.setCards(cards);
				result.setLayout(Layout.HighCard);
			}
		}
		return result;
	}
	private static List<Card> checkHighCard(List<Card> cards){
		List<Card> tmp = new ArrayList<Card>();
		tmp.add(cards.get(cards.size()-1));
		return tmp;
	}
	private static List<Card> checkOnePair(List<Card> cards){
		Card prev = null;
		List<Card> tmp = new ArrayList<Card>();
		for (Card card: cards){
			if(prev==null){
				prev = card;
			} else {
				if (prev.getFigure() == card.getFigure()){
					tmp.add(prev);
					tmp.add(card);
					return tmp;
				} else {
					prev = card;
				}
			}
		}
		return null;
	}
	private static List<Card> checkTwoPairs(List<Card> cards){
		List<Card> firstPair = checkOnePair(cards);
		List<Card> secondPair = null;
		if (firstPair != null){
			List<Card> tmp = new ArrayList<Card>();
			for(Card card: cards){
				if (!firstPair.contains(card)){
					tmp.add(card);
				}
			}
			secondPair = checkOnePair(tmp);
			if(secondPair != null){
				for(Card card: secondPair){
					firstPair.add(card);
				}
				return firstPair;
			}
		}
		return null;
	}
	private static List<Card> checkSet(List<Card> cards){
		
		List<Card> twoPairs = checkTwoPairs(cards);
		if (twoPairs != null) {
			List<Card> tmp = new ArrayList<Card>();
			for(Card card: cards){
				if (!twoPairs.contains(card)){
					tmp.add(card);
				}
			}
			Figure fig1 = twoPairs.get(0).getFigure();
			Figure fig2 = twoPairs.get(2).getFigure();
			for(Card card: tmp){
				if(card.getFigure()==fig1) {
					twoPairs.remove(3);
					twoPairs.remove(2);
					twoPairs.add(card);
					return twoPairs;
				} else if (card.getFigure() == fig2) {
					twoPairs.remove(1);
					twoPairs.remove(0);
					twoPairs.add(card);
					return twoPairs;
				}
			}
		} else {
			List<Card> firstPair = checkOnePair(cards);
			if (firstPair != null){
				List<Card> tmp = new ArrayList<Card>();
				for(Card card: cards){
					if (!firstPair.contains(card)){
						tmp.add(card);
					}
				}
				Figure fig = firstPair.get(0).getFigure();
				for(Card card: tmp){
					if(card.getFigure()==fig){
						firstPair.add(card);
						return firstPair;
					}
				}
			}
		}
		return null;
	}
	private static List<Card> checkFullHouse(List<Card> cards){
		List<Card> twoPairs = checkTwoPairs(cards);
		List<Card> set = checkSet(cards);
		if(twoPairs != null && set != null){
			for(Card card: set){
				if(!twoPairs.contains(card)){
					twoPairs.add(card);
					return twoPairs;
				}
			}
		}
		return null;
	}
	private static List<Card> checkStraight(List<Card> cards){
		//System.out.println(cards);
		List<Card> tmp = new ArrayList<Card>();
/*		Card prev = null;
		for(Card card: cards){
			if(tmp.size()==0){
				tmp.add(card);
				prev = card;
			} else {
				if(prev.getFigure().ordinal()+1<Figure.values().length && 
						card.getFigure() == Figure.values()[prev.getFigure().ordinal()+1]){
					prev = card;
					tmp.add(card);					
				} else {
					if(tmp.size()>=5){
						break;
					} else {
						tmp.clear();
					}
				}
			}
		}
		if(tmp.size()>=5){
			int toDelete = tmp.size()-5;
			for(int i=0;i<toDelete;i++){
				tmp.remove(0);
			}
			return tmp;
		}*/
		int[] temp = new int [13];
		for (Card card: cards) {
			temp[card.getFigure().ordinal()] = 1;
		}
		for (int i=0; i<temp.length;i++) {
			//System.out.print(temp[i] + " ");
		}
		int count = 0;
		for (int i=0; i<=temp.length; i++) {
			if (count == 5) {
				break;
			}
			if (temp[i % temp.length] == 1) {
				count++;
			} else {
				count = 0;
			}
		}
		if (count == 5){ 
			//System.out.println("jest");
			return tmp;
		} else {
			return null;
		}
	
	}
	
	private static List<Card> checkFlush(List<Card> cards){
		Suit tmp = null;
		for (Suit suit : Suit.values()) {
			int count = 0;
			for (Card card : cards) {
				if (card.getSuit() == suit) {
					count++;
				}
			}
			if (count >=5){
				tmp = suit;
				break;
			}
		}
		if(tmp!=null){
			List<Card> tmpCards = new ArrayList<Card>();
			for(Card card: cards){
				if(card.getSuit()==tmp){
					tmpCards.add(card);
				}
			}
			if(tmpCards.size()>=5){
				int toDelete = tmpCards.size()-5;
				for(int i=0;i<toDelete;i++){
					tmpCards.remove(0);
				}
			}
			return tmpCards;
		}
		return null;
	}
	
	private static List<Card> checkFourOfaKind2(List<Card> cards){
		List<Card> set = checkSet(cards);
		if(set!=null){
			Figure fig = set.get(0).getFigure();
			List<Card> tmp = new ArrayList<Card>();
			for(Card card: cards){
				if(!set.contains(card)){
					tmp.add(card);
				}
			}
			for(Card card: tmp){
				if(card.getFigure() == fig){
					set.add(card);
					return set;
				}
			}
		}
		return null;
	}
	
	private static List<Card> checkFourOfaKind(List<Card> cards){
		Map<Figure, Integer> count = new HashMap<Figure, Integer>();
		for (Card card: cards) {
			Figure nextFigure = card.getFigure();
			if (count.containsKey(nextFigure)) {
				Integer tmp = count.get(nextFigure);
				count.put(nextFigure, tmp+1);
			} else {
				count.put(nextFigure, 1);
			}
		}
		for (Figure figure: count.keySet()) {
			if (count.get(figure).equals(4)) {
				List<Card> four = new ArrayList<Card>();
				for (Card card: cards) {
					if (card.getFigure().equals(figure)) {
						four.add(card);
					}
				}
				if (four.size() == 4) {
					return four;
				}
			}
		}
		return null;
	}
	
	private static List<Card> checkStraightFlush(List<Card> cards){
		List<Card> tmp = new ArrayList<Card>();
		Card prev = null;
		for(Card card: cards){
			if(tmp.size()==0){
				tmp.add(card);
				prev = card;
			} else {
				if(prev.getFigure().ordinal()+1<Figure.values().length && 
						card.getFigure() == Figure.values()[prev.getFigure().ordinal()+1]){
					prev = card;
					tmp.add(card);					
				} else {
					if(tmp.size()>=5){
						break;
					} else {
						tmp.clear();
					}
				}
			}
		}
		for(int i=tmp.size()-5;i>=0;i--){
			List<Card> flush = checkFlush(tmp.subList(i, 5+i));
			if(flush!=null){
				return flush;
			}
		}
		return null;
	}
	private static List<Card> checkRoyalStraightFlush(List<Card> cards){
		List<Card> tmp = checkStraightFlush(cards);
		if(tmp!=null){
			for(Card card: tmp){
				if(card.getFigure() == Figure.Ace){
					return tmp;
				}
			}
		}
		return null;
	}
	private static List<Card> extendToFiveCards(List<Card> cards, List<Card> table){
		List<Card> tmp = new ArrayList<Card>();
		List<Card> tmp2 = new ArrayList<Card>();
		tmp2.addAll(cards);
		tmp.addAll(table);
		for(Card card: tmp2){
			if(tmp.contains(card)){
				tmp.remove(card);
			}
		}
		Collections.sort(tmp, new CardComparatorDescending());
		while(tmp2.size()<5){
			tmp2.add(tmp.remove(0));
		}
		return tmp2;
	}
	
	public static int compareCards(List<Card> a, List<Card> b, Layout layout){
		List<Card> tmp1 = null;
		List<Card> tmp2 = null;
		int result = 0;
		switch (layout) {
		case Flush:
			tmp1 = checkFlush(a);
			tmp2 = checkFlush(b);
			Collections.sort(tmp1, new CardComparatorDescending());
			Collections.sort(tmp2, new CardComparatorDescending());
			return tmp1.get(0).getFigure().compareTo(tmp2.get(0).getFigure());
		case FourOfaKind:
			tmp1 = checkFourOfaKind(a);
			tmp2 = checkFourOfaKind(b);
			int fourComp = tmp1.get(0).getFigure().compareTo(tmp2.get(0).getFigure());
			if(fourComp==0){
				Card card1 = null;
				Card card2 = null;
				for(Card card: a){
					if(!tmp1.contains(card)){
						card1 = card;
					}
				}
				for(Card card: b){
					if(!tmp2.contains(card)){
						card2 = card;
					}
				}
				return card1.getFigure().compareTo(card2.getFigure());			//TODO REPAIR NULL POINTER EXCEPTION (chyba zrobione juz :P)
			} else {
				return fourComp;
			}
		case FullHouse:
			List<Card> full1  = checkFullHouse(a);
			tmp1 = checkSet(full1);
			List<Card> full2  = checkFullHouse(b);
			tmp2 = checkSet(full2);
			int setComp = tmp1.get(0).getFigure().compareTo(tmp2.get(0).getFigure());
			if(setComp==0){
				for(Card card: tmp1){
					if(full1.contains(card))full1.remove(card);
				}
				for(Card card: tmp2){
					if(full2.contains(card))full2.remove(card);
				}
				return full1.get(0).getFigure().compareTo(full2.get(0).getFigure());
			} else {
				return setComp;
			}
		case HighCard:
			tmp1 = a;
			tmp2 = b;
			Collections.sort(tmp1, new CardComparatorDescending());
			Collections.sort(tmp2, new CardComparatorDescending());
			for (int i=0; i<tmp1.size(); i++) {
				int res = tmp1.get(i).getFigure().compareTo(tmp2.get(i).getFigure());
				if (res != 0) {
					return res;
				}
			}
			return 0;
		case OnePair:
			tmp1 = checkOnePair(a);
			tmp2 = checkOnePair(b);
			int pairComp = tmp1.get(0).getFigure().compareTo(tmp2.get(0).getFigure());
			if(pairComp==0){
				List<Card> cards1 = new ArrayList<Card>();
				cards1.addAll(a);
				List<Card> cards2 = new ArrayList<Card>();
				cards2.addAll(b);
				Collections.sort(cards1, new CardComparatorDescending());
				Collections.sort(cards2, new CardComparatorDescending());
				for(Card card: tmp1){
					if(cards1.contains(card)){
						cards1.remove(card);
					}
				}
				for(Card card: tmp2){
					if(cards2.contains(card)){
						cards2.remove(card);
					}
				}
				for(int i=0;i<cards1.size();i++){
					int tmp = cards1.get(i).getFigure().compareTo(cards2.get(i).getFigure());
					if(tmp!=0){
						return tmp;
					}
				}
				return 0;
			} else {
				return pairComp;
			}
		case RoyalStraightFlush:
			//System.out.println("DWA ROYALE");
			return 0;
		case Straight:
			tmp1 = checkStraight(a);
			tmp2 = checkStraight(b);
			Collections.sort(tmp1, new CardComparatorDescending());
			Collections.sort(tmp2, new CardComparatorDescending());
			return tmp1.get(0).getFigure().compareTo(tmp2.get(0).getFigure());
		case StraightFlush:
			tmp1 = checkStraightFlush(a);
			tmp2 = checkStraightFlush(b);
			Collections.sort(tmp1, new CardComparatorDescending());
			Collections.sort(tmp2, new CardComparatorDescending());
			return tmp1.get(0).getFigure().compareTo(tmp2.get(0).getFigure());
		case ThreeOfaKind:
			tmp1 = checkSet(a);
			tmp2 = checkSet(b);
			int setComparator = tmp1.get(0).getFigure().compareTo(tmp2.get(0).getFigure());
			if(setComparator==0){
				List<Card> cards1 = new ArrayList<Card>();
				cards1.addAll(a);
				List<Card> cards2 = new ArrayList<Card>();
				cards2.addAll(b);
				Collections.sort(cards1, new CardComparatorDescending());
				Collections.sort(cards2, new CardComparatorDescending());
				for(Card card: tmp1){
					if(cards1.contains(card)){
						cards1.remove(card);
					}
				}
				for(Card card: tmp2){
					if(cards2.contains(card)){
						cards2.remove(card);
					}
				}
				for(int i=0;i<2;i++){
					int tmp = cards1.get(i).getFigure().compareTo(cards2.get(i).getFigure());
					if(tmp!=0){
						return tmp;
					}
				}
				return 0;
			} else {
				return setComparator;
			}
		case TwoPairs:
			tmp1 = checkTwoPairs(a);
			tmp2 = checkTwoPairs(b);
			Collections.sort(tmp1, new CardComparatorDescending());
			Collections.sort(tmp2, new CardComparatorDescending());
			int firstPair = tmp1.get(0).getFigure().compareTo(tmp2.get(0).getFigure());
			int secondPair = tmp1.get(2).getFigure().compareTo(tmp2.get(2).getFigure());
			if(firstPair==0){
				if(secondPair==0){
					List<Card> cards1 = new ArrayList<Card>();
					cards1.addAll(a);
					List<Card> cards2 = new ArrayList<Card>();
					cards2.addAll(b);
					Collections.sort(cards1, new CardComparatorDescending());
					Collections.sort(cards2, new CardComparatorDescending());
					for(Card card: tmp1){
						if(cards1.contains(card)){
							cards1.remove(card);
						}
					}
					for(Card card: tmp2){
						if(cards2.contains(card)){
							cards2.remove(card);
						}
					}
					return -cards1.get(0).getFigure().compareTo(cards2.get(0).getFigure());
				} else {
					return secondPair;
				}
			} else {
				return firstPair;
			}
		}
		return result;
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
			return -o1.getFigure().compareTo(o2.getFigure());
		}
		
	};
	
	public static double getTableStrenght(List<Card> communityCards) {
		double value = 0.0;
		for (Card card: communityCards) {
			value += figureStrenght.get(card.getFigure());
		}
		return value / communityCards.size();	
	}
}

