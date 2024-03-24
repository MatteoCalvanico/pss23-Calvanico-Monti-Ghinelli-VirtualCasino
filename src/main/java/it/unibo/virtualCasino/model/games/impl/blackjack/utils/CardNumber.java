package it.unibo.virtualCasino.model.games.impl.blackjack.utils;

/**
 * Enums representing the number of French cards
*/
public enum CardNumber {
  ACE_ONE(1, "ACE"),
  ACE_ELEVEN(11, "ACE"), // The ACE can be both one or eleven [PLAYER DECIDE]
  TWO(2, "TWO"),
  THREE(3, "THREE"),
  FOUR(4, "FOUR"),
  FIVE(5, "FIVE"),
  SIX(6, "SIX"),
  SEVEN(7, "SEVEN"),
  EIGHT(8, "EIGHT"),
  NINE(9, "NINE"),
  TEN(10, "TEN"),
  JACK(10, "JACK"),
  QUEEN(10, "QUEEN"),
  KING(10, "KING");

  private final int value;
  private final String displayName;
  
  private CardNumber(int val, String displayName) { //The constructor is private because the cards' value are already set
    this.value = val;
    this.displayName = displayName;
  }

  /**
   * @return the value of the card
   */
  public int getValue() {
      return this.value;
  }

  /**
   * @return the displayName of the card
   */
  public String getName() {
      return this.displayName;
  }
}
