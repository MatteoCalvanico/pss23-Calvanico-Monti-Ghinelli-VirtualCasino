package it.unibo.virtualCasino.view.roulette;

import java.util.function.UnaryOperator;

import it.unibo.virtualCasino.model.games.impl.roulette.RouletteBetPositionsGrid;
import it.unibo.virtualCasino.model.games.impl.roulette.dtos.Coordinate;
import it.unibo.virtualCasino.model.games.impl.roulette.dtos.RouletteTableLayoutDto;
import it.unibo.virtualCasino.model.games.impl.roulette.enums.RouletteBetType;
import it.unibo.virtualCasino.view.roulette.utils.RouletteViewInfo;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public final class RouletteView extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("layouts/roulette.fxml"));

        Scene scene = new Scene(root);

        Pane roulettePane = (Pane) root.lookup("#roulettePane");

        Pane tableContainer = (Pane) roulettePane.lookup("#tableContainer");
        initializeBetPositionsIndicators(tableContainer);

        Pane betFormContainer = (Pane) roulettePane.lookup("#betFormContainer");
        initializeBetForm(betFormContainer);

        stage.setTitle("Remember...the house always win.");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initializeBetPositionsIndicators(Pane tableContainer) {

        // Get roulette table position references
        Circle topLeftNumsTable = (Circle) tableContainer.lookup("#topLeftNumsTable");
        Circle bottomRightNumsTable = (Circle) tableContainer.lookup("#bottomRightNumsTable");
        Circle bottomLeftBetsTable = (Circle) tableContainer.lookup("#bottomLeftBetsTable");

        // Initialize bet positions grid object
        RouletteBetPositionsGrid rouletteBetPositionsGrid = new RouletteBetPositionsGrid(
                new RouletteTableLayoutDto(
                        new Coordinate(topLeftNumsTable.getLayoutX(), topLeftNumsTable.getLayoutY()),
                        new Coordinate(topLeftNumsTable.getLayoutY(), bottomRightNumsTable.getLayoutX()),
                        new Coordinate(bottomRightNumsTable.getLayoutX(), bottomRightNumsTable.getLayoutY()),
                        new Coordinate(bottomLeftBetsTable.getLayoutX(), bottomLeftBetsTable.getLayoutY())));

        // Create indicator for each bet position indicator
        rouletteBetPositionsGrid
                .getBetPositionIdicatorsList()
                .forEach(positionIndicator -> {
                    Circle circle = createBetPositionCircle(
                            positionIndicator.coordinate.xAxisValue,
                            positionIndicator.coordinate.yAxisValue);
                    tableContainer.getChildren().add(circle);
                });
    }

    private Circle createBetPositionCircle(
            double layoutX,
            double layoutY) {
        Circle circle = new Circle();
        circle.setLayoutX(layoutX);
        circle.setLayoutY(layoutY);
        circle.setRadius(RouletteViewInfo.CIRCLE_RADIUS);
        circle.setStrokeType(RouletteViewInfo.CIRCLE_STROKE_TYPE);
        circle.setFill(RouletteViewInfo.CIRCLE_FILL);
        return circle;
    }

    private void initializeBetForm(Pane betFormContainer) {
        // Combo select
        @SuppressWarnings("unchecked")
        ComboBox<RouletteBetType> cmbBetType = (ComboBox<RouletteBetType>) betFormContainer.lookup("#cmbBetType");
        cmbBetType.setItems(FXCollections.observableArrayList(RouletteBetType.values()));

        // Text field
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) { // This regex allows only digits
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        TextField txtBetAmount = (TextField) betFormContainer.lookup("#txtBetAmount");
        txtBetAmount.setTextFormatter(textFormatter);
    }
}
