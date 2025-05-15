package de.dhbw_ravensburg.theSettlersOfJava.game;

import static com.almasb.fxgl.dsl.FXGL.spawn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.dhbw_ravensburg.theSettlersOfJava.buildings.Building;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.City;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.Road;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.Settlement;
import de.dhbw_ravensburg.theSettlersOfJava.map.Hex;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexEdge;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexEdgeOrientation;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexPosition;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.scene.paint.Color;

public class GameBoard {

    private static final int[][] coords = {
        {0,-2},{-2,0},{-1,-1},{0,-1},{-1,0},{0,0},{0,2},{2,0},{1,1},{0,1},{1,0},
        {-1,1},{1,-1},{-1,2},{2,-1},{-2,1},{-2,2},{1,-2},{2,-2}
    };
    
    private static final int[][] waterCoords = {
        {-3,0},{-2,-1},{-1,-2},{0,-3},{1,-3},{2,-3},{3,-3},{3,-2},{3,-1},
        {3,0},{2,1},{1,2},{0,3},{-1,3},{-2,3},{-3,3},{-3,2},{-3,1}
    };

    private Set<Hex> hexes = new HashSet<>();
    private Set<HexCorner> hexCorners = new HashSet<>();
    private Set<HexEdge> hexEdges = new HashSet<>();

    public GameBoard(List<HexType> hexTypeList) {
    	Random random = new Random();
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

            
        }

        for (int i = 0; i < waterCoords.length; i++) {
            int[] coord = waterCoords[i];
            Hex tile = new Hex(HexType.WATER , 0, new HexPosition(coord[0], coord[1]));
            hexes.add(tile);
            spawn("hexagon", tile.getSpawnData());
        }

        for (Hex hex : hexes) {
        	calculateCornersAndEdgesForHex(hex);
        }
        
        
        for (HexEdge edge: hexEdges) {
    		edge.visualizeEdge(Color.WHITE);
    	}
        for (HexCorner corner : hexCorners) {
            corner.visualizeCorner(Color.WHITE);
        }
        
        List<HexEdge> l = new ArrayList<>(hexEdges);
	    Player owner = new Player("Jonas");
	    Road r = new Road(l.get(3), owner);
	    r.visualize();
	    Building b = new City(l.get(3).getCorners()[0], owner);
	    b.visualize();
	    
    }

    private void calculateCornersAndEdgesForHex(Hex hex) {
    	HexPosition pos = hex.getPosition();
        int q = pos.getQ();
        int r = pos.getR();

        // Richtungen im Uhrzeigersinn + Wiederholung der ersten
        int[][] adjazenz = {
            { 0, -1 }, { 1, -1 }, { 1, 0 },
            { 0, 1 }, { -1, 1 }, { -1, 0 },
            { 0, -1 } // Wiederholung für einfaches Paar
        };

        HexCorner[] corners = new HexCorner[6];
        
        for (int i = 0; i < 6; i++) {
            int[] off1 = adjazenz[i];
            int[] off2 = adjazenz[i + 1];

            HexPosition pos1 = new HexPosition(q + off1[0], r + off1[1]);
            HexPosition pos2 = new HexPosition(q + off2[0], r + off2[1]);

            Hex h1 = getHexByPosition(pos1);
            Hex h2 = getHexByPosition(pos2);
			

			if (h1 != null && h2 != null) {
				HexCorner corner = new HexCorner(hex, h1, h2);
				hexCorners.add(corner);
				corners[i] = corner;
			}
        }
        
        for (int i = 0; i < 6; i++) {
            HexCorner c1 = corners[i];
            HexCorner c2 = corners[(i + 1) % 6]; // Ringstruktur

            if (c1 != null && c2 != null) {
            	switch(i % 3) {
                case 0:
                    hexEdges.add(new HexEdge(c1, c2, HexEdgeOrientation.LEFT_TO_RIGHT));
                    break;
                case 1:
                    hexEdges.add(new HexEdge(c1, c2, HexEdgeOrientation.STRAIGHT));
                    break;
                case 2:
                    hexEdges.add(new HexEdge(c1, c2, HexEdgeOrientation.RIGHT_TO_LEFT));
                    break;
            }

                hexEdges.add(new HexEdge(c1, c2, HexEdgeOrientation.LEFT_TO_RIGHT));
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

