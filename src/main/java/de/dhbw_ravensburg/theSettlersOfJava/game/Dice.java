package de.dhbw_ravensburg.theSettlersOfJava.game;

import java.util.Random;

import com.almasb.fxgl.dsl.FXGL;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Dice {
    public static final int SIZE = 60;
    private final Random random = new Random();
    private StackPane diceView;
    
    public Dice(double x, double y) {
        createDiceView();
        addToUI(x,y);
    }

    private void createDiceView() {
        Rectangle diceBackground = new Rectangle(SIZE, SIZE);
        diceBackground.setFill(Color.WHITE);
        diceBackground.setStroke(Color.BLACK);
        diceBackground.setStrokeWidth(2);
        diceBackground.setArcWidth(15);
        diceBackground.setArcHeight(15);

        Text diceText = new Text("?");
        diceText.setFont(Font.font("Myriad Pro", FontWeight.BOLD, 30));
        diceText.setFill(Color.BLACK);

        diceView = new StackPane(diceBackground, diceText);
        diceView.setAlignment(Pos.CENTER);

        diceView.setOnMouseClicked(e -> rollDice());
    }

    private void addToUI(double x, double y) {
        // Berechne Position unten rechts mit Rand


        // Füge das UI-Element zum GameScene UI-Layer hinzu
        FXGL.getGameScene().addUINode(diceView);

        // Positioniere das UI-Element manuell
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
        
        FXGL.getNotificationService().pushNotification("Würfelergebnis: " + total);
        c.onDiceRolled(total); // Delegiere Ergebnis an GameController
    }


    public StackPane getView() {
        return diceView;
    }
}
