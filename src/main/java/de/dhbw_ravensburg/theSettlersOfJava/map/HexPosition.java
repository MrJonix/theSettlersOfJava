package de.dhbw_ravensburg.theSettlersOfJava.map;

import javafx.geometry.Point2D;
import java.util.Objects;

/**
 * Represents the axial position of a hex tile on a grid using q and r coordinates.
 * 
 * This class also provides methods to convert these axial coordinates
 * into pixel-based world coordinates for rendering purposes.
 */
public class HexPosition {

    private final int q;
    private final int r;

    /**
     * Constructs a new HexPosition with the specified q and r coordinates.
     *
     * @param q the q coordinate (column in axial system)
     * @param r the r coordinate (row in axial system)
     */
    public HexPosition(int q, int r) {
        this.q = q;
        this.r = r;
    }

    /**
     * Returns the q coordinate.
     *
     * @return the q coordinate
     */
    public int getQ() {
        return q;
    }

    /**
     * Gets the r coordinate.
     *
     * @return the r coordinate
     */
    public int getR() {
        return r;
    }

    /**
     * Calculates the x coordinate in pixel space for this hex position.
     *
     * @return the x coordinate in world space
     */
    public int getX() {
        return (int) (Hex.HEX_SIZE * Math.sqrt(3) * (q + r / 2.0) + 700);
    }

    /**
     * Calculates the y coordinate in pixel space for this hex position.
     *
     * @return the y coordinate in world space
     */
    public int getY() {
        return (int) (Hex.HEX_SIZE * 1.5 * r + 500);
    }

    /**
     * Converts this hex position into a JavaFX Point2D representing pixel coordinates.
     *
     * @return the pixel-based world position as a Point2D
     */
    public Point2D getWorldPosition() {
        return new Point2D(getX(), getY());
    }

    /**
     * Returns a string representation of the hex position.
     *
     * @return a formatted string representing the q and r coordinates
     */
    @Override
    public String toString() {
        return String.format("HexPosition: [%d, %d]", q, r);
    }

    /**
     * Checks whether another object is equal to this HexPosition.
     * Two HexPositions are equal if they have the same q and r values.
     *
     * @param obj the object to compare
     * @return true if the object is a HexPosition with the same coordinates, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof HexPosition)) return false;
        HexPosition other = (HexPosition) obj;
        return q == other.q && r == other.r;
    }

    /**
     * Computes a hash code based on the q and r coordinates.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(q, r);
    }
}