package de.dhbw_ravensburg.theSettlersOfJava.units;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import com.almasb.fxgl.dsl.FXGL;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.Building;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.Road;
import de.dhbw_ravensburg.theSettlersOfJava.game.GameState;
import de.dhbw_ravensburg.theSettlersOfJava.game.TradeOffer;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.view.TradeUIController;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.ButtonBar;


public class Player {

    private final StringProperty name = new SimpleStringProperty();
    private final PlayerColor color;
    private final IntegerProperty victoryPoints = new SimpleIntegerProperty(0);
    private final IntegerProperty longestRoad = new SimpleIntegerProperty(0);
    
    private final MapProperty<ResourceType, Integer> resources =
        new SimpleMapProperty<>(FXCollections.observableHashMap());

    private final IntegerBinding resourceSize = new IntegerBinding() {
        {
            super.bind(resources);
            resources.addListener((MapChangeListener.Change<? extends ResourceType, ? extends Integer> change) -> {
                this.invalidate();
            });
        }

        @Override
        protected int computeValue() {
            int sum = 0;
            for (int val : resources.values()) {
                sum += val;
            }
            return sum;
        }
    };

    // Konstruktor
    public Player(String name, PlayerColor color) {
        this.name.set(name);
        this.color = color;
        
        for (ResourceType type : ResourceType.values()) {
            resources.put(type, 0);
        }
    }
    
    public void build(Building b, Consumer<Boolean> onBuilt) {
    	if(!App.getGameController().getCurrentGameState().equals(GameState.ACTION_PHASE) &&
    			!App.getGameController().getCurrentGameState().equals(GameState.SETUP_PHASE)) return;
        FXGL.getDialogService().showConfirmationBox("Möchtest du hier ein Gebäude bauen?", result -> {
            if (!result) {
                onBuilt.accept(false);
                return;
            }
            
            Map<ResourceType, Integer> cost = b.getBuildingCost();
            boolean setupPhase = App.getGameController().getCurrentGameState().equals(GameState.SETUP_PHASE);

            if (!setupPhase && !hasResources(cost)) {
                FXGL.getNotificationService().pushNotification("Nicht genügend Ressourcen.");
                onBuilt.accept(false);
                return;
            }
            

            if (App.getGameController().getGameBoard().buildBuilding(b)) {
                victoryPoints.set(victoryPoints.get() + 1);

                if (!setupPhase) {
                    cost.forEach(this::removeResources);
                }

                onBuilt.accept(true);
            } else {
                onBuilt.accept(false);
            }
        });
        App.getGameController().trade();
    }

    public void build(Road r, Consumer<Boolean> onBuilt) {
    	if(!App.getGameController().getCurrentGameState().equals(GameState.ACTION_PHASE) &&
    			!App.getGameController().getCurrentGameState().equals(GameState.SETUP_PHASE)) return;
        FXGL.getDialogService().showConfirmationBox("Möchtest du hier eine Straße bauen?", answer -> {
            if (!answer) {
                onBuilt.accept(false);
                return;
            }

            Map<ResourceType, Integer> cost = r.getRoadCost();
            boolean setupPhase = App.getGameController().getCurrentGameState().equals(GameState.SETUP_PHASE);

            if (!hasResources(cost) && !setupPhase) {
                FXGL.getNotificationService().pushNotification("Nicht genügend Ressourcen.");
                onBuilt.accept(false);
                return;
            }

            if (App.getGameController().getGameBoard().buildRoad(r)) {
                if (!setupPhase) {
                    for (Map.Entry<ResourceType, Integer> entry : cost.entrySet()) {
                        removeResources(entry.getKey(), entry.getValue());
                    }
                }
                onBuilt.accept(true);
            } else {
                onBuilt.accept(false);
            }
        });
    }


    public boolean hasResources(Map<ResourceType,Integer> cost) {
        for (Map.Entry<ResourceType, Integer> entry : cost.entrySet()) {
            ResourceType type = entry.getKey();
            int requiredAmount = entry.getValue();
            int available = resources.getOrDefault(type, 0);
            if (available < requiredAmount) {
                return false; // Not enough resources
            }
        }
        return true;
    }

    // Name Property
    public StringProperty nameProperty() { return name; }
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public Color getColor() { return color.getColor(); }
    public PlayerColor getPlayerColor() {
    	return color;
    }
    // Longest Road
    public IntegerProperty longestRoadProperty() { return longestRoad; }
    
    // VictoryPoints Property
    public IntegerProperty victoryPointsProperty() { return victoryPoints; }
    public int getVictoryPoints() { return victoryPoints.get(); }
    public void setVictoryPoints(int vp) { victoryPoints.set(vp); }

    // Resources MapProperty
    public MapProperty<ResourceType, Integer> resourcesProperty() { return resources; }
    public ObservableMap<ResourceType, Integer> getResources() { return resources.get(); }
    public void setResources(ObservableMap<ResourceType, Integer> map) { resources.set(map); }

    // ResourceSize Binding
    public IntegerBinding resourceSizeProperty() { return resourceSize; }
    public int getResourceSize() { return resourceSize.get(); }

    // Methoden zum Ressourcen ändern
    public void addResources(ResourceType type, int amount) {
        int current = resources.getOrDefault(type, 0);
        resources.put(type, current + amount);
    }

    public boolean removeResources(ResourceType type, int amount) {
        int current = resources.getOrDefault(type, 0);
        if (current < amount) return false;
        resources.put(type, current - amount);
        return true;
    }
    //Methode um direkt mit Map ressourcen zu entfernen
    public void removeResourcesMap (Map<ResourceType, Integer> resources) {
    	for(Map.Entry<ResourceType, Integer> entry: resources.entrySet()) {
    	removeResources(entry.getKey(), entry.getValue());	
    	}
    }
    //Methode um direkt mit Map ressourcen zu adden
    public void addResourcesMap(Map<ResourceType, Integer> resources) {
        for (Map.Entry<ResourceType, Integer> entry : resources.entrySet()) {
            addResources(entry.getKey(), entry.getValue());
        }
    }
    public void receiveTradeOffer(TradeOffer offer) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Handelsangebot von " + offer.getOfferer().getName());

        VBox content = new VBox(10);
        content.getChildren().add(new Label("Angebotene Ressourcen:"));
        for (Map.Entry<ResourceType, Integer> entry : offer.getOfferedResources().entrySet()) {
            content.getChildren().add(new Label(entry.getKey() + ": " + entry.getValue()));
        }

        content.getChildren().add(new Label("Angeforderte Ressourcen:"));
        for (Map.Entry<ResourceType, Integer> entry : offer.getRequestedResources().entrySet()) {
            content.getChildren().add(new Label(entry.getKey() + ": " + entry.getValue()));
        }

        dialog.getDialogPane().setContent(content);
        ButtonType acceptButton = new ButtonType("Annehmen", ButtonBar.ButtonData.YES);
        ButtonType counterButton = new ButtonType("Gegenangebot", ButtonBar.ButtonData.OTHER);
        ButtonType declineButton = new ButtonType("Ablehnen", ButtonBar.ButtonData.NO);

        dialog.getDialogPane().getButtonTypes().addAll(acceptButton, counterButton, declineButton);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (result.get() == acceptButton) {
                if (!this.hasResources(offer.getRequestedResources()) ||
                    !offer.getOfferer().hasResources(offer.getOfferedResources())) {

                    FXGL.showMessage("Mindestens einer der Spieler hat nicht genügend Ressourcen.");
                    return;
                }

                this.removeResourcesMap(offer.getRequestedResources());
                this.addResourcesMap(offer.getOfferedResources());

                offer.getOfferer().removeResourcesMap(offer.getOfferedResources());
                offer.getOfferer().addResourcesMap(offer.getRequestedResources());

                FXGL.showMessage("Handel erfolgreich durchgeführt.");

            } else if (result.get() == counterButton) {
                TradeUIController tradeUI = FXGL.geto("tradeUI");
                tradeUI.openTradeDialogWithTarget(this, offer.getOfferer());

            } else if (result.get() == declineButton) {
                FXGL.showMessage("Du hast das Handelsangebot abgelehnt.");
            }
        }
    }

    @Override
    public String toString() {
    	return getName();
    }

    public boolean hasResources(ResourceType offeredResource, int offeredAmount) {
        return resources.containsKey(offeredResource) && resources.get(offeredResource) >= offeredAmount;
    }
}
