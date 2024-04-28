package it.unibo.modelTest;

import it.unibo.virtualCasino.model.games.impl.blackjack.Deck;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardColor;
import it.unibo.virtualCasino.model.games.impl.blackjack.Card;

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

    @Test void testInitPlayDeckColorSeed(){
        d.initPlayDeck();
        //inizializzo due contatori a zero
        int redCardCount = 0;
        int blackCardCount = 0;

        for (int i = 0; i < d.size(); i++){//uso d.size per ottenere la dimensione del mazzo per poi iterare usando un indice
            Card card = d.checkCardFromDeck(i);
            if (card.getCardColor() == CardColor.RED) {
                redCardCount++;
            }else if (card.getCardColor() == CardColor.BLACK){
                blackCardCount++;
            }else{
               fail("Unexpected card color found: " + card.getCardColor()); //Se viene trovato un colore diverso da rosso o nero il test fallisce
            }
        }
        assertEquals(28, redCardCount);
        assertEquals(28, blackCardCount);
        assertEquals(56, d.size());

    }

    @Test public void testCheckCardFromDeck() {
        d.initPlayDeck();
        Card firstCard = d.checkCardFromDeck(51);
        assertNotNull(firstCard, "I check that the first card is nothing");
        // Check if the retrieved card remains in the deck (not removed by checkCardFromDeck)
        assertEquals(56, d.size());
    }



}