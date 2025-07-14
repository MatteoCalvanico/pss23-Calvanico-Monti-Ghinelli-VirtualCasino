package it.unibo.modelTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.impl.dice.Dice;

class DiceTest {

    private static final long SEED = 42L; // RNG deterministico

    private Player player;
    private Dice dice;

    /*
     * Crea un nuovo Player (saldo 1000) e un Dice con Random fissato.
     */
    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer"); // balance iniziale 1000
        dice = new Dice(player, new Random(SEED)); // RNG iniettato
    }

    /**
     * Verifica che roll():
     * -restituisca sempre una somma compresa tra 2 e 12;
     * -aggiorni correttamente i getter getLastRoll;
     * -mantenga coerenza tra d1+d2 e somma restituita.
     */
    @Test
    @DisplayName("roll() produce una somma valida e sincronizza gli accessor")
    void testRoll() {
        int sum = dice.roll();

        // Somma nel range 2-12
        assertTrue(sum >= 2 && sum <= 12, "Somma fuori range (2-12)");

        // Somma coerente con le due facce restituite dai getter
        int d1 = dice.getLastRollFirstDie();
        int d2 = dice.getLastRollSecondDie();
        assertEquals(sum, d1 + d2, "d1+d2 deve corrispondere alla somma");

        // Getter lastRoll allineato
        assertEquals(sum, dice.getLastRoll(), "getLastRoll() incoerente");
    }

}
