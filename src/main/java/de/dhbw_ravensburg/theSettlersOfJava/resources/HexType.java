package de.dhbw_ravensburg.theSettlersOfJava.resources;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum HexType {
	
    FOREST("Forest", "/images/forest.png", new Color(34, 139, 34)),
    HILL("Hill", "/images/hill.png", new Color(160, 82, 45)),
    FIELD("Field", "/images/field.png", new Color(255, 255, 0)),
    MOUNTAIN("Mountain", "/images/mountain.png", new Color(128, 128, 128)),
    PASTURE("Pasture", "/images/pasture.png", new Color(124, 252, 0)),
    DESERT("Desert", "/images/desert.png", new Color(237, 201, 175)),
    WATER("Water", "/images/water.png", new Color(0, 119, 190));

    private final String displayName;
    private final String imagePath;
    private final Color color;

    HexType(String displayName, String imagePath, Color color) {
        this.displayName = displayName;
        this.imagePath = imagePath;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Color getColor() {
        return color;
    }

    public static List<HexType> generateHexTypeList() {
        List<HexType> hexTypeList = new ArrayList<>();

        // 4x Forest, Hill, Field
        for (int i = 0; i < 4; i++) {
            hexTypeList.add(FOREST);
            hexTypeList.add(HILL);
            hexTypeList.add(FIELD);
        }

        // 3x Mountain, Pasture
        for (int i = 0; i < 3; i++) {
            hexTypeList.add(MOUNTAIN);
            hexTypeList.add(PASTURE);
        }

        // 1x Desert
        hexTypeList.add(DESERT);

        // Liste mischen
        Collections.shuffle(hexTypeList);
        return hexTypeList;
    }
}

