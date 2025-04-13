package it.unibo.virtualCasino.helpers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * The {@code RoutingHelper} class provides navigation support within the
 * application.
 * It includes methods to transition between views by utilizing JavaFX
 * {@code Stage} and {@code Application}.
 */
public final class RoutingHelper {

    /** Private constructor to prevent instantiation of this utility class. */
    private RoutingHelper() {
    }

    /**
     * Transitions to a specified view by replacing the current stage's scene.
     *
     * @param event   the {@code ActionEvent} triggering the navigation.
     * @param appView the {@code Application} view to display.
     */
    public static void goTo(ActionEvent event, Application appView) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appView.start(stage);
        } catch (Exception e) {
            AlertHelper.show(AlertType.ERROR, "Error", e.getMessage());
        }
    }
}
