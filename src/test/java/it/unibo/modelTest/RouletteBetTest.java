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

/**
 * Test di unità focalizzati unicamente sulla classe RouletteBet.
 * Tutti i test usano JUnit 5.
 */
public class RouletteBetTest {

        private Roulette colorsHelper; // ci serve la mappa numero-colore
        private Map<Integer, RouletteColors> colorMap; // inizializzata in @BeforeEach

        @BeforeEach
        void initColorMap() {
                // La roulette ci serve solo per accedere a getColorNumberMap().
                colorsHelper = new Roulette(new Player("dummy"));
                colorMap = colorsHelper.getColorNumberMap();
        }

        // TEST METODO getPossibleWin

        // Verifico che il payout 35:1 per STRAIGHT_UP sia calcolato correttamente.
        @Test
        void possibleWin_straightUp() {
                RouletteBet bet = new RouletteBet(10, RouletteBetType.STRAIGHT_UP, 17);
                assertEquals(350,
                                bet.getPossibleWin(),
                                "Una puntata straight-up da 10 deve pagare 350");
        }

        // Verifico che il payout 17:1 per SPLIT sia calcolato correttamente.
        @Test
        void possibleWin_split() {
                RouletteBet bet = new RouletteBet(20, RouletteBetType.SPLIT, 1);
                assertEquals(340,
                                bet.getPossibleWin(),
                                "Una puntata split da 20 deve pagare 340");
        }

        // Verifico che il payout 2:1 per COLUMN sia calcolato correttamente.
        @Test
        void possibleWin_column() {
                RouletteBet bet = new RouletteBet(15, RouletteBetType.COLUMN, 2);
                assertEquals(30,
                                bet.getPossibleWin(),
                                "Una puntata column da 15 deve pagare 30");
        }
}
