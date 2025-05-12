package de.dhbw_ravensburg.theSettlersOfJava.map;

public class HexPosition {
	private int q;
	private int r;
	
	public HexPosition(int q, int r) {
		this.q = q;
		this.r = r;
	}
	
	public int getQ() {
		return q;
	}
	
	public int getR() {
		return r;
	}
	
	public int getX(int hexSize) {
		return (int) (hexSize * Math.sqrt(3) * (q + r / 2.0) + 700);
	}
	
    public int getY(int hexSize) { 
    	return (int) (hexSize * 1.5 * r + 500); 
    }
	
}
