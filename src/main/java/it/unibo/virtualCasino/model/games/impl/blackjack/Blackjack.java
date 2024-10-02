package it.unibo.virtualCasino.model.games.impl.blackjack;

import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.Games;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a blackjack game, handling player bets and deck creation and management
 * @author it.unibo.virtualCasino
 * @see Deck
 * @see Card
 * @see Player
 */
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
     * Indicates which deck we are currently using
     */
    private int playDeckIndex = 0;

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
     * @param player the one whos play
     */
    public Blackjack(int numberOfDeck, Player player){
        this.currentPlayer = player;

        this.playDeck = new ArrayList<>();
        for(int i = 0; i < numberOfDeck; i++){ //Creation and adding of the number of playing deck
            Deck newDeck = new Deck();
            newDeck.initPlayDeck();
            newDeck.shuffleDeck();
            this.playDeck.add(newDeck);
        }
        this.playerDeck = new Deck[2];
        this.playerDeck[0] = new Deck();
        this.playerDeck[1] = new Deck();

        this.dealerDeck = new Deck();

        this.bet = new double[2];
        this.insurance = false;
    }

    /**
     * Return the dealer deck
     * @return the dealer deck
     */
    public Deck getDealerDeck(){
        return this.dealerDeck;
    }

    /**
     * Return the player deck
     * @param deckNumber indicates which deck we want to return
     * @return the dealer deck
     */
    public Deck getPlayerDeck(int deckNumber){
        return this.playerDeck[deckNumber];
    }

    /**
     * Bet the amount the money passed
     * @param amount how much money you want to bet
     * @throws IllegalArgumentException if the bet exceed the balance
     */
    @Override
    public void bet(double amount) throws IllegalArgumentException {
        if (amount > this.currentPlayer.getAccount()){
            throw new IllegalArgumentException("The bet exceed account balance");
        }else{
            bet[0] = amount;
        }
    }

    /**
     * Bet the amount the money passed
     * @param amount how much money you want to bet
     * @param deckNumber indicates which deck the bet refers to
     * @throws IllegalArgumentException if the bet exceed the balance
     */
    public void bet(double amount, int deckNumber) throws IllegalArgumentException { //Overloading the method: bet(double amount)
        if (amount > this.currentPlayer.getAccount()){
            throw new IllegalArgumentException("The bet exceed account balance");
        }else{
            bet[deckNumber] = amount;
        }
    }

    /**
     * Cleans the playing field
     */
    @Override
    public void nextRound() {
        checkAndChangeDeck();

        this.dealerDeck.emptyDeck();
        this.playerDeck[0].emptyDeck();
        this.playerDeck[1].emptyDeck();

        this.bet[0] = 0;
        this.bet[1] = 0;

        this.insurance = false;
    }

    /**
     * End the round, dealer flip and add the cards, checks who won
     */
    @Override
    public void showResult() {
        this.dealerDeck.flipAll();

        //If the player exceeds 21 he immediately loses
        if (this.playerDeck[0].countCard() > 21) {
            this.currentPlayer.removeLoss(this.bet[0]);

            if (this.playerDeck[1].countCard() > 21 || this.playerDeck[1].countCard() == 0) { //Check if the second deck is used and if exceeds 21. If the second deck is not used is usless to go on
                this.currentPlayer.removeLoss(this.bet[1]);
                return;
            }
        }
 
        while (this.dealerDeck.countCard() <= 16) { //Apply "Regola del banco"
            receive(1);
        }
        this.dealerDeck.flipAll();

        int dealerCount = this.dealerDeck.countCard();
        int playerCount0 = this.playerDeck[0].countCard();
        int playerCount1 = this.playerDeck[1].countCard();

        if (dealerCount > 21) { //Dealer exceeds 21 - Player WON
            this.currentPlayer.addWin(this.bet[0] + this.bet[1]);
        }else{
            if (dealerCount > playerCount0) { //Dealer WON
                this.currentPlayer.removeLoss(this.bet[0]);
            }
            if (dealerCount > playerCount1) { //Dealer WON
                this.currentPlayer.removeLoss(this.bet[1]);
            }

            if (playerCount0 > dealerCount) { //Player WON
                this.currentPlayer.addWin(this.bet[0]);
            }
            if (playerCount1 > dealerCount) { //Player WON
                this.currentPlayer.addWin(this.bet[1]);
            }
        }
    }

    /**
     * Ask the dealer for another card (already flipped)
     * @param deckNumber the deck that receives the card
     */
    public void call(int deckNumber){
        checkAndChangeDeck(); //Check if the deck is over and change it if necessary
         
        this.playerDeck[deckNumber].insert(this.playDeck.get(usedPlayDeck()).draw());
        this.playerDeck[deckNumber].flipAll();
    }

    /**
     * Give the dealer a number of cards
     * @param numberOfCard the number of cards to add
     */
    public void receive(int numberOfCard){
        for(int i = 0; i < numberOfCard; i++ ){
            checkAndChangeDeck(); //Check if the deck is over and change it if necessary

            this.dealerDeck.insert(this.playDeck.get(usedPlayDeck()).draw());
        }
    }

    /**
     * It says the index of the used playDeck
     * @return the index
     */
    private int usedPlayDeck(){
        return this.playDeckIndex;
    }

    /**
     * Increment by one playDeckIndex
     */
    private void setplayDeckIndex(){
        this.playDeckIndex++;
    }

    /**
     * Check and change the playDeck if is over. If all the deck is over throw an exception
     * @throws IllegalAccessError if all the deck used to play are over
     */
    private void checkAndChangeDeck() throws IllegalAccessError{
        if (this.playDeck.get(usedPlayDeck() + 1) != null) {
            Deck currentPlayDeck = this.playDeck.get(usedPlayDeck());
            if(currentPlayDeck.size() == 0 ){
                setplayDeckIndex();
            }
        }else{
            throw new IllegalAccessError("All playDecks are over");
        }
    }

    /**
     * Change between true and false the insurance variable
     */
    public void setInsurance(){
        this.insurance = (this.insurance == false) ? true : false;
    }

    /**
     * @return the value of insurace
     */
    public boolean checkInsurance(){
        return this.insurance;
    } 

    /**
     * Check if the combination of card is a blackjack in the player deck
     * @return true if the player made a blackjack
     */
    public boolean isBlackjack(){
        return (playerDeck[0].size() == 2 && playerDeck[0].countCard() == 21) ? true : false; //Is possible to do blackjack if you do 21 with the first two cards the dealer give
    }

    /**
     * Check if the split is possible
     * @return true if possible, false otherwise
     */
    private boolean isSplit(){
        if (this.playerDeck[0].size() == 2 && this.playerDeck[1].size() == 0) {
            return (this.playerDeck[0].checkCardFromDeck(0).getCardNumber() == this.playerDeck[0].checkCardFromDeck(1).getCardNumber()) ? true : false;            
        } else {
            return false;
        }
    }

    /**
     * Take one card from the main player deck and put in the second deck [ONLY IF THE DECK HAVE TWO CARD WITH THE SAME VALUE]
     * @throws RuntimeException if you try to split a non-splitting deck
     */
    public void split() throws RuntimeException{
        if (isSplit()) {
            this.playerDeck[1].insert(this.playerDeck[0].draw());
        }else{
            throw new RuntimeException("Split is not possible, you need two card with the same value");
        }
    }
}
