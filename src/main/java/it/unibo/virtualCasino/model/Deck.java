package it.unibo.virtualCasino.model;

import it.unibo.virtualCasino.model.utils.CardSeed;
import it.unibo.virtualCasino.model.utils.CardColor;
import it.unibo.virtualCasino.model.utils.CardNumber;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Deck {
    private List<Card> deck;

    /**
     * Every deck as 13 card for each seed. Example: "A♠", "2♠", "3♠", "4♠", "5♠", "6♠", "7♠", "8♠", "9♠", "10♠", "J♠", "Q♠", "K♠"
     * @return a brand new deck with 52 card
     */
    public Deck(){
        this.deck = new ArrayList<>();
        for (CardSeed s : CardSeed.values()) { //We iterate for each seed
            for (CardNumber c : CardNumber.values()) { //For each seed we associate a number
                Card newC = new Card(c, s, (s == CardSeed.HEARTS || s == CardSeed.DIAMONDS) ? CardColor.RED : CardColor.BLACK); //The Heart and Diamonds have red cards, black for the other
                this.deck.add(newC);
            }
        }
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

    /**
     * @return the info of the deck
    */
    @Override
    public String toString() {
        String info = "Deck: ";
        for (Card card : deck) {
            info += card.toString();
        }
        return info;
    }
}
