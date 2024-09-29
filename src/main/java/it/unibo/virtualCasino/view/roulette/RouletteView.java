package it.unibo.virtualCasino.view.roulette;

import java.util.function.UnaryOperator;

import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteBetTypes;
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
    private double layoutStartX;
    private double layoutStartY;
    private double layoutEndX;
    private double layoutEndY;
    private double hLinesVerticalOffset;
    private double vLinesHorizontalOffset;
    private double bottomLeftLayoutY;
    private Pane tableContainer;
    private Pane betFormContainer;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("layouts/roulette.fxml"));

        Scene scene = new Scene(root);

        Pane roulettePane = (Pane) root.lookup("#roulettePane");

        tableContainer = (Pane) roulettePane.lookup("#tableContainer");
        betFormContainer = (Pane) roulettePane.lookup("#betFormContainer");

        Circle topLeftNumsTable = (Circle) tableContainer.lookup("#topLeftNumsTable");
        Circle bottomLeftBetsTable = (Circle) tableContainer.lookup("#bottomLeftBetsTable");
        Circle bottomRightNumsTable = (Circle) tableContainer.lookup("#bottomRightNumsTable");

        layoutStartX = topLeftNumsTable.getLayoutX();
        layoutStartY = topLeftNumsTable.getLayoutY();

        layoutEndX = bottomRightNumsTable.getLayoutX();
        layoutEndY = bottomRightNumsTable.getLayoutY();

        bottomLeftLayoutY = bottomLeftBetsTable.getLayoutY();

        hLinesVerticalOffset = Math.abs(layoutStartY - layoutEndY) / RouletteViewInfo.H_LINES_COUNT;

        vLinesHorizontalOffset = Math.abs(layoutStartX - layoutEndX) / RouletteViewInfo.V_LINES_COUNT;

        initializeBetPositionIndicatorsOnTable();

        initializeBetForm();

        stage.setTitle("Remember...the house always win.");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initializeBetPositionIndicatorsOnTable() {

        // creates horizontal split bets positions indicators
        createHorizontalSplitBetsPositionsInidicators();

        // creates vertical split bets positions indicators
        createVerticalSplitBetsPositionsInidicators();

        // creates street bets positions indicators
        createStreetBetsPositionsInidicators();

        // creates double street bets positions indicators
        createDoubleStreetBetsPositionsInidicators();

        // creates corner bets positions indicators
        createCornerBetsPositionsInidicators();

        // creates column bets positions indicators
        createColumnBetsPositionsInidicators();

        // creates dozen bets positions indicators
        createDozenBetsPositionsInidicators();

        // creates dozen bets positions indicators
        createHalfBetsPositionsInidicators();

        // creates even or odd bets positions indicators
        createEvenOddBetsPositionsInidicators();

        // creates red or black bets positions indicators
        createRedBlackBetsPositionsInidicators();
    }

    private void createHorizontalSplitBetsPositionsInidicators() {
        for (int i = 1; i < RouletteViewInfo.H_LINES_COUNT; i++) {
            double layoutY = layoutStartY + (hLinesVerticalOffset * i);

            for (int j = 0; j < RouletteViewInfo.V_SPLIT_BETS; j += 2) {
                double layoutX = layoutStartX + (vLinesHorizontalOffset * (j + 1) / 2);

                Circle circle = createBetPositionCircle(
                        layoutX,
                        layoutY);

                tableContainer.getChildren().add(circle);
            }
        }
    }

    private void createVerticalSplitBetsPositionsInidicators() {
        for (int i = 0; i < RouletteViewInfo.H_LINES_COUNT; i++) {
            double layoutY = layoutStartY + (hLinesVerticalOffset * i) + hLinesVerticalOffset / 2;

            for (int j = 1; j <= RouletteViewInfo.H_SPLIT_BETS / 3; j++) {
                double layoutX = layoutStartX + (vLinesHorizontalOffset * j);

                Circle circle = createBetPositionCircle(
                        layoutX,
                        layoutY);

                tableContainer.getChildren().add(circle);
            }
        }
    }

    private void createStreetBetsPositionsInidicators() {
        for (int j = 0; j < RouletteViewInfo.V_SPLIT_BETS; j += 2) {
            double layoutX = layoutStartX + (vLinesHorizontalOffset * (j + 1) / 2);

            Circle circle = createBetPositionCircle(
                    layoutX,
                    layoutStartY);

            tableContainer.getChildren().add(circle);
        }
    }

    private void createDoubleStreetBetsPositionsInidicators() {
        for (int j = 1; j < RouletteViewInfo.V_SPLIT_BETS / 2; j++) {
            double layoutX = layoutStartX + (vLinesHorizontalOffset * (j));

            Circle circle = createBetPositionCircle(
                    layoutX,
                    layoutEndY);

            tableContainer.getChildren().add(circle);
        }
    }

    private void createCornerBetsPositionsInidicators() {
        for (int i = 1; i < RouletteViewInfo.H_LINES_COUNT; i++) {
            double layoutY = layoutStartY + (hLinesVerticalOffset * i);

            for (int j = 0; j < (RouletteViewInfo.V_SPLIT_BETS / 2) - 1; j++) {
                double layoutX = layoutStartX + (vLinesHorizontalOffset * (j + 1));

                Circle circle = createBetPositionCircle(
                        layoutX,
                        layoutY);

                tableContainer.getChildren().add(circle);
            }
        }
    }

    private void createColumnBetsPositionsInidicators() {
        for (int i = 0; i < RouletteViewInfo.H_LINES_COUNT; i++) {
            double layoutY = layoutStartY + (hLinesVerticalOffset * i) + hLinesVerticalOffset / 2;

            Circle circle = createBetPositionCircle(
                    layoutEndX - vLinesHorizontalOffset / 2,
                    layoutY);

            tableContainer.getChildren().add(circle);
        }
    }

    private void createDozenBetsPositionsInidicators() {
        double layoutY = layoutEndY + (Math.abs(layoutEndY - bottomLeftLayoutY) / 4);

        double offset = (layoutEndX - vLinesHorizontalOffset - layoutStartX) / 3;

        for (int i = 0; i < 3; i++) {
            double layoutX = layoutStartX + ((offset * i) + (offset / 2));

            Circle circle = createBetPositionCircle(
                    layoutX,
                    layoutY);

            tableContainer.getChildren().add(circle);
        }
    }

    private void createHalfBetsPositionsInidicators() {
        double layoutY = bottomLeftLayoutY - (Math.abs(layoutEndY - bottomLeftLayoutY) / 4);
        double offset = (layoutEndX - vLinesHorizontalOffset - layoutStartX) / 6;

        Circle firstHalf = createBetPositionCircle(
                layoutStartX + (offset / 2),
                layoutY);

        Circle secondHalf = createBetPositionCircle(
                layoutEndX - vLinesHorizontalOffset - (offset / 2),
                layoutY);

        tableContainer.getChildren().add(firstHalf);
        tableContainer.getChildren().add(secondHalf);

    }

    private void createEvenOddBetsPositionsInidicators() {
        double layoutY = bottomLeftLayoutY - (Math.abs(layoutEndY - bottomLeftLayoutY) / 4);
        double offset = (layoutEndX - vLinesHorizontalOffset - layoutStartX) / 6;

        Circle firstHalf = createBetPositionCircle(
                layoutStartX + (offset / 2) + offset,
                layoutY);

        Circle secondHalf = createBetPositionCircle(
                layoutEndX - vLinesHorizontalOffset - (offset / 2) - offset,
                layoutY);

        tableContainer.getChildren().add(firstHalf);
        tableContainer.getChildren().add(secondHalf);

    }

    private void createRedBlackBetsPositionsInidicators() {
        double layoutY = bottomLeftLayoutY - (Math.abs(layoutEndY - bottomLeftLayoutY) / 4);
        double offset = (layoutEndX - vLinesHorizontalOffset - layoutStartX) / 6;

        Circle firstHalf = createBetPositionCircle(
                layoutStartX + (offset / 2) + offset * 2,
                layoutY);

        Circle secondHalf = createBetPositionCircle(
                layoutEndX - vLinesHorizontalOffset - (offset / 2) - offset * 2,
                layoutY);

        tableContainer.getChildren().add(firstHalf);
        tableContainer.getChildren().add(secondHalf);

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

    private void initializeBetForm() {
        // Combo select
        @SuppressWarnings("unchecked")
        ComboBox<RouletteBetTypes> cmbBetType = (ComboBox<RouletteBetTypes>) betFormContainer.lookup("#cmbBetType");
        cmbBetType.setItems(FXCollections.observableArrayList(RouletteBetTypes.values()));

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
