package de.dhbw_ravensburg.theSettlersOfJava.buildings;



import java.util.EnumMap;
import java.util.Map;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexEdge;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexEdgeOrientation;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;

public class Road {
	private static final Map<HexEdgeOrientation, String> IMAGE_PATH = new EnumMap<>(HexEdgeOrientation.class);

	static {
	    IMAGE_PATH.put(HexEdgeOrientation.LEFT_TO_RIGHT, "BLUE_road.png");
	    IMAGE_PATH.put(HexEdgeOrientation.STRAIGHT, "pink_roadStraight1.png");
	    IMAGE_PATH.put(HexEdgeOrientation.RIGHT_TO_LEFT, "PINK_road.png");
	}

	private final HexEdge location;
	private final Player owner;
	
	public Road(HexEdge hexEdges, Player owner) {
		this.location = hexEdges;
		this.owner = owner;
	}
	
	public void visualize() {
	    HexCorner[] hexCorners = location.getCorners();
	    double x1 = hexCorners[0].getX();
	    double y1 = hexCorners[0].getY();
	    double x2 = hexCorners[1].getX();
	    double y2 = hexCorners[1].getY();
	    // Mittelpunkt der Kante
	    double centerX = (x1 + x2) / 2;
	    double centerY = (y1 + y2) / 2;

	    // Winkel zwischen Start und Endpunkt berechnen
	    //double angle = Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
	    double angle = 0;
	    //TODO Fix angle with diffrent assets
	    
	    // Bild laden und drehen
	    
	    Texture texture = FXGL.getAssetLoader().loadTexture(IMAGE_PATH.get(location.getHexEdgeOrientation()));
	    texture.setRotate(angle);

	    // Optionale Skalierung
	    texture.setScaleX(0.4);
	    texture.setScaleY(0.4);

	    // Entity mit Texture erzeugen
	    FXGL.entityBuilder()
	        .at(centerX - texture.getWidth() / 2, centerY - texture.getHeight() / 2)
	        .view(texture)
	        .buildAndAttach();

	    // Klick-Handler
	    texture.setOnMouseClicked(event -> {
	        FXGL.getDialogService().showMessageBox("Straße von Spieler " + owner + ".");
	    });
	}


	public Player getOwner() {
		return owner;
	}
	
	
}
