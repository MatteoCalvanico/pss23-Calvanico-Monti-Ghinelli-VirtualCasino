package it.unibo.viewTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import it.unibo.virtualCasino.controller.singleton.PlayerHolder;
import it.unibo.virtualCasino.model.Player;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
class BlackjackViewTest {

        private Stage stage;

        /**
         * Inserts a "rich" player into the singleton so the controller does not throw.
         */
        @BeforeAll
        static void preparePlayer() {
                Player p = new Player("TestPlayer");
                p.addWin(10_000); // plenty of balance for bets
                PlayerHolder.getInstance().setPlayerHolded(p);
        }

        @Start
        private void start(Stage stage) throws Exception {
                Parent root = javafx.fxml.FXMLLoader.load(
                                ClassLoader.getSystemResource("layouts/blackjack.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
                this.stage = stage;
        }

        @AfterEach
        void tearDown() throws Exception {
                TestUtils.cleanAfterFxTest();
        }

        /** Smoke test: Blackjack scene loads. */
        @Test
        @DisplayName("Smoke-test: Blackjack scene loads")
        void rootNotNull() {
                assertNotNull(stage.getScene().getRoot());
        }

        /** Initial state: action buttons disabled, bet = 0. */
        @Test
        @DisplayName("Initial state – buttons disabled, bet = 0")
        void initialState(FxRobot robot) {
                assertTrue(robot.lookup("#btnCard0").queryButton().isDisabled());
                assertTrue(robot.lookup("#btnStay0").queryButton().isDisabled());
                assertTrue(robot.lookup("#btnSplit").queryButton().isDisabled());
                assertEquals("0", robot.lookup("#txtBet0").queryText().getText());
        }

        /** +100 / −100 buttons update the bet amount. */
        @Test
        @DisplayName("+100 / −100 buttons update bet")
        void addAndRemoveBet(FxRobot robot) {
                robot.clickOn("#btnBet100").clickOn("#btnBet100");
                assertEquals("200", robot.lookup("#txtBet0").queryText().getText());

                robot.clickOn("#btnBetMinus100");
                assertEquals("100", robot.lookup("#txtBet0").queryText().getText());
        }

        /** Exit returns to the Games menu. */
        @Test
        @DisplayName("Exit returns to Games-menu")
        void exitReturnsToGamesMenu(FxRobot robot) throws TimeoutException {
                robot.clickOn("#btnExit");
                WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                                () -> robot.lookup("#btnBlackjack").tryQuery().isPresent());
                assertTrue(robot.lookup("#btnBlackjack").tryQuery().isPresent());
        }

        /** Placing a bet starts the round and enables game controls. */
        @Test
        @DisplayName("Set bet enables game controls")
        void setBetEnablesGameControls(FxRobot robot) throws TimeoutException {
                robot.clickOn("#btnBet100").clickOn("#btnPlaceBet");
                WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                                () -> !robot.lookup("#btnCard0").queryButton().isDisabled());

                assertFalse(robot.lookup("#btnCard0").queryButton().isDisabled());
                assertFalse(robot.lookup("#btnStay0").queryButton().isDisabled());
                assertFalse(robot.lookup("#btnSplit").queryButton().isDisabled());

                assertTrue(robot.lookup("#btnBet100").queryButton().isDisabled());
                assertTrue(robot.lookup("#btnBetMinus100").queryButton().isDisabled());
        }

        /** Balance remains unchanged if the player exits before playing a hand. */
        @Test
        @DisplayName("Balance persists if exiting before playing")
        void balancePersistsAfterRound(FxRobot robot) throws TimeoutException {
                String balanceBefore = robot.lookup("#txtBalance").queryText().getText();

                robot.clickOn("#btnBet100"); // set 100 but do not start hand
                robot.clickOn("#btnExit");

                WaitForAsyncUtils.waitForFxEvents();
                WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS,
                                () -> robot.lookup("#btnBlackjack").tryQuery().isPresent());

                String balanceAfter = robot.lookup("#txtBalance").queryText().getText();
                assertEquals(balanceBefore, balanceAfter);
        }

        /** "+" button (CALL) adds one card to player deck 0. */
        @Test
        @DisplayName("+ button adds one card to player deck 0")
        void callButtonAddsOneCard(FxRobot robot) throws TimeoutException {
                robot.clickOn("#btnBet100").clickOn("#btnPlaceBet");
                WaitForAsyncUtils.waitForFxEvents();

                WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS,
                                () -> !robot.lookup("#btnCard0").queryButton().isDisabled());

                int before = Integer.parseInt(robot.lookup("#txtDeckCards0").queryText().getText());
                robot.clickOn("#btnCard0");
                WaitForAsyncUtils.waitForFxEvents();
                TestUtils.closeAnyAlert(robot);
                int after = Integer.parseInt(robot.lookup("#txtDeckCards0").queryText().getText());

                assertTrue(after > before);
        }

        /** Stay disables Card 0 / Stay 0 / Split buttons. */
        @Test
        @DisplayName("Stay disables Card 0 / Stay 0 / Split")
        void stayButtonDisablesActions(FxRobot robot) throws TimeoutException {
                robot.clickOn("#btnBet100").clickOn("#btnPlaceBet");
                WaitForAsyncUtils.waitForFxEvents();

                WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS,
                                () -> !robot.lookup("#btnStay0").queryButton().isDisabled());

                robot.clickOn("#btnStay0");
                WaitForAsyncUtils.waitForFxEvents();
                TestUtils.closeAnyAlert(robot);

                assertAll(
                                () -> assertTrue(robot.lookup("#btnCard0").queryButton().isDisabled()),
                                () -> assertTrue(robot.lookup("#btnStay0").queryButton().isDisabled()),
                                () -> assertTrue(robot.lookup("#btnSplit").queryButton().isDisabled()));
        }

        /** Split shows warning alert or enables Card 1 / Stay 1 when split is valid. */
        @Test
        @DisplayName("Split: shows alert or enables Card 1 / Stay 1")
        void splitButtonBehaviour(FxRobot robot) throws TimeoutException {
                robot.clickOn("#btnBet100").clickOn("#btnPlaceBet");
                WaitForAsyncUtils.waitForFxEvents();

                WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS,
                                () -> !robot.lookup("#btnSplit").queryButton().isDisabled());

                robot.clickOn("#btnSplit");
                WaitForAsyncUtils.waitForFxEvents();

                boolean alertShown = robot.lookup(".dialog-pane").tryQuery().isPresent();
                if (alertShown) {
                        TestUtils.closeAnyAlert(robot);
                }

                boolean secondDeckEnabled = !robot.lookup("#btnCard1").queryButton().isDisabled()
                                && !robot.lookup("#btnStay1").queryButton().isDisabled();

                assertTrue(alertShown || secondDeckEnabled);
        }
}
