package de.dhbw_ravensburg.theSettlersOfJava.resources;

import java.awt.Color;
import java.util.Random;

/**
 * Enum representing the different resource types available in the game.
 *
 * Each resource type has a display name, a representative color, and
 * an image path for UI display.
 */
public enum ResourceType {
	
    BRICK("Brick", Color.RED, "cards/card_CLAY.png"),
    WOOD("WOOD", Color.GREEN, "cards/card_WOOD.png"),
    WOOL("Wool", Color.WHITE, "cards/card_WOOL.png"),
    WHEAT("Wheat", Color.YELLOW, "cards/card_WHEAT.png"),
    ORE("Ore", Color.GRAY, "cards/card_MOUNTAIN.png");

    private final String displayName;
    private final Color color;
    private final String imagePath;

    /**
     * Constructs a ResourceType with a display name, color, and image path.
     *
     * @param displayName the name used for display in the UI
     * @param color the color associated with this resource
     * @param imagePath the path to the resource card image
     */
    ResourceType(String displayName, Color color, String imagePath) {
        this.displayName = displayName;
        this.color = color;
        this.imagePath = imagePath;
    }

    /**
     * Returns the display name of the resource.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the color associated with this resource type.
     *
     * @return the color for UI rendering
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the image path for this resource card.
     *
     * @return the image path as a string
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Returns a randomly selected ResourceType.
     *
     * @return a random resource from the enum
     */
    public static ResourceType getRandomResource() {
        Random random = new Random();
        ResourceType[] values = ResourceType.values();
        return values[random.nextInt(values.length)];
    }
}
