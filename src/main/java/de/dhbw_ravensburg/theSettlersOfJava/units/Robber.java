package de.dhbw_ravensburg.theSettlersOfJava.units;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import de.dhbw_ravensburg.theSettlersOfJava.map.Hex;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Robber {
	private Hex location;
	private Entity robberEntity;
	
	public Robber(Hex location) {
		this.location = location;
	}

	public Hex getLocation() {
		return location;
	}

	public void moveRobber(Hex newLocation) {
	    this.location = newLocation;

	    if (robberEntity != null) {
	        robberEntity.setPosition(newLocation.getPosition().getWorldPosition());
	    }
	}

	
	public void stealRandomResourceFromPlayer(Player thief, Player victim) {
	    Map<ResourceType, Integer> victimResources = victim.getResources();
	    List<ResourceType> availableResources = new ArrayList<>();

	    // Sammle alle Ressourcen, die der Opfer-Spieler besitzt
	    for (Map.Entry<ResourceType, Integer> entry : victimResources.entrySet()) {
	        ResourceType type = entry.getKey();
	        int amount = entry.getValue();
	        for (int i = 0; i < amount; i++) {
	            availableResources.add(type);
	        }
	    }

	    if (availableResources.isEmpty()) {
	        // Opfer hat keine Ressourcen, nichts zu stehlen
	        return;
	    }

	    // Zufällige Ressource auswählen
	    ResourceType stolenResource = availableResources.get(new Random().nextInt(availableResources.size()));

	    // Ressource beim Opfer abziehen
	    victimResources.put(stolenResource, victimResources.get(stolenResource) - 1);

	    // Ressource dem Dieb hinzufügen
	    Map<ResourceType, Integer> thiefResources = thief.getResources();
	    thiefResources.put(stolenResource, thiefResources.getOrDefault(stolenResource, 0) + 1);

	    // Benachrichtigung ausgeben (FXGL NotificationService)
	    FXGL.getNotificationService().pushNotification(
	        thief.getName() + " hat eine " + stolenResource.name() + " von " + victim.getName() + " gestohlen!");
	}


	
	public void visualize() {
	    robberEntity = FXGL.entityBuilder()
	            .at(location.getPosition().getX(), location.getPosition().getY())
	            .viewWithBBox(new Circle(15, Color.BLACK))
	            .with("robber", this)
	            .buildAndAttach();
	}


	
}
