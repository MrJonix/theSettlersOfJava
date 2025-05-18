package de.dhbw_ravensburg.theSettlersOfJava;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.InputStream;

public class CatanMainMenu extends FXGLMenu {

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 60;

    public CatanMainMenu() {
        super(MenuType.MAIN_MENU);

        // Background image
        String backgroundImagePath = "/images/startscreen.png";
        InputStream imageStream = getClass().getResourceAsStream(backgroundImagePath);
        
        if (imageStream != null) {
            Image backgroundImage = new Image(imageStream);
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(FXGL.getAppWidth());
            backgroundView.setFitHeight(FXGL.getAppHeight());
            getContentRoot().getChildren().add(backgroundView);
        } else {
            // Fallback solid color
            Rectangle background = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
            background.setFill(Color.DARKGREEN);
            getContentRoot().getChildren().add(background);
        }

        // Title
        Text title = new Text("The Settlers of Java");
        title.setFont(Font.font("Arial", 48));
        title.setFill(Color.WHITE);
        title.setStroke(Color.BLACK);
        title.setStrokeWidth(2);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setEffect(new DropShadow(10, Color.BLACK));

        // Buttons
        Button startButton = createButton("Spiel starten");
        startButton.setOnAction(e -> fireNewGame());

        Button optionsButton = createButton("Optionen");
        optionsButton.setOnAction(e -> {
            // Optionen-Funktionalität hier einfügen
        });

        Button exitButton = createButton("Spiel beenden");
        exitButton.setOnAction(e -> fireExit());

        // Layout
        VBox menuBox = new VBox(20, title, startButton, optionsButton, exitButton);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setTranslateX(FXGL.getAppWidth() / 2 - BUTTON_WIDTH);
        menuBox.setTranslateY(FXGL.getAppHeight() / 2 - 150);

        getContentRoot().getChildren().add(menuBox);
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setFont(Font.font("Arial", 20));

        String baseStyle = "-fx-background-color: #f5f5dc; " +
                           "-fx-background-radius: 15; " +
                           "-fx-border-color: #a57e38; " +
                           "-fx-border-width: 2; " +
                           "-fx-border-radius: 15; " +
                           "-fx-text-fill: black;";
        String hoverStyle = "-fx-background-color: #a57e38; " +
                            "-fx-background-radius: 15; " +
                            "-fx-border-color: #8b5c27; " +
                            "-fx-border-width: 2; " +
                            "-fx-border-radius: 15; " +
                            "-fx-text-fill: white;";

        button.setStyle(baseStyle);

        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));

        return button;
    }
}
