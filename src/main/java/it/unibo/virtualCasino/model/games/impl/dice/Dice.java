package it.unibo.virtualCasino.model.games.impl.dice;

import java.util.Random;

import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.Games;

/**
 * Bonus game “Dice”.
 * – Two traditional dice (faces 1-6) are rolled.
 * – The player chooses the expected sum in advance (guess 2-12).
 * – If the guess is correct, the balance is doubled; otherwise it is halved.
 */
public class Dice implements Games {

    private static final int FACES = 6;
    private static final int MIN_SUM = 2;
    private static final int MAX_SUM = 12;

    private final Player player; // current player
    private final Random rng; // RNG injection (useful for tests)

    private Integer lastRoll = null; // last roll’s sum (null → never rolled)

    private int lastD1 = -1;
    private int lastD2 = -1;

    public Dice(Player player) {
        this(player, new Random());
    }

    /** Allows injection of a deterministic Random instance. */
    public Dice(Player player, Random rng) {
        this.player = player;
        this.rng = rng;
    }

    /** Rolls two dice and returns their sum (2 – 12). */
    public int roll() {
        lastD1 = rng.nextInt(FACES) + 1;
        lastD2 = rng.nextInt(FACES) + 1;
        lastRoll = lastD1 + lastD2;
        return lastRoll;
    }

    /** Returns the last roll or -1 if no roll has occurred yet. */
    public int getLastRoll() {
        return lastRoll == null ? -1 : lastRoll;
    }

    /**
     * Applies the “lucky factor” to the player’s balance.
     *
     * @param guess number chosen by the player (2 – 12)
     * @throws IllegalArgumentException if the guess is out of range
     * @throws IllegalStateException    if roll() has not yet been called
     */
    public void applyLuckyFactor(int guess) {
        if (guess < MIN_SUM || guess > MAX_SUM) {
            throw new IllegalArgumentException("Guess must be between 2 and 12");
        }
        if (lastRoll == null) {
            throw new IllegalStateException("Roll the dice before applying the factor");
        }

        double oldBalance = player.getAccount();
        double newBalance = (guess == lastRoll) ? oldBalance * 2 : oldBalance / 2;

        // Update the balance via the addWin / removeLoss semantics
        double delta = newBalance - oldBalance;
        if (delta >= 0) {
            player.addWin(delta);
        } else {
            player.removeLoss(-delta);
        }
    }

    /* ----- Methods required by the Games interface ----- */

    /** Resets the last roll, ready for a new round. */
    @Override
    public void nextRound() {
        lastRoll = null;
    }

    /** No output logic: the view will handle result display. */
    @Override
    public void showResult() {
        /* no-op */
    }

    public int getLastRollFirstDie() {
        return lastD1;
    }

    public int getLastRollSecondDie() {
        return lastD2;
    }
}
