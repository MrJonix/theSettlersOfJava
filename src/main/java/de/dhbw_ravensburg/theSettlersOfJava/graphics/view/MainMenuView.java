package de.dhbw_ravensburg.theSettlersOfJava.graphics.view;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.UIHelpers;
import javafx.geometry.Pos;

public class MainMenuView {
    public static VBox create(Runnable onPlay, Runnable onRules, Runnable onCredits, Runnable onExit) {
        VBox card = new VBox(20);
        card.setMaxWidth(500);
        card.setMaxHeight(500);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-padding: 35;" +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 16, 0.2, 0, 6);");

        Text title = new Text("Settlers Of Java");
        title.setFont(Font.font("Myriad Pro", FontWeight.EXTRA_BOLD, 52));
        title.setFill(javafx.scene.paint.Color.web("#FFD700"));

        Text subtitle = new Text("Board Game Classic.\nSettle. Build. Trade.");
        subtitle.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 22));
        subtitle.setFill(javafx.scene.paint.Color.web("#222"));
        subtitle.setTextAlignment(TextAlignment.CENTER);

        Button playBtn = UIHelpers.createStyledButton("PLAY", onPlay, "#FFD700", "#000");
        Button exitBtn = UIHelpers.createStyledButton("EXIT", onExit, "#E0E0E0", "#888");

        Button rulesBtn = UIHelpers.createHalfSizeButton("RULES", onRules, "#E0E0E0", "#888");
        Button creditsBtn = UIHelpers.createHalfSizeButton("CREDITS", onCredits, "#E0E0E0", "#888");

        HBox rulesAndCredits = new HBox(10, rulesBtn, creditsBtn);
        rulesAndCredits.setAlignment(Pos.CENTER);
        
        VBox buttons = new VBox(12, playBtn, rulesAndCredits, exitBtn);
        buttons.setAlignment(Pos.CENTER);

        card.getChildren().addAll(title, subtitle, buttons);
        return card;
    }
}
