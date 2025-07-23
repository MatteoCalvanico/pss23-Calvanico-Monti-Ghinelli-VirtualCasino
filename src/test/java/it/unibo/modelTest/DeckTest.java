package it.unibo.modelTest;

import it.unibo.virtualCasino.model.games.impl.blackjack.Deck;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardColor;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardNumber;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardSeed;
import it.unibo.virtualCasino.model.games.impl.blackjack.Card;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeckTest {
    Deck d;

    @BeforeEach
    void setup() {
        d = new Deck();
    }

    /** Ensures initPlayDeck populates the deck with 56 cards. */
    @Test
    void testInitPlayDeck() {
        d.initPlayDeck();
        assertEquals(56, d.size());
    }

    /** Ensures initPlayDeck produces 28 red and 28 black cards. */
    @Test
    void testInitPlayDeckColorSeed() {
        d.initPlayDeck();
        int redCardCount = 0;
        int blackCardCount = 0;

        for (int i = 0; i < d.size(); i++) {
            Card card = d.checkCardFromDeck(i);
            if (card.getCardColor() == CardColor.RED) {
                redCardCount++;
            } else if (card.getCardColor() == CardColor.BLACK) {
                blackCardCount++;
            } else {
                fail("Unexpected card color found: " + card.getCardColor());
            }
        }
        assertEquals(28, redCardCount);
        assertEquals(28, blackCardCount);
        assertEquals(56, d.size());
    }

    /**
     * Ensures checkCardFromDeck returns a card without removing it from the deck.
     */
    @Test
    public void testCheckCardFromDeck() {
        d.initPlayDeck();
        Card firstCard = d.checkCardFromDeck(40);
        assertNotNull(firstCard);
        assertEquals(56, d.size());
    }

    /** Ensures countCard returns the correct total of card values. */
    @Test
    public void testCountCardes() {
        Deck de = new Deck();
        Card card1 = new Card(CardNumber.TWO, CardSeed.SPADES, CardColor.BLACK);
        Card card2 = new Card(CardNumber.KING, CardSeed.HEARTS, CardColor.RED);
        de.insert(card1);
        de.insert(card2);

        int expectedSum = CardNumber.TWO.getValue() + CardNumber.KING.getValue();
        int actualSum = de.countCard();
        assertEquals(expectedSum, actualSum);
    }

    /** Ensures draw returns a card from a full deck. */
    @Test
    public void testDrawFullDeck() {
        d.initPlayDeck();
        Card drawnCard = d.draw();
        assertNotNull(drawnCard);
    }

    /** Ensures draw removes a card from the deck. */
    @Test
    public void testDrawRemoveCard() {
        d.initPlayDeck();
        int initialSize = d.size();
        d.draw();
        assertEquals(initialSize - 1, d.size());
    }

    /** Ensures draw returns null when called on an empty deck. */
    @Test
    public void testDrawEmptyDeck_() {
        Deck emptyDeck = new Deck();
        Card drawnCard = emptyDeck.draw();
        assertNull(drawnCard);
    }

    /** Ensures draw returns null after emptyDeck is called on a populated deck. */
    @Test
    public void testDrawEmptyDeck() {
        d.initPlayDeck();
        d.emptyDeck();
        Card drawnCard = d.draw();
        assertNull(drawnCard);
    }

    /** Ensures shuffleDeck changes the order of cards. */
    @Test
    public void testShuffleDeck() {
        d.initPlayDeck();
        d.shuffleDeck();
        String expectedString = "Deck: [ACE,HEARTS,RED][ACE,HEARTS,RED][TWO,HEARTS,RED][THREE,HEARTS,RED][FOUR,HEARTS,RED][FIVE,HEARTS,RED][SIX,HEARTS,RED][SEVEN,HEARTS,RED][EIGHT,HEARTS,RED][NINE,HEARTS,RED][TEN,HEARTS,RED][JACK,HEARTS,RED][QUEEN,HEARTS,RED][KING,HEARTS,RED][ACE,DIAMONDS,RED][ACE,DIAMONDS,RED][TWO,DIAMONDS,RED][THREE,DIAMONDS,RED][FOUR,DIAMONDS,RED][FIVE,DIAMONDS,RED][SIX,DIAMONDS,RED][SEVEN,DIAMONDS,RED][EIGHT,DIAMONDS,RED][NINE,DIAMONDS,RED][TEN,DIAMONDS,RED][JACK,DIAMONDS,RED][QUEEN,DIAMONDS,RED][KING,DIAMONDS,RED][ACE,CLUBS,BLACK][ACE,CLUBS,BLACK][TWO,CLUBS,BLACK][THREE,CLUBS,BLACK][FOUR,CLUBS,BLACK][FIVE,CLUBS,BLACK][SIX,CLUBS,BLACK][SEVEN,CLUBS,BLACK][EIGHT,CLUBS,BLACK][NINE,CLUBS,BLACK][TEN,CLUBS,BLACK][JACK,CLUBS,BLACK][QUEEN,CLUBS,BLACK][KING,CLUBS,BLACK][ACE,SPADES,BLACK][ACE,SPADES,BLACK][TWO,SPADES,BLACK][THREE,SPADES,BLACK][FOUR,SPADES,BLACK][FIVE,SPADES,BLACK][SIX,SPADES,BLACK][SEVEN,SPADES,BLACK][EIGHT,SPADES,BLACK][NINE,SPADES,BLACK][TEN,SPADES,BLACK][JACK,SPADES,BLACK][QUEEN,SPADES,BLACK][KING,SPADES,BLACK]";
        assertNotEquals(expectedString, d.toString());
    }

    /** Ensures draw removes the top card when the deck is unshuffled. */
    @Test
    public void testDrawNotShuffled() {
        d.initPlayDeck();
        Card firstCard = d.draw();
        Card secondCard = d.checkCardFromDeck(1);
        assertNotEquals(firstCard, secondCard);
    }

    /**
     * Ensures two consecutive draws from a shuffled deck return different cards.
     */
    @Test
    public void testDrawShuffled() {
        d.initPlayDeck();
        d.shuffleDeck();
        Card firstCard = d.draw();
        Card secondCard = d.draw();
        assertNotEquals(firstCard, secondCard);
    }

    /** Ensures insert adds a single card to an empty deck. */
    @Test
    public void testInsertCard() {
        Card card = new Card(CardNumber.ACE_ELEVEN, CardSeed.HEARTS, CardColor.BLACK);
        d.insert(card);
        assertEquals(1, d.size());
        assertEquals(card, d.checkCardFromDeck(0));
    }

    /** Ensures insert adds multiple cards to an existing deck. */
    @Test
    public void testInsertMoreCard() {
        d.initPlayDeck();
        Deck deck = new Deck();
        Card card1 = new Card(CardNumber.TWO, CardSeed.HEARTS, CardColor.RED);
        Card card2 = new Card(CardNumber.KING, CardSeed.CLUBS, CardColor.BLACK);
        deck.insert(card1);
        deck.insert(card2);
        assertEquals(2, deck.size());
        assertEquals(card1, deck.checkCardFromDeck(0));
        assertEquals(card2, deck.checkCardFromDeck(1));
    }

    /** Ensures flipAll does nothing on an empty deck. */
    @Test
    public void testFlipAllOnEmptyDeck() {
        d.flipAll();
    }

    /** Ensures flipAll turns all cards face-down after they were face-up. */
    @Test
    public void testFlipAllCards() {
        d.initPlayDeck();
        for (int i = 0; i < d.size(); i++) {
            d.flipCard(i);
        }
        d.flipAll();
        for (int i = 0; i < d.size(); i++) {
            assertFalse(d.checkCardFromDeck(i).isFlip());
        }
    }

    /** Ensures flipCard toggles a card’s face state. */
    @Test
    public void testFlipCardDiscovered() {
        d.initPlayDeck();
        int pos = 24;
        Card card = d.checkCardFromDeck(pos);
        assertTrue(card.isFlip());

        d.flipCard(pos);
        assertFalse(d.checkCardFromDeck(pos).isFlip());

        d.flipCard(pos);
        assertTrue(d.checkCardFromDeck(pos).isFlip());
    }

    /** Ensures flipCard throws IndexOutOfBoundsException for an invalid index. */
    @Test
    public void testFlipCardMargin() {
        assertThrows(IndexOutOfBoundsException.class, () -> d.flipCard(56));
    }

    /** Ensures toString returns header only for an empty deck. */
    @Test
    public void testToStringEmptyDeck() {
        Deck emptyDeck = new Deck();
        String expectedString = "Deck: ";
        assertEquals(expectedString, emptyDeck.toString());
    }

    /** Ensures toString returns expected representation for face-down cards. */
    @Test
    public void testToStringHoleCards() {
        d.initPlayDeck();
        String expectedString = "Deck: [ACE,HEARTS,RED][ACE,HEARTS,RED][TWO,HEARTS,RED][THREE,HEARTS,RED][FOUR,HEARTS,RED][FIVE,HEARTS,RED][SIX,HEARTS,RED][SEVEN,HEARTS,RED][EIGHT,HEARTS,RED][NINE,HEARTS,RED][TEN,HEARTS,RED][JACK,HEARTS,RED][QUEEN,HEARTS,RED][KING,HEARTS,RED][ACE,DIAMONDS,RED][ACE,DIAMONDS,RED][TWO,DIAMONDS,RED][THREE,DIAMONDS,RED][FOUR,DIAMONDS,RED][FIVE,DIAMONDS,RED][SIX,DIAMONDS,RED][SEVEN,DIAMONDS,RED][EIGHT,DIAMONDS,RED][NINE,DIAMONDS,RED][TEN,DIAMONDS,RED][JACK,DIAMONDS,RED][QUEEN,DIAMONDS,RED][KING,DIAMONDS,RED][ACE,CLUBS,BLACK][ACE,CLUBS,BLACK][TWO,CLUBS,BLACK][THREE,CLUBS,BLACK][FOUR,CLUBS,BLACK][FIVE,CLUBS,BLACK][SIX,CLUBS,BLACK][SEVEN,CLUBS,BLACK][EIGHT,CLUBS,BLACK][NINE,CLUBS,BLACK][TEN,CLUBS,BLACK][JACK,CLUBS,BLACK][QUEEN,CLUBS,BLACK][KING,CLUBS,BLACK][ACE,SPADES,BLACK][ACE,SPADES,BLACK][TWO,SPADES,BLACK][THREE,SPADES,BLACK][FOUR,SPADES,BLACK][FIVE,SPADES,BLACK][SIX,SPADES,BLACK][SEVEN,SPADES,BLACK][EIGHT,SPADES,BLACK][NINE,SPADES,BLACK][TEN,SPADES,BLACK][JACK,SPADES,BLACK][QUEEN,SPADES,BLACK][KING,SPADES,BLACK]";
        assertEquals(expectedString, d.toString());
    }

    /** Ensures toString returns expected representation for face-up cards. */
    @Test
    public void testToStringFlippCards() {
        d.initPlayDeck();
        d.flipAll();
        String expectedString = "Deck: [ACE,HEARTS,RED][ACE,HEARTS,RED][TWO,HEARTS,RED][THREE,HEARTS,RED][FOUR,HEARTS,RED][FIVE,HEARTS,RED][SIX,HEARTS,RED][SEVEN,HEARTS,RED][EIGHT,HEARTS,RED][NINE,HEARTS,RED][TEN,HEARTS,RED][JACK,HEARTS,RED][QUEEN,HEARTS,RED][KING,HEARTS,RED][ACE,DIAMONDS,RED][ACE,DIAMONDS,RED][TWO,DIAMONDS,RED][THREE,DIAMONDS,RED][FOUR,DIAMONDS,RED][FIVE,DIAMONDS,RED][SIX,DIAMONDS,RED][SEVEN,DIAMONDS,RED][EIGHT,DIAMONDS,RED][NINE,DIAMONDS,RED][TEN,DIAMONDS,RED][JACK,DIAMONDS,RED][QUEEN,DIAMONDS,RED][KING,DIAMONDS,RED][ACE,CLUBS,BLACK][ACE,CLUBS,BLACK][TWO,CLUBS,BLACK][THREE,CLUBS,BLACK][FOUR,CLUBS,BLACK][FIVE,CLUBS,BLACK][SIX,CLUBS,BLACK][SEVEN,CLUBS,BLACK][EIGHT,CLUBS,BLACK][NINE,CLUBS,BLACK][TEN,CLUBS,BLACK][JACK,CLUBS,BLACK][QUEEN,CLUBS,BLACK][KING,CLUBS,BLACK][ACE,SPADES,BLACK][ACE,SPADES,BLACK][TWO,SPADES,BLACK][THREE,SPADES,BLACK][FOUR,SPADES,BLACK][FIVE,SPADES,BLACK][SIX,SPADES,BLACK][SEVEN,SPADES,BLACK][EIGHT,SPADES,BLACK][NINE,SPADES,BLACK][TEN,SPADES,BLACK][JACK,SPADES,BLACK][QUEEN,SPADES,BLACK][KING,SPADES,BLACK]";
        assertEquals(expectedString, d.toString());
    }
}
