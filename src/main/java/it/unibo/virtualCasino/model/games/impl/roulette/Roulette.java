package it.unibo.virtualCasino.model.games.impl.roulette;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.Games;
import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteColors;
import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteBase;
import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteBetTypes;

/**
 * Represents a Roulette game, handling player bets, executing spins, and calculating outcomes.
 *
 * @author it.unibo.virtualCasino
 * @see Games
 * @see RouletteBase
 * @see Player
 * @see RouletteBet
 */
public class Roulette extends RouletteBase implements Games{

    private final int SEED_ARRAY_MAX_LENGHT = 30;

    private final int[][] tableMatrix = createRouletteTableMatrix();

    private final int seedArrayLenght = new Random().nextInt(SEED_ARRAY_MAX_LENGHT);

    private final byte[] seed = new byte[seedArrayLenght];

    private Player currentPlayer;

    private Map<String, RouletteBet> placedBets = new HashMap<>();
    
    /**
     * Places a bet on the Roulette game.
     *
     * @param amount The amount of money to bet.
     * @throws IllegalArgumentException If the total bet amount exceeds the player's account balance.
     */
    @Override
    public void bet(double amount) {
        double totalRiskedMoney = amount;
        for (Map.Entry<String, RouletteBet> entry : placedBets.entrySet()) {
          totalRiskedMoney += entry.getValue().getBetAmount();
        }
        if (totalRiskedMoney > currentPlayer.getAccount()) {
            throw new IllegalArgumentException("Total bets amount exceed account balance");
        }
    }

    /**
     * Prepares the game for a new round.
     *
     * This method resets the placed bets list to an empty HashMap, ensuring a fresh start for the next round.
     * Players need to place new bets before calling `showResult()`.
     */
    @Override
    public void nextRound() {
        this.placedBets = new HashMap<>();
    }

    /**
     * Simulates a Roulette spin, determines the winning number, and updates the player's account based on the results.
     */
    @Override
    public void showResult() {
        int winningNum = this.spinRoulette();
        int gameRes = this.getGameProfitOrLoss(winningNum);
        
        if (gameRes < 0) {
            this.currentPlayer.removeLost(Math.abs(gameRes));
        } else if (gameRes > 0) {
            this.currentPlayer.addWin(gameRes);
        }
    }

    /**
     * Constructs a new Roulette game instance for a specific player.
     *
     * @param player The Player object representing the game participant.
     */
    public Roulette(Player player) {
        new Random().nextBytes(seed);
        this.currentPlayer = player;
    }
    
    /**
     * Returns a copy of the current roulette table layout (2D integer array).
     *
     * @return a copy of the roulette table as an integer array
     */
    public int[][] getRouletteTable() {
        return this.tableMatrix;
    }

    /**
     * Returns a copy of the map that associates roulette numbers with their corresponding color enums.
     *
     * @return a copy of the roulette slots map
     */
    public Map<Integer, RouletteColors> getColorNumberMap() {
        return this.colorNumberMap;
    }

    /**
     * Creates a new bet and adds it to the list of placed bets.
     *
     * @param amount The amount of the bet.
     * @param betType The type of bet being placed.
     * @param betPositionInTable The position of the bet on the roulette table.
     * @return The updated map of placed bets.
     */
    public Map<String, RouletteBet> createBet(int amount, RouletteBetTypes betType, int betPositionInTable) {
        try {
            this.bet((double) amount);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }

        this.placedBets.put(
            this.generateRandomUuid(), 
            new RouletteBet(amount, betType, betPositionInTable)
        );        

        return this.placedBets;
    }

    /**
     * Deletes a bet from the list of placed bets.
     *
     * @param betId The unique identifier of the bet to be removed.
     * @return The updated map of placed bets.
     */
    public Map<String, RouletteBet> deleteBet(String betId) {
        this.placedBets.remove(betId);

        return this.placedBets;
    }
    
    /**
     * Generates a random roulette spin result using a SecureRandom object.
     *
     * @return The generated random number representing the spin result.
     */
    private int spinRoulette() {
        SecureRandom random = new SecureRandom(this.seed);
        return random.nextInt(this.NUMS_TOTAL + 1);
    }

    /**
     * Calculates the overall profit or loss for the game based on the winning number and placed bets.
     *
     * @param winningNumber The winning number of the spin.
     * @return The total profit or loss for the game.
     */
    private int getGameProfitOrLoss(int winningNumber) {
        int profitOrLoss = 0;

        for (Map.Entry<String, RouletteBet> betEntry : placedBets.entrySet()) {
            RouletteBet bet = betEntry.getValue();

            if (bet.getWinningNumbers().contains(winningNumber)) {
                profitOrLoss += bet.getPossibleWin();
            } else {
                profitOrLoss -= bet.getBetAmount();
            }
        }

        return profitOrLoss;
    }

    /**
     * Creates the initial roulette table layout as a 2D integer array.
     *
     * @return A 2D integer array representing the roulette table structure.
     */
    private int[][] createRouletteTableMatrix() {
        int[][] tempMatrix = new int[this.TABLE_ROWS][this.TABLE_COLS];

        for (int i = 0; i < tempMatrix.length; i++) {
            for (int j = 0; j < tempMatrix[i].length; j++) {
                tempMatrix[i][j] = i + 1;
            }
        }

        return tempMatrix;
    }

    /**
     * Generates a unique identifier for a bet.
     *
     * @return A randomly generated UUID string.
     */
    private String generateRandomUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}