package it.unibo.virtualCasino.controller.blackjack;

import it.unibo.virtualCasino.controller.BaseController;
import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.impl.blackjack.Blackjack;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

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


    /**Per creare un metodo e linkarlo fare:
     * 1) Creare nel tag all'interno del fxml il giusto evento, es: <Button text='Ciao' onAction='#method'/>
     * 2) Nel controller creare quel metodo con il giusto evento come parametro passato, es: public void method(Action e){ ... }
    */

    @Override
    protected void setGame() {
        Blackjack BJGame = new Blackjack(6, currentPlayer);

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
}
