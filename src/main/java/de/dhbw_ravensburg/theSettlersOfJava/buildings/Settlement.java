package de.dhbw_ravensburg.theSettlersOfJava.buildings;

import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;

public class Settlement extends Building {
	private final static String IMAGE_PATH = "settlement.png";

	public Settlement(HexCorner position, Player owner) {
		super(position, owner);
	}

	@Override
	public int getVictoryPoints() {
		return 1;
	}

	@Override
	public String getImagePath() {
		return IMAGE_PATH;
	}
	
}
