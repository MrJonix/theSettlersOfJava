package de.dhbw_ravensburg.theSettlersOfJava.game;

import static com.almasb.fxgl.dsl.FXGL.spawn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.dhbw_ravensburg.theSettlersOfJava.map.Hex;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexPosition;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;


public class GameBoard {

	private static final int[][] coords = {{0,-2},{-2,0},{-1,-1},{0,-1},{-1,0},{0,0},{0,2},{2,0},{1,1},{0,1},{1,0},{-1,1},{1,-1},{-1,2},{2,-1},{-2,1},{-2,2},{1,-2},{2,-2}};
	private static final int[][] waterCoords = {{-3,0},{-2,-1},{-1,-2},{0,-3},{1,-3},{2,-3},{3,-3},{3,-2},{3,-1},{3,0},{2,1},{1,2},{0,3},{-1,3},{-2,3},{-3,3},{-3,2},{-3,1}};
	
	private List<Hex> hexes = new ArrayList<>();   
	private Random random = new Random();
	
	public GameBoard() {
		List<HexType> hexTypeList = HexType.generateHexTypeList();
		System.out.println();
        for (int i = 0; i < coords.length; i++) {
            int[] coord = coords[i];
            HexType type = hexTypeList.get(i);

            int number = 0;
            do {
            	number = random.nextInt(2,13);
            } while (number == 7);
            
            if (type == HexType.DESERT) {
                number = 0; // Dessert bekommt keine Zahl (0)
            }
            
            Hex tile = new Hex(type , number, new HexPosition(coord[0], coord[1]));
            hexes.add(tile);
            spawn("hexagon", tile.getSpawnData());
        }
        
        for (int i = 0; i < waterCoords.length; i++) {
        	int[] coord = waterCoords[i];
        	Hex tile = new Hex(HexType.WATER , 0, new HexPosition(coord[0], coord[1]));
        	spawn("hexagon", tile.getSpawnData());
        	
        }
	}
}
 