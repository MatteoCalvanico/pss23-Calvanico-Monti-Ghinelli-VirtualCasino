package it.unibo.virtualCasino.controller.scoreboard;

import it.unibo.virtualCasino.controller.BaseController;
import it.unibo.virtualCasino.helpers.RoutingHelper;
import it.unibo.virtualCasino.model.scoreboard.Scoreboard;
import it.unibo.virtualCasino.model.scoreboard.dtos.ScoreboardRecord;
import it.unibo.virtualCasino.view.menu.MenuView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;

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

            // \u00B0 is the unicode squence for the char ° that has some porability issues
            scoreboardList
                    .getItems()
                    .add(String.format("%d\u00B0 %s - Balance: $%.2f", i, record.playerName, record.playerBalance));
        }

        // Center text in each ListView item
        scoreboardList.setStyle("-fx-alignment: center; -fx-text: yellow;");
    }

    /**
     * Method called when the user want to exit the scorboard view
     */
    protected void exit(ActionEvent event) {
        RoutingHelper.goTo(event, new MenuView());
    }
}
