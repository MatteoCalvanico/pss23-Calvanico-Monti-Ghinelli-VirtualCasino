package it.unibo.virtualCasino.model.games.impl.roulette.enums;

/**
 * The {@code RouletteBetType} enum represents the different types of bets that
 * can be placed in a game of roulette.
 */
public enum RouletteBetType {

    /**
     * A bet on a single number.
     */
    STRAIGHT_UP,

    /**
     * A bet on two adjacent numbers.
     */
    SPLIT,

    /**
     * A bet on three numbers in a row.
     */
    STREET,

    /**
     * A bet on four numbers that form a square.
     */
    CORNER,

    /**
     * A bet on six numbers across two adjacent rows.
     */
    DOUBLE_STREET,

    /**
     * A bet on all numbers in one of the three vertical columns.
     */
    COLUMN,

    /**
     * A bet on one of the two groups of twelve numbers.
     */
    DOZEN,

    /**
     * A bet on whether the result will be odd or even.
     */
    ODD_EVEN,

    /**
     * A bet on whether the result will be red or black.
     */
    RED_BLACK,

    /**
     * A bet on half of the numbers on the wheel.
     */
    HALF
}
