package de.dhbw_ravensburg.theSettlersOfJava.map;

import javafx.geometry.Point2D;
import java.util.Objects;

/**
 * Represents the axial position of a hex tile on a grid.
 */
public class HexPosition {

    private final int q;
    private final int r;

    /**
     * Constructs a new HexPosition with the specified q and r coordinates.
     *
     * @param q the q coordinate (column)
     * @param r the r coordinate (row)
     */
    public HexPosition(int q, int r) {
        this.q = q;
        this.r = r;
    }

    /**
     * Gets the q coordinate.
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
     * Calculates and returns the x coordinate based on the axial coordinates.
     *
     * @return the x coordinate in the world
     */
    public int getX() {
        return (int) (Hex.HEX_SIZE * Math.sqrt(3) * (q + r / 2.0) + 700);
    }

    /**
     * Calculates and returns the y coordinate based on the axial coordinates.
     *
     * @return the y coordinate in the world
     */
    public int getY() {
        return (int) (Hex.HEX_SIZE * 1.5 * r + 500);
    }

    /**
     * Gets the world position as a 2D point.
     *
     * @return the world position as a Point2D
     */
    public Point2D getWorldPosition() {
        return new Point2D(getX(), getY());
    }

    @Override
    public String toString() {
        return String.format("HexPosition: [%d, %d]", q, r);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof HexPosition)) return false;
        HexPosition other = (HexPosition) obj;
        return q == other.q && r == other.r;
    }

    @Override
    public int hashCode() {
        return Objects.hash(q, r);
    }
}