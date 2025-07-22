package it.unibo.viewTest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import it.unibo.virtualCasino.controller.singleton.PlayerHolder;
import it.unibo.virtualCasino.model.Player;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
class GamesMenuViewTest {

    private Stage stage;

    @BeforeEach
    void resetScene() throws Exception {
        FxToolkit.setupFixture(() -> {
            try {
                Parent root = FXMLLoader.load(
                        ClassLoader.getSystemResource("layouts/gamesMenu.fxml"));
                stage.getScene().setRoot(root);
            } catch (IOException ex) {
                throw new RuntimeException("Unable to load gamesMenu.fxml", ex);
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    /** Inserts a dummy player into the singleton. */
    @BeforeAll
    static void preparePlayer() {
        PlayerHolder.getInstance().setPlayerHolded(new Player("TestPlayer"));
    }

    @Start
    private void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(
                ClassLoader.getSystemResource("layouts/gamesMenu.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
        this.stage = stage;
    }

    /** Games menu shows player name and balance. */
    @Test
    @DisplayName("Games menu displays name and balance")
    void playerAndBalanceVisible(FxRobot robot) {
        assertTrue(robot.lookup("#txtPlayer").tryQuery().isPresent());
        assertTrue(robot.lookup("#txtBalance").tryQuery().isPresent());
    }

    /** Clicking Blackjack loads the Blackjack view. */
    @Test
    @DisplayName("Blackjack button loads Blackjack view")
    void playBlackjackLoadsView(FxRobot robot) throws TimeoutException {
        robot.clickOn("#btnBlackjack");
        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> robot.lookup("#btnCard0").tryQuery().isPresent());
        assertTrue(robot.lookup("#btnCard0").tryQuery().isPresent());
    }

    /** Clicking Roulette loads the Roulette view. */
    @Test
    @DisplayName("Roulette button loads Roulette view")
    void playRouletteLoadsView(FxRobot robot) throws TimeoutException {
        robot.clickOn("#btnRoulette");
        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> robot.lookup("#btnSpeenWheel").tryQuery().isPresent());
        assertTrue(robot.lookup("#btnSpeenWheel").tryQuery().isPresent());
    }

    /** Player name and balance persist after switching to Blackjack view. */
    @Test
    @DisplayName("Player data persists across views")
    void playerDataPersistsAcrossViews(FxRobot robot) throws TimeoutException {
        String playerBefore = robot.lookup("#txtPlayer").queryAs(Text.class).getText();
        String balanceBefore = robot.lookup("#txtBalance").queryAs(Text.class).getText();

        robot.clickOn("#btnBlackjack");
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS,
                () -> robot.lookup("#btnCard0").tryQuery().isPresent());

        String playerAfter = robot.lookup("#txtPlayer").queryAs(Text.class).getText();
        String balanceAfter = robot.lookup("#txtBalance").queryAs(Text.class).getText();

        assertEquals(playerBefore, playerAfter);
        assertEquals(balanceBefore, balanceAfter);
    }

    /** Exit -> Cancel keeps the Games menu. */
    @Test
    @DisplayName("Exit -> Cancel keeps Games menu")
    void exitCancelKeepsGamesMenu(FxRobot robot) {
        robot.clickOn("#btnExit");
        robot.clickOn("Cancel");
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(robot.lookup("#btnBlackjack").tryQuery().isPresent());
    }

    /** Exit -> Yes loads the Dice view. */
    @Test
    @DisplayName("Exit -> Yes loads Dice view")
    void exitYesLoadsDice(FxRobot robot) {
        robot.clickOn("#btnExit");
        assertTrue(robot.lookup(".dialog-pane").tryQuery().isPresent());
        robot.clickOn("Roll the dice!");
        assertTrue(robot.lookup("#btnRoll").tryQuery().isPresent());
    }

    /** Exit -> No returns to the Main menu. */
    @Test
    @DisplayName("Exit -> No returns to Main menu")
    void exitNoReturnsToMainMenu(FxRobot robot) {
        robot.clickOn("#btnExit");
        robot.clickOn("Exit now");
        assertTrue(robot.lookup("#btnPlay").tryQuery().isPresent());
    }
}
