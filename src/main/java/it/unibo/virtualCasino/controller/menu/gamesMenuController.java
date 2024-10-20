package it.unibo.virtualCasino.controller.menu;

import it.unibo.virtualCasino.controller.BaseController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class gamesMenuController extends BaseController {

    @FXML
    private Text txtBalance;

    @FXML
    private Text txtPlayer;

    @Override
    protected void setGame() {

        // Set the current player
        txtPlayer.setText(currentPlayer.getName());
        txtBalance.setText(Double.toString(currentPlayer.getAccount()));
    }
}
