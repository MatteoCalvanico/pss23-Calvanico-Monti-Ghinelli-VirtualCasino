package it.unibo.virtualCasino.controller.blackjack;

import it.unibo.virtualCasino.controller.BaseGameController;
import it.unibo.virtualCasino.helpers.AlertHelper;
import it.unibo.virtualCasino.model.games.impl.blackjack.Blackjack;
import it.unibo.virtualCasino.model.games.impl.blackjack.Card;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;

public class BlackjackController extends BaseGameController {
    @FXML
    private Button btnCard0;

    @FXML
    private Button btnCard1;

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
    private Text txtPlayer;

    @FXML
    private Text txtPlayer1;

    @FXML
    private HBox chipBox;

    @FXML
    private Button btnBet100;

    @FXML
    private Button btnBetMinus100;

    private Blackjack BJGame;

    private int currentBet = 0;

    @Override
    protected void setGame() {
        BJGame = new Blackjack(6, currentPlayer);

        deckBox1.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        // Set the dinamic text and images + disable the buttons
        updateScreen();
        btnCard0.disableProperty().set(true);
        btnStay0.disableProperty().set(true);
        btnSplit.disableProperty().set(true);

        // Set images - player deck
        ImageView cardBackView = getImage("cardBack_red2.png");
        playDeckBox.getChildren().add(cardBackView);

        // Set images - chip
        ImageView chipView = getImage("chipBlackWhite.png");
        chipBox.getChildren().add(chipView);
    }

    // IPlaceBetObj method implementation, inherits javadoc
    public void placeBet() {

        if (currentBet == 0) {
            AlertHelper.show(AlertType.WARNING, "Bet Warning", "You must bet something!");
            return;
        }
        BJGame.nextRound(); // Clean the playing field
        BJGame.bet(currentBet);

        // Disable the buttons to change the bet
        btnBet100.disableProperty().set(true);
        btnBetMinus100.disableProperty().set(true);
        btnPlaceBet.disableProperty().set(true);

        // Disable the buttons to exit
        btnExit.disableProperty().set(true);

        // The bet is set, we can start the game
        startGame();
    }

    // IPlaceBetObj method implementation, inherits javadoc
    public double getTotalBetsAmount() {
        return currentBet;
    }

    /*
     * Method to start the game
     */
    protected void startGame() {
        BJGame.receive(2); // Give 2 cards to the dealer
        BJGame.getDealerDeck().flipCard(0); // Set the first card of the dealer face up

        // Give 2 cards to the player
        BJGame.call(0);
        BJGame.call(0);

        updateScreen();
        btnCard0.disableProperty().set(false);
        btnStay0.disableProperty().set(false);
        btnSplit.disableProperty().set(false);

        // Check if the player has a blackjack
        if (BJGame.isBlackjack()) {
            showResult();
        }
    }

    /**
     * Method to get the image of the card
     * 
     * @param card the card to get the image
     * @return the file name of the card's image
     */
    protected String getCardImage(Card card) {
        String cardPath;

        if (card.isFlip()) {
            cardPath = "cardBack_red2.png";
        } else {
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

            cardPath = "card" + Character.toUpperCase(cardSeed.charAt(0)) + cardSeed.substring(1) + cardNumber + ".png";
        }

        return cardPath;
    }

    /**
     * Method to display the result of the Blackjack game and updates the screen
     * accordingly.
     */
    protected void showResult() {
        BJGame.showResult();
        updateScreen();

        // Block other buttons
        btnCard0.disableProperty().set(true);
        btnCard1.disableProperty().set(true);
        btnStay0.disableProperty().set(true);
        btnStay1.disableProperty().set(true);
        btnSplit.disableProperty().set(true);

        // Re-able the buttons to change the bet
        btnBet100.disableProperty().set(false);
        btnBetMinus100.disableProperty().set(false);
        btnPlaceBet.disableProperty().set(false);

        // Re-able the buttons to exit
        btnExit.disableProperty().set(false);
    }

    /**
     * Method to update the screen items
     */
    protected void updateScreen() {
        boolean isSplit = BJGame.getPlayerDeck(1).countCard() > 0;

        // Update texts
        txtPlayer.setText(currentPlayer.getName());
        txtBalance.setText(Double.toString(currentPlayer.getAccount()));
        txtBet0.setText(Integer.toString(currentBet));
        if (isSplit) {
            txtBet1.setText(Integer.toString(currentBet)); // Set the bet for the second deck
        } else {
            txtBet1.setText("0");
        }
        txtDeckCards0.setText(Integer.toString(BJGame.getPlayerDeck(0).countCard()));
        txtDeckCards1.setText(Integer.toString(BJGame.getPlayerDeck(1).countCard()));
        if (BJGame.getDealerDeck().size() != 0 && BJGame.getDealerDeck().checkCardFromDeck(1).isFlip()) {
            txtDeckCards2.setText(Integer.toString(BJGame.getDealerDeck().checkCardFromDeck(0).getCardNumber()));
        } else {
            txtDeckCards2.setText(Integer.toString(BJGame.getDealerDeck().countCard()));
        }

        // Update images
        // Clear previous images
        deckBox0.getChildren().clear();
        deckBox1.getChildren().clear();
        dealerDeckBox.getChildren().clear();

        // Set the images of the cards - player deck 0
        for (int i = 0; i < BJGame.getPlayerDeck(0).size(); i++) {
            Card card = BJGame.getPlayerDeck(0).checkCardFromDeck(i);
            ImageView cardView = getImage(getCardImage(card));
            deckBox0.getChildren().add(cardView);

            // Play the sound only for the last card
            if (i == BJGame.getPlayerDeck(0).size() - 1) {
                playSound("/sound/cardPlace1.mp3");
            }
        }

        // Set the images of the cards - player deck 1
        if (isSplit) {
            for (int i = 0; i < BJGame.getPlayerDeck(1).size(); i++) {
                Card card = BJGame.getPlayerDeck(1).checkCardFromDeck(i);
                ImageView cardView = getImage(getCardImage(card));
                deckBox1.getChildren().add(cardView);
            }

            playSound("/sound/cardPlace2.mp3");
        }

        // Set the images of the cards - dealer deck
        for (int i = 0; i < BJGame.getDealerDeck().size(); i++) {
            Card card = BJGame.getDealerDeck().checkCardFromDeck(i);
            ImageView cardView = getImage(getCardImage(card));
            dealerDeckBox.getChildren().add(cardView);

            // Play the sound only for the last card
            if (i == BJGame.getDealerDeck().size() - 1) {
                playSound("/sound/cardPlace3.mp3");
            }
        }
    }

    @FXML
    /*
     * Method to add 100 to the current bet
     */
    protected void addBet() {
        currentBet += 100;
        txtBet0.setText(Integer.toString(currentBet));
        playSound("/sound/chipsCollide.mp3");
    }

    @FXML
    /*
     * Method to remove 100 to the current bet
     */
    protected void removeBet() {
        if (currentBet >= 100) {
            currentBet -= 100;
            txtBet0.setText(Integer.toString(currentBet));
            playSound("/sound/chipsCollide.mp3");
        }
    }

    @FXML
    /**
     * Method called when the player don't want to take other cards - check result
     * and game ends
     */
    protected void stay() {
        if (btnStay1.disableProperty().get() == true) { // If the player stayed in the second deck we can show the
                                                        // result
            showResult();
        } else { // Else we have to wait for the player to stay in the second deck
            btnCard0.disableProperty().set(true);
            btnStay0.disableProperty().set(true);
            btnSplit.disableProperty().set(true);
        }
    }

    @FXML
    /**
     * Handle the call for the first deck
     */
    protected void handleCall0() {
        call(0);
    }

    @FXML
    /**
     * Handle the call for the second deck
     */
    protected void handleCall1() {
        call(1);
    }

    @FXML
    /**
     * Method called when the player want to take other cards
     */
    protected void call(int deckNumber) {
        BJGame.call(deckNumber);

        if (BJGame.getPlayerDeck(deckNumber).countCard() > 21) { // Check if the player has bust
            updateScreen();
            if (deckNumber == 0) {
                stay();
            } else {
                splitStay();
            }
        } else {
            updateScreen();
        }
    }

    @FXML
    /**
     * Method called when the player want to split
     */
    protected void split() {
        try {
            BJGame.split();

        } catch (RuntimeException e) {
            AlertHelper.show(AlertType.WARNING, "Split Warning", e.getMessage());
            return;

        } catch (Exception e) {
            AlertHelper.show(AlertType.ERROR, "Error", e.getMessage());
            return;
        }

        BJGame.bet(currentBet, 1); // Set the bet for the second deck
        updateScreen();
        btnCard1.disableProperty().set(false);
        btnStay1.disableProperty().set(false);
    }

    @FXML
    /**
     * Method called when the player want to stay in the second deck
     */
    protected void splitStay() {
        if (btnStay0.disableProperty().get() == true) { // If the player stayed in the first deck we can show the result
            showResult();
        } else { // Else we have to wait for the player to stay in the first deck
            btnCard1.disableProperty().set(true);
            btnStay1.disableProperty().set(true);
        }
    }
}
