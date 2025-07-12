package de.dhbw_ravensburg.theSettlersOfJava.units;

import java.util.Random;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.game.GameController;
import de.dhbw_ravensburg.theSettlersOfJava.game.GameState;
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

/**
 * Represents a clickable dice button in the game that triggers the dice roll process,
 * displays an animated result view, and interacts with the GameController.
 */
public class Dice {
    public static final int SIZE = 60;
    private final Random random = new Random();
    private StackPane diceView;
    private VBox rollResultView;

    /**
     * Constructs a Dice UI component and adds it to the game scene at the specified coordinates.
     *
     * @param x the horizontal position for the dice UI
     * @param y the vertical position for the dice UI
     */
    public Dice(double x, double y) {
        createDiceView();
        createRollResultView();
        addToUI(x, y);
    }

    /**
     * Creates the visual component of the dice button, including image and hover animation.
     */
    private void createDiceView() {
        Rectangle diceBackground = new Rectangle(SIZE, SIZE);
        diceBackground.setFill(Color.WHITE);
        diceBackground.setStroke(Color.BLACK);
        diceBackground.setStrokeWidth(2);
        diceBackground.setArcWidth(15);
        diceBackground.setArcHeight(15);

        ImageView diceImage = new ImageView(FXGL.getAssetLoader().loadTexture("/dice/dice3D.png").getImage());
        diceImage.setFitWidth(SIZE * 0.7); 
        diceImage.setFitHeight(SIZE * 0.7);
        diceImage.setPreserveRatio(true);
        diceImage.setSmooth(true);

        diceView = new StackPane(diceBackground, diceImage);
        diceView.setAlignment(Pos.CENTER);

        // Hover-effect
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

        //Click to roll dice
        diceView.setOnMouseClicked(e -> rollDice());
    }

    /**
     * Initializes the UI container that will show the result of the dice roll with animation.
     */
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


    /**
     * Adds the dice button and result display to the FXGL UI layer at the specified position.
     *
     * @param x the X position for the dice button
     * @param y the Y position for the dice button
     */
    private void addToUI(double x, double y) {
        // Füge beide UI-Elemente zum GameScene UI-Layer hinzu
        FXGL.getGameScene().addUINode(diceView);
        FXGL.getGameScene().addUINode(rollResultView);

        // Positioniere den Würfel-Button
        diceView.setTranslateX(x);
        diceView.setTranslateY(y);
        

    }

    /**
     * Rolls two dice and triggers the result animation if allowed by the current game state.
     */
    private void rollDice() {
        GameController c = App.getGameController();
        
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

    /**
     * Displays the result of the dice roll with images, text, and animation.
     *
     * @param dice1 the result of the first die
     * @param dice2 the result of the second die
     * @param total the sum of both dice
     */
    private void showDiceRollAnimation(int dice1, int dice2, int total) {
        // load dice image
        ImageView diceImage1 = createDiceImageView(dice1);
        ImageView diceImage2 = createDiceImageView(dice2);
        
        // container for both dice 
        HBox diceContainer = new HBox(30);
        diceContainer.setAlignment(Pos.CENTER);
        diceContainer.getChildren().addAll(diceImage1, diceImage2);
        
        // text for the sum value after rolling dice
        Text resultText = new Text("Würfelergebnis: " + total);
        resultText.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 28));
        resultText.setFill(Color.BLACK);
        
        // White background, for better UX
        Rectangle background = new Rectangle(300, 200);
        background.setFill(Color.WHITE);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(3);
        background.setArcWidth(20);
        background.setArcHeight(20);
        
        // bring together
        rollResultView.getChildren().clear();
        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(diceContainer, resultText);
        
        StackPane completeView = new StackPane(background, contentBox);
        // position result in the center of the frame
        rollResultView.setTranslateX(FXGL.getAppWidth() / 2 - 151.5);
        rollResultView.setTranslateY(FXGL.getAppHeight() / 2 - 101.5);
        rollResultView.getChildren().add(completeView);

        // Create animation
        createRollAnimation();
    }

    /**
     * Creates an ImageView for the specified dice value.
     * Falls back to a simple text display if the image cannot be loaded.
     *
     * @param diceValue the rolled dice value (1-6)
     * @return an ImageView representing the die face
     */
    private ImageView createDiceImageView(int diceValue) {
        try {
            // load pertaining dice image
        	Texture diceTexture = FXGL.getAssetLoader().loadTexture("/dice/dice_" + diceValue + ".png");
        	ImageView imageView = new ImageView(diceTexture.getImage());
            
            // scaling image to an appropriate size
            imageView.setFitWidth(80);
            imageView.setFitHeight(80);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            
            return imageView;
        } catch (Exception e) {
            // Fallback: invisible image placeholder
            Text fallbackText = new Text(String.valueOf(diceValue));
            fallbackText.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 40));
            fallbackText.setFill(Color.BLACK);
            
            Rectangle fallbackBg = new Rectangle(80, 80);
            fallbackBg.setFill(Color.WHITE);
            fallbackBg.setStroke(Color.BLACK);
            fallbackBg.setStrokeWidth(3);
            fallbackBg.setArcWidth(10);
            fallbackBg.setArcHeight(10);
            
            new StackPane(fallbackBg, fallbackText);
            
            // Convert to image view (simplified)
            ImageView fallbackView = new ImageView();
            fallbackView.setFitWidth(80);
            fallbackView.setFitHeight(80);
            
            System.out.println("Warnung: Konnte Würfelbild nicht laden: dice/dice_" + diceValue + ".png");
            return fallbackView;
        }
    }

    /**
     * Creates and plays the animation for showing and hiding the dice result.
     */
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
        
        // 3 second pause
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
        
        // create sequence
        SequentialTransition sequence = new SequentialTransition();
        
        // show result
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
        
        // hide result (at the end)
        sequence.setOnFinished(e -> rollResultView.setVisible(false));
        
        sequence.play();
    }

    /**
     * Returns the dice button as a JavaFX StackPane.
     *
     * @return the dice button UI node
     */
    public StackPane getView() {
        return diceView;
    }
}