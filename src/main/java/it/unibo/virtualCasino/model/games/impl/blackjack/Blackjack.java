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

    @Override
    public void bet(double amount) {
        if (amount > this.currentPlayer.getAccount()){
            throw new IllegalArgumentException("The bet exceed account balance");
        }else{
            bet[0] = amount;
        }
    }

    public void bet(double amount, int deckNumber){ //Overloading the method: bet(double amount)
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

    @Override
    public void showResult() { // TODO: DA FINIRE

        this.dealerDeck.flipAll();
        this.playerDeck[0].flipAll();

        //Flip the second deck only if is not empty
        if (this.playerDeck[1].size() != 0) {
            this.playerDeck[1].flipAll();
        }
    }

    /**
     * Starts the next round of blackjack, in order:
     * <ul>
            <li>give the dealer two card, one is hidden;</li>
            <li>give to the player two card, both flipped</li>   
            <li>check if the player or the dealer made a blackjack, if so he adds the win or removes the loss and ends the round</li>
        </ul>
     */
    public void startRound(){
        receive(2);
        this.dealerDeck.flipCard(0);

        call(0);
        call(0);

        if(isBlackjack() && this.dealerDeck.countCard() == 21){ //Both made blackjack
            this.dealerDeck.flipAll();
            nextRound();
            return;
        }
        if(isBlackjack()){ //Player made blackjack
            this.currentPlayer.addWin(this.bet[0] * 2);
            nextRound();
        }
        if(this.dealerDeck.countCard() == 21){ //Dealer made blackjack
            this.currentPlayer.removeLoss(this.bet[0]);
            this.dealerDeck.flipAll();
            nextRound();
        }
    }

    /**
     * Ask the dealer for another card (already flipped)
     * @param deckNumber the deck that receives the card
     */
    public void call(int deckNumber){
        this.playerDeck[deckNumber].insert(this.playDeck.get(usedPlayDeck()).draw());
        this.playerDeck[deckNumber].flipAll();
    }

    /**
     * Give the dealer a number of cards
     * @param numberOfCard the number of cards to add
     */
    public void receive(int numberOfCard){
        for(int i = 0; i < numberOfCard; i++ ){
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
     */
    private void checkAndChangeDeck(){
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
     * Check if the combination of card is a blackjack
     * @return true if the player made a blackjack
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
