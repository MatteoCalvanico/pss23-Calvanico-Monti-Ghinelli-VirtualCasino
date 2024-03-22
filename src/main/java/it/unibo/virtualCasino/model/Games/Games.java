package it.unibo.virtualCasino.model.Games;

/**
* Interface that defines the basic structure of all games.
* The contract define:
* <pre>
- betting credits;
- pass to the next round;
- show the result of the current hand.
* </pre>
*/
public interface Games {
    
    public void bet(double amount);
    
    public void nextRound();

    public void showResult();
}
