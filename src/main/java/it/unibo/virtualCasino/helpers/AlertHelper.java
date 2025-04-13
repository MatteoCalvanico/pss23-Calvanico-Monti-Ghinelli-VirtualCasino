package it.unibo.virtualCasino.helpers;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * The {@code AlertHelper} class provides a utility method for displaying alert
 * dialogs
 * in the application. This helper simplifies the creation and configuration of
 * JavaFX
 * {@link Alert} dialogs, setting a default header and making it easy to show
 * messages
 * of various types, such as error or information alerts.
 */
public final class AlertHelper {

    /** Private constructor to prevent instantiation of this utility class. */
    private AlertHelper() {
    }

    /**
     * Displays an alert dialog with the specified type, title, and content.
     * <p>
     * This method creates an {@link Alert} of the given {@code AlertType}, sets its
     * title and content,
     * and displays it to the user. The header text is set to {@code null}.
     * The alert waits for the user to close it before proceeding.
     *
     * @param type    the type of alert (e.g., {@link AlertType#INFORMATION},
     *                {@link AlertType#ERROR})
     * @param title   the title of the alert window
     * @param content the message to be displayed in the alert
     */
    public static void show(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
