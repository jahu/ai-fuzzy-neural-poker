package pl.j4hu.j4poker.headsup.view.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pl.j4hu.j4poker.headsup.domain.Card;
import pl.j4hu.j4poker.headsup.domain.Hand;

public class Utils {

	private static BufferedImage cards = null;

	static {
		try {
			cards = ImageIO.read(new File("resources/img/cards.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static BufferedImage getCardImage(Card card){
		return cards.getSubimage(card.getFigure().ordinal()*73, card.getSuit().ordinal()*98, 73, 98);
	}
	
	public static BufferedImage getTwoReversedCardsImage() {
		BufferedImage image = new BufferedImage(140, 94, BufferedImage.TYPE_INT_BGR);
		Graphics2D g = image.createGraphics();
		try {
			g.drawImage(ImageIO.read(new File("resources/img/reverseCard.png")), 0, 0, null);
			g.drawImage(ImageIO.read(new File("resources/img/reverseCard.png")), 70, 0, null);
			g.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	public static BufferedImage getReversedCardImage() {
		BufferedImage image = new BufferedImage(69, 94, BufferedImage.TYPE_INT_BGR);
		Graphics2D g = image.createGraphics();
		try {
			g.drawImage(ImageIO.read(new File("resources/img/reverseCard.png")), 0, 0, null);
			g.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	public static BufferedImage getHandImage(Hand hand) {
		BufferedImage image = new BufferedImage(140, 98, BufferedImage.TYPE_INT_BGR);
		Graphics2D g = image.createGraphics();
		g.drawImage(getCardImage(hand.getHand().get(0)), 0, 0, null);
		g.drawImage(getCardImage(hand.getHand().get(1)), 71, 0, null);
		g.dispose();
		return image;
	}
	
}
