package it.unibo.virtualCasino.view.roulette;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public final class RouletteView extends Application {
    private final int TOTAL_BET_POSITIONS = 113;
    private final int H_LINES_POSITIONS = 48;
    private final int V_LINES_POSITIONS = 33;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("layouts/roulette.fxml"));
        Scene scene = new Scene(root);

        setUpBetPositionIndicatorsOnTable(root);

        stage.setTitle("Remember...the house always win.");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setUpBetPositionIndicatorsOnTable(Parent root) {
      Pane roulettePane = (Pane) root.lookup("#roulettePane");

      for (int i = 0; i < TOTAL_BET_POSITIONS; i++) {
        double centerX = 100;
        double centerY = 100;
        double radius = 2.0;
        Circle circle = createCircle(centerX, centerY, radius);
        roulettePane.getChildren().add(circle);
      }
      
    }

    private Circle createCircle(double centerX, double centerY, double radius) {
      Circle circle = new Circle(radius);
      circle.setCenterX(centerX);
      circle.setCenterY(centerY);
      circle.setFill(Color.WHITE); // Adjust fill color if needed
      circle.setStroke(Color.WHITE); // Adjust stroke color if needed
      circle.setStrokeWidth(2.0); // Adjust stroke width if needed
      return circle;
    }
}

//Define a function for each bet type