package de.dhbw_ravensburg.theSettlersOfJava.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.City;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.Settlement;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class HexCorner {

    private double x;
    private double y;
    private Hex[] adjacentHexes;

    public HexCorner(Hex hex1, Hex hex2, Hex hex3) {
        this.adjacentHexes = new Hex[]{hex1, hex2, hex3};
        calculateCorner();
    }
    
    private void calculateCorner() {

        double x1 = adjacentHexes[0].getPosition().getX();
        double y1 = adjacentHexes[0].getPosition().getY();
        double x2 = adjacentHexes[1].getPosition().getX();
        double y2 = adjacentHexes[1].getPosition().getY();
        double x3 = adjacentHexes[2].getPosition().getX();
        double y3 = adjacentHexes[2].getPosition().getY();

        // Berechne den Durchschnitt der drei Mittelpunkte (Ecke)
        this.x = (x1 + x2 + x3) / 3;
        this.y = (y1 + y2 + y3) / 3;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Hex[] getAdjacentHexes() {
        return adjacentHexes;
    }
    
    public void visualizeCorner(Color color) {

        Circle circle = new Circle(10, color);

        FXGL.entityBuilder()
            .at(x, y)
            .view(circle)
            .buildAndAttach();

        // Klick-Handler hinzufügen
        circle.setOnMouseClicked(event -> {
        	Player currentPlayer = App.getGameController().getCurrentPlayer();
        	if(currentPlayer.build(new Settlement(this, currentPlayer))) {
        		FXGL.getDialogService().showMessageBox("Build");
        	} else {
        		FXGL.getDialogService().showMessageBox("didn't build");
        	}
        	
            /*
        	StringBuilder message = new StringBuilder("Benachbarte Hexes:\n");
            for (Hex hex : this.getAdjacentHexes()) {
            	HexPosition pos = hex.getPosition();
                message.append(String.format("- Typ: %s | Position: (%d, %d)\n",
                        hex.getHexType(), pos.getQ(), pos.getR()));
            }
            FXGL.getDialogService().showMessageBox(message.toString());
            */
        });
    }

    @Override
    public String toString() {
        return String.format("Ecke Koordinaten: (%.2f, %.2f)", x, y);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        HexCorner other = (HexCorner) obj;

        Set<Hex> thisSet = new HashSet<>(Arrays.asList(this.adjacentHexes));
        Set<Hex> otherSet = new HashSet<>(Arrays.asList(other.adjacentHexes));

        return thisSet.equals(otherSet);
    }

    @Override
    public int hashCode() {
        Set<Hex> set = new HashSet<>(Arrays.asList(adjacentHexes));
        return set.hashCode();
    }
}


