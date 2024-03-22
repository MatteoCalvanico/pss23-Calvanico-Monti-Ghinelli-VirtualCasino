package it.unibo.virtualCasino.model;

/**
* This class represent the one who play
*/
public class Player {

    private double account; //Total amount of money the player have
    private String name;    //Player's name [NOT EDITABLE AFTER THE INIT]
    
    /**
    * Each player instance have a unique name and start with 1000 credits
    */
    public Player(String newName){
        this.name = newName; //TODO: check the leaderboard 
        this.account = 1000.0;
    }

    /**
     * Return the player's name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Return the player's credits
     */
    public double getAccount(){
        return this.account;
    }

    /**
    * Add the amount of credit to the player's account
    */
    public void addWin(double amount){
        this.account += amount;
    }

    /**
    * Remove the amount of credit to the player's account
    */
    public void removeLost(double amount){
        this.account -= amount;
    }
}
