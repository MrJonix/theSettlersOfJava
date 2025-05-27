package de.dhbw_ravensburg.theSettlersOfJava.buildings;import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;

import de.dhbw_ravensburg.theSettlersOfJava.map.Hex;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexEdge;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HarborType;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Represents a harbor located on a specific hex edge, providing trade benefits for a specific resource type.
 */
public class Harbor {

    private final HexEdge location;
    private final HarborType harborType;
    private final Hex locationHex;
    /**
     * Constructs a Harbor at a specific location with a specified trade resource type.
     *
     * @param location    the hex edge location of the harbor
     * @param harborType the resource type associated with the harbor
     */
    public Harbor(HexEdge location, HarborType harborType, Hex locationHex) {
        this.location = location;
        this.harborType = harborType;
        this.locationHex = locationHex;
        visualize();
    }

    /**
     * Visualizes the harbor on the game board.
     * Currently, this method does not perform any operations.
     */
	public void visualize() {
	    Color color;
	    if (location == null) return;
		switch (harborType) {
	        case BRICK : color = Color.ORANGERED; break; //correct
	        case WOOD : color = Color.FORESTGREEN; break; //correct
	        case WOOL : color = Color.LIGHTGREEN; break;  //correct 
	        case WHEAT : color = Color.GOLD; break; 
	        case ORE : color = Color.DARKGRAY; break;
	        case THREE_TO_ONE : color = Color.PURPLE; break; //3:1 trade ratio //1 out of 4
	        default : color = Color.BLACK; break;

	    }
		try {
		    Texture texture = FXGL.getAssetLoader().loadTexture("/ship/boat_WOOL.png");
		    

		    // Originalgröße des Bildes
		    double originalWidth = Hex.HEX_SIZE * 2 - 10;
		    double originalHeight = Math.sqrt(3) * Hex.HEX_SIZE - 10;
	        texture.setFitWidth(originalHeight);  
	        texture.setFitHeight(originalWidth);

	        texture.setPreserveRatio(true);

		    // Positionieren, zentriert
		    double drawX = locationHex.getPosition().getX() - originalHeight/2;
		    double drawY = locationHex.getPosition().getY() - originalWidth/2;

		    FXGL.entityBuilder()
		        .at(drawX, drawY)
		        .zIndex(10)
		        .view(texture)
		        .buildAndAttach();

		} catch (Exception e) {
		    System.err.println("Failed to load or display harbor ship texture: " + e.getMessage());
		    e.printStackTrace();
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

	public Hex getLocationHex() {
		return locationHex;
	}
}