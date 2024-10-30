package it.unibo.virtualCasino.model.scoreboard;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unibo.virtualCasino.model.scoreboard.dtos.ScoreboardRecord;
import it.unibo.virtualCasino.model.scoreboard.helpers.PersistentStorageHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Scoreboard {
    private static final int MAX_SCOREBOARD_RECORDS = 10;
    private static final String SCOREBOARD_FILE = "scoreboard.json";
    private static final Gson gson = new Gson();

    private ArrayList<ScoreboardRecord> scoreBoardRecords;

    public Scoreboard() {
        this.scoreBoardRecords = loadScoreboard();
    }

    public ArrayList<ScoreboardRecord> getScoreBoardRecords() {
        ArrayList<ScoreboardRecord> records = new ArrayList<ScoreboardRecord>(this.scoreBoardRecords);

        // Sort by playerBalance in descending order
        records.sort(Comparator.comparingInt((ScoreboardRecord record) -> record.playerBalance).reversed());

        return records;
    };

    // Method to add a ScoreBoardRecord to the JSON file
    public void addRecord(ScoreboardRecord recordToAdd) {
        this.scoreBoardRecords.add(recordToAdd);
        this.scoreBoardRecords
                .sort(Comparator
                        .comparingInt((ScoreboardRecord record) -> record.playerBalance)
                        .reversed());

        // Remove last item until the list is of the correct size
        while (this.scoreBoardRecords.size() > MAX_SCOREBOARD_RECORDS) {
            this.scoreBoardRecords.remove(this.scoreBoardRecords.size() - 1);
        }

        saveScoreboard(this.scoreBoardRecords);
    }

    public static void saveScoreboard(ArrayList<ScoreboardRecord> records) {
        try (FileWriter writer = new FileWriter(PersistentStorageHelper.getStorageFile(SCOREBOARD_FILE))) {
            gson.toJson(records, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ScoreboardRecord> loadScoreboard() {
        try (FileReader reader = new FileReader(PersistentStorageHelper.getStorageFile(SCOREBOARD_FILE))) {
            ArrayList<ScoreboardRecord> records = gson
                    .fromJson(reader, new TypeToken<ArrayList<ScoreboardRecord>>() {
                    }.getType());

            return records != null ? records : new ArrayList<>(); // Ensure non-null return
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return empty list if file doesn't exist or on error
        }
    }
}