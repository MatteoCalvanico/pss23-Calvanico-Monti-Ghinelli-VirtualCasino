package it.unibo.virtualCasino.model.games.impl.roulette;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.Games;
import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteColors;
import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteBase;
import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteBetTypes;

public class Roulette extends RouletteBase implements Games{

    private final int[][] tableMatrix = createRouletteTableMatrix();

    private Player currentPlayer;
    private Map<String, RouletteBet> placedBets = new HashMap<>();

    @Override
    public void bet(double amount) {
        double totalRiskedMoney = amount;
        for (Map.Entry<String, RouletteBet> entry : placedBets.entrySet()) {
          totalRiskedMoney += entry.getValue().getBetAmount();
        }
        if (totalRiskedMoney > currentPlayer.getAccount()) {
            throw new IllegalArgumentException("Total bets amount exceed account balance");
        }
    }

    @Override
    public void nextRound() {
        throw new UnsupportedOperationException("Unimplemented method 'nextRound'");
    }

    @Override
    public void showResult() {
        throw new UnsupportedOperationException("Unimplemented method 'showResult'");
    }


    public Roulette(Player player) {
        this.currentPlayer = player;
    }
    
    /**
     * Returns a copy of the current roulette table layout (2D integer array).
     *
     * @return a copy of the roulette table as an integer array
     */
    public int[][] getRouletteTable() {
        return this.tableMatrix;
    }

    /**
     * Returns a copy of the map that associates roulette numbers with their corresponding color enums.
     *
     * @return a copy of the roulette slots map
     */
    public Map<Integer, RouletteColors> getColorNumberMap() {
        return this.colorNumberMap;
    }

    public Map<String, RouletteBet> createBet(int amount, RouletteBetTypes betType, int betPositionInTable) {
        try {
            this.bet((double) amount);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }

        this.placedBets.put(
            this.generateRandomUuid(), 
            new RouletteBet(amount, betType, betPositionInTable)
        );        

        return this.placedBets;
    }

    public Map<String, RouletteBet> deleteBet(String betId) {
        this.placedBets.remove(betId);

        return this.placedBets;
    }

    private int spinRoulette() {
        SecureRandom random = new SecureRandom();
        return random.nextInt(this.NUMS_TOTAL + 1);
    }

    private int getGameProfitOrLoss(int winningNumber) {
        int profitOrLoss = 0;
      
        for (Map.Entry<String, RouletteBet> betEntry : placedBets.entrySet()) {
          RouletteBet bet = betEntry.getValue();
      
          if (bet.getWinningNumbers().contains(winningNumber)) {
            profitOrLoss += bet.getPossibleWin();
          } else {
            profitOrLoss -= bet.getBetAmount();
          }
        }
      
        return profitOrLoss;
    }

    private int[][] createRouletteTableMatrix () {
        int[][] tempMatrix = new int[this.TABLE_ROWS][this.TABLE_COLS];
        
        for (int i = 0; i < tempMatrix.length; i++) {
            for (int j = 0; j < tempMatrix[i].length; j++) {
                tempMatrix[i][j] = i + 1;
            }
        }
        
        return tempMatrix;
    }  

    private String generateRandomUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
