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
        addBackground();
        addMenuUI();
    }

    private void addBackground() {
        ImageView backgroundView = loadBackgroundImage("/images/startscreen.png");
        if (backgroundView != null) {
            backgroundView.setFitWidth(FXGL.getAppWidth());
            backgroundView.setFitHeight(FXGL.getAppHeight());
            getContentRoot().getChildren().add(backgroundView);
        } else {
            Rectangle fallback = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
            fallback.setFill(Color.DARKGREEN);
            getContentRoot().getChildren().add(fallback);
        }
    }

    private ImageView loadBackgroundImage(String path) {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream != null) {
            return new ImageView(new Image(stream));
        }
        return null;
    }

    private void addMenuUI() {
        Text title = createTitle();

        Button startButton = createButton("Spiel starten", this::fireNewGame);
        Button optionsButton = createButton("Spielregeln", () -> {
            // TODO: Add options functionality
        });
        Button exitButton = createButton("Spiel beenden", this::fireExit);

        VBox menuBox = new VBox(20, title, startButton, optionsButton, exitButton);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setTranslateX(FXGL.getAppWidth() / 2.0 - BUTTON_WIDTH);
        menuBox.setTranslateY(FXGL.getAppHeight() / 2.0 - 150);

        getContentRoot().getChildren().add(menuBox);
    }

    private Text createTitle() {
        Text title = new Text("The Settlers of Java");
        title.setFont(Font.font("Arial", 48));
        title.setFill(Color.WHITE);
        title.setStroke(Color.BLACK);
        title.setStrokeWidth(2);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setEffect(new DropShadow(10, Color.BLACK));
        return title;
    }

    private Button createButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setFont(Font.font("Arial", 20));

        String baseStyle = getBaseButtonStyle();
        String hoverStyle = getHoverButtonStyle();

        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
        button.setOnAction(e -> action.run());

        return button;
    }

    private String getBaseButtonStyle() {
        return "-fx-background-color: #f5f5dc; " +
               "-fx-background-radius: 15; " +
               "-fx-border-color: #a57e38; " +
               "-fx-border-width: 2; " +
               "-fx-border-radius: 15; " +
               "-fx-text-fill: black;";
    }

    private String getHoverButtonStyle() {
        return "-fx-background-color: #a57e38; " +
               "-fx-background-radius: 15; " +
               "-fx-border-color: #8b5c27; " +
               "-fx-border-width: 2; " +
               "-fx-border-radius: 15; " +
               "-fx-text-fill: white;";
    }
}
