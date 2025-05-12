package de.dhbw_ravensburg.theSettlersOfJava.resources;

import java.awt.Color;
import java.util.Random;

public enum ResourceType {
	//TODO: Bilder einfügen
    BRICK("Brick", Color.RED, "images/brick.png"),
    LUMBER("Lumber", Color.GREEN, "images/lumber.png"),
    WOOL("Wool", Color.WHITE, "images/wool.png"),
    GRAIN("Grain", Color.YELLOW, "images/grain.png"),
    ORE("Ore", Color.GRAY, "images/ore.png");

    private final String displayName;
    private final Color color;
    private final String imagePath;

    // Konstruktor
    ResourceType(String displayName, Color color, String imagePath) {
        this.displayName = displayName;
        this.color = color;
        this.imagePath = imagePath;
    }

    // Getter für den Displaynamen
    public String getDisplayName() {
        return displayName;
    }

    // Getter für die Farbe
    public Color getColor() {
        return color;
    }

    // Getter für den Image-Pfad
    public String getImagePath() {
        return imagePath;
    }

    // Methode, um eine zufällige Ressource zu erhalten
    public static ResourceType getRandomResource() {
        Random random = new Random();
        ResourceType[] values = ResourceType.values();
        return values[random.nextInt(values.length)];
    }
}
