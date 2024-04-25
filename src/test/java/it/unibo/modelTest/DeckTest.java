package it.unibo.modelTest;
import it.unibo.virtualCasino.model.games.impl.blackjack.Card;
import it.unibo.virtualCasino.model.games.impl.blackjack.Deck;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardColor;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardNumber;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardSeed;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeckTest {
    Deck d;

    @BeforeEach
    void setup(){
        d = new Deck();
    }

    //testo il numero di carte
    @Test void testInitPlayDeck(){
        d.initPlayDeck();
        assertEquals(56, d.size()); //there are 56 cards because the ace and the one are two distinct cards 
    }

    @Test
    public void testInitPlayDeck_shouldHaveAllRanksForEverySeed() {
        d.initPlayDeck();

        int expectedCards = CardSeed.values().length * CardNumber.values().length;
        assertEquals(expectedCards, d.size());//Calcolo il numero atteso di carte moltiplicando il numero di semi per il numero di valori delle carte e poi lo confrontiamo con la dimensione effettiva
    }
}
