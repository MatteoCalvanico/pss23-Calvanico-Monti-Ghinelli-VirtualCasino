package it.unibo.virtualCasino.controller.scoreboard;

import it.unibo.virtualCasino.controller.BaseController;
import it.unibo.virtualCasino.model.scoreboard.Scoreboard;
import it.unibo.virtualCasino.model.scoreboard.dtos.ScoreboardRecord;
import it.unibo.virtualCasino.view.menu.MenuView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ScoreboardController extends BaseController {
    @FXML
    private ListView<String> scoreboardList;

    @FXML
    private TableColumn<ScoreboardRecord, String> colName;

    @FXML
    private TableColumn<ScoreboardRecord, Double> colFinalBalance;

    @FXML
    private Button btnExit;

    @Override
    protected void setBaseController() {
        btnExit.setOnAction(event -> exit(event));

        ArrayList<ScoreboardRecord> scoreboardRecords = Scoreboard.loadScoreboard();

        for (int i = 1; i <= scoreboardRecords.size(); i++) {
            ScoreboardRecord record = scoreboardRecords.get(i - 1);

            scoreboardList
                    .getItems()
                    .add(String.format("%s° %s - Balance: $%s", i, record.playerName, record.playerBalance));
        }

        // Center text in each ListView item
        scoreboardList.setStyle("-fx-alignment: center; -fx-text: yellow;");
    }

    /**
     * Method called when the user want to exit the scorboard view
     */
    protected void exit(ActionEvent event) {
        // Save the player in the singleton class
        sendData();

        // Open the menu
        try {
            MenuView gamesView = new MenuView();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            gamesView.start(stage);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", e.getMessage());
            return;
        }
    }
}
