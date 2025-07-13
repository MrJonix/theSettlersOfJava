package de.dhbw_ravensburg.theSettlersOfJava.graphics.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.UIHelpers;

/**
 * A modular UI component that displays game rules.
 *
 * Includes headings, bullet points, and paragraph text in a scrollable layout.
 * Used to present the game rules within a stylized, readable panel.
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
        rules.setMaxWidth(700);
        rules.setMaxHeight(800);
        rules.setAlignment(Pos.CENTER);
        rules.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-padding: 25;" +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 16, 0.2, 0, 6);");

        // Title
        Text title = new Text("RULES");
        title.setFont(Font.font("Myriad Pro", FontWeight.EXTRA_BOLD, 62));
        title.setFill(Color.web("#FFC700"));

        // Rules content using TextFlow
        TextFlow content = new TextFlow();
        content.setPadding(Insets.EMPTY);
        content.setPrefWidth(640);
        content.setLineSpacing(8);
        content.setPadding(new Insets(20));

        content.getChildren().addAll(
            sectionTitle("🎯 Goal of the Game"),
            paragraph("Be the first to reach 10 victory points through constructions, roads, and development cards."),

            
            sectionTitle("🧩 Game Material"),
            bullet("19 terrain hexes (Forest, Pasture, Field, Hill, Mountain, Desert)"),
            bullet("Number tokens (2–12, except 7)"),
            bullet("Robber, Resource Cards, Development Cards"),
            bullet("Game pieces: roads, settlements, cities"),

            sectionTitle("🏝️ Game Setup"),
            bullet("Lay out hexes and number tokens, place robber on the desert"),
            bullet("Each player builds 2 settlements + 1 road (one in reverse order)"),
            bullet("Collect resources for 2nd settlement"),

            sectionTitle("🧱 Turn Sequence:"),
            numbered("1. Roll Dice:",
                "All players receive resources from adjacent hexes with this number\n" +
                "On a roll of 7: move robber, discard cards (if 8 or more), steal a card"),
            numbered("2. Trading:",
                "With other players or the supply (4:1, 3:1, or 2:1 if at a port)"),
            numbered("3. Building:",
                "Road: wood + brick\n" +
                "Settlement (1 victory point): wood + brick + grain + wool\n" +
                "City (2 victory points): 2 grain + 3 ore"),

            sectionTitle("🏆 Longest Road"),
            bullet("The player with the longest continuous road of at least 5 segments gains 2 victory points."),
            bullet("The longest road can change hands if another player builds a longer one."),

            sectionTitle("🛠️ Settlement Placement"),
            paragraph("Settlements can only be placed at intersections where you have no adjacent buildings."),
            paragraph("Settlements cannot be placed next to each other without space in between (distance rule)."),

            sectionTitle("🛑 End of the Game"),
            paragraph("The first player to reach 10 victory points wins immediately.")
        );

        // ScrollPane for rule text
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
        	    "-fx-background: white; " +
        	    "-fx-background-color: white; " +
        	    "-fx-control-inner-background: white; " +
        	    "-fx-background-insets: 0; " +
        	    "-fx-padding: 0;"
        	);


        // Wrapper with white background and DropShadow
        VBox scrollContainer = new VBox(scrollPane);
        scrollContainer.setPadding(new Insets(10));
        scrollContainer.setMaxHeight(580);
        scrollContainer.setStyle("-fx-background-color: white; -fx-background-radius: 14;" +
                                 "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0.2, 0, 4);");
        scrollContainer.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(14), Insets.EMPTY)));

        // Back button
        Button backBtn = UIHelpers.createStyledButton("BACK", onBack, "#E0E0E0", "#888");

        rules.getChildren().addAll(title, scrollContainer, backBtn);
        return rules;
    }

    /**
     * Creates a stylized heading text for a rule section.
     *
     * @param title the section title to display
     * @return a {@link Text} styled as a heading
     */

    private static Text sectionTitle(String title) {
        Text text = new Text("\n" + title + "\n");
        text.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 20));
        text.setFill(Color.web("#FFC700"));
        return text;
    }

    /**
     * Creates a paragraph text block for rule descriptions.
     *
     * @param body the main paragraph content
     * @return a {@link Text} styled as normal body text
     */

    private static Text paragraph(String body) {
        Text text = new Text(body + "\n");
        text.setFont(Font.font("Myriad Pro", 16));
        return text;
    }

    /**
     * Creates a bullet point text line.
     *
     * @param line the content of the bullet point
     * @return a {@link Text} element prefixed with a bullet symbol
     */

    private static Text bullet(String line) {
        Text text = new Text("• " + line + "\n");
        text.setFont(Font.font("Myriad Pro", 16));
        return text;
    }

    /**
     * Creates a numbered step with a title and explanation.
     *
     * @param stepTitle the heading/title of the step
     * @param body      the description or details of the step
     * @return a combined {@link Text} element with styled title and body
     */

    private static Text numbered(String stepTitle, String body) {
        Text title = new Text(stepTitle + "\n");
        title.setFont(Font.font("Myriad Pro", FontWeight.SEMI_BOLD, 16));
        title.setFill(Color.BLACK);

        Text bodyText = new Text(body + "\n");
        bodyText.setFont(Font.font("Myriad Pro", 16));

        Text combined = new Text();
        combined.setText(title.getText() + bodyText.getText());
        combined.setFont(Font.font("Myriad Pro", 16));
        return combined;
    }
}