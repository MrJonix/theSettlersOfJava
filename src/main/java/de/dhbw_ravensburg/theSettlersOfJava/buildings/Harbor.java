package de.dhbw_ravensburg.theSettlersOfJava.buildings;import de.dhbw_ravensburg.theSettlersOfJava.map.HexEdge;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HarborType;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import javafx.scene.paint.Color;

/**
 * Represents a harbor located on a specific hex edge, providing trade benefits for a specific resource type.
 */
public class Harbor {

    private final HexEdge location;
    private final HarborType harborType;
    /**
     * Constructs a Harbor at a specific location with a specified trade resource type.
     *
     * @param location    the hex edge location of the harbor
     * @param harborType the resource type associated with the harbor
     */
    public Harbor(HexEdge location, HarborType harborType) {
        this.location = location;
        this.harborType = harborType;
    }

    /**
     * Visualizes the harbor on the game board.
     * Currently, this method does not perform any operations.
     */
	public void visualize() {
	    Color color;
		switch (harborType) {
	        case BRICK : color = Color.ORANGERED; break; //correct
	        case WOOD : color = Color.FORESTGREEN; break; //correct
	        case WOOL : color = Color.LIGHTGREEN; break;  //correct 
	        case WHEAT : color = Color.GOLD; break; 
	        case ORE : color = Color.DARKGRAY; break;
	        case THREE_TO_ONE : color = Color.PURPLE; break; //3:1 trade ratio //1 out of 4
	        default : color = Color.BLACK; break;

	    }
	    location.visualizeHarborEdge(color,15); // visual: draws a thick colored edge
	}

    /**
     * Gets the location of the harbor.
     *
     * @return the hex edge where the harbor is located
     */
    public HexEdge getLocation() {
        return location;
    }

    /**
     * Gets the resource type associated with the harbor for trading.
     *
     * @return the harbor type of the harbor
     */
    public HarborType getHarborType() {
        return harborType;
    }
}