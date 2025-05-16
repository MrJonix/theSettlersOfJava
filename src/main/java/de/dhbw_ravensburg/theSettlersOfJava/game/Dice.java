package de.dhbw_ravensburg.theSettlersOfJava.game;

import java.util.Random;

import com.almasb.fxgl.dsl.FXGL;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Dice {
    private static final int SIZE = 60;
    private final Random random = new Random();
    private StackPane diceView;
    
    public Dice() {
        createDiceView();
        addToUI();
    }

    private void createDiceView() {
        Rectangle diceBackground = new Rectangle(SIZE, SIZE);
        diceBackground.setFill(Color.WHITE);
        diceBackground.setStroke(Color.BLACK);
        diceBackground.setStrokeWidth(2);
        diceBackground.setArcWidth(15);
        diceBackground.setArcHeight(15);

        Text diceText = new Text("?");
        diceText.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        diceText.setFill(Color.BLACK);

        diceView = new StackPane(diceBackground, diceText);
        diceView.setAlignment(Pos.CENTER);

        diceView.setOnMouseClicked(e -> rollDice());
    }

    private void addToUI() {
        // Berechne Position unten rechts mit Rand
        double x = FXGL.getAppWidth() - SIZE - 20;
        double y = FXGL.getAppHeight() - SIZE - 20;

        // Füge das UI-Element zum GameScene UI-Layer hinzu
        FXGL.getGameScene().addUINode(diceView);

        // Positioniere das UI-Element manuell
        diceView.setTranslateX(x);
        diceView.setTranslateY(y);
    }


    private void rollDice() {
        int roll1 = random.nextInt(6) + 1;
        int roll2 = random.nextInt(6) + 1;
        int total = roll1 + roll2;

        FXGL.getDialogService().showMessageBox("Dice Roll: " + total);
    }

    public StackPane getView() {
        return diceView;
    }
}
