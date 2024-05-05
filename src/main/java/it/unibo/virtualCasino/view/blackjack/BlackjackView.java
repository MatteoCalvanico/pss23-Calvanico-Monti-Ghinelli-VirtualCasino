package it.unibo.virtualCasino.view.blackjack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class BlackjackView extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("layouts/blackjack.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Remember....the house always win.");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
