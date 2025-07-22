package it.unibo.viewTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import it.unibo.virtualCasino.controller.singleton.PlayerHolder;
import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.impl.roulette.enums.RouletteBetType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
class RouletteViewTest {

    private Stage stage;

    /** Inserts a dummy player so the controller initializes. */
    @BeforeAll
    static void preparePlayer() {
        PlayerHolder.getInstance().setPlayerHolded(new Player("Robot"));
    }

    @Start
    private void start(Stage stage) throws Exception {
        Parent root = javafx.fxml.FXMLLoader.load(
                ClassLoader.getSystemResource("layouts/roulette.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
        this.stage = stage;
    }

    /** Smoke test: root is not null. */
    @Test
    @DisplayName("Smoke-test: root not null")
    void rootLoadOk() {
        assertNotNull(stage.getScene().getRoot());
    }

    /** Player name and balance labels are visible. */
    @Test
    @DisplayName("Player & balance visible")
    void playerAndBalanceVisible(FxRobot robot) {
        assertTrue(robot.lookup("#txtPlayer").tryQuery().isPresent());
        assertTrue(robot.lookup("#txtBalance").tryQuery().isPresent());
    }

    /** Creating a bet adds it to the ListView. */
    @Test
    @DisplayName("Create bet -> appears in list")
    void newBetAppearsInList(FxRobot robot) throws TimeoutException {
        @SuppressWarnings("unchecked")
        ComboBox<RouletteBetType> cmb = robot.lookup("#cmbBetType").queryAs(ComboBox.class);
        robot.interact(() -> cmb.getSelectionModel().select(RouletteBetType.STRAIGHT_UP));

        robot.clickOn("#txtBetAmount").write("50");

        Pane indicators = robot.lookup("#betPositionsIndicatorsPane").queryAs(Pane.class);
        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS, () -> !indicators.getChildren().isEmpty());

        robot.clickOn(indicators.getChildren().get(0), MouseButton.PRIMARY);
        robot.clickOn("#btnPlaceBet");

        ListView<?> list = robot.lookup("#betList").queryAs(ListView.class);
        assertEquals(1, list.getItems().size());
    }

    /** Clicking the X button removes the bet from the list. */
    @Test
    @DisplayName("Delete bet button removes bet")
    void deleteBetButtonWorks(FxRobot robot) throws TimeoutException {
        newBetAppearsInList(robot);

        ListView<?> list = robot.lookup("#betList").queryAs(ListView.class);
        Button btnDelete = (Button) list.lookup(".button");
        robot.clickOn(btnDelete);

        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(0, list.getItems().size());
    }

    /** Spin updates the winning-number label. */
    @Test
    @DisplayName("Spin updates winning number")
    void spinWheelUpdatesWinningNumber(FxRobot robot) throws TimeoutException {
        String before = robot.lookup("#txtWinningNumber").queryText().getText();
        robot.clickOn("#btnSpeenWheel");

        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> !robot.lookup("#txtWinningNumber").queryText().getText().equals(before));

        String after = robot.lookup("#txtWinningNumber").queryText().getText();
        assertNotEquals(before, after);
    }

    /** Exit returns to the Games menu. */
    @Test
    @DisplayName("Exit returns to Games menu")
    void exitReturnsToGamesMenu(FxRobot robot) throws TimeoutException {
        robot.clickOn("#btnExit");
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS,
                () -> robot.lookup("#btnBlackjack").tryQuery().isPresent());
        assertTrue(robot.lookup("#btnBlackjack").tryQuery().isPresent());
    }

    /** Full RED_BLACK flow: 50 $, New Bet, Spin. */
    @Test
    @DisplayName("Full RED_BLACK flow: 50 $ -> New Bet -> Spin")
    void redBlackBetFlow(FxRobot robot) throws TimeoutException {
        robot.clickOn("#txtBetAmount").eraseText(10).write("50");

        @SuppressWarnings("unchecked")
        ComboBox<RouletteBetType> cmb = robot.lookup("#cmbBetType").queryAs(ComboBox.class);
        robot.interact(() -> cmb.getSelectionModel().select(RouletteBetType.RED_BLACK));

        Pane indicators = robot.lookup("#betPositionsIndicatorsPane").queryAs(Pane.class);
        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS, () -> !indicators.getChildren().isEmpty());
        robot.clickOn(indicators.getChildren().get(0), MouseButton.PRIMARY);

        robot.clickOn("#btnPlaceBet");

        ListView<?> list = robot.lookup("#betList").queryAs(ListView.class);
        assertTrue(list.getItems().size() >= 1);

        String before = robot.lookup("#txtWinningNumber").queryText().getText();
        robot.clickOn("#btnSpeenWheel");
        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> !robot.lookup("#txtWinningNumber").queryText().getText().equals(before));
    }
}
