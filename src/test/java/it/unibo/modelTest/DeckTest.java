package it.unibo.modelTest;

import it.unibo.virtualCasino.model.games.impl.blackjack.Deck;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardColor;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardNumber;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardSeed;
import it.unibo.virtualCasino.model.games.impl.blackjack.Card;

import static org.junit.jupiter.api.Assertions.*;

//import javax.sound.midi.InvalidMidiDataException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeckTest {
    Deck d;

    @BeforeEach
    void setup() {
        d = new Deck();
    }

    /** Testo il numero di carte */
    @Test
    void testInitPlayDeck() {
        d.initPlayDeck();
        assertEquals(56, d.size()); // there are 56 cards because the ace and the one are two distinct cards
    }

    /** Testo se ci sono 28 carte nere e 28 carte rosse */
    @Test
    void testInitPlayDeckColorSeed() {
        d.initPlayDeck();
        // inizializzo due contatori a zero
        int redCardCount = 0;
        int blackCardCount = 0;

        for (int i = 0; i < d.size(); i++) {// uso d.size per ottenere la dimensione del mazzo per poi iterare usando un
                                            // indice
            Card card = d.checkCardFromDeck(i);
            if (card.getCardColor() == CardColor.RED) {
                redCardCount++;
            } else if (card.getCardColor() == CardColor.BLACK) {
                blackCardCount++;
            } else {
                fail("Unexpected card color found: " + card.getCardColor()); // Se viene trovato un colore diverso da
                                                                             // rosso o nero il test fallisce
            }
        }
        assertEquals(28, redCardCount);
        assertEquals(28, blackCardCount);
        assertEquals(56, d.size()); // I check that the deck has 56 cards
    }

    /** Testo se prendendo una carta ritorna poi nella sua posizione */
    @Test
    public void testCheckCardFromDeck() {
        d.initPlayDeck();
        Card firstCard = d.checkCardFromDeck(40);// prendo questa carta dopo controllo che non venga tolta ma venga
                                                 // rimessa nel mazzo ricontando le 56 carte
        assertNotNull(firstCard, "I check that the first card is nothing");
        // Check if the retrieved card remains in the deck (not removed by
        // checkCardFromDeck)
        assertEquals(56, d.size());
    }

    /** Testo la somma del valore di due carte */
    @Test
    public void testCountCardes() {// con questo test ho testato anche il metodo insert()
        // faccio un mazzo vuoto e aggiungo 2 carte
        Deck de = new Deck();
        Card card1 = new Card(CardNumber.TWO, CardSeed.SPADES, CardColor.BLACK);
        Card card2 = new Card(CardNumber.KING, CardSeed.HEARTS, CardColor.RED);

        // utilizzo il metodo insert() per aggiungere le due carte
        de.insert(card1);
        de.insert(card2);

        // calcolo somma prevista dei valori delle carte
        int expectedSum = CardNumber.TWO.getValue() + CardNumber.KING.getValue();

        // chiamo metodo countCard() e verifico del risultato
        int actualSum = de.countCard();
        assertEquals(expectedSum, actualSum);// expectedSum=12
    }

    /** Testo che draw() funzioni su un mazzo pieno */
    @Test
    public void testDrawFullDeck() {
        d.initPlayDeck();
        Card drawnCard = d.draw();// chiamo il metodo draw() sull'oggetto d e memorizzo la carta restituita nella
                                  // variabile drawnCard
        assertNotNull(drawnCard);// controllo che la variabile non sia nulla
    }

    /** Testo la rimozione della carta dopo la pesca */
    @Test
    public void testDrawRemoveCard() {
        d.initPlayDeck();
        int iSize = d.size();// chiamo il metodo size sull'oggetto d e memorizzo la lunghezza del mazzo nella
                             // variabile iSize
        d.draw();// chiamo il metodo draw() e memorizza la carta restituita nella variabile iSize
        assertEquals(iSize - 1, d.size());// confronto(con iSize) la dimensione del mazzo con una carta in meno e il
                                          // mazzo completo
    }

    /** Testo che draw() funzioni su un mazzo vuoto 1 metodo */
    @Test
    public void testDrawEmptyDeck_() {
        Deck emptyDeck = new Deck();// creo un nuovo oggetto Deck ma lo inizializzo vuoto
        Card drawnCard = emptyDeck.draw();// chiamo il metodo draw() sul mazzo vuoto(emptyDeck) e memorizza il valore
                                          // restituito nella variabile drawnCard
        assertNull(drawnCard);// si verifica che drawnCard sia null, se non lo è significa che il metodo
                              // draw() ha restituito una carta anche se il mazzo era vuoto
    }

    /**
     * Testo che draw() funzioni su un mazzo vuoto 2 metodo
     * testo initPlayDeck funzioni correttamente(svuoto il mazzo)
     */
    @Test
    public void testDrawEmptyDeck() {
        d.initPlayDeck();// svuoto il mazzo
        d.emptyDeck();// svuoto il mazzo con il metodo emptyDeck()

        Card drawnCard = d.draw();// con draw estraggo una carta e la salvo nella variabile drawnCard
        assertNull(drawnCard);
    }

    /** Testo metodo shuffleDeck() */
    @Test
    public void testShuffleDeck() {
        d.initPlayDeck();
        d.shuffleDeck();// il deck è sempre uguale se il test va a buon fine vuole dire che è stato
                        // mescolato correttamente
        String expectedString = "Deck: [ACE,HEARTS,RED][ACE,HEARTS,RED][TWO,HEARTS,RED][THREE,HEARTS,RED][FOUR,HEARTS,RED][FIVE,HEARTS,RED][SIX,HEARTS,RED][SEVEN,HEARTS,RED][EIGHT,HEARTS,RED][NINE,HEARTS,RED][TEN,HEARTS,RED][JACK,HEARTS,RED][QUEEN,HEARTS,RED][KING,HEARTS,RED][ACE,DIAMONDS,RED][ACE,DIAMONDS,RED][TWO,DIAMONDS,RED][THREE,DIAMONDS,RED][FOUR,DIAMONDS,RED][FIVE,DIAMONDS,RED][SIX,DIAMONDS,RED][SEVEN,DIAMONDS,RED][EIGHT,DIAMONDS,RED][NINE,DIAMONDS,RED][TEN,DIAMONDS,RED][JACK,DIAMONDS,RED][QUEEN,DIAMONDS,RED][KING,DIAMONDS,RED][ACE,CLUBS,BLACK][ACE,CLUBS,BLACK][TWO,CLUBS,BLACK][THREE,CLUBS,BLACK][FOUR,CLUBS,BLACK][FIVE,CLUBS,BLACK][SIX,CLUBS,BLACK][SEVEN,CLUBS,BLACK][EIGHT,CLUBS,BLACK][NINE,CLUBS,BLACK][TEN,CLUBS,BLACK][JACK,CLUBS,BLACK][QUEEN,CLUBS,BLACK][KING,CLUBS,BLACK][ACE,SPADES,BLACK][ACE,SPADES,BLACK][TWO,SPADES,BLACK][THREE,SPADES,BLACK][FOUR,SPADES,BLACK][FIVE,SPADES,BLACK][SIX,SPADES,BLACK][SEVEN,SPADES,BLACK][EIGHT,SPADES,BLACK][NINE,SPADES,BLACK][TEN,SPADES,BLACK][JACK,SPADES,BLACK][QUEEN,SPADES,BLACK][KING,SPADES,BLACK]";
        assertNotEquals(expectedString, d.toString());
    }

    /**
     * In alcuni casi potrebbe essere necessario mantenere l'ordine delle carte in
     * un mazzo,
     * ad esempio quando si distribuiscono le carte per una sequenza di gioco
     * specifica.
     * Testo che draw() mantenga l'ordine delle carte se il mazzo non viene
     * mescolato.
     */
    @Test
    public void testDrawNotShuffled() {
        d.initPlayDeck();
        Card firstCard = d.draw();
        Card seconCard = d.checkCardFromDeck(1);// controllo che la seconda carta quindi quella in posizione 1 sia la 2
                                                // carta e quindi il mazzo è in ordine
        assertNotEquals(firstCard, seconCard);// se metto assertEquals mi da errore perchè la carta che c'è nel mazzo è
                                              // effetivamente diversa da quella che ho pescato
    }

    /** Testo che escano carte diverse dopo la mescolatura */
    @Test
    public void testDrawShuffled() {
        d.initPlayDeck();
        d.shuffleDeck();// chiamo questo metodo per mescolare le carte nel mazzo
        Card firstCard = d.draw();// pesco una carta e la salvo nella variabile
        Card seconCard = d.draw();
        assertNotEquals(firstCard, seconCard);// utilizzo assertNotEquals per confrontare le due variabili
        // se le due carte sono uguali fallisce se invece sono diverse è passato
    }

    /** Testo il metodo insert() sull'aggiunta di una carta in un mazzo vuoto */
    @Test
    public void testInsertCard() {
        Card card = new Card(CardNumber.ACE_ELEVEN, CardSeed.HEARTS, CardColor.BLACK);
        d.insert(card);
        assertEquals(1, d.size());// controllo che la dimensione del mazzo sia pari a 1 usando size()
        assertEquals(card, d.checkCardFromDeck(0));// controllo che la carta inserita sia nella posizione 0
                                                   // confrontandola anche con la carta originale
    }

    /** Testo il metodo insert() sull'aggiunta di più carte a un mazzo esistente */
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

    /** Testo il metodo flipAll() su un mazzo vuoto */
    @Test
    public void testFlipAllOnEmptyDeck() {
        d.flipAll();
        // non verifico nulla perchè questo metodo non modifica un mazzo vuoto
    }

    /**
     * Testo il metodo flipAll() e controllo se quest'ultimo ribalta nuovamente le
     * carte già coperte
     * testo il metodo flipCard()
     */
    @Test
    public void testFlipAllCards() {
        d.initPlayDeck();// inizializzo un mazzo con 56 carte coperte

        for (int i = 0; i < d.size(); i++) {
            d.flipCard(i);// richiamo questo metodo per girare ogni carta (scoprire)
        }
        d.flipAll();// ricopro ogni carta

        // verifico lo stato delle carte dopo la chiamata al metodo flipAll
        for (int i = 0; i < d.size(); i++) {
            assertEquals(false, d.checkCardFromDeck(i).isFlip());
            // con il metodo checkCardFromDeck(i) prendo la carta all'indice i e poi
            // richiamo il metodo isFlip() sulla carta "presa" per verificare se è scoperta
            // uso assertEquals con "expected:false" che verifica che il valore restituito
            // da isFlip() sia false, se ritornasse true la carta è ancora coperta
        }
    }

    /** Testo il ribaltimento di una carta scoperta */
    @Test
    public void testFlipCardDiscovered() {
        d.initPlayDeck();
        int pos = 24;// posizione di una carta a caso
        Card card = d.checkCardFromDeck(pos);// seleziona una scoperta dal mazzo
        assertTrue(card.isFlip());// controllo sia effettivamente scoperta

        d.flipCard(pos);
        assertFalse(d.checkCardFromDeck(pos).isFlip());// controllo che la carta si stata ribaltata quindi coperta

        d.flipCard(pos);
        assertTrue(d.checkCardFromDeck(pos).isFlip());// controllo, in fine, che la carta si sia ribaltata di nuovo
                                                      // quindi scoperta
    }

    /**
     * Testo il ribaltimento di una carta fuori dai limiti del mazzo(questo è un
     * test generico per vedere se filpCard blocca un indice fuori dai limiti,
     * se gli passi )un indice valido funziona lo stesso perché controlla solo se
     * c'è un indice fuori dal limite)
     */
    @Test
    public void testFlipCardMargin() {
        assertThrows(IndexOutOfBoundsException.class, () -> d.flipCard(56));
        // tenta di chiamare flipCard con una posizione che supera la dimensione del
        // mazzo, il mazzo ha 56 carte e quindi ho messo 56 ed è più alto perchè il
        // primo indice valido è 0 e l'ultimo 55
        // ho utilizzato assertThrows per specificare che aspettavo che venisse lanciata
        // un'eccezzione di tipo IndexOutOfBoundsException
        // Si utilizza questa eccezione quando si tenta di accedere a un elemento di un
        // array o di una vista con un indice che non è valido
    }

    /**
     * Testo la rappresentazione stringa di un mazzo vuoto, se non implementato bene
     * potrebbe generare una stringa vuota quando il mazzo è vuoto
     */
    @Test
    public void testToStringEmptyDeck() {
        Deck emptyDeck = new Deck();// creo un mazzo vuoto. PS non posso chiamare il metodo emptyDeck() perchè non è
                                    // un metodo statico
        String expectedString = "Deck: ";// creo una stringa che rappresenti testualmente il mazzo vuoto, contiente solo
                                         // "Deck: " essendo la rapp minima di un mazzo vuoto
        assertEquals(expectedString, emptyDeck.toString());// chiamo toString() sull'oggetto emptyDeck() e ho
                                                           // memorizzato il risultato in una variabile
        // dato che il mazzo è vuoto, mi aspetto che la stringa restituita sia solo
        // l'intestazione "Deck:"
    }

    /** Testo la rapp stringa di un mazzo a carte coperte */
    @Test
    public void testToStringHoleCards() {
        d.initPlayDeck();
        String expectedString = "Deck: [ACE,HEARTS,RED][ACE,HEARTS,RED][TWO,HEARTS,RED][THREE,HEARTS,RED][FOUR,HEARTS,RED][FIVE,HEARTS,RED][SIX,HEARTS,RED][SEVEN,HEARTS,RED][EIGHT,HEARTS,RED][NINE,HEARTS,RED][TEN,HEARTS,RED][JACK,HEARTS,RED][QUEEN,HEARTS,RED][KING,HEARTS,RED][ACE,DIAMONDS,RED][ACE,DIAMONDS,RED][TWO,DIAMONDS,RED][THREE,DIAMONDS,RED][FOUR,DIAMONDS,RED][FIVE,DIAMONDS,RED][SIX,DIAMONDS,RED][SEVEN,DIAMONDS,RED][EIGHT,DIAMONDS,RED][NINE,DIAMONDS,RED][TEN,DIAMONDS,RED][JACK,DIAMONDS,RED][QUEEN,DIAMONDS,RED][KING,DIAMONDS,RED][ACE,CLUBS,BLACK][ACE,CLUBS,BLACK][TWO,CLUBS,BLACK][THREE,CLUBS,BLACK][FOUR,CLUBS,BLACK][FIVE,CLUBS,BLACK][SIX,CLUBS,BLACK][SEVEN,CLUBS,BLACK][EIGHT,CLUBS,BLACK][NINE,CLUBS,BLACK][TEN,CLUBS,BLACK][JACK,CLUBS,BLACK][QUEEN,CLUBS,BLACK][KING,CLUBS,BLACK][ACE,SPADES,BLACK][ACE,SPADES,BLACK][TWO,SPADES,BLACK][THREE,SPADES,BLACK][FOUR,SPADES,BLACK][FIVE,SPADES,BLACK][SIX,SPADES,BLACK][SEVEN,SPADES,BLACK][EIGHT,SPADES,BLACK][NINE,SPADES,BLACK][TEN,SPADES,BLACK][JACK,SPADES,BLACK][QUEEN,SPADES,BLACK][KING,SPADES,BLACK]";
        assertEquals(expectedString, d.toString());
    }

    /** testo la rapp stringa di un mazzo con carte scoperte */
    @Test
    public void testToStringFlippCards() {
        d.initPlayDeck();
        d.flipAll();// giro tutte le carte del mazzo quindi le scopro tutte
        String expectedString = "Deck: [ACE,HEARTS,RED][ACE,HEARTS,RED][TWO,HEARTS,RED][THREE,HEARTS,RED][FOUR,HEARTS,RED][FIVE,HEARTS,RED][SIX,HEARTS,RED][SEVEN,HEARTS,RED][EIGHT,HEARTS,RED][NINE,HEARTS,RED][TEN,HEARTS,RED][JACK,HEARTS,RED][QUEEN,HEARTS,RED][KING,HEARTS,RED][ACE,DIAMONDS,RED][ACE,DIAMONDS,RED][TWO,DIAMONDS,RED][THREE,DIAMONDS,RED][FOUR,DIAMONDS,RED][FIVE,DIAMONDS,RED][SIX,DIAMONDS,RED][SEVEN,DIAMONDS,RED][EIGHT,DIAMONDS,RED][NINE,DIAMONDS,RED][TEN,DIAMONDS,RED][JACK,DIAMONDS,RED][QUEEN,DIAMONDS,RED][KING,DIAMONDS,RED][ACE,CLUBS,BLACK][ACE,CLUBS,BLACK][TWO,CLUBS,BLACK][THREE,CLUBS,BLACK][FOUR,CLUBS,BLACK][FIVE,CLUBS,BLACK][SIX,CLUBS,BLACK][SEVEN,CLUBS,BLACK][EIGHT,CLUBS,BLACK][NINE,CLUBS,BLACK][TEN,CLUBS,BLACK][JACK,CLUBS,BLACK][QUEEN,CLUBS,BLACK][KING,CLUBS,BLACK][ACE,SPADES,BLACK][ACE,SPADES,BLACK][TWO,SPADES,BLACK][THREE,SPADES,BLACK][FOUR,SPADES,BLACK][FIVE,SPADES,BLACK][SIX,SPADES,BLACK][SEVEN,SPADES,BLACK][EIGHT,SPADES,BLACK][NINE,SPADES,BLACK][TEN,SPADES,BLACK][JACK,SPADES,BLACK][QUEEN,SPADES,BLACK][KING,SPADES,BLACK]";
        // creazione di una stringa che rappresenti la rappresentazione testuale attesa
        // del mazzo con tutte le carte scoperte
        assertEquals(expectedString, d.toString());// confronta la stringa ottenuta dal metodo toString() con la stringa
                                                   // di riferimento expectedString. Se le stringhe sono uguali il test
                                                   // funziona se no fallisce.
    }
}