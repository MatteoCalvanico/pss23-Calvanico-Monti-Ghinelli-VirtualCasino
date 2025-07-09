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
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;

public class DiceController extends BaseController {

    @FXML
    private Text txtPlayer;
    @FXML
    private Text txtBalance;
    @FXML
    private ImageView imgDie1, imgDie2;
    @FXML
    private TextField txtGuess;
    @FXML
    private Button btnRoll, btnContinue;
    @FXML
    private Label lblRolled;
    @FXML
    private Label lblOutcome;

    private Dice dice;

    @Override
    protected void setBaseController() {
        txtPlayer.setText(currentPlayer.getName());
        txtBalance.setText(String.format("%.2f $", currentPlayer.getAccount()));

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

        // Aggiorno il saldo
        dice.applyLuckyFactor(guess);

        lblRolled.setText("Rolled: " + sum);
        if (guess == sum) {
            lblOutcome.setText("YOU WIN  ×2!");
        } else {
            lblOutcome.setText("You lose  (balance / 2)");
        }

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