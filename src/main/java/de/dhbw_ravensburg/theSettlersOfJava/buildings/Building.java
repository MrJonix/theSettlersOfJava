package de.dhbw_ravensburg.theSettlersOfJava.buildings;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a building in the game, with a specific location, owner, and building cost.
 */
public abstract class Building {

    protected final HexCorner location;
    protected final Player owner;
    protected final Map<ResourceType, Integer> buildingCost;
    protected Entity entity;

    /**
     * Constructs a Building with a specified location, owner, and building cost.
     *
     * @param location the location of the building
     * @param owner the owner of the building
     * @param buildingCost the cost for constructing the building
     */
    public Building(HexCorner location, Player owner, Map<ResourceType, Integer> buildingCost) {
        this.location = Objects.requireNonNull(location, "Location cannot be null");
        this.owner = Objects.requireNonNull(owner, "Owner cannot be null");
        this.buildingCost = Objects.requireNonNull(buildingCost, "Building cost cannot be null");
    }

    /**
     * Gets the owner of the building.
     *
     * @return the player owning the building.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Gets the building cost.
     *
     * @return a map representing the building cost.
     */
    public Map<ResourceType, Integer> getBuildingCost() {
        return buildingCost;
    }

    /**
     * Gets the victory points provided by the building.
     *
     * @return the number of victory points.
     */
    public abstract int getVictoryPoints();

    /**
     * Gets the image path for the building's texture.
     *
     * @return the image path as a string.
     */
    public abstract String getImagePath();

    /**
     * Visualizes the building at its location in the game world.
     */
    public void visualize() {
        Texture texture = FXGL.getAssetLoader().loadTexture(getImagePath());

        // Scale the texture
        texture.setScaleX(0.4);
        texture.setScaleY(0.4);

        // Calculate the position to center the texture on the location
        double x = location.getX() - texture.getWidth() / 2;
        double y = location.getY() - texture.getHeight() / 2;

        // Create and attach entity
        entity = FXGL.entityBuilder()
                .at(x, y)
                .view(texture)
                .buildAndAttach();
    }

    /**
     * Gets the entity representing the building in the game world.
     *
     * @return the entity.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Gets the location of the building.
     *
     * @return the hex corner location.
     */
    public HexCorner getLocation() {
        return location;
    }
}