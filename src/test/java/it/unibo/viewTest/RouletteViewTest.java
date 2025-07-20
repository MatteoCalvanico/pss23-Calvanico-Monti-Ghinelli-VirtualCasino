package it.unibo.viewTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import it.unibo.virtualCasino.controller.singleton.PlayerHolder;
import it.unibo.virtualCasino.model.Player;
import it.unibo.virtualCasino.model.games.impl.roulette.enums.RouletteBetType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import javafx.scene.Node;

import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
class RouletteViewTest {

    private Stage stage;

    /** prepara nel singleton un player “ricco” così da poter puntare. */
    @BeforeAll
    static void initPlayer() {
        PlayerHolder.getInstance().setPlayerHolded(new Player("Tester")); // balance 1000
    }

    @Start
    private void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(
                ClassLoader.getSystemResource("layouts/roulette.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
        this.stage = stage;
    }

    private void selectFirstBetTypeProgrammatically(FxRobot robot) {
        ComboBox<RouletteBetType> combo = robot.lookup("#cmbBetType").queryComboBox();

        if (combo.getValue() == null) {
            javafx.application.Platform.runLater(() -> combo.setValue(combo.getItems().get(0))); // es. STRAIGHT
        }
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    @DisplayName("Smoke-test: root FXML non nullo")
    void rootLoaded() {
        assertNotNull(stage.getScene().getRoot());
    }

    @Nested
    @DisplayName("Bet form / lista")
    class BetForm {

        /** clicca sul primo indicatore-cerchio generato dal controller */
        private void pickFirstBetCircle(FxRobot robot) throws TimeoutException {
            /* seleziona un tipo di scommessa con certezza */
            selectFirstBetTypeProgrammatically(robot);

            /* attendi max 5 s che compaiano cerchi visibili dentro il pane */
            WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS,
                    () -> !robot.lookup(n -> n instanceof Circle
                            && n.isVisible()
                            && "#betPositionsIndicatorsPane"
                                    .equals(((Node) n).getParent().getId()))
                            .queryAll().isEmpty());

            /* clicca il primo cerchio trovato (non serve un secondo clickOn) */
            Node firstCircle = robot.lookup(n -> n instanceof Circle
                    && n.isVisible()
                    && "#betPositionsIndicatorsPane"
                            .equals(((Node) n).getParent().getId()))
                    .query();
            robot.clickOn(firstCircle);
        }

        @Test
        @DisplayName("Una nuova puntata compare nella ListView")
        void placeBetAppearsInList(FxRobot robot) throws TimeoutException {

            robot.clickOn("#txtBetAmount").eraseText(1).write("10");
            selectFirstBetTypeProgrammatically(robot);

            pickFirstBetCircle(robot); // clic sul cerchio
            robot.clickOn("#btnPlaceBet");

            ListView<?> list = robot.lookup("#betList").queryListView();
            assertEquals(1, list.getItems().size());
        }

        @Test
        @DisplayName("Il pulsante X elimina la puntata dalla ListView")
        void deleteBetRemoves(FxRobot robot) throws TimeoutException {

            /* pre-condizione: aggiungo prima una puntata */
            robot.clickOn("#txtBetAmount").eraseText(1).write("5");

            pickFirstBetCircle(robot);
            robot.clickOn("#btnPlaceBet");

            ListView<?> list = robot.lookup("#betList").queryListView();
            assertEquals(1, list.getItems().size(), "Pre-condizione fallita");

            /* clic sul bottone X nella cella */
            robot.clickOn(list.lookup(".button"));
            assertEquals(0, list.getItems().size());
        }
    }

    @Test
    @DisplayName("Spin! mostra un numero vincente")
    void spinWheelShowsNumber(FxRobot robot) throws TimeoutException {

        /* 1. piazzo velocemente una puntata qualsiasi (come sopra) */
        robot.clickOn("#txtBetAmount").eraseText(1).write("15");
        selectFirstBetTypeProgrammatically(robot);

        WaitForAsyncUtils.waitForFxEvents(); // fa apparire i cerchi
        robot.clickOn((Node) robot.lookup("#betPositionsIndicatorsPane .circle").query());
        robot.clickOn("#btnPlaceBet");

        /* 2. salvo il testo prima dello spin */
        String before = robot.lookup("#txtWinningNumber").queryText().getText();

        /* 3. clic su Spin! */
        robot.clickOn("#btnSpeenWheel");

        /* 4. attendiamo che il numero cambi */
        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> !robot.lookup("#txtWinningNumber").queryText().getText().equals(before));

        String after = robot.lookup("#txtWinningNumber").queryText().getText();
        assertNotEquals(before, after, "Dopo lo spin il testo del numero vincente deve cambiare");
    }

    @Test
    @DisplayName("Spin con puntate > saldo mostra un Alert")
    void spinWithInsufficientBalance(FxRobot robot) throws TimeoutException {

        /* set puntata enorme */
        robot.clickOn("#txtBetAmount").eraseText(1).write("9999");
        selectFirstBetTypeProgrammatically(robot);

        WaitForAsyncUtils.waitFor(1, TimeUnit.SECONDS,
                () -> !robot.lookup((Circle c) -> c.isVisible()).queryAll().isEmpty());
        Node betCircle = robot.lookup((Circle c) -> c.isVisible()).query(); // tipizzato!
        robot.clickOn(betCircle);

        robot.clickOn("#btnPlaceBet");

        /* deve comparire un dialogo di WARNING */
        assertTrue(robot.lookup(".dialog-pane").tryQuery().isPresent());
        closeAnyAlert(robot); // lo chiudiamo per non “sporcare” altri test
    }

    @Test
    @DisplayName("Exit → «Exit now» riporta al Games-menu")
    void exitReturnsToGamesMenu(FxRobot robot) {

        robot.clickOn("#btnExit"); // apre l’alert

        // individua il bottone con testo che inizia per “Exit”
        Button exitNow = robot.lookup(b -> b instanceof Button
                && ((Button) b).getText().startsWith("Exit"))
                .queryAs(Button.class);
        robot.clickOn(exitNow);

        // se il Games-menu è attivo, esiste #btnRoulette
        assertTrue(robot.lookup("#btnRoulette").tryQuery().isPresent());
    }

    /** chiude (se presente) il primo Alert premendo il suo bottone default. */
    private void closeAnyAlert(FxRobot robot) {
        robot.lookup(".dialog-pane").tryQuery().ifPresent(node -> {
            DialogPane pane = (DialogPane) node;
            Button btn = (Button) pane.lookupButton(pane.getButtonTypes().get(0));
            robot.clickOn(btn);
            WaitForAsyncUtils.waitForFxEvents();
        });
    }
}
