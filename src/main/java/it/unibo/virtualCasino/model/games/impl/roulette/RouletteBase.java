package it.unibo.virtualCasino.model.games.impl.roulette;

import java.util.HashMap;
import java.util.Map;

import it.unibo.virtualCasino.model.games.impl.roulette.enums.RouletteBetType;
import it.unibo.virtualCasino.model.games.impl.roulette.enums.RouletteColors;

/**
 * Provides fundamental constants and utility methods for Roulette game logic.
 */
public class RouletteBase {

    /**
     * The sequence of numbers on a Roulette wheel.
     */
    protected final int[] ROULETTE_SEQUENCE = {
            0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13,
            36, 11, 30, 8, 23, 10, 5, 24, 16, 33, 1, 20, 14,
            31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26
    };

    /**
     * The total number of numbers on the Roulette wheel.
     */
    protected final int NUMS_TOTAL = 36;

    /**
     * The number of rows on a Roulette table.
     */
    protected final int TABLE_ROWS = 12;

    /**
     * The number of columns on a Roulette table.
     */
    protected final int TABLE_COLS = 3;

    /**
     * The number of numbers covered by a double street bet.
     */
    protected final int DOUBLE_STREET_W_NUMS = 6;

    /**
     * The number of splits in a single row of a Roulette table.
     */
    protected final int SPLITS_IN_ROW = 2;

    /**
     * The maximum number of vertical splits on a Roulette table.
     */
    protected final int MAX_VERTICAL_SPLITS = 24;

    /**
     * The maximum number of horizontal splits on a Roulette table.
     */
    protected final int MAX_HORIZONTAL_SPLITS = 33;

    /**
     * A map associating bet types with their corresponding payout multipliers.
     */
    protected final Map<RouletteBetType, Integer> betTypePayoutMap = createBetTypePayoutMap();

    /**
     * A map associating numbers with their corresponding colors on the Roulette
     * wheel.
     */
    protected final Map<Integer, RouletteColors> colorNumberMap = createColorNumberMap();

    /**
     * Maps a number to its corresponding color enum.
     *
     * @param number The number to map.
     * @return The corresponding color enum (RED, BLACK, or GREEN).
     */
    private RouletteColors mapNumberToColorEnum(int number) {
        return (number == 0 ? RouletteColors.GREEN : (number % 2 == 0) ? RouletteColors.BLACK : RouletteColors.RED);
    }

    /**
     * Creates the colorNumberMap by mapping numbers to colors.
     *
     * @return The created colorNumberMap.
     */
    private Map<Integer, RouletteColors> createColorNumberMap() {
        Map<Integer, RouletteColors> tempMap = new HashMap<>();

        for (int i = 0; i <= this.NUMS_TOTAL; i++) {
            tempMap.put(
                    this.ROULETTE_SEQUENCE[i],
                    mapNumberToColorEnum(i));
        }

        return tempMap;
    }

    /**
     * Creates the betTypePayoutMap by associating bet types with their payouts.
     *
     * @return The created betTypePayoutMap.
     */
    private Map<RouletteBetType, Integer> createBetTypePayoutMap() {
        return new HashMap<>() {
            {
                put(RouletteBetType.STRAIGHT_UP, 35);
                put(RouletteBetType.SPLIT, 17);
                put(RouletteBetType.STREET, 11);
                put(RouletteBetType.CORNER, 8);
                put(RouletteBetType.DOUBLE_STREET, 5);
                put(RouletteBetType.COLUMN, 2);
                put(RouletteBetType.DOZEN, 2);
                put(RouletteBetType.ODD_EVEN, 1);
                put(RouletteBetType.RED_BLACK, 1);
                put(RouletteBetType.HALF, 1);
            }
        };
    }
}
