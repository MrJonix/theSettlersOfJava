package de.dhbw_ravensburg.theSettlersOfJava.game;

import java.util.Random;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

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
    private Entity diceEntity;
    
    public Dice() {
        createDiceEntity();
    }
    
    private void createDiceEntity() {
        StackPane diceView = createDiceView();
        
        diceEntity = FXGL.entityBuilder()
                .view(diceView)
                .buildAndAttach();
        
        // Position in the bottom right corner
        positionInBottomRight();
        
        // Add click handler
        diceView.setOnMouseClicked(e -> rollDice());
    }
    
    private StackPane createDiceView() {
        Rectangle diceBackground = new Rectangle(SIZE, SIZE);
        diceBackground.setFill(Color.WHITE);
        diceBackground.setStroke(Color.BLACK);
        diceBackground.setStrokeWidth(2);
        diceBackground.setArcWidth(15);
        diceBackground.setArcHeight(15);
        
        Text diceText = new Text("?");
        diceText.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        diceText.setFill(Color.BLACK);
        
        StackPane dicePane = new StackPane(diceBackground, diceText);
        dicePane.setAlignment(Pos.CENTER);
        
        return dicePane;
    }
    
    public void positionInBottomRight() {
        // Get screen dimensions
        double screenWidth = FXGL.getAppWidth();
        double screenHeight = FXGL.getAppHeight();
        
        // Set position in bottom right with a small margin
        diceEntity.setX(screenWidth - SIZE - 20);
        diceEntity.setY(screenHeight - SIZE - 20);
    }
    
    private void rollDice() {
        // Generate random number between 2 and 12 (2d6)
        int roll1 = random.nextInt(6) + 1;
        int roll2 = random.nextInt(6) + 1;
        int total = roll1 + roll2;
        
        // Show the result as a message
        FXGL.getDialogService().showMessageBox("Dice Roll: " + total);
    }
    
    public Entity getEntity() {
        return diceEntity;
    }
}