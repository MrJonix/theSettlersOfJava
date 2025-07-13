package de.dhbw_ravensburg.theSettlersOfJava.units;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.game.GameController;

/**
 * Represents a UI button that ends the current player's turn and switches to the next player.
 * 
 * The button includes a hover animation and an icon, and is positioned in the game UI.
 */
public class NextPlayerButton {

    private static final int SIZE = 60;
    private StackPane buttonView;

    /**
     * Constructs the NextPlayerButton and adds it to the UI at the specified position.
     *
     * @param x the horizontal position in the game scene
     * @param y the vertical position in the game scene
     */
    public NextPlayerButton(double x, double y) {
        createButtonView();
        addToUI(x, y);
    }

    /**
     * Creates the visual representation of the button including background, icon,
     * and mouse event handlers for hover and click effects.
     */
    private void createButtonView() {
        Rectangle background = new Rectangle(SIZE, SIZE);
        background.setFill(Color.WHITE);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(2);
        background.setArcWidth(15);
        background.setArcHeight(15);

        // load the icon
        Texture icon = FXGL.getAssetLoader().loadTexture("icons/icon_NEXT.png");
        ImageView iconView = new ImageView(icon.getImage());
        iconView.setFitWidth(30);
        iconView.setFitHeight(30);
        iconView.setPreserveRatio(true);

        buttonView = new StackPane(background, iconView);
        buttonView.setAlignment(Pos.CENTER);

        buttonView.setOnMouseEntered(this::scaleUp);
        buttonView.setOnMouseExited(this::scaleDown);
        buttonView.setOnMouseClicked(e -> onNextPlayer());
    }

    /**
     * Increases the button scale slightly on hover.
     *
     * @param e the mouse event triggering the scale
     */
    private void scaleUp(MouseEvent e) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(100), buttonView);
        scale.setToX(1.1);
        scale.setToY(1.1);
        scale.play();
    }

    /**
     * Resets the button scale when the mouse exits.
     *
     * @param e the mouse event triggering the scale reset
     */
    private void scaleDown(MouseEvent e) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(100), buttonView);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.play();
    }

    /**
     * Ends the current player's turn and transitions to the next player.
     */
    private void onNextPlayer() {
        GameController c = App.getGameController();
        c.endTurn();
    }

    /**
     * Adds the button to the FXGL game UI and sets its position.
     *
     * @param x the X coordinate in the scene
     * @param y the Y coordinate in the scene
     */
    private void addToUI(double x, double y) {
        FXGL.getGameScene().addUINode(buttonView);
        buttonView.setTranslateX(x);
        buttonView.setTranslateY(y);
    }

    /**
     * Returns the button as a JavaFX StackPane node.
     *
     * @return the StackPane containing the button UI
     */
    public StackPane getView() {
        return buttonView;
    }
}
