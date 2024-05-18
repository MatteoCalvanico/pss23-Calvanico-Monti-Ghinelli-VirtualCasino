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
      Pane lineContainer = (Pane) roulettePane.lookup("#lineContainer");
      Line verticalMainLine = (Line) lineContainer.lookup("#verticalMainLine");
      Line horizontalMainLine = (Line) lineContainer.lookup("#horizontalMainLine");

      double hLinesVerticalOffset = calculateLineLength(
        horizontalMainLine.getStartX(),
        horizontalMainLine.getStartY(),
        horizontalMainLine.getEndX(),
        horizontalMainLine.getEndY() 
      ) / RouletteViewInfo.H_LINES_COUNT;

      double vLinesHorizontalOffset = calculateLineLength(
        verticalMainLine.getStartX(),
        verticalMainLine.getStartY(),
        verticalMainLine.getEndX(),
        verticalMainLine.getEndY() 
      ) / RouletteViewInfo.V_LINES_COUNT;

      //TODO set to roulette view

      // create horizontal split bets possible position indicators
      for (int i = 1; i < RouletteViewInfo.H_LINES_COUNT - 1; i++) {
        double centerY = horizontalMainLine.getStartY() + (hLinesVerticalOffset * i);
        
        int startIndex = i == 1 ? 1 : 0;

        for (int j = startIndex; j < RouletteViewInfo.V_SPLIT_BETS; j+=2) {
          double centerX = horizontalMainLine.getStartX() + (vLinesHorizontalOffset * (j + 1) / 2);
          
          Circle circle = createBetPositionCircle(
            centerX,
            centerY
          );

          roulettePane.getChildren().add(circle);
        }
      }
    }


    private Circle createBetPositionCircle(
      double centerX,
      double centerY
    ) {
      Circle circle = new Circle();
      circle.setLayoutX(0);
      circle.setLayoutY(0);
      circle.setCenterX(centerX);
      circle.setCenterY(centerY);
      circle.setRadius(RouletteViewInfo.CIRCLE_RADIUS);
      circle.setStrokeType(RouletteViewInfo.CIRCLE_STROKE_TYPE);
      circle.setFill(RouletteViewInfo.CIRCLE_FILL);
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