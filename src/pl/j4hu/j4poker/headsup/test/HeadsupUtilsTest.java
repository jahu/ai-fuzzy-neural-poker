package pl.j4hu.j4poker.headsup.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pl.j4hu.j4poker.headsup.domain.Card;
import pl.j4hu.j4poker.headsup.domain.Deck;
import pl.j4hu.j4poker.headsup.domain.Figure;
import pl.j4hu.j4poker.headsup.domain.Hand;
import pl.j4hu.j4poker.headsup.domain.HumanPlayer;
import pl.j4hu.j4poker.headsup.domain.Layout;
import pl.j4hu.j4poker.headsup.domain.Player;
import pl.j4hu.j4poker.headsup.domain.Result;
import pl.j4hu.j4poker.headsup.domain.Suit;
import pl.j4hu.j4poker.utils.HeadsupUtils;

public class HeadsupUtilsTest {

	private Player player1;
	private Player player2;
	private List<Card> communityCards;
	private Deck deck;
	
	public HeadsupUtilsTest() {
		player1 = new HumanPlayer("Player 1");
		player2 = new HumanPlayer("Player 2");
		deck = new Deck();
	}

	private void setCards(List<Card> cards1, List<Card> cards2, List<Card> communityCards) {
		Hand hand1 = new Hand(deck);
		hand1.setHand(cards1);
		player1.setHand(hand1);
		
		Hand hand2 = new Hand(deck);
		hand2.setHand(cards2);
		player2.setHand(hand2);
		
		this.communityCards = communityCards;
	}
	
	@Test
	public void testHighCard() throws Exception {
		List<Card> hand1cards = new ArrayList<Card>();
		hand1cards.add(new Card(Figure.Ace, Suit.Club));
		hand1cards.add(new Card(Figure.King, Suit.Club));
		
		List<Card> hand2cards = new ArrayList<Card>();
		hand2cards.add(new Card(Figure.King, Suit.Diamond));
		hand2cards.add(new Card(Figure.Queen, Suit.Diamond));
		
		List<Card> community = new ArrayList<Card>();
		community.add(new Card(Figure.Five, Suit.Heart));
		community.add(new Card(Figure.Four, Suit.Spade));
		community.add(new Card(Figure.Two, Suit.Spade));
		community.add(new Card(Figure.Ten, Suit.Heart));
		community.add(new Card(Figure.Seven, Suit.Club));
		
		setCards(hand1cards, hand2cards, community);
		Result result = HeadsupUtils.checkLayout(player1.getHand(), communityCards);
		assertTrue(result.getLayout().equals(Layout.HighCard));
		
		Result result2 = HeadsupUtils.checkLayout(player2.getHand(), communityCards);
		assertTrue(result2.getLayout().equals(Layout.HighCard));
		
/*		System.out.println(result.getCards());
		System.out.println(result2.getCards());*/
	
		assertTrue(HeadsupUtils.compareCards(result.getCards(), result2.getCards(), Layout.HighCard) > 0);
		assertTrue(HeadsupUtils.compareCards(result2.getCards(), result.getCards(), Layout.HighCard) < 0);
		assertTrue(HeadsupUtils.compareCards(result.getCards(), result.getCards(), Layout.HighCard) == 0);
		assertTrue(HeadsupUtils.compareCards(result2.getCards(), result2.getCards(), Layout.HighCard) == 0);
	}
	
	@Test
	public void testOnePair() throws Exception {
		List<Card> hand1cards = new ArrayList<Card>();
		hand1cards.add(new Card(Figure.Ace, Suit.Club));
		hand1cards.add(new Card(Figure.Ace, Suit.Heart));
		
		List<Card> hand2cards = new ArrayList<Card>();
		hand2cards.add(new Card(Figure.King, Suit.Diamond));
		hand2cards.add(new Card(Figure.King, Suit.Club));
		
		List<Card> community = new ArrayList<Card>();
		community.add(new Card(Figure.Five, Suit.Heart));
		community.add(new Card(Figure.Four, Suit.Spade));
		community.add(new Card(Figure.Two, Suit.Spade));
		community.add(new Card(Figure.Ten, Suit.Heart));
		community.add(new Card(Figure.Seven, Suit.Club));
		
		setCards(hand1cards, hand2cards, community);
		Result result = HeadsupUtils.checkLayout(player1.getHand(), communityCards);
		assertTrue(result.getLayout().equals(Layout.OnePair));
		
		Result result2 = HeadsupUtils.checkLayout(player2.getHand(), communityCards);
		assertTrue(result2.getLayout().equals(Layout.OnePair));
		
/*		System.out.println(result.getCards());
		System.out.println(result2.getCards());*/
	
		assertTrue(HeadsupUtils.compareCards(result.getCards(), result2.getCards(), result.getLayout()) > 0);
		assertTrue(HeadsupUtils.compareCards(result2.getCards(), result.getCards(), result.getLayout()) < 0);
		assertTrue(HeadsupUtils.compareCards(result.getCards(), result.getCards(), result.getLayout()) == 0);
		assertTrue(HeadsupUtils.compareCards(result2.getCards(), result2.getCards(), result.getLayout()) == 0);
		
		assertTrue(result.compareTo(result2) > 0);
		assertTrue(result2.compareTo(result) < 0);
		assertTrue(result.compareTo(result) == 0);
		assertTrue(result2.compareTo(result2) == 0);
	}
	
	@Test
	public void testTwoPairs() throws Exception {
		List<Card> hand1cards = new ArrayList<Card>();
		hand1cards.add(new Card(Figure.Ace, Suit.Club));
		hand1cards.add(new Card(Figure.Ace, Suit.Heart));
		
		List<Card> hand2cards = new ArrayList<Card>();
		hand2cards.add(new Card(Figure.King, Suit.Diamond));
		hand2cards.add(new Card(Figure.King, Suit.Club));
		
		List<Card> community = new ArrayList<Card>();
		community.add(new Card(Figure.Five, Suit.Heart));
		community.add(new Card(Figure.Five, Suit.Spade));
		community.add(new Card(Figure.Two, Suit.Spade));
		community.add(new Card(Figure.Ten, Suit.Heart));
		community.add(new Card(Figure.Seven, Suit.Club));
		
		setCards(hand1cards, hand2cards, community);
		Result result = HeadsupUtils.checkLayout(player1.getHand(), communityCards);
		assertTrue(result.getLayout().equals(Layout.TwoPairs));
		
		Result result2 = HeadsupUtils.checkLayout(player2.getHand(), communityCards);
		assertTrue(result2.getLayout().equals(Layout.TwoPairs));
		
/*		System.out.println(result.getCards());
		System.out.println(result2.getCards());*/
	
		assertTrue(HeadsupUtils.compareCards(result.getCards(), result2.getCards(), result.getLayout()) > 0);
		assertTrue(HeadsupUtils.compareCards(result2.getCards(), result.getCards(), result.getLayout()) < 0);
		assertTrue(HeadsupUtils.compareCards(result.getCards(), result.getCards(), result.getLayout()) == 0);
		assertTrue(HeadsupUtils.compareCards(result2.getCards(), result2.getCards(), result.getLayout()) == 0);
	}
	
	@Test
	public void testSet() throws Exception {
		List<Card> hand1cards = new ArrayList<Card>();
		hand1cards.add(new Card(Figure.Ace, Suit.Club));
		hand1cards.add(new Card(Figure.Ace, Suit.Heart));
		
		List<Card> hand2cards = new ArrayList<Card>();
		hand2cards.add(new Card(Figure.King, Suit.Diamond));
		hand2cards.add(new Card(Figure.King, Suit.Club));
		
		List<Card> community = new ArrayList<Card>();
		community.add(new Card(Figure.Ace, Suit.Spade));
		community.add(new Card(Figure.Five, Suit.Spade));
		community.add(new Card(Figure.King, Suit.Spade));
		community.add(new Card(Figure.Ten, Suit.Heart));
		community.add(new Card(Figure.Seven, Suit.Club));
		
		setCards(hand1cards, hand2cards, community);
		Result result = HeadsupUtils.checkLayout(player1.getHand(), communityCards);
		assertTrue(result.getLayout().equals(Layout.ThreeOfaKind));
		
		Result result2 = HeadsupUtils.checkLayout(player2.getHand(), communityCards);
		assertTrue(result2.getLayout().equals(Layout.ThreeOfaKind));
		
/*		System.out.println(result.getCards());
		System.out.println(result2.getCards());*/
	
		assertTrue(HeadsupUtils.compareCards(result.getCards(), result2.getCards(), result.getLayout()) > 0);
		assertTrue(HeadsupUtils.compareCards(result2.getCards(), result.getCards(), result.getLayout()) < 0);
		assertTrue(HeadsupUtils.compareCards(result.getCards(), result.getCards(), result.getLayout()) == 0);
		assertTrue(HeadsupUtils.compareCards(result2.getCards(), result2.getCards(), result.getLayout()) == 0);
	}
	
	@Test
	public void testFullHouse() throws Exception {
		List<Card> hand1cards = new ArrayList<Card>();
		hand1cards.add(new Card(Figure.Ace, Suit.Club));
		hand1cards.add(new Card(Figure.Ace, Suit.Heart));
		
		List<Card> hand2cards = new ArrayList<Card>();
		hand2cards.add(new Card(Figure.King, Suit.Diamond));
		hand2cards.add(new Card(Figure.King, Suit.Club));
		
		List<Card> community = new ArrayList<Card>();
		community.add(new Card(Figure.Ace, Suit.Spade));
		community.add(new Card(Figure.Five, Suit.Spade));
		community.add(new Card(Figure.King, Suit.Spade));
		community.add(new Card(Figure.Five, Suit.Heart));
		community.add(new Card(Figure.Seven, Suit.Club));
		
		setCards(hand1cards, hand2cards, community);
		Result result = HeadsupUtils.checkLayout(player1.getHand(), communityCards);
		assertTrue("Aces and fives full house", result.getLayout().equals(Layout.FullHouse));
		
		Result result2 = HeadsupUtils.checkLayout(player2.getHand(), communityCards);
		assertTrue("Kings and fives full house", result2.getLayout().equals(Layout.FullHouse));
		
/*		System.out.println(result.getCards());
		System.out.println(result2.getCards());*/
	
		assertTrue("Aces are stronger than Kings", HeadsupUtils.compareCards(result.getCards(), result2.getCards(), result.getLayout()) > 0);
		assertTrue("Kings are weeker than Aces", HeadsupUtils.compareCards(result2.getCards(), result.getCards(), result.getLayout()) < 0);
		assertTrue("Aces are equal", HeadsupUtils.compareCards(result.getCards(), result.getCards(), result.getLayout()) == 0);
		assertTrue("Kings are equal", HeadsupUtils.compareCards(result2.getCards(), result2.getCards(), result.getLayout()) == 0);
	}
	
	@Test
	public void testStraight() throws Exception {
		List<Card> hand1cards = new ArrayList<Card>();
		hand1cards.add(new Card(Figure.Ace, Suit.Club));
		hand1cards.add(new Card(Figure.King, Suit.Heart));
		
		List<Card> hand2cards = new ArrayList<Card>();
		hand2cards.add(new Card(Figure.King, Suit.Diamond));
		hand2cards.add(new Card(Figure.King, Suit.Club));
		
		List<Card> community = new ArrayList<Card>();
		community.add(new Card(Figure.Queen, Suit.Spade));
		community.add(new Card(Figure.Five, Suit.Spade));
		community.add(new Card(Figure.Jack, Suit.Spade));
		community.add(new Card(Figure.Ten, Suit.Heart));
		community.add(new Card(Figure.Nine, Suit.Club));
		
		setCards(hand1cards, hand2cards, community);
		Result result = HeadsupUtils.checkLayout(player1.getHand(), communityCards);
		assertTrue("Ace straight", result.getLayout().equals(Layout.Straight));
		
		Result result2 = HeadsupUtils.checkLayout(player2.getHand(), communityCards);
		//System.out.println("############################# " + result2.getLayout());
		assertTrue("Kings straight", result2.getLayout().equals(Layout.Straight));
		
/*		System.out.println(result.getCards());
		System.out.println(result2.getCards());*/
	
		assertTrue("Aces are stronger than Kings", HeadsupUtils.compareCards(result.getCards(), result2.getCards(), result.getLayout()) > 0);
		assertTrue("Kings are weeker than Aces", HeadsupUtils.compareCards(result2.getCards(), result.getCards(), result.getLayout()) < 0);
		assertTrue("Aces are equal", HeadsupUtils.compareCards(result.getCards(), result.getCards(), result.getLayout()) == 0);
		assertTrue("Kings are equal", HeadsupUtils.compareCards(result2.getCards(), result2.getCards(), result.getLayout()) == 0);
	}
	
	@Test
	public void testFlush() throws Exception {
		List<Card> hand1cards = new ArrayList<Card>();
		hand1cards.add(new Card(Figure.Ace, Suit.Spade));
		hand1cards.add(new Card(Figure.King, Suit.Heart));
		
		List<Card> hand2cards = new ArrayList<Card>();
		hand2cards.add(new Card(Figure.King, Suit.Diamond));
		hand2cards.add(new Card(Figure.King, Suit.Spade));
		
		List<Card> community = new ArrayList<Card>();
		community.add(new Card(Figure.Queen, Suit.Spade));
		community.add(new Card(Figure.Five, Suit.Spade));
		community.add(new Card(Figure.Jack, Suit.Spade));
		community.add(new Card(Figure.Ten, Suit.Heart));
		community.add(new Card(Figure.Nine, Suit.Club));
		
		setCards(hand1cards, hand2cards, community);
		Result result = HeadsupUtils.checkLayout(player1.getHand(), communityCards);
		assertTrue("Ace straight", result.getLayout().equals(Layout.Flush));
		
		Result result2 = HeadsupUtils.checkLayout(player2.getHand(), communityCards);
		assertTrue("Kings straight", result2.getLayout().equals(Layout.Flush));
		
/*		System.out.println(result.getCards());
		System.out.println(result2.getCards());*/
	
		assertTrue("Aces are stronger than Kings", HeadsupUtils.compareCards(result.getCards(), result2.getCards(), result.getLayout()) > 0);
		assertTrue("Kings are weeker than Aces", HeadsupUtils.compareCards(result2.getCards(), result.getCards(), result.getLayout()) < 0);
		assertTrue("Aces are equal", HeadsupUtils.compareCards(result.getCards(), result.getCards(), result.getLayout()) == 0);
		assertTrue("Kings are equal", HeadsupUtils.compareCards(result2.getCards(), result2.getCards(), result.getLayout()) == 0);
	}
	
	
	@Test
	public void testFourOfAKind() throws Exception {
		List<Card> hand1cards = new ArrayList<Card>();
		hand1cards.add(new Card(Figure.Ace, Suit.Spade));
		hand1cards.add(new Card(Figure.Ace, Suit.Heart));
		
		List<Card> hand2cards = new ArrayList<Card>();
		hand2cards.add(new Card(Figure.King, Suit.Diamond));
		hand2cards.add(new Card(Figure.Seven, Suit.Spade));
		
		List<Card> community = new ArrayList<Card>();
		community.add(new Card(Figure.Ace, Suit.Club));
		community.add(new Card(Figure.Ace, Suit.Diamond));
		community.add(new Card(Figure.King, Suit.Spade));
		community.add(new Card(Figure.King, Suit.Heart));
		community.add(new Card(Figure.King, Suit.Club));
		
		setCards(hand1cards, hand2cards, community);
		Result result = HeadsupUtils.checkLayout(player1.getHand(), communityCards);
		assertTrue("Ace straight", result.getLayout().equals(Layout.FourOfaKind));
		
		Result result2 = HeadsupUtils.checkLayout(player2.getHand(), communityCards);
		assertTrue("Kings straight", result2.getLayout().equals(Layout.FourOfaKind));
		
/*		System.out.println(result.getCards());
		System.out.println(result2.getCards());*/
	
		assertTrue("Aces are stronger than Kings", HeadsupUtils.compareCards(result.getCards(), result2.getCards(), result.getLayout()) > 0);
		assertTrue("Kings are weeker than Aces", HeadsupUtils.compareCards(result2.getCards(), result.getCards(), result.getLayout()) < 0);
		assertTrue("Aces are equal", HeadsupUtils.compareCards(result.getCards(), result.getCards(), result.getLayout()) == 0);
		assertTrue("Kings are equal", HeadsupUtils.compareCards(result2.getCards(), result2.getCards(), result.getLayout()) == 0);
	}
	
}
