package de.dhbw_ravensburg.theSettlersOfJava.map;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

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
 * Represents an edge between two hexagonal corners.
 */
public class HexEdge {

    private final HexCorner start;
    private final HexCorner end;
    private final HexEdgeOrientation orientation;
    private Entity highlightEntity;

    /**
     * Constructs a HexEdge given two corners and an orientation.
     *
     * @param start       the start corner of the edge
     * @param end         the end corner of the edge
     * @param orientation the orientation of the edge
     */
    public HexEdge(HexCorner start, HexCorner end, HexEdgeOrientation orientation) {
        if (start == null || end == null || orientation == null) {
            throw new IllegalArgumentException("Hex corners and orientation cannot be null");
        }
        this.start = start;
        this.end = end;
        this.orientation = orientation;

    }

    public HexEdgeOrientation getHexEdgeOrientation() {
        return orientation;
    }

    public HexCorner[] getCorners() {
        return new HexCorner[]{start, end};
    }

    @Override
    public String toString() {
        return String.format("HexEdge between %s and %s", start, end);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof HexEdge)) return false;
        HexEdge other = (HexEdge) obj;
        return (start.equals(other.start) && end.equals(other.end)) ||
               (start.equals(other.end) && end.equals(other.start));
    }

    @Override
    public int hashCode() {
        return Objects.hash(start) + Objects.hash(end);
    }

    /**
     * Visualizes the edge by drawing a line between its corners with the specified color.
     *
     * @param color the color of the line
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
    
    public void visualizeHarborEdge (Color color, double thickness) {
    	double x1 = start.getX();
        double y1 = start.getY();
        double x2 = end.getX();
        double y2 = end.getY();

        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(color);
        line.setStrokeWidth(thickness);
        line.toFront(); // Make sure it's not hidden

        FXGL.entityBuilder()
            .at(0, 0)
            .view(line)
            .buildAndAttach();
    }

    /**
     * Handles a mouse click on the line.
     */
    private void handleMouseClick() {
        Player currentPlayer = App.getGameController().getCurrentPlayer();
        if (currentPlayer.build(new Road(this, currentPlayer))) {
            System.out.println("Road built successfully.");
        } else {
            System.out.println("Failed to build road.");
        }
    }

    public boolean isAdjacentTo(HexEdge otherEdge) {
        if (otherEdge == null) return false;
        return start.equals(otherEdge.start) || start.equals(otherEdge.end) ||
               end.equals(otherEdge.start) || end.equals(otherEdge.end);
    }

    public boolean isAdjacentToCorner(HexCorner corner) {
        return start.equals(corner) || end.equals(corner);
    }

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

    public void removeHighlight() {
        if (highlightEntity != null) {
            highlightEntity.removeFromWorld();
            highlightEntity = null;
        }
    }

}