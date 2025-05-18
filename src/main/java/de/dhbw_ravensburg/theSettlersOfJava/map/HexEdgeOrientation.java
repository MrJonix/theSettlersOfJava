package de.dhbw_ravensburg.theSettlersOfJava.map;
/**
 * Represents the orientation of a hex edge.
 */
public enum HexEdgeOrientation {
    LEFT_TO_RIGHT,
    RIGHT_TO_LEFT,
    STRAIGHT;
    
    /**
     * Gets a description of the orientation for better readability or debugging.
     *
     * @return a string description of the orientation
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