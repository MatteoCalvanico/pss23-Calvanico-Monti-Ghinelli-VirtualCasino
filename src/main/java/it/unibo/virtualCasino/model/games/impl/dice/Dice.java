package it.unibo.virtualCasino.model.games.impl.dice;

import java.util.Random;

import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.Games;

/**
 * Gioco bonus "Dadi".
 * – Si lanciano due dadi tradizionali (1-6).
 * – Il giocatore sceglie in anticipo la somma (guess 2-12).
 * – Se indovina il saldo viene raddoppiato, altrimenti dimezzato.
 */
public class Dice implements Games {

    private static final int FACES = 6;
    private static final int MIN_SUM = 2;
    private static final int MAX_SUM = 12;

    private final Player player; // giocatore corrente
    private final Random rng; // random injection (utile nei test)

    private Integer lastRoll = null; // somma ultimo lancio (null → mai lanciato)

    private int lastD1 = -1;
    private int lastD2 = -1;

    public Dice(Player player) {
        this(player, new Random());
    }

    /**
     * permette d’iniettare un Random deterministico.
     */
    public Dice(Player player, Random rng) {
        this.player = player;
        this.rng = rng;
    }

    /** Lancia due dadi e restituisce la loro somma (2-12). */
    public int roll() {
        lastD1 = rng.nextInt(FACES) + 1;
        lastD2 = rng.nextInt(FACES) + 1;
        lastRoll = lastD1 + lastD2;
        return lastRoll;
    }

    /** Restituisce l’ultimo lancio o -1 se non ancora lanciato. */
    public int getLastRoll() {
        return lastRoll == null ? -1 : lastRoll;
    }

    /**
     * Applica il "lucky factor" al saldo del giocatore.
     *
     * @param guess numero scelto dall’utente (2-12)
     * @throws IllegalArgumentException se guess è fuori range
     * @throws IllegalStateException    se roll() non è stato ancora chiamato
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

        // aggiorna il saldo mantenendo la semantica addWin / removeLoss
        double delta = newBalance - oldBalance;
        if (delta >= 0) {
            player.addWin(delta);
        } else {
            player.removeLoss(-delta);
        }
    }

    /*
     * Metodi richiesti dall’interfaccia Games
     */

    /** Azzera l’ultimo lancio, pronto per un nuovo turno. */
    @Override
    public void nextRound() {
        lastRoll = null;
    }

    /** Nessuna logica di output: sarà la view a mostrare il risultato. */
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
