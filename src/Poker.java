// Dalton Avery, Josh Mallari, Nick Wade
// Group Project
// Poker Game -- Poker.java
// 4.23.2020


import java.util.ArrayList;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Poker extends Application {
	static ArrayList <Card> cardList = new ArrayList<Card>(); // ArrayList used to initialize current hand
	static Card[] currentHand = new Card[5]; // current cards in the players hand
	static ImageView[] imagesDisplayed = new ImageView[5]; // Card imageviews displayed in the game
	static Boolean[] discard = new Boolean[5]; // boolean values discerning which cards to discard
	static double bankAmount = 200; // starting bank amount
	static double betAmount = 0; // initial bet amount
	
	@Override
	public void start(Stage primStage) throws Exception {
		
		// main pane
		Pane root = new VBox(); 
		
		
		// ***********************************************************
		/*
		 * This section of code defines the betting option displayed in the game
		 * Such as the bank, bank amount, bet 1 & bet all buttons, 
		 * textfield to input unique bet
		 * This is put here in order to ensure we can pull values from
	 	 * the text field
		 */
		
		HBox bankNdBetting = new HBox(); // pane the betting nodes are added to
		Text bankText = new Text("Bank: "); // text to display word Bank:
		bankText.setFill(Color.NAVAJOWHITE); // color change
		Text bankAmountText = new Text("$"+bankAmount); // displays amount left in the bank
		bankAmountText.setFill(Color.NAVAJOWHITE); 
		bankText.setFont(Font.font(20));
		bankAmountText.setFont(Font.font(20));
		
		// Setting up radio buttons for placing bets
		HBox radioBettingButs = new HBox(); // Pane used to add radio Buttons
		radioBettingButs.setAlignment(Pos.CENTER_RIGHT);
		radioBettingButs.setPadding(new Insets(3,0,0,600));
		VBox bet = new VBox(); // used to vertically align the radio buttons
		
		// FEATURE #5
		TextField betAmtField = new TextField(); // text field for unique bets
		betAmtField.setStyle("-fx-background-color: navajowhite"); 
		RadioButton bet1 = new RadioButton("$1"); // bet $1 radio 
		RadioButton betAll = new RadioButton("MAX"); // bet entire bank radio 
		bet1.setStyle("-fx-selected-color: navajowhite");
		bet1.setStyle("-fx-text-fill: navajowhite");
		betAll.setStyle("-fx-text-fill: navajowhite");
		Text betAmtText = new Text("Enter bet:"); // on screen instruction text
		betAmtText.setFill(Color.NAVAJOWHITE);
		ToggleGroup betButs = new ToggleGroup(); // add buttons to toggle group for exclusivity
		bet1.setToggleGroup(betButs);
		betAll.setToggleGroup(betButs);
		
		// FEATURE #6
		// These actions fill in the text Box upon being clicked
		bet1.setOnMouseClicked(e -> {betAmtField.setText("1");});
		betAll.setOnMouseClicked(e -> {String temp = String.format("%.2f", bankAmount);
				betAmtField.setText(temp);});
		
		// Adding nodes to panes
		bet.getChildren().addAll(betAmtText,betAmtField,betAll,bet1);
		bet.setMaxWidth(75);
		radioBettingButs.getChildren().addAll(bet);
				
		bankNdBetting.getChildren().addAll(bankText , bankAmountText, radioBettingButs);
		
		// ***********************************************************
		// initializes beginning hand
		for (int i = 0; i<5; ++i) {
			currentHand[i] = cardList.remove(i);
			imagesDisplayed[i] = currentHand[i].getCardImageView();
		}
		//Hbox for cards
		HBox cardHBox = new HBox();
		//displays cards
		cardHBox.getChildren().addAll(imagesDisplayed[0],
				imagesDisplayed[1],
				imagesDisplayed[2],
				imagesDisplayed[3],
				imagesDisplayed[4]
				);
		// ***********************************************************
		/* This animation is not able to be viewed
		 * but is necessary when dealing with animation timing
		 * as well as when checking for a winning hand after deal animation*/
		Line phantomOutLine = new Line(0,0,300,300);
		PathTransition phantom = new PathTransition();
		phantom.setDuration(Duration.millis(2000));
		phantom.setPath(phantomOutLine);
		phantom.setNode(new Label ());
		
		// ***********************************************************
		// Adding draw and deal buttons to change the cards
		
		
		HBox DrwDlBtns = new HBox(); // Pane for adding draw and deal buttons
		
		/* This button takes all the cards on the screen, 
		 * flips them, 
		 * removes them 
		 * and brings in new ones
		 */
		Button dealBtn = new Button("Deal");
		dealBtn.setOnAction(e -> {
			for (int i=0; i<5; ++i) // sets discard to false player can reselect
				discard[i] = false;
			System.out.println(cardList.size()); // used in testing to determine amount of cards left in deck
			
			// loops through each card and activates animations
			for (int i = 0; i<5; ++i) {
				
				// flips card over
				imagesDisplayed[i].setImage(new Image("file:./card/b2fv.png"));
				
				//Animation to take cards out
				PathTransition out = new PathTransition();
				out.setNode(imagesDisplayed[i]);out.setPath(new Line(85,150,85,1000));
				out.setDuration(Duration.millis(1000)); out.play();
				
				// draws new cards from arraylist
				currentHand[i] = cardList.remove(i);
				System.out.println(currentHand[i].getCardValue() + " " + currentHand[i].getCardFace()); //used in testing
				
				// final animation to bring new cards in
				final int temp = i;
				out.setOnFinished(f -> {imagesDisplayed[temp].setImage(currentHand[temp].getCardImage());
					PathTransition in = new PathTransition();
					in.setNode(imagesDisplayed[temp]);in.setPath(new Line(85,0,85,150));
					in.setDuration(Duration.millis(1000)); in.play();
				});
				
			}
			// FEATURE #3 RESHUFFLE CARDS WHEN LESS THAT 25% ARE LEFT
			if (cardList.size()<8) {
				setDeck();
			}
		});
		
		/* This button takes all discarded cards on the screen, 
		 * removes them 
		 * and brings in new ones
		 * and then checks for a winning hand
		 */
		Button drawBtn = new Button("Draw");
		drawBtn.setOnAction(e -> {
			phantom.play();
			
			// ensures text is a double
			try {
			betAmount = Double.parseDouble(betAmtField.getText());
			}
			catch (Exception notADouble) {
				System.out.println("Wrong input, use a number");
			}
			
			//subtracts bet amount from bank and replaced bank amount text with new value
			bankAmount -= betAmount;
			bankAmountText.setText("$"+(bankAmount));
			
			
			System.out.println(cardList.size()); // used in testing
			
			// loops through cards in hand
			for (int i = 0; i<5; ++i) {
				
				// checks if cards have been selected for discard
				if (discard[i]) {
					discard[i] = false; // sets it to false in order to be reselected 
					
					// Animation used to take discarded cards out of screen
					PathTransition out = new PathTransition();
					out.setNode(imagesDisplayed[i]);out.setPath(new Line(85,150,85,1000));
					out.setDuration(Duration.millis(1000)); out.play();
					phantom.play(); // needed for timing in order to check current hand after the cards come in and not before
					
					// draws new cards
					currentHand[i] = cardList.remove(i);
					System.out.println(currentHand[i].getCardValue() + " " + currentHand[i].getCardFace());
					
					// Animation to being new cards in
					final int temp = i;
					out.setOnFinished(f -> {imagesDisplayed[temp].setImage(currentHand[temp].getCardImage());
					PathTransition in = new PathTransition();
					in.setNode(imagesDisplayed[temp]);in.setPath(new Line(85,0,85,150));
					in.setDuration(Duration.millis(1000)); in.play();
					
						});
				}
				
				// FEATURE #3 RESHUFFLE CARDS WHEN LESS THAT 25% ARE LEFT
				if (cardList.size()<8) {
					setDeck();
				}
			}
			// phantom animation used to set timing on checking the hand for winning patters then replacing bank amount with new amount
			phantom.setOnFinished(q -> {isWinningHand(); bankAmountText.setText("$"+bankAmount);});
		});
		
		// setting size/color/position and adding button nodes to the pane
		drawBtn.setMinHeight(50);drawBtn.setMinWidth(125);
		dealBtn.setMinHeight(50);dealBtn.setMinWidth(125);
		drawBtn.setStyle("-fx-background-color: navajowhite");
		dealBtn.setStyle("-fx-background-color: navajowhite");
		DrwDlBtns.setAlignment(Pos.CENTER);
		DrwDlBtns.getChildren().addAll(drawBtn, dealBtn);
		DrwDlBtns.setSpacing(50);
		
		// ***********************************************************
		
		// initializes discard array as all false, so no cards are unintentionally exchanged
		for (int i =0; i< 5; ++i) {
			discard[i] = false;
		}
		
		// ***********************************************************
		
		/*  
		 * FEATURE #7 IMAGES ARE NOW CLICKED INSTEAD OF BUTTONS
		 * FEATURE #8 CARDS ARE NOW TO BE SELECTED FOR DISCARD
		 * FEATURE #9 CARDS ARE ALSO FLIPPED UPON SELECTION
		 */
		
		// each section takes a different card, making it clickable, flips it for discard
		imagesDisplayed[0].setOnMouseClicked(e -> {
			
			if (discard[0] == false) {
				imagesDisplayed[0].setImage(new Image("file:./card/b2fv.png"));
				discard[0] = true;
			}
			else {
				discard[0] = false;
				imagesDisplayed[0].setImage(currentHand[0].getCardImage());
			}
		});
		imagesDisplayed[1].setOnMouseClicked(e -> {
			if (discard[1] == false) {
				imagesDisplayed[1].setImage(new Image("file:./card/b2fv.png"));
				discard[1] = true;
			}
			else {
				discard[1] = false;
				imagesDisplayed[1].setImage(currentHand[1].getCardImage());
			}
		});
		imagesDisplayed[2].setOnMouseClicked(e -> {
			if (discard[2] == false) {
				imagesDisplayed[2].setImage(new Image("file:./card/b2fv.png"));
				discard[2] = true;
			}
			else {
				discard[2] = false;
				imagesDisplayed[2].setImage(currentHand[2].getCardImage());
			}
		});
		imagesDisplayed[3].setOnMouseClicked(e -> {
			if (discard[3] == false) {
				imagesDisplayed[3].setImage(new Image("file:./card/b2fv.png"));
				discard[3] = true;
			}
			else {
				discard[3] = false;
				imagesDisplayed[3].setImage(currentHand[3].getCardImage());
			}
		});
		imagesDisplayed[4].setOnMouseClicked(e -> {
			if (discard[4] == false) {
				imagesDisplayed[4].setImage(new Image("file:./card/b2fv.png"));
				discard[4] = true;
			}
			else {
				discard[4] = false;
				imagesDisplayed[4].setImage(currentHand[4].getCardImage());
			}
		});
		
		// ***********************************************************
		// binds the image dimensions to be responsive 
		for (int i=0; i<5; ++i) {
			imagesDisplayed[i].fitHeightProperty().bind(root.heightProperty().subtract(200));
			imagesDisplayed[i].fitWidthProperty().bind(root.widthProperty().divide(5));
		}
		
		// ***********************************************************
		// Text telling user to click cards to discard
		Text clickCardsToDiscardText = new Text("Click Cards to Discard"); // on screen instructional text
		VBox discardTextBox = new VBox();
		clickCardsToDiscardText.setFill(Color.NAVAJOWHITE);
		discardTextBox.setAlignment(Pos.CENTER);
		discardTextBox.getChildren().add(clickCardsToDiscardText);
		
		
		
		// ***********************************************************
		// adding all panes and nodes to root VBox
		root.getChildren().addAll(cardHBox,discardTextBox,DrwDlBtns,bankNdBetting );
		//set up of scene and primary stage
		Scene scene = new Scene(root, 850,500);
		root.setStyle("-fx-background-color: green");
		primStage.setScene(scene);
		primStage.setTitle("Poker");
        
        primStage.show();
	}
	
	// This method used the checkHand.java class in order to check the winning patterns to determine the current hand
	public static void isWinningHand() {
		String hand = checkHand.checkWinner(currentHand);
		switch(hand) {
			case "RoyalFlush" : bankAmount += (betAmount * 800); break;
			case "StraightFlush": bankAmount += (betAmount * 200);break;
			case "FourOfaKind" : bankAmount += (betAmount * 30); break;
			case "FullHouse" : bankAmount += (betAmount * 20); break;
			case "Flush" : bankAmount += (betAmount * 10); break;
			case "Straight" : bankAmount += (betAmount * 7); break;
			case "ThreeOfaKind" : bankAmount += (betAmount * 5); break;
			case "TwoPair" : bankAmount += (betAmount * 3); break;
		}
	}
	
	public static void main(String args[]) {
		setDeck(); // initialize card arraylist
		launch(args);
	}
	
	// Initializes the Deck
	private static void setDeck() {
		cardList.clear();
		for (int i = 1; i<=54; ++i)
			cardList.add(new Card(i));
		shuffleDeck();
		System.out.println("Deck Set");
	}
	
	// This method simply shuffles the deck
	private static void shuffleDeck() {
		java.util.Collections.shuffle(cardList);
	}
}
