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

public class CatanMainMenu extends FXGLMenu {
    
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 60;
    
    public CatanMainMenu() {
        super(MenuType.MAIN_MENU);
        
        // Füge Hintergrundbild hinzu
      //Bilddatei ist irgendwie fehlerhaft, entweder alternatives Bild oder reparieren
        String backgroundImagePath = "/images/startscreen.png"; // Pfad zum Hintergrundbild anpassen
        try {
            Image backgroundImage = new Image(getClass().getResourceAsStream(backgroundImagePath));
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(FXGL.getAppWidth()+500); //Muss noch vielleicht angepasst werden!!
            backgroundView.setFitHeight(FXGL.getAppHeight());
            getContentRoot().getChildren().add(backgroundView);
        } catch (Exception e) {
            // Fallback bei Fehlern: Einfarbiger Hintergrund
            Rectangle background = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
            background.setFill(Color.DARKGREEN);
            getContentRoot().getChildren().add(background);
        }
        
        // Titel erstellen
        Text title = new Text("The Settlers of Java");
        title.setFont(Font.font("Arial", 48));
        title.setFill(Color.WHITE);
        title.setStroke(Color.BLACK);
        title.setStrokeWidth(2);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setEffect(new DropShadow(10, Color.BLACK));
        
        // Buttons erstellen
        Button startButton = createButton("Spiel starten");
        startButton.setOnAction(e -> fireNewGame());
        
        Button optionsButton = createButton("Optionen");
        optionsButton.setOnAction(e -> {
            // Hier später Optionen-Funktionalität einfügen
        });
        
        Button exitButton = createButton("Spiel beenden");
        exitButton.setOnAction(e -> fireExit());
        
        // Layout erstellen
        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.getChildren().addAll(title, startButton, optionsButton, exitButton);
        
        getContentRoot().getChildren().add(menuBox);
        
        // Zentriere das Menü
        menuBox.setTranslateX(FXGL.getAppWidth() / 2 - BUTTON_WIDTH / 2);
        menuBox.setTranslateY(FXGL.getAppHeight() / 2 - 150);
    }
    
    private Button createButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(BUTTON_WIDTH);
        button.setPrefHeight(BUTTON_HEIGHT);
        button.setFont(Font.font("Arial", 20));
        
        // Button-Styling
        button.setStyle(
            "-fx-background-color: #f5f5dc; " + // beige Farbe
            "-fx-background-radius: 15; " + // abgerundete Ecken
            "-fx-border-color: #a57e38; " + // dunklere Umrandung
            "-fx-border-width: 2; " +
            "-fx-border-radius: 15;"
        );
        
        // Hover-Effekt
        button.setOnMouseEntered(e -> {
            button.setStyle(
                "-fx-background-color: #a57e38; " + // dunklere Farbe beim Hover
                "-fx-background-radius: 15; " +
                "-fx-border-color: #8b5c27; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 15; " +
                "-fx-text-fill: white;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: #f5f5dc; " +
                "-fx-background-radius: 15; " +
                "-fx-border-color: #a57e38; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 15; " +
                "-fx-text-fill: black;"
            );
        });
        
        return button;
    }
}