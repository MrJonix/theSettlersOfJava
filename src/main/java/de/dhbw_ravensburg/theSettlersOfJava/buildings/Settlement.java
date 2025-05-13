package de.dhbw_ravensburg.theSettlersOfJava.buildings;

import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;

public class Settlement extends Building {

	public Settlement(HexCorner position, Player owner) {
		super(position, owner);
	}

	@Override
	int getVictoryPoints() {
		return 1;
	}
	
}
