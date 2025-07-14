package it.unibo.virtualCasino.view.dice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class DiceView extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(
                ClassLoader.getSystemResource("layouts/dice.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Try your luck!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
