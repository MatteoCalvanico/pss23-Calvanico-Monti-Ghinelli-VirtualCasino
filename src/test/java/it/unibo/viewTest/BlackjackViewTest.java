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

}
