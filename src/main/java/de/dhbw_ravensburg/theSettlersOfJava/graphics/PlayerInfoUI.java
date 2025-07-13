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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.util.List;

import com.almasb.fxgl.dsl.FXGL;

/**
 * UI component for displaying a horizontal list of all players.
 * 
 * Each player's box shows their name, color, victory points, longest road length,
 * and resource count. The currently active player is visually highlighted.
 */
public class PlayerInfoUI {

    private static final Insets PLAYER_BOX_PADDING = new Insets(15);
    private static final double PLAYER_BOX_MIN_WIDTH = 200.0;
    private static final double PLAYER_BOX_MIN_HEIGHT = 50.0;
    private static final CornerRadii CORNER_RADII = new CornerRadii(8);
    private static final BorderWidths BORDER_WIDTHS = new BorderWidths(2);
    private static final String FONT_FAMILY = "Myriad Pro";
    private static final Color DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final Color ACTIVE_PLAYER_BACKGROUND = Color.web("#ecd28f");
    private static final Color INACTIVE_PLAYER_BACKGROUND = Color.WHITE;
    private static final double HEADER_SPACING = 10.0;

    private double stat_hbox_spacing; // calculated spacing between player boxes based on app width

    /**
     * Creates a horizontal panel displaying all players.
     * 
     * Each player's box includes stats and highlights the current player.
     *
     * @param players list of all players in the game
     * @param currentPlayer the property indicating the currently active player
     * @return a {@link Pane} containing the styled player boxes
     */
    public Pane createPlayerListPanel(List<Player> players, ObjectProperty<Player> currentPlayer) {
        int appWidth = FXGL.getAppWidth();
        int numberOfGaps = players.size() + 1;
        double totalPlayerBoxWidth = players.size() * PLAYER_BOX_MIN_WIDTH;
        double availableWidthForSpacing = appWidth - totalPlayerBoxWidth;
        stat_hbox_spacing = Math.max(0, availableWidthForSpacing / numberOfGaps);

        HBox playerListBox = new HBox(stat_hbox_spacing);
        playerListBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-padding: 20;");
        playerListBox.setPrefWidth(appWidth);

        // Add all player info boxes to the horizontal layout
        for (Player player : players) {
            Node playerInfo = createPlayerInfo(player, currentPlayer);
            playerListBox.getChildren().add(playerInfo);
            HBox.setHgrow(playerInfo, Priority.ALWAYS);
        }

        return playerListBox;
    }

    /**
     * Creates a styled info box for a single player, including their stats.
     *
     * @param player the player to represent
     * @param currentPlayer the currently active player for highlight comparison
     * @return a {@link Node} representing the player's UI box
     */
    private Node createPlayerInfo(Player player, ObjectProperty<Player> currentPlayer) {
        VBox playerBox = new VBox(HEADER_SPACING);
        playerBox.setBorder(buildPlayerBorder(player));
        playerBox.setPadding(PLAYER_BOX_PADDING);
        playerBox.setMinWidth(PLAYER_BOX_MIN_WIDTH);
        playerBox.setMinHeight(PLAYER_BOX_MIN_HEIGHT);
        playerBox.setMaxWidth(Double.MAX_VALUE);
        playerBox.backgroundProperty().bind(createBackgroundBinding(player, currentPlayer));

        playerBox.getChildren().addAll(
                createHeader(player),
                createStatLine(player)
        );

        return playerBox;
    }

    /**
     * Creates a border around the player's box using their assigned color.
     *
     * @param player the player whose color should be used
     * @return a {@link Border} styled with the player's color
     */
    private Border buildPlayerBorder(Player player) {
        return new Border(new BorderStroke(
                player.getColor(), BorderStrokeStyle.SOLID, CORNER_RADII, BORDER_WIDTHS));
    }

    /**
     * Binds the background color of a player's box to their active status.
     *
     * @param player the player represented
     * @param currentPlayer the currently active player
     * @return an {@link ObjectBinding} that updates the background color dynamically
     */
    private ObjectBinding<Background> createBackgroundBinding(Player player, ObjectProperty<Player> currentPlayer) {
        return Bindings.createObjectBinding(() -> {
            Color fillColor = player.equals(currentPlayer.get()) ? ACTIVE_PLAYER_BACKGROUND : INACTIVE_PLAYER_BACKGROUND;
            return new Background(new BackgroundFill(fillColor, CORNER_RADII, Insets.EMPTY));
        }, currentPlayer);
    }

    /**
     * Creates the header area showing the player's color and name.
     *
     * @param player the player to display
     * @return an {@link HBox} containing the header section
     */
    private HBox createHeader(Player player) {
        HBox header = new HBox(HEADER_SPACING);

        Circle colorIndicator = new Circle(6, player.getColor());
        Text nameLabel = createBoundText(player.nameProperty(), 20);

        header.getChildren().addAll(colorIndicator, nameLabel);
        header.setFillHeight(true);
        return header;
    }

    /**
     * Creates the horizontal stat line showing victory points, longest road,
     * and resource count for the player.
     *
     * @param player the player whose stats are displayed
     * @return an {@link HBox} with player statistics and spacing
     */
    private HBox createStatLine(Player player) {
        HBox stats = new HBox();
        stats.setAlignment(Pos.CENTER_LEFT);
        stats.setSpacing(10);

        HBox vp = createStatIconWithText("icons/icon_SP.png", player.victoryPointsProperty().asString(), 18);
        HBox longR = createStatIconWithText("icons/icon_STREET.png", player.longestRoadProperty().asString(), 18);
        HBox resources = createStatIconWithText("icons/icon_CARDS.png", player.resourceSizeProperty().asString(), 16);

        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        stats.getChildren().addAll(vp, spacer1, longR, spacer2, resources);
        return stats;
    }

    /**
     * Creates an icon and bound text label for a specific player stat.
     *
     * @param imagePath the path to the icon image
     * @param textProperty the value to be displayed
     * @param fontSize the font size for the text
     * @return an {@link HBox} with the icon and dynamic text
     */
    private HBox createStatIconWithText(String imagePath, javafx.beans.value.ObservableValue<String> textProperty, double fontSize) {
        ImageView icon = FXGL.getAssetLoader().loadTexture(imagePath);
        icon.setFitWidth(18);
        icon.setFitHeight(18);

        Text text = new Text();
        text.setFill(DEFAULT_TEXT_COLOR);
        text.setFont(Font.font(FONT_FAMILY, FontWeight.EXTRA_BOLD, fontSize));
        text.textProperty().bind(textProperty);

        HBox box = new HBox(5, icon, text);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    /**
     * Creates a {@link Text} node bound to a given observable string value.
     *
     * @param property the observable text property
     * @param fontSize the font size for the text
     * @return a {@link Text} element with styling and data binding
     */
    private Text createBoundText(javafx.beans.value.ObservableValue<String> property, double fontSize) {
        Text text = new Text();
        text.setFill(DEFAULT_TEXT_COLOR);
        text.setFont(Font.font(FONT_FAMILY, FontWeight.EXTRA_BOLD, fontSize));
        text.textProperty().bind(property);
        return text;
    }
}
