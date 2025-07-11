package de.dhbw_ravensburg.theSettlersOfJava.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum HarborType {
    BRICK("/ship/boat_CLAY.png"),
    WOOD("/ship/boat_WOOD.png"),
    WOOL("/ship/boat_WOOL.png"),
    WHEAT("/ship/boat_WHEAT.png"),
    ORE("/ship/boat_MOUNTAIN.png"),
    THREE_TO_ONE("/ship/boat_3_1.png");
	private final String imagePath;
	
	private HarborType(String imagePath) {
		this.imagePath = imagePath;
	}
    
	public static List<HarborType> generateHarborTypes() {
	    List<HarborType> harbors = new ArrayList<>(List.of(
	        BRICK, THREE_TO_ONE, WOOD, THREE_TO_ONE,
	        WOOL, THREE_TO_ONE, ORE, THREE_TO_ONE, WHEAT
	    ));
	    Collections.shuffle(harbors);
	    return harbors;
	}


	public String getImagePath() {
		return imagePath;
	}

	public boolean isSpecific() {
	    return this != THREE_TO_ONE;
	}

	public ResourceType toResourceType() {
	    return switch (this) {
	        case BRICK -> ResourceType.BRICK;
	        case WOOD -> ResourceType.WOOD;
	        case WOOL -> ResourceType.WOOL;
	        case WHEAT -> ResourceType.WHEAT;
	        case ORE -> ResourceType.ORE;
	        default -> null; // THREE_TO_ONE has no specific resource
	    };
	}
}