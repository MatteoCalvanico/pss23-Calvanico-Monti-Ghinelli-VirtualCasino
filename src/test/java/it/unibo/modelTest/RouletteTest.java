package it.unibo.modelTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.UUID;

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

        /*
         * TEST di Roulette
         */
        // Testo che addRouletteBet inserisca correttamente la puntata nella mappa.
        @Test
        void addRouletteBet_addsBetToMap() {
                RouletteBet bet = new RouletteBet(10, RouletteBetType.STRAIGHT_UP, 7);
                roulette.addRouletteBet(bet);

                assertEquals(1,
                                roulette.getBetsList().size(),
                                "Dopo addRouletteBet la mappa deve contenere 1 puntata");
                assertTrue(roulette.getBetsList().containsKey(bet.getBetId()),
                                "La puntata inserita deve essere presente in mappa");
        }

        // Testo che getTotalBetsAmount sommi correttamente gli importi di tutte le
        // puntate.
        @Test
        void getTotalBetsAmount_returnsCorrectSum() {
                roulette.addRouletteBet(new RouletteBet(50, RouletteBetType.STRAIGHT_UP, 12));
                roulette.addRouletteBet(new RouletteBet(20, RouletteBetType.STRAIGHT_UP, 1));

                assertEquals(70.0,
                                roulette.getTotalBetsAmount(),
                                0.01,
                                "La somma totale delle puntate deve essere 70");
        }

        // Testo che deleteBet rimuova la puntata corretta dalla mappa.
        @Test
        void deleteBet_removesCorrectBet() {
                RouletteBet bet = new RouletteBet(15, RouletteBetType.STRAIGHT_UP, 3);
                roulette.addRouletteBet(bet);

                Map<UUID, RouletteBet> afterDelete = roulette.deleteBet(bet.getBetId());

                assertFalse(afterDelete.containsKey(bet.getBetId()),
                                "La puntata deve essere rimossa dalla mappa");
                assertEquals(0,
                                afterDelete.size(),
                                "Dopo la rimozione la mappa deve essere vuota");
        }

        // Testo che nextRound azzeri la lista delle puntate posizionate.
        @Test
        void nextRound_clearsBetsList() {
                roulette.addRouletteBet(new RouletteBet(25, RouletteBetType.STRAIGHT_UP, 5));
                roulette.nextRound();

                assertEquals(0,
                                roulette.getBetsList().size(),
                                "Alla partenza del nuovo round non devono esserci puntate attive");
        }

}
