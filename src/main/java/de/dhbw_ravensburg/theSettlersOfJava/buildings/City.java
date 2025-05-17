package de.dhbw_ravensburg.theSettlersOfJava.buildings;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;

public class City extends Building {
	private final static String IMAGE_PATH = "city 2.png";
	
	private static final Map<ResourceType, Integer> CITY_COST;

	static {
	    Map<ResourceType, Integer> cost = new HashMap<>();
	    cost.put(ResourceType.WHEAT, 2);
	    cost.put(ResourceType.ORE, 3);
	    CITY_COST = Collections.unmodifiableMap(cost);
	}

	public City(HexCorner location, Player owner) {
	    super(location, owner, CITY_COST);
	}
	
	@Override
	public int getVictoryPoints() {
		return 2;
	}

	@Override
	public String getImagePath() {
		return IMAGE_PATH;
	}
	

}
