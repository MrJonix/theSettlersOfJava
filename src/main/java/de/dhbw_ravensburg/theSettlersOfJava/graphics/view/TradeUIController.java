package de.dhbw_ravensburg.theSettlersOfJava.graphics.view;

import com.almasb.fxgl.dsl.FXGL;
import de.dhbw_ravensburg.theSettlersOfJava.game.TradeOffer;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TradeUIController {

    private Supplier<Player> currentPlayerSupplier;
    private List<Player> allPlayers;
    private Button tradeButton;

    public TradeUIController(Supplier<Player> currentPlayerSupplier, List<Player> allPlayers) {
        this.currentPlayerSupplier = currentPlayerSupplier;
        this.allPlayers = allPlayers;
        FXGL.set("tradeUI", this);
    }
    private Player getCurrentPlayer() {
    	return currentPlayerSupplier.get();
    }

    public void initTradeButton() {
        ImageView icon = new ImageView(new Image("assets/textures/icons/icon_TRADE.png"));
        icon.setFitWidth(45);
        icon.setFitHeight(45);

        tradeButton = new Button();
        tradeButton.setGraphic(icon);
        tradeButton.setMaxSize(60, 60);
        tradeButton.setStyle("""
        	    -fx-background-color: white;
        	    -fx-border-color: black;
        	    -fx-border-width: 2px;
        	    -fx-background-radius: 10;
        	    -fx-border-radius: 10;
        	""");        
        tradeButton.setVisible(false);
        tradeButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            tradeButton.setScaleX(1.1);
            tradeButton.setScaleY(1.1);
        });
        tradeButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            tradeButton.setScaleX(1.0);
            tradeButton.setScaleY(1.0);
        });

        tradeButton.setOnAction(e -> openTradeDialog());
        tradeButton.setTranslateX((FXGL.getAppWidth() - 80));
        tradeButton.setTranslateY(100);

        FXGL.getGameScene().addUINode(tradeButton);
    }

    public void showTradeButton() {
    	if (tradeButton != null) {
    		tradeButton.setVisible(true);
    	}
    }
    private void openTradeDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Handel konfigurieren");

        VBox content = new VBox(10);

        Map<ResourceType, Spinner<Integer>> offeredMap = new EnumMap<>(ResourceType.class);
        Map<ResourceType, Spinner<Integer>> requestedMap = new EnumMap<>(ResourceType.class);

        content.getChildren().add(new Label("Du bietest:"));
        for (ResourceType type : ResourceType.values()) {
            Spinner<Integer> spinner = new Spinner<>(0, 10, 0);
            offeredMap.put(type, spinner);
            content.getChildren().add(new Label(type.name()));
            content.getChildren().add(spinner);
        }

        content.getChildren().add(new Label("Du möchtest:"));
        for (ResourceType type : ResourceType.values()) {
            Spinner<Integer> spinner = new Spinner<>(0, 10, 0);
            requestedMap.put(type, spinner);
            content.getChildren().add(new Label(type.name()));
            content.getChildren().add(spinner);
        }

        dialog.getDialogPane().setContent(content);
        ButtonType nextButton = new ButtonType("Weiter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(nextButton, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == nextButton) {
                Map<ResourceType, Integer> offered = new EnumMap<>(ResourceType.class);
                Map<ResourceType, Integer> requested = new EnumMap<>(ResourceType.class);

                boolean hasOffered = false;
                boolean hasRequested = false;

                for (Map.Entry<ResourceType, Spinner<Integer>> entry : offeredMap.entrySet()) {
                    int amount = entry.getValue().getValue();
                    if (amount > 0) {
                        offered.put(entry.getKey(), amount);
                        hasOffered = true;
                    }
                }
                for (Map.Entry<ResourceType, Spinner<Integer>> entry : requestedMap.entrySet()) {
                    int amount = entry.getValue().getValue();
                    if (amount > 0) {
                        requested.put(entry.getKey(), amount);
                        hasRequested = true;
                    }
                }

                if (!hasOffered || !hasRequested) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ungültiger Handel");
                    alert.setHeaderText(null);
                    alert.setContentText("Bitte gib sowohl angebotene als auch gewünschte Ressourcen an.");
                    alert.showAndWait();
                    return null;
                }

                openPlayerSelectDialog(offered, requested);
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void openPlayerSelectDialog(Map<ResourceType, Integer> offered, Map<ResourceType, Integer> requested) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Mitspieler auswählen");

        VBox content = new VBox(10);
        ToggleGroup group = new ToggleGroup();

        for (Player p : allPlayers) {
            if (!p.equals(getCurrentPlayer())) {
                RadioButton btn = new RadioButton(p.getName());
                btn.setUserData(p);
                btn.setToggleGroup(group);
                content.getChildren().add(btn);
            }
        }

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK && group.getSelectedToggle() != null) {
                Player selected = (Player) group.getSelectedToggle().getUserData();
                TradeOffer offer = new TradeOffer(getCurrentPlayer(), selected, offered, requested);
                selected.receiveTradeOffer(offer);
            }
            return null;
        });

        dialog.showAndWait();
    }
    //fromPlayer Spieler der Handelsangebot bekommt / toPlayer Spieler Empfänger des Gegenangebots
    public void openTradeDialogWithTarget(Player fromPlayer, Player toPlayer) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Gegenangebot erstellen");

        VBox content = new VBox(10);

        Map<ResourceType, Spinner<Integer>> offeredMap = new EnumMap<>(ResourceType.class);
        Map<ResourceType, Spinner<Integer>> requestedMap = new EnumMap<>(ResourceType.class);

        content.getChildren().add(new Label("Du bietest:"));
        for (ResourceType type : ResourceType.values()) {
            Spinner<Integer> spinner = new Spinner<>(0, 10, 0);
            offeredMap.put(type, spinner);
            content.getChildren().add(new Label(type.name()));
            content.getChildren().add(spinner);
        }

        content.getChildren().add(new Label("Du möchtest:"));
        for (ResourceType type : ResourceType.values()) {
            Spinner<Integer> spinner = new Spinner<>(0, 10, 0);
            requestedMap.put(type, spinner);
            content.getChildren().add(new Label(type.name()));
            content.getChildren().add(spinner);
        }

        dialog.getDialogPane().setContent(content);
        ButtonType sendButton = new ButtonType("Senden", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sendButton, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == sendButton) {
                Map<ResourceType, Integer> offered = new EnumMap<>(ResourceType.class);
                Map<ResourceType, Integer> requested = new EnumMap<>(ResourceType.class);

                boolean hasOffered = false;
                boolean hasRequested = false;

                for (Map.Entry<ResourceType, Spinner<Integer>> entry : offeredMap.entrySet()) {
                    int amount = entry.getValue().getValue();
                    if (amount > 0) {
                        offered.put(entry.getKey(), amount);
                        hasOffered = true;
                    }
                }
                for (Map.Entry<ResourceType, Spinner<Integer>> entry : requestedMap.entrySet()) {
                    int amount = entry.getValue().getValue();
                    if (amount > 0) {
                        requested.put(entry.getKey(), amount);
                        hasRequested = true;
                    }
                }

                if (!hasOffered || !hasRequested) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ungültiges Gegenangebot");
                    alert.setHeaderText(null);
                    alert.setContentText("Bitte gib sowohl angebotene als auch gewünschte Ressourcen an.");
                    alert.showAndWait();
                    return null;
                }

                TradeOffer counterOffer = new TradeOffer(fromPlayer, toPlayer, offered, requested);
                toPlayer.receiveTradeOffer(counterOffer);
            }
            return null;
        });

        dialog.showAndWait();
    }
}