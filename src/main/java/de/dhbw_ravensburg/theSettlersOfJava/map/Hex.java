package de.dhbw_ravensburg.theSettlersOfJava.map;

import com.almasb.fxgl.entity.SpawnData;

import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;

public class Hex {
	public static final int HEX_SIZE = 100;
	private HexType type;
	private int numberToken;
	private HexPosition position;
	
	public Hex(HexType type, int numberToken, HexPosition position) {
		this.type = type;
		this.numberToken = numberToken;
		this.position = position;
	}
	public HexPosition getPosition() {
		return position;
	}
	public HexType getHexType() {
		return type;
	}
	
	public SpawnData getSpawnData() {
        return new SpawnData(position.getX(), position.getY())
                .put("size", HEX_SIZE)
                .put("position", position)
        		.put("numberToken", numberToken)
        		.put("hexType", type);
    }
	
}
