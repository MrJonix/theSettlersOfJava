package de.dhbw_ravensburg.theSettlersOfJava.buildings;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.scene.paint.Color;

/**
 * Represents a city building in the game.
 * Cities provide additional functionality and victory points for players.
 */
public class City extends Building {

    private static final Map<ResourceType, Integer> CITY_COST = createCityCost();

    /**
     * Constructs a City at a specific location owned by a player.
     *
     * @param location the hex corner location of the city
     * @param owner the player who owns the city
     */
    public City(HexCorner location, Player owner) {
        super(location, owner, CITY_COST);
    }

    /**
     * Gets the victory points provided by the city.
     *
     * @return the number of victory points
     */
    @Override
    public int getVictoryPoints() {
        return 2;
    }

    /**
     * Gets the image path for the city's texture based on the owner's color.
     *
     * @return the image path as a string
     */
    @Override
    public String getImagePath() {
        return owner.getPlayerColor() + "_city.png";
    }

    /**
     * Initializes the city cost map.
     *
     * @return an unmodifiable map representing the city cost
     */
    private static Map<ResourceType, Integer> createCityCost() {
        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.WHEAT, 2);
        cost.put(ResourceType.ORE, 3);
        return Collections.unmodifiableMap(cost);
    }

}