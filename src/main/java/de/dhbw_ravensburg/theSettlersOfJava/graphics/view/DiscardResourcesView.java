package de.dhbw_ravensburg.theSettlersOfJava.graphics.view;

import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;

import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DiscardResourcesView {

    private final ObservableMap<ResourceType, Integer> playerResources;
    private final String playerName;

    public DiscardResourcesView(String playerName, ObservableMap<ResourceType, Integer> playerResources) {
        this.playerName = playerName;
        this.playerResources = playerResources;
    }

    public Optional<Map<ResourceType, Integer>> showAndWait() {
        Dialog<Map<ResourceType, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Ressourcen abgeben");
        dialog.setHeaderText(playerName + " muss die Hälfte seiner Rohstoffe abgeben!");

        ButtonType confirmButtonType = new ButtonType("Abgeben", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(confirmButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Map<ResourceType, Spinner<Integer>> spinners = new HashMap<>();
        int row = 0;

        for (Map.Entry<ResourceType, Integer> entry : playerResources.entrySet()) {
            ResourceType type = entry.getKey();
            Integer maxAmount = entry.getValue();

            if (type == null || maxAmount == null || maxAmount <= 0) continue;

            Label label = new Label(type.name() + " (" + maxAmount + ")");
            Spinner<Integer> spinner = new Spinner<>(0, maxAmount, 0);
            spinner.setEditable(false); // Verhindert falsche Eingaben

            spinners.put(type, spinner);
            grid.add(label, 0, row);
            grid.add(spinner, 1, row);
            row++;
        }

        dialog.getDialogPane().setContent(grid);

        Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true); // Anfangszustand: deaktiviert

        Runnable updateConfirmState = () -> {
            int totalDiscarded = spinners.values().stream().mapToInt(Spinner::getValue).sum();
            int totalResources = playerResources.values().stream()
                    .filter(v -> v != null)
                    .mapToInt(Integer::intValue)
                    .sum();
            confirmButton.setDisable(totalDiscarded != totalResources / 2);
        };

        spinners.values().forEach(spinner ->
                spinner.valueProperty().addListener((obs, oldVal, newVal) -> updateConfirmState.run())
        );

        dialog.setResultConverter(button -> {
            if (button == confirmButtonType && !confirmButton.isDisable()) {
                Map<ResourceType, Integer> result = new HashMap<>();
                for (Map.Entry<ResourceType, Spinner<Integer>> entry : spinners.entrySet()) {
                    int value = entry.getValue().getValue();
                    if (value > 0) {
                        result.put(entry.getKey(), value);
                    }
                }
                return result;
            }
            return null;
        });

        Optional<Map<ResourceType, Integer>> result;
        do {
            result = dialog.showAndWait();
        } while (!result.isPresent()); // zwingt Benutzer zur Auswahl

        return result;
    }
}
