package de.dhbw_ravensburg.theSettlersOfJava.game;

import java.util.Random;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Dice {
    public static final int SIZE = 60;
    private final Random random = new Random();
    private StackPane diceView;
    private VBox rollResultView;

    
    public Dice(double x, double y) {
        createDiceView();
        createRollResultView();
        addToUI(x, y);
    }

    private void createDiceView() {
        Rectangle diceBackground = new Rectangle(SIZE, SIZE);
        diceBackground.setFill(Color.WHITE);
        diceBackground.setStroke(Color.BLACK);
        diceBackground.setStrokeWidth(2);
        diceBackground.setArcWidth(15);
        diceBackground.setArcHeight(15);

        Text diceText = new Text("🎲");
        diceText.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 25));
        diceText.setFill(Color.BLACK);

        diceView = new StackPane(diceBackground, diceText);
        diceView.setAlignment(Pos.CENTER);

        // Hover-Effekt
        diceView.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), diceView);
            scale.setToX(1.1);
            scale.setToY(1.1);
            scale.play();
        });
        
        diceView.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), diceView);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        diceView.setOnMouseClicked(e -> rollDice());
    }

    private void createRollResultView() {
        rollResultView = new VBox(10);
        rollResultView.setAlignment(Pos.CENTER);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        shadow.setRadius(10);
        rollResultView.setEffect(shadow);
    }


    private void addToUI(double x, double y) {
        // Füge beide UI-Elemente zum GameScene UI-Layer hinzu
        FXGL.getGameScene().addUINode(diceView);
        FXGL.getGameScene().addUINode(rollResultView);

        // Positioniere den Würfel-Button
        diceView.setTranslateX(x);
        diceView.setTranslateY(y);
        

    }

    private void rollDice() {
        GameController c = App.getGameController();
        
        // Nur würfeln, wenn der aktuelle State ROLL_DICE ist
        if (c.getCurrentGameState() != GameState.ROLL_DICE) {
            FXGL.getNotificationService().pushNotification("Du kannst jetzt nicht würfeln!");
            return;
        }

        int roll1 = random.nextInt(6) + 1;
        int roll2 = random.nextInt(6) + 1;
        int total = roll1 + roll2;
        
        showDiceRollAnimation(roll1, roll2, total);
        c.onDiceRolled(total); // Delegiere Ergebnis an GameController
    }

    private void showDiceRollAnimation(int dice1, int dice2, int total) {
        // Lade die Würfelbilder
        ImageView diceImage1 = createDiceImageView(dice1);
        ImageView diceImage2 = createDiceImageView(dice2);
        
        // Container für die beiden Würfel
        HBox diceContainer = new HBox(30);
        diceContainer.setAlignment(Pos.CENTER);
        diceContainer.getChildren().addAll(diceImage1, diceImage2);
        
        // Text für das Gesamtergebnis
        Text resultText = new Text("Würfelergebnis: " + total);
        resultText.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 28));
        resultText.setFill(Color.BLACK);
        
        // Hintergrund für bessere Lesbarkeit (weißer Hintergrund)
        Rectangle background = new Rectangle(300, 200);
        background.setFill(Color.WHITE);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(3);
        background.setArcWidth(20);
        background.setArcHeight(20);
        
        // Alles zusammenfügen
        rollResultView.getChildren().clear();
        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(diceContainer, resultText);
        
        StackPane completeView = new StackPane(background, contentBox);
        // Positioniere das Ergebnis-Display in der Bildschirmmitte
        rollResultView.setTranslateX(FXGL.getAppWidth() / 2 - 151.5);
        rollResultView.setTranslateY(FXGL.getAppHeight() / 2 - 101.5);
        rollResultView.getChildren().add(completeView);

        // Animation erstellen
        createRollAnimation();
    }

    private ImageView createDiceImageView(int diceValue) {
        try {
            // Lade das entsprechende Würfelbild
        	Texture diceTexture = FXGL.getAssetLoader().loadTexture("/dice/dice_" + diceValue + ".png");
        	ImageView imageView = new ImageView(diceTexture.getImage());
            
            // Skaliere das Bild auf eine angemessene Größe
            imageView.setFitWidth(80);
            imageView.setFitHeight(80);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            
            return imageView;
        } catch (Exception e) {
            // Fallback: Erstelle einen Text-basierten Würfel falls Bild nicht geladen werden kann
            Text fallbackText = new Text(String.valueOf(diceValue));
            fallbackText.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 40));
            fallbackText.setFill(Color.BLACK);
            
            Rectangle fallbackBg = new Rectangle(80, 80);
            fallbackBg.setFill(Color.WHITE);
            fallbackBg.setStroke(Color.BLACK);
            fallbackBg.setStrokeWidth(3);
            fallbackBg.setArcWidth(10);
            fallbackBg.setArcHeight(10);
            
            StackPane fallbackPane = new StackPane(fallbackBg, fallbackText);
            
            // Konvertiere zu ImageView (vereinfacht)
            ImageView fallbackView = new ImageView();
            fallbackView.setFitWidth(80);
            fallbackView.setFitHeight(80);
            
            System.out.println("Warnung: Konnte Würfelbild nicht laden: dice/dice_" + diceValue + ".png");
            return fallbackView;
        }
    }

    private void createRollAnimation() {
        // Fade In Animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), rollResultView);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        
        // Scale In Animation
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), rollResultView);
        scaleIn.setFromX(0.5);
        scaleIn.setFromY(0.5);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);
        
        // Pause für 3 Sekunden
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        
        // Fade Out Animation
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), rollResultView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        // Scale Out Animation
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(300), rollResultView);
        scaleOut.setFromX(1.0);
        scaleOut.setFromY(1.0);
        scaleOut.setToX(0.5);
        scaleOut.setToY(0.5);
        
        // Sequenz erstellens
        SequentialTransition sequence = new SequentialTransition();
        
        // Zeige das Ergebnis
        sequence.getChildren().add(new javafx.animation.Transition() {
            {
                setCycleDuration(Duration.millis(1));
            }
            @Override
            protected void interpolate(double frac) {
                rollResultView.setVisible(true);
            }
        });
        
        sequence.getChildren().addAll(fadeIn, scaleIn, pause, fadeOut, scaleOut);
        
        // Verstecke das Ergebnis am Ende
        sequence.setOnFinished(e -> rollResultView.setVisible(false));
        
        sequence.play();
    }

    public StackPane getView() {
        return diceView;
    }
}