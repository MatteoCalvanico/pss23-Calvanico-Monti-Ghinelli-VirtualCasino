package it.unibo.virtualCasino.controller.menu;

import it.unibo.virtualCasino.controller.BaseController;
import it.unibo.virtualCasino.helpers.RoutingHelper;
import it.unibo.virtualCasino.model.scoreboard.Scoreboard;
import it.unibo.virtualCasino.model.scoreboard.dtos.ScoreboardRecord;
import it.unibo.virtualCasino.view.blackjack.BlackjackView;
import it.unibo.virtualCasino.view.dice.DiceView;
import it.unibo.virtualCasino.view.menu.MenuView;
import it.unibo.virtualCasino.view.roulette.RouletteView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class GamesMenuController extends BaseController {

    @FXML
    private HBox bjScreenBox;

    @FXML
    private Button btnBlackjack;

    @FXML
    private Button btnRoulette;

    @FXML
    private Button btnBack;

    @FXML
    private HBox rlScreenBox;

    @FXML
    private Text txtBalance;

    @FXML
    private Text txtPlayer;

    @FXML
    private HBox diceScreenBox;

    @FXML
    private Button btnDice;

    @FXML
    void playBlackjack(ActionEvent event) {
        RoutingHelper.goTo(event, new BlackjackView());
    }

    @FXML
    void playRoulette(ActionEvent event) {
        RoutingHelper.goTo(event, new RouletteView());
    }

    @FXML
    void exit(ActionEvent event) {
        // TODO: go back to menu, but first ask the user if he wants to trow the dice
        // and after that save the record (only if he made a new record)

        Scoreboard.addRecord(
                new ScoreboardRecord(
                        this.currentPlayer.getName(),
                        this.currentPlayer.getAccount()));

        RoutingHelper.goTo(event, new MenuView());
    }

    @Override
    protected void setBaseController() {

        // Set the current player
        txtPlayer.setText(currentPlayer.getName());
        txtBalance.setText(Double.toString(currentPlayer.getAccount()));

        // Set screenshots
        ImageView bjScreen = getImage("blackjackScreen.png");
        bjScreen.setFitWidth(bjScreenBox.getPrefWidth());
        bjScreen.setFitHeight(bjScreenBox.getPrefHeight());
        bjScreen.setPreserveRatio(true);
        bjScreenBox.getChildren().add(bjScreen);

        ImageView rlScreen = getImage("rouletteScreen.png");
        rlScreen.setFitWidth(rlScreenBox.getPrefWidth());
        rlScreen.setFitHeight(rlScreenBox.getPrefHeight());
        rlScreen.setPreserveRatio(true);
        rlScreenBox.getChildren().add(rlScreen);
    }

    @FXML
    void goToDice(ActionEvent event) {
        RoutingHelper.goTo(event, new DiceView());

        ImageView diceScreen = getImage("dieRed1.png"); // usa il PNG che già possiedi
        diceScreen.setFitWidth(diceScreenBox.getPrefWidth());
        diceScreen.setFitHeight(diceScreenBox.getPrefHeight());
        diceScreen.setPreserveRatio(true);
        diceScreenBox.getChildren().add(diceScreen);
    }
}
