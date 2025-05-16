package de.dhbw_ravensburg.theSettlersOfJava.buildings;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;

import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;

public abstract class Building {
	
	private final HexCorner location;
	private final Player owner;
	
	public Building (HexCorner location, Player owner) {
		this.location = location;
		this.owner = owner;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public abstract int getVictoryPoints();
	
	public abstract String getImagePath();
	
	public void visualize() {
		    Texture texture = FXGL.getAssetLoader().loadTexture(getImagePath());

		    // Optional skalieren
		    texture.setScaleX(0.4);
		    texture.setScaleY(0.4);

		    // Platziere das Bild zentriert auf der Ecke
		    double x = location.getX() - texture.getWidth() / 2;
		    double y = location.getY() - texture.getHeight() / 2;

		    // Entity erstellen und anhängen
		    FXGL.entityBuilder()
		        .at(x, y)
		        .view(texture)
		        .buildAndAttach();

		    // Klick-Interaktion: Gebäudeinformationen anzeigen
		    texture.setOnMouseClicked(event -> {
		        FXGL.getDialogService().showMessageBox(String.format(
		            "%s von Spieler %s\nSiegpunkte: %d",
		            this.getClass().getSimpleName(),
		            owner.getName(),
		            getVictoryPoints()
		        ));
		    });
		}

	public HexCorner getLocation() {
		return location;
	}
}
// Test