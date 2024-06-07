package it.unibo.virtualCasino.controller.roulette;

import it.unibo.virtualCasino.controller.BaseController;
import it.unibo.virtualCasino.model.games.impl.roulette.Roulette;
import it.unibo.virtualCasino.model.games.impl.roulette.RouletteBet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class RouletteController extends BaseController{
  
  private ObservableMap<String, RouletteBet> placedBets;
  
  @FXML
  private Button btnCreateBet;

  @FXML
  private Button btnDeleteBet;

  @FXML
  private Button btnSpeenWheel;

  @FXML
  private Text txtPlayer;

  @FXML
  private Text txtBalance;

  @Override
  protected void setGame() {
    Roulette RouletteGame = new Roulette(currentPlayer);


    placedBets = FXCollections.observableHashMap();
        
    txtPlayer.setText(currentPlayer.getName());
    txtBalance.setText(Double.toString(currentPlayer.getAccount()));


  }
}
