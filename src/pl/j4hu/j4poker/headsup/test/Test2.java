package pl.j4hu.j4poker.headsup.test;

import java.util.ArrayList;
import java.util.List;

import pl.j4hu.ai.fuzzy.FuzzyController;
import pl.j4hu.j4poker.headsup.domain.Card;
import pl.j4hu.j4poker.headsup.domain.Deck;
import pl.j4hu.j4poker.headsup.domain.Figure;
import pl.j4hu.j4poker.headsup.domain.Hand;
import pl.j4hu.j4poker.headsup.domain.MoveType;
import pl.j4hu.j4poker.headsup.domain.Result;
import pl.j4hu.j4poker.headsup.domain.Suit;
import pl.j4hu.j4poker.headsup.service.ai.PokerFuzzyController;
import pl.j4hu.j4poker.utils.FuzzyLoader;
import pl.j4hu.j4poker.utils.HeadsupUtils;
import pl.j4hu.j4poker.utils.ReactionsLoader;

public class Test2 {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
/*		Hand hand = new Hand(new Deck());
		
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Figure.King, Suit.Club));
		cards.add(new Card(Figure.Queen, Suit.Club));
		hand.setHand(cards);
		
		cards = new ArrayList<Card>();
		cards.add(new Card(Figure.Ten, Suit.Diamond));
		cards.add(new Card(Figure.Six, Suit.Club));
		cards.add(new Card(Figure.Seven, Suit.Club));
		cards.add(new Card(Figure.Jack, Suit.Diamond));
		cards.add(new Card(Figure.Ten, Suit.Club));
		
		Result result = HeadsupUtils.checkLayout(hand, cards);
		System.out.println(result);*/
		
/*		FuzzyController controller = FuzzyLoader.getFuzzyControllerFromXML("resources/pokerFuzzyController.xml");
		System.out.println(controller.getKnowledgeBase().getRules());*/
		PokerFuzzyController pokerFuzzyController = new PokerFuzzyController(FuzzyLoader.getFuzzyControllerFromXML("resources/pokerFuzzyController.xml"));
		//pokerFuzzyController.getFuzzyController().getKnowledgeBase().re
		ReactionsLoader
				.getLearningVectorsFromReactionsFile(
						"reactions.txt",pokerFuzzyController
						);
		
	}

}
