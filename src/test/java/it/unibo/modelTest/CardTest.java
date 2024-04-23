package it.unibo.modelTest;
import it.unibo.virtualCasino.model.games.impl.blackjack.Card;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardColor;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardNumber;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardSeed;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class CardTest {
    Card c;
    @BeforeEach
    void setup(){
        System.out.println("!!! START Card Test!!!");
        c = new Card(CardNumber.FIVE, CardSeed.SPADES, CardColor.BLACK);
    }

    @Test void testGetCardNumber(){
        assertEquals(5, c.getCardNumber());
    }
    
    @Test void testGetCardSeed(){
        assertEquals(CardSeed.SPADES, c.getCardSeed());
    }

    @Test void testGetCardColor(){
        assertEquals(CardColor.BLACK, c.getCardColor());
    }

    @Test void testFlip(){
        assertEquals(true, c.isFlip());
        c.flip();
        assertEquals(false, c.isFlip());
    }
}
