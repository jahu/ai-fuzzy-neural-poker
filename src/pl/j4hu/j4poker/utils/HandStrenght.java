package pl.j4hu.j4poker.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import pl.j4hu.j4poker.headsup.domain.Figure;
import pl.j4hu.j4poker.headsup.domain.Hand;

public class HandStrenght {
	
	private static final String HAND_FILE = "resources/handStrenght.txt";
	private static Map<FigurePair, Double> strenght;
	private static Map<FigurePair, Double> suitedStrenght;
	
	private static class FigurePair {
		
		Figure figure1 = null;
		Figure figure2 = null;
		
		public FigurePair(Figure figure1, Figure figure2) {
			super();
			this.figure1 = figure1;
			this.figure2 = figure2;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((figure1 == null) ? 0 : figure1.hashCode());
			result = prime * result
					+ ((figure2 == null) ? 0 : figure2.hashCode());
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
			FigurePair other = (FigurePair) obj;
			if (figure1 != other.figure1)
				return false;
			if (figure2 != other.figure2)
				return false;
			return true;
		}
		
	}
	
	static {
		try {
			strenght = new HashMap<FigurePair, Double>();
			suitedStrenght = new HashMap<FigurePair, Double>();
			FileInputStream fstream = new FileInputStream(HAND_FILE);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(strLine);
				while (token.hasMoreElements()) {
					String cards = token.nextToken(";");
					String strn = token.nextToken(";");
					FigurePair figurePair = getFigurePair(cards);
					if (cards.contains("s")) {
						suitedStrenght.put(figurePair, Double.valueOf(strn));
					} else {
						strenght.put(figurePair, Double.valueOf(strn));
					}
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public static double getHandStrenght(Hand hand) {
		FigurePair figurePair = getFigurePair(hand);
		if (isSuited(hand)) {
			return suitedStrenght.get(figurePair);
		} else {
			return strenght.get(figurePair);
		}
	}
	
	private static boolean isSuited(Hand hand) {
		return hand.getHand().get(0).getSuit().equals(hand.getHand().get(1).getSuit());
	}
	
	private static Figure convertCharToFigure(char card) {
		Figure figure = null;
		switch (card) {
			case 'A': figure = Figure.Ace; break;
			case 'K': figure = Figure.King; break;
			case 'Q': figure = Figure.Queen; break;
			case 'J': figure = Figure.Jack; break;
			case 'T': figure = Figure.Ten; break;
			case '9': figure = Figure.Nine; break;
			case '8': figure = Figure.Eight; break;
			case '7': figure = Figure.Seven; break;
			case '6': figure = Figure.Six; break;
			case '5': figure = Figure.Five; break;
			case '4': figure = Figure.Four; break;
			case '3': figure = Figure.Three; break;
			case '2': figure = Figure.Two; break;
		}
		return figure;
	}
	
	private static FigurePair getFigurePair(String cards) {
		FigurePair figurePair = new FigurePair(convertCharToFigure(cards.charAt(0)), convertCharToFigure(cards.charAt(1)));
		return figurePair;
	}
	
	private static FigurePair getFigurePair(Hand hand) {
		FigurePair figurePair = null;
		Figure figure1 = hand.getHand().get(0).getFigure();
		Figure figure2 = hand.getHand().get(1).getFigure();
		if (figure1.compareTo(figure2) > 0) {
			figurePair = new FigurePair(figure1, figure2);
		} else {
			figurePair = new FigurePair(figure2, figure1);
		}
		return figurePair;
	}
	
}
