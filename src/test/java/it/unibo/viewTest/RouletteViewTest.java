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
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
class RouletteViewTest {

    private Stage stage;

    /** Inietta un giocatore nel singleton per inizializzare il controller */
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

    @Test
    @DisplayName("Smoke-test: root non nullo")
    void rootLoadOk() {
        assertNotNull(stage.getScene().getRoot());
    }

    @Test
    @DisplayName("Player & balance visibili")
    void playerAndBalanceVisible(FxRobot robot) {
        assertTrue(robot.lookup("#txtPlayer").tryQuery().isPresent());
        assertTrue(robot.lookup("#txtBalance").tryQuery().isPresent());
    }

    @Test
    @DisplayName("Creazione puntata: tipo > posizione > New Bet aggiunge alla lista")
    void newBetAppearsInList(FxRobot robot) throws TimeoutException {

        /* seleziona un tipo di puntata dal ComboBox */
        @SuppressWarnings("unchecked")
        ComboBox<RouletteBetType> cmb = robot.lookup("#cmbBetType").queryAs(ComboBox.class);
        robot.interact(() -> cmb.getSelectionModel().select(RouletteBetType.STRAIGHT_UP));

        /* scrive l’importo */
        robot.clickOn("#txtBetAmount").write("50");

        /* il controller, dopo la scelta tipo, genera dei cerchietti cliccabili */
        Pane indicators = robot.lookup("#betPositionsIndicatorsPane").queryAs(Pane.class);
        /* aspetta che compaiano (max 3 s) */
        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> !indicators.getChildren().isEmpty());

        /* click sul primo indicatore disponibile */
        robot.clickOn(indicators.getChildren().get(0), MouseButton.PRIMARY);

        /* New Bet */
        robot.clickOn("#btnPlaceBet");

        /* la ListView dev’essere popolata di 1 elemento */
        ListView<?> list = robot.lookup("#betList").queryAs(ListView.class);
        assertEquals(1, list.getItems().size(),
                "La puntata deve comparire nella ListView");
    }

    @Test
    @DisplayName("Clic sul bottone X rimuove la puntata dalla lista")
    void deleteBetButtonWorks(FxRobot robot) throws TimeoutException {

        /* pre-condizione: creiamo una piccola puntata */
        newBetAppearsInList(robot); // riusa il test precedente

        ListView<?> list = robot.lookup("#betList").queryAs(ListView.class);
        assertEquals(1, list.getItems().size());

        /* il pulsante X è il graphic della prima cella */
        Button btnDelete = (Button) list.lookup(".button");
        robot.clickOn(btnDelete);

        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(0, list.getItems().size(),
                "Dopo il click su X la puntata deve sparire");
    }

    @Test
    @DisplayName("Spin aggiorna il numero vincente (txtWinningNumber)")
    void spinWheelUpdatesWinningNumber(FxRobot robot) throws TimeoutException {

        String before = robot.lookup("#txtWinningNumber").queryText().getText();
        robot.clickOn("#btnSpeenWheel");

        /* attende che il controller imposti il testo */
        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS, () -> {
            String now = robot.lookup("#txtWinningNumber").queryText().getText();
            return !now.equals(before); // deve cambiare
        });

        String after = robot.lookup("#txtWinningNumber").queryText().getText();
        assertNotEquals(before, after,
                "Dopo lo Spin il numero vincente deve cambiare");
    }

    @Test
    @DisplayName("Exit rimanda correttamente alla Games-menu")
    void exitReturnsToGamesMenu(FxRobot robot) throws TimeoutException {

        robot.clickOn("#btnExit");

        /* aspetta 5s che la nuova scena abbia il bottone #btnBlackjack */
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS,
                () -> robot.lookup("#btnBlackjack").tryQuery().isPresent());

        assertTrue(robot.lookup("#btnBlackjack").tryQuery().isPresent(),
                "Dopo Exit deve tornare la Games-menu");
    }

    @Test
    @DisplayName("Flusso completo RED_BLACK: 50$ > New Bet > Spin")
    void redBlackBetFlow(FxRobot robot) throws TimeoutException {

        /* 1. importo */
        robot.clickOn("#txtBetAmount").eraseText(10).write("50");

        /* 2. select RED_BLACK nel combo */
        @SuppressWarnings("unchecked")
        ComboBox<RouletteBetType> cmb = robot.lookup("#cmbBetType").queryAs(ComboBox.class);
        robot.interact(() -> cmb.getSelectionModel().select(RouletteBetType.RED_BLACK));

        /* 3. attendi la comparsa indicatori e cliccane uno */
        Pane indicators = robot.lookup("#betPositionsIndicatorsPane").queryAs(Pane.class);
        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS, () -> !indicators.getChildren().isEmpty());
        robot.clickOn(indicators.getChildren().get(0), MouseButton.PRIMARY);

        /* 4. New Bet */
        robot.clickOn("#btnPlaceBet");

        /* conferma che la lista contenga almeno un elemento */
        ListView<?> list = robot.lookup("#betList").queryAs(ListView.class);
        assertTrue(list.getItems().size() >= 1);

        /* 5. Spin! */
        String before = robot.lookup("#txtWinningNumber").queryText().getText();
        robot.clickOn("#btnSpeenWheel");
        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> !robot.lookup("#txtWinningNumber").queryText().getText().equals(before));
    }

}
