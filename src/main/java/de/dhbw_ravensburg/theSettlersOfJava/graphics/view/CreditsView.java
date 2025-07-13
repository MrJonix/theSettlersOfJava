package de.dhbw_ravensburg.theSettlersOfJava.graphics.view;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.UIHelpers;

import java.awt.Desktop;
import java.net.URI;

/**
 * UI component that displays the game's credits screen.
 * 
 * Includes a title, list of team members, a GitHub link to the project,
 * and a back button to return to the main menu.
 */

public class CreditsView {

	/**
     * Creates the visual layout for the credits screen.
     * 
     * This includes a title, a list of team members, a GitHub hyperlink,
     * and a back button to return to the main menu or previous screen.
     * 
     * @param onBack a Runnable that will be executed when the BACK button is clicked
     * @return a VBox containing the complete credits UI layout
     */
    public static VBox create(Runnable onBack) {
        VBox credits = new VBox(20);
        credits.setMaxWidth(500);
        credits.setMaxHeight(500);
        credits.setAlignment(Pos.CENTER);
        credits.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-padding: 35;" +
                         "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 16, 0.2, 0, 6);");

        // Title
        Text title = new Text("CREDITS");
        title.setFont(Font.font("Myriad Pro", FontWeight.EXTRA_BOLD, 62));
        title.setFill(Color.web("#FFC700"));

        String text = "The Settlers of Java\n\n" +
                "- Jonas Thelen\n" +
                "- Nico Schweinbenz\n" +
                "- Kim Wolf\n" +
                "- Arthur Nulet";


        Label label = new Label(text);
        label.setFont(Font.font("Myriad Pro", 24));
        label.setWrapText(true);
        label.setTextFill(Color.BLACK);

        // GitHub link
        Hyperlink githubLink = new Hyperlink("Visit our GitHub");
        githubLink.setFont(Font.font("Myriad Pro", 24));
        githubLink.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/MrJonix/theSettlersOfJava"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Back button
        Button backBtn = UIHelpers.createStyledButton("BACK", onBack, "#E0E0E0", "#888");

        credits.getChildren().addAll(title, label, githubLink, backBtn);
        return credits;
    }
}

