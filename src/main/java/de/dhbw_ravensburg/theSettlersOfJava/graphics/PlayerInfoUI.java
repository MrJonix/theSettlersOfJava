package de.dhbw_ravensburg.theSettlersOfJava.graphics;

import com.almasb.fxgl.dsl.FXGL;

import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;

import java.util.List;

public class PlayerInfoUI {

	public Pane createPlayerListPanel(List<Player> players, ObjectProperty<Player> currentPlayer) {
	    VBox playerListBox = new VBox(10);
	    playerListBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 10;");

	    for (Player player : players) {
	        playerListBox.getChildren().add(createPlayerInfo(player, currentPlayer));
	    }

	    return playerListBox;
	}

	private Node createPlayerInfo(Player player, ObjectProperty<Player> currentPlayer) {
	    VBox playerBox = new VBox(5);
	    playerBox.setBorder(new Border(new BorderStroke(
	        player.getColor(), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

	    // Hintergrundfarbe dynamisch anpassen je nachdem, ob Spieler gerade aktiv ist
	    playerBox.backgroundProperty().bind(Bindings.createObjectBinding(() -> {
	        if (player.equals(currentPlayer.get())) {
	            return new Background(new BackgroundFill(Color.DARKGOLDENROD, CornerRadii.EMPTY, null));
	        } else {
	            return new Background(new BackgroundFill(Color.color(0, 0, 0, 0.3), CornerRadii.EMPTY, null));
	        }
	    }, currentPlayer));

	    Text name = FXGL.getUIFactoryService().newText(player.getName(), Color.WHITE, 16);
	    name.textProperty().bind(player.nameProperty());

	    Text vp = FXGL.getUIFactoryService().newText("", Color.YELLOW, 14);
	    vp.textProperty().bind(player.victoryPointsProperty().asString("VP: %d"));

	    Text resources = FXGL.getUIFactoryService().newText("", Color.LIGHTGREEN, 12);
	    resources.textProperty().bind(player.resourceSizeProperty().asString("Resources: %d"));

	    playerBox.getChildren().addAll(name, vp, resources);
	    return playerBox;
	}
}