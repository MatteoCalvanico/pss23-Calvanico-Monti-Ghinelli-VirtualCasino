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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    void playBlackjack(ActionEvent event) {
        RoutingHelper.goTo(event, new BlackjackView());
    }

    @FXML
    void playRoulette(ActionEvent event) {
        RoutingHelper.goTo(event, new RouletteView());
    }

    // GamesMenuController.java
    @FXML
    void exit(ActionEvent event) {

        /* 1. popup di conferma */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit the casino");
        alert.setHeaderText("Are you sure you want to leave?");
        alert.setContentText(
                "Try your luck with the dice, double your balance OR risk cutting it in half!");

        ButtonType btnYesDice = new ButtonType("Roll the dice!");
        ButtonType btnNoExit = new ButtonType("Exit now");
        ButtonType btnCancel = ButtonType.CANCEL;

        alert.getButtonTypes().setAll(btnYesDice, btnNoExit, btnCancel);

        alert.showAndWait().ifPresent(choice -> {
            if (choice == btnYesDice) {
                /* --- Vai al gioco dei dadi --- */
                sendData(); // salvo il Player nel singleton
                RoutingHelper.goTo(event, new DiceView());

            } else if (choice == btnNoExit) {
                /* --- Nessun dado: salvo il record e torno al menu iniziale --- */
                Scoreboard.addRecord(
                        new ScoreboardRecord(
                                currentPlayer.getName(),
                                currentPlayer.getAccount()));

                RoutingHelper.goTo(event, new MenuView());
            }
            /* Se CANCEL non faccio nulla: resta nel games-menu */
        });
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
}
