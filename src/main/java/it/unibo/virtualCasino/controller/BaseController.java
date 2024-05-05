package it.unibo.virtualCasino.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.unibo.virtualCasino.controller.singleton.PlayerHolder;
import it.unibo.virtualCasino.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * Base class that define the base methods of a controller:
 * <pre>
- initialize: when the FXML file is launch call receiveData() and setGame();
- sendData: invoked when you need to change scene, save the player in the singleton class;
- receiveData: method to take the instance of the player thanks to the singleton class
- setGame: needs to be override to set your own game.
* </pre>
*/
public abstract class BaseController implements Initializable {

    protected Player currentPlayer; 

    /**
     * Do something when the FXML file is loaded
     */
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        receiveData();
        setGame();
    }

    /**
     * Method to be invoked when you need to change scene, save the player in the singleton class
     */
    @FXML
    protected void sendData(){
        //TODO followe the example (https://dev.to/devtony101/javafx-3-ways-of-passing-information-between-scenes-1)
    }

    //Method to take the instance of the player thanks to the singleton class
    @FXML
    private void receiveData() {
        PlayerHolder holderInstance = PlayerHolder.getInstance();

        this.currentPlayer = holderInstance.getPlayerHolded();
        
        this.currentPlayer = (this.currentPlayer == null) ? new Player("PROVA") : this.currentPlayer;
    }  
    
    /**
     * Override this method to set your own game
     */
    abstract protected void setGame();
}