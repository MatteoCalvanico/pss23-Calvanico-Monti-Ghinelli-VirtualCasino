package it.unibo.virtualCasino.controller.dice;

import it.unibo.virtualCasino.controller.BaseController;
import it.unibo.virtualCasino.model.games.impl.dice.Dice;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;

public class DiceController extends BaseController {

    @FXML
    private ImageView imgDie1, imgDie2;
    @FXML
    private TextField txtGuess;
    @FXML
    private Button btnRoll, btnContinue;
    @FXML
    private Label lblResult;

    private Dice dice;

    @Override
    protected void setBaseController() {
        this.dice = new Dice(currentPlayer); // model
        btnRoll.setOnAction(e -> onRoll());
        btnContinue.setOnAction(this::onContinue);
    }

    // Placeholder methods
    private void onRoll() {
    }

    private void onContinue(javafx.event.ActionEvent event) {
    }
}
