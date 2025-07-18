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
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
class MainMenuViewTest {

        private Stage stage; // lo teniamo per controllare la scena corrente

        @Start
        private void start(Stage stage) throws Exception {
                Parent root = javafx.fxml.FXMLLoader.load(
                                ClassLoader.getSystemResource("layouts/mainMenu.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
                this.stage = stage;
        }

        @Test
        @DisplayName("Smoke-test: mainMenu.fxml si carica")
        void rootNotNull() {
                assertNotNull(stage.getScene().getRoot());
        }

        @Test
        @DisplayName("Play → inserisci nome → arriva alla Games-menu")
        void playGoesToGamesMenu(FxRobot robot) throws TimeoutException {

                /* 1) click sul bottone “Enter in the Casinò” */
                robot.clickOn("#btnPlay");

                /* 2) compare il TextInputDialog */
                String uniqueName = "Test-" + Instant.now().toEpochMilli();
                robot.clickOn(".text-field") // campo di input del dialogo
                                .write(uniqueName)
                                .clickOn((Button b) -> "OK".equals(b.getText()));

                /* 3. aspetta che compaia la label #txtPlayer (max 3 s) */
                WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                                () -> robot.lookup("#txtPlayer").tryQuery().isPresent());

                assertTrue(robot.lookup("#txtPlayer").tryQuery().isPresent(),
                                "La Games-menu deve essere in scena");

                /* 5) il testo della label deve contenere il nome inserito */
                String txt = ((javafx.scene.text.Text) robot.lookup("#txtPlayer").query())
                                .getText();
                assertTrue(txt.contains(uniqueName));
        }

        @Test
        @DisplayName("Click su Scoreboard apre la scoreboard-view")
        void scoreboardButtonLoadsView(FxRobot robot) {

                robot.clickOn("#btnScoreboard");

                WaitForAsyncUtils.waitForFxEvents(); // attende cambio scena

                /* presence of the ListView defined in scoreboard.fxml */
                assertTrue(robot.lookup("#scoreboardList").tryQuery().isPresent(),
                                "Dopo il click deve comparire la scoreboard");
        }
}
