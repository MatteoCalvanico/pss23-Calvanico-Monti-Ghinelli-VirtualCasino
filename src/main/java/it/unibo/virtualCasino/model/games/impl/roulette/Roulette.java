package it.unibo.virtualCasino.model.games.impl.roulette;

import java.util.HashMap;
import java.util.Map;

import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.Games;
import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteBetTypes;
import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteColors;

public class Roulette implements Games{

    private static final int TABLE_ROWS = 12;
    private static final int TABLE_COLS = 3;
    private static final int NUMS_TOTAL = 36;  
    private static final int[] rouletteSequence = {
        0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 
        36, 11, 30, 8, 23, 10, 5, 24, 16, 33, 1, 20, 14, 
        31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26
    };
    private final int[][] tableMatrix = createRouletteTableMatrix();
    private final Map<Integer, RouletteColors> colorNumberMap = createColorNumberMap();
    private final Map<RouletteBetTypes, Integer> betTypePayoutMap = createBetTypePayoutMap();
    
    private double currentBetAmount;
    private Player currentPlayer;

    @Override
    public void bet(double amount) {
        if (amount > currentPlayer.getAccount()) {
            throw new IllegalArgumentException("bet amount exceed account balance");
        }

        currentBetAmount = amount;
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
        currentPlayer = player;
    }

    public void bet(double amount, RouletteBet bet) {
        //
    }
    
    /**
     * Returns a copy of the current roulette table layout (2D integer array).
     *
     * @return a copy of the roulette table as an integer array
     */
    public int[][] getRouletteTable() {
        return tableMatrix;
    }

    /**
     * Returns a copy of the map that associates roulette numbers with their corresponding color enums.
     *
     * @return a copy of the roulette slots map
     */
    public Map<Integer, RouletteColors> getColorNumberMap() {
        return colorNumberMap;
    }


    private RouletteColors mapNumberToColorEnum(int number) {
        return (
            number == 0 ? 
            RouletteColors.GREEN : 
            (number % 2 == 0) ? RouletteColors.BLACK : RouletteColors.RED
        );
    }

    private Map<Integer, RouletteColors> createColorNumberMap() {
        Map<Integer, RouletteColors> tempMap = new HashMap<>(); 

        for (int i = 0; i <= NUMS_TOTAL; i++) {
            tempMap.put(
                Roulette.rouletteSequence[i],
                mapNumberToColorEnum(i)
            );
        }

        return tempMap;
    }

    private int[][] createRouletteTableMatrix () {
        int[][] tempMatrix = new int[TABLE_ROWS][TABLE_COLS];
        
        for (int i = 0; i < tempMatrix.length; i++) {
            for (int j = 0; j < tempMatrix[i].length; j++) {
                tempMatrix[i][j] = i + 1;
            }
        }
        
        return tempMatrix;

    }

    private Map<RouletteBetTypes, Integer> createBetTypePayoutMap () {
        return new HashMap<>(){{
            put(RouletteBetTypes.STRAIGHT_UP, 35);
            put(RouletteBetTypes.SPLIT, 17);
            put(RouletteBetTypes.STREET, 11);
            put(RouletteBetTypes.CORNER, 8);
            put(RouletteBetTypes.TOP_LINE, 8);
            put(RouletteBetTypes.DOUBLE_STREET, 5);
            put(RouletteBetTypes.COLUMN, 2);
            put(RouletteBetTypes.DOZEN, 2);
            put(RouletteBetTypes.ODD_EVEN, 1);
            put(RouletteBetTypes.RED_BLACK, 1);
            put(RouletteBetTypes.HALF, 1);
        }};
    }
    
}
