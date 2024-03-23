package it.unibo.virtualCasino.model.Games.Impl;

import java.util.HashMap;
import java.util.Map;

import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.Games.Games;
import it.unibo.virtualCasino.model.Games.Impl.Utils.RouletteColors;

public class Roulette implements Games{

    private static final int TABLE_ROWS = 12;
    private static final int TABLE_COLS = 3;
    private static final int NUMS_TOTAL = 36;
    private static final int[] rouletteSequence = {
        0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 
        36, 11, 30, 8, 23, 10, 5, 24, 16, 33, 1, 20, 14, 
        31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26
    };
    
    private double currentBet;
    private Player currentPlayer;
    private final int[][] tableMatrix;
    private final Map<Integer, RouletteColors> rouletteSlots;


    public Roulette(Player player) {
        Map<Integer, RouletteColors> tempMap = new HashMap<>(); 
        int[][] tempMatrix = new int[TABLE_ROWS][TABLE_COLS];

        for (int i = 0; i <= NUMS_TOTAL; i++) {
            tempMap.put(
                Roulette.rouletteSequence[i],
                this.mapNumberToColorEnum(i)
            );
        }
        
        for (int i = 0; i < tempMatrix.length; i++) {
            for (int j = 0; j < tempMatrix[i].length; j++) {
                tempMatrix[i][j] = i + 1;
            }
        }

        this.rouletteSlots = tempMap;
        this.tableMatrix = tempMatrix;
        this.currentPlayer = player;
    }

    @Override
    public void bet(double amount) {
        if (amount > currentPlayer.getAccount()) {
            throw new IllegalArgumentException("bet amount exceed account balance");
        }

        this.currentBet = amount;
    }

    @Override
    public void nextRound() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'nextRound'");
    }

    @Override
    public void showResult() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showResult'");
    }

    private RouletteColors mapNumberToColorEnum(int number) {
        return (
            number == 0 ? 
            RouletteColors.GREEN : 
            (number % 2 == 0) ? RouletteColors.BLACK : RouletteColors.RED
        );
    }

    public int[][] getRouletteTable() {
        return this.tableMatrix;
    }
    
}
