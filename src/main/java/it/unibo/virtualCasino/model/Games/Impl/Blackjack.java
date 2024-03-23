package it.unibo.virtualCasino.model.Games.Impl;

import it.unibo.virtualCasino.model.Games.Games;
import it.unibo.virtualCasino.model.Deck;
import it.unibo.virtualCasino.model.Card;
import java.util.List;

public class Blackjack implements Games{

    /**
     * In blackjack we use 2 to 6 normal deck [52 card each]
     */
    private List<Deck> playDeck;

    /**
     * The cards the player have. The player have 2 deck, the normal one and one if he want to split
     */
    private Deck[] playerDeck;

    /**
     * The cards in the bench
     */
    private Deck dealerDeck;

    /**
     * Same thing with the playerDeck, in the 0 position we have the current bet and in the 1 position the bet for the split
     */
    private double[] bet;

    /**
     * The player can decide to make an insurance if the dealer have an ACE. 
     * If the dealer do a blackjack and the insurance is true the player don't lose any money
     */
    private boolean insurance; 

    @Override
    public void bet(double amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bet'");
    }

    @Override
    public void nextRound() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'nextRound'");
    }

    @Override
    public void showResult() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showResult'");
    }
    
}
