package it.unibo.virtualCasino.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

/**
 * Base class that define the base methods of a controller
 */
public abstract class BaseGameController extends BaseController implements IPlaceBetObj {

    @FXML
    protected Button btnPlaceBet;

    /**
     * Initializes the controller when the FXML file is loaded
     */
    @Override
    protected void setBaseController() {
        btnPlaceBet.setOnAction(event -> placeBetInternal());
        setGame();
    }

    /**
     * Override this method to set your own game
     */
    abstract protected void setGame();

    /**
     * Handles the internal logic for placing a bet, including validation.
     *
     * @param placeBetObj the object responsible for handling bet placement.
     */
    public void placeBetInternal() {
        try {
            currentPlayer.placeBet(this);
        } catch (Exception e) {
            showAlert(AlertType.WARNING, "Invalid bet amount", e.getMessage());
        }
    }
}