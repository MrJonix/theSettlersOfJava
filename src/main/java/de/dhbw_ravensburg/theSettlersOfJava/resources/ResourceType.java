package de.dhbw_ravensburg.theSettlersOfJava.resources;

import java.awt.Color;
import java.util.Random;

public enum ResourceType {
	//TODO: Bilder einfügen
    BRICK("Brick", Color.RED, "/cards/card_CLAY.png"),
    WOOD("WOOD", Color.GREEN, "/cards/card_WOOD.png"),
    WOOL("Wool", Color.WHITE, "/cards/card_WOOL.png"),
    WHEAT("Wheat", Color.YELLOW, "/cards/card_WHEAT.png"),
    ORE("Ore", Color.GRAY, "/cards/card_MOUNTAIN.png");

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
