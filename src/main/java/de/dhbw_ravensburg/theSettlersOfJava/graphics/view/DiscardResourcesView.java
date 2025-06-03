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

public class DiscardResourcesView {

    private final ObservableMap<ResourceType, Integer> playerResources;
    private final String playerName;
    private final Consumer<Map<ResourceType, Integer>> onDiscard;

    public DiscardResourcesView(String playerName,
                                ObservableMap<ResourceType, Integer> playerResources,
                                Consumer<Map<ResourceType, Integer>> onDiscard) {
        this.playerName = playerName;
        this.playerResources = playerResources;
        this.onDiscard = onDiscard;
    }

	public Node createOverlay() {
    UIFactoryService ui = FXGL.getUIFactoryService();

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
    header.setWrappingWidth(360); // Textumbruch bei kleiner Breite

    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(15);
    grid.setVgap(8);

    Map<ResourceType, Spinner<Integer>> spinners = new HashMap<>();
    int row = 0;

    for (Map.Entry<ResourceType, Integer> entry : playerResources.entrySet()) {
        ResourceType type = entry.getKey();
        Integer maxAmount = entry.getValue();

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
    confirmBtn.setDisable(true);

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
    overlay.setTranslateX(FXGL.getAppWidth()/2 - overlay.getWidth());
    overlay.setTranslateX(FXGL.getAppHeight()/2 - overlay.getHeight());
    return overlay;
}

}
