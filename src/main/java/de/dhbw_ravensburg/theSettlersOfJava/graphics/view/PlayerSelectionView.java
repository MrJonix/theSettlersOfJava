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

public class PlayerSelectionView {

    private final List<Player> players;
    private Player selectedPlayer = null;

    public PlayerSelectionView(List<Player> players) {
        this.players = players;
    }

    /**
     * Zeigt das Auswahlfenster und gibt den gewählten Spieler zurück.
     * Rückgabe ist Optional.empty(), wenn abgebrochen wurde.
     */
    public Optional<Player> showAndWait() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Spieler wählen");

        Label label = new Label("Wähle einen Spieler zum Stehlen aus:");

        ListView<Player> listView = new ListView<>(FXCollections.observableArrayList(players));
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        Button okButton = new Button("OK");
        Button cancelButton = new Button("Abbrechen");
        okButton.setDisable(true);

        okButton.setOnAction(e -> {
            selectedPlayer = listView.getSelectionModel().getSelectedItem();
            dialog.close();
        });

        cancelButton.setOnAction(e -> {
            selectedPlayer = null;
            dialog.close();
        });

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            okButton.setDisable(newSel == null);
        });

        VBox layout = new VBox(10, label, listView, okButton, cancelButton);
        layout.setPrefSize(300, 400);
        layout.setStyle("-fx-padding: 10; -fx-alignment: center;");

        Scene scene = new Scene(layout);
        dialog.setScene(scene);
        dialog.showAndWait();

        return Optional.ofNullable(selectedPlayer);
    }
}
