package de.dhbw_ravensburg.theSettlersOfJava.buildings;

import de.dhbw_ravensburg.theSettlersOfJava.map.HexEdge;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;

public class Harbor {
	
	private final HexEdge location;
	private final ResourceType resourceType;
	
	public Harbor(HexEdge location, ResourceType resourceType) {
		this.location = location;
		this.resourceType = resourceType;
	}
	
	public void visualize() {
		
	}

	public HexEdge getLocation() {
		return location;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}
}
