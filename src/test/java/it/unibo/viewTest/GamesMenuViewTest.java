package it.unibo.viewTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import it.unibo.virtualCasino.controller.singleton.PlayerHolder;
import it.unibo.virtualCasino.model.Player;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
class GamesMenuViewTest {

    private Stage stage;

    /*
     * Mettiamo un player nel singleton per non far crashare il
     * controller
     */
    @BeforeAll
    static void preparePlayer() {
        PlayerHolder.getInstance().setPlayerHolded(new Player("TestPlayer"));
    }

    @Start
    private void start(Stage stage) throws Exception {
        Parent root = javafx.fxml.FXMLLoader.load(
                ClassLoader.getSystemResource("layouts/gamesMenu.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
        this.stage = stage;
    }

    @Test
    @DisplayName("La Games-menu mostra nome e saldo")
    void playerAndBalanceVisible(FxRobot robot) {
        assertTrue(robot.lookup("#txtPlayer").tryQuery().isPresent());
        assertTrue(robot.lookup("#txtBalance").tryQuery().isPresent());
    }

    @Test
    @DisplayName("Pulsante Blackjack carica la blackjack-view")
    void playBlackjackLoadsView(FxRobot robot) throws TimeoutException {

        robot.clickOn("#btnBlackjack");

        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> robot.lookup("#btnCard0").tryQuery().isPresent());

        assertTrue(robot.lookup("#btnCard0").tryQuery().isPresent(),
                "Dopo il click deve comparire la schermata Blackjack");
    }

    @Test
    @DisplayName("Pulsante Roulette carica la roulette-view")
    void playRouletteLoadsView(FxRobot robot) throws TimeoutException {

        robot.clickOn("#btnRoulette");

        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> robot.lookup("#btnSpeenWheel").tryQuery().isPresent());

        assertTrue(robot.lookup("#btnSpeenWheel").tryQuery().isPresent(),
                "Dopo il click deve comparire la schermata Roulette");
    }

    @Test
    @DisplayName("Exit → popup – Cancel → resta in Games-menu")
    void exitCancelKeepsGamesMenu(FxRobot robot) {

        robot.clickOn("#btnExit");
        robot.clickOn("Cancel"); // terza scelta del dialogo

        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(robot.lookup("#btnBlackjack").tryQuery().isPresent(),
                "Con Cancel si deve restare nella Games-menu");
    }

    @Test
    @DisplayName("Exit → popup – Yes → scena dadi")
    void exitYesLoadsDice(FxRobot robot) {

        robot.clickOn("#btnExit");

        /* il dialogo deve comparire */
        assertTrue(robot.lookup(".dialog-pane").tryQuery().isPresent());

        /* clicchiamo sul bottone “Roll the dice!” */
        robot.clickOn("Roll the dice!");

        /* ora c’è la dice-view (ha il bottone #btnRoll) */
        assertTrue(robot.lookup("#btnRoll").tryQuery().isPresent(),
                "Dopo Yes deve comparire la scena del gioco dei dadi");
    }

    @Test
    @DisplayName("Exit → popup – No → torna al Main-menu")
    void exitNoReturnsToMainMenu(FxRobot robot) {

        robot.clickOn("#btnExit");
        robot.clickOn("Exit now"); // scegliamo l’altra opzione

        /* bottone #btnPlay è del mainMenu.fxml */
        assertTrue(robot.lookup("#btnPlay").tryQuery().isPresent(),
                "Dopo Exit now si torna al main-menu");
    }
}
