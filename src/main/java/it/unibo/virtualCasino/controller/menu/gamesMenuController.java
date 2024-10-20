package it.unibo.virtualCasino.controller.menu;

import it.unibo.virtualCasino.controller.BaseController;
import it.unibo.virtualCasino.view.blackjack.BlackjackView;
import it.unibo.virtualCasino.view.roulette.RouletteView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class gamesMenuController extends BaseController {

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
        try {
            BlackjackView BJView = new BlackjackView();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            BJView.start(stage);
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
    }

    @FXML
    void playRoulette(ActionEvent event) {
        try {
            RouletteView RLView = new RouletteView();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            RLView.start(stage);
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
    }

    @FXML
    void exit(ActionEvent event) {
        //TODO: go back to menu, but first ask the user if he wants to trow the dice and after that save the record (only if he made a new record)
    }

    @Override
    protected void setGame() {

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
