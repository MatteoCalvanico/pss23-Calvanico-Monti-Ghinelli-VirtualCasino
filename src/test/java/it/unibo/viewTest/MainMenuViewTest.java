package it.unibo.viewTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
class MainMenuViewTest {

        private Stage stage; // kept for checking the current scene

        @Start
        private void start(Stage stage) throws Exception {
                Parent root = javafx.fxml.FXMLLoader.load(
                                ClassLoader.getSystemResource("layouts/mainMenu.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
                this.stage = stage;
        }

        /** Smoke test: mainMenu.fxml loads. */
        @Test
        @DisplayName("Smoke-test: mainMenu.fxml loads")
        void rootNotNull() {
                assertNotNull(stage.getScene().getRoot());
        }

        /** Clicking Play → enter name → navigates to Games menu. */
        @Test
        @DisplayName("Play → enter name → shows Games menu")
        void playGoesToGamesMenu(FxRobot robot) throws TimeoutException {

                robot.clickOn("#btnPlay"); // "Enter in the Casinò"

                String uniqueName = "Test-" + Instant.now().toEpochMilli();
                robot.clickOn(".text-field")
                                .write(uniqueName)
                                .clickOn((Button b) -> "OK".equals(b.getText()));

                WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                                () -> robot.lookup("#txtPlayer").tryQuery().isPresent());

                assertTrue(robot.lookup("#txtPlayer").tryQuery().isPresent());

                String labelText = robot.lookup("#txtPlayer").queryLabeled().getText();
                assertTrue(labelText.contains(uniqueName));
        }

        /** Clicking Scoreboard loads the scoreboard view. */
        @Test
        @DisplayName("Scoreboard button loads scoreboard view")
        void scoreboardButtonLoadsView(FxRobot robot) {
                robot.clickOn("#btnScoreboard");
                WaitForAsyncUtils.waitForFxEvents();
                assertTrue(robot.lookup("#scoreboardList").tryQuery().isPresent());
        }
}
