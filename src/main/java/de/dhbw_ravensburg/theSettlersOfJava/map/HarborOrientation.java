package de.dhbw_ravensburg.theSettlersOfJava.map;

/**
 * Enum representing the orientation of a harbor relative to a hex tile.
 *
 * Used to determine on which edge of a hexagon the harbor is positioned,
 * affecting visual placement and trade logic.
 */
public enum HarborOrientation {
    /** Harbor is located at the top-left edge of the hex. */
    TOP_LEFT,

    /** Harbor is located at the top-right edge of the hex. */
    TOP_RIGHT,

    /** Harbor is located at the middle-left edge of the hex. */
    MIDDLE_LEFT,

    /** Harbor is located at the middle-right edge of the hex. */
    MIDDLE_RIGHT,

    /** Harbor is located at the bottom-left edge of the hex. */
    BOTTOM_LEFT,

    /** Harbor is located at the bottom-right edge of the hex. */
    BOTTOM_RIGHT;
}
