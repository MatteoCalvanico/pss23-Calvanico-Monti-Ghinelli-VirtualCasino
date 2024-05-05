package it.unibo.virtualCasino.controller.singleton;

import it.unibo.virtualCasino.model.Player;

/**
 * Singleton class (design pattern) to store the information of the player between scene
 * @author it.unibo.virtualCasino.controller
 * @see Player
 */
public final class PlayerHolder {

    /**
     * The instance of the current player
     */
    private Player playerHolded;

    /**
     * Instance we call to set and get the player info
     */
    private final static PlayerHolder INSTANCE = new PlayerHolder();


    //We leave the constructor private, so new instances can't be created
    private PlayerHolder() {}


    /**
     * Use it to get the current player
     * @return the instance of the player
     */
    public Player getPlayerHolded() {
        return playerHolded;
    }

    /**
     * Use it to save the player info and share it between scene
     * @param playerHolded
     */
    public void setPlayerHolded(Player playerHolded) {
        this.playerHolded = playerHolded;
    }


    /**
     * Get the instance to use the getter and setter
     * @return the PlayerHolder instance
     */
    public static PlayerHolder getInstance() {
        return INSTANCE;
    }
}
