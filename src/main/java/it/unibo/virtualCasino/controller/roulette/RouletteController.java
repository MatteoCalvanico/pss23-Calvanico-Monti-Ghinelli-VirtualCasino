package it.unibo.virtualCasino.controller.roulette;

import java.util.function.UnaryOperator;

import it.unibo.virtualCasino.controller.BaseGameController;
import it.unibo.virtualCasino.controller.IPlaceBetObj;
import it.unibo.virtualCasino.model.games.impl.roulette.Roulette;
import it.unibo.virtualCasino.model.games.impl.roulette.RouletteBet;
import it.unibo.virtualCasino.model.games.impl.roulette.RouletteBetPositionsGrid;
import it.unibo.virtualCasino.model.games.impl.roulette.dtos.Coordinate;
import it.unibo.virtualCasino.model.games.impl.roulette.dtos.RouletteTableLayout;
import it.unibo.virtualCasino.model.games.impl.roulette.enums.RouletteBetType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.util.Callback;

// TODO JavaDoc
public class RouletteController extends BaseGameController {

    private Roulette rouletteGame;

    private RouletteBetPositionsGrid rouletteBetPositionsGrid;

    private Circle selectedBetPositionIndicatorCircle;

    @FXML
    private Text txtPlayer;

    @FXML
    private Text txtBalance;

    @FXML
    private TextField txtBetAmount;

    @FXML
    private Button btnSpeenWheel;

    @FXML
    private ListView<RouletteBet> betList;

    @FXML
    private ComboBox<RouletteBetType> cmbBetType;

    @FXML
    private Pane betPositionsIndicatorsPane;

    // Bet table position indicators (hidden circles used to define roulette numbers
    // table
    // position)
    @FXML
    private Circle topLeftNumsTable;

    @FXML
    private Circle bottomRightNumsTable;

    @FXML
    private Circle bottomLeftBetsTable;

    @Override
    protected void setGame() {
        // Initialize models
        rouletteGame = new Roulette(this.currentPlayer);
        rouletteBetPositionsGrid = new RouletteBetPositionsGrid(
                new RouletteTableLayout(
                        new Coordinate(topLeftNumsTable.getLayoutX(), topLeftNumsTable.getLayoutY()),
                        new Coordinate(topLeftNumsTable.getLayoutY(), bottomRightNumsTable.getLayoutX()),
                        new Coordinate(bottomRightNumsTable.getLayoutX(), bottomRightNumsTable.getLayoutY()),
                        new Coordinate(bottomLeftBetsTable.getLayoutX(), bottomLeftBetsTable.getLayoutY())));

        // Set text items
        txtPlayer.setText(this.currentPlayer.getName());
        txtBalance.setText(Double.toString(this.currentPlayer.getAccount()));

        // Set on action methods
        cmbBetType.setOnAction(event -> onBetTypeSelected());
        btnSpeenWheel.setOnAction(event -> onSpeenWheelClicked());

        // Initialize ListView to display custom cells
        initializeListViewCustomCells();

        // Initialize bet form
        initializeBetForm();
    }

    // IPlaceBetObj method implementation, inherits javadoc
    public double getTotalBetsAmount() {
        return rouletteGame.getTotalBetsAmount() + Integer.parseInt(txtBetAmount.getText());
    }

    // IPlaceBetObj method implementation, inherits javadoc
    public void placeBet() {

        // Retrieve the selected bet type from the ComboBox
        RouletteBetType betType = cmbBetType.getValue();
        if (betType == null) {
            showAlert(AlertType.WARNING, "Invalid bet", "Bet type can't be null");
            return;
        }

        // Retrieve the bet amount from the form and parse it as an integer
        int betAmount = Integer.parseInt(txtBetAmount.getText());
        if (betAmount == 0) {
            showAlert(AlertType.WARNING, "Invalid bet", "Please, bets need to have a value greater then 0");
            return;
        }

        // Retrieve bet position indicator number from selected bet position indicator
        // circle
        if (selectedBetPositionIndicatorCircle == null) {
            showAlert(AlertType.WARNING, "Invalid bet", "Please, select a position on the table for your bet");
            return;
        }

        int betPositionNumber;
        try {
            betPositionNumber = rouletteBetPositionsGrid
                    .getBetPositionIndicatorById(selectedBetPositionIndicatorCircle.getId()).betPositionNumber;
        } catch (Exception e) {
            showAlert(AlertType.WARNING, "Internal error", e.getMessage());
            return;
        }

        // Create a new RouletteBet object using form data
        RouletteBet bet = new RouletteBet(betAmount, betType, betPositionNumber);

        // Place the bet
        rouletteGame.addRouletteBet(bet);
        betList.getItems().add(bet);

        // Empty form fields
        txtBetAmount.setText("0");
        cmbBetType.setValue(null);
    }

    public void onSpeenWheelClicked() {
        if (currentPlayer.isPlayerSolvent(rouletteGame.getTotalBetsAmount())) {
            showAlert(AlertType.WARNING, "Insufficient balance", "Money at risk exeeds your balance");
            return;
        }
        rouletteGame.showResult();
        txtBalance.setText(Double.toString(this.currentPlayer.getAccount()));
    }

    public void onBetTypeSelected() {
        // Clear previous indicators
        betPositionsIndicatorsPane.getChildren().clear();

        // Remove style from the previously selected circle (if any)
        if (selectedBetPositionIndicatorCircle != null) {
            selectedBetPositionIndicatorCircle.setStyle("");
            selectedBetPositionIndicatorCircle = null;
        }

        // Retrieve the selected bet type from the ComboBox
        RouletteBetType betType = cmbBetType.getValue();

        // Create indicator for each bet position indicator
        rouletteBetPositionsGrid
                .getBetPositionIdicatorsListByBetType(betType)
                .forEach(positionIndicator -> betPositionsIndicatorsPane
                        .getChildren()
                        .add(createBetPositionCircle(
                                positionIndicator.coordinate.xAxisValue,
                                positionIndicator.coordinate.yAxisValue,
                                positionIndicator.Id)));
    }

    // Initialize ListView to display custom cells
    private void initializeListViewCustomCells() {
        betList.setItems(FXCollections.observableArrayList());
        betList.setCellFactory(new Callback<ListView<RouletteBet>, ListCell<RouletteBet>>() {
            @Override
            public ListCell<RouletteBet> call(ListView<RouletteBet> param) {
                return new ListCell<RouletteBet>() {
                    @Override
                    protected void updateItem(RouletteBet bet, boolean empty) {
                        super.updateItem(bet, empty);
                        if (bet != null) {
                            // Create a horizontal layout with text and a delete button
                            Button deleteButton = new Button("X");
                            deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");

                            deleteButton.setOnAction(event -> {
                                // Delete bet from the game
                                rouletteGame.deleteBet(bet.getBetId());
                                betList.getItems().remove(bet);
                            });

                            setText(String.format("$%s %s", bet.getBetAmount(), bet.getBetType()));
                            setGraphic(deleteButton); // Set button on the right side
                            // getStyleClass().add("list-cell");
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };
            }
        });
    }

    private void initializeBetForm() {
        // Bet type combo select
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
        txtBetAmount.setTextFormatter(textFormatter);
    }

    private Circle createBetPositionCircle(double layoutX, double layoutY, String positionId) {
        // Create the circle for positioning
        Circle circle = new Circle();

        // Set the positionId as the Id of the circle
        circle.setId(positionId);

        // Set layout position
        circle.setLayoutX(layoutX);
        circle.setLayoutY(layoutY);

        // Set styles
        circle.setRadius(6);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setFill(Color.GRAY);

        // Set the cursor to hand when hovering over the circle
        circle.setOnMouseEntered(event -> {
            circle.setCursor(Cursor.HAND);
            circle.setFill(Color.LIGHTSLATEGRAY);
        });

        // Add an onClick event to handle selection styling and storing the selected
        // circle
        circle.setOnMouseClicked(event -> {
            // Remove style from the previously selected circle (if any)
            if (selectedBetPositionIndicatorCircle != null) {
                selectedBetPositionIndicatorCircle.setStyle(""); // Reset the style
            }

            // Apply new style to the clicked circle
            circle.setStyle("-fx-stroke: blue; -fx-stroke-width: 3;"); // Example of a 'clicked' style

            // Update the class property with the selected circle
            selectedBetPositionIndicatorCircle = circle;
        });

        return circle;
    }
}
