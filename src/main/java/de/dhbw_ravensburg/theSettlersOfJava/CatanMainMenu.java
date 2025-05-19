package de.dhbw_ravensburg.theSettlersOfJava;

import java.awt.Desktop;
import java.io.InputStream;
import java.net.URI;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CatanMainMenu extends FXGLMenu {

    private static final int BUTTON_WIDTH = 400;
    private static final int BUTTON_HEIGHT = 60;

    private VBox mainMenu;
    private VBox creditsView;

    public CatanMainMenu() {
        super(MenuType.MAIN_MENU);
        setupUI();
    }

    private void setupUI() {
        // Load background image
        String backgroundImagePath = "/images/background.png"; // Example path
        InputStream imageStream = getClass().getResourceAsStream(backgroundImagePath);

        ImageView backgroundView = new ImageView();
        if (imageStream != null) {
            Image backgroundImage = new Image(imageStream);
            backgroundView.setImage(backgroundImage);
        }

        backgroundView.setPreserveRatio(true);
        backgroundView.setFitHeight(getAppHeight());
        backgroundView.setFitWidth(getAppWidth());
        backgroundView.setSmooth(true);
        backgroundView.setCache(true);

        // Main menu setup
        mainMenu = setupMainMenu();

        // Credits view setup
        creditsView = setupCreditsView();

        // StackPane to hold both main menu and credits view
        StackPane root = new StackPane(backgroundView, mainMenu, creditsView);
        root.setAlignment(Pos.CENTER);

        // Initially show main menu
        showMainMenu();

        getContentRoot().getChildren().add(root);
    }

    private VBox setupMainMenu() {
        VBox card = new VBox(20);
        card.setMaxWidth(500);
        card.setMaxHeight(500);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-padding: 35;" +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 16, 0.2, 0, 6);");

        Text title = new Text("Settlers Of Java");
        title.setFont(Font.font("Myriad Pro", FontWeight.EXTRA_BOLD, 52));
        title.setFill(Color.web("#FFD700"));
        title.setEffect(new DropShadow(5, Color.web("#5c4a10")));

        Text subtitle = new Text("Board Game Classic.\nSettle. Build. Trade.");
        subtitle.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 22));
        subtitle.setFill(Color.web("#222"));
        subtitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Button playBtn = createStyledButton("PLAY", this::fireNewGame, "#FFD700", "#000");
        Button exitBtn = createStyledButton("EXIT", this::fireExit, "#E0E0E0", "#888");

        // Create half-size buttons for Settings and Credits
        Button settingsBtn = createHalfSizeButton("SETTINGS", this::fireSettings, "#E0E0E0", "#888");
        Button creditsBtn = createHalfSizeButton("CREDITS", this::showCredits, "#E0E0E0", "#888");

        HBox settingsAndCredits = new HBox(10, settingsBtn, creditsBtn);
        settingsAndCredits.setAlignment(Pos.CENTER);
        
        VBox buttons = new VBox(12, playBtn, settingsAndCredits, exitBtn);
        buttons.setAlignment(Pos.CENTER);

        card.getChildren().addAll(title, subtitle, buttons);
        return card;
    }

    private VBox setupCreditsView() {
        VBox credits = new VBox(20);
        credits.setMaxWidth(500);
        credits.setMaxHeight(500);
        credits.setAlignment(Pos.CENTER);
        credits.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-padding: 35;" +
                         "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 16, 0.2, 0, 6);");

        // Title with larger font size
        Text creditsTitle = new Text("CREDITS");
        creditsTitle.setFont(Font.font("Myriad Pro", FontWeight.EXTRA_BOLD, 62)); // Increased font size
        creditsTitle.setFill(Color.web("#FFC700"));

        // Credits text with a larger font size
        String creditsText = """
            The Settlers of Java
            
            - Jonas Thelen
            - Nico Schweinbenz
            - Kim Wolf
            - Arthur Nulet""";

        Label creditsLabel = new Label(creditsText);
        creditsLabel.setFont(Font.font("Myriad Pro", 24)); // Increased font size for body text
        creditsLabel.setWrapText(true);

        Hyperlink githubLink = new Hyperlink("Visit our GitHub");
        githubLink.setFont(Font.font("Myriad Pro", 24)); // Larger font size for the hyperlink
        githubLink.setOnAction(e -> openWebPage("https://github.com/MrJonix/theSettlersOfJava"));

        Button backButton = createStyledButton("BACK", this::showMainMenu, "#E0E0E0", "#888");

        credits.getChildren().addAll(creditsTitle, creditsLabel, githubLink, backButton);
        return credits;
    }

    private void showMainMenu() {
        mainMenu.setVisible(true);
        creditsView.setVisible(false);
    }

    private void showCredits() {
        mainMenu.setVisible(false);
        creditsView.setVisible(true);
    }

    private void openWebPage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URI(urlString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Button createStyledButton(String text, Runnable action, String bgColor, String textColor) {
        Button button = new Button(text);
        button.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 18));
        button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setStyle("-fx-background-color: " + bgColor + ";"
                      + "-fx-text-fill: " + textColor + ";"
                      + "-fx-background-radius: 12;"
                      + "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 4, 0, 0, 2);");

        if (action != null) {
            button.setOnAction(e -> action.run());
            button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: derive(" + bgColor + ", 10%);"
                + "-fx-text-fill: " + textColor + ";"
                + "-fx-background-radius: 12;"
                + "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.3), 6, 0, 0, 3);"));
            button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: " + bgColor + ";"
                + "-fx-text-fill: " + textColor + ";"
                + "-fx-background-radius: 12;"
                + "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 4, 0, 0, 2);"));
        } else {
            button.setDisable(true);
        }

        return button;
    }

    private Button createHalfSizeButton(String text, Runnable action, String bgColor, String textColor) {
        Button button = new Button(text);
        button.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 18));
        button.setPrefSize(BUTTON_WIDTH / 2, BUTTON_HEIGHT);
        button.setStyle("-fx-background-color: " + bgColor + ";"
                      + "-fx-text-fill: " + textColor + ";"
                      + "-fx-background-radius: 12;"
                      + "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 4, 0, 0, 2);");

        if (action != null) {
            button.setOnAction(e -> action.run());
            button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: derive(" + bgColor + ", 10%);"
                + "-fx-text-fill: " + textColor + ";"
                + "-fx-background-radius: 12;"
                + "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.3), 6, 0, 0, 3);"));
            button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: " + bgColor + ";"
                + "-fx-text-fill: " + textColor + ";"
                + "-fx-background-radius: 12;"
                + "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 4, 0, 0, 2);"));
        } else {
            button.setDisable(true);
        }

        return button;
    }


    private void fireSettings() {
        System.out.println("Opening settings...");
    }

}