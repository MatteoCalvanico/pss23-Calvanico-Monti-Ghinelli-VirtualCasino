package it.unibo.virtualCasino.model.games.impl.blackjack;

import java.util.Collections;
import java.util.List;

import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardColor;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardNumber;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardSeed;

import java.util.ArrayList;

public class Deck {
    private List<Card> deck;

    /**
     * Init a empty deck
     */
    public Deck(){
        this.deck = new ArrayList<>();
    }

    /**
     * Popolate the deck with 52 card. 
     * Every deck as 13 card for each seed. Example: "A♠", "2♠", "3♠", "4♠", "5♠", "6♠", "7♠", "8♠", "9♠", "10♠", "J♠", "Q♠", "K♠"
     */
    public void initPlayDeck(){
        for (CardSeed s : CardSeed.values()) { //We iterate for each seed
            for (CardNumber c : CardNumber.values()) { //For each seed we associate a number
                Card newC = new Card(c, s, (s == CardSeed.HEARTS || s == CardSeed.DIAMONDS) ? CardColor.RED : CardColor.BLACK); //The Heart and Diamonds have red cards, black for the other
                this.deck.add(newC);
            }
        }
    }

    /**
     * Look at a specific card in the deck
     * @param pos where to get the card
     * @return the card from the position (doesn't remove)
     */
    public Card checkCardFromDeck(int pos){
        return this.deck.get(pos);
    }

    /**
     * @return the number of card in the deck
     */
    public int size(){
        return this.deck.size();
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
     * Remove all the card from the deck
     */
    public void emptyDeck(){
        this.deck.clear();
    }

    /**
     * Insert a new card in the deck
     * @param c the card to insert
     */
    public void insert(Card c){
        this.deck.add(c);
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
