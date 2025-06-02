package de.dhbw_ravensburg.theSettlersOfJava.graphics.view;

import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DiscardResourcesView {

    private final ObservableMap<ResourceType, Integer> playerResources;
    private final String playerName;
    private final Consumer<Map<ResourceType, Integer>> onDiscard;

    public DiscardResourcesView(String playerName, ObservableMap<ResourceType, Integer> playerResources, Consumer<Map<ResourceType, Integer>> onDiscard) {
        this.playerName = playerName;
        this.playerResources = playerResources;
        this.onDiscard = onDiscard;
    }

    public Node createOverlay() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-background-radius: 10;");

        Label header = new Label(playerName + " muss die Hälfte seiner Rohstoffe abgeben!");
        header.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        Map<ResourceType, Spinner<Integer>> spinners = new HashMap<>();
        int row = 0;

        for (Map.Entry<ResourceType, Integer> entry : playerResources.entrySet()) {
            ResourceType type = entry.getKey();
            Integer maxAmount = entry.getValue();

            if (type == null || maxAmount == null || maxAmount <= 0) continue;

            Label label = new Label(type.name() + " (" + maxAmount + ")");
            label.setStyle("-fx-text-fill: white;");

            Spinner<Integer> spinner = new Spinner<>(0, maxAmount, 0);
            spinner.setEditable(false);

            spinners.put(type, spinner);
            grid.add(label, 0, row);
            grid.add(spinner, 1, row);
            row++;
        }

        Button confirmBtn = new Button("Abgeben");
        confirmBtn.setDisable(true);
        confirmBtn.setOnAction(e -> {
            Map<ResourceType, Integer> result = new HashMap<>();
            spinners.forEach((type, spinner) -> {
                int val = spinner.getValue();
                if (val > 0) {
                    result.put(type, val);
                }
            });

            onDiscard.accept(result);
            ((Pane) root.getParent()).getChildren().remove(root); // Remove overlay
        });

        // Listener zum Aktivieren/Deaktivieren des Buttons
        Runnable updateConfirm = () -> {
            int totalSelected = spinners.values().stream().mapToInt(Spinner::getValue).sum();
            int totalAvailable = playerResources.values().stream().mapToInt(Integer::intValue).sum();
            confirmBtn.setDisable(totalSelected != totalAvailable / 2);
        };

        spinners.values().forEach(spinner -> spinner.valueProperty().addListener((obs, oldVal, newVal) -> updateConfirm.run()));
        updateConfirm.run();

        root.getChildren().addAll(header, grid, confirmBtn);
        StackPane.setAlignment(root, Pos.CENTER);
        return root;
    }
}
