package it.unibo.virtualCasino.controller;

import it.unibo.virtualCasino.abstractions.IPlaceBet;
import it.unibo.virtualCasino.helpers.AlertHelper;
import it.unibo.virtualCasino.helpers.RoutingHelper;
import it.unibo.virtualCasino.view.menu.GamesView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

/**
 * Base class that define the base methods of a controller
 */
public abstract class BaseGameController extends BaseController implements IPlaceBet {

    @FXML
    protected Button btnPlaceBet;

    @FXML
    protected Button btnExit;

    /**
     * Override this method to set your own game
     */
    abstract protected void setGame();

    /**
     * Initializes the controller when the FXML file is loaded
     */
    @Override
    protected void setBaseController() {
        btnPlaceBet.setOnAction(event -> placeBetInternal());
        setGame();
    }

    /**
     * Handles the internal logic for placing a bet, including validation.
     *
     * @param placeBetObj the object responsible for handling bet placement.
     */
    public void placeBetInternal() {
        try {
            currentPlayer.placeBet(this);
        } catch (Exception e) {
            AlertHelper.show(AlertType.WARNING, "Invalid bet amount", e.getMessage());
        }
    }

    @FXML
    /**
     * Method called when the player want to exit the game
     */
    protected void exit(ActionEvent event) {
        // Save the player in the singleton class
        sendData();

        RoutingHelper.goTo(event, new GamesView());
    }
}