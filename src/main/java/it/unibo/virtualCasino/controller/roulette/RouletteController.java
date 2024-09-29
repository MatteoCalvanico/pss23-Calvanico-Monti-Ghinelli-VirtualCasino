package it.unibo.virtualCasino.controller.roulette;

import it.unibo.virtualCasino.controller.BaseController;
import it.unibo.virtualCasino.model.games.impl.roulette.Roulette;
import it.unibo.virtualCasino.model.games.impl.roulette.RouletteBet;
import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteBetTypes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class RouletteController extends BaseController{
  
  private ObservableMap<String, RouletteBet> placedBets;

  private Roulette rouletteGame;
  
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
    rouletteGame = new Roulette(this.currentPlayer);
        
    txtPlayer.setText(this.currentPlayer.getName());

    txtBalance.setText(Double.toString(this.currentPlayer.getAccount()));
  }

  public void createBet() {

    try {

      rouletteGame.bet(100, RouletteBetTypes.SPLIT, 1);

    } catch (Exception e) {

      System.out.println(e.getMessage());

    }

    placedBets = FXCollections.observableMap(rouletteGame.getBetsList());


    System.out.println("placed bets: " + placedBets.toString());
  }
}
