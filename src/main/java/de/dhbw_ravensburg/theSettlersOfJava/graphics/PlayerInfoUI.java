package de.dhbw_ravensburg.theSettlersOfJava.graphics;

import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

import java.util.List;

import com.almasb.fxgl.dsl.FXGL;

public class PlayerInfoUI {

    private static final Insets PLAYER_BOX_PADDING = new Insets(15);
    private static final double PLAYER_BOX_MIN_WIDTH = 200.0;
    private static final double PLAYER_BOX_MIN_HEIGHT = 100.0;
    private static final CornerRadii CORNER_RADII = new CornerRadii(8);
    private static final BorderWidths BORDER_WIDTHS = new BorderWidths(2);
    private static final String FONT_FAMILY = "Myriad Pro";
    private static final Color DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final Color ACTIVE_PLAYER_BACKGROUND = Color.web("#ecd28f");
    private static final Color INACTIVE_PLAYER_BACKGROUND = Color.WHITE;
    private static final double HEADER_SPACING = 10.0;
    private double stat_hbox_spacing;

    public Pane createPlayerListPanel(List<Player> players, ObjectProperty<Player> currentPlayer) {
        int appWidth = FXGL.getAppWidth();
        int numberOfGaps = players.size() + 1;

        // Calculate the spacing
        double totalPlayerBoxWidth = players.size() * PLAYER_BOX_MIN_WIDTH;
        double availableWidthForSpacing = appWidth - totalPlayerBoxWidth;

        // Ensure spacing is non-negative
        stat_hbox_spacing = Math.max(0, availableWidthForSpacing / numberOfGaps);

        HBox playerListBox = new HBox(stat_hbox_spacing);
        playerListBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-padding: 20;");
        playerListBox.setPrefWidth(appWidth); // Ensure playerListBox uses all available app width

        for (Player player : players) {
            Node playerInfo = createPlayerInfo(player, currentPlayer);
            playerListBox.getChildren().add(playerInfo);
            HBox.setHgrow(playerInfo, Priority.ALWAYS); // Allow each VBox to stretch
        }

        return playerListBox;
    }

    private Node createPlayerInfo(Player player, ObjectProperty<Player> currentPlayer) {
        VBox playerBox = new VBox(HEADER_SPACING);
        playerBox.setBorder(buildPlayerBorder(player));
        playerBox.setPadding(PLAYER_BOX_PADDING);
        playerBox.setMinWidth(PLAYER_BOX_MIN_WIDTH);
        playerBox.setMinHeight(PLAYER_BOX_MIN_HEIGHT);
        playerBox.setMaxWidth(Double.MAX_VALUE); // Allow VBox to grow
        playerBox.backgroundProperty().bind(createBackgroundBinding(player, currentPlayer));

        playerBox.getChildren().addAll(
            createHeader(player),
            createStatLine(player)
        );

        return playerBox;
    }

    private Border buildPlayerBorder(Player player) {
        return new Border(new BorderStroke(
            player.getColor(), BorderStrokeStyle.SOLID, CORNER_RADII, BORDER_WIDTHS));
    }

    private ObjectBinding<Background> createBackgroundBinding(Player player, ObjectProperty<Player> currentPlayer) {
        return Bindings.createObjectBinding(() -> {
            Color fillColor = player.equals(currentPlayer.get()) ? ACTIVE_PLAYER_BACKGROUND : INACTIVE_PLAYER_BACKGROUND;
            return new Background(new BackgroundFill(fillColor, CORNER_RADII, Insets.EMPTY));
        }, currentPlayer);
    }

    private HBox createHeader(Player player) {
        HBox header = new HBox(HEADER_SPACING);

        Circle colorIndicator = new Circle(6, player.getColor());
        Text nameLabel = createBoundText(player.nameProperty(), 20);

        header.getChildren().addAll(colorIndicator, nameLabel);
        header.setFillHeight(true);
        return header;
    }

    private HBox createStatLine(Player player) {
        HBox stats = new HBox();

        Text vp = createBoundText(player.victoryPointsProperty().asString("🏆 %d"), 18);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Text resources = createBoundText(player.resourceSizeProperty().asString("🛠️ %d"), 16);

        stats.getChildren().addAll(vp, spacer, resources);
        return stats;
    }

    private Text createBoundText(javafx.beans.value.ObservableValue<String> property, double fontSize) {
        Text text = new Text();
        text.setFill(DEFAULT_TEXT_COLOR);
        text.setFont(Font.font(FONT_FAMILY, FontWeight.EXTRA_BOLD, fontSize));
        text.textProperty().bind(property);
        return text;
    }
}