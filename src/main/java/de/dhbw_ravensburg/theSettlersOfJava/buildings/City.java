package de.dhbw_ravensburg.theSettlersOfJava.buildings;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.scene.paint.Color;

public class City extends Building {
	
    private final static Map<Color, String> IMAGE_PATHS = Map.of(
            Color.RED, "RED_city.png",
            Color.BLUE, "BLUE_city.png",
            Color.ORANGE, "ORANGE_city.png",
            Color.GREEN, "GREEN_city.png"
        );
    
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
		return IMAGE_PATHS.get(owner.getColor());
	}
	

}
