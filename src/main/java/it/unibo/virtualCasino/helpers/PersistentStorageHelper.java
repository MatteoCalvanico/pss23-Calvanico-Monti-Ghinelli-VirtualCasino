package it.unibo.virtualCasino.helpers;

import java.io.File;
import java.io.IOException;

/**
 * The {@code PersistentStorageHelper} class provides utility methods for
 * managing
 * persistent storage files and directories specific to the application. It
 * ensures
 * that a dedicated storage directory is available in the user's home directory
 * and
 * that requested files are created if they do not already exist.
 */
public class PersistentStorageHelper {
    /**
     * The directory path for the application's storage, located in the user's home
     * directory.
     */
    private static final String APP_STORAGE_DIR = System
            .getProperty("user.home") + File.separator + ".virtualCasino";

    /**
     * Retrieves the application's storage directory path. Creates the directory if
     * it does not exist.
     *
     * @return the path to the application's storage directory as a {@code String}.
     */
    public static String getStorageDirectory() {
        File directory = new File(APP_STORAGE_DIR);
        if (!directory.exists()) {
            directory.mkdirs(); // Create directory if it doesn’t exist
        }
        return APP_STORAGE_DIR;
    }

    /**
     * Retrieves the specified storage file within the application's storage
     * directory.
     * Creates a new file if it does not already exist.
     *
     * @param filename the name of the file to retrieve or create.
     * @return the requested {@code File} object located in the storage directory.
     * @throws IOException if an I/O error occurs while creating the file.
     */
    public static File getStorageFile(String filename) throws IOException {
        File file = new File(getStorageDirectory(), filename);
        if (!file.exists()) {
            file.createNewFile(); // Create file if it doesn’t exist
        }
        return file;
    }
}
