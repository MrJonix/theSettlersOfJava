package de.dhbw_ravensburg.theSettlersOfJava.resources;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Enum representing the different types of hex tiles on the game board.
 * 
 * Each HexType has a display name, optional associated resource type,
 * image path, and a representative color.
 */
public enum HexType {
	
    FOREST("Forest", ResourceType.WOOD, "/images/forest.png", new Color(34, 139, 34)),
    HILL("Hill", ResourceType.WOOL, "/images/hill.png", new Color(160, 82, 45)),
    FIELD("Field", ResourceType.WHEAT , "/images/field.png", new Color(255, 255, 0)),
    MOUNTAIN("Mountain", ResourceType.ORE, "/images/mountain.png", new Color(128, 128, 128)),
    PASTURE("Pasture", ResourceType.BRICK, "/images/pasture.png", new Color(124, 252, 0)),
    DESERT("Desert", null, "/images/desert.png", new Color(237, 201, 175)),
    WATER("Water", null,"/images/water.png", new Color(0, 119, 190));

    private final String displayName;
    private final String imagePath;
    private final Color color;
    private ResourceType resourceType;

    /**
     * Constructs a HexType with all required properties.
     *
     * @param displayName   the name shown in the UI
     * @param resourceType  the type of resource this hex provides (can be null)
     * @param imagePath     the path to the image representing this hex
     * @param color         the representative color used in the UI
     */
    HexType(String displayName, ResourceType resourceType, String imagePath, Color color) {
        this.displayName = displayName;
        this.imagePath = imagePath;
        this.color = color;
        this.resourceType = resourceType;
    }

    /**
     * Returns the display name of the hex type.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the image path of the hex type.
     *
     * @return the image path as a string
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Returns the representative color for this hex type.
     *
     * @return the color used in UI rendering
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Returns the resource type associated with this hex type.
     *
     * @return the resource type, or null if none
     */
    public ResourceType getResourceType() {
    	return resourceType;
    }
    
    /**
     * Generates and returns a shuffled list of hex types for initializing the board.
     * 
     * The distribution follows standard Catan-style logic:
     * - 4x FOREST, HILL, FIELD
     * - 3x MOUNTAIN, PASTURE
     * - 1x DESERT
     *
     * @return a shuffled list of hex types
     */
    public static List<HexType> generateHexTypeList() {
        List<HexType> hexTypeList = new ArrayList<>();

        // Add 4 of each: Forst, Hill, Field
        for (int i = 0; i < 4; i++) {
            hexTypeList.add(FOREST);
            hexTypeList.add(HILL);
            hexTypeList.add(FIELD);
        }

        // Add 3 of each: Mountain, Pasture
        for (int i = 0; i < 3; i++) {
            hexTypeList.add(MOUNTAIN);
            hexTypeList.add(PASTURE);
        }

        // Add 1 Desert
        hexTypeList.add(DESERT);

        // Shuffle to randomize the placement
        Collections.shuffle(hexTypeList);
        return hexTypeList;
    }
}

