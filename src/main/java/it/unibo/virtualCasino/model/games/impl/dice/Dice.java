package it.unibo.virtualCasino.model.games.impl.dice;

import java.util.Random;

import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.Games;

/**
 * Gioco bonus “Dadi”.
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
        int d1 = rng.nextInt(FACES) + 1;
        int d2 = rng.nextInt(FACES) + 1;
        lastRoll = d1 + d2;
        return lastRoll;
    }

    /** Restituisce l’ultimo lancio o -1 se non ancora lanciato. */
    public int getLastRoll() {
        return lastRoll == null ? -1 : lastRoll;
    }

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
}
