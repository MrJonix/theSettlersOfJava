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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * UI component used during the robber phase when a player must discard half of their resources.
 * * This modal overlay blocks other interactions and displays all resource types owned by the player,
 * allowing them to select which ones to discard.
 * The confirm button becomes active only when exactly half of the resources are selected.
 */
public class DiscardResourcesView {

    private final ObservableMap<ResourceType, Integer> playerResources;
    private final String playerName;
    private final Consumer<Map<ResourceType, Integer>> onDiscard;
    private Text selectedAmountText; // New Text element to show selected amount
    private Button confirmBtn; // Make confirmBtn a field so it can be accessed by updateConfirmationState
    private Map<ResourceType, Spinner<Integer>> spinners; // Make spinners a field

    /**
     * Constructs a new discard resource view for the given player.
     *
     * @param playerName the name of the player who must discard resources
     * @param playerResources a map containing the player's current resources
     * @param onDiscard a callback that is executed when the player confirms the discard selection;
     * receives a map of resource types and the amounts to discard
     */
    public DiscardResourcesView(String playerName,
                                ObservableMap<ResourceType, Integer> playerResources,
                                Consumer<Map<ResourceType, Integer>> onDiscard) {
        this.playerName = playerName;
        this.playerResources = playerResources;
        this.onDiscard = onDiscard;
        this.spinners = new HashMap<>(); // Initialize the spinners map
    }

    /**
     * Builds and returns the discard resources overlay.
     * * The overlay includes:
     * - A header explaining the discard requirement.
     * - A grid with spinners for each resource type.
     * - A confirm button that becomes enabled once exactly half of the total resources are selected.
     * * Upon confirmation, the provided callback is invoked with the selected resource amounts.
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
    content.setMaxHeight(400); // Erhöhte Maximalhöhe, um Platz für neue Elemente zu schaffen
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

    // spinners map is now a class field
    // Map<ResourceType, Spinner<Integer>> spinners = new HashMap<>(); // Removed local declaration
    int row = 0;

    for (Map.Entry<ResourceType, Integer> entry : playerResources.entrySet()) {
        ResourceType type = entry.getKey();
        Integer maxAmount = entry.getValue();
        //Skip empty or invalid resource types
        if (type == null || maxAmount == null || maxAmount <= 0) continue;

        Text label = ui.newText(type.name() + " (" + maxAmount + ")", 14);
        label.setFill(Color.WHITE);

        Spinner<Integer> spinner = new Spinner<>(0, maxAmount, 0);
        spinner.setEditable(false); // Can be set to true if direct input is desired
        spinner.setPrefWidth(80); // Slightly larger width for spinners

        // Custom buttons for increment/decrement for better usability
        Button minusBtn = ui.newButton("-");
        minusBtn.setPrefWidth(30);
        minusBtn.setOnAction(e -> spinner.decrement(1));

        Button plusBtn = ui.newButton("+");
        plusBtn.setPrefWidth(30);
        plusBtn.setOnAction(e -> spinner.increment(1));

        HBox spinnerControl = new HBox(5, minusBtn, spinner, plusBtn);
        spinnerControl.setAlignment(Pos.CENTER_LEFT);

        spinners.put(type, spinner);
        grid.add(label, 0, row);
        grid.add(spinnerControl, 1, row); // Add the HBox with custom buttons and spinner
        row++;
    }

    selectedAmountText = ui.newText("Ausgewählt: 0 / Benötigt: 0", 14); // Initialize the text
    selectedAmountText.setFill(Color.WHITE);
    
    Button autoDiscardBtn = ui.newButton("Hälfte automatisch wählen");
    autoDiscardBtn.setStyle("-fx-font-size: 14px;");
    autoDiscardBtn.setOnAction(e -> {
        int totalResources = playerResources.values().stream().mapToInt(Integer::intValue).sum();
        int targetDiscard = totalResources / 2;
        
        // Reset all spinners to 0 first
        spinners.values().forEach(spinner -> spinner.getValueFactory().setValue(0));

        AtomicInteger currentDiscarded = new AtomicInteger(0);

        // Distribute resources proportionally
        playerResources.entrySet().stream()
            .sorted(Map.Entry.comparingByValue((v1, v2) -> v2.compareTo(v1))) // Prioritize discarding from larger stacks
            .forEach(entry -> {
                ResourceType type = entry.getKey();
                int maxAmount = entry.getValue();
                Spinner<Integer> spinner = spinners.get(type);

                if (spinner != null && currentDiscarded.get() < targetDiscard) {
                    int amountToDiscard = Math.min(maxAmount, targetDiscard - currentDiscarded.get());
                    // Try to discard half of the current resource type, but not more than needed for total
                    int halfOfCurrent = (int) Math.ceil(maxAmount / 2.0);
                    int actualDiscard = Math.min(halfOfCurrent, amountToDiscard);
                    
                    spinner.getValueFactory().setValue(actualDiscard);
                    currentDiscarded.addAndGet(actualDiscard);
                }
            });
        
        // If still not enough, take more from largest remaining stacks
        if (currentDiscarded.get() < targetDiscard) {
            playerResources.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((v1, v2) -> v2.compareTo(v1)))
                .forEach(entry -> {
                    ResourceType type = entry.getKey();
                    Spinner<Integer> spinner = spinners.get(type);
                    if (spinner != null && currentDiscarded.get() < targetDiscard) {
                        int currentSpinnerValue = spinner.getValue();
                        int remainingForType = entry.getValue() - currentSpinnerValue;
                        int needed = targetDiscard - currentDiscarded.get();
                        int toAdd = Math.min(remainingForType, needed);
                        spinner.getValueFactory().setValue(currentSpinnerValue + toAdd);
                        currentDiscarded.addAndGet(toAdd);
                    }
                });
        }

        // If too much was selected due to rounding, reduce from smallest stacks
        if (currentDiscarded.get() > targetDiscard) {
            playerResources.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()) // Prioritize reducing from smaller stacks
                .forEach(entry -> {
                    ResourceType type = entry.getKey();
                    Spinner<Integer> spinner = spinners.get(type);
                    if (spinner != null && spinner.getValue() > 0 && currentDiscarded.get() > targetDiscard) {
                        int currentSpinnerValue = spinner.getValue();
                        int excess = currentDiscarded.get() - targetDiscard;
                        int toReduce = Math.min(currentSpinnerValue, excess);
                        spinner.getValueFactory().setValue(currentSpinnerValue - toReduce);
                        currentDiscarded.addAndGet(-toReduce); // Correct the atomic integer
                    }
                });
        }

        updateConfirmationState(); // Call the new private method
    });

    confirmBtn = ui.newButton("Abgeben"); // confirmBtn is now a class field
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
    
    // Add listeners to spinners
    spinners.values().forEach(spinner ->
        spinner.valueProperty().addListener((obs, oldVal, newVal) -> updateConfirmationState())
    );
    updateConfirmationState(); // Initial update of text and button state

    content.getChildren().addAll(header, grid, selectedAmountText, autoDiscardBtn, confirmBtn); // Add new elements

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

    /**
     * Updates the state of the confirm button and the selected amount text.
     * This method calculates the total selected resources and enables/disables
     * the confirm button based on whether exactly half of the total resources are selected.
     */
    private void updateConfirmationState() {
        int selected = spinners.values().stream().mapToInt(Spinner::getValue).sum();
        int total = playerResources.values().stream().mapToInt(Integer::intValue).sum();
        int requiredDiscard = total / 2;
        confirmBtn.setDisable(selected != requiredDiscard);
        selectedAmountText.setText("Ausgewählt: " + selected + " / Benötigt: " + requiredDiscard);
    }
}