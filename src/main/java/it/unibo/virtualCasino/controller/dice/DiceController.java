package it.unibo.virtualCasino.controller.dice;

import it.unibo.virtualCasino.controller.BaseController;
import it.unibo.virtualCasino.helpers.RoutingHelper;
import it.unibo.virtualCasino.model.games.impl.dice.Dice;
import it.unibo.virtualCasino.model.scoreboard.Scoreboard;
import it.unibo.virtualCasino.model.scoreboard.dtos.ScoreboardRecord;
import it.unibo.virtualCasino.view.scoreboard.ScoreboardView;
import javafx.event.ActionEvent;
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
        int guess;
        try {
            guess = Integer.parseInt(txtGuess.getText());
        } catch (NumberFormatException ex) {
            it.unibo.virtualCasino.helpers.AlertHelper.show(AlertType.WARNING, "Invalid guess",
                    "Enter a number between 2 and 12");
            return;
        }
        if (guess < 2 || guess > 12) {
            it.unibo.virtualCasino.helpers.AlertHelper.show(AlertType.WARNING, "Invalid guess", "Guess must be 2-12");
            return;
        }
        int sum = dice.roll();

        // Update dice images
        int d1 = dice.getLastRollFirstDie();
        int d2 = dice.getLastRollSecondDie();

        imgDie1.setImage(getImage("dieRed" + d1 + ".png").getImage());
        imgDie2.setImage(getImage("dieRed" + d2 + ".png").getImage());

        // Apply lucky factor
        dice.applyLuckyFactor(guess);

        lblResult.setText("Rolled " + sum + (guess == sum ? " – YOU WIN x2!" : " – You lose (balance /2)"));
        btnRoll.setDisable(true);
        btnContinue.setDisable(false);
    }

    private void onContinue(ActionEvent event) {
        Scoreboard.addRecord(
                new ScoreboardRecord(currentPlayer.getName(),
                        currentPlayer.getAccount()));
        // Salva Player nel singleton per la Scoreboard
        sendData();
        // Vai alla schermata successiva (Scoreboard)
        RoutingHelper.goTo(event, new ScoreboardView());
    }
}