package de.dhbw_ravensburg.theSettlersOfJava.graphics;

import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;

import java.util.List;

public class PlayerInfoUI {

    public Pane createPlayerListPanel(List<Player> players, ObjectProperty<Player> currentPlayer) {
        HBox playerListBox = new HBox(15); // Etwas mehr Abstand zwischen den Player Boxes
        playerListBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-padding: 15;");

        for (Player player : players) {
            playerListBox.getChildren().add(createPlayerInfo(player, currentPlayer));
        }

        return playerListBox;
    }

    private Node createPlayerInfo(Player player, ObjectProperty<Player> currentPlayer) {
        VBox playerBox = new VBox(15);
        playerBox.setBorder(new Border(new BorderStroke(
            player.getColor(), BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(2))));
        playerBox.setPadding(new Insets(10));
        playerBox.setMinWidth(100);

        // We use a more modern dynamic background change
        playerBox.backgroundProperty().bind(Bindings.createObjectBinding(() -> {
            if (player.equals(currentPlayer.get())) {
                return new Background(new BackgroundFill(Color.web("#DAA520"), new CornerRadii(8), null));
            } else {
                return new Background(new BackgroundFill(Color.WHITE, new CornerRadii(8), null));
            }
        }, currentPlayer));

        Text name = new Text(player.getName());
        name.setFill(Color.BLACK);
        name.setFont(Font.font("Myriad Pro", FontWeight.EXTRA_BOLD , 18)); // Verwende eine klarere Schriftart und Größe
        name.textProperty().bind(player.nameProperty());

        Text vp = new Text();
        vp.setFill(Color.BLACK);
        vp.setFont(Font.font("Myriad Pro",FontWeight.EXTRA_BOLD,  16));
        vp.textProperty().bind(player.victoryPointsProperty().asString("🏆 %d"));

        Text resources = new Text();
        resources.setFill(Color.BLACK);
        resources.setFont(Font.font("Myriad Pro", FontWeight.EXTRA_BOLD, 14));
        resources.textProperty().bind(player.resourceSizeProperty().asString("🛠️ %d"));
        playerBox.getChildren().addAll(name, vp, resources);
        return playerBox;
    }
}