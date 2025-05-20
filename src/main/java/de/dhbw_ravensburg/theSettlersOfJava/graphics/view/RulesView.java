package de.dhbw_ravensburg.theSettlersOfJava.graphics.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.UIHelpers;
import javafx.geometry.Pos;

/**
 * A modular UI component that displays game rules.
 */
public class RulesView {

    /**
     * Creates the rules view layout.
     *
     * @param onBack Runnable to execute when the BACK button is pressed.
     * @return a VBox representing the Rules screen.
     */
    public static VBox create(Runnable onBack) {
        VBox rules = new VBox(20);
        rules.setMaxWidth(500);
        rules.setMaxHeight(500);
        rules.setAlignment(Pos.CENTER);
        rules.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-padding: 35;" +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 16, 0.2, 0, 6);");

        // Title
        Text title = new Text("RULES");
        title.setFont(Font.font("Myriad Pro", FontWeight.EXTRA_BOLD, 62));
        title.setFill(Color.web("#FFC700"));

        // Rules text
        String rulesText = """
            Welcome to the Settlers of Java!

            The rules of the game are as follows:

            - Settle your territories.
            - Build roads and settlements.
            - Trade resources with other players.
            - Aim to gather 10 points to win the game.""";

        Label label = new Label(rulesText);
        label.setFont(Font.font("Myriad Pro", 24));
        label.setWrapText(true);
        label.setTextFill(Color.BLACK);

        // Back button
        Button backBtn = UIHelpers.createStyledButton("BACK", onBack, "#E0E0E0", "#888");

        rules.getChildren().addAll(title, label, backBtn);
        return rules;
    }
}
