package de.dhbw_ravensburg.theSettlersOfJava.graphics;

import com.almasb.fxgl.dsl.FXGL;

import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;

import java.util.List;

public class PlayerInfoUI {

    public Pane createPlayerListPanel(List<Player> players) {
        VBox playerListBox = new VBox(10);
        playerListBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 10;");
        
        for (Player player : players) {
            playerListBox.getChildren().add(createPlayerInfo(player));
        }

        return playerListBox;
    }

    private Node createPlayerInfo(Player player) {
        VBox playerBox = new VBox(5);
        playerBox.setBorder(new Border(new BorderStroke(
            player.getColor(), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        Text name = FXGL.getUIFactoryService().newText(player.getName(), Color.WHITE, 16);
        // Binde den Namen an die Property (optional, wenn sich Name ändern kann)
        name.textProperty().bind(player.nameProperty());

        Text vp = FXGL.getUIFactoryService().newText("", Color.YELLOW, 14);
        vp.textProperty().bind(player.victoryPointsProperty().asString("VP: %d"));

        Text resources = FXGL.getUIFactoryService().newText("", Color.LIGHTGREEN, 12);
        resources.textProperty().bind(player.resourceSizeProperty().asString("Resources: %d"));

        playerBox.getChildren().addAll(name, vp, resources);
        return playerBox;
    }

}

