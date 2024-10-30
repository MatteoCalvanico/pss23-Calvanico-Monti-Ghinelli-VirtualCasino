package it.unibo.virtualCasino.model.scoreboard.helpers;

import java.io.File;
import java.io.IOException;

public class PersistentStorageHelper {
    private static final String APP_STORAGE_DIR = System.getProperty("user.home") + File.separator + ".virtualCasino";

    public static String getStorageDirectory() {
        File directory = new File(APP_STORAGE_DIR);
        if (!directory.exists()) {
            directory.mkdirs(); // Create directory if it doesn’t exist
        }
        return APP_STORAGE_DIR;
    }

    public static File getStorageFile(String filename) throws IOException {
        File file = new File(getStorageDirectory(), filename);
        if (!file.exists()) {
            file.createNewFile(); // Create file if it doesn’t exist
        }
        return file;
    }
}
