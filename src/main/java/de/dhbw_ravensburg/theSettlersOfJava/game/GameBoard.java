package de.dhbw_ravensburg.theSettlersOfJava.game;

import static com.almasb.fxgl.dsl.FXGL.spawn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.almasb.fxgl.dsl.FXGL;

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
import de.dhbw_ravensburg.theSettlersOfJava.units.Robber;
import javafx.scene.paint.Color;

public class GameBoard {

    private static final int[][] coords = {
        {0,-2},{-2,0},{-1,-1},{0,-1},{-1,0},{0,0},{0,2},{2,0},{1,1},{0,1},{1,0},{-1,1},{1,-1},{-1,2},{2,-1},{-2,1},{-2,2},{1,-2},{2,-2}
    };
    
    private static final int[][] waterCoords = {
        {-3,0},{-2,-1},{-1,-2},{0,-3},{1,-3},{2,-3},{3,-3},{3,-2},{3,-1},
        {3,0},{2,1},{1,2},{0,3},{-1,3},{-2,3},{-3,3},{-3,2},{-3,1}
    };

    private Set<Hex> hexes = new HashSet<>();
    private Set<HexCorner> hexCorners = new HashSet<>();
    private Set<HexEdge> hexEdges = new HashSet<>();
    private Set<Building> buildings = new HashSet<>();
    private Set<Road> roads = new HashSet<>();
    private Robber robber;

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
            
            if (type == HexType.DESERT) {
            	this.robber = new Robber(tile);
            }
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
        /*
         * Test Code for Developmenty
         */
        /*
        List<HexEdge> l = new ArrayList<>(hexEdges);
	    Player owner = new Player("Jonas");
	    roads.add(new Road(l.get(3), owner));
	    buildings.add(new City(l.get(3).getCorners()[0], owner));
	    
	    buildings.add(new City(l.get(3).getCorners()[1],owner));
	    */
	    robber.visualize();
	    
	    
        for (HexEdge edge: hexEdges) {
    		edge.visualizeEdge(Color.WHITE);
    	}
        for (HexCorner corner : hexCorners) {
            corner.visualizeCorner(Color.WHITE);
        }
        for (Road r : roads) {
        	r.visualize();
        }
	    for(Building b : buildings) {
	    	b.visualize();
	    }
    }
    
    public boolean buildRoad(Road road) {
        // Check if there's already a road at this location to prevent duplicate roads
        for (Road existingRoad : roads) {
            if (existingRoad.getLocation().equals(road.getLocation())) {
                FXGL.getDialogService().showMessageBox("Road already exists at this location!");
                return false;
            }
        }

        HexEdge roadLocation = road.getLocation();
        Player owner = road.getOwner();
        boolean isAdjacentToStructure = false;

        // Check adjacency with existing buildings
        for (Building building : buildings) {
            HexCorner buildingCorner = building.getLocation();
            if(building.getOwner().equals(owner)) {
	            if (roadLocation.isAdjacentToCorner(buildingCorner)) {
	                isAdjacentToStructure = true;
	                break;
	            }
            }
        }

        // Check adjacency with existing roads
        if (!isAdjacentToStructure) {
            for (Road existingRoad : roads) {
	            if (existingRoad.getOwner().equals(owner) && roadLocation.isAdjacentTo(existingRoad.getLocation())) {
	                 isAdjacentToStructure = true;
	                 break;
	            }
	        }
        }

        // If the road is not adjacent to any building or roadway, construction is not allowed
        if (!isAdjacentToStructure) {
            FXGL.getDialogService().showMessageBox("Road must be built adjacent to an existing building or road!");
            return false;
        }

        // Add the road if all checks pass
        roads.add(road);
        road.visualize();
        return true;
    }
    
    public boolean buildBuilding(Building building) {
    	    HexCorner corner = building.getLocation();
    	    Player owner = building.getOwner();
    	    List<HexCorner> adjacentList = new ArrayList<>();
    	    boolean upgradeToCity = false;
    	    
    	    // Erstellen der Liste der Ecken, die an die übergebene Ecke angrenzen
    	    for (HexEdge edge : hexEdges) {
    	        HexCorner[] corners = edge.getCorners();
    	        if (corners[0].equals(corner)) {
    	            adjacentList.add(corners[1]);
    	        } else if (corners[1].equals(corner)) {
    	            adjacentList.add(corners[0]);
    	        }
    	    }
    	    
    	    // Überprüfen, ob es bereits ein Gebäude an der Ecke gibt oder ob ein angrenzendes Gebäude vorhanden ist
    	    for (Building existingBuilding : buildings) {
    	        HexCorner buildingLocation = existingBuilding.getLocation();
    	        if (buildingLocation.equals(corner)) {
    	        	if (existingBuilding.getOwner().equals(owner)) {
    	        		if(existingBuilding instanceof Settlement &&
    	        		    building instanceof City) {
    	        			buildings.remove(existingBuilding);
    	        			buildings.add(building);
    	        			building.visualize();
    	        			existingBuilding.getEntity().removeFromWorld();
    	        			return true;
    	        			
    	        		} else {
    	        			return false;
    	        		}
    	        	}
    	        }
    	        if (adjacentList.contains(buildingLocation)) {
    	            FXGL.getDialogService().showMessageBox("Kein Gebäudebau möglich - zu nahe an einem anderen Gebäude!");
    	            return false; // Ein angrenzendes Gebäude verhindert den Bau
    	        }
    	    }

    	    // Zusätzliche Überprüfung: Wenn bereits zwei oder mehr Gebäude in der Liste sind,
    	    // prüfe, ob es eine anliegende Straße des gleichen Besitzers gibt.
    	    // TODO: Anpassen an die Owner mit der Anzahl an Gebäuden
    	    if (buildings.size() >= 2) {
    	        boolean hasAdjacentRoad = false;
    	        for (Road road : roads) {
    	            HexCorner[] corners = road.getLocation().getCorners();
    	            if ((corners[0].equals(corner) || corners[1].equals(corner)) && road.getOwner().equals(owner)) {
    	                hasAdjacentRoad = true;
    	                break;
    	            }
    	        }
    	        if (!hasAdjacentRoad) {
    	            FXGL.getDialogService().showMessageBox("Kein Gebäudebau möglich - es muss eine anliegende Straße des gleichen Besitzers geben!");
    	            return false;
    	        }
    	    }

    	    // Wenn alle Überprüfungen bestanden sind, füge das Gebäude hinzu und visualisiere es

        	buildings.add(building);
        	building.visualize();
    	    
    	    return true;
    }
    
    public HexCorner[] getConnectedCorners(HexCorner corner) {
        List<HexCorner> connectedCorners = new ArrayList<>();

        // Iterate through each edge to find those connected to our given corner
        for (HexEdge edge : hexEdges) {
            // If the edge is connected to the given corner, add both corners of the edge
            if (edge.getCorners()[0].equals(corner) || edge.getCorners()[1].equals(corner)) {
                // Add the corner from the edge that isn't the input corner
                if (!edge.getCorners()[0].equals(corner)) {
                    connectedCorners.add(edge.getCorners()[0]);
                }
                if (!edge.getCorners()[1].equals(corner)) {
                    connectedCorners.add(edge.getCorners()[1]);
                }
            }
        }

        // Return as an array
        return connectedCorners.toArray(new HexCorner[0]);
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
            	HexEdgeOrientation orientation = null;
            	switch(i % 3) {
                case 0:
                	orientation = HexEdgeOrientation.LEFT_TO_RIGHT;
                    break;
                case 1:
                	orientation = HexEdgeOrientation.STRAIGHT;
                    break;
                case 2:
                    orientation = HexEdgeOrientation.RIGHT_TO_LEFT;
                    break;
            	}
            	hexEdges.add(new HexEdge(c1, c2, orientation));
            	

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

