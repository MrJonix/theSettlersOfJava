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

/**
 * Represents the Robber entity in the game.
 * 
 * The Robber can move between hexes, steal resources from other players,
 * and be visually rendered on the game board.
 */
public class Robber {
	private Hex location;
	private Entity robberEntity;
	
	/**
     * Creates a Robber instance at the specified initial hex location.
     *
     * @param location the hex where the robber starts
     */
	public Robber(Hex location) {
		this.location = location;
	}

	/**
     * Returns the current hex where the robber is located.
     *
     * @return the current hex of the robber
     */
	public Hex getLocation() {
		return location;
	}

	/**
     * Moves the robber to a new hex and updates its visual position if already visualized.
     *
     * @param newLocation the new hex location for the robber
     */
	public void moveRobber(Hex newLocation) {
	    this.location = newLocation;

	    if (robberEntity != null) {
	        robberEntity.setPosition(newLocation.getPosition().getWorldPosition());
	    }
	}
	
	/**
     * Steals a random resource from the victim and transfers it to the thief, if possible.
     *
     * @param thief  the player who is stealing
     * @param victim the player being stolen from
     */
	public void stealRandomResourceFromPlayer(Player thief, Player victim) {
	    Map<ResourceType, Integer> victimResources = victim.getResources();
	    List<ResourceType> availableResources = new ArrayList<>();

	    // Collect all resources possessed by the victim
	    for (Map.Entry<ResourceType, Integer> entry : victimResources.entrySet()) {
	        ResourceType type = entry.getKey();
	        int amount = entry.getValue();
	        for (int i = 0; i < amount; i++) {
	            availableResources.add(type);
	        }
	    }

	    if (availableResources.isEmpty()) {
	        // Victim has no resources, nothing to steal
	        return;
	    }

	    // Randomly chosen resource
	    ResourceType stolenResource = availableResources.get(new Random().nextInt(availableResources.size()));
	    if (stolenResource == null) return;
	    // deduct resources of the victim
	    victimResources.put(stolenResource, victimResources.get(stolenResource) - 1);

	    // Add resource to thief
	    Map<ResourceType, Integer> thiefResources = thief.getResources();
	    thiefResources.put(stolenResource, thiefResources.getOrDefault(stolenResource, 0) + 1);

	    // Notify players via FXGL (FXGL NotificationService)
	    FXGL.getNotificationService().pushNotification(
	        thief.getName() + " hat eine " + stolenResource.name() + " von " + victim.getName() + " gestohlen!");
	}

	/**
     * Renders the robber visually on the current hex using a 2D texture.
     * If already visualized, the previous instance is removed first.
     */
	public void visualize() {
	    if (robberEntity != null) {
	        robberEntity.removeFromWorld(); // Alte Instanz entfernen
	    }

	    double hexCenterX = location.getPosition().getX();
	    double hexCenterY = location.getPosition().getY();

	    Texture robberTexture = FXGL.getAssetLoader().loadTexture("Robber.png");
	    robberTexture.setFitWidth(Hex.HEX_SIZE);
	    robberTexture.setFitHeight(Hex.HEX_SIZE);
	    robberTexture.setX( - robberTexture.getFitWidth() / 2);
	    robberTexture.setY( -robberTexture.getFitHeight() / 2);

	    robberEntity = FXGL.entityBuilder()
	            .at(hexCenterX, hexCenterY)
	            .viewWithBBox(robberTexture)
	            .zIndex(10)
	            .with("robber", this)
	            .buildAndAttach();


	}



	
}
