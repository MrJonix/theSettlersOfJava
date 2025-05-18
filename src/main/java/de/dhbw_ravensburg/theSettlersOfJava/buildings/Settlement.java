package de.dhbw_ravensburg.theSettlersOfJava.buildings;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.map.HexCorner;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.scene.paint.Color;

public class Settlement extends Building {
	
	private final static Map<Color, String> IMAGE_PATHS = Map.of(
	            Color.RED, "RED_settlement.png",
	            Color.BLUE, "BLUE_settlement.png",
	            Color.ORANGE, "ORANGE_settlement.png",
	            Color.GREEN, "GREEN_settlement.png"
	        );

	private static final Map<ResourceType, Integer> SETTLEMENT_COST;
	
	static {
        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.WOOD, 1);
        cost.put(ResourceType.BRICK, 1);
        cost.put(ResourceType.WHEAT, 1);
        cost.put(ResourceType.WOOL, 1);
        SETTLEMENT_COST = Collections.unmodifiableMap(cost);
    }

    public Settlement(HexCorner location, Player owner) {
        super(location, owner, SETTLEMENT_COST);
    }

	@Override
	public int getVictoryPoints() {
		return 1;
	}

	@Override
	public String getImagePath() {
		return IMAGE_PATHS.get(owner.getColor());
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
	        .onClick(entity -> {
	            if (getOwner().equals(App.getGameController().getCurrentPlayer())) {
	                getOwner().build(new City(getLocation(), getOwner()));
	            }
	        })
	        .buildAndAttach();
	}
}
