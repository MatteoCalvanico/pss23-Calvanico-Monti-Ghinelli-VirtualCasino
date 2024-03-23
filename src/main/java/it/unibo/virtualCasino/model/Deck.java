package it.unibo.virtualCasino.model;

import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> deck;

    public Deck(){
        this.deck = createDeck();
    }

    /**
     * @return a brand new deck with 52 card
     */
    private List<Card> createDeck(){
        // TODO finish this
        throw new UnsupportedOperationException("Unimplemented method 'createDeck'");
    }

    /**
     * Shuffle the card in the deck
    */
    public void shuffleDeck(){
        Collections.shuffle(deck);
    }

    /**
     * Take the next card from the deck
     * @return a Card (null if the deck is empty)
    */
    public Card draw(){
        return (deck.isEmpty()) ? null : deck.remove(0);
    }
}
