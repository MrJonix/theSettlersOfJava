package de.dhbw_ravensburg.theSettlersOfJava.graphics.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.UIHelpers;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import de.dhbw_ravensburg.theSettlersOfJava.units.PlayerColor;

import java.util.ArrayList;
import java.util.List;

/**
 * UI component for setting up players before the game starts.
 * 
 * Allows the user to select the number of players (3 or 4),
 * enter player names, and confirm the setup.
 * 
 * Each player is assigned a predefined color and validated before starting the game.
 */
public class PlayerSetupView {

	/**
	 * Functional interface for handling confirmed player setups.
	 * Called when the user has entered all player names and clicks "Start Game".
	 */

    @FunctionalInterface
    public interface OnPlayersConfirmed {
        void handle(List<Player> players);
    }
    
    /** Fixed color assignment order for up to 4 players. */
    private static final PlayerColor[] PREASSIGNED_COLORS = {PlayerColor.ORANGE, PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.RED};
    private static final String ACTIVE_STYLE = "-fx-background-color: #FFD700; -fx-text-fill: #000;";
    private static final String INACTIVE_STYLE = "-fx-background-color: #E0E0E0; -fx-text-fill: #888;";

    /**
     * Creates the player setup UI.
     *
     * @param onBack    Runnable for back button.
     * @param onConfirm Callback when players are confirmed.
     * @return VBox layout of the player setup screen.
     */
    public static VBox create(Runnable onBack, OnPlayersConfirmed onConfirm) {
        VBox card = new VBox(20);
        card.setMaxWidth(500);
        card.setMaxHeight(600);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-padding: 35;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 16, 0.2, 0, 6);");

        Text title = new Text("Player Setup");
        title.setFont(Font.font("Myriad Pro", FontWeight.EXTRA_BOLD, 52));
        title.setFill(Color.web("#FFC700"));

        Label instruction = new Label("Enter names for players:");
        instruction.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 22));
        instruction.setTextFill(Color.web("#222"));
        instruction.setTextAlignment(TextAlignment.CENTER);

        VBox playersBox = new VBox(15);
        playersBox.setAlignment(Pos.CENTER);

        List<HBox> playerRows = createPlayerRows();

        // Buttons for selecting number of players
        Button threePlayersBtn = new Button("3 Players");
        Button fourPlayersBtn = new Button("4 Players");

        threePlayersBtn.setStyle(ACTIVE_STYLE);  // Default to 3 players
        fourPlayersBtn.setStyle(INACTIVE_STYLE);

        threePlayersBtn.setOnAction(e -> {
            threePlayersBtn.setStyle(ACTIVE_STYLE);
            fourPlayersBtn.setStyle(INACTIVE_STYLE);
            playersBox.getChildren().clear();
            for (int i = 0; i < 3; i++) {
                playersBox.getChildren().add(playerRows.get(i));
            }
        });

        fourPlayersBtn.setOnAction(e -> {
            fourPlayersBtn.setStyle(ACTIVE_STYLE);
            threePlayersBtn.setStyle(INACTIVE_STYLE);
            playersBox.getChildren().clear();
            for (int i = 0; i < 4; i++) {
                playersBox.getChildren().add(playerRows.get(i));
            }
        });

        HBox playerCountBox = new HBox(10, threePlayersBtn, fourPlayersBtn);
        playerCountBox.setAlignment(Pos.CENTER);

        // Add initial players (3 players setup)
        for (int i = 0; i < 3; i++) {
            playersBox.getChildren().add(playerRows.get(i));
        }

        // Buttons
        Button startGameBtn = UIHelpers.createStyledButton("START GAME", () -> {
            List<Player> players = new ArrayList<>();
            int playerCount = threePlayersBtn.getStyle().equals(ACTIVE_STYLE) ? 3 : 4;
            for (int i = 0; i < playerCount; i++) {
                HBox row = playerRows.get(i);
                TextField nameField = (TextField) row.getChildren().get(1);

                String name = nameField.getText().trim();
                String color = PREASSIGNED_COLORS[i].getColor().toString();

                if (!name.isEmpty()) {
                    players.add(new Player(name, PREASSIGNED_COLORS[i]));
                }
            }

            if (players.size() == playerCount) {
                onConfirm.handle(players);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Please enter names for " + playerCount + " players.");
                alert.showAndWait();
            }
        }, "#FFD700", "#000");

        Button backBtn = UIHelpers.createStyledButton("BACK", onBack, "#E0E0E0", "#888");

        VBox buttons = new VBox(12, startGameBtn, backBtn);
        buttons.setAlignment(Pos.CENTER);

        card.getChildren().addAll(title, instruction, playerCountBox, playersBox, buttons);
        return card;
    }

    private static List<HBox> createPlayerRows() {
        List<HBox> playerRows = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            HBox row = new HBox(15);
            row.setAlignment(Pos.CENTER);

            Circle colorCircle = new Circle(10);
            colorCircle.setFill(PREASSIGNED_COLORS[i].getColor());

            TextField nameField = new TextField();
            nameField.setPromptText("Player " + (i + 1) + " Name");
            nameField.setPrefWidth(180);

            nameField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.trim().isEmpty()) {
                    nameField.setStyle("-fx-border-color: red;");
                } else {
                    nameField.setStyle(null);
                }
            });

            row.getChildren().addAll(colorCircle, nameField);
            playerRows.add(row);
        }
        return playerRows;
    }
}