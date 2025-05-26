package de.dhbw_ravensburg.theSettlersOfJava.map;

import com.almasb.fxgl.entity.SpawnData;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a hexagonal tile in the game map, with a specific type,
 * token number, position, and adjacent corners.
 */
public class Hex {
    public static final int HEX_SIZE = 100;
    private static final int ADJACENT_CORNERS_COUNT = 6;
    private final HexType type;
    private final int numberToken;
    private final HexPosition position;
    private final HexCorner[] adjacentCorners = new HexCorner[ADJACENT_CORNERS_COUNT];

    /**
     * Creates a new Hex with the specified type, token number, and position.
     *
     * @param type        the type of the hex
     * @param numberToken the token number
     * @param position    the position of the hex
     */
    public Hex(HexType type, int numberToken, HexPosition position) {
        if (type == null || position == null) {
            throw new IllegalArgumentException("HexType and HexPosition cannot be null");
        }

        this.type = type;
        this.numberToken = numberToken;
        this.position = position;
    }

    /**
     * Sets an adjacent corner at the specified index.
     *
     * @param corner the adjacent corner to set
     * @param index  the index at which to set the adjacent corner
     */
    public void setAdjacentCorner(HexCorner corner, int index) {
        if (index < 0 || index >= ADJACENT_CORNERS_COUNT) {
            throw new IndexOutOfBoundsException("Index should be between 0 and 5");
        }

        adjacentCorners[index] = corner;
    }

    /**
     * Returns the position of the hex.
     *
     * @return the position of the hex
     */
    public HexPosition getPosition() {
        return position;
    }

    /**
     * Returns the type of the hex.
     *
     * @return the type of the hex
     */
    public HexType getHexType() {
        return type;
    }

    /**
     * Returns the token number of the hex.
     *
     * @return the token number of the hex
     */
    public int getNumberToken() {
        return numberToken;
    }

    /**
     * Returns spawn data associated with the hex.
     *
     * @return the spawn data
     */
    public SpawnData getSpawnData() {
        return new SpawnData(position.getX(), position.getY())
                .put("hex", this);
    }

    /**
     * Returns an unmodifiable list of adjacent corners.
     *
     * @return an unmodifiable list containing the adjacent corners
     */
    public List<HexCorner> getAdjacentHexCorners() {
        return Collections.unmodifiableList(Arrays.asList(adjacentCorners));
    }
    
}