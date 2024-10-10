package it.unibo.virtualCasino.controller.roulette;

import it.unibo.virtualCasino.controller.BaseController;
import it.unibo.virtualCasino.model.games.impl.roulette.Roulette;
import it.unibo.virtualCasino.model.games.impl.roulette.RouletteBet;
import it.unibo.virtualCasino.model.games.impl.roulette.enums.RouletteBetType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.util.Callback;

// TODO JavaDoc
public class RouletteController extends BaseController {

    private Roulette rouletteGame;

    @FXML
    private Button btnCreateBet;

    @FXML
    private Button btnDeleteBet;

    @FXML
    private Button btnSpeenWheel;

    @FXML
    private Text txtPlayer;

    @FXML
    private Text txtBalance;

    @FXML
    private ListView<RouletteBet> betList;

    @FXML
    private TextField txtBetAmount;

    @FXML
    private ComboBox<RouletteBetType> cmbBetType;

    @FXML
    private ToggleGroup executionGroup;

    @Override
    protected void setGame() {
        rouletteGame = new Roulette(this.currentPlayer);

        txtPlayer.setText(this.currentPlayer.getName());

        txtBalance.setText(Double.toString(this.currentPlayer.getAccount()));

        // Initialize ListView to display custom cells
        initializeListViewCustomCells();
    }

    public void createBet() {

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

    private void initializeListViewCustomCells() {
        // Initialize ListView to display custom cells
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
}
