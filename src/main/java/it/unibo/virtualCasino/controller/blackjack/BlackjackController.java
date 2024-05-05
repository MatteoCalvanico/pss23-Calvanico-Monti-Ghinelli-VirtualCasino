package it.unibo.virtualCasino.controller.blackjack;

import java.net.URL;
import java.util.ResourceBundle;

import it.unibo.virtualCasino.controller.singleton.PlayerHolder;
import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.impl.blackjack.Blackjack;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class BlackjackController implements Initializable {
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

    private Player currentPlayer;


    /**Per creare un metodo e linkarlo fare:
     * 1) Creare nel tag all'interno del fxml il giusto evento, es: <Button text='Ciao' onAction='#method'/>
     * 2) Nel controller creare quel metodo con il giusto evento come parametro passato, es: public void method(Action e){ ... }
    */

    /**
     * Do something when the FXML file is loaded
     */
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        receiveData();

        Blackjack BJGame = new Blackjack(6, currentPlayer);

        
        txtPlayer.setText(currentPlayer.getName());
        txtBalance.setText(Double.toString(currentPlayer.getAccount()));
    }

    //Method to be invoked when you need to change scene, save the player in the singleton class
    @FXML
    private void sendData(){
        //TODO followe the example (https://dev.to/devtony101/javafx-3-ways-of-passing-information-between-scenes-1)
    }

    //Method to take the instance of the player thanks to the singleton class
    @FXML
    private void receiveData() {
        PlayerHolder holderInstance = PlayerHolder.getInstance();

        this.currentPlayer = holderInstance.getPlayerHolded();
        
        this.currentPlayer = (this.currentPlayer == null) ? new Player("PROVA") : this.currentPlayer;
    }
}
