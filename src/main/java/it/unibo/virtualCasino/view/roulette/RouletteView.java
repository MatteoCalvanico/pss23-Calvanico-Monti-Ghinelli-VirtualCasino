package it.unibo.virtualCasino.view.roulette;

import it.unibo.virtualCasino.view.roulette.utils.RouletteViewInfo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public final class RouletteView extends Application {
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
      Pane tableContainer = (Pane) roulettePane.lookup("#tableContainer");
      Circle topLeftNumsTable = (Circle) tableContainer.lookup("#topLeftNumsTable");
      Circle bottomRightNumsTable = (Circle) tableContainer.lookup("#bottomRightNumsTable");

      double layoutStartX = topLeftNumsTable.getLayoutX();
      double layoutStartY = topLeftNumsTable.getLayoutY();

      double layoutEndX = bottomRightNumsTable.getLayoutX();
      double layoutEndY = bottomRightNumsTable.getLayoutY();

      double hLinesVerticalOffset = Math.abs(layoutStartY - layoutEndY) / RouletteViewInfo.H_LINES_COUNT;

      double vLinesHorizontalOffset = Math.abs(layoutStartX - layoutEndX) / RouletteViewInfo.V_LINES_COUNT;

      // creates horizontal split bets positions indicators
      for (int i = 1; i < RouletteViewInfo.H_LINES_COUNT; i++) {
        double layoutY = layoutStartY + (hLinesVerticalOffset * i);
        
        for (int j = 0; j < RouletteViewInfo.V_SPLIT_BETS; j+=2) {
          double layoutX = layoutStartX + (vLinesHorizontalOffset * (j + 1) / 2);
          
          Circle circle = createBetPositionCircle(
            layoutX,
            layoutY
          );

          tableContainer.getChildren().add(circle);
        }
      }

      // creates vertical split bets positions indicators
      for (int i = 0; i < RouletteViewInfo.H_LINES_COUNT; i++) {
        double layoutY = layoutStartY + (hLinesVerticalOffset * i) + hLinesVerticalOffset / 2;
        
        for (int j = 1; j <= RouletteViewInfo.H_SPLIT_BETS / 3; j++) {
          double layoutX = layoutStartX + (vLinesHorizontalOffset * j);
          
          Circle circle = createBetPositionCircle(
            layoutX,
            layoutY
          );

          tableContainer.getChildren().add(circle);
        }
      }

      //creates street bets positions indicators
      for (int j = 0; j < RouletteViewInfo.V_SPLIT_BETS; j+=2) {
        double layoutX = layoutStartX + (vLinesHorizontalOffset * (j + 1) / 2);
        
        Circle circle = createBetPositionCircle(
          layoutX,
          layoutStartY
        );

        tableContainer.getChildren().add(circle);
      }

      
      //creates double street bets positions indicators
      for (int j = 1; j < RouletteViewInfo.V_SPLIT_BETS / 2; j++) {
        double layoutX = layoutStartX + (vLinesHorizontalOffset * (j));
        
        Circle circle = createBetPositionCircle(
          layoutX,
          layoutEndY
        );

        tableContainer.getChildren().add(circle);
      }
      

    }


    private Circle createBetPositionCircle(
      double layoutX,
      double layoutY
    ) {
      Circle circle = new Circle();   
      circle.setLayoutX(layoutX);
      circle.setLayoutY(layoutY);   
      circle.setRadius(RouletteViewInfo.CIRCLE_RADIUS);
      circle.setStrokeType(RouletteViewInfo.CIRCLE_STROKE_TYPE);
      circle.setFill(RouletteViewInfo.CIRCLE_FILL);
      return circle;
    }
}
