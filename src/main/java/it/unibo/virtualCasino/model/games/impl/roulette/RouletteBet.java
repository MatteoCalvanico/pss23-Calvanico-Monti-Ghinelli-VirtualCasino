package it.unibo.virtualCasino.model.games.impl.roulette;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import it.unibo.virtualCasino.model.games.impl.roulette.enums.RouletteBetType;
import it.unibo.virtualCasino.model.games.impl.roulette.enums.RouletteColors;

/**
 * Represents a specific bet placed in a Roulette game.
 *
 * @author it.unibo.virtualCasino
 */
public class RouletteBet extends RouletteBase {

    /**
     * The bet identifier uuid
     */
    private final UUID betId;

    /**
     * The type of bet placed (e.g., STRAIGHT_UP, SPLIT, STREET, etc.).
     */
    private final RouletteBetType betType;

    /**
     * The winning numbers associated with this bet.
     */
    private final ArrayList<Integer> winningNumbers;

    /**
     * The amount of the bet.
     */
    private int amount;

    /**
     * Creates a new RouletteBet instance.
     *
     * @param betType            The type of bet being placed.
     * @param betPositionInTable The position of the bet on the table.
     */
    public RouletteBet(
            int amount,
            RouletteBetType betType,
            int betPositionInTable) {

        this.betId = UUID.randomUUID();
        this.amount = amount;
        this.betType = betType;
        this.winningNumbers = this.getWinningNumbers(betType, betPositionInTable);
    }

    /**
     * Gets the id of the bet.
     *
     * @return The bet id.
     */
    public UUID getBetId() {
        return this.betId;
    }

    /**
     * Gets the amount of the bet.
     *
     * @return The bet amount.
     */
    public int getBetAmount() {
        return this.amount;
    }

    public RouletteBetType getBetType() {
        return this.betType;
    }

    /**
     * Calculates the potential win for this bet.
     *
     * @return The potential win amount.
     */
    public int getPossibleWin() {
        int multiplier = this.betTypePayoutMap.get(this.betType);
        return this.amount * multiplier;
    }

    /**
     * Gets the winning numbers associated with this bet.
     *
     * @return A list of winning numbers.
     */
    public ArrayList<Integer> getWinningNumbers() {
        return this.winningNumbers;
    }

    /**
     * Determines the winning numbers based on the bet type and position.
     *
     * @param betType            The type of bet.
     * @param betPositionInTable The position of the bet on the table.
     * @return A list of winning numbers for the specified bet.
     */
    private ArrayList<Integer> getWinningNumbers(RouletteBetType betType, int betPositionInTable) {
        ArrayList<Integer> numbers = new ArrayList<>();
        switch (betType) {
            case STRAIGHT_UP:
                numbers.add(betPositionInTable);
                break;
            case SPLIT: {
                if (betPositionInTable < this.MAX_VERTICAL_SPLITS) {
                    int topNum = calcBottomNumberBasedOnPosition(betPositionInTable);
                    numbers.add(topNum + 1);
                    numbers.add(topNum);
                } else {
                    int bottomNum = betPositionInTable - this.MAX_VERTICAL_SPLITS + 1;
                    numbers.add(bottomNum);
                    numbers.add(bottomNum + this.TABLE_COLS);
                }
            }
                break;
            case STREET: {
                int topNum = betPositionInTable * this.TABLE_COLS;
                numbers.add(topNum--);
                numbers.add(topNum--);
                numbers.add(topNum);
            }
                break;
            case CORNER: {
                int topNum = calcTopNumberBasedOnPosition(betPositionInTable);
                numbers.add(topNum);
                numbers.add(topNum - 1);
                numbers.add(topNum + 2);
                numbers.add(topNum + 3);
            }
                break;
            case DOUBLE_STREET: {
                int startingNum = (betPositionInTable * this.TABLE_COLS) - 2;
                for (int i = 0; i < this.DOUBLE_STREET_W_NUMS; i++) {
                    numbers.add(startingNum + i);
                }
            }
                break;
            case COLUMN: {
                int startingNum = betPositionInTable;
                for (int i = 0; i < this.TABLE_ROWS; i++) {
                    numbers.add(startingNum);
                    startingNum += this.TABLE_COLS;
                }
            }
                break;
            case DOZEN: {
                int startingNum = betPositionInTable * this.TABLE_ROWS;
                for (int i = startingNum; i > startingNum - this.TABLE_ROWS; i--) {
                    numbers.add(i);
                }
            }
                break;
            case ODD_EVEN: {
                if (betPositionInTable == 1) {
                    for (int i = 2; i <= 36; i += 2) {
                        numbers.add(i);
                    }
                } else {
                    for (int i = 1; i <= 36; i += 2) {
                        numbers.add(i);
                    }
                }
            }
                break;
            case RED_BLACK: {
                for (Map.Entry<Integer, RouletteColors> entry : colorNumberMap.entrySet()) {
                    if (betPositionInTable == 1 && entry.getValue() == RouletteColors.RED) {
                        numbers.add(entry.getKey());
                    } else if (betPositionInTable == 2 && entry.getValue() == RouletteColors.BLACK) {
                        numbers.add(entry.getKey());
                    }
                }
            }
                break;
            case HALF: {
                int startingNum = betPositionInTable * (this.NUMS_TOTAL / 2);
                for (int i = startingNum; i > startingNum - (this.NUMS_TOTAL / 2); i--) {
                    numbers.add(i);
                }
            }
                break;
            default:
                break;
        }

        return numbers;
    }

    /**
     * Calculates the top number in a split bet based on its position on the table.
     *
     * @param position The position of the split bet.
     * @return The top number in the split bet.
     */
    private int calcTopNumberBasedOnPosition(int position) {
        double number = ((double) position / this.SPLITS_IN_ROW) * this.TABLE_COLS;

        // Ceil If First Decimal Greater Than Zero
        if (number % 1 > 0) {
            int integerPart = (int) number;
            double firstDecimal = number - integerPart;

            if (firstDecimal > 0) {
                number = Math.ceil(number);
            }
        }

        return (int) number;
    }

    private int calcBottomNumberBasedOnPosition(int position) {
        double number = ((double) position / this.SPLITS_IN_ROW) * this.TABLE_COLS;

        // Ceil If First Decimal Greater Than Zero
        if (number % 1 > 0) {
            int integerPart = (int) number;
            double firstDecimal = number - integerPart;

            if (firstDecimal > 0) {
                number = Math.floor(number);
            }
        }

        return (int) number + 1;
    }

}
