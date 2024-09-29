package it.unibo.virtualCasino.controller.blackjack;

import it.unibo.virtualCasino.controller.BaseController;
import it.unibo.virtualCasino.model.games.impl.blackjack.Blackjack;
import it.unibo.virtualCasino.model.games.impl.blackjack.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class BlackjackController extends BaseController {
    @FXML
    private Button btnCard0;

    @FXML
    private Button btnCard1;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnSplit;

    @FXML
    private Button btnStay0;

    @FXML
    private Button btnStay1;

    @FXML
    private HBox dealerDeckBox;

    @FXML
    private HBox deckBox0;

    @FXML
    private HBox deckBox1;

    @FXML
    private HBox playDeckBox;

    @FXML
    private Text txtBalance;

    @FXML
    private Text txtBet0;

    @FXML
    private Text txtBet1;

    @FXML
    private Text txtDeckCards0;

    @FXML
    private Text txtDeckCards1;

    @FXML
    private Text txtDeckCards2;

    @FXML
    private Text txtInsurance;

    @FXML
    private Text txtPlayer;

    @FXML
    private Text txtPlayer1;

    @FXML
    private HBox chipBox;

    @FXML
    private Button btnBet100;

    @FXML 
    private Button btnBetMinus100;

    @FXML
    private Button btnSetBet;


    private Blackjack BJGame;

    private int currentBet = 0;

    @Override
    protected void setGame() {
        BJGame = new Blackjack(6, currentPlayer);

        // Set the player name and balance
        txtPlayer.setText(currentPlayer.getName());
        txtBalance.setText(Double.toString(currentPlayer.getAccount()));

        // Set images - player deck
        Image cardBack = new Image(getClass().getResourceAsStream("/sprite/cards/cardBack_red2.png"));
        ImageView cardBackView = new ImageView(cardBack); // We gonna put this in the HBox
        playDeckBox.getChildren().add(cardBackView);

        // Set images - chip
        Image chip = new Image(getClass().getResourceAsStream("/sprite/chips/chipBlackWhite.png"));
        ImageView chipView = new ImageView(chip); // We gonna put this in the HBox
        chipBox.getChildren().add(chipView);
    }

    /*
    * Method to start the game
    */
    protected void startGame(){
        BJGame.receive(2);         // Give 2 cards to the dealer
        BJGame.getDealerDeck().flipCard(0); // Set the first card of the dealer face up
        txtDeckCards2.setText(Integer.toString(BJGame.getDealerDeck().checkCardFromDeck(0).getCardNumber())); 

        // Give 2 cards to the player
        BJGame.call(0);           
        BJGame.call(0);   
        txtDeckCards0.setText(Integer.toString(BJGame.getPlayerDeck(0).countCard()));

        // Set the images of the cards - player deck
        for(int i = 0; i < BJGame.getPlayerDeck(0).size(); i++){
            Card card = BJGame.getPlayerDeck(0).checkCardFromDeck(i);
            Image cardImage = getCardImage(card);
            ImageView cardView = new ImageView(cardImage);
            deckBox0.getChildren().add(cardView);
        }

        // Set the images of the cards - dealer deck
        for(int i = 0; i < BJGame.getDealerDeck().size(); i++){
            Card card = BJGame.getDealerDeck().checkCardFromDeck(i);
            Image cardImage = getCardImage(card);
            ImageView cardView = new ImageView(cardImage);
            dealerDeckBox.getChildren().add(cardView);
        }

        // Check if the player has a blackjack
        if (BJGame.isBlackjack()) {
            BJGame.showResult();
        }
    }

    /**
    * Method to get the image of the card
    * @param card the card to get the image
    * @return the image of the card
    */
    protected Image getCardImage(Card card){
        Image cardImage;
        
        if(card.isFlip()){
            cardImage = new Image(getClass().getResourceAsStream("/sprite/cards/cardBack_red2.png"));
        }
        else{
            String cardNumber;
            switch (card.getCardName()) {
                case "JACK":
                    cardNumber = "J";
                    break;

                case "QUEEN":
                    cardNumber = "Q";
                    break;

                case "KING":
                    cardNumber = "K";
                    break;
                
                case "ACE":
                    cardNumber = "A";
                    break;
            
                default:
                    cardNumber = String.valueOf(card.getCardNumber());
                    break;
            }

            String cardSeed = card.getCardSeed().toString().toLowerCase();

            cardImage = new Image(getClass().getResourceAsStream("/sprite/cards/card" + Character.toUpperCase(cardSeed.charAt(0)) + cardSeed.substring(1) + cardNumber + ".png"));
        }

        return cardImage;
    }

    @FXML
    /*
    * Method to add 100 to the current bet
    */
    protected void addBet(){
        currentBet += 100;
        txtBet0.setText(Integer.toString(currentBet));
    }

    @FXML
    /*
    * Method to remove 100 to the current bet
    */
    protected void removeBet(){
        if(currentBet >= 100){
            currentBet -= 100;
            txtBet0.setText(Integer.toString(currentBet));
        }
    }

    @FXML
    /*
    * Method to set the ultimate bet
    * 
    * !!! The balance don't change until the game is over !!!
    */
    protected void setBet(){
        
        if (currentBet == 0) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Bet Warning");
            alert.setHeaderText(null);
            alert.setContentText("You must bet something!");
            alert.showAndWait();
            return;
            
        }

        try {
            BJGame.bet(currentBet);

            // Disable the buttons to change the bet
            btnBet100.disableProperty().set(true); 
            btnBetMinus100.disableProperty().set(true);
            btnSetBet.disableProperty().set(true);

            // The bet is set, we can start the game
            startGame();

        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Bet Warning");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
