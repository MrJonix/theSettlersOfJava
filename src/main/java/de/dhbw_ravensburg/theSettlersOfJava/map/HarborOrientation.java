package de.dhbw_ravensburg.theSettlersOfJava.map;

public enum HarborOrientation {
	
    TOP_LEFT("/harbour/TOP_LEFT.png"),
    TOP_RIGHT("/harbour/TOP_RIGHT.png"),
    MIDDLE_LEFT("/harbour/MIDDLE_LEFT.png"),
    MIDDLE_RIGHT("/harbour/MIDDLE_RIGHT.png"),
    BOTTOM_LEFT("/harbour/BOTTOM_LEFT.png"),
    BOTTOM_RIGHT("/harbour/BOTTOM_RIGHT.png");

    private final String imagePath;

    HarborOrientation(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }
}
