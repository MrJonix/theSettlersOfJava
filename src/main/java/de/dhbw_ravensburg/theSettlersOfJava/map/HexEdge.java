package de.dhbw_ravensburg.theSettlersOfJava.map;

import java.util.Set;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class HexEdge {
	private HexCorner start;
	private HexCorner end;
	
	public HexEdge(HexCorner start, HexCorner end) {
		this.start = start;
		this.end = end;
	}
	
	public HexCorner[] getCorners() {
		return new HexCorner[] {start,end};
	}
	@Override
	public String toString() {
		return start + " " + end;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HexEdge) {
			HexEdge edge = (HexEdge) obj;
			HexCorner[] corners = edge.getCorners();
			return (start.equals(corners[0]) || start.equals(corners[1])) && 
					(end.equals(corners[0]) || end.equals(corners[1]));
		}
		return false;
		
	}
	
 	public void visualizeEdge(Color color) {
	    // Koordinaten der beiden Ecken
	    double x1 = start.getX();
	    double y1 = start.getY();
	    double x2 = end.getX();
	    double y2 = end.getY();

	    // Linie erzeugen
	    Line line = new Line(x1, y1, x2, y2);
	    line.setStroke(color);
	    line.setStrokeWidth(5);

	    // Entity mit Linie erzeugen
	    Entity lineEntity = FXGL.entityBuilder()
	        .at(0, 0) // Linie hat absolute Koordinaten
	        .view(line)
	        .buildAndAttach();

	    // Klick-Handler für die Linie
	    line.setOnMouseClicked(event -> {
	        StringBuilder message = new StringBuilder("HexEdge verbindet zwei Ecken:\n");

	        message.append("Ecke 1:\n");
	        for (Hex hex : start.getAdjacentHexes()) {
	            HexPosition pos = hex.getPosition();
	            message.append(String.format(" - Typ: %s | Position: (%d, %d)\n",
	                    hex.getHexType(), pos.getQ(), pos.getR()));
	        }

	        message.append("Ecke 2:\n");
	        for (Hex hex : end.getAdjacentHexes()) {
	            HexPosition pos = hex.getPosition();
	            message.append(String.format(" - Typ: %s | Position: (%d, %d)\n",
	                    hex.getHexType(), pos.getQ(), pos.getR()));
	        }

	        FXGL.getDialogService().showMessageBox(message.toString());
	    });
	}

}
