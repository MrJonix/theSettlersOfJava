package de.dhbw_ravensburg.theSettlersOfJava.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum HarborType {
    BRICK,
    WOOD,
    WOOL,
    WHEAT,
    ORE,
    THREE_TO_ONE;
    
    
	public static List<HarborType> generateHarborTypes() {
	    List<HarborType> harbors = new ArrayList<>(List.of(
	        BRICK, THREE_TO_ONE, WOOD, THREE_TO_ONE,
	        WOOL, THREE_TO_ONE, ORE, THREE_TO_ONE, WHEAT
	    ));
	    Collections.shuffle(harbors);
	    return harbors;
	}

}