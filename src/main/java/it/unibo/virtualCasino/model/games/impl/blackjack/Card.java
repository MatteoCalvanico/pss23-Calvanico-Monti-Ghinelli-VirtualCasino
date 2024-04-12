package it.unibo.virtualCasino.model.games.impl.blackjack;

import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardColor;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardNumber;
import it.unibo.virtualCasino.model.games.impl.blackjack.utils.CardSeed;

/**
*  the class simulates a generic playing card of a French deck
*/
public class Card {

    private final CardNumber number;
    private final CardSeed seed;
    private final CardColor color;
    private boolean flip;

    public Card(CardNumber cardNum, CardSeed cardSeed, CardColor cardColor){
        this.color = cardColor;
        this.seed = cardSeed;
        this.number = cardNum;
        this.flip = true;
    }

    /**
     * @return the value of the card
     */
    public int getCardNumber(){
        return this.number.getValue();
    }

    /**
     * @return the seed of the car
     */
    public CardSeed getCardSeed(){
        return this.seed;
    }

    /**
     * @return the color of the card
    */
    public CardColor getCardColor(){
        return this.color;
    }

    /**
     * Indicates whether the card is flipped or not. True means the card is hidden
     * @return a boolean
     */
    public boolean isFlip(){
        return this.flip;
    }

    /**
     * Change the state of the flip var
     */
    public void flip(){
        this.flip = (this.flip == true) ? false : true;
    }

    /**
     * @return the info of the card in this syntax: [10,HEART,RED] 
    */
    @Override
    public String toString() {
        return "[" + this.number.getName() + "," + this.seed + "," + this.color + "]";
    }
    
}
