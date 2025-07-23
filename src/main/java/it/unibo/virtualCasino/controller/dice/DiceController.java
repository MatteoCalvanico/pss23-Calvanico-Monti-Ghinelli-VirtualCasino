package it.unibo.virtualCasino.controller.dice;

import it.unibo.virtualCasino.controller.BaseController;
import it.unibo.virtualCasino.helpers.RoutingHelper;
import it.unibo.virtualCasino.model.games.impl.dice.Dice;
import it.unibo.virtualCasino.model.scoreboard.Scoreboard;
import it.unibo.virtualCasino.model.scoreboard.dtos.ScoreboardRecord;
import it.unibo.virtualCasino.view.scoreboard.ScoreboardView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

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
    private Text lblRolled;
    @FXML
    private Text lblOutcome;

    private Dice dice;

    @Override
    protected void setBaseController() {
        this.dice = new Dice(currentPlayer);

        // initial images showing face "1"
        imgDie1.setImage(getImage("dieRed1.png").getImage());
        imgDie2.setImage(getImage("dieRed1.png").getImage());

        txtPlayer.setText(currentPlayer.getName());
        txtBalance.setText(String.format("%.2f", currentPlayer.getAccount()));

        btnRoll.setOnAction(e -> onRoll());
        btnContinue.setOnAction(this::onContinue);
        btnContinue.setDisable(true);
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

        btnRoll.setDisable(true); // disables until animation finishes
        lblRolled.setText(""); // clear previous messages
        lblOutcome.setText("");

        playSound("/sound/dieShuffle.mp3");

        final int FRAMES = 24; // number of "shake" frames
        final int INTERVAL_MS = 80; // speed (ms)

        Timeline shake = new Timeline();
        for (int i = 0; i < FRAMES; i++) {
            shake.getKeyFrames().add(
                    new KeyFrame(Duration.millis(i * INTERVAL_MS), ev -> {
                        int f1 = ThreadLocalRandom.current().nextInt(1, 7);
                        int f2 = ThreadLocalRandom.current().nextInt(1, 7);
                        imgDie1.setImage(getImage("dieRed" + f1 + ".png").getImage());
                        imgDie2.setImage(getImage("dieRed" + f2 + ".png").getImage());
                    }));
        }

        shake.setOnFinished(ev -> {

            int sum = dice.roll();

            // Update dice images
            int d1 = dice.getLastRollFirstDie();
            int d2 = dice.getLastRollSecondDie();

            imgDie1.setImage(getImage("dieRed" + d1 + ".png").getImage());
            imgDie2.setImage(getImage("dieRed" + d2 + ".png").getImage());

            playSound("/sound/dieThrow.mp3");

            // Update the balance
            dice.applyLuckyFactor(guess);

            txtBalance.setText(String.format("%.2f", currentPlayer.getAccount()));

            lblRolled.setText("Rolled: " + sum);
            if (guess == sum) {
                lblOutcome.setText("YOU WIN  \u00D72");
            } else {
                lblOutcome.setText("YOU LOSE  (balance / 2)");
            }

            btnContinue.setDisable(false);
        });
        shake.play();
    }

    private void onContinue(ActionEvent event) {
        Scoreboard.addRecord(
                new ScoreboardRecord(currentPlayer.getName(),
                        currentPlayer.getAccount()));
        // Saves Player in the singleton for the Scoreboard
        sendData();
        // Go to the next screen (Scoreboard)
        RoutingHelper.goTo(event, new ScoreboardView());
    }
}
