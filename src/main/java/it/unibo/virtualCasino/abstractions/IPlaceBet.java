package it.unibo.virtualCasino.abstractions;

/**
 * Interface for placing bets in the virtual casino.
 * It defines methods for placing a bet and retrieving the total bet amount.
 */
public interface IPlaceBet {

    /**
     * Places a bet.
     * Contains the game specific logic for placing a bet
     */
    void placeBet();

    /**
     * Returns the total amount of bets placed.
     *
     * @return the total bet amount.
     */
    double getTotalBetsAmount();
}
