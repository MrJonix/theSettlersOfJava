package de.dhbw_ravensburg.theSettlersOfJava.map;

import com.almasb.fxgl.dsl.FXGL;
import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.Settlement;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Represents a corner between hexagonal tiles, calculated from the centers of adjacent hexes.
 */
public class HexCorner {

    private final double x;
    private final double y;
    private final List<Hex> adjacentHexes;

    /**
     * Initializes a HexCorner given three adjacent hexes.
     *
     * @param hex1 the first adjacent hex
     * @param hex2 the second adjacent hex
     * @param hex3 the third adjacent hex
     */
    public HexCorner(Hex hex1, Hex hex2, Hex hex3) {
        if (hex1 == null || hex2 == null || hex3 == null) {
            throw new IllegalArgumentException("Adjacent hexes cannot be null");
        }

        // Initialize adjacent hexes as an unmodifiable list
        adjacentHexes = List.of(hex1, hex2, hex3);

        // Calculate the corner position
        double[] xy = calculateCornerPosition();
        this.x = xy[0];
        this.y = xy[1];
    }

    /**
     * Calculates the average position of the three hex centers to find the corner.
     *
     * @return an array containing x and y coordinates of the corner
     */
    private double[] calculateCornerPosition() {
        double xSum = 0;
        double ySum = 0;
        for (Hex hex : adjacentHexes) {
            xSum += hex.getPosition().getX();
            ySum += hex.getPosition().getY();
        }
        return new double[]{xSum / 3, ySum / 3};
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public List<Hex> getAdjacentHexes() {
        return Collections.unmodifiableList(adjacentHexes);
    }

    /**
     * Visualizes this hex corner using a circle with the specified color.
     *
     * @param color the color of the circle
     */
    public void visualizeCorner(Color color) {
        Circle circle = new Circle(10, color);
        FXGL.entityBuilder()
            .at(x, y)
            .view(circle)
            .buildAndAttach();

        circle.setOnMouseClicked(event -> handleMouseClick());
    }

    private void handleMouseClick() {
        Player currentPlayer = App.getGameController().getCurrentPlayer();
        currentPlayer.build(new Settlement(this, currentPlayer));
    }

    @Override
    public String toString() {
        return String.format("Corner Coordinates: (%.2f, %.2f)", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof HexCorner)) return false;
        HexCorner other = (HexCorner) obj;

        return new HashSet<>(adjacentHexes).equals(new HashSet<>(other.adjacentHexes));
    }

    @Override
    public int hashCode() {
        return Objects.hash(new HashSet<>(adjacentHexes));
    }
}