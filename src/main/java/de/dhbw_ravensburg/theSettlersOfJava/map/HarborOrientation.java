package de.dhbw_ravensburg.theSettlersOfJava.map;

/**
 * Enum representing the orientation of a harbor relative to a hex tile.
 *
 * Used to determine on which edge of a hexagon the harbor is positioned,
 * affecting visual placement and trade logic.
 */
public enum HarborOrientation {
	  /** Harbor is located at the top-left edge of the hex. */
    TOP_LEFT("/harbour/TOP_LEFT.png"),
    /** Harbor is located at the top-right edge of the hex. */
    TOP_RIGHT("/harbour/TOP_RIGHT.png"),
    /** Harbor is located at the middle-left edge of the hex. */
    MIDDLE_LEFT("/harbour/MIDDLE_LEFT.png"),
    /** Harbor is located at the middle-right edge of the hex. */
    MIDDLE_RIGHT("/harbour/MIDDLE_RIGHT.png"),
    /** Harbor is located at the bottom-left edge of the hex. */
    BOTTOM_LEFT("/harbour/BOTTOM_LEFT.png"),
    /** Harbor is located at the bottom-right edge of the hex. */
    BOTTOM_RIGHT("/harbour/BOTTOM_RIGHT.png");

    private final String imagePath;

    HarborOrientation(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }
}
