package de.dhbw_ravensburg.theSettlersOfJava.units;

public class Player {
	private static int ID_COUNTER = 0;
	private int id;
	private int victoryPoints;
	private String name;
	
	public Player(String name) {
		this.id = ID_COUNTER;
		this.victoryPoints = 0;
		ID_COUNTER++;
		this.name = name;
	}

	public int getVictoryPoints() {
		return victoryPoints;
	}

	public void addVictoryPoints(int differenz) {
		this.victoryPoints += differenz;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	public String toString() {
		return name;
	}
}
