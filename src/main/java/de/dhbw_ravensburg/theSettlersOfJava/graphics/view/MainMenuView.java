package de.dhbw_ravensburg.theSettlersOfJava.graphics.view;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.UIHelpers;
import javafx.geometry.Pos;

/**
 * UI component that renders the main menu of the game.
 * 
 * Includes the game title, subtitle, and navigation buttons for:
 * - Play
 * - Rules
 * - Credits
 * - Exit
 *
 * This layout is designed as a centered card with drop shadow and styling.
 */
public class MainMenuView {
    
    /**
     * Creates the visual layout for the main menu screen.
     * 
     * This includes the game title, subtitle, and buttons for:
     * - Playing the game
     * - Viewing rules
     * - Viewing credits
     * - Exiting the game
     *
     * @param onPlay    callback executed when the "PLAY" button is pressed
     * @param onRules   callback executed when the "RULES" button is pressed
     * @param onCredits callback executed when the "CREDITS" button is pressed
     * @param onExit    callback executed when the "EXIT" button is pressed
     * @return a VBox containing the entire main menu UI
     */

	public static VBox create(Runnable onPlay, Runnable onRules, Runnable onCredits, Runnable onExit) {
        //Create main container card for the menu
		VBox card = new VBox(20);
        card.setMaxWidth(500);
        card.setMaxHeight(500);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-padding: 35;" +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 16, 0.2, 0, 6);");
        //Game title 
        Text title = new Text("Settlers Of Java");
        title.setFont(Font.font("Myriad Pro", FontWeight.EXTRA_BOLD, 52));
        title.setFill(javafx.scene.paint.Color.web("#FFD700"));

        //Subtitle text
        Text subtitle = new Text("Board Game Classic.\nSettle. Build. Trade.");
        subtitle.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 22));
        subtitle.setFill(javafx.scene.paint.Color.web("#222"));
        subtitle.setTextAlignment(TextAlignment.CENTER);

        //Main action buttons
        Button playBtn = UIHelpers.createStyledButton("PLAY", onPlay, "#FFD700", "#000");
        Button exitBtn = UIHelpers.createStyledButton("EXIT", onExit, "#E0E0E0", "#888");

        //Secondary buttons: rules and credits side by side
        Button rulesBtn = UIHelpers.createHalfSizeButton("RULES", onRules, "#E0E0E0", "#888");
        Button creditsBtn = UIHelpers.createHalfSizeButton("CREDITS", onCredits, "#E0E0E0", "#888");

        HBox rulesAndCredits = new HBox(10, rulesBtn, creditsBtn);
        rulesAndCredits.setAlignment(Pos.CENTER);
        
        //Vertical layout for all buttons
        VBox buttons = new VBox(12, playBtn, rulesAndCredits, exitBtn);
        buttons.setAlignment(Pos.CENTER);

        //Add all components to the main card
        card.getChildren().addAll(title, subtitle, buttons);
        return card;
    }
}
