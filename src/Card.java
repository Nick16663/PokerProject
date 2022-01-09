// Dalton Avery, Josh Mallari, Nick Wade
// Group Project
// Poker Game -- Card.java
// Used to determine suits and values of cards, as well as return images of cards.
// 4.23.2020

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card {
	
	private int cardValue;
	private String cardFace;
	private ImageView cardImageView;
	private Image cardImage;
	// Card constructor that takes the image number as an int and converts it into a value and face
	public Card(int i) {
		if (i<=52) {
			cardValue = ((i) % 13);
			if (cardValue == 0)
				cardValue = 13;
			switch((i-1)/13) {
			case 0: cardFace = "Spades"; break;
			case 1: cardFace = "Hearts"; break;
			case 2: cardFace = "Diamonds"; break;
			case 3: cardFace = "Clubs"; break;
			}
		}
		else {
			cardValue = 99;
			cardFace = "Joker";
		}
		cardImageView = new ImageView(new Image("file:./card/"+i+".png"));
		cardImage = new Image("file:./card/"+i+".png");
	}
	
	// returns the value of the card as an integer
	public int getCardValue() {
		return cardValue;
	}
	
	// returns the card face as a string
	public String getCardFace() {
		return cardFace;
	}
	
	// returns an imageview for fx using the int from the constructor
	public Image getCardImage() {
		return cardImage;
	}
	public ImageView getCardImageView() {
		return cardImageView;
	}
	// determines if the card is an ace
	public boolean isAce() {
		if (cardValue == 1)
			return true;
		return false;
	}
}
