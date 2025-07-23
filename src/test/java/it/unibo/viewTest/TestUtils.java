package it.unibo.viewTest;

import org.testfx.api.FxToolkit;
import org.testfx.util.WaitForAsyncUtils;
import it.unibo.virtualCasino.model.scoreboard.Scoreboard;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;

import org.testfx.api.FxRobot;

public final class TestUtils {

    private TestUtils() {
    }

    public static void cleanAfterFxTest() throws Exception {
        FxToolkit.cleanupStages();

        Scoreboard.clear();
        Scoreboard.deleteStorageFile();
    }

    public static void closeAnyAlert(FxRobot robot) {
        robot.lookup(".dialog-pane").tryQuery().ifPresent(node -> {
            DialogPane pane = (DialogPane) node;
            Button firstBtn = (Button) pane.lookupButton(pane.getButtonTypes().get(0));
            robot.clickOn(firstBtn);
            WaitForAsyncUtils.waitForFxEvents();
        });
    }
}
