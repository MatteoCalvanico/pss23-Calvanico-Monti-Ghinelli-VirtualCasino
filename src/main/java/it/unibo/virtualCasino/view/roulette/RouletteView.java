package it.unibo.virtualCasino.view.roulette;

import it.unibo.virtualCasino.view.roulette.utils.RouletteViewInfo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

public final class RouletteView extends Application {
    private final int TOTAL_BET_POSITIONS = 113;

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
      Line verticalMainLine = (Line) root.lookup("#verticalMainLine");
      Line horizontalMainLine = (Line) root.lookup("horizontalMainLine");
      
      System.out.println("verticalline: " + verticalMainLine.getStartX());
      System.out.println("horizontalline: " + horizontalMainLine.getStartX());

      //TODO set to roulette view
      for (int i = 0; i < TOTAL_BET_POSITIONS; i++) { 
        Circle circle = createCircle(
          RouletteViewInfo.CIRCLE_CENTER_X,
          RouletteViewInfo.CIRCLE_CENTER_X,
          RouletteViewInfo.CIRCLE_RADIUS,
          RouletteViewInfo.CIRCLE_STROKE_TYPE,
          RouletteViewInfo.CIRCLE_FILL
        );
        roulettePane.getChildren().add(circle);
      }
    }


    private Circle createCircle(
      double centerX,
      double centerY,
      double radius,
      StrokeType strokeType,
      Color color
    ) {
      Circle circle = new Circle(radius);
      circle.setCenterX(centerX);
      circle.setCenterY(centerY);
      circle.setRadius(radius);
      circle.setStrokeType(strokeType);
      circle.setFill(color);
      return circle;
    }

    private double calculateLineLength(double startX, double startY, double endX, double endY) {
      // Use the distance formula
      double distanceX = Math.pow(endX - startX, 2);
      double distanceY = Math.pow(endY - startY, 2);
      return Math.sqrt(distanceX + distanceY);
    }
}

//Define a function for each bet type