package de.mrjonix.catan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Material {
	
	WOOL,IRON,WOOD,WHEAT,CLAY,DESSERT,WATER,BOAT_WATER;
	
	public static List<Material> generateMaterialList() {
    	List<Material> materialsList = new ArrayList<>();

        // 4x jeder dieser Rohstoffe
        for (int i = 0; i < 4; i++) {
            materialsList.add(Material.WOOL);
            materialsList.add(Material.WOOD);
            materialsList.add(Material.WHEAT);
        }

        // 3x CLAY
        for (int i = 0; i < 3; i++) {
            materialsList.add(Material.CLAY);
            materialsList.add(Material.IRON);
        }

        // 1x DESSERT
        materialsList.add(Material.DESSERT);

        // Optional: Mischen
        Collections.shuffle(materialsList);
        return materialsList;
    }
}
