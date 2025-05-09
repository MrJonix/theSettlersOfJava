package de.mrjonix.catan;

import de.mrjonix.catan.building.Building;

public class Node {
	private HexagonTile[] neighborTiles;
	private Building building;
	
	public Node(HexagonTile[] neighborTiles) {
		building = null;
	}
	
	public Building getBuilding() {
		return building;
	}
	
}
