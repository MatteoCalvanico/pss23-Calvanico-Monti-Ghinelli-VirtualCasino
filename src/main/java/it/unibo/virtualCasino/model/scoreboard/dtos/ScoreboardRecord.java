package it.unibo.virtualCasino.model.scoreboard.dtos;

public class ScoreboardRecord {
    public final String playerName;

    public final Integer playerBalance;

    public ScoreboardRecord(String playerName, int playerBalance) {
        this.playerName = playerName;
        this.playerBalance = playerBalance;
    }
}
