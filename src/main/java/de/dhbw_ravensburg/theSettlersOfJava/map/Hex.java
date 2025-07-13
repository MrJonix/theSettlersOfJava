package de.dhbw_ravensburg.theSettlersOfJava.map;

import com.almasb.fxgl.entity.SpawnData;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a hexagonal tile in the game map.
 * Each hex has a resource type, a number token (for resource distribution),
 * a position on the board, and up to six adjacent corners where buildings can be placed.
 */
public class Hex {
    public static final int HEX_SIZE = 100;     /** Standard size of the hex tile, used for layout and rendering. */
    private static final int ADJACENT_CORNERS_COUNT = 6;	/** Number of corners adjacent to a hex tile. */
    private final HexType type;
    private final int numberToken;
    private final HexPosition position;
    private final HexCorner[] adjacentCorners = new HexCorner[ADJACENT_CORNERS_COUNT];

    /**
     * Constructs a new hex tile with the specified resource type, token number, and position.
     *
     * @param type        the resource type of the hex (e.g. WOOD, WHEAT)
     * @param numberToken the production token number (e.g. 6 or 8)
     * @param position    the position of the hex on the game board
     * @throws IllegalArgumentException if type or position is null
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
     * @param corner the HexCorner to assign
     * @param index  the index of the corner (0 to 5)
     * @throws IndexOutOfBoundsException if the index is outside the valid range
     */
    public void setAdjacentCorner(HexCorner corner, int index) {
        if (index < 0 || index >= ADJACENT_CORNERS_COUNT) {
            throw new IndexOutOfBoundsException("Index should be between 0 and 5");
        }

        adjacentCorners[index] = corner;
    }

    /**
     * Returns the position of the hex on the board.
     *
     * @return the position object of the hex
     */
    public HexPosition getPosition() {
        return position;
    }

    /**
     * Returns the resource type assigned to the hex.
     *
     * @return the HexType of the tile
     */
    public HexType getHexType() {
        return type;
    }

    /**
     * Returns the number token assigned to the hex.
     *
     * @return the number token for production rolls
     */
    public int getNumberToken() {
        return numberToken;
    }

    /**
     * Returns the FXGL spawn data used for rendering this hex.
     *
     * @return SpawnData including position and a reference to this hex
     */
    public SpawnData getSpawnData() {
        return new SpawnData(position.getX(), position.getY())
                .put("hex", this);
    }

    /**
     * Returns an unmodifiable list of the hex's adjacent corners.
     *
     * @return a list of adjacent HexCorner objects
     */
    public List<HexCorner> getAdjacentHexCorners() {
        return Collections.unmodifiableList(Arrays.asList(adjacentCorners));
    }
    
    /**
     * Returns the internal array of adjacent corners.
     * This method gives direct access to the modifiable array and should be used with care.
     *
     * @return an array of adjacent HexCorner references
     */
    public HexCorner[] getAdjacentCornersArray() {
    	return adjacentCorners;
    }
    
}