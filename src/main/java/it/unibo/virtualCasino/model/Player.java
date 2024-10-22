package it.unibo.virtualCasino.model;

import it.unibo.virtualCasino.controller.IPlaceBetObj;

/**
 * This class represent the one who play
 */
public class Player {

    private double account; // Total amount of money the player have
    private String name; // Player's name [NOT EDITABLE AFTER THE INIT]

    /**
     * Each player instance have a unique name and start with 1000 credits
     */
    public Player(String newName) {
        this.name = newName; // TODO: check the leaderboard
        this.account = 1000.0;
    }

    /**
     * Return the player's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the player's credits
     */
    public double getAccount() {
        return this.account;
    }

    /**
     * Add the amount of credit to the player's account
     */
    public void addWin(double amount) {
        this.account += amount;
    }

    /**
     * Remove the amount of credit to the player's account
     */
    public void removeLoss(double amount) {
        this.account -= amount;
    }

    /**
     * Places a bet for the player, if the total bet amount does not exceed the
     * player's account balance.
     *
     * @param placeBetObj the bet object containing the bet details.
     * @throws Exception if the total bet amount exceeds the player's balance.
     */
    public void placeBet(IPlaceBetObj placeBetObj) throws Exception {
        if (!isPlayerSolvent(placeBetObj.getTotalBetsAmount())) {
            throw new Exception("Unable to play, bets amount at risk exceeds player balance");
        }

        placeBetObj.placeBet();
    }

    /**
     * Checks if the player has enough balance to place a bet.
     *
     * @param amountAtRisk the total amount at risk for the bet.
     * @return {@code true} if the player has sufficient balance, {@code false}
     *         otherwise.
     */
    public boolean isPlayerSolvent(double amountAtRisk) {
        return amountAtRisk <= account;
    }
}
