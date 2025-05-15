package de.dhbw_ravensburg.theSettlersOfJava.buildings;

import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;

public class City extends Building {
	private final static String IMAGE_PATH = "city 2.png";
	
	public City(HexCorner position, Player owner) {
		super(position, owner);
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
