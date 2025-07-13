package de.dhbw_ravensburg.theSettlersOfJava.graphics.view;

import com.almasb.fxgl.dsl.FXGL;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * View component that displays the win screen when a player wins the game.
 */
public class WinView {

    /**
     * Displays a win screen overlay for the specified player.
     * 
     * @param winner the player who has won the game
     */
    public void showWinScreen(Player winner) {
        VBox winBox = new VBox(20);
        winBox.setAlignment(Pos.CENTER);
        winBox.setStyle(
            "-fx-background-color: rgba(0,0,0,0.75);" +
            "-fx-padding: 40;" +
            "-fx-background-radius: 15;"
        );

        Text winText = FXGL.getUIFactoryService().newText(winner.getName() + " hat gewonnen!", Color.GOLD, 36);
        Text congratsText = FXGL.getUIFactoryService().newText("Herzlichen Glückwunsch!", Color.WHITE, 24);

        Button closeButton = new Button("Spiel beenden");
        closeButton.setOnAction(e -> {
            FXGL.getGameController().exit(); // beendet das Spiel
        });

        winBox.getChildren().addAll(winText, congratsText, closeButton);

        FXGL.getGameScene().addUINode(winBox);
        winBox.setTranslateX(FXGL.getAppWidth() / 2 - 200);
        winBox.setTranslateY(FXGL.getAppHeight() / 2 - 100);
    }
}
