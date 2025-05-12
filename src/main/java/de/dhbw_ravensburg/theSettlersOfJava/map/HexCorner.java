package de.dhbw_ravensburg.theSettlersOfJava.map;


public class HexCorner {

    private double x;
    private double y;
    private Hex[] adjacentHexes;

    public HexCorner(Hex hex1, Hex hex2, Hex hex3) {
        this.adjacentHexes = new Hex[]{hex1, hex2, hex3};
        calculateCorner();
    }
    
    private void calculateCorner() {

        double x1 = adjacentHexes[0].getPosition().getX();
        double y1 = adjacentHexes[0].getPosition().getY();
        double x2 = adjacentHexes[1].getPosition().getX();
        double y2 = adjacentHexes[1].getPosition().getY();
        double x3 = adjacentHexes[2].getPosition().getX();
        double y3 = adjacentHexes[2].getPosition().getY();

        // Berechne den Durchschnitt der drei Mittelpunkte (Ecke)
        this.x = (x1 + x2 + x3) / 3;
        this.y = (y1 + y2 + y3) / 3;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Hex[] getAdjacentHexes() {
        return adjacentHexes;
    }

    @Override
    public String toString() {
        return String.format("Ecke Koordinaten: (%.2f, %.2f)", x, y);
    }
}

