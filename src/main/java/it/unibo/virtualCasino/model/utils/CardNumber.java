package it.unibo.virtualCasino.model.utils;

/**
 * Enums representing the number of French cards
*/
public enum CardNumber {
    ACE_ONE(1),
    ACE_ELEVEN(11), //The ACE can be both one or eleven [PLAYER DECIDE]
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(10),
    QUEEN(10),
    KING(10);
  
    private final int value;
  
    private CardNumber(int val) { //The constructor is private because the cards' value are already set
      this.value = val;
    }
  
    /**
     * @return the value of the card
     */
    public int getValue() {
      return this.value;
    }
  
}
