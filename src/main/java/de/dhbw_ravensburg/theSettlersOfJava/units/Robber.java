package de.dhbw_ravensburg.theSettlersOfJava.units;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;

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
	    if (stolenResource == null) return;
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
	    if (robberEntity != null) {
	        robberEntity.removeFromWorld(); // Alte Instanz entfernen
	    }

	    double hexCenterX = location.getPosition().getX();
	    double hexCenterY = location.getPosition().getY();

	    Texture robberTexture = FXGL.getAssetLoader().loadTexture("Robber.png");
	    robberTexture.setFitWidth(Hex.HEX_SIZE);
	    robberTexture.setFitHeight(Hex.HEX_SIZE);

	    // Manuell zentrieren: links oben minus halbe Breite/Höhe
	    double offsetX = hexCenterX - robberTexture.getFitWidth() / 2;
	    double offsetY = hexCenterY - robberTexture.getFitHeight() / 2;

	    robberEntity = FXGL.entityBuilder()
	            .at(offsetX, offsetY)
	            .viewWithBBox(robberTexture)
	            .zIndex(10)
	            .with("robber", this)
	            .buildAndAttach();


	}



	
}
