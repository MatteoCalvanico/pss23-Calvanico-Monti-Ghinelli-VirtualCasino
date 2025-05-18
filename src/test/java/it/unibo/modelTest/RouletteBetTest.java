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

        // TEST METODO getWinningNumbers

        // Controllo che una STRAIGHT_UP contenga esattamente il numero puntato.
        @Test
        void winningNumbers_straightUpContainsOnlyThatNumber() {
                int number = 21;
                RouletteBet bet = new RouletteBet(5, RouletteBetType.STRAIGHT_UP, number);

                assertEquals(1,
                                bet.getWinningNumbers().size(),
                                "Straight-up deve avere un solo numero");
                assertTrue(bet.getWinningNumbers().contains(number),
                                "Il numero vincente deve corrispondere a quello puntato");
        }

        // Verifico che una SPLIT generi esattamente due numeri distinti compresi fra 1
        // e 36.
        @Test
        void winningNumbers_splitHasTwoDistinctNumbers() {
                RouletteBet bet = new RouletteBet(5, RouletteBetType.SPLIT, 1);

                assertEquals(2,
                                bet.getWinningNumbers().size(),
                                "Uno split deve coprire due numeri");
                Set<Integer> unique = new HashSet<>(bet.getWinningNumbers());
                assertEquals(2, unique.size(), "I due numeri devono essere distinti");
                bet.getWinningNumbers().forEach(n -> assertTrue(n >= 1 && n <= 36,
                                "I numeri di uno split devono essere nel range 1-36"));
        }

        // Verifico che una COLUMN copra 12 numeri e che tutti abbiano resto 2 (colonna
        // centrale) se position=2.
        @Test
        void winningNumbers_columnHasTwelveNumbersWithCorrectSpacing() {
                RouletteBet bet = new RouletteBet(5, RouletteBetType.COLUMN, 2);

                assertEquals(12,
                                bet.getWinningNumbers().size(),
                                "Una column deve contenere 12 numeri");
                bet.getWinningNumbers().forEach(n -> assertEquals(2,
                                n % 3,
                                "Tutti i numeri della colonna 2 devono avere resto 2 mod 3"));
        }

        // Verifico che una DOZEN (seconda dozzina) abbia 12 numeri fra 13 e 24.
        @Test
        void winningNumbers_dozenHasCorrectRangeAndSize() {
                RouletteBet bet = new RouletteBet(5, RouletteBetType.DOZEN, 2);

                assertEquals(12,
                                bet.getWinningNumbers().size(),
                                "Una dozen deve contenere 12 numeri");
                bet.getWinningNumbers().forEach(n -> assertTrue(n >= 13 && n <= 24,
                                "I numeri della seconda dozzina devono essere 13-24"));
        }

        // Verifico che una ODD_EVEN su EVEN ritorni solo numeri pari ed esattamente 18
        // numeri.
        @Test
        void winningNumbers_evenBetContainsOnlyEvenNumbers() {
                RouletteBet bet = new RouletteBet(5, RouletteBetType.ODD_EVEN, 1); // 1 = EVEN nel codice

                assertEquals(18,
                                bet.getWinningNumbers().size(),
                                "La puntata EVEN deve avere 18 numeri");
                bet.getWinningNumbers().forEach(n -> assertEquals(0,
                                n % 2,
                                "Tutti i numeri della puntata EVEN devono essere pari"));
        }

        // Verifico che una RED_BLACK su ROSSO ritorni 18 numeri tutti marcati RED nella
        // mappa colori.
        @Test
        void winningNumbers_redBetAreAllRed() {
                RouletteBet redBet = new RouletteBet(5, RouletteBetType.RED_BLACK, 1); // 1 = rosso

                assertEquals(18,
                                redBet.getWinningNumbers().size(),
                                "La puntata su rosso deve coprire 18 numeri");
                redBet.getWinningNumbers().forEach(n -> assertEquals(RouletteColors.RED,
                                colorMap.get(n),
                                "Ogni numero nella puntata ROSSO deve essere di colore RED"));
        }

        // Verifico che una HALF (primo half: 1-18) abbia 18 numeri tutti nel range
        // corretto.
        @Test
        void winningNumbers_halfBetHasCorrectRange() {
                RouletteBet halfBet = new RouletteBet(5, RouletteBetType.HALF, 1); // 1 = 1-18

                assertEquals(18,
                                halfBet.getWinningNumbers().size(),
                                "La puntata 1-18 deve contenere 18 numeri");
                halfBet.getWinningNumbers().forEach(n -> assertTrue(n >= 1 && n <= 18,
                                "Ogni numero deve trovarsi fra 1 e 18"));
        }

}
