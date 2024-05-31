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
    private double layoutStartX;
    private double layoutStartY;
    private double layoutEndX;
    private double layoutEndY;
    private double hLinesVerticalOffset;
    private double vLinesHorizontalOffset;
    private double bottomLeftLayoutX;
    private double bottomLeftLayoutY;
    private Pane tableContainer;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("layouts/roulette.fxml"));
        
        Scene scene = new Scene(root);

        Pane roulettePane = (Pane) root.lookup("#roulettePane");
        
        tableContainer = (Pane) roulettePane.lookup("#tableContainer");
        
        Circle topLeftNumsTable = (Circle) tableContainer.lookup("#topLeftNumsTable");
        Circle bottomLeftBetsTable = (Circle) tableContainer.lookup("#bottomLeftBetsTable");
        Circle bottomRightNumsTable = (Circle) tableContainer.lookup("#bottomRightNumsTable");

        layoutStartX = topLeftNumsTable.getLayoutX();
        layoutStartY = topLeftNumsTable.getLayoutY();

        layoutEndX = bottomRightNumsTable.getLayoutX();
        layoutEndY = bottomRightNumsTable.getLayoutY();

        bottomLeftLayoutX = bottomLeftBetsTable.getLayoutX();
        bottomLeftLayoutY = bottomLeftBetsTable.getLayoutY();

        hLinesVerticalOffset = Math.abs(layoutStartY - layoutEndY) / RouletteViewInfo.H_LINES_COUNT;

        vLinesHorizontalOffset = Math.abs(layoutStartX - layoutEndX) / RouletteViewInfo.V_LINES_COUNT;
        
        setUpBetPositionIndicatorsOnTable(tableContainer);

        stage.setTitle("Remember...the house always win.");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
  
    private void setUpBetPositionIndicatorsOnTable(Pane tableContainer) {

      // creates horizontal split bets positions indicators
      createHorizontalSplitBetsPositionsInidicatos();

      // creates vertical split bets positions indicators
      createVerticalSplitBetsPositionsInidicatos();

      // creates street bets positions indicators
      createStreetBetsPositionsInidicatos();
      
      // creates double street bets positions indicators
      createDoubleStreetBetsPositionsInidicatos();

      // creates corner bets positions indicators
      createCornerBetsPositionsInidicatos();

      // creates column bets positions indicators
      createColumnBetsPositionsInidicatos();

      // creates dozen bets positions indicators
      createDozenBetsPositionsInidicatos();

      // creates dozen bets positions indicators
      createHalfBetsPositionsInidicatos();

      // creates even or odd bets positions indicators
      createEvenOddBetsPositionsInidicatos();

      // creates red or black bets positions indicators
      createRedBlackBetsPositionsInidicatos();
    }

    private void createHorizontalSplitBetsPositionsInidicatos() {
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
    }

    private void createVerticalSplitBetsPositionsInidicatos() {
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
    }

    private void createStreetBetsPositionsInidicatos() {
      for (int j = 0; j < RouletteViewInfo.V_SPLIT_BETS; j+=2) {
        double layoutX = layoutStartX + (vLinesHorizontalOffset * (j + 1) / 2);
        
        Circle circle = createBetPositionCircle(
          layoutX,
          layoutStartY
        );

        tableContainer.getChildren().add(circle);
      }
    }

    private void createDoubleStreetBetsPositionsInidicatos() {
      for (int j = 1; j < RouletteViewInfo.V_SPLIT_BETS / 2; j++) {
        double layoutX = layoutStartX + (vLinesHorizontalOffset * (j));
        
        Circle circle = createBetPositionCircle(
          layoutX,
          layoutEndY
        );

        tableContainer.getChildren().add(circle);
      }
    }

    private void createCornerBetsPositionsInidicatos() {
      for (int i = 1; i < RouletteViewInfo.H_LINES_COUNT; i++) {
        double layoutY = layoutStartY + (hLinesVerticalOffset * i);
        
        for (int j = 0; j < (RouletteViewInfo.V_SPLIT_BETS / 2) - 1; j++) {
          double layoutX = layoutStartX + (vLinesHorizontalOffset * (j + 1));
          
          Circle circle = createBetPositionCircle(
            layoutX,
            layoutY
          );

          tableContainer.getChildren().add(circle);
        }
      }
    }

    private void createColumnBetsPositionsInidicatos() {
      for (int i = 0; i < RouletteViewInfo.H_LINES_COUNT; i++) {
        double layoutY = layoutStartY + (hLinesVerticalOffset * i) + hLinesVerticalOffset / 2;
          
        Circle circle = createBetPositionCircle(
          layoutEndX - vLinesHorizontalOffset / 2,
          layoutY
        );

        tableContainer.getChildren().add(circle);
      }
    }

    private void createDozenBetsPositionsInidicatos() {
      double layoutY = layoutEndY + (Math.abs(layoutEndY - bottomLeftLayoutY) / 4);

      System.out.println("layoutY: " + layoutY);
      
      double offset = (layoutEndX - vLinesHorizontalOffset - layoutStartX) / 3;

      for(int i = 0; i < 3; i++) {  
        double layoutX = layoutStartX + ((offset * i) + (offset / 2));
          
        Circle circle = createBetPositionCircle(
          layoutX,
          layoutY
        );

        tableContainer.getChildren().add(circle);
      }
    }

    private void createHalfBetsPositionsInidicatos() {
      double layoutY = bottomLeftLayoutY - (Math.abs(layoutEndY - bottomLeftLayoutY) / 4);
      double offset = (layoutEndX - vLinesHorizontalOffset - layoutStartX) / 6;
          
      Circle firstHalf = createBetPositionCircle(
        layoutStartX + (offset / 2),
        layoutY
      );

      
      Circle secondHalf = createBetPositionCircle(
        layoutEndX - vLinesHorizontalOffset - (offset / 2),
        layoutY
      );
        
      tableContainer.getChildren().add(firstHalf);
      tableContainer.getChildren().add(secondHalf);

    }

    private void createEvenOddBetsPositionsInidicatos() {
      double layoutY = bottomLeftLayoutY - (Math.abs(layoutEndY - bottomLeftLayoutY) / 4);
      double offset = (layoutEndX - vLinesHorizontalOffset - layoutStartX) / 6;
          
      Circle firstHalf = createBetPositionCircle(
        layoutStartX + (offset / 2) + offset,
        layoutY
      );

      
      Circle secondHalf = createBetPositionCircle(
        layoutEndX - vLinesHorizontalOffset - (offset / 2) - offset,
        layoutY
      );
        
      tableContainer.getChildren().add(firstHalf);
      tableContainer.getChildren().add(secondHalf);

    }

    private void createRedBlackBetsPositionsInidicatos() {
      double layoutY = bottomLeftLayoutY - (Math.abs(layoutEndY - bottomLeftLayoutY) / 4);
      double offset = (layoutEndX - vLinesHorizontalOffset - layoutStartX) / 6;
          
      Circle firstHalf = createBetPositionCircle(
        layoutStartX + (offset / 2) + offset*2,
        layoutY
      );

      
      Circle secondHalf = createBetPositionCircle(
        layoutEndX - vLinesHorizontalOffset - (offset / 2) - offset*2,
        layoutY
      );
        
      tableContainer.getChildren().add(firstHalf);
      tableContainer.getChildren().add(secondHalf);

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
