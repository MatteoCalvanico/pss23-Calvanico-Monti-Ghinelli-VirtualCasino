package it.unibo.virtualCasino.model.scoreboard.dtos;

/**
 * The {@code ScoreboardRecord} class represents a single record in the
 * scoreboard,
 * holding information about a player's name and their final balance.
 */
public class ScoreboardRecord {
    /** The name of the player associated with this record. */
    public final String playerName;

    /** The final balance of the player associated with this record. */
    public final double playerBalance;

    /**
     * Constructs a new {@code ScoreboardRecord} with the specified player name and
     * balance.
     *
     * @param playerName    the name of the player.
     * @param playerBalance the final balance of the player.
     */
    public ScoreboardRecord(String playerName, double playerBalance) {
        this.playerName = playerName;
        this.playerBalance = playerBalance;
    }
}
