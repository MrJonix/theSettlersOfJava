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

/**
 * View component for configuring and executing trades.
 * 
 * Supports both player-to-player and harbor (sea) trading modes.
 * Allows users to select trade types, partners, resources, and quantities.
 */
public class TradeView {

	/**
     * Creates the trade UI for the current player.
     * 
     * @param onClose a {@link Runnable} callback that is executed when the trade dialog is closed
     * @return the constructed VBox layout containing the full trade interface
     */
	public VBox createTradeUI(Runnable onClose) {
        Player currentPlayer = App.getGameController().getCurrentPlayer();

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setStyle(
        	    "-fx-background-color: rgba(230,230,230,0.95);" +
        	    "-fx-border-color: black;" +
        	    "-fx-border-radius: 5;" +
        	    "-fx-background-radius: 5;" +
        	    "-fx-padding: 15;"
        	);

        Text title = FXGL.getUIFactoryService().newText("Handel", Color.BLACK, 24);

        // --- Trade mode selection: trade with player or trade with harbor
        ToggleGroup tradeModeGroup = new ToggleGroup();
        RadioButton playerTradeBtn = new RadioButton("Spielerhandel");
        RadioButton seaTradeBtn = new RadioButton("Seehandel");
        playerTradeBtn.setToggleGroup(tradeModeGroup);
        seaTradeBtn.setToggleGroup(tradeModeGroup);
        playerTradeBtn.setSelected(true); // Default

        HBox tradeModeBox = new HBox(10, playerTradeBtn, seaTradeBtn);

        // --- player selection (players trade)
        ComboBox<Player> playerSelector = new ComboBox<>();
        playerSelector.getItems().addAll(App.getGameController().getPlayers().stream()
            .filter(p -> !p.equals(currentPlayer))
            .toList());

        // --- harbor selection (sea trade)
        ComboBox<String> harborSelector = new ComboBox<>();
        harborSelector.getItems().addAll(
            "Bank (4:1)", "3:1 Hafen", "2:1 Holz", "2:1 Ziegel", "2:1 Erz" // Beispiel
        );
        harborSelector.setDisable(true);

        // switch enabled selector based on trade type
        tradeModeGroup.selectedToggleProperty().addListener((obs, old, selected) -> {
            boolean isPlayerTrade = playerTradeBtn.isSelected();
            playerSelector.setDisable(!isPlayerTrade);
            harborSelector.setDisable(isPlayerTrade);
        });

        // --- enter offering resources and requesting inputs
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

        // --- buttons
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

	/**
	 * Attempts to parse an integer from a string. Returns 0 if parsing fails.
	 *
	 * @param text the input string
	 * @return the parsed integer or 0 on failure
	 */
    private int parseIntOrZero(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Executes a trade between two players if the offering player has enough resources.
     * Currently assumes automatic acceptance by the other player.
     *
     * @param from       the player offering resources
     * @param to         the target player
     * @param offered    the resource type being offered
     * @param offerAmt   the amount of the offered resource
     * @param requested  the resource type being requested
     * @param requestAmt the amount of the requested resource
     */
     private void handlePlayerTrade(Player from, Player to, ResourceType offered, int offerAmt, ResourceType requested, int requestAmt) {
        if (from.hasResources(offered, offerAmt)) {
            from.removeResources(offered, offerAmt);
            to.addResources(offered, offerAmt);
            to.removeResources(requested, requestAmt);
            from.addResources(requested, requestAmt);
            FXGL.getDialogService().showMessageBox("Handel abgeschlossen mit " + to.getName());
        } else {
            FXGL.getDialogService().showMessageBox("Nicht genug Ressourcen.");
        }
    }

     /**
      * Executes a trade between the player and a harbor, using a fixed trade ratio.
      * Currently assumes the player is eligible for the chosen harbor.
      *
      * @param player      the player initiating the sea trade
      * @param harbor      the harbor type (e.g., "4:1", "2:1 Holz")
      * @param offered     the resource being offered
      * @param offerAmt    the amount being offered
      * @param requested   the resource being requested
      * @param requestAmt  the amount being requested
      */
    private void handleSeaTrade(Player player, String harbor, ResourceType offered, int offerAmt, ResourceType requested, int requestAmt) {
        if (player.hasResources(offered, offerAmt)) {
            player.removeResources(offered, offerAmt);
            player.addResources(requested, requestAmt);
            FXGL.getDialogService().showMessageBox("Seehandel erfolgreich: " + harbor);
        } else {
            FXGL.getDialogService().showMessageBox("Nicht genug Ressourcen.");
        }
    }
}
