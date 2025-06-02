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

public class NextPlayerButton {

    private static final int SIZE = 60;
    private StackPane buttonView;

    public NextPlayerButton(double x, double y) {
        createButtonView();
        addToUI(x, y);
    }

    private void createButtonView() {
        Rectangle background = new Rectangle(SIZE, SIZE);
        background.setFill(Color.WHITE);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(2);
        background.setArcWidth(15);
        background.setArcHeight(15);

        // Lade das Icon
        Texture icon = FXGL.getAssetLoader().loadTexture("/icons/icon_NEXT.png");
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

    private void scaleUp(MouseEvent e) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(100), buttonView);
        scale.setToX(1.1);
        scale.setToY(1.1);
        scale.play();
    }

    private void scaleDown(MouseEvent e) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(100), buttonView);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.play();
    }

    private void onNextPlayer() {
        GameController c = App.getGameController();
        c.endTurn();
    }

    private void addToUI(double x, double y) {
        FXGL.getGameScene().addUINode(buttonView);
        buttonView.setTranslateX(x);
        buttonView.setTranslateY(y);
    }

    public StackPane getView() {
        return buttonView;
    }
}
