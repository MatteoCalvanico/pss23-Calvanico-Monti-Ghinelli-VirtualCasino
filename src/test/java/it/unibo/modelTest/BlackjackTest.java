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

//import javax.sound.midi.InvalidMidiDataException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BlackjackTest {
    private Blackjack blackjack;
    private Player player;

    /**
     * Crea un Blackgiak con 2 mazzi di carte
     */
    @BeforeEach
    public void setup() {
        player = new Player("Test Player");
        blackjack = new Blackjack(2, player);
    }

    /*
     * Verifica che i mazzi del dealer e dei giocatori siano correttamente
     * inizializzati e non siano null
     */
    @Test
    public void testConstructorInitializesDecks() {
        assertNotNull(blackjack.getDealerDeck(), "Dealer deck should not be null");
        assertNotNull(blackjack.getPlayerDeck(0), "Player deck 0 should not be null");
        assertNotNull(blackjack.getPlayerDeck(1), "Player deck 1 should not be null");
    }

    /**
     * testo la restituzione del mazzo
     */
    @Test
    public void testInitialDealerDeckEmpty() {
        assertEquals(0, blackjack.getDealerDeck().size(), "Il mazzo del dealer dovrebbe essere inizialmente vuoto");
    }

    /*
     * Controlla che chiamando call(0), una carta venga aggiunta al mazzo del
     * giocatore 0
     */
    @Test
    public void testCallAddsCardToPlayerDeck() {
        // Chiama una carta per il mazzo del giocatore 0
        blackjack.call(0);
        // Verifica che il mazzo del giocatore abbia una carta
        assertEquals(1, blackjack.getPlayerDeck(0).size(), "Player deck 0 should have 1 card after call");
    }

    /*
     * Verifica che il dealer riceva il numero corretto di carte quando viene
     * chiamato receive(2)
     */
    @Test
    public void testReceiveAddsCardsToDealerDeck() {
        // Il dealer riceve 2 carte
        blackjack.receive(2);
        // Verifica che il mazzo del dealer abbia 2 carte
        assertEquals(2, blackjack.getDealerDeck().size(), "Dealer deck should have 2 cards after receive");
    }

    /*
     * Assicura che il metodo isBlackjack restituisca true quando il giocatore ha un
     * blackjack
     */
    @Test
    public void testIsBlackjackReturnsTrueWhenPlayerHasBlackjack() {
        // Prepara il mazzo del giocatore con un Asso e una carta di valore 10
        Deck playerDeck = blackjack.getPlayerDeck(0);
        playerDeck.emptyDeck();
        playerDeck.insert(new Card(CardNumber.ACE_ELEVEN, CardSeed.HEARTS, CardColor.RED));
        playerDeck.insert(new Card(CardNumber.TEN, CardSeed.SPADES, CardColor.BLACK));

        // Verifica che isBlackjack restituisca true
        assertTrue(blackjack.isBlackjack(), "isBlackjack should return true when player has blackjack");
    }

    /*
     * Controlla che isBlackjack restituisca false quando il giocatore non ha un
     * blackjack
     */
    @Test
    public void testIsBlackjackReturnsFalseWhenPlayerDoesNotHaveBlackjack() {
        // Prepara il mazzo del giocatore senza un blackjack
        Deck playerDeck = blackjack.getPlayerDeck(0);
        playerDeck.emptyDeck();
        playerDeck.insert(new Card(CardNumber.NINE, CardSeed.HEARTS, CardColor.RED));
        playerDeck.insert(new Card(CardNumber.TEN, CardSeed.SPADES, CardColor.BLACK));

        // Verifica che isBlackjack restituisca false
        assertFalse(blackjack.isBlackjack(), "isBlackjack should return false when player does not have blackjack");
    }

    /*
     * Verifica che lo split abbia successo quando le due carte iniziali del
     * giocatore hanno lo stesso valore
     */
    @Test
    public void testSplitSuccessfulWhenPossible() {
        // Prepara il mazzo del giocatore con due carte dello stesso valore
        Deck playerDeck0 = blackjack.getPlayerDeck(0);
        Deck playerDeck1 = blackjack.getPlayerDeck(1);
        playerDeck0.emptyDeck();
        playerDeck1.emptyDeck();

        playerDeck0.insert(new Card(CardNumber.EIGHT, CardSeed.HEARTS, CardColor.RED));
        playerDeck0.insert(new Card(CardNumber.EIGHT, CardSeed.DIAMONDS, CardColor.RED));

        // Tenta di eseguire lo split
        blackjack.split();

        // Verifica che i mazzi del giocatore abbiano una carta ciascuno dopo lo split
        assertEquals(1, playerDeck0.size(), "Player deck 0 should have 1 card after split");
        assertEquals(1, playerDeck1.size(), "Player deck 1 should have 1 card after split");
    }

    /*
     * Assicura che venga lanciata una RuntimeException quando si tenta di eseguire
     * uno split non valido
     */
    @Test
    public void testSplitFailsWhenNotPossible() {
        // Prepara il mazzo del giocatore con due carte di valori diversi
        Deck playerDeck0 = blackjack.getPlayerDeck(0);
        playerDeck0.emptyDeck();

        playerDeck0.insert(new Card(CardNumber.NINE, CardSeed.HEARTS, CardColor.RED));
        playerDeck0.insert(new Card(CardNumber.TEN, CardSeed.DIAMONDS, CardColor.RED));

        // Tenta di eseguire lo split, dovrebbe lanciare RuntimeException
        assertThrows(RuntimeException.class, () -> {
            blackjack.split();
        }, "split should throw RuntimeException when split is not possible");
    }

    /*
     * Controlla che il dealer peschi carte aggiuntive fino a raggiungere almeno 17
     * punti, come da regole del blackjack
     */
    @Test
    public void testDealerPlaysAccordingToRules() {
        // Prepara il mazzo del dealer
        Deck dealerDeck = blackjack.getDealerDeck();
        dealerDeck.emptyDeck();

        // Il dealer ha un totale di 16
        dealerDeck.insert(new Card(CardNumber.SEVEN, CardSeed.HEARTS, CardColor.RED));
        dealerDeck.insert(new Card(CardNumber.NINE, CardSeed.DIAMONDS, CardColor.RED));

        // Mostra il risultato, il dealer dovrebbe ricevere carte aggiuntive fino a
        // raggiungere almeno 17
        blackjack.showResult();

        // Il totale del dealer dovrebbe essere almeno 17
        int dealerTotal = dealerDeck.countCard();
        assertTrue(dealerTotal >= 17, "Dealer's total should be at least 17");
    }

    /* Verifica che dopo showResult, tutte le carte del dealer siano scoperte */
    @Test
    public void testFlipAllDealerCardsAfterShowResult() {
        // Prepara il mazzo del dealer con carte coperte
        Deck dealerDeck = blackjack.getDealerDeck();
        dealerDeck.emptyDeck();

        Card card1 = new Card(CardNumber.FIVE, CardSeed.HEARTS, CardColor.RED);
        Card card2 = new Card(CardNumber.SIX, CardSeed.DIAMONDS, CardColor.RED);
        dealerDeck.insert(card1);
        dealerDeck.insert(card2);

        // Chiama showResult
        blackjack.showResult();

        // Verifica che tutte le carte del dealer siano scoperte (flip == false)
        for (int i = 0; i < dealerDeck.size(); i++) {
            assertFalse(dealerDeck.checkCardFromDeck(i).isFlip(), "Dealer's cards should be face-up after showResult");
        }
    }

    @Test
    public void testCheckAndChangeDeckWhenCurrentDeckIsEmpty() throws Exception {
        // Accedi al campo privato 'playDeck' tramite reflection
        Field playDeckField = Blackjack.class.getDeclaredField("playDeck");
        playDeckField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Deck> playDeck = (List<Deck>) playDeckField.get(blackjack);

        // Svuota il mazzo corrente
        playDeck.get(0).emptyDeck();

        // Chiama una carta, che dovrebbe causare il passaggio al prossimo mazzo
        blackjack.call(0);

        // Verifica che il mazzo del giocatore abbia una carta
        assertEquals(1, blackjack.getPlayerDeck(0).size(),
                "Player's deck should have 1 card after drawing from new deck");
    }

    @Test
    public void testCheckAndChangeDeckThrowsExceptionWhenNoDecksLeft() throws Exception {
        // Accedi al campo privato 'playDeck' tramite reflection
        Field playDeckField = Blackjack.class.getDeclaredField("playDeck");
        playDeckField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Deck> playDeck = (List<Deck>) playDeckField.get(blackjack);

        // Svuota tutti i mazzi di gioco
        for (Deck deck : playDeck) {
            deck.emptyDeck();
        }

        // Tenta di chiamare una carta, dovrebbe lanciare IllegalAccessError
        assertThrows(IllegalAccessError.class, () -> {
            blackjack.call(0);
        }, "call should throw IllegalAccessError when no decks are left");
    }

    /*
     * Verifica che il metodo countCard calcoli correttamente il totale dei punti
     * nel mazzo.
     */
    @Test
    public void testCountCardCalculatesCorrectTotal() {
        // Prepara un mazzo con carte note
        Deck deck = new Deck();
        deck.insert(new Card(CardNumber.TWO, CardSeed.CLUBS, CardColor.BLACK));
        deck.insert(new Card(CardNumber.THREE, CardSeed.DIAMONDS, CardColor.RED));
        deck.insert(new Card(CardNumber.FIVE, CardSeed.HEARTS, CardColor.RED));

        // Verifica che countCard restituisca il totale corretto
        assertEquals(10, deck.countCard(), "Deck count should be 10");
    }

    /* Controlla che il metodo flipAll scopra tutte le carte nel mazzo. */
    @Test
    public void testFlipAllFlipsAllCardsInDeck() {
        // Prepara un mazzo con carte coperte
        Deck deck = new Deck();
        Card card1 = new Card(CardNumber.FOUR, CardSeed.CLUBS, CardColor.BLACK);
        Card card2 = new Card(CardNumber.SIX, CardSeed.SPADES, CardColor.BLACK);
        deck.insert(card1);
        deck.insert(card2);

        // Scopri tutte le carte
        deck.flipAll();

        // Verifica che tutte le carte siano scoperte (flip == false)
        for (int i = 0; i < deck.size(); i++) {
            assertFalse(deck.checkCardFromDeck(i).isFlip(), "All cards should be face-up after flipAll");
        }
    }

    /* Assicura che il metodo flipCard scopra una carta specifica nel mazzo. */
    @Test
    void testSplitWithEqualCards() {
        // Cerchiamo due carte uguali dal dealer deck
        Card firstCard = new Card(CardNumber.EIGHT, CardSeed.HEARTS, CardColor.RED);
        Card secondCard = new Card(CardNumber.EIGHT, CardSeed.SPADES,
                CardColor.BLACK);

        // Aggiungiamo le carte al deck del player
        blackjack.getPlayerDeck(0).insert(firstCard);
        blackjack.getPlayerDeck(0).insert(secondCard);

        // Effettuiamo lo split
        blackjack.split();

        // Verifica che lo split sia andato a buon fine
        assertEquals(1, blackjack.getPlayerDeck(1).size(),
                "Il secondo mazzo del player dovrebbe avere una carta dopo lo split");
        assertEquals(1, blackjack.getPlayerDeck(0).size(),
                "Il primo mazzo del player dovrebbe avere una carta dopo lo split");
    }
}
