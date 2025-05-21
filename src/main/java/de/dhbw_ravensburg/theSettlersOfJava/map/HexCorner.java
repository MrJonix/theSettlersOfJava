package de.dhbw_ravensburg.theSettlersOfJava.map;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.AnimationBuilder.ScaleAnimationBuilder;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.Settlement;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a corner between hexagonal tiles, calculated from the centers of adjacent hexes.
 */
public class HexCorner {

    private final double x;
    private final double y;
    private final Set<Hex> adjacentHexes;
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
        adjacentHexes = Set.of(hex1, hex2, hex3);

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

    public Set<Hex> getAdjacentHexes() {
        return Collections.unmodifiableSet(adjacentHexes);
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

 // fields in your class
    private Entity highlightEntity;
    private Animation<?> highlightAnimation;

    public void highlight() {
        Circle highlightCircle = new Circle(14, Color.TRANSPARENT);
        highlightCircle.setStroke(App.getGameController().getCurrentPlayer().getColor());
        highlightCircle.setStrokeWidth(2);

        // create and store the highlight entity
        highlightEntity = FXGL.entityBuilder()
            .at(x, y)
            .view(highlightCircle)
            .zIndex(99).build();
        FXGL.getGameWorld().addEntity(highlightEntity);
        // create and store the animation
        FXGL.animationBuilder()
            .autoReverse(true)
            .repeatInfinitely()
            .duration(Duration.seconds(1))
            .interpolator(javafx.animation.Interpolator.EASE_BOTH)
            .scale(highlightCircle)
            .from(new Point2D(1.0, 1.0))
            .to(new Point2D(1.3, 1.3))
            .buildAndPlay();
        

        // optional: click to remove
        highlightCircle.setOnMouseClicked(event -> {
        	handleMouseClick();
        });
    }

    public void removeHighlight() {
        if (highlightAnimation != null) {
            highlightAnimation.stop();
            highlightAnimation = null;
        }

        if (highlightEntity != null) {
            if (highlightEntity.isActive()) {  // Prüfen, ob die Entität aktiv ist
                highlightEntity.removeFromWorld();
                highlightEntity = null;
            } else {
                System.err.println("Fehler: Die Entität ist bereits entfernt oder nicht aktiv.");
            }
        } else {
            System.err.println("Fehler: highlightEntity ist null.");
        }
    }



}