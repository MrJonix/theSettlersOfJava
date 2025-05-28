package de.dhbw_ravensburg.theSettlersOfJava.graphics;

import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.beans.property.ObjectProperty;
import javafx.collections.MapChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.EnumMap;
import java.util.Map;

import com.almasb.fxgl.dsl.FXGL;

import static com.almasb.fxgl.dsl.FXGL.*;

public class CurrentPlayerInfoUI {

    private final VBox root;
    private final Label nameLabel = new Label();
    private final Label pointsLabel = new Label();
    private final Circle colorIndicator = new Circle(8);
    private final GridPane resourceGrid = new GridPane();
    private final Map<ResourceType, Label> resourceLabels = new EnumMap<>(ResourceType.class);

    private Player currentPlayer;


    // Listener für Ressourcenänderungen
    private final MapChangeListener<ResourceType, Integer> resourceListener = change -> {
        if (currentPlayer != null) {
            updateResourceDisplay(currentPlayer);
        }
    };

    public CurrentPlayerInfoUI(ObjectProperty<Player> currentPlayerProperty) {
        root = new VBox(15);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 6; -fx-background-radius: 6;");

        nameLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        pointsLabel.setStyle("-fx-font-size: 13;");

        root.getChildren().addAll(
                createHeader(),
                pointsLabel,
                createResourceSection()
        );

        // Spielerwechsel-Listener
        currentPlayerProperty.addListener((obs, oldPlayer, newPlayer) -> {
            if (oldPlayer != null) {
                nameLabel.textProperty().unbind();
                pointsLabel.textProperty().unbind();
                oldPlayer.resourcesProperty().removeListener(resourceListener);
            }
            if (newPlayer != null) {
                newPlayer.resourcesProperty().addListener(resourceListener);
                updateInfo(newPlayer);
                updateResourceDisplay(newPlayer);
            }
        });

        // Initialisierung bei bestehendem Spieler
        if (currentPlayerProperty.get() != null) {
            Player p = currentPlayerProperty.get();
            p.resourcesProperty().addListener(resourceListener);
            updateInfo(p);
            updateResourceDisplay(p);
        }
    }

    private HBox createHeader() {
        HBox header = new HBox(10);
        header.getChildren().addAll(colorIndicator, nameLabel);
        return header;
    }

    private VBox createResourceSection() {
        VBox resourceSection = new VBox(5);

        Label titleLabel = new Label("Rohstoffe:");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");
        resourceSection.getChildren().add(titleLabel);

        resourceGrid.setHgap(10);
        resourceGrid.setVgap(6);
        resourceGrid.getColumnConstraints().add(new ColumnConstraints(120));

        int row = 0;
        for (ResourceType type : ResourceType.values()) {
        	ImageView iconView = FXGL.getAssetLoader().loadTexture(type.getImagePath());
        	iconView.setFitWidth(20);
            iconView.setPreserveRatio(true);

            Label typeLabel = new Label(type.name() + ":");
            typeLabel.setFont(new Font("Myriad Pro", 20));
            typeLabel.setTooltip(new Tooltip("Anzahl der " + type.name()));

            HBox labelBox = new HBox(5, iconView, typeLabel);

            Label countLabel = new Label("0");
            resourceLabels.put(type, countLabel);

            resourceGrid.addRow(row++, labelBox, countLabel);
        }

        resourceSection.getChildren().add(resourceGrid);
        return resourceSection;
    }

    private void updateInfo(Player player) {
        this.currentPlayer = player;

        nameLabel.textProperty().bind(player.nameProperty().concat(": "));
        colorIndicator.setFill(player.getColor() != null ? player.getColor() : Color.GRAY);
        pointsLabel.textProperty().bind(player.victoryPointsProperty().asString("Siegpunkte: %d"));
    }

    private void updateResourceDisplay(Player player) {
        for (ResourceType type : ResourceType.values()) {
            Integer amount = player.getResources().get(type);
            resourceLabels.get(type).setText(String.valueOf(amount != null ? amount : 0));
        }
    }

    public VBox getRoot() {
        return root;
    }
}
