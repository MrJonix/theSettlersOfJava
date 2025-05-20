package de.dhbw_ravensburg.theSettlersOfJava.units;

import java.util.Map;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.Building;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.Road;
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
    
    public boolean build(Building b) {
        Map<ResourceType, Integer> cost = b.getBuildingCost();

        // Check if player has all required resources
        if(!hasResources(cost)) {
        	return false;
        }
        
        if(App.getGameController().getGameBoard().buildBuilding(b)) {
        	victoryPoints.set(victoryPoints.get()+1);
            // Remove resources
            for (Map.Entry<ResourceType, Integer> entry : cost.entrySet()) {
                ResourceType type = entry.getKey();
                int requiredAmount = entry.getValue();
                removeResources(type, requiredAmount);
            }
        }
        return true;
    }
    
    public boolean build(Road r) {
        Map<ResourceType, Integer> cost = r.getRoadCost();

        // Prüfen, ob der Spieler alle benötigten Ressourcen hat
        if (!hasResources(cost)) {
            return false;
        }

        // Straße bauen (z.B. auf dem Spielbrett platzieren)
        if(App.getGameController().getGameBoard().buildRoad(r))
        {
            // Ressourcen entfernen
            for (Map.Entry<ResourceType, Integer> entry : cost.entrySet()) {
                ResourceType type = entry.getKey();
                int requiredAmount = entry.getValue();
                removeResources(type, requiredAmount);
            }
        }
        return true;
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
}
