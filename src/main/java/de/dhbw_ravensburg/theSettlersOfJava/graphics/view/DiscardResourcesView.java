package de.dhbw_ravensburg.theSettlersOfJava.graphics.view;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.UIFactoryService;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * UI component used during the robber phase when a player must discard half of their resources.
 * 
 * This modal overlay blocks other interactions and displays all resource types owned by the player,
 * allowing them to select which ones to discard.
 * The confirm button becomes active only when exactly half of the resources are selected.
 */
public class DiscardResourcesView {

    private final ObservableMap<ResourceType, Integer> playerResources;
    private final String playerName;
    private final Consumer<Map<ResourceType, Integer>> onDiscard;

    /**
     * Constructs a new discard resource view for the given player.
     *
     * @param playerName the name of the player who must discard resources
     * @param playerResources a map containing the player's current resources
     * @param onDiscard a callback that is executed when the player confirms the discard selection;
     *                  receives a map of resource types and the amounts to discard
     */
    public DiscardResourcesView(String playerName,
                                ObservableMap<ResourceType, Integer> playerResources,
                                Consumer<Map<ResourceType, Integer>> onDiscard) {
        this.playerName = playerName;
        this.playerResources = playerResources;
        this.onDiscard = onDiscard;
    }

    /**
     * Builds and returns the discard resources overlay.
     * 
     * The overlay includes:
     * - A header explaining the discard requirement.
     * - A grid with spinners for each resource type.
     * - A confirm button that becomes enabled once exactly half of the total resources are selected.
     * 
     * Upon confirmation, the provided callback is invoked with the selected resource amounts.
     *
     * @return a {@link Node} representing the centered discard resource dialog overlay
     */
	public Node createOverlay() {
    UIFactoryService ui = FXGL.getUIFactoryService();

    // Main container with padding, background, and shadow
    VBox content = new VBox(20);
    content.setAlignment(Pos.CENTER);
    content.setPadding(new Insets(25));
    content.setMaxWidth(400);  // Maximalbreite fixiert
    content.setMaxHeight(350); // Maximalhöhe fixiert
    content.setBackground(new Background(new BackgroundFill(
        Color.rgb(0, 0, 0, 0.85), new CornerRadii(12), Insets.EMPTY
    )));

    Text header = ui.newText(playerName + " muss die Hälfte seiner Rohstoffe abgeben!", 20);
    header.setFill(Color.WHITE);
    header.setWrappingWidth(360); // Enable line wrapping for narrow layouts

    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(15);
    grid.setVgap(8);

    Map<ResourceType, Spinner<Integer>> spinners = new HashMap<>();
    int row = 0;

    for (Map.Entry<ResourceType, Integer> entry : playerResources.entrySet()) {
        ResourceType type = entry.getKey();
        Integer maxAmount = entry.getValue();
        //Skip empty or invalid resource types
        if (type == null || maxAmount == null || maxAmount <= 0) continue;

        Text label = ui.newText(type.name() + " (" + maxAmount + ")", 14);
        label.setFill(Color.WHITE);

        Spinner<Integer> spinner = new Spinner<>(0, maxAmount, 0);
        spinner.setEditable(false);
        spinner.setPrefWidth(70);

        spinners.put(type, spinner);
        grid.add(label, 0, row);
        grid.add(spinner, 1, row);
        row++;
    }

    Button confirmBtn = ui.newButton("Abgeben");
    confirmBtn.setStyle("-fx-font-size: 14px;");
    confirmBtn.setDisable(true); // Disabled by default; becomes enabled when exactly half the resources are selected


    confirmBtn.setOnAction(e -> {
        Map<ResourceType, Integer> discarded = new HashMap<>();
        spinners.forEach((type, spinner) -> {
            int amount = spinner.getValue();
            if (amount > 0) {
                discarded.put(type, amount);
            }
        });

        onDiscard.accept(discarded);

        StackPane overlay = (StackPane) content.getParent();
        ((Pane) overlay.getParent()).getChildren().remove(overlay);
    });
    // Dynamically enables the confirm button if the selected total equals exactly half the player's resources
    Runnable updateConfirm = () -> {
        int selected = spinners.values().stream().mapToInt(Spinner::getValue).sum();
        int total = playerResources.values().stream().mapToInt(Integer::intValue).sum();
        confirmBtn.setDisable(selected != total / 2);
    };

    spinners.values().forEach(spinner ->
        spinner.valueProperty().addListener((obs, oldVal, newVal) -> updateConfirm.run())
    );
    updateConfirm.run();

    content.getChildren().addAll(header, grid, confirmBtn);

    StackPane overlay = new StackPane(content);

    // Initial position based on assumed max dimensions
    // This provides a good starting point before actual layout is computed
    overlay.setTranslateX(FXGL.getAppWidth() / 2 - content.getMaxWidth() / 2);
    overlay.setTranslateY(FXGL.getAppHeight() / 2 - content.getMaxHeight() / 2);

    // Listener to precisely center the overlay after it has been laid out
    overlay.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
        overlay.setTranslateX(FXGL.getAppWidth() / 2 - newBounds.getWidth() / 2);
        overlay.setTranslateY(FXGL.getAppHeight() / 2 - newBounds.getHeight() / 2);
    });
    return overlay;
}

}