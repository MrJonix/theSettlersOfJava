package de.dhbw_ravensburg.theSettlersOfJava.buildings;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;

public class Settlement extends Building {
	private final static String IMAGE_PATH = "settlement.png";

	public Settlement(HexCorner position, Player owner) {
		super(position, owner);
	}

	@Override
	public int getVictoryPoints() {
		return 1;
	}

	@Override
	public String getImagePath() {
		return IMAGE_PATH;
	}
	@Override
	public void visualize() {
	    Texture texture = FXGL.getAssetLoader().loadTexture(getImagePath());
	    texture.setScaleX(0.4);
	    texture.setScaleY(0.4);

	    double x = getLocation().getX() - texture.getWidth() / 2;
	    double y = getLocation().getY() - texture.getHeight() / 2;

	    entity = FXGL.entityBuilder()
	        .at(x, y)
	        .view(texture)
	        .onClick(entity -> App.getGameController().getGameBoard().buildBuilding(
	        	    new City(getLocation(), getOwner())
	        	))
	        .buildAndAttach();
	}

	
}
