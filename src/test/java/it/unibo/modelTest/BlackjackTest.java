package it.unibo.modelTest;

import it.unibo.virtualCasino.model.games.impl.blackjack.Deck;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardColor;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardNumber;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardSeed;
import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.impl.blackjack.Blackjack;
import it.unibo.virtualCasino.model.games.impl.blackjack.Card;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.List;

// import javax.sound.midi.InvalidMidiDataException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BlackjackTest {
    private Blackjack blackjack;
    private Player player;

    /**
     * Creates a Blackjack with 2 decks of cards
     */
    @BeforeEach
    public void setup() {
        player = new Player("Test Player");
        blackjack = new Blackjack(2, player);
    }

    /**
     * Verifies that the dealer and player decks are correctly
     * initialized and not null
     */
    @Test
    public void testConstructorInitializesDecks() {
        assertNotNull(blackjack.getDealerDeck(), "Dealer deck should not be null");
        assertNotNull(blackjack.getPlayerDeck(0), "Player deck 0 should not be null");
        assertNotNull(blackjack.getPlayerDeck(1), "Player deck 1 should not be null");
    }

    /**
     * Tests the initial dealer deck size
     */
    @Test
    public void testInitialDealerDeckEmpty() {
        assertEquals(0, blackjack.getDealerDeck().size(), "Dealer deck should initially be empty");
    }

    /**
     * Checks that calling call(0) adds one card to player deck 0
     */
    @Test
    public void testCallAddsCardToPlayerDeck() {
        // Draw a card for player deck 0
        blackjack.call(0);
        // Verify that the player deck contains one card
        assertEquals(1, blackjack.getPlayerDeck(0).size(), "Player deck 0 should have 1 card after call");
    }

    /**
     * Verifies that the dealer receives the correct number of cards
     * when receive(2) is called
     */
    @Test
    public void testReceiveAddsCardsToDealerDeck() {
        // Dealer receives 2 cards
        blackjack.receive(2);
        // Verify that the dealer deck contains 2 cards
        assertEquals(2, blackjack.getDealerDeck().size(), "Dealer deck should have 2 cards after receive");
    }

    /**
     * Ensures that isBlackjack returns true when the player has a blackjack
     */
    @Test
    public void testIsBlackjackReturnsTrueWhenPlayerHasBlackjack() {
        // Prepare player deck with an Ace and a 10-value card
        Deck playerDeck = blackjack.getPlayerDeck(0);
        playerDeck.emptyDeck();
        playerDeck.insert(new Card(CardNumber.ACE_ELEVEN, CardSeed.HEARTS, CardColor.RED));
        playerDeck.insert(new Card(CardNumber.TEN, CardSeed.SPADES, CardColor.BLACK));

        // Verify that isBlackjack returns true
        assertTrue(blackjack.isBlackjack(), "isBlackjack should return true when player has blackjack");
    }

    /**
     * Checks that isBlackjack returns false when the player
     * does not have a blackjack
     */
    @Test
    public void testIsBlackjackReturnsFalseWhenPlayerDoesNotHaveBlackjack() {
        // Prepare player deck without a blackjack
        Deck playerDeck = blackjack.getPlayerDeck(0);
        playerDeck.emptyDeck();
        playerDeck.insert(new Card(CardNumber.NINE, CardSeed.HEARTS, CardColor.RED));
        playerDeck.insert(new Card(CardNumber.TEN, CardSeed.SPADES, CardColor.BLACK));

        // Verify that isBlackjack returns false
        assertFalse(blackjack.isBlackjack(), "isBlackjack should return false when player does not have blackjack");
    }

    /**
     * Verifies that a split is successful when the player's first two
     * cards have the same value
     */
    @Test
    public void testSplitSuccessfulWhenPossible() {
        // Prepare player decks with two cards of the same value
        Deck playerDeck0 = blackjack.getPlayerDeck(0);
        Deck playerDeck1 = blackjack.getPlayerDeck(1);
        playerDeck0.emptyDeck();
        playerDeck1.emptyDeck();

        playerDeck0.insert(new Card(CardNumber.EIGHT, CardSeed.HEARTS, CardColor.RED));
        playerDeck0.insert(new Card(CardNumber.EIGHT, CardSeed.DIAMONDS, CardColor.RED));

        // Attempt to split
        blackjack.split();

        // Verify that each player deck contains one card after the split
        assertEquals(1, playerDeck0.size(), "Player deck 0 should have 1 card after split");
        assertEquals(1, playerDeck1.size(), "Player deck 1 should have 1 card after split");
    }

    /**
     * Ensures that a RuntimeException is thrown when attempting
     * an invalid split
     */
    @Test
    public void testSplitFailsWhenNotPossible() {
        // Prepare player deck with two cards of different values
        Deck playerDeck0 = blackjack.getPlayerDeck(0);
        playerDeck0.emptyDeck();

        playerDeck0.insert(new Card(CardNumber.NINE, CardSeed.HEARTS, CardColor.RED));
        playerDeck0.insert(new Card(CardNumber.TEN, CardSeed.DIAMONDS, CardColor.RED));

        // Attempt to split; should throw RuntimeException
        assertThrows(RuntimeException.class, () -> {
            blackjack.split();
        }, "split should throw RuntimeException when split is not possible");
    }

    /**
     * Checks that the dealer draws additional cards until reaching
     * at least 17 points, according to blackjack rules
     */
    @Test
    public void testDealerPlaysAccordingToRules() {
        // Prepare dealer deck
        Deck dealerDeck = blackjack.getDealerDeck();
        dealerDeck.emptyDeck();

        // Dealer total is 16
        dealerDeck.insert(new Card(CardNumber.SEVEN, CardSeed.HEARTS, CardColor.RED));
        dealerDeck.insert(new Card(CardNumber.NINE, CardSeed.DIAMONDS, CardColor.RED));

        // Show result; dealer should draw until at least 17
        blackjack.showResult();

        // Dealer total should be at least 17
        int dealerTotal = dealerDeck.countCard();
        assertTrue(dealerTotal >= 17, "Dealer's total should be at least 17");
    }

    /* Checks that after showResult all dealer cards are face-up */
    @Test
    public void testFlipAllDealerCardsAfterShowResult() {
        // Prepare dealer deck with face-down cards
        Deck dealerDeck = blackjack.getDealerDeck();
        dealerDeck.emptyDeck();

        Card card1 = new Card(CardNumber.FIVE, CardSeed.HEARTS, CardColor.RED);
        Card card2 = new Card(CardNumber.SIX, CardSeed.DIAMONDS, CardColor.RED);
        dealerDeck.insert(card1);
        dealerDeck.insert(card2);

        // Call showResult
        blackjack.showResult();

        // Verify that all dealer cards are face-up (flip == false)
        for (int i = 0; i < dealerDeck.size(); i++) {
            assertFalse(dealerDeck.checkCardFromDeck(i).isFlip(), "Dealer's cards should be face-up after showResult");
        }
    }

    @Test
    public void testCheckAndChangeDeckWhenCurrentDeckIsEmpty() throws Exception {
        // Access private field 'playDeck' via reflection
        Field playDeckField = Blackjack.class.getDeclaredField("playDeck");
        playDeckField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Deck> playDeck = (List<Deck>) playDeckField.get(blackjack);

        // Empty the current deck
        playDeck.get(0).emptyDeck();

        // Draw a card; should switch to the next deck
        blackjack.call(0);

        // Verify that the player deck contains one card
        assertEquals(1, blackjack.getPlayerDeck(0).size(),
                "Player's deck should have 1 card after drawing from new deck");
    }

    /**
     * Ensures call() throws when all play decks are empty.
     */
    @Test
    void testCallThrowsWhenNoDecksLeft() throws Exception {

        // Empty every play deck via reflection
        Field playDeckField = Blackjack.class.getDeclaredField("playDeck");
        playDeckField.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Deck> playDeck = (List<Deck>) playDeckField.get(blackjack);
        playDeck.forEach(Deck::emptyDeck);

        // Action under test
        assertThrows(RuntimeException.class,
                () -> blackjack.call(0),
                "call() deve lanciare un’eccezione quando non ci sono carte nei mazzi");
    }

    /**
     * Verifies that countCard correctly calculates the total points
     * in the deck
     */
    @Test
    public void testCountCardCalculatesCorrectTotal() {
        // Prepare a deck with known cards
        Deck deck = new Deck();
        deck.insert(new Card(CardNumber.TWO, CardSeed.CLUBS, CardColor.BLACK));
        deck.insert(new Card(CardNumber.THREE, CardSeed.DIAMONDS, CardColor.RED));
        deck.insert(new Card(CardNumber.FIVE, CardSeed.HEARTS, CardColor.RED));

        // Verify that countCard returns the correct total
        assertEquals(10, deck.countCard(), "Deck count should be 10");
    }

    /* Checks that flipAll turns all cards face-up in the deck */
    @Test
    public void testFlipAllFlipsAllCardsInDeck() {
        // Prepare a deck with face-down cards
        Deck deck = new Deck();
        Card card1 = new Card(CardNumber.FOUR, CardSeed.CLUBS, CardColor.BLACK);
        Card card2 = new Card(CardNumber.SIX, CardSeed.SPADES, CardColor.BLACK);
        deck.insert(card1);
        deck.insert(card2);

        // Flip all cards
        deck.flipAll();

        // Verify that all cards are face-up (flip == false)
        for (int i = 0; i < deck.size(); i++) {
            assertFalse(deck.checkCardFromDeck(i).isFlip(), "All cards should be face-up after flipAll");
        }
    }

    /* Ensures that split works correctly with identical cards */
    @Test
    void testSplitWithEqualCards() {
        // Search for two identical cards from the dealer deck
        Card firstCard = new Card(CardNumber.EIGHT, CardSeed.HEARTS, CardColor.RED);
        Card secondCard = new Card(CardNumber.EIGHT, CardSeed.SPADES, CardColor.BLACK);

        // Add the cards to the player's deck
        blackjack.getPlayerDeck(0).insert(firstCard);
        blackjack.getPlayerDeck(0).insert(secondCard);

        // Perform the split
        blackjack.split();

        // Verify that the split was successful
        assertEquals(1, blackjack.getPlayerDeck(1).size(),
                "Player's second deck should have 1 card after split");
        assertEquals(1, blackjack.getPlayerDeck(0).size(),
                "Player's first deck should have 1 card after split");
    }
}
