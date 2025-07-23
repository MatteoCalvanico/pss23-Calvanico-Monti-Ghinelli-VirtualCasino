package it.unibo.modelTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.impl.roulette.Roulette;
import it.unibo.virtualCasino.model.games.impl.roulette.RouletteBet;
import it.unibo.virtualCasino.model.games.impl.roulette.enums.RouletteBetType;
import it.unibo.virtualCasino.model.games.impl.roulette.enums.RouletteColors;

/** Unit tests focused solely on the RouletteBet class (JUnit 5). */
public class RouletteBetTest {

    private Roulette colorsHelper; // helper to access the color-number map
    private Map<Integer, RouletteColors> colorMap; // initialized in @BeforeEach

    @BeforeEach
    void initColorMap() {
        colorsHelper = new Roulette(new Player("dummy"));
        colorMap = colorsHelper.getColorNumberMap();
    }

    /** Ensures 35:1 payout for STRAIGHT_UP is calculated correctly. */
    @Test
    void possibleWin_straightUp() {
        RouletteBet bet = new RouletteBet(10, RouletteBetType.STRAIGHT_UP, 17);
        assertEquals(350, bet.getPossibleWin());
    }

    /** Ensures 17:1 payout for SPLIT is calculated correctly. */
    @Test
    void possibleWin_split() {
        RouletteBet bet = new RouletteBet(20, RouletteBetType.SPLIT, 1);
        assertEquals(340, bet.getPossibleWin());
    }

    /** Ensures 2:1 payout for COLUMN is calculated correctly. */
    @Test
    void possibleWin_column() {
        RouletteBet bet = new RouletteBet(15, RouletteBetType.COLUMN, 2);
        assertEquals(30, bet.getPossibleWin());
    }

    /** Ensures STRAIGHT_UP contains exactly the bet number. */
    @Test
    void winningNumbers_straightUpContainsOnlyThatNumber() {
        int number = 21;
        RouletteBet bet = new RouletteBet(5, RouletteBetType.STRAIGHT_UP, number);

        assertEquals(1, bet.getWinningNumbers().size());
        assertTrue(bet.getWinningNumbers().contains(number));
    }

    /** Ensures SPLIT covers exactly two distinct numbers between 1 and 36. */
    @Test
    void winningNumbers_splitHasTwoDistinctNumbers() {
        RouletteBet bet = new RouletteBet(5, RouletteBetType.SPLIT, 1);

        assertEquals(2, bet.getWinningNumbers().size());
        Set<Integer> unique = new HashSet<>(bet.getWinningNumbers());
        assertEquals(2, unique.size());
        bet.getWinningNumbers().forEach(n -> assertTrue(n >= 1 && n <= 36));
    }

    /** Ensures COLUMN contains 12 numbers with correct spacing (position = 2). */
    @Test
    void winningNumbers_columnHasTwelveNumbersWithCorrectSpacing() {
        RouletteBet bet = new RouletteBet(5, RouletteBetType.COLUMN, 2);

        assertEquals(12, bet.getWinningNumbers().size());
        bet.getWinningNumbers().forEach(n -> assertEquals(2, n % 3));
    }

    /** Ensures DOZEN (second dozen) covers numbers 13–24. */
    @Test
    void winningNumbers_dozenHasCorrectRangeAndSize() {
        RouletteBet bet = new RouletteBet(5, RouletteBetType.DOZEN, 2);

        assertEquals(12, bet.getWinningNumbers().size());
        bet.getWinningNumbers().forEach(n -> assertTrue(n >= 13 && n <= 24));
    }

    /** Ensures ODD_EVEN (EVEN) covers 18 even numbers. */
    @Test
    void winningNumbers_evenBetContainsOnlyEvenNumbers() {
        RouletteBet bet = new RouletteBet(5, RouletteBetType.ODD_EVEN, 1); // 1 = EVEN

        assertEquals(18, bet.getWinningNumbers().size());
        bet.getWinningNumbers().forEach(n -> assertEquals(0, n % 2));
    }

    /** Ensures RED_BLACK (RED) covers 18 red numbers according to the color map. */
    @Test
    void winningNumbers_redBetAreAllRed() {
        RouletteBet redBet = new RouletteBet(5, RouletteBetType.RED_BLACK, 1); // 1 = RED

        assertEquals(18, redBet.getWinningNumbers().size());
        redBet.getWinningNumbers().forEach(n -> assertEquals(RouletteColors.RED, colorMap.get(n)));
    }

    /** Ensures HALF (1–18) covers 18 numbers in the correct range. */
    @Test
    void winningNumbers_halfBetHasCorrectRange() {
        RouletteBet halfBet = new RouletteBet(5, RouletteBetType.HALF, 1); // 1 = 1–18

        assertEquals(18, halfBet.getWinningNumbers().size());
        halfBet.getWinningNumbers().forEach(n -> assertTrue(n >= 1 && n <= 18));
    }
}
