package de.dhbw_ravensburg.theSettlersOfJava.map;

/**
 * Represents the orientation of an edge between two hex corners.
 * Used for layout, rendering, and harbor texture alignment.
 */
public enum HexEdgeOrientation {

    // Edge runs from left corner to right corner
    LEFT_TO_RIGHT,

    // Edge runs from right corner to left corner
    RIGHT_TO_LEFT,

    // Edge is vertically or horizontally aligned without clear direction
    STRAIGHT;

    /**
     * Returns a readable description of the edge orientation.
     *
     * @return a string representation of the orientation
     */
    @Override
    public String toString() {
        switch (this) {
            case LEFT_TO_RIGHT:
                return "Left to Right";
            case RIGHT_TO_LEFT:
                return "Right to Left";
            case STRAIGHT:
                return "Straight";
            default:
                throw new IllegalArgumentException("Unexpected orientation: " + this);
        }
    }
}
