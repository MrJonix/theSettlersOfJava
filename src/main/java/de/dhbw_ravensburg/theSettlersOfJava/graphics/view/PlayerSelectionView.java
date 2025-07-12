package de.dhbw_ravensburg.theSettlersOfJava.graphics.view;

import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;


/**
 * UI component that displays a modal dialog for selecting a player.
 *
 * Used in scenarios such as stealing resources from another player.
 * The dialog presents a list of players, allows selection, and returns the chosen player.
 */
public class PlayerSelectionView {

    private final List<Player> players;
    private Player selectedPlayer = null;

    /**
     * Constructs a new PlayerSelectionView with a given list of players to choose from.
     *
     * @param players the list of players displayed in the selection dialog
     */
    public PlayerSelectionView(List<Player> players) {
        this.players = players;
    }

    /**
     * Displays a modal dialog prompting the user to select a player.
     *
     * @return an {@link Optional} containing the selected player,
     *         or {@code Optional.empty()} if the dialog was canceled
     */
    public Optional<Player> showAndWait() {
    	// Create and configure modal window
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Spieler wählen");

        // Instructional label
        Label label = new Label("Wähle einen Spieler zum Stehlen aus:");

        // List of players to select from
        ListView<Player> listView = new ListView<>(FXCollections.observableArrayList(players));
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        // Action buttons
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Abbrechen");
        okButton.setDisable(true);

        // OK-button logic: store selected player and close
        okButton.setOnAction(e -> {
            selectedPlayer = listView.getSelectionModel().getSelectedItem();
            dialog.close();
        });

        // Canel-button logic: clear selection and close
        cancelButton.setOnAction(e -> {
            selectedPlayer = null;
            dialog.close();
        });
        
        // Enable OK-button when selection is made
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            okButton.setDisable(newSel == null);
        });

        // Layout setup
        VBox layout = new VBox(10, label, listView, okButton, cancelButton);
        layout.setPrefSize(300, 400);
        layout.setStyle("-fx-padding: 10; -fx-alignment: center;");

        // Final scene and dialog presentation 
        Scene scene = new Scene(layout);
        dialog.setScene(scene);
        dialog.showAndWait();

        // Return selected player wrapped in Optional
        return Optional.ofNullable(selectedPlayer);
    }
}
