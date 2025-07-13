package de.dhbw_ravensburg.theSettlersOfJava.buildings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;

import de.dhbw_ravensburg.theSettlersOfJava.map.HarborOrientation;
import de.dhbw_ravensburg.theSettlersOfJava.map.Hex;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexEdge;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HarborType;

/**
 * Represents a harbor located on a hex edge, providing trade benefits 
 * for a specific resource type and including a visual orientation on the map.
 */
public class Harbor {

    private final HexEdge location;
    private final HarborType harborType;
    private final HarborOrientation orientation;
    private final Hex locationHex;
    
    /**
     * Creates a Harbor at a specific edge with a specified trade resource type.
     *
     * @param location    the hex edge location of the harbor
     * @param harborType the resource type associated with the harbor
     * @param locationHex the hex tile associated with the harbor
     * @param orientation the orientation of the harbor relative to the hex
     */
    public Harbor(HexEdge location, HarborType harborType, Hex locationHex, HarborOrientation orientation) {
        this.location = location;
        this.harborType = harborType;
        this.locationHex = locationHex;
        this.orientation = orientation;
        visualize();
    }

    /**
     * Visualizes the harbor on the game board using its image and position.
     * Applies scaling and centering based on the associated hex and orientation.
     */
	public void visualize() {
		
	    if (location == null) return;
		
	    try {
		    Texture texture = FXGL.getAssetLoader().loadTexture(harborType.getImagePath());
		    
		    // Original size of the image
		    double originalWidth = Hex.HEX_SIZE * 2 - 10;
		    double originalHeight = Math.sqrt(3) * Hex.HEX_SIZE - 10;
	        texture.setFitWidth(originalHeight);
	        texture.setFitHeight(originalWidth);
	        texture.setPreserveRatio(true);

		    // Positioning and centering
		    double drawX = locationHex.getPosition().getX() - originalHeight/2;
		    double drawY = locationHex.getPosition().getY() - originalWidth/2;

		    FXGL.entityBuilder()
		        .at(drawX, drawY)
		        .zIndex(10)
		        .view(texture)
		        .buildAndAttach();
		    
		    Texture stegTexture = FXGL.getAssetLoader().loadTexture(orientation.getImagePath());
		    stegTexture.setFitWidth(originalHeight);
		    stegTexture.setFitHeight(originalWidth);
		    stegTexture.setPreserveRatio(true);

		    FXGL.entityBuilder()
		        .at(drawX, drawY)
		        .zIndex(11)
		        .view(stegTexture)
		        .buildAndAttach();


		} catch (Exception e) {
		    System.err.println("Failed to load or display harbor ship texture: " + e.getMessage());
		    e.printStackTrace();
		}

	}

    /**
     * Gets the location of the harbor on the game board.
     *
     * @return the hex edge where the harbor is located
     */
    public HexEdge getLocation() {
        return location;
    }
    
    /**
     * Gets the type of resource the harbor offers for trading.
     *
     * @return the harbor's type of the harbor
     */
    public HarborType getHarborType() {
        return harborType;
    }
    
    /**
     * Gets the hex tile associated with the harbor's position.
     * 
     * @return the hex containing the harbor
     */
	public Hex getLocationHex() {
		return locationHex;
	}

	/**
	 * Gets the orientation of the harbor relative to its hex tile.
	 * 
	 * @return the harbor's orientation
	 */
	public HarborOrientation getOrientation() {
		return orientation;
	}
}