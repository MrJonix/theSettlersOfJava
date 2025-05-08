package de.mrjonix.catan;

import com.almasb.fxgl.entity.SpawnData;
public class HexagonTile {
    private int q, r, number;
    private double size;
    private String imagePath;

    public HexagonTile(int q, int r, double size, String imagePath, int number) {
        this.q = q;
        this.r = r;
        this.size = size;
        this.imagePath = imagePath;
        this.number = number;
    }

    public SpawnData getSpawnData() {
        return new SpawnData(getX(), getY())
                .put("size", size)
                .put("path", imagePath)
                .put("q", q)
                .put("r", r)
        		.put("number", number);
    }
    public int getX() {return (int) (size * Math.sqrt(3) * (q + r / 2.0) + 700);}
    public int getY() { return (int) (size * 1.5 * r + 500); }
    public int getQ() { return q; }
    public int getR() { return r; }
}
