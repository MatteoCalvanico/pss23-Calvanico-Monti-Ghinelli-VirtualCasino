package it.unibo.virtualCasino.model.scoreboard;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.unibo.virtualCasino.helpers.PersistentStorageHelper;
import it.unibo.virtualCasino.model.scoreboard.dtos.ScoreboardRecord;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The {@code Scoreboard} class provides static methods to manage the scoreboard
 * of players, which includes loading, saving, and adding new records to a
 * persistent storage file.
 * 
 * <p>
 * The scoreboard is stored in JSON format and maintains a maximum of ten
 * records, ranked by player balance in descending order.
 * </p>
 */
public final class Scoreboard {
    /** The maximum number of records allowed on the scoreboard. */
    private static final int MAX_SCOREBOARD_RECORDS = 10;

    /** The filename for the JSON file where scoreboard data is stored. */
    private static final String SCOREBOARD_FILE = "scoreboard.json";

    /** Gson instance for JSON serialization and deserialization. */
    private static final Gson gson = new Gson();

    private static final List<ScoreboardRecord> records = new ArrayList<>();
    private static final Path STORAGE_FILE = Path.of(System.getProperty("user.home"), ".virtual-casino",
            "scoreboard.json");

    /**
     * Private constructor to prevent instantiation of this class.
     */
    private Scoreboard() {
    };

    /**
     * Loads and returns the scoreboard records from persistent storage.
     * If the file does not exist or cannot be read, an empty list is returned.
     * The returned list is sorted by player balance in descending order.
     *
     * @return a sorted list of {@code ScoreboardRecord} objects, or an empty list
     *         if an error occurs.
     */
    public static ArrayList<ScoreboardRecord> loadScoreboard() {
        try (FileReader reader = new FileReader(PersistentStorageHelper.getStorageFile(SCOREBOARD_FILE))) {
            // Deserialize json file to ScoreboardRecord objects list
            ArrayList<ScoreboardRecord> records = gson
                    .fromJson(reader, new TypeToken<ArrayList<ScoreboardRecord>>() {
                    }.getType());

            // Sort and return
            if (records != null) {
                ensureRecordListFormat(records);
                return records;
            }
            // If null retruns empty list
            else {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return empty list if file doesn't exist or on error
        }
    };

    /**
     * Adds a new record to the scoreboard if it qualifies. If the scoreboard is at
     * maximum capacity and the new record has a lower balance than the lowest on
     * the scoreboard, the record is not added.
     *
     * @param recordToAdd the {@code ScoreboardRecord} to be added.
     * @return {@code true} if the record was added, {@code false} otherwise.
     */
    public static boolean addRecord(ScoreboardRecord recordToAdd) {
        ArrayList<ScoreboardRecord> records = loadScoreboard();

        if (records.size() == MAX_SCOREBOARD_RECORDS
                && records.get(MAX_SCOREBOARD_RECORDS - 1).playerBalance > recordToAdd.playerBalance) {
            return false;
        }

        records.add(recordToAdd);
        saveScoreboard(records);
        return true;
    }

    /**
     * Checks if the scoreboard contains a record with the specified player name.
     *
     * @param name the player name to search for.
     * @return {@code true} if a record with the specified name exists,
     *         {@code false} otherwise.
     */
    public static boolean containsName(String name) {
        return loadScoreboard()
                .stream()
                .anyMatch(record -> record.playerName.equals(name));
    }

    /**
     * Saves the list of scoreboard records to persistent storage in JSON format.
     * The list is sorted and trimmed to the maximum allowed number of records
     * before saving.
     *
     * @param records the list of {@code ScoreboardRecord} objects to save.
     */
    private static void saveScoreboard(ArrayList<ScoreboardRecord> records) {
        try (FileWriter writer = new FileWriter(PersistentStorageHelper.getStorageFile(SCOREBOARD_FILE))) {
            ensureRecordListFormat(records);
            gson.toJson(records, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ensures that the list of scoreboard records is sorted by player balance in
     * descending order and is trimmed to the maximum allowed number of records.
     *
     * @param records the list of {@code ScoreboardRecord} objects to format.
     */
    private static void ensureRecordListFormat(ArrayList<ScoreboardRecord> records) {
        // Sort by balance descending
        records.sort(Comparator
                .comparingDouble((ScoreboardRecord record) -> record.playerBalance)
                .reversed());

        // Remove last item until the list is of the correct size
        while (records.size() > MAX_SCOREBOARD_RECORDS) {
            records.remove(records.size() - 1);
        }
    }

    public static void clear() {
        records.clear();
    }

    public static void deleteStorageFile() {
        try {
            Files.deleteIfExists(STORAGE_FILE);
        } catch (IOException ignored) {
        }
    }
}