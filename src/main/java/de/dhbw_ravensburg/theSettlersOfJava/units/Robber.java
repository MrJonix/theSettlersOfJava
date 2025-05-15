package de.dhbw_ravensburg.theSettlersOfJava.units;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import de.dhbw_ravensburg.theSettlersOfJava.map.Hex;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Robber {
	private Hex location;
	private Entity robberEntity;
	
	public Robber(Hex location) {
		this.location = location;
	}

	public Hex getLocation() {
		return location;
	}

	public void moveRobber(Hex newLocation) {
	    this.location = newLocation;

	    if (robberEntity != null) {
	        robberEntity.setPosition(newLocation.getPosition().getWorldPosition());
	    }
	}

	
	public void stealResources(Player player) {
		
	}
	
	public void visualize() {
	    robberEntity = FXGL.entityBuilder()
	            .at(location.getPosition().getX(), location.getPosition().getY())
	            .viewWithBBox(new Circle(15, Color.BLACK))
	            .with("robber", this)
	            .buildAndAttach();
	}


	
}
