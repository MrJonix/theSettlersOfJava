package de.dhbw_ravensburg.theSettlersOfJava.game;

import static com.almasb.fxgl.dsl.FXGL.spawn;

import java.util.*;
import java.util.stream.Collectors;
import com.almasb.fxgl.dsl.FXGL;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.*;
import de.dhbw_ravensburg.theSettlersOfJava.map.*;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import de.dhbw_ravensburg.theSettlersOfJava.units.Robber;
import javafx.scene.paint.Color;

public class GameBoard {

	private static final int[][] coords = {
			{0, -2},
		    {-1, -1},
		    {-2, 0},
		    {-2, 1},
		    {-2, 2},
		    {-1, 2},
		    {0, 2},
		    {1, 1},
		    {2, 0},
		    {2, -1},
		    {2, -2},
		    {1, -2},
		   //Border end
		    {0, -1},
		    {-1, 0},
		    {-1, 1},
		    {0, 1},
		    {1, 0},
		    {1, -1},
		    {0, 0}
		};

    private static final int[] numberTokens = {
    	    5,  // A
    	    2,  // B
    	    6,  // C
    	    3,  // D
    	    8,  // E
    	    10, // F
    	    9,  // G
    	    12, // H
    	    11, // I
    	    4,  // J
    	    8,  // K
    	    10, // L
    	    9,  // M
    	    4,  // N
    	    5,  // O
    	    6,  // P
    	    3,  // Q
    	    11  // R
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
    private Building setupBuilding;

    public GameBoard(List<HexType> hexTypeList) {
        initializeHexes(hexTypeList);
        initializeWaterTiles();
        hexes.forEach(this::calculateCornersAndEdgesForHex);
        robber.visualize();
        visualizeEdgesAndCorners();
    }

    private void initializeHexes(List<HexType> hexTypeList) {
        Random random = new Random();
        boolean desert = false;
        int startingPosition = random.nextInt(5);

        for (int i = 0; i < coords.length; i++) {
            int[] coord = coords[i];

            // Rotate the coordinate 'startingPosition' times
            for (int j = 0; j < startingPosition; j++) {
                coord = rotateCCW60(coord);
            }

            HexType type = hexTypeList.get(i);
            int number = -1; // Initialize with a default invalid number

            if (type.equals(HexType.DESERT)) {
                number = 0; // Desert does not have a number
                desert = true;
            } else {
                number = desert ? numberTokens[i - 1] : numberTokens[i]; // Adjust for the shifted index due to desert
            }

            Hex tile = new Hex(type, number, new HexPosition(coord[0], coord[1]));
            if (type.equals(HexType.DESERT)) {
                robber = new Robber(tile);
            }
            hexes.add(tile);
            spawn("hexagon", tile.getSpawnData());
        }
    }

    private static int[] rotateCCW60(int[] coord) {
        int newQ = -coord[1];
        int newR = coord[0] + coord[1];
        return new int[]{newQ, newR};
    }

    private void initializeWaterTiles() {
        Arrays.stream(waterCoords).forEach(coord -> {
            Hex tile = new Hex(HexType.WATER, 0, new HexPosition(coord[0], coord[1]));
            hexes.add(tile);
            spawn("hexagon", tile.getSpawnData());
        });
    }

    private void visualizeEdgesAndCorners() {
        hexEdges.forEach(edge -> edge.visualizeEdge(Color.WHITE));
        hexCorners.forEach(corner -> corner.visualizeCorner(Color.WHITE));
        roads.forEach(Road::visualize);
        buildings.forEach(Building::visualize);
    }

    public boolean buildRoad(Road road) {
        if (roads.stream().anyMatch(existingRoad -> existingRoad.getLocation().equals(road.getLocation()))) {
            FXGL.getDialogService().showMessageBox("Road already exists at this location!");
            return false;
        }

        if (!isAdjacentToPlayerStructure(road.getLocation(), road.getOwner())) {
            FXGL.getDialogService().showMessageBox("Road must be built adjacent to an existing building or road!");
            return false;
        }
        
        if(App.getGameController().getCurrentGameState().equals(GameState.SETUP_PHASE)) {
        	HexCorner[] corners = road.getLocation().getCorners();
        	if (!(corners[0].equals(setupBuilding.getLocation()) || corners[1].equals(setupBuilding.getLocation()))){
        		FXGL.getDialogService().showMessageBox("Road musst be adjacent to a your placed building");
        		return false;
        	}
        	
        	for(HexEdge e : hexEdges) {
        		e.removeHighlight();
        	}
        	
        	App.getGameController().finishedPlayerSetup(road.getOwner());
        }
        
        roads.add(road);
        road.getOwner().longestRoadProperty().set(getLongestRoadLength(road.getOwner()));;
        road.visualize();
        return true;
    }

    private boolean isAdjacentToPlayerStructure(HexEdge roadLocation, Player owner) {
        return buildings.stream().anyMatch(building -> 
                    building.getOwner().equals(owner) && roadLocation.isAdjacentToCorner(building.getLocation())) 
                ||
                roads.stream().anyMatch(existingRoad -> 
                    existingRoad.getOwner().equals(owner) && roadLocation.isAdjacentTo(existingRoad.getLocation()));
    }

    public boolean buildBuilding(Building building) {
        HexCorner corner = building.getLocation();
        Player owner = building.getOwner();
        if (isExistingBuildingBlocking(corner, owner, building) || isTooNearOtherBuilding(corner)) {
            return false;
        }

        if (!isPlayerAllowedToBuild(corner, owner)) {
            return false;
        }

        buildings.add(building);
        building.visualize();

        if(App.getGameController().getCurrentGameState().equals(GameState.SETUP_PHASE)) {
            for (HexCorner c : hexCorners) {
            	c.removeHighlight();
            }
            buildSetupRoad(corner);
            
            if (!App.getGameController().getFirstSetup()) {
                for (Hex h : corner.getAdjacentHexes()) {
                	owner.addResources(h.getHexType().getResourceType(), 1);
                	
                }
            }
            setupBuilding = building;

        }
        
        return true;
    }
    
	private void buildSetupRoad(HexCorner loc) {

		for(HexEdge e : hexEdges) {
			HexCorner[] corners = e.getCorners();
			if(corners[0].equals(loc) || corners[1].equals(loc)) {
				e.highlight();
			}
		}
		
	}

    private boolean isExistingBuildingBlocking(HexCorner corner, Player owner, Building building) {
        for (Building existingBuilding : buildings) {
            HexCorner buildingLocation = existingBuilding.getLocation();
            if (buildingLocation.equals(corner) && existingBuilding.getOwner().equals(owner)) {
                if (existingBuilding instanceof Settlement) {
                    // Check if we are trying to build a City on top of an existing Settlement
                    if (building instanceof City) {
                        upgradeToCity(existingBuilding, building);
                        return false; // Return false because we can upgrade and the caller doesn't need to block this action
                    }
                }
                return true; // There's a building on this corner by the same player, and it's not an upgrade scenario
            }
        }
        return false; // No building blocks this action
    }

    private void upgradeToCity(Building existingBuilding, Building newBuilding) {
        buildings.remove(existingBuilding);
        buildings.add(newBuilding);
        newBuilding.visualize();
        existingBuilding.getEntity().removeFromWorld();
    }

    private boolean isTooNearOtherBuilding(HexCorner corner) {
        return getConnectedCorners(corner).stream().anyMatch(adjacentCorner -> 
            buildings.stream().anyMatch(building -> building.getLocation().equals(adjacentCorner)));
    }

    private boolean isPlayerAllowedToBuild(HexCorner corner, Player owner) {
        if (getBuildingsFromPlayer(owner).size() >= 2) {
            return roads.stream().anyMatch(road -> 
                road.getOwner().equals(owner) && Arrays.asList(road.getLocation().getCorners()).contains(corner));
        }
        return true;
    }

    public List<HexCorner> getConnectedCorners(HexCorner corner) {
        return hexEdges.stream()
            .filter(edge -> Arrays.asList(edge.getCorners()).contains(corner))
            .flatMap(edge -> Arrays.stream(edge.getCorners()))
            .filter(connectedCorner -> !connectedCorner.equals(corner))
            .collect(Collectors.toList());
    }
    
    public List<HexCorner> getPossibleStartPositions() {
        List<HexCorner> list = new ArrayList<>();
        for (HexCorner c : hexCorners) {
            boolean isBlocked = false;

            // Check c and all connected corners for existing buildings
            for (HexCorner n : getConnectedCorners(c)) {
                for (Building b : buildings) {
                    if (b.getLocation().equals(c) || b.getLocation().equals(n)) {
                        isBlocked = true;
                        break;
                    }
                }
                if (isBlocked) break;
            }

            if (!isBlocked) {
                list.add(c);
            }
        }
        return list;
    }


    public void distributeResources(int total) {
        hexes.stream()
            .filter(hex -> hex.getNumberToken() == total && !hex.equals(robber.getLocation()))
            .forEach(this::distributeResourcesForHex);
    }

    private void distributeResourcesForHex(Hex hex) {
        ResourceType resource = hex.getHexType().getResourceType();
        if (resource == null) return;
       
        hex.getAdjacentHexCorners().stream()
            .flatMap(corner -> buildings.stream()
                .filter(building -> building.getLocation().equals(corner)))
            .forEach(building -> giveResourcesToPlayer(resource, building));
    }

    private void giveResourcesToPlayer(ResourceType resource, Building building) {
        Player owner = building.getOwner();
        if (owner == null) return;
        int amount = building.getVictoryPoints();
        owner.addResources(resource, amount);
        System.out.println(owner.getName() + " erhält " + amount + "x " + resource);
    }

    private void calculateCornersAndEdgesForHex(Hex hex) {
        HexPosition pos = hex.getPosition();
        int q = pos.getQ();
        int r = pos.getR();

        // Directions in clockwise + repetition of the first
        int[][] adjazenz = {
            { 0, -1 }, { 1, -1 }, { 1, 0 },
            { 0, 1 }, { -1, 1 }, { -1, 0 },
            { 0, -1 }
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
                HexCorner corner = createAndAddHexCorner(hex, h1, h2);
                corners[i] = corner;
                hex.setAdjacentCorner(corner, i);
            }
        }
        createAndAddHexEdges(corners);
    }

    private HexCorner createAndAddHexCorner(Hex hex, Hex h1, Hex h2) {
        HexCorner corner = new HexCorner(hex, h1, h2);
        hexCorners.add(corner);
        return corner;
    }

    private void createAndAddHexEdges(HexCorner[] corners) {
        for (int i = 0; i < 6; i++) {
            HexCorner corner1 = corners[i];
            HexCorner corner2 = corners[(i + 1) % 6];
            
            if (corner1 != null && corner2 != null) {
                HexEdgeOrientation orientation;
                switch (i % 3) {
                    case 0:
                        orientation = HexEdgeOrientation.LEFT_TO_RIGHT;
                        break;
                    case 1:
                        orientation = HexEdgeOrientation.STRAIGHT;
                        break;
                    case 2:
                        orientation = HexEdgeOrientation.RIGHT_TO_LEFT;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + (i % 3));
                }
                HexEdge edge = new HexEdge(corner1, corner2, orientation);
                hexEdges.add(edge);
            }
        }
    }
    
    public List<Player> getPlayersAdjacentToHex(Hex hex) {
        // Sammle alle Spieler, die an den Ecken angrenzen, an denen Gebäude stehen
        return buildings.stream()
            // Filtere Gebäude, die an dem Hex angrenzen
            .filter(building -> building.getLocation().getAdjacentHexes().contains(hex))
            // Hole die Besitzer der Gebäude
            .map(Building::getOwner)
            // Entferne Duplikate
            .distinct()
            // Nur Spieler zurückgeben, die nicht null sind (Sicherheit)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public Hex getHexByPosition(HexPosition pos) {
        return hexes.stream().filter(hex -> hex.getPosition().equals(pos)).findFirst().orElse(null);
    }

    private Set<Building> getBuildingsFromPlayer(Player player) {
        return buildings.stream().filter(building -> building.getOwner().equals(player)).collect(Collectors.toSet());
    }

    public Robber getRobber() {
        return robber;
    }
    
    private Map<HexCorner, List<HexCorner>> buildPlayerRoadGraph(Player player) {
        Map<HexCorner, List<HexCorner>> graph = new HashMap<>();

        for (Road road : roads) {
            if (!road.getOwner().equals(player)) continue;

            HexEdge edge = road.getLocation();
            HexCorner[] corners = edge.getCorners();

            // Verhindere, dass eine gegnerische Siedlung den Weg blockiert
            boolean blocked = buildings.stream().anyMatch(b -> {
                return !b.getOwner().equals(player) &&
                        (b.getLocation().equals(corners[0]) || b.getLocation().equals(corners[1]));
            });
            if (blocked) continue;

            graph.computeIfAbsent(corners[0], k -> new ArrayList<>()).add(corners[1]);
            graph.computeIfAbsent(corners[1], k -> new ArrayList<>()).add(corners[0]);
        }

        return graph;
    }
    
    private int findLongestPath(Map<HexCorner, List<HexCorner>> graph) {
        int maxLength = 0;
        Set<HexCorner> visited = new HashSet<>();

        for (HexCorner corner : graph.keySet()) {
            maxLength = Math.max(maxLength, dfs(corner, graph, visited));
        }

        return maxLength;
    }

    private int dfs(HexCorner current, Map<HexCorner, List<HexCorner>> graph, Set<HexCorner> visited) {
        visited.add(current);
        int maxDepth = 0;

        for (HexCorner neighbor : graph.getOrDefault(current, Collections.emptyList())) {
            if (!visited.contains(neighbor)) {
                maxDepth = Math.max(maxDepth, dfs(neighbor, graph, visited));
            }
        }

        visited.remove(current); // Backtracking!
        return maxDepth + 1;
    }
    
    public int getLongestRoadLength(Player player) {
        Map<HexCorner, List<HexCorner>> roadGraph = buildPlayerRoadGraph(player);
        int longest = 0;

        for (HexCorner start : roadGraph.keySet()) {
            longest = Math.max(longest, dfs(start, roadGraph, new HashSet<>()) - 1);
        }

        return longest;
    }




}