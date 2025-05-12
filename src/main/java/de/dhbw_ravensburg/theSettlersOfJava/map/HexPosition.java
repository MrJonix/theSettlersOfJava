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
	
	public int getX() {
		return (int) (Hex.HEX_SIZE * Math.sqrt(3) * (q + r / 2.0) + 700);
	}
	
    public int getY() { 
    	return (int) (Hex.HEX_SIZE * 1.5 * r + 500); 
    }
    public String toString() {
		return "HexPosition: " + q + " " + r;
    }
    @Override
    public boolean equals(Object obj) {
    	if (obj instanceof HexPosition) {
    		HexPosition pos = (HexPosition) obj;
    		return pos.getQ() == q && pos.getR() == r;
    	}
		return false;
    }
}
