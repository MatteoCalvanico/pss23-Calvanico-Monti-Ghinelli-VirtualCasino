package it.unibo.virtualCasino.controller.menu;

import java.util.Optional;

import it.unibo.virtualCasino.controller.BaseController;
import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.view.menu.GamesView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenuController extends BaseController {

    @FXML
    private Button btnPlay;

    @FXML
    private Button btnScoreboard;

    @FXML
    private HBox cardBox1;

    @FXML
    private HBox cardBox2;

    @FXML
    private HBox chipBox1;

    @FXML
    private HBox chipBox2;

    @FXML
    private HBox chipBox3;

    @FXML
    private HBox chipBox4;

    @FXML
    private HBox chipBox5;

    @FXML
    private HBox diceBox1;

    @FXML
    private HBox diceBox2;

    @FXML
    private HBox diceBox3;

    @FXML
    private Text txtWelcome;

    @FXML
    void showGames(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Name");
        dialog.setGraphic(null);
        dialog.setHeaderText("Virtual Casino says:");
        dialog.setContentText("Before starting, please enter your name:");

        Optional<String> result;
        String name = "";
        boolean validName = false;

        // Take the name from the user - loop until a valid name is entered
        while (!validName) {
            result = dialog.showAndWait();
            if (result.isPresent()) {
                name = result.get().trim();
                if (!name.isEmpty()) {
                    validName = true;
                } else {
                    dialog.setHeaderText("Name cannot be empty. Please enter your name:");
                }
            } else { // User pressed "Cancel"
                return;
            }
        }

        // Save the player in the singleton class
        this.currentPlayer = new Player(name);
        sendData();

        // Open the games menu
        try {
            GamesView gamesView = new GamesView();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            gamesView.start(stage);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", e.getMessage());
            return;
        }
    }

    @FXML
    void showScoreBoard(ActionEvent event) {
        // TODO: apri la finestra del punteggio direttamente
    }

    @Override
    protected void setBaseController() {

        // Set images - chips
        ImageView chipView1 = getImage("chipBlackWhite.png");
        chipBox1.getChildren().add(chipView1);

        ImageView chipView2 = getImage("chipBlueWhite.png");
        chipBox2.getChildren().add(chipView2);

        ImageView chipView3 = getImage("chipRedWhite.png");
        chipBox3.getChildren().add(chipView3);

        ImageView chipView4 = getImage("chipGreenWhite.png");
        chipBox4.getChildren().add(chipView4);

        ImageView chipView5 = getImage("chipWhite.png");
        chipBox5.getChildren().add(chipView5);

        // Set images - cards
        ImageView cardView2 = getImage("cardHeartsQ.png");
        cardBox2.getChildren().add(cardView2);

        ImageView cardView1 = getImage("cardJoker.png");
        cardBox1.getChildren().add(cardView1);

        // Set images - dice
        ImageView diceView1 = getImage("dieRed3.png");
        diceBox1.getChildren().add(diceView1);

        ImageView diceView2 = getImage("dieRed6.png");
        diceBox2.getChildren().add(diceView2);

        ImageView diceView3 = getImage("dieRed1.png");
        diceBox3.getChildren().add(diceView3);
    }

}
