package it.unibo.modelTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.impl.dice.Dice;

class DiceTest {

    private static final long SEED = 42L; // deterministic RNG

    private Player player;
    private Dice dice;

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer"); // initial balance 1000
        dice = new Dice(player, new Random(SEED)); // injected RNG
    }

    /**
     * Ensures roll() returns 2-12, synchronizes accessors, and keeps d1+d2
     * coherent.
     */
    @Test
    @DisplayName("roll() produces a valid sum and synchronizes accessors")
    void testRoll() {
        int sum = dice.roll();

        // Sum in range 2-12
        assertTrue(sum >= 2 && sum <= 12, "Sum out of range (2-12)");

        // d1 + d2 matches the reported sum
        int d1 = dice.getLastRollFirstDie();
        int d2 = dice.getLastRollSecondDie();
        assertEquals(sum, d1 + d2, "d1+d2 must equal the sum");

        // getLastRoll aligned
        assertEquals(sum, dice.getLastRoll(), "getLastRoll() inconsistent");
    }

    @Nested
    @DisplayName("applyLuckyFactor – doubling/halving and error checks")
    class ApplyLuckyFactor {

        /** Doubles the balance when the guess is correct. */
        @Test
        @DisplayName("doubles balance on win")
        void doublesBalanceOnWin() {
            dice.roll(); // 6 + 1 = 7 with seed 42
            dice.applyLuckyFactor(7);

            assertEquals(2000.0, player.getAccount(), 0.0001);
        }

        /** Halves the balance when the guess is wrong. */
        @Test
        @DisplayName("halves balance on lose")
        void halvesBalanceOnLose() {
            dice.roll();
            dice.applyLuckyFactor(2); // wrong guess

            assertEquals(500.0, player.getAccount(), 0.0001);
        }

        /** Throws IllegalStateException if applyLuckyFactor is called before roll(). */
        @Test
        @DisplayName("throws IllegalStateException when roll() was not called")
        void requireRollBeforeApply() {
            assertThrows(IllegalStateException.class, () -> dice.applyLuckyFactor(7));
        }

        /** Throws IllegalArgumentException for a guess outside 2-12. */
        @Test
        @DisplayName("throws IllegalArgumentException for guess out of range")
        void invalidGuessRange() {
            dice.roll();
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> dice.applyLuckyFactor(1)),
                    () -> assertThrows(IllegalArgumentException.class, () -> dice.applyLuckyFactor(13)));
        }
    }

    /** Ensures nextRound() resets last roll state to –1. */
    @Test
    @DisplayName("nextRound() resets last roll state")
    void testNextRound() {
        dice.roll();
        dice.nextRound();
        assertEquals(-1, dice.getLastRoll());
    }
}
