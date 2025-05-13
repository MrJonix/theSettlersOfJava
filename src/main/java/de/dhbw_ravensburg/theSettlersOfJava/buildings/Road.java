package de.dhbw_ravensburg.theSettlersOfJava.buildings;

import de.dhbw_ravensburg.theSettlersOfJava.map.HexEdge;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;

public class Road {
	private final HexEdge location;
	private final Player owner;
	
	public Road(HexEdge location, Player owner) {
		this.location = location;
		this.owner = owner;
	}
	
}
