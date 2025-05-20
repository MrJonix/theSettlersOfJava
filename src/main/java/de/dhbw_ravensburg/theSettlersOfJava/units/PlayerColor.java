package de.dhbw_ravensburg.theSettlersOfJava.units;

import javafx.scene.paint.Color;

public enum PlayerColor {
    RED(Color.web("#CC0000"), "RED"),
    BLUE(Color.web("#0011CC"), "BLUE"),
    GREEN(Color.web("#00CC70"), "GREEN"),
    ORANGE(Color.web("#CC7B00"), "ORANGE");

    private final Color color;
    private final String name;

    PlayerColor(Color color, String name) {
        this.color = color;
        this.name = name;
    }
    
    public Color getColor() {
        return color;
    }
    
    @Override
    public String toString() {
    	return name;
    }
}