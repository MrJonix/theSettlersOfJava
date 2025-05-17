package de.dhbw_ravensburg.theSettlersOfJava.map;

import com.almasb.fxgl.entity.SpawnData;

import de.dhbw_ravensburg.theSettlersOfJava.buildings.Building;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;

public class Hex {
	public static final int HEX_SIZE = 100;
	private HexType type;
	private int numberToken;
	private HexPosition position;
	private HexCorner[] adjacentCorners = new HexCorner[6];
	
	public Hex(HexType type, int numberToken, HexPosition position) {
		this.type = type;
		this.numberToken = numberToken;
		this.position = position;
	}
	
	public void setAdjacentCorner(HexCorner corner, int index) {
		adjacentCorners[index] = corner;
	}
	
	public HexPosition getPosition() {
		return position;
	}
	
	public HexType getHexType() {
		return type;
	}
	
	public int getNumberToken() {
		return numberToken;
	}
	
	public SpawnData getSpawnData() {
        return new SpawnData(position.getX(), position.getY())
        		.put("hex", this);
    }
	public HexCorner[] getAdjacentHexCorners() {
		return adjacentCorners;
	}
	
}
