package de.dhbw_ravensburg.theSettlersOfJava.buildings;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexEdge;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexEdgeOrientation;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a road built between hex edges by a player.
 */
public class Road {

    private static final Map<HexEdgeOrientation, String> IMAGE_PATHS = createImagePaths();
    private static final Map<ResourceType, Integer> ROAD_COST = createRoadCost();

    private final HexEdge location;
    private final Player owner;

    /**
     * Constructs a Road at a specific hex edge owned by a player.
     *
     * @param location the hex edge location of the road
     * @param owner    the player who owns the road
     */
    public Road(HexEdge location, Player owner) {
        this.location = location;
        this.owner = owner;
    }

    /**
     * Gets the cost of building a road.
     *
     * @return a map representing the road cost
     */
    public Map<ResourceType, Integer> getRoadCost() {
        return ROAD_COST;
    }

    /**
     * Visualizes the road on the game board.
     */
    public void visualize() {
        HexCorner[] hexCorners = location.getCorners();
        double x1 = hexCorners[0].getX();
        double y1 = hexCorners[0].getY();
        double x2 = hexCorners[1].getX();
        double y2 = hexCorners[1].getY();

        double centerX = (x1 + x2) / 2;
        double centerY = (y1 + y2) / 2;
        
        // Load and rotate the texture
        String imagePath = owner.getPlayerColor() + "_" + IMAGE_PATHS.get(location.getHexEdgeOrientation());
        Texture texture = FXGL.getAssetLoader().loadTexture(imagePath);
        texture.setScaleX(0.4);
        texture.setScaleY(0.4);

        // Create and attach entity with texture
        FXGL.entityBuilder()
            .at(centerX - texture.getWidth() / 2, centerY - texture.getHeight() / 2)
            .view(texture)
            .buildAndAttach();
    }

    public HexEdge getLocation() {
        return location;
    }

    public Player getOwner() {
        return owner;
    }

    /**
     * Initializes the image paths map.
     *
     * @return a map for image paths based on edge orientation
     */
    private static Map<HexEdgeOrientation, String> createImagePaths() {
        Map<HexEdgeOrientation, String> paths = new EnumMap<>(HexEdgeOrientation.class);
        paths.put(HexEdgeOrientation.LEFT_TO_RIGHT, "road_lr.png");
        paths.put(HexEdgeOrientation.STRAIGHT, "road_straight.png");
        paths.put(HexEdgeOrientation.RIGHT_TO_LEFT, "road_rl.png");
        return paths;
    }


    /**
     * Initializes the road cost map.
     *
     * @return an unmodifiable map representing the road cost
     */
    private static Map<ResourceType, Integer> createRoadCost() {
        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.WOOD, 1);
        cost.put(ResourceType.BRICK, 1);
        return Collections.unmodifiableMap(cost);
    }
}