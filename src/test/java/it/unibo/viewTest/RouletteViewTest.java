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
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
class RouletteViewTest {

    private Stage stage;

    /** Inietta un giocatore nel singleton per inizializzare il controller. */
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
}
