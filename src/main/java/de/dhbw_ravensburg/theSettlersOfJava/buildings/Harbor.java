package de.dhbw_ravensburg.theSettlersOfJava.buildings;import de.dhbw_ravensburg.theSettlersOfJava.map.HexEdge;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;

/**
 * Represents a harbor located on a specific hex edge, providing trade benefits for a specific resource type.
 */
public class Harbor {

    private final HexEdge location;
    private final ResourceType resourceType;

    /**
     * Constructs a Harbor at a specific location with a specified trade resource type.
     *
     * @param location    the hex edge location of the harbor
     * @param resourceType the resource type associated with the harbor
     */
    public Harbor(HexEdge location, ResourceType resourceType) {
        this.location = location;
        this.resourceType = resourceType;
    }

    /**
     * Visualizes the harbor on the game board.
     * Currently, this method does not perform any operations.
     */
    public void visualize() {
        // Future visualization implementation will go here
        // For example, rendering a harbor icon on the game board
    }

    /**
     * Gets the location of the harbor.
     *
     * @return the hex edge where the harbor is located
     */
    public HexEdge getLocation() {
        return location;
    }

    /**
     * Gets the resource type associated with the harbor for trading.
     *
     * @return the resource type of the harbor
     */
    public ResourceType getResourceType() {
        return resourceType;
    }
}