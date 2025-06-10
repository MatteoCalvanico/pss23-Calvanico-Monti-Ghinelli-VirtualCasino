package it.unibo.viewTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tenta a caricare ogni file FXML
 * e verifica che il root non sia nullo
 */
class FxmlSmokeTest {

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {
            /* no-op */ });
    }

    @ParameterizedTest(name = "Load {0}")
    @MethodSource("fxmlPathsProvider")
    void fxmlLoadsWithoutException(String resourcePath) throws Exception {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource(resourcePath));

        assertNotNull(root,
                "Il file " + resourcePath + " deve caricarsi senza eccezioni");
    }

    private static Stream<Named<String>> fxmlPathsProvider() {
        return Stream.of(
                Named.of("blackjack.fxml", "layouts/blackjack.fxml"),
                Named.of("roulette.fxml", "layouts/roulette.fxml"),
                Named.of("mainMenu.fxml", "layouts/mainMenu.fxml"),
                Named.of("gamesMenu.fxml", "layouts/gamesMenu.fxml"),
                Named.of("scoreboard.fxml", "layouts/scoreboard.fxml"));
    }
}
