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
 * A modular UI component that displays game credits.
 */
public class CreditsView {

    /**
     * Creates the credits view layout.
     *
     * @param onBack Runnable to execute when the BACK button is pressed.
     * @return a VBox representing the Credits screen.
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

        // Credits text
        String text = """
            The Settlers of Java

            - Jonas Thelen
            - Nico Schweinbenz
            - Kim Wolf
            - Arthur Nulet""";

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

