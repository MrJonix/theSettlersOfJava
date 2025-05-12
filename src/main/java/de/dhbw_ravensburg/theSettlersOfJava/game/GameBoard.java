package de.dhbw_ravensburg.theSettlersOfJava.game;

import static com.almasb.fxgl.dsl.FXGL.spawn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.almasb.fxgl.dsl.FXGL;

import de.dhbw_ravensburg.theSettlersOfJava.map.Hex;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexPosition;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class GameBoard {

    private static final int[][] coords = {
        {0,-2},{-2,0},{-1,-1},{0,-1},{-1,0},{0,0},{0,2},{2,0},{1,1},{0,1},{1,0},
        {-1,1},{1,-1},{-1,2},{2,-1},{-2,1},{-2,2},{1,-2},{2,-2}
    };
    private static final int[][] waterCoords = {
        {-3,0},{-2,-1},{-1,-2},{0,-3},{1,-3},{2,-3},{3,-3},{3,-2},{3,-1},
        {3,0},{2,1},{1,2},{0,3},{-1,3},{-2,3},{-3,3},{-3,2},{-3,1}
    };

    private List<Hex> hexes = new ArrayList<>();
    private List<HexCorner> hexCorners = new ArrayList<>();
    private Random random = new Random();

    public GameBoard() {
        List<HexType> hexTypeList = HexType.generateHexTypeList();
        for (int i = 0; i < coords.length; i++) {
            int[] coord = coords[i];
            HexType type = hexTypeList.get(i);

            int number = 0;
            do {
                number = random.nextInt(2, 13);
            } while (number == 7);
            
            if (type == HexType.DESERT) {
                number = 0;
            }
            
            Hex tile = new Hex(type , number, new HexPosition(coord[0], coord[1]));
            hexes.add(tile);
            spawn("hexagon", tile.getSpawnData());

            //calculateCornersForHex(tile);
        }

        for (int i = 0; i < waterCoords.length; i++) {
            int[] coord = waterCoords[i];
            Hex tile = new Hex(HexType.WATER , 0, new HexPosition(coord[0], coord[1]));
            hexes.add(tile);
            spawn("hexagon", tile.getSpawnData());
        }

        // Visualisiere die Ecken
        calculateCornersForHex(hexes.get(10));
        
        visualizeCorners();
    }

    private void calculateCornersForHex(Hex hex) {
    	HexPosition pos = hex.getPosition();
        int q = pos.getQ();
        int r = pos.getR();

        // Richtungen im Uhrzeigersinn + Wiederholung der ersten
        int[][] adjazenz = {
            { 0, -1 }, { 1, -1 }, { 1, 0 },
            { 0, 1 }, { -1, 1 }, { -1, 0 },
            { 0, -1 } // Wiederholung für einfaches Paar
        };

        if (hex == null) return; // Falls zentrales Hex nicht vorhanden ist

        for (int i = 0; i < 6; i++) {
            int[] off1 = adjazenz[i];
            int[] off2 = adjazenz[i + 1];

            HexPosition pos1 = new HexPosition(q + off1[0], r + off1[1]);
            HexPosition pos2 = new HexPosition(q + off2[0], r + off2[1]);

            Hex h1 = getHexByPosition(pos1);
            Hex h2 = getHexByPosition(pos2);
            System.out.println(pos1);
            System.out.println(pos2);
            System.out.println(h1);
            System.out.println(h2);
            if (h1 != null && h2 != null) {
            	
                hexCorners.add(new HexCorner(hex,h1,h2));
            }
        }

    }


    private void visualizeCorners() {
        for (HexCorner corner : hexCorners) {
            // Ecken visualisieren (beispielsweise mit Kreisen oder Linien)
            double x = corner.getX();
            double y = corner.getY();
            
            // Visualisiere Ecken als kleine Kreise
            Circle circle = new Circle(x, y, 5);
            circle.setFill(Color.RED);
            FXGL.getGameScene().addUINode(circle);

            Hex[] adjacentHexes = corner.getAdjacentHexes();  // Wir erhalten ein Array von Hexen

            for (int i = 0; i < adjacentHexes.length; i++) { // Verwenden von Array statt List
                double x1 = adjacentHexes[i].getPosition().getX();
                double y1 = adjacentHexes[i].getPosition().getY();
                double x2 = adjacentHexes[(i + 1) % adjacentHexes.length].getPosition().getX();  // Array-Indexmodifikation
                double y2 = adjacentHexes[(i + 1) % adjacentHexes.length].getPosition().getY();  // Array-Indexmodifikation

                // Erstelle Linie
                Line line = new Line(x1, y1, x2, y2);
                line.setStroke(Color.BLUE);
                //FXGL.getGameScene().addUINode(line);
            }
        }
    }
    private Hex getHexByPosition(HexPosition pos) {
        for (Hex hex : hexes) {
            if (hex.getPosition().equals(pos)) {
                return hex;
            }
        }
        return null; // falls nicht vorhanden
    }
}

