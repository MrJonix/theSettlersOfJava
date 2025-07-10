package de.dhbw_ravensburg.theSettlersOfJava.graphics.view;

import com.almasb.fxgl.dsl.FXGL;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
public class TradeView {

    public VBox createTradeUI(Runnable onClose) {
        Player currentPlayer = App.getGameController().getCurrentPlayer();

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setStyle("""
            -fx-background-color: rgba(230,230,230,0.95);
            -fx-border-color: black;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            -fx-padding: 15;
        """);

        Text title = FXGL.getUIFactoryService().newText("Handel", Color.BLACK, 24);

        // --- Auswahl: Spielerhandel oder Seehandel
        ToggleGroup tradeModeGroup = new ToggleGroup();
        RadioButton playerTradeBtn = new RadioButton("Spielerhandel");
        RadioButton seaTradeBtn = new RadioButton("Seehandel");
        playerTradeBtn.setToggleGroup(tradeModeGroup);
        seaTradeBtn.setToggleGroup(tradeModeGroup);
        playerTradeBtn.setSelected(true); // Default

        HBox tradeModeBox = new HBox(10, playerTradeBtn, seaTradeBtn);

        // --- Spielerwahl
        ComboBox<Player> playerSelector = new ComboBox<>();
        playerSelector.getItems().addAll(App.getGameController().getPlayers().stream()
            .filter(p -> !p.equals(currentPlayer))
            .toList());

        // --- Hafenwahl
        ComboBox<String> harborSelector = new ComboBox<>();
        harborSelector.getItems().addAll(
            "Bank (4:1)", "3:1 Hafen", "2:1 Holz", "2:1 Ziegel", "2:1 Erz" // Beispiel
        );
        harborSelector.setDisable(true);

        // Reaktion auf Auswahländerung
        tradeModeGroup.selectedToggleProperty().addListener((obs, old, selected) -> {
            boolean isPlayerTrade = playerTradeBtn.isSelected();
            playerSelector.setDisable(!isPlayerTrade);
            harborSelector.setDisable(isPlayerTrade);
        });

        // --- Ressourcenangabe
        ComboBox<ResourceType> offerType = new ComboBox<>();
        offerType.getItems().addAll(ResourceType.values());
        offerType.setValue(ResourceType.WOOD);

        TextField offerAmount = new TextField();
        offerAmount.setPromptText("Anbieten");

        ComboBox<ResourceType> requestType = new ComboBox<>();
        requestType.getItems().addAll(ResourceType.values());
        requestType.setValue(ResourceType.BRICK);

        TextField requestAmount = new TextField();
        requestAmount.setPromptText("Anfragen");

        // --- Buttons
        Button tradeButton = new Button("Handel ausführen");
        tradeButton.setOnAction(e -> {
            if (playerTradeBtn.isSelected()) {
                Player target = playerSelector.getValue();
                if (target == null) {
                    FXGL.getDialogService().showMessageBox("Bitte einen Spieler auswählen.");
                    return;
                }
                handlePlayerTrade(currentPlayer, target, offerType.getValue(), parseIntOrZero(offerAmount.getText()), requestType.getValue(), parseIntOrZero(requestAmount.getText()));
            } else {
                String harbor = harborSelector.getValue();
                if (harbor == null) {
                    FXGL.getDialogService().showMessageBox("Bitte einen Hafen auswählen.");
                    return;
                }
                handleSeaTrade(currentPlayer, harbor, offerType.getValue(), parseIntOrZero(offerAmount.getText()), requestType.getValue(), parseIntOrZero(requestAmount.getText()));
            }
            onClose.run();
        });

        Button cancelButton = new Button("Abbrechen");
        cancelButton.setOnAction(e -> onClose.run());

        layout.getChildren().addAll(
            title,
            new Label("Handelsart:"), tradeModeBox,
            new Label("Spieler auswählen:"), playerSelector,
            new Label("Hafen auswählen:"), harborSelector,
            new Label("Anbieten:"), offerType, offerAmount,
            new Label("Anfragen:"), requestType, requestAmount,
            new HBox(10, tradeButton, cancelButton)
        );

        return layout;
    }

    private int parseIntOrZero(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void handlePlayerTrade(Player from, Player to, ResourceType offered, int offerAmt, ResourceType requested, int requestAmt) {
        if (from.hasResources(offered, offerAmt)) {
            // TODO: Logik für Spieler-zu-Spieler-Handel mit Zustimmung etc.
            from.removeResources(offered, offerAmt);
            to.addResources(offered, offerAmt);
            to.removeResources(requested, requestAmt);
            from.addResources(requested, requestAmt);
            FXGL.getDialogService().showMessageBox("Handel abgeschlossen mit " + to.getName());
        } else {
            FXGL.getDialogService().showMessageBox("Nicht genug Ressourcen.");
        }
    }

    private void handleSeaTrade(Player player, String harbor, ResourceType offered, int offerAmt, ResourceType requested, int requestAmt) {
        // TODO: Logik je nach Hafen (z.B. 4:1, 3:1, 2:1)
        if (player.hasResources(offered, offerAmt)) {
            player.removeResources(offered, offerAmt);
            player.addResources(requested, requestAmt);
            FXGL.getDialogService().showMessageBox("Seehandel erfolgreich: " + harbor);
        } else {
            FXGL.getDialogService().showMessageBox("Nicht genug Ressourcen.");
        }
    }
}
