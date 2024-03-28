package it.unibo.virtualCasino.model.games.impl.blackjack;

import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.Games;

import java.util.List;
import java.util.ArrayList;

public class Blackjack implements Games{

    /**
     * The one who plays
     */
    private Player currentPlayer;

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

    /**
     * Init all we need for play Blackjack
     * @param numberOfDeck how many deck of card we want to use
     */
    public Blackjack(int numberOfDeck){
        this.playDeck = new ArrayList<>();
        for(int i = 0; i < numberOfDeck; i++){ //Creation and adding of the number of playing deck
            Deck newDeck = new Deck();
            newDeck.initPlayDeck();
            this.playDeck.add(newDeck);
        }
        this.playerDeck = new Deck[2];
        this.playerDeck[0] = new Deck();
        this.playerDeck[1] = new Deck();

        this.dealerDeck = new Deck();

        this.bet = new double[2];
        this.insurance = false;
    }

    @Override
    public void bet(double amount) {
        if (amount > this.currentPlayer.getAccount()){
            throw new IllegalArgumentException("The bet exceed account balance");
        }else{
            bet[0] = amount;
        }
    }

    public void bet(double amount, int deckNumber){ //Overloading the method
        if (amount > this.currentPlayer.getAccount()){
            throw new IllegalArgumentException("The bet exceed account balance");
        }else{
            bet[deckNumber] = amount;
        }
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

    /**
     * Ask the dealer for another card
     */
    public void call(){
        // TODO

    }

    /**
     * Change between true and false the insurance variable
     */
    public void setInsurance(){
        this.insurance = (this.insurance == false) ? true : false;
    }

    /**
     * Check if the combination of card is a blackjack
     */
    private boolean isBlackjack(){
        return (playerDeck[0].size() == 2 && playerDeck[0].countCard() == 21) ? true : false; //Is possible to do blackjack if you do 21 with the first two cards the dealer give
    }

    /**
     * Check if the split is possible
     * @return true if possible, false otherwise
     */
    private boolean isSplit(){
        return (this.playerDeck[0].checkCardFromDeck(0).getCardNumber() == this.playerDeck[0].checkCardFromDeck(1).getCardNumber()) ? true : false;
    }

    /**
     * Take one card from the main player deck and put in the second deck [ONLY IF THE DECK HAVE TWO CARD WITH THE SAME VALUE]
     */
    public void split(){
        if (isSplit()) {
            this.playerDeck[1].insert(this.playerDeck[0].draw());
        }else{
            throw new RuntimeException("Impossible to split");
        }
    }
}
