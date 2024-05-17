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
    private final int TABLE_VERTICAL_LINES = 15;
    private final int TABLE_HORIZONTAL_LINES = 4;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("layouts/roulette.fxml"));
        
        Scene scene = new Scene(root);
        
        setUpTableLines(root);
        setUpBetPositionIndicatorsOnTable(root);

        stage.setTitle("Remember...the house always win.");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private void setUpTableLines(Parent root) {
      Pane roulettePane = (Pane) root.lookup("#roulettePane");
     
      
      double offset = RouletteViewInfo.V_LINE_H_OFFSET; 
      double lastVerticalLineStartX = 0;
      
      // create vertical lines
      for (int i = 0; i < RouletteViewInfo.V_LINE_COUNT; i++){
        Line line = createLine(
          RouletteViewInfo.LAYOUT_Y,
          RouletteViewInfo.LAYOUT_X,
          RouletteViewInfo.V_LINE_START_X + (offset * i),
          RouletteViewInfo.V_LINE_START_Y,
          RouletteViewInfo.V_LINE_END_X + (offset * i),
          RouletteViewInfo.V_LINE_END_Y,
          RouletteViewInfo.LINE_STROKE_W
        );
        roulettePane.getChildren().add(line);
        lastVerticalLineStartX = RouletteViewInfo.V_LINE_END_X + (offset * i);
      }

      double verticalLinesLength = calculateLineLength(
        RouletteViewInfo.V_LINE_START_X,
        RouletteViewInfo.V_LINE_START_Y,
        RouletteViewInfo.V_LINE_END_X,
        RouletteViewInfo.V_LINE_END_Y
      );

      offset = (verticalLinesLength / (RouletteViewInfo.H_LINE_COUNT - 1));

      // create horizontal lines 
      for (int i = 0; i < RouletteViewInfo.H_LINE_COUNT; i++) {
        Line line = createLine(
          RouletteViewInfo.LAYOUT_Y,
          RouletteViewInfo.LAYOUT_X,
          RouletteViewInfo.H_LINE_START_X,
          RouletteViewInfo.H_LINE_START_Y + (offset * i),
          lastVerticalLineStartX,
          RouletteViewInfo.H_LINE_END_Y + (offset * i),
          RouletteViewInfo.LINE_STROKE_W
        );
        roulettePane.getChildren().add(line);
      }
    }

    private void setUpBetPositionIndicatorsOnTable(Parent root) {
      Pane roulettePane = (Pane) root.lookup("#roulettePane");

      //TODO set to roulette view
      for (int i = 0; i < TOTAL_BET_POSITIONS; i++) { 
        Circle circle = createCircle(
          RouletteViewInfo.CIRCLE_CENTER_X,
          RouletteViewInfo.CIRCLE_CENTER_X,
          i,
          i,
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
      double layoutX,
      double layoutY,
      double radius,
      StrokeType strokeType,
      Color color
    ) {
      Circle circle = new Circle(radius);
      circle.setCenterX(centerX);
      circle.setCenterY(centerY);
      circle.setLayoutX(layoutX);
      circle.setLayoutY(layoutY);
      circle.setRadius(radius);
      circle.setStrokeType(strokeType);
      circle.setFill(color);
      return circle;
    }

    private Line createLine(
      double layoutX,
      double layoutY,
      double startX,
      double startY,
      double endX,
      double endY,
      double strokeWidth
    ) {
      Line line = new Line();
      line.setLayoutX(layoutX);
      line.setLayoutY(layoutY);
      line.setStartX(startX);
      line.setStartY(startY);
      line.setEndX(endX);
      line.setEndY(endY);
      line.setStrokeWidth(strokeWidth);
      return line;
    }

    private double calculateLineLength(double startX, double startY, double endX, double endY) {
      // Use the distance formula
      double distanceX = Math.pow(endX - startX, 2);
      double distanceY = Math.pow(endY - startY, 2);
      return Math.sqrt(distanceX + distanceY);
    }
}

//Define a function for each bet type