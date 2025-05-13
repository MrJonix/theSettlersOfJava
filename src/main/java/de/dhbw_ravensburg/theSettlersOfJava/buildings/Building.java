package de.dhbw_ravensburg.theSettlersOfJava.buildings;

import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;

public abstract class Building {
	private final HexCorner position;
	private final Player owner;
	
	public Building (HexCorner position, Player owner) {
		this.position = position;
		this.owner = owner;
	}
	
	public HexCorner getPosition() {
		return position;
	}
	public Player getOwner() {
		return owner;
	}
	
	public abstract int getVictoryPoints();
	public abstract String getImagePath();
	public void visualize() {
		
	}
}
