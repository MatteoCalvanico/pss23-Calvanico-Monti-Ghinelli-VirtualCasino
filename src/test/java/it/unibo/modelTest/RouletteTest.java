package it.unibo.modelTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.impl.roulette.Roulette;
import it.unibo.virtualCasino.model.games.impl.roulette.RouletteBet;
import it.unibo.virtualCasino.model.games.impl.roulette.enums.RouletteBetType;

public class RouletteTest {

    private Player player;
    private Roulette roulette;

    @BeforeEach
    void init() {
        player = new Player("TestPlayer");
        roulette = new Roulette(player);
    }

    /** Ensures addRouletteBet adds the bet to the map. */
    @Test
    void addRouletteBet_addsBetToMap() {
        RouletteBet bet = new RouletteBet(10, RouletteBetType.STRAIGHT_UP, 7);
        roulette.addRouletteBet(bet);

        assertEquals(1, roulette.getBetsList().size());
        assertTrue(roulette.getBetsList().containsKey(bet.getBetId()));
    }

    /** Ensures getTotalBetsAmount returns the correct sum of all bets. */
    @Test
    void getTotalBetsAmount_returnsCorrectSum() {
        roulette.addRouletteBet(new RouletteBet(50, RouletteBetType.STRAIGHT_UP, 12));
        roulette.addRouletteBet(new RouletteBet(20, RouletteBetType.STRAIGHT_UP, 1));

        assertEquals(70.0, roulette.getTotalBetsAmount(), 0.01);
    }

    /** Ensures deleteBet removes the specified bet from the map. */
    @Test
    void deleteBet_removesCorrectBet() {
        RouletteBet bet = new RouletteBet(15, RouletteBetType.STRAIGHT_UP, 3);
        roulette.addRouletteBet(bet);

        Map<UUID, RouletteBet> afterDelete = roulette.deleteBet(bet.getBetId());

        assertFalse(afterDelete.containsKey(bet.getBetId()));
        assertEquals(0, afterDelete.size());
    }

    /** Ensures nextRound clears the bet list. */
    @Test
    void nextRound_clearsBetsList() {
        roulette.addRouletteBet(new RouletteBet(25, RouletteBetType.STRAIGHT_UP, 5));
        roulette.nextRound();

        assertEquals(0, roulette.getBetsList().size());
    }

    /**
     * Ensures getWinningNumber throws when no winning number has been generated.
     */
    @Test
    void getWinningNumber_throwsWhenNotGenerated() {
        Exception ex = assertThrows(Exception.class, roulette::getWinningNumber);
        assertEquals("Winning number is null", ex.getMessage());
    }

    /** Ensures getRouletteTable returns a 12 × 3 matrix. */
    @Test
    void getRouletteTable_hasExpectedDimensions() {
        int[][] table = roulette.getRouletteTable();

        assertEquals(12, table.length);
        for (int[] row : table) {
            assertEquals(3, row.length);
        }
    }

    /** Ensures color-number map contains all 37 numbers (0–36). */
    @Test
    void getColorNumberMap_containsThirtySevenEntries() {
        assertEquals(37, roulette.getColorNumberMap().size());
        assertTrue(roulette.getColorNumberMap().containsKey(0));
    }

    /**
     * Ensures showResult leaves player balance unchanged when there are no bets.
     */
    @Test
    void showResult_withNoBets_doesNotChangeAccount() {
        double initialBalance = player.getAccount();
        roulette.showResult();
        assertEquals(initialBalance, player.getAccount(), 0.0001);
    }

    /** Ensures balance updates correctly for a single straight-up bet. */
    @Test
    void showResult_singleStraightBet_updatesAccountCorrectly() throws Exception {
        RouletteBet bet = new RouletteBet(10, RouletteBetType.STRAIGHT_UP, 7);
        roulette.addRouletteBet(bet);

        double initialBalance = player.getAccount();
        roulette.showResult();
        int winning = roulette.getWinningNumber();

        double expectedBalance = (winning == 7)
                ? initialBalance + bet.getPossibleWin()
                : initialBalance - bet.getBetAmount();

        assertEquals(expectedBalance, player.getAccount(), 0.0001);
    }

    /** Betting 1 on all 37 numbers should always yield a net loss of 1. */
    @Test
    void showResult_allNumbersBet_alwaysNetLossOfOne() throws Exception {
        for (int n = 0; n <= 36; n++) {
            roulette.addRouletteBet(new RouletteBet(1, RouletteBetType.STRAIGHT_UP, n));
        }

        double initialBalance = player.getAccount();
        roulette.showResult();

        assertEquals(initialBalance - 1, player.getAccount(), 0.0001);
    }

    /**
     * Forces a bet to win on any number and verifies account increases accordingly.
     */
    @Test
    void showResult_alwaysWinningBet_increasesAccount() throws Exception {
        RouletteBet bet = new RouletteBet(10, RouletteBetType.STRAIGHT_UP, 0);

        Field wn = RouletteBet.class.getDeclaredField("winningNumbers");
        wn.setAccessible(true);
        @SuppressWarnings("unchecked")
        ArrayList<Integer> list = (ArrayList<Integer>) wn.get(bet);
        list.clear();
        for (int n = 0; n <= 36; n++) {
            list.add(n);
        }

        roulette.addRouletteBet(bet);
        double initialBalance = player.getAccount();

        roulette.showResult();

        assertEquals(initialBalance + bet.getPossibleWin(), player.getAccount(), 0.0001);
    }

    /** Ensures winning number is within 0–36 after showResult. */
    @Test
    void getWinningNumber_afterShowResult_isWithinRange() throws Exception {
        roulette.showResult();
        int winning = roulette.getWinningNumber();

        assertTrue(winning >= 0 && winning <= 36);
    }
}
