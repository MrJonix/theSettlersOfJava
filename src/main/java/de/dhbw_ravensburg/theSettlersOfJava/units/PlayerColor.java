package de.dhbw_ravensburg.theSettlersOfJava.units;

import javafx.scene.paint.Color;

/**
 * Enum representing the available player colors in the game.
 *
 * Each color has an associated JavaFX Color and a string representation
 * used for display and logic purposes.
 */
public enum PlayerColor {
    RED(Color.web("#CC0000"), "RED"),
    BLUE(Color.web("#0011CC"), "BLUE"),
    GREEN(Color.web("#00CC70"), "GREEN"),
    ORANGE(Color.web("#CC7B00"), "ORANGE");

    private final Color color;
    private final String name;

    /**
     * Constructs a PlayerColor enum with the specified JavaFX Color and name.
     *
     * @param color the JavaFX Color object associated with the player
     * @param name the name used for display and logic
     */
    PlayerColor(Color color, String name) {
        this.color = color;
        this.name = name;
    }
    
    /**
     * Returns the JavaFX Color associated with the player color.
     *
     * @return the color object used for rendering
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Returns the string representation of the player color.
     *
     * @return the name of the color
     */
    @Override
    public String toString() {
    	return name;
    }
}