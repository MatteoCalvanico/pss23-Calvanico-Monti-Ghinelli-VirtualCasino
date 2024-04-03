package it.unibo.virtualCasino.model.games.impl.roulette;

import java.util.HashMap;
import java.util.Map;

import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.Games;
import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteBetTypes;
import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteColors;
import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteInfo;

public class Roulette implements Games{

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

        for (int i = 0; i <= RouletteInfo.NUMS_TOTAL; i++) {
            tempMap.put(
                RouletteInfo.ROULETTE_SEQUENCE[i],
                mapNumberToColorEnum(i)
            );
        }

        return tempMap;
    }

    private int[][] createRouletteTableMatrix () {
        int[][] tempMatrix = new int[RouletteInfo.TABLE_ROWS][RouletteInfo.TABLE_COLS];
        
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
            put(RouletteBetTypes.DOUBLE_STREET, 5);
            put(RouletteBetTypes.COLUMN, 2);
            put(RouletteBetTypes.DOZEN, 2);
            put(RouletteBetTypes.ODD_EVEN, 1);
            put(RouletteBetTypes.RED_BLACK, 1);
            put(RouletteBetTypes.HALF, 1);
        }};
    }
    
}
