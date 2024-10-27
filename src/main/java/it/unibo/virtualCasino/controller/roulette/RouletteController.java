package it.unibo.virtualCasino.controller.roulette;

import java.util.function.UnaryOperator;

import it.unibo.virtualCasino.controller.BaseGameController;
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

/**
 * Controller class for handling user interactions with the Roulette game UI.
 * Manages bet placement, game state, and updates to the player's balance and
 * winning number display.
 */
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
    private Text txtWinningNumber;

    @FXML
    private Button btnSpeenWheel;

    @FXML
    private ListView<RouletteBet> betList;

    @FXML
    private ComboBox<RouletteBetType> cmbBetType;

    @FXML
    private Pane betPositionsIndicatorsPane;

    @FXML
    private Circle topLeftNumsTable;

    @FXML
    private Circle bottomRightNumsTable;

    @FXML
    private Circle bottomLeftBetsTable;

    /**
     * Initializes the game settings and UI elements, setting up event handlers and
     * form input constraints.
     */
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

    /**
     * Returns the total amount of all current bets combined with the input in
     * txtBetAmount field.
     *
     * @return the sum of all bets placed and current bet input.
     */
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
            showAlert(AlertType.ERROR, "Error", e.getMessage());
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

    /**
     * Spins the roulette wheel, determines the winning number, and updates the
     * player's balance based on bets.
     */
    public void onSpeenWheelClicked() {
        if (!currentPlayer.isPlayerSolvent(rouletteGame.getTotalBetsAmount())) {
            showAlert(AlertType.WARNING, "Insufficient balance", "Money at risk exeeds your balance");
            return;
        }
        rouletteGame.showResult();

        try {
            txtWinningNumber.setText(rouletteGame.getWinningNumber().toString());
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", e.getMessage());
        }

        txtBalance.setText(Double.toString(this.currentPlayer.getAccount()));
    }

    /**
     * Updates the list of selectable bet positions on the table according to the
     * chosen bet type.
     */
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

    /**
     * Initializes the custom cell layout for the bet list, including a delete
     * button
     * for each bet entry.
     */
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

    /**
     * Sets up the bet form for user input, applying a formatter to ensure only
     * numeric values are entered for bet amounts.
     */
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

    /**
     * Creates a new circle element to represent a selectable bet position indicator
     * on the UI.
     *
     * @param layoutX    the x-axis position of the circle.
     * @param layoutY    the y-axis position of the circle.
     * @param positionId the identifier for the position.
     * @return a circle representing the bet position indicator.
     */
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
        circle.setFill(Color.GOLD);

        // Set the cursor to hand when hovering over the circle
        circle.setOnMouseEntered(event -> {
            circle.setCursor(Cursor.HAND);
            circle.setFill(Color.LIGHTSLATEGRAY);
        });
        circle.setOnMouseExited(event -> {
            circle.setCursor(Cursor.DEFAULT);
            circle.setFill(Color.GOLD);
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
