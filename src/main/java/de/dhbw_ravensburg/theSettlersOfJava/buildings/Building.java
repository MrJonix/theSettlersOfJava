package de.dhbw_ravensburg.theSettlersOfJava.buildings;

import java.util.Map;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;

import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;

public abstract class Building {
	
	private final HexCorner location;
	protected final Player owner;
	protected Entity entity;
	protected Map<ResourceType,Integer> buildingCost;
	
	public Building (HexCorner location, Player owner, Map<ResourceType,Integer> buildingCost) {
		this.location = location;
		this.owner = owner;
		this.buildingCost = buildingCost;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public Map<ResourceType,Integer> getBuildingCost() {
		return buildingCost;
	}
	
	public abstract int getVictoryPoints();
	
	public abstract String getImagePath();
	
	public void visualize() {
		    Texture texture = FXGL.getAssetLoader().loadTexture(getImagePath());

		    // Optional skalieren
		    texture.setScaleX(0.4);
		    texture.setScaleY(0.4);

		    // Platziere das Bild zentriert auf der Ecke
		    double x = location.getX() - texture.getWidth() / 2;
		    double y = location.getY() - texture.getHeight() / 2;

		    // Entity erstellen und anhängen
		    entity = FXGL.entityBuilder()
		        .at(x, y)
		        .view(texture)
		        .buildAndAttach();
	}
	public Entity getEntity() {
		return entity;
	}

	public HexCorner getLocation() {
		return location;
	}
}
