package de.dhbw_ravensburg.theSettlersOfJava.map;

import com.almasb.fxgl.dsl.FXGL;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.buildings.Road;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class HexEdge {
	private HexCorner start;
	private HexCorner end;
	private HexEdgeOrientation orientation;
	
	public HexEdge(HexCorner start, HexCorner end, HexEdgeOrientation orientation) {
		this.start = start;
		this.end = end;
		this.orientation = orientation;
	}
	
	public HexEdgeOrientation getHexEdgeOrientation() {
		return orientation;
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
	    FXGL.entityBuilder()
	        .at(0, 0) // Linie hat absolute Koordinaten
	        .view(line)
	        .buildAndAttach();

	    // Klick-Handler für die Linie
	    line.setOnMouseClicked(event -> {
	    	App.getGameController().getGameBoard().buildRoad(new Road(this, App.getGameController().getCurrentPlayer()));
	        /*
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
	        */
	    });
	}

    public boolean isAdjacentTo(HexEdge otherEdge) {
        return this.start.equals(otherEdge.getCorners()[0]) || this.start.equals(otherEdge.getCorners()[1]) ||
               this.end.equals(otherEdge.getCorners()[0]) || this.end.equals(otherEdge.getCorners()[1]);
    }

    public boolean isAdjacentToCorner(HexCorner corner) {
        return start.equals(corner) || end.equals(corner);
    }

}
