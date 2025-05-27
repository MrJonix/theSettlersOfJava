package de.dhbw_ravensburg.theSettlersOfJava.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum HarborType {
    BRICK("ship/boat_BRICK.png"),
    WOOD("ship/boat_WOOD.png"),
    WOOL("ship/boat_WOOL.png"),
    WHEAT("ship/boat_WHEAT.png"),
    ORE("ship/boat_ORE.png"),
    THREE_TO_ONE("ship/boat_THREE_TO_ONE.png");
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

}