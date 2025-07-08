package de.dhbw_ravensburg.theSettlersOfJava.units;

import java.util.Map;
import java.util.function.Consumer;

import com.almasb.fxgl.dsl.FXGL;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.Building;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.Road;
import de.dhbw_ravensburg.theSettlersOfJava.game.GameState;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;

import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;

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
            return resources.values().stream().mapToInt(Integer::intValue).sum();
        }
    };

    public Player(String name, PlayerColor color) {
        this.name.set(name);
        this.color = color;
        
        for (ResourceType type : ResourceType.values()) {
            resources.put(type, 0);
        }
    }

    public void build(Building b, Consumer<Boolean> onBuilt) {
        GameState currentState = App.getGameController().getCurrentGameState();
        boolean setupPhase = currentState.equals(GameState.SETUP_PHASE);

        if (!(setupPhase || currentState.equals(GameState.ACTION_PHASE))) return;

        FXGL.getDialogService().showConfirmationBox("Möchtest du hier ein Gebäude bauen?", result -> {
            if (!result) {
                onBuilt.accept(false);
                return;
            }

            Map<ResourceType, Integer> cost = b.getBuildingCost();

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

            // Offer trade after build attempt
            App.getGameController().trade();
        });
    }

    public void build(Road r, Consumer<Boolean> onBuilt) {
        GameState currentState = App.getGameController().getCurrentGameState();
        boolean setupPhase = currentState.equals(GameState.SETUP_PHASE);

        if (!(setupPhase || currentState.equals(GameState.ACTION_PHASE))) return;

        FXGL.getDialogService().showConfirmationBox("Möchtest du hier eine Straße bauen?", answer -> {
            if (!answer) {
                onBuilt.accept(false);
                return;
            }

            Map<ResourceType, Integer> cost = r.getRoadCost();

            if (!setupPhase && !hasResources(cost)) {
                FXGL.getNotificationService().pushNotification("Nicht genügend Ressourcen.");
                onBuilt.accept(false);
                return;
            }

            if (App.getGameController().getGameBoard().buildRoad(r)) {
                if (!setupPhase) {
                    cost.forEach(this::removeResources);
                }
                onBuilt.accept(true);
            } else {
                onBuilt.accept(false);
            }
        });
    }

    public boolean hasResources(Map<ResourceType, Integer> cost) {
        return cost.entrySet().stream()
            .allMatch(entry -> resources.getOrDefault(entry.getKey(), 0) >= entry.getValue());
    }

    public boolean hasResources(ResourceType offeredResource, int offeredAmount) {
        return resources.getOrDefault(offeredResource, 0) >= offeredAmount;
    }

    public StringProperty nameProperty() { return name; }
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public Color getColor() { return color.getColor(); }
    public PlayerColor getPlayerColor() { return color; }

    public IntegerProperty longestRoadProperty() { return longestRoad; }

    public IntegerProperty victoryPointsProperty() { return victoryPoints; }
    public int getVictoryPoints() { return victoryPoints.get(); }
    public void setVictoryPoints(int vp) {
        if (vp >= 10) victory();
        victoryPoints.set(vp);
    }

    public void victory() {
        FXGL.getDialogService().showMessageBox(
            getName() + " hat das Spiel gewonnen! 🎉",
            () -> FXGL.getGameScene().getInput().clearAll()
        );
    }

    public MapProperty<ResourceType, Integer> resourcesProperty() { return resources; }
    public ObservableMap<ResourceType, Integer> getResources() { return resources.get(); }
    public void setResources(ObservableMap<ResourceType, Integer> map) { resources.set(map); }

    public IntegerBinding resourceSizeProperty() { return resourceSize; }
    public int getResourceSize() { return resourceSize.get(); }

    public void addResources(ResourceType type, int amount) {
        resources.put(type, resources.getOrDefault(type, 0) + amount);
    }

    public boolean removeResources(ResourceType type, int amount) {
        int current = resources.getOrDefault(type, 0);
        if (current < amount) return false;
        resources.put(type, current - amount);
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }
}
