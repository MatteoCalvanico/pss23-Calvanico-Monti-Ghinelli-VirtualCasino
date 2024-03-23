package it.unibo.virtualCasino.model;

import it.unibo.virtualCasino.model.utils.CardColor;
import it.unibo.virtualCasino.model.utils.CardNumber;
import it.unibo.virtualCasino.model.utils.CardSeed;

/**
*  the class simulates a generic playing card of a French deck
*/
public class Card {

    private final CardNumber number;
    private final CardSeed seed;
    private final CardColor color;

    public Card(CardNumber cardNum, CardSeed cardSeed, CardColor cardColor){
        this.color = cardColor;
        this.seed = cardSeed;
        this.number = cardNum;
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
     * Return the info of the card in this syntax: [10,HEART,RED] 
    */
    @Override
    public String toString() {
        return "[" + this.number.getValue() + "," + this.seed + "," + this.color + "]";
    }
    
}
