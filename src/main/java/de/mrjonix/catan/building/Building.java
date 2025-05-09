package de.mrjonix.catan.building;

import de.mrjonix.catan.Node;
import de.mrjonix.catan.Player;

public abstract class Building {
	private Node node;
	private Player owner;
	
	public Building(Node node, Player owner) {
		this.node = node;
		this.owner = owner;
	}
	public Player getOwner() {
		return owner;
	}
	
	public Node getNode() {
		return node;
	}
}
