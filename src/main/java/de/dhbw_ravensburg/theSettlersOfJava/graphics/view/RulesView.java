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
            sectionTitle("🎯 Ziel des Spiels"),
            paragraph("Erreiche als Erster 10 Siegpunkte durch Bauwerke, Straßen und Entwicklungskarten."),

            sectionTitle("🧩 Spielmaterial"),
            bullet("- 19 Landschaftsfelder (Wald, Weide, Acker, Hügel, Gebirge, Wüste)"),
            bullet("- Zahlenchips (2–12, außer 7)"),
            bullet("- Räuber, Rohstoffkarten, Entwicklungskarten"),
            bullet("- Spielsteine: Straßen, Siedlungen, Städte"),

            sectionTitle("🏝️ Spielaufbau"),
            bullet("- Landschaft und Zahlenchips auslegen, Räuber auf die Wüste setzen"),
            bullet("- Jeder Spieler baut 2 Siedlungen + je 1 Straße (eine in umgekehrter Reihenfolge)"),
            bullet("- Rohstoffe für 2. Siedlung einsammeln"),

            sectionTitle("🧱 Spielablauf pro Zug:"),
            numbered("1. Würfeln:",
                "- Alle Spieler erhalten Rohstoffe von angrenzenden Feldern mit dieser Zahl\n" +
                "- Bei 7: Räuber bewegt sich, Karten abwerfen (ab 8), Karte stehlen"),
            numbered("2. Handeln:",
                "- Mit Mitspielern oder dem Vorrat (4:1, 3:1 oder 2:1 bei Hafen)"),
            numbered("3. Bauen:",
                "- Straße: Holz + Lehm\n" +
                "- Siedlung: Holz + Lehm + Getreide + Wolle\n" +
                "- Stadt: 2 Getreide + 3 Erz\n"),

            sectionTitle("🛑 Spielende"),
            paragraph("Der erste Spieler mit 10 Siegpunkten gewinnt sofort.")
        );

        // ScrollPane für Regeltext
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("""
        	    -fx-background: white;
        	    -fx-background-color: white;
        	    -fx-control-inner-background: white;
        	    -fx-background-insets: 0;
        	    -fx-padding: 0;
        	""");


        // Wrapper mit weißem Hintergrund und DropShadow
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

    // Helper for section headings
    private static Text sectionTitle(String title) {
        Text text = new Text("\n" + title + "\n");
        text.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 20));
        text.setFill(Color.web("#FFC700"));
        return text;
    }

    // Helper for normal paragraphs
    private static Text paragraph(String body) {
        Text text = new Text(body + "\n");
        text.setFont(Font.font("Myriad Pro", 16));
        return text;
    }

    // Helper for bullet points
    private static Text bullet(String line) {
        Text text = new Text("• " + line + "\n");
        text.setFont(Font.font("Myriad Pro", 16));
        return text;
    }

    // Helper for numbered steps
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
