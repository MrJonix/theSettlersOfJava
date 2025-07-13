package de.dhbw_ravensburg.theSettlersOfJava.map;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.Road;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.effect.Glow;
import javafx.util.Duration;

import java.util.Objects;

/**
 * Represents an edge between two hexagonal corners on the game board.
 * Used for placing roads, harbors, and handling user interactions.
 */
public class HexEdge {

    private final HexCorner start;
    private final HexCorner end;
    private final HexEdgeOrientation orientation;
    private Entity highlightEntity;	// Holds the FXGL entity representing the highlight line (if active)

    /**
     * Constructs a HexEdge given two corners and an orientation.
     *
     * @param start       the start corner of the edge
     * @param end         the end corner of the edge
     * @param orientation the orientation of the edge
     * @throws IllegalArgumentException if any parameter is null
     */
    public HexEdge(HexCorner start, HexCorner end, HexEdgeOrientation orientation) {
        if (start == null || end == null || orientation == null) {
            throw new IllegalArgumentException("Hex corners and orientation cannot be null");
        }
        this.start = start;
        this.end = end;
        this.orientation = orientation;

    }

    /**
     * Returns the orientation of this edge.
     *
     * @return the edge orientation
     */
    public HexEdgeOrientation getHexEdgeOrientation() {
        return orientation;
    }

    /**
     * Returns an array containing the two corners of the edge.
     *
     * @return an array with start and end corners
     */
    public HexCorner[] getCorners() {
        return new HexCorner[]{start, end};
    }

    /**
     * Returns a string representation of the edge showing both corners.
     *
     * @return a string showing the edge endpoints
     */
    @Override
    public String toString() {
        return String.format("HexEdge between %s and %s", start, end);
    }

    /**
     * Checks equality based on corner pairs, regardless of order.
     *
     * @param obj the object to compare
     * @return true if the same corners are connected
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof HexEdge)) return false;
        HexEdge other = (HexEdge) obj;
        return (start.equals(other.start) && end.equals(other.end)) ||
               (start.equals(other.end) && end.equals(other.start));
    }

    /**
     * Computes a hash code based on both edge corners.
     *
     * @return the hash code of this edge
     */
    @Override
    public int hashCode() {
        return Objects.hash(start) + Objects.hash(end);
    }

    /**
     * Visualizes the edge using a colored line between its two corners.
     *
     * @param color the color to use for the line
     */
    public void visualizeEdge(Color color) {
        double x1 = start.getX();
        double y1 = start.getY();
        double x2 = end.getX();
        double y2 = end.getY();

        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(color);
        line.setStrokeWidth(5);

        FXGL.entityBuilder()
            .at(0, 0)
            .view(line)
            .buildAndAttach();

        line.setOnMouseClicked(event -> handleMouseClick());
    }
    
    /**
     * Visualizes a harbor edge using a textured image, positioned and scaled correctly.
     *
     * @param orientation the orientation of the harbor (used to determine texture)
     * @param color       unused (reserved for future customizations)
     * @param thickness   currently unused; intended for sizing adjustments
     */

    public void visualizeHarborEdge(HarborOrientation orientation, Color color, double thickness) {
        double x1 = start.getX();
        double y1 = start.getY();
        double x2 = end.getX();
        double y2 = end.getY();

        String texturePath;
        switch (orientation) {
            case TOP_LEFT:
                texturePath = "/harbor/steg_LINKSOBEN.png";
                break;
            case TOP_RIGHT:
                texturePath = "/harbor/steg_RECHTSOBEN.png";
                break;
            case BOTTOM_LEFT:
                texturePath = "/harbor/steg_LINKSUNTEN.png";
                break;
            case BOTTOM_RIGHT:
                texturePath = "/harbor/steg_RECHTSUNTEN.png";
                break;
            default:
                texturePath = "/harbor/steg_LINKSOBEN.png";
                break;
        }


        Texture texture = FXGL.getAssetLoader().loadTexture(texturePath);

        // Berechne die Richtung und Länge des Hafens
        double dx = x2 - x1;
        double dy = y2 - y1;
        double length = Math.hypot(dx, dy);

        // Setze die Größe des Bildes (z. B. Länge der Kante)
        texture.setFitWidth(length);
        texture.setPreserveRatio(true); // Höhe wird automatisch skaliert


        // Positioniere zentriert zwischen den beiden Punkten
        double centerX = (x1 + x2) / 2;
        double centerY = (y1 + y2) / 2;

        FXGL.entityBuilder()
            .at(centerX - texture.getFitWidth() / 2, centerY - texture.getFitHeight() / 2)
            .zIndex(9)
            .view(texture)
            .buildAndAttach();
    }


    /**
     * Handles mouse click interactions for the edge.
     * Attempts to build a road for the current player.
     */
    private void handleMouseClick() {
        Player currentPlayer = App.getGameController().getCurrentPlayer();
        currentPlayer.build(new Road(this, currentPlayer), success -> {
            if (success) {
                FXGL.getNotificationService().pushNotification("Straße erfolgreich gebaut!");
            } else {
                FXGL.getNotificationService().pushNotification("Straßenbau fehlgeschlagen.");
            }
        });
    }

    /**
     * Returns true if this edge is adjacent to the given edge.
     *
     * @param otherEdge the edge to check adjacency with
     * @return true if the edges share a common corner
     */
    public boolean isAdjacentTo(HexEdge otherEdge) {
        if (otherEdge == null) return false;
        return start.equals(otherEdge.start) || start.equals(otherEdge.end) ||
               end.equals(otherEdge.start) || end.equals(otherEdge.end);
    }

    /**
     * Returns true if this edge is adjacent to the given corner.
     *
     * @param corner the corner to check
     * @return true if the corner is part of this edge
     */
    public boolean isAdjacentToCorner(HexCorner corner) {
        return start.equals(corner) || end.equals(corner);
    }

    /**
     * Highlights the edge with an animated glow and interactive stroke effect.
     */
    public void highlight() {
    	double x1 = start.getX();
    	double y1 = start.getY();
    	double x2 = end.getX();
    	double y2 = end.getY();

    	// Mittelpunkt berechnen
    	double midX = (x1 + 3*x2) / 4;
    	double midY = (y1 + 3*y2) / 4;
    	
    	double mid1X = (3*x1 + x2) / 4;
    	double mid1Y = (3*y1 + y2) / 4;

        Line highlightLine = new Line(mid1X, mid1Y, midX, midY);
        highlightLine.setStroke(App.getGameController().getCurrentPlayer().getColor());
        highlightLine.setStrokeWidth(5);
        highlightLine.setMouseTransparent(false);

        // Glow-Effekt hinzufügen
        Glow glow = new Glow(0.3);
        highlightLine.setEffect(glow);

        highlightEntity = FXGL.entityBuilder()
            .at(0, 0)
            .view(highlightLine)
            .zIndex(1)
            .buildAndAttach();

        // Animation: strokeWidth pulsieren lassen
        Timeline animation = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(highlightLine.strokeWidthProperty(), 5),
                new KeyValue(glow.levelProperty(), 0.3)
            ),
            new KeyFrame(Duration.seconds(0.5),
                new KeyValue(highlightLine.strokeWidthProperty(), 8),
                new KeyValue(glow.levelProperty(), 0.8)
            ),
            new KeyFrame(Duration.seconds(1),
                new KeyValue(highlightLine.strokeWidthProperty(), 5),
                new KeyValue(glow.levelProperty(), 0.3)
            )
        );
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.setAutoReverse(true);
        animation.play();

        highlightLine.setOnMouseClicked(event -> {
            handleMouseClick();
        });
    }

    /**
     * Removes the visual highlight from the edge.
     */
    public void removeHighlight() {
        if (highlightEntity != null) {
            highlightEntity.removeFromWorld();
            highlightEntity = null;
        }
    }

}