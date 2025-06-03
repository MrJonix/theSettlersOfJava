package de.dhbw_ravensburg.theSettlersOfJava.graphics;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.function.Consumer;

public class BuildingConfirmation {

    /**
     * Zeigt eine eingebettete Bestätigung im gegebenen Root-Node.
     *
     * @param root     Der Root-Knoten (z. B. StackPane deiner Spielszene)
     * @param onResult Callback mit true (OK) oder false (Cancel)
     */
    public static void showConfirmationOverlay(StackPane root, Consumer<Boolean> onResult) {

        // === Overlay mit halbtransparentem Hintergrund ===
        VBox overlay = new VBox();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-padding: 20px;");
        overlay.setAlignment(Pos.CENTER);

        // Overlay nimmt gesamte Fenstergröße ein
        overlay.prefWidthProperty().bind(root.widthProperty());
        overlay.prefHeightProperty().bind(root.heightProperty());

        // === Inhalt des Dialogs ===
        VBox dialog = new VBox(10);
        dialog.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        dialog.setAlignment(Pos.CENTER);

        Label label = new Label("Are you sure you want to build here?");
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);

        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        buttons.getChildren().addAll(ok, cancel);

        dialog.getChildren().addAll(label, buttons);
        overlay.getChildren().add(dialog);

        // === Buttons reagieren ===
        ok.setOnAction(e -> {
            root.getChildren().remove(overlay);
            onResult.accept(true);
        });

        cancel.setOnAction(e -> {
            root.getChildren().remove(overlay);
            onResult.accept(false);
        });

        // === Overlay anzeigen ===
        if (!root.getChildren().contains(overlay)) {
            root.getChildren().add(overlay);
        }
    }
}
