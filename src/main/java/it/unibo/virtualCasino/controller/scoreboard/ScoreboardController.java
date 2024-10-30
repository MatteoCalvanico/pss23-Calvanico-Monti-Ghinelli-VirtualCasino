package it.unibo.virtualCasino.controller.scoreboard;

import it.unibo.virtualCasino.controller.BaseController;
import it.unibo.virtualCasino.model.scoreboard.Scoreboard;
import it.unibo.virtualCasino.model.scoreboard.dtos.ScoreboardRecord;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.ArrayList;

public class ScoreboardController extends BaseController {
    @FXML
    private TableView<ScoreboardRecord> scoreboardTable;
    @FXML
    private TableColumn<ScoreboardRecord, String> colName;
    @FXML
    private TableColumn<ScoreboardRecord, Double> colFinalBalance;

    @Override
    protected void setBaseController() {
        Scoreboard scoreboard = new Scoreboard();

        // Retrieve records
        ArrayList<ScoreboardRecord> records = scoreboard.getScoreBoardRecords();

        // Initialize columns without PropertyValueFactory
        colName.setCellFactory(column -> new TextFieldTableCell<>());
        colFinalBalance.setCellFactory(column -> new TextFieldTableCell<>());

        // Add each record to the TableView
        records.forEach(record -> scoreboardTable.getItems().add(record));
    }

}
