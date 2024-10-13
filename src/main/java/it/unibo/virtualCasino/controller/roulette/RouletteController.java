package it.unibo.virtualCasino.controller.roulette;

import java.util.function.UnaryOperator;

import it.unibo.virtualCasino.controller.BaseController;
import it.unibo.virtualCasino.model.games.impl.roulette.Roulette;
import it.unibo.virtualCasino.model.games.impl.roulette.RouletteBet;
import it.unibo.virtualCasino.model.games.impl.roulette.RouletteBetPositionsGrid;
import it.unibo.virtualCasino.model.games.impl.roulette.dtos.CoordinateDto;
import it.unibo.virtualCasino.model.games.impl.roulette.dtos.RouletteTableLayoutDto;
import it.unibo.virtualCasino.model.games.impl.roulette.enums.RouletteBetType;
import it.unibo.virtualCasino.view.roulette.utils.RouletteViewInfo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Callback;

// TODO JavaDoc
public class RouletteController extends BaseController {

    private Roulette rouletteGame;

    private RouletteBetPositionsGrid rouletteBetPositionsGrid;

    // Text
    @FXML
    private Text txtPlayer;

    @FXML
    private Text txtBalance;

    @FXML
    private TextField txtBetAmount;

    // Buttons
    @FXML
    private Button btnCreateBet;

    @FXML
    private Button btnSpeenWheel; // TODO implement

    // Lists
    @FXML
    private ListView<RouletteBet> betList;

    // Combo box
    @FXML
    private ComboBox<RouletteBetType> cmbBetType;

    @FXML
    private ToggleGroup executionGroup; // TODO implement

    // Panes
    // @FXML
    // private Pane betFormPane;

    @FXML
    private Pane betPositionsIndicatorsPane;

    // Bet position indicators (hidden circles used to define roulette numbers table
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
                new RouletteTableLayoutDto(
                        new CoordinateDto(topLeftNumsTable.getLayoutX(), topLeftNumsTable.getLayoutY()),
                        new CoordinateDto(topLeftNumsTable.getLayoutY(), bottomRightNumsTable.getLayoutX()),
                        new CoordinateDto(bottomRightNumsTable.getLayoutX(), bottomRightNumsTable.getLayoutY()),
                        new CoordinateDto(bottomLeftBetsTable.getLayoutX(), bottomLeftBetsTable.getLayoutY())));

        // Set text items
        txtPlayer.setText(this.currentPlayer.getName());
        txtBalance.setText(Double.toString(this.currentPlayer.getAccount()));

        // Set on action methods
        btnCreateBet.setOnAction(event -> onCreateBetClicked());
        cmbBetType.setOnAction(event -> onBetTypeSelected());

        // Initialize ListView to display custom cells
        initializeListViewCustomCells();

        // Initialize bet form
        initializeBetForm();
    }

    public void onCreateBetClicked() {

        // Retrieve the selected bet type from the ComboBox
        RouletteBetType betType = cmbBetType.getValue();

        if (betType == null) {
            // TODO alert message
            System.out.println("Bet type can't be null");
            return;
        }

        // Retrieve the bet amount from the form and parse it as an integer
        int betAmount = Integer.parseInt(txtBetAmount.getText());

        // TODO replace radio buttons with table position indicators
        // Retrieve the selected execution from the ToggleGroup
        // RadioButton selectedRadio = (RadioButton) executionGroup.getSelectedToggle();
        // String executionChoice = selectedRadio.getText();

        // Create a new RouletteBet object using form data
        RouletteBet bet = new RouletteBet(betAmount, betType, 1); // Use form data

        // Try to place the bet
        try {
            rouletteGame.bet(bet);
        } catch (Exception e) {
            // TODO alert message
            System.out.println(e.getMessage());
            return;
        }

        betList.getItems().add(bet);

        // Empty form fields
        txtBetAmount.setText("0");
        cmbBetType.setValue(null);
    }

    public void onBetTypeSelected() {
        // Retrieve the selected bet type from the ComboBox
        RouletteBetType betType = cmbBetType.getValue();

        // Clear previous indicators
        betPositionsIndicatorsPane.getChildren().clear();

        // Create indicator for each bet position indicator
        rouletteBetPositionsGrid
                .getBetPositionIdicatorsList()
                .forEach(positionIndicator -> {
                    if (positionIndicator.betType == betType) {
                        Circle circle = createBetPositionCircle(
                                positionIndicator.coordinate.xAxisValue,
                                positionIndicator.coordinate.yAxisValue);
                        betPositionsIndicatorsPane.getChildren().add(circle);
                    }
                });
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

    private static Circle createBetPositionCircle(
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
}
