package de.dhbw_ravensburg.theSettlersOfJava.graphics;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class BuildingConfirmation {

    /**
     * Zeigt eine eingebettete Bestätigung im gegebenen Root-Node.
     *
     * @param root        Der Root-Knoten (z. B. StackPane deiner Spielszene)
     * @param onResult    Callback mit true (OK) oder false (Cancel)
     */
    public static void showConfirmationOverlay(Pane root, Consumer<Boolean> onResult) {
        // Halbtransparenter Hintergrund
        VBox overlay = new VBox(10);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-padding: 20px;");
        overlay.setPrefSize(root.getWidth(), root.getHeight());
        overlay.setTranslateX((root.getWidth() - 300) / 2);  // Optional zentrieren
        overlay.setTranslateY((root.getHeight() - 150) / 2);

        // Inhalt
        VBox dialog = new VBox(10);
        dialog.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        Label label = new Label("Are you sure you want to build here?");
        HBox buttons = new HBox(10);
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");

        buttons.getChildren().addAll(ok, cancel);
        dialog.getChildren().addAll(label, buttons);

        overlay.getChildren().add(dialog);

        // Buttons
        ok.setOnAction(e -> {
            root.getChildren().remove(overlay);
            onResult.accept(true);
        });
        cancel.setOnAction(e -> {
            root.getChildren().remove(overlay);
            onResult.accept(false);
        });

        // Als oberstes Element anzeigen
        if (!root.getChildren().contains(overlay)) {
            root.getChildren().add(overlay);
        }
    }
}
