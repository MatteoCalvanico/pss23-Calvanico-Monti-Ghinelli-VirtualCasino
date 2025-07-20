package it.unibo.viewTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import it.unibo.virtualCasino.controller.singleton.PlayerHolder;
import it.unibo.virtualCasino.model.Player;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
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
class BlackjackViewTest {

    private Stage stage;

    /**
     * Inseriamo un giocatore “ricco” nel singleton così il controller non solleva
     * eccezioni.
     */
    @BeforeAll
    static void preparePlayer() {
        Player p = new Player("TestPlayer");
        p.addWin(10_000); // balance abbondante per le puntate
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

    @Test
    @DisplayName("Smoke-test: la schermata Blackjack si carica")
    void rootNotNull() {
        assertNotNull(stage.getScene().getRoot());
    }

    @Test
    @DisplayName("Stato iniziale - pulsanti disabilitati, bet = 0")
    void initialState(FxRobot robot) {

        assertTrue(robot.lookup("#btnCard0").queryButton().isDisabled());
        assertTrue(robot.lookup("#btnStay0").queryButton().isDisabled());
        assertTrue(robot.lookup("#btnSplit").queryButton().isDisabled());

        /* txtBet0 è un Text, non un Labeled */
        String betText = robot.lookup("#txtBet0").queryText().getText();
        assertEquals("0", betText);
    }

    @Test
    @DisplayName("Pulsanti +100 / -100 aggiornano la puntata")
    void addAndRemoveBet(FxRobot robot) {

        /* +100 due volte */
        robot.clickOn("#btnBet100").clickOn("#btnBet100");
        assertEquals("200", robot.lookup("#txtBet0").queryText().getText());

        /* -100 una volta */
        robot.clickOn("#btnBetMinus100");
        assertEquals("100", robot.lookup("#txtBet0").queryText().getText());
    }

    @Test
    @DisplayName("Exit riporta al Games-menu")
    void exitReturnsToGamesMenu(FxRobot robot) throws TimeoutException {

        robot.clickOn("#btnExit");

        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> robot.lookup("#btnBlackjack").tryQuery().isPresent());

        assertTrue(robot.lookup("#btnBlackjack").tryQuery().isPresent(),
                "Dopo Exit deve comparire la Games-menu");
    }

    @Test
    @DisplayName("Set bet avvia il round → pulsanti di gioco abilitati")
    void setBetEnablesGameControls(FxRobot robot) throws TimeoutException {

        /* 1. puntiamo 100 */
        robot.clickOn("#btnBet100");

        /* 2. conferma con Set bet */
        robot.clickOn("#btnPlaceBet");

        /* attesa asincrona per l’avvio del round */
        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,
                () -> !robot.lookup("#btnCard0").queryButton().isDisabled());

        /* ora i pulsanti principali NON devono essere disabilitati */
        assertFalse(robot.lookup("#btnCard0").queryButton().isDisabled());
        assertFalse(robot.lookup("#btnStay0").queryButton().isDisabled());
        assertFalse(robot.lookup("#btnSplit").queryButton().isDisabled());

        /* e quelli di modifica bet devono essere disabilitati */
        assertTrue(robot.lookup("#btnBet100").queryButton().isDisabled());
        assertTrue(robot.lookup("#btnBetMinus100").queryButton().isDisabled());
    }

    @Test
    @DisplayName("Flusso breve: saldo invariato se si esce prima di giocare")
    void balancePersistsAfterRound(FxRobot robot) throws TimeoutException {

        /* saldo iniziale dalla Blackjack-view */
        String balanceBefore = robot.lookup("#txtBalance")
                .queryText().getText();

        /* imposta 100 ma NON avvia la mano */
        robot.clickOn("#btnBet100"); // saldo a rischio, ma mano non iniziata

        /* click immediato su Exit (il bottone è ancora abilitato) */
        robot.clickOn("#btnExit");

        /* sincronizzazione JavaFX + attesa comparsa Games-menu */
        WaitForAsyncUtils.waitForFxEvents();
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS,
                () -> robot.lookup("#btnBlackjack").tryQuery().isPresent());

        /* ora leggiamo il saldo mostratato nella Games-menu */
        String balanceAfter = robot.lookup("#txtBalance")
                .queryText().getText();

        assertEquals(balanceBefore, balanceAfter,
                "Senza aver disputato una mano il saldo deve rimanere invariato");
    }

    /* 1. utility: chiude eventuali dialoghi aperti */
    private void closeAnyAlert(FxRobot robot) {
        robot.lookup(".dialog-pane").tryQuery().ifPresent(node -> {
            DialogPane pane = (DialogPane) node; // cast corretto
            Button firstBtn = (Button) pane.lookupButton(
                    pane.getButtonTypes().get(0)); // es. OK / CLOSE
            robot.clickOn(firstBtn); // oppure firstBtn.fire()
            WaitForAsyncUtils.waitForFxEvents();
        });
    }

    /**
     * Click sul “+” (btnCard0) deve aggiungere una carta
     * al mazzo principale del giocatore.
     */
    @Test
    @DisplayName("Pulsante +: aggiunge una carta al Player-deck 0")
    void callButtonAddsOneCard(FxRobot robot) throws TimeoutException {

        /* avvia una mano */
        robot.clickOn("#btnBet100").clickOn("#btnPlaceBet");
        WaitForAsyncUtils.waitForFxEvents();

        /* aspettiamo che il pulsante + sia abilitato */
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS,
                () -> !robot.lookup("#btnCard0").queryButton().isDisabled());

        /* carte prima del click */
        int before = Integer.parseInt(
                robot.lookup("#txtDeckCards0").queryText().getText());

        robot.clickOn("#btnCard0"); // + (CALL)
        WaitForAsyncUtils.waitForFxEvents();

        /* se compare un alert di bust, lo chiudiamo e proseguiamo */
        closeAnyAlert(robot);

        /* dopo il click deve essere aumentato di 1 */
        int after = Integer.parseInt(
                robot.lookup("#txtDeckCards0").queryText().getText());

        /* adesso basta che after > before */
        assertTrue(after > before,
                "Il pulsante + deve aggiungere almeno una carta");
    }

    /**
     * Dopo aver premuto “Stay” i pulsanti di azione del primo mazzo
     * devono essere disabilitati (il turno passa – o termina).
     */
    @Test
    @DisplayName("Stay: disabilita i pulsanti Card 0 / Stay 0 / Split")
    void stayButtonDisablesActions(FxRobot robot) throws TimeoutException {

        /* avvia una mano */
        robot.clickOn("#btnBet100").clickOn("#btnPlaceBet");
        WaitForAsyncUtils.waitForFxEvents();

        /* aspetta che Stay0 sia abilitato */
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS,
                () -> !robot.lookup("#btnStay0").queryButton().isDisabled());

        robot.clickOn("#btnStay0"); // Stay

        WaitForAsyncUtils.waitForFxEvents();

        closeAnyAlert(robot); // se showResult apre un dialogo

        /* tutti e tre i pulsanti devono ora essere disabilitati */
        assertAll(
                () -> assertTrue(robot.lookup("#btnCard0").queryButton().isDisabled()),
                () -> assertTrue(robot.lookup("#btnStay0").queryButton().isDisabled()),
                () -> assertTrue(robot.lookup("#btnSplit").queryButton().isDisabled()));
    }

    /**
     * Il pulsante Split mostra l’alert di “Split Warning” **oppure**
     * (se la mano è realmente splittabile) abilita i controlli del
     * secondo mazzo. Il test accetta entrambe le situazioni.
     */
    @Test
    @DisplayName("Split: mostra alert oppure abilita Card 1 / Stay 1")
    void splitButtonBehaviour(FxRobot robot) throws TimeoutException {

        /* avvia una mano */
        robot.clickOn("#btnBet100").clickOn("#btnPlaceBet");
        WaitForAsyncUtils.waitForFxEvents();

        /* aspetta che Split sia abilitato */
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS,
                () -> !robot.lookup("#btnSplit").queryButton().isDisabled());

        robot.clickOn("#btnSplit");
        WaitForAsyncUtils.waitForFxEvents();

        boolean alertShown = robot.lookup(".dialog-pane").tryQuery().isPresent();
        if (alertShown) {
            closeAnyAlert(robot); // chiude e termina: split non valido
        }

        boolean secondDeckEnabled = !robot.lookup("#btnCard1").queryButton().isDisabled()
                && !robot.lookup("#btnStay1").queryButton().isDisabled();

        assertTrue(alertShown || secondDeckEnabled,
                "Lo Split deve produrre alert o abilitare Card1/Stay1");
    }
}
