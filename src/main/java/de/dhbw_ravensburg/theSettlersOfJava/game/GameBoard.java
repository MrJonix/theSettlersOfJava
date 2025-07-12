package de.dhbw_ravensburg.theSettlersOfJava.game;

import static com.almasb.fxgl.dsl.FXGL.spawn;

import java.util.*;
import java.util.stream.Collectors;
import com.almasb.fxgl.dsl.FXGL;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.*;
import de.dhbw_ravensburg.theSettlersOfJava.map.*;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HarborType;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import de.dhbw_ravensburg.theSettlersOfJava.units.Robber;
import javafx.scene.paint.Color;


/**
 * Represents the full game board of Settlers of Catan.
 *
 * Responsibilities:
 * - Initialize and rotate land and water hex tiles
 * - Generate and assign number tokens and the robber
 * - Place and validate buildings and roads
 * - Handle resource distribution and setup phase
 * - Visualize all board components (hexes, edges, corners, harbors)
 * - Evaluate longest road and harbor access
 */
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
    
    private static final HarborOrientation[] harborOrientations = {
    	    HarborOrientation.MIDDLE_RIGHT,
    	    HarborOrientation.BOTTOM_RIGHT,
    	    HarborOrientation.BOTTOM_LEFT,
    	    HarborOrientation.BOTTOM_LEFT,
    	    HarborOrientation.BOTTOM_LEFT,
    	    HarborOrientation.MIDDLE_LEFT,
    	    HarborOrientation.TOP_LEFT,
    	    HarborOrientation.TOP_LEFT,
    	    HarborOrientation.TOP_RIGHT,
    	};


    private Set<Hex> hexes = new HashSet<>();
    private Set<HexCorner> hexCorners = new HashSet<>();
    private Set<HexEdge> hexEdges = new HashSet<>();
    private Set<Building> buildings = new HashSet<>();
    private Set<Road> roads = new HashSet<>();
    private Set<Harbor> harbors = new HashSet<>();
    private Robber robber;
    private Building setupBuilding;

    /**
     * Constructs the game board by initializing hexes, water tiles, edges, corners,
     * and the robber. Also sets up harbor tiles and visualizes all necessary components.
     *
     * @param hexTypeList the list of hex types to populate the board tiles with
     */
    public GameBoard(List<HexType> hexTypeList) {
        initializeHexes(hexTypeList);
        initializeWaterTiles();
        hexes.forEach(this::calculateCornersAndEdgesForHex);
        robber.visualize();
        visualizeEdgesAndCorners();
        initalizeHarbors();
   }
    
    /**
     * Initializes and places harbors on the game board using predefined water coordinates
     * and orientations. Assigns each harbor a random trade type and visualizes it on the map.
     */
    private void initalizeHarbors() {
    		List<HarborType> types = HarborType.generateHarborTypes();
        for (int i = 0; i < 18; i = i +2) {
            int q = waterCoords[i][0];
            int r = waterCoords[i][1];
            HarborOrientation orientation = harborOrientations[i/2];

            // Nur weiter machen, wenn eine gültige Orientierung vorhanden ist
            if (orientation == null) {
                continue;
            }

            HexPosition harborPosition = new HexPosition(q, r);
            HexEdge harborEdge = getHarborPosition(harborPosition, orientation);

            if (harborEdge == null) {
               continue;
            }

            Harbor harbor = new Harbor(harborEdge, types.get(i/2),getHexByPosition(harborPosition),orientation); // oder spezifischer Typ je nach Bedarf
           
            harbors.add(harbor);
        }
    }

    
    /**
     * Initializes the land hex tiles on the game board using the given hex types.
     * Applies a random rotation to the layout and assigns number tokens.
     * The desert tile is excluded from numbering and is used to place the robber.
     *
     * @param hexTypeList the shuffled list of hex types used to populate the board
     */
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
                number = desert ? numberTokens[i - 1] : numberTokens[i]; // Add edge to graph (enemy blocks are handled during DFS traversal)

            }

            Hex tile = new Hex(type, number, new HexPosition(coord[0], coord[1]));
            if (type.equals(HexType.DESERT)) {
                robber = new Robber(tile);
            }
            hexes.add(tile);
            spawn("hexagon", tile.getSpawnData());
        }
    }

    /**
     * Rotates a hex coordinate 60 degrees counter-clockwise in axial coordinate system.
     *
     * @param coord the original axial coordinate [q, r]
     * @return the rotated coordinate after applying 60° CCW transformation
     */
    private static int[] rotateCCW60(int[] coord) {
        int newQ = -coord[1];
        int newR = coord[0] + coord[1];
        return new int[]{newQ, newR};
    }

    /**
     * Initializes and spawns water hex tiles around the game board.
     * These tiles form the border and are visually rendered as water.
     */
    private void initializeWaterTiles() {
        Arrays.stream(waterCoords).forEach(coord -> {
            Hex tile = new Hex(HexType.WATER, 0, new HexPosition(coord[0], coord[1]));
            hexes.add(tile);
            spawn("hexagon", tile.getSpawnData());
        });
        
    }

    /**
     * Visualizes all structural components of the game board.
     * This includes hex edges, corners, roads, and buildings.
     * Used after initialization or to refresh the board view.
     */
    private void visualizeEdgesAndCorners() {
        hexEdges.forEach(edge -> edge.visualizeEdge(Color.WHITE));
        hexCorners.forEach(corner -> corner.visualizeCorner(Color.WHITE));
        roads.forEach(Road::visualize);
        buildings.forEach(Building::visualize);
    }

    /**
     * Attempts to build a road at the given location for the specified player.
     * Validates placement rules, including adjacency to the player's existing structures.
     * In the setup phase, ensures the road is connected to the player's placed building.
     *
     * @param road the road to be built
     * @return true if the road was successfully built, false otherwise
     */
    public boolean buildRoad(Road road) {
        if (roads.stream().anyMatch(existingRoad -> existingRoad.getLocation().equals(road.getLocation()))) {
            FXGL.getDialogService().showMessageBox("Road already exists at this location!");
            return false;
        }

        if (!isAdjacentToPlayerStructure(road.getLocation(), road.getOwner())) {
            FXGL.getDialogService().showMessageBox("Road must be adjacent to your palced building!");
            return false;
        }
        
        if(App.getGameController().getCurrentGameState().equals(GameState.SETUP_PHASE)) {
        	HexCorner[] corners = road.getLocation().getCorners();
        	if (!(corners[0].equals(setupBuilding.getLocation()) || corners[1].equals(setupBuilding.getLocation()))){
        		FXGL.getDialogService().showMessageBox("Road must be adjacent to a your placed building");
        		return false;
        	}
        	
        	for(HexEdge e : hexEdges) {
        		e.removeHighlight();
        	}
        	
        	App.getGameController().finishedPlayerSetup(road.getOwner());
        }
        
        roads.add(road);
        App.getGameController().setAllPlayersLongestRoad();
        road.visualize();
        return true;
    }

    /**
     * Checks whether the specified road location is adjacent to an existing structure
     * (road or building) owned by the given player.
     *
     * @param roadLocation the hex edge where the road is intended to be placed
     * @param owner the player attempting to build the road
     * @return true if the road is adjacent to a player's existing building or road, false otherwise
     */
    private boolean isAdjacentToPlayerStructure(HexEdge roadLocation, Player owner) {
        return buildings.stream().anyMatch(building -> 
                    building.getOwner().equals(owner) && roadLocation.isAdjacentToCorner(building.getLocation())) 
                ||
                roads.stream().anyMatch(existingRoad -> 
                    existingRoad.getOwner().equals(owner) && roadLocation.isAdjacentTo(existingRoad.getLocation()));
    }

    /**
     * Attempts to build a building at the specified location.
     * Validates placement rules, including distance to other buildings, player eligibility, and setup-phase behavior.
     * If valid, adds the building to the board and visualizes it.
     *
     * @param building the building to be placed on the board
     * @return true if the building was successfully built, false otherwise
     */
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
        App.getGameController().setAllPlayersLongestRoad();
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
    
    /**
     * Highlights all adjacent hex edges around the specified corner during the setup phase.
     * This allows the player to select a valid edge for placing the initial road.
     *
     * @param loc the corner where the initial settlement was placed
     */
	private void buildSetupRoad(HexCorner loc) {

		for(HexEdge e : hexEdges) {
			HexCorner[] corners = e.getCorners();
			if(corners[0].equals(loc) || corners[1].equals(loc)) {
				e.highlight();
			}
		}
		
	}

	/**
	 * Checks if a building already exists at the specified corner that would block the new building.
	 * If the existing building is a settlement owned by the same player and the new building is a city,
	 * it triggers an upgrade instead of blocking.
	 *
	 * @param corner   the location to check
	 * @param owner    the owner attempting to build
	 * @param building the new building to be placed
	 * @return true if the location is blocked by an existing building (not upgradable), false otherwise
	 */
    private boolean isExistingBuildingBlocking(HexCorner corner, Player owner, Building building) {
        for (Building existingBuilding : buildings) {
            HexCorner buildingLocation = existingBuilding.getLocation();
            if (buildingLocation.equals(corner) && existingBuilding.getOwner().equals(owner)) {
                if (existingBuilding instanceof Settlement) {
                    // Check if we are trying to build a City on top of an existing Settlement
                    if (building instanceof City) {
                        upgradeToCity(existingBuilding, building);
                        return false; // Return false to indicate no conflict,upgrade is allowed and handled internally
                    }
                }
                return true; // There's a building on this corner by the same player, and it's not an upgrade scenario
            }
        }
        return false; // No building blocks this action
    }

    /**
     * Replaces an existing settlement with a new city at the same location.
     * The old building is removed from the world and the new city is visualized and added to the board.
     *
     * @param existingBuilding the existing settlement to be replaced
     * @param newBuilding      the new city building that will replace the settlement
     */
    private void upgradeToCity(Building existingBuilding, Building newBuilding) {
        buildings.remove(existingBuilding);
        buildings.add(newBuilding);
        newBuilding.visualize();
        existingBuilding.getEntity().removeFromWorld();
    }

    /**
     * Checks if the specified hex corner is too close to any existing building.
     * According to the game rules, buildings must not be placed directly adjacent to each other.
     *
     * @param corner the corner to check
     * @return true if there is another building on any connected corner, false otherwise
     */
    private boolean isTooNearOtherBuilding(HexCorner corner) {
        return getConnectedCorners(corner).stream().anyMatch(adjacentCorner -> 
            buildings.stream().anyMatch(building -> building.getLocation().equals(adjacentCorner)));
    }

    /**
     * Checks whether the given player is allowed to build at the specified corner.
     * In regular gameplay, a building must be connected to one of the player's roads
     * unless it's one of the first two buildings during the setup phase.
     *
     * @param corner the hex corner where the player wants to build
     * @param owner the player attempting to build
     * @return true if the player is allowed to build at the specified corner, false otherwise
     */
    private boolean isPlayerAllowedToBuild(HexCorner corner, Player owner) {
        if (getBuildingsFromPlayer(owner).size() >= 2) {
            return roads.stream().anyMatch(road -> 
                road.getOwner().equals(owner) && Arrays.asList(road.getLocation().getCorners()).contains(corner));
        }
        return true;
    }

    /**
     * Returns a list of all hex corners directly connected to the given corner
     * via existing hex edges. This represents the immediate neighboring corners.
     *
     * @param corner the corner for which to find connected neighbors
     * @return a list of adjacent hex corners
     */
    public List<HexCorner> getConnectedCorners(HexCorner corner) {
        return hexEdges.stream()
            .filter(edge -> Arrays.asList(edge.getCorners()).contains(corner))
            .flatMap(edge -> Arrays.stream(edge.getCorners()))
            .filter(connectedCorner -> !connectedCorner.equals(corner))
            .collect(Collectors.toList());
    }
    
    /**
     * Determines all valid starting positions for new settlements during setup.
     * A valid position must not be adjacent to any existing buildings.
     *
     * @return a list of all available hex corners for initial settlement placement
     */
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


    /**
     * Distributes resources to players based on the rolled number.
     * Only hexes with the matching number token and not occupied by the robber are considered.
     *
     * @param total the number rolled by the dice
     */
    public void distributeResources(int total) {
        hexes.stream()
            .filter(hex -> hex.getNumberToken() == total && !hex.equals(robber.getLocation()))
            .forEach(this::distributeResourcesForHex);
    }

    /**
     * Distributes resources from a specific hex to all adjacent buildings.
     * Skips hexes without a resource type.
     *
     * @param hex the hex from which to distribute resources
     */
    private void distributeResourcesForHex(Hex hex) {
        ResourceType resource = hex.getHexType().getResourceType();
        if (resource == null) return;
       
        hex.getAdjacentHexCorners().stream()
            .flatMap(corner -> buildings.stream()
                .filter(building -> building.getLocation().equals(corner)))
            .forEach(building -> giveResourcesToPlayer(resource, building));
    }

    /**
     * Transfers resources to the owner of a building based on its type.
     * Settlements provide 1 resource, cities 2 resources.
     *
     * @param resource  the type of resource to be given
     * @param building  the building whose owner receives the resources
     */
    private void giveResourcesToPlayer(ResourceType resource, Building building) {
        Player owner = building.getOwner();
        if (owner == null) return;
        int amount = building.getVictoryPoints();
        owner.addResources(resource, amount);
    }

    /**
     * Calculates and assigns the six corners and edges for a given hex tile.
     * Corners are shared with adjacent hexes, and edges are formed between adjacent corners.
     *
     * @param hex the hex tile for which corners and edges are calculated
     */
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

    /**
     * Creates a HexCorner object representing the shared corner between the given three hexes
     * and adds it to the global set of hex corners.
     *
     * @param hex the main hex tile
     * @param h1 the first adjacent hex
     * @param h2 the second adjacent hex
     * @return the created HexCorner instance
     */
    private HexCorner createAndAddHexCorner(Hex hex, Hex h1, Hex h2) {
        HexCorner corner = new HexCorner(hex, h1, h2);
        hexCorners.add(corner);
        return corner;
    }

    /**
     * Creates and adds all six hex edges for a given array of corners.
     * Each edge connects two consecutive corners in the array.
     * Also assigns a visual orientation to each edge based on its position.
     *
     * @param corners an array of six HexCorner instances representing the corners of a hex tile
     */
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
    
    /**
     * Retrieves all players who own buildings adjacent to the given hex.
     * Used for determining possible resource distribution or trade access.
     *
     * @param hex the hex tile to check
     * @return list of players with adjacent buildings
     */
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

    /**
     * Retrieves the hex tile at the specified position.
     *
     * @param pos the position to look for
     * @return the hex located at the given position, or null if none exists
     */
    public Hex getHexByPosition(HexPosition pos) {
        return hexes.stream().filter(hex -> hex.getPosition().equals(pos)).findFirst().orElse(null);
    }

    /**
     * Returns all buildings owned by the specified player.
     *
     * @param player the player whose buildings are to be retrieved
     * @return a set of buildings owned by the player
     */
    private Set<Building> getBuildingsFromPlayer(Player player) {
        return buildings.stream().filter(building -> building.getOwner().equals(player)).collect(Collectors.toSet());
    }

    /**
     * Gets the robber instance currently on the game board.
     *
     * @return the robber object
     */
    public Robber getRobber() {
        return robber;
    }
    
    /**
     * Builds a graph representation of all roads owned by the specified player.
     * The graph connects corners with edges representing roads.
     *
     * @param player the player whose roads are used to build the graph
     * @return a map where each corner is connected to its adjacent corners by player's roads
     */
    private Map<HexCorner, Set<HexCorner>> buildPlayerRoadGraph(Player player) {
        Map<HexCorner, Set<HexCorner>> graph = new HashMap<>();

        for (Road r : roads) {
            if (!player.equals(r.getOwner())) continue;

            HexCorner[] corners = r.getLocation().getCorners();
            HexCorner node1 = corners[0];
            HexCorner node2 = corners[1];

            // Füge Kante hinzu (wir prüfen später auf gegnerische Blockaden)
            graph.computeIfAbsent(node1, k -> new HashSet<>()).add(node2);
            graph.computeIfAbsent(node2, k -> new HashSet<>()).add(node1);
        }

        return graph;
    }

    /**
     * Checks whether there is an enemy building at the given corner.
     *
     * @param corner the hex corner to check
     * @param currentPlayer the player to compare ownership against
     * @return true if a building from another player is located at the corner
     */
    private boolean hasEnemyBuilding(HexCorner corner, Player currentPlayer) {
        return buildings.stream()
            .anyMatch(b -> b.getLocation().equals(corner) && !b.getOwner().equals(currentPlayer));
    }

    /**
     * Calculates the longest continuous road for the given player using DFS.
     *
     * @param player the player whose roads to evaluate
     * @return the length of the longest road
     */
    public int getLongestRoadLength(Player player) {
        Map<HexCorner, Set<HexCorner>> roadGraph = buildPlayerRoadGraph(player);
        int longest = 0;

        for (HexCorner start : roadGraph.keySet()) {
        	// Fix not sure if right
            //if (hasEnemyBuilding(start, player)) continue;

            longest = Math.max(longest, dfsLongestPath(roadGraph, start, null, new HashSet<>(), player));
        }

        return longest;
    }

    /**
     * Recursive depth-first search to find the longest path in the player's road network.
     *
     * @param graph          the road graph of the player
     * @param current        the current hex corner in traversal
     * @param prev           the previous hex corner visited
     * @param visitedEdges   the set of visited edges (as unordered corner pairs)
     * @param player         the player owning the roads
     * @return the length of the longest found path from this node
     */
    private int dfsLongestPath(
        Map<HexCorner, Set<HexCorner>> graph,
        HexCorner current,
        HexCorner prev,
        Set<Set<HexCorner>> visitedEdges,
        Player player
    ) {
        int maxLength = 0;

        for (HexCorner neighbor : graph.getOrDefault(current, Collections.emptySet())) {
            Set<HexCorner> edge = Set.of(current, neighbor); // unordered

            if (visitedEdges.contains(edge)) continue;

            // Block if the neighbor corner contains an enemy building (except at endpoints)
            if (hasEnemyBuilding(neighbor, player) && graph.getOrDefault(neighbor, Collections.emptySet()).size() > 1) continue;

            visitedEdges.add(edge);
            int length = 1 + dfsLongestPath(graph, neighbor, current, visitedEdges, player);
            visitedEdges.remove(edge); // backtrack

            maxLength = Math.max(maxLength, length);
        }

        return maxLength;
    }

    /**
     * Finds the correct harbor edge on a given hex position and orientation, if it exists.
     *
     * @param pos the hex position containing the harbor
     * @param orientation the orientation of the harbor on the hex
     * @return the corresponding HexEdge if found; otherwise null
     */
    private HexEdge getHarborPosition(HexPosition pos, HarborOrientation orientation) {
        Hex hex = getHexByPosition(pos);
        HexCorner[] corners = hex.getAdjacentCornersArray();

        if (corners == null || corners.length < 6) {
            return null;
        }

        int i;
        switch (orientation) {
            case TOP_RIGHT:     i = 0; break;
            case MIDDLE_RIGHT:  i = 1; break;
            case BOTTOM_RIGHT:  i = 2; break;
            case BOTTOM_LEFT:   i = 3; break;
            case MIDDLE_LEFT:   i = 4; break;
            case TOP_LEFT:      i = 5; break;
            default:
                return null;
        }

        HexCorner c1 = corners[i];
        HexCorner c2 = corners[(i + 1) % 6];

        if (c1 == null || c2 == null) {
            return null;
        }
        
        HexEdge edge = new HexEdge(c1, c2, HexEdgeOrientation.LEFT_TO_RIGHT);

        for (HexEdge e : hexEdges) {
            if (e.equals(edge)) {
                return e;
            }
        }

        return null;
    }
    
    /**
     * Returns the set of harbor types a player has access to based on adjacent buildings.
     *
     * @param p the player whose accessible harbor types are to be determined
     * @return a set of HarborType values available to the player
     */
    public Set<HarborType> getAvailibeHarborTypes(Player p) {
        Set<HarborType> harborTypes = new HashSet<>();
        
        for (Harbor harbor : harbors) {
            HexEdge harborEdge = harbor.getLocation();
            HexCorner[] corners = harborEdge.getCorners();
           
            boolean playerHasAccess = false;
            for (HexCorner corner : corners) {
                // Check buildings at this corner
                if (buildings.stream().anyMatch(building -> 
                        building.getLocation().equals(corner) && 
                        building.getOwner().equals(p))) {
                    playerHasAccess = true;
                    break;
                }
            }
            
            if (playerHasAccess) {
            	// These tiles form the outer border of the map and are rendered as water hexes.
            	harborTypes.add(harbor.getHarborType());
            }
        }
        
        return harborTypes;
    }

}