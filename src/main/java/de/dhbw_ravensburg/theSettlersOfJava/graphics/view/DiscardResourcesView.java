package de.dhbw_ravensburg.theSettlersOfJava.graphics.view;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import javafx.collections.ObservableMap;

/**
 * View-Klasse zur Auswahl von Ressourcen, die abgegeben werden sollen.
 */
public class DiscardResourcesView {

    private final ObservableMap<ResourceType, Integer> playerResources;
    private final String playerName;

    public DiscardResourcesView(String playerName, ObservableMap<ResourceType, Integer> playerResources) {
        this.playerName = playerName;
        this.playerResources = playerResources;
    }


    /**
     * Zeigt das Popup und gibt eine Map mit den abgegebenen Ressourcen zurück.
     * Die originale playerResources Map wird nicht automatisch verändert!
     */
    public Optional<Map<ResourceType, Integer>> showAndWait() {
        Dialog<Map<ResourceType, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Ressourcen abgeben");
        dialog.setHeaderText(playerName + " muss die Hälfte seiner Rohstoffe abgeben!");


        ButtonType confirmButtonType = new ButtonType("Abgeben", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Map<ResourceType, Spinner<Integer>> spinners = new HashMap<>();

        int row = 0;
        for (Map.Entry<ResourceType, Integer> entry : playerResources.entrySet()) {
            ResourceType type = entry.getKey();
            Integer maxAmount = entry.getValue();

            if (type == null) {
                System.err.println("WARNUNG: Ressourcenliste enthält einen null-Key und wird übersprungen.");
                continue;
            }

            if (maxAmount == null || maxAmount <= 0) {
                continue; // Nicht anzeigbare oder leere Ressourcen überspringen
            }

            Label label = new Label(type.name() + " (" + maxAmount + ")");
            Spinner<Integer> spinner = new Spinner<>(0, maxAmount, 0);

            spinners.put(type, spinner);
            grid.add(label, 0, row);
            grid.add(spinner, 1, row);
            row++;
        }

        dialog.getDialogPane().setContent(grid);

        Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);

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
            if (button == confirmButtonType) {
                Map<ResourceType, Integer> discarded = new HashMap<>();
                for (Map.Entry<ResourceType, Spinner<Integer>> entry : spinners.entrySet()) {
                    int amount = entry.getValue().getValue();
                    if (amount > 0) {
                        discarded.put(entry.getKey(), amount);
                    }
                }
                return discarded;
            }
            return null;
        });

        return dialog.showAndWait();
    }
}
