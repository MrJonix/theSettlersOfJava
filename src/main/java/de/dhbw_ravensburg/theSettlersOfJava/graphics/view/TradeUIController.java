package de.dhbw_ravensburg.theSettlersOfJava.graphics.view;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.UIFactoryService;

import de.dhbw_ravensburg.theSettlersOfJava.resources.HarborType;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.Map;
import java.util.Set;
import de.dhbw_ravensburg.theSettlersOfJava.game.*;
import de.dhbw_ravensburg.theSettlersOfJava.game.TradeOffer;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class TradeUIController {

    private Supplier<Player> currentPlayerSupplier;
    private List<Player> allPlayers;
    private Button tradeButton;
    private boolean tradeDialogOpen = false;

    public TradeUIController(Supplier<Player> currentPlayerSupplier, List<Player> allPlayers) {
        this.currentPlayerSupplier = currentPlayerSupplier;
        this.allPlayers = allPlayers;
        FXGL.set("tradeUI", this);
    }
    private Player getCurrentPlayer() {
    	return currentPlayerSupplier.get();
    }

    public void initTradeButton() {
        ImageView icon = new ImageView(new Image("assets/textures/icons/icon_TRADE.png"));
        icon.setFitWidth(45);
        icon.setFitHeight(45);

        tradeButton = new Button();
        tradeButton.setGraphic(icon);
        tradeButton.setMaxSize(60, 60);
        tradeButton.setStyle("""
        	    -fx-background-color: white;
        	    -fx-border-color: black;
        	    -fx-border-width: 2px;
        	    -fx-background-radius: 10;
        	    -fx-border-radius: 10;
        	""");        
        tradeButton.setVisible(false);
        tradeButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            tradeButton.setScaleX(1.1);
            tradeButton.setScaleY(1.1);
        });
        tradeButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            tradeButton.setScaleX(1.0);
            tradeButton.setScaleY(1.0);
        });

        tradeButton.setOnAction(e -> openTradeDialog());
        tradeButton.setTranslateX((FXGL.getAppWidth() - 80));
        tradeButton.setTranslateY(100);

        FXGL.getGameScene().addUINode(tradeButton);
    }

    public void showTradeButton() {
    	if (tradeButton != null) {
    		tradeButton.setVisible(true);
    	}
    }
    public void hideTradeButton() {
    	if (tradeButton != null) {
    		tradeButton.setVisible(false);
    	}
    }
    public void openTradeDialog() {
    	if (tradeDialogOpen) {
            return;
        }
        tradeDialogOpen = true;
        UIFactoryService ui = FXGL.getUIFactoryService();

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(25));
        content.setMinWidth(400);
        content.setMaxWidth(500);
        content.setBackground(new Background(new BackgroundFill(
            Color.rgb(0, 0, 0, 0.85), new CornerRadii(12), Insets.EMPTY
        )));

        Text header = ui.newText("Handelsoption wählen", 16);
        header.setFill(Color.WHITE);

        Button playerTradeBtn = ui.newButton("Mitspieler handeln");
        Button harbourTradeBtn = ui.newButton("Mit dem Hafen handeln");
        playerTradeBtn.setMinWidth(250);
        harbourTradeBtn.setMinWidth(250);
        playerTradeBtn.setPrefWidth(400);
        harbourTradeBtn.setMaxWidth(400);
        playerTradeBtn.setMaxWidth(450);
        harbourTradeBtn.setPrefWidth(450);
        StackPane overlay = new StackPane(content);
        StackPane close = createCloseableContent(content, () -> {
            FXGL.getGameScene().removeUINode(overlay);
            tradeDialogOpen = false;
        });
        overlay.getChildren().add(close);
        FXGL.getGameScene().addUINode(overlay);
        centerOverlay(overlay, content);

        playerTradeBtn.setOnAction(e -> {
            FXGL.getGameScene().removeUINode(overlay);
            openPlayerTrade();
        });

        harbourTradeBtn.setOnAction(e -> {
            FXGL.getGameScene().removeUINode(overlay);
            openHarbourTrade();
        });

        content.getChildren().addAll(header, playerTradeBtn, harbourTradeBtn);
    }
    public void openHarbourTrade() {
        Player player = getCurrentPlayer();
        GameBoard game = FXGL.geto("GameBoard");
        Set<HarborType> harborTypes = game.getAvailibeHarborTypes(player);
        UIFactoryService ui = FXGL.getUIFactoryService();

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(25));
        content.setMaxWidth(400);
        content.setBackground(new Background(new BackgroundFill(
            Color.rgb(0, 0, 0, 0.85), new CornerRadii(12), Insets.EMPTY
        )));

        Text header = ui.newText("Hafenhandel", 16);
        header.setFill(Color.WHITE);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setAlignment(Pos.CENTER);

        Map<ResourceType, Spinner<Integer>> offeredMap = new EnumMap<>(ResourceType.class);
        ResourceType[] types = ResourceType.values();

        for (int i = 0; i < types.length; i++) {
            ResourceType type = types[i];
            Spinner<Integer> spinner = new Spinner<>(0, 100, 0);
            offeredMap.put(type, spinner);
            grid.add(ui.newText(type.name(), 14), 0, i);
            grid.add(spinner, 1, i);
        }

        ComboBox<ResourceType> requestBox = new ComboBox<>();
        requestBox.getItems().addAll(types);
        requestBox.setPromptText("Wähle gewünschte Ressource");

        Button tradeBtn = ui.newButton("Tauschen");
        tradeBtn.setOnAction(e -> {
            ResourceType requested = requestBox.getValue();
            if (requested == null) {
                FXGL.showMessage("Bitte gewünschte Ressource wählen.");
                return;
            }
            ResourceType offered = null;
            int amountOffered = 0;
            for (Map.Entry<ResourceType, Spinner<Integer>> entry : offeredMap.entrySet()) {
                if (entry.getValue().getValue() > 0) {
                    offered = entry.getKey();
                    amountOffered = entry.getValue().getValue();
                    break;
                }
            }
            if (offered == null || amountOffered == 0) {
                FXGL.showMessage("Bitte eine Ressource angeben, die du anbieten möchtest.");
                return;
            }
            int ratio = 4;
            if (harborTypes.contains(HarborType.THREE_TO_ONE)) {
                ratio = 3;
            }

            for (HarborType ht : harborTypes) {
                if (ht.isSpecific() && ht.toResourceType() == offered) {
                    ratio = 2;
                    break;
                }
            }

            if (amountOffered < ratio) {
                FXGL.showMessage("Du brauchst mindestens " + ratio + "x " + offered.name() + " für den Tausch.");
                return;
            }

            Map<ResourceType, Integer> offeredMapTrade = new EnumMap<>(ResourceType.class);
            offeredMapTrade.put(offered, ratio);

            Map<ResourceType, Integer> requestedMapTrade = new EnumMap<>(ResourceType.class);
            requestedMapTrade.put(requested, 1);

            if (!player.hasResources(offeredMapTrade)) {
                FXGL.showMessage("Du hast nicht genug Ressourcen für diesen Tausch.");
                return;
            }

            player.removeResourcesMap(offeredMapTrade);
            player.addResourcesMap(requestedMapTrade);

            FXGL.showMessage("Tausch erfolgreich: " + ratio + "x " + offered.name() + " gegen 1x " + requested.name() + ".");
        });

        content.getChildren().addAll(header, grid, requestBox, tradeBtn);

        StackPane overlay = new StackPane();
        Node closable = createCloseableContent(content, () -> {
            FXGL.getGameScene().removeUINode(overlay);
            tradeDialogOpen = false;
        });
        overlay.getChildren().add(closable);

        FXGL.getGameScene().addUINode(overlay);
        centerOverlay(overlay, content);
    }

    public void openPlayerTrade() {
        UIFactoryService ui = FXGL.getUIFactoryService();

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(25));
        content.setMaxWidth(400);
        content.setBackground(new Background(new BackgroundFill(
            Color.rgb(0, 0, 0, 0.85), new CornerRadii(12), Insets.EMPTY
        )));

        Text header = ui.newText("Handel konfigurieren", 20);
        header.setFill(Color.WHITE);

        Map<ResourceType, Spinner<Integer>> offeredMap = new EnumMap<>(ResourceType.class);
        Map<ResourceType, Spinner<Integer>> requestedMap = new EnumMap<>(ResourceType.class);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setAlignment(Pos.CENTER);

        int row = 0;
        grid.add(ui.newText("Du bietest:", 16), 0, row++, 2, 1);
        for (ResourceType type : ResourceType.values()) {
            Spinner<Integer> spinner = new Spinner<>(0, 10, 0);
            offeredMap.put(type, spinner);
            grid.add(ui.newText(type.name(), 14), 0, row);
            grid.add(spinner, 1, row++);
        }

        grid.add(ui.newText("Du möchtest:", 16), 0, row++, 2, 1);
        for (ResourceType type : ResourceType.values()) {
            Spinner<Integer> spinner = new Spinner<>(0, 10, 0);
            requestedMap.put(type, spinner);
            grid.add(ui.newText(type.name(), 14), 0, row);
            grid.add(spinner, 1, row++);
        }

        Button nextBtn = ui.newButton("Weiter");
        Button backBtn = ui.newButton("Zurück");

        StackPane overlay = new StackPane(content);
        StackPane close = createCloseableContent(content, () -> {
            FXGL.getGameScene().removeUINode(overlay);
            tradeDialogOpen = false;
        });
        overlay.getChildren().add(close);
        
        FXGL.getGameScene().addUINode(overlay);
        centerOverlay(overlay, content);

        nextBtn.setOnAction(e -> {
            Map<ResourceType, Integer> offered = new EnumMap<>(ResourceType.class);
            Map<ResourceType, Integer> requested = new EnumMap<>(ResourceType.class);
            boolean hasOffered = false;
            boolean hasRequested = false;

            for (Map.Entry<ResourceType, Spinner<Integer>> entry : offeredMap.entrySet()) {
                int amount = entry.getValue().getValue();
                if (amount > 0) {
                    offered.put(entry.getKey(), amount);
                    hasOffered = true;
                }
            }
            for (Map.Entry<ResourceType, Spinner<Integer>> entry : requestedMap.entrySet()) {
                int amount = entry.getValue().getValue();
                if (amount > 0) {
                    requested.put(entry.getKey(), amount);
                    hasRequested = true;
                }
            }

            if (!hasOffered || !hasRequested) {
                FXGL.showMessage("Bitte gib sowohl angebotene als auch gewünschte Ressourcen an.");
                return;
            }

            openPlayerSelectDialog(offered, requested);
            FXGL.getGameScene().removeUINode(overlay);
        });
        backBtn.setOnAction(e -> {
        	FXGL.getGameScene().removeUINode(overlay);
        	openTradeDialog();
        });

        content.getChildren().addAll(header, grid, nextBtn);
    }

    public void openPlayerSelectDialog(Map<ResourceType, Integer> offered, Map<ResourceType, Integer> requested) {
        UIFactoryService ui = FXGL.getUIFactoryService();

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(25));
        content.setMaxWidth(400);
        content.setBackground(new Background(new BackgroundFill(
            Color.rgb(0, 0, 0, 0.85), new CornerRadii(12), Insets.EMPTY
        )));

        Text header = ui.newText("Mitspieler auswählen", 20);
        header.setFill(Color.WHITE);

        ToggleGroup group = new ToggleGroup();
        VBox options = new VBox(10);
        for (Player p : allPlayers) {
            if (!p.equals(getCurrentPlayer())) {
                RadioButton btn = new RadioButton(p.getName());
                btn.setTextFill(Color.WHITE);
                btn.setToggleGroup(group);
                btn.setUserData(p);
                options.getChildren().add(btn);
            }
        }

        Button sendBtn = ui.newButton("Angebot senden");

        StackPane overlay = new StackPane(content);
        FXGL.getGameScene().addUINode(overlay);
        centerOverlay(overlay, content);

        sendBtn.setOnAction(e -> {
            if (group.getSelectedToggle() != null) {
                Player selected = (Player) group.getSelectedToggle().getUserData();
                TradeOffer offer = new TradeOffer(getCurrentPlayer(), selected, offered, requested);
                selected.receiveTradeOffer(offer);
                FXGL.getGameScene().removeUINode(overlay);
            } else {
                FXGL.showMessage("Bitte wähle einen Mitspieler aus.");
            }
        });

        content.getChildren().addAll(header, options, sendBtn);
    }

    public void openTradeDialogWithTarget(Player fromPlayer, Player toPlayer) {
        UIFactoryService ui = FXGL.getUIFactoryService();

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(25));
        content.setMaxWidth(400);
        content.setBackground(new Background(new BackgroundFill(
            Color.rgb(0, 0, 0, 0.85), new CornerRadii(12), Insets.EMPTY
        )));

        Text header = ui.newText("Gegenangebot an " + toPlayer.getName(), 20);
        header.setFill(Color.WHITE);

        Map<ResourceType, Spinner<Integer>> offeredMap = new EnumMap<>(ResourceType.class);
        Map<ResourceType, Spinner<Integer>> requestedMap = new EnumMap<>(ResourceType.class);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setAlignment(Pos.CENTER);

        int row = 0;
        grid.add(ui.newText("Du bietest:", 16), 0, row++, 2, 1);
        for (ResourceType type : ResourceType.values()) {
            Spinner<Integer> spinner = new Spinner<>(0, 10, 0);
            offeredMap.put(type, spinner);
            grid.add(ui.newText(type.name(), 14), 0, row);
            grid.add(spinner, 1, row++);
        }

        grid.add(ui.newText("Du möchtest:", 16), 0, row++, 2, 1);
        for (ResourceType type : ResourceType.values()) {
            Spinner<Integer> spinner = new Spinner<>(0, 10, 0);
            requestedMap.put(type, spinner);
            grid.add(ui.newText(type.name(), 14), 0, row);
            grid.add(spinner, 1, row++);
        }

        Button sendBtn = ui.newButton("Senden");

        StackPane overlay = new StackPane(content);
        StackPane close = createCloseableContent(content, () -> {
            FXGL.getGameScene().removeUINode(overlay);
            tradeDialogOpen = false;
        });
        overlay.getChildren().add(close);
        FXGL.getGameScene().addUINode(overlay);
        centerOverlay(overlay, content);

        sendBtn.setOnAction(e -> {
            Map<ResourceType, Integer> offered = new EnumMap<>(ResourceType.class);
            Map<ResourceType, Integer> requested = new EnumMap<>(ResourceType.class);

            boolean hasOffered = false;
            boolean hasRequested = false;

            for (Map.Entry<ResourceType, Spinner<Integer>> entry : offeredMap.entrySet()) {
                int amount = entry.getValue().getValue();
                if (amount > 0) {
                    offered.put(entry.getKey(), amount);
                    hasOffered = true;
                }
            }

            for (Map.Entry<ResourceType, Spinner<Integer>> entry : requestedMap.entrySet()) {
                int amount = entry.getValue().getValue();
                if (amount > 0) {
                    requested.put(entry.getKey(), amount);
                    hasRequested = true;
                }
            }

            if (!hasOffered || !hasRequested) {
                FXGL.showMessage("Bitte gib sowohl angebotene als auch gewünschte Ressourcen an.");
                return;
            }

            TradeOffer counterOffer = new TradeOffer(fromPlayer, toPlayer, offered, requested);
            toPlayer.receiveTradeOffer(counterOffer);
            FXGL.getGameScene().removeUINode(overlay);
        });

        content.getChildren().addAll(header, grid, sendBtn);
    }
    private StackPane createCloseableContent(Node innerContent, Runnable onClose) {
        Button closeBtn = new Button("✕");
        closeBtn.setFont(Font.font(null, FontWeight.BOLD, 20));        
        closeBtn.setTextFill(Color.WHITE);
        closeBtn.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        closeBtn.setOnAction(e -> onClose.run());

        HBox topBar = new HBox(closeBtn);
        topBar.setAlignment(Pos.TOP_RIGHT);

        VBox container = new VBox(topBar, innerContent);
        container.setAlignment(Pos.TOP_CENTER);

        StackPane wrapper = new StackPane(container);
        wrapper.setPickOnBounds(false);
        return wrapper;
    }
    
    private void centerOverlay(StackPane overlay, Region content) {
        overlay.setTranslateX(FXGL.getAppWidth() / 2 - content.getMaxWidth() / 2);
        overlay.setTranslateY(FXGL.getAppHeight() / 2 - content.getMaxHeight() / 2);

        overlay.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            overlay.setTranslateX(FXGL.getAppWidth() / 2 - newBounds.getWidth() / 2);
            overlay.setTranslateY(FXGL.getAppHeight() / 2 - newBounds.getHeight() / 2);
        });
    }
    public void showTradeOfferOverlay(Player targetPlayer, TradeOffer offer) {
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(15));

        Label title = new Label("Handelsangebot von " + offer.getOfferer().getName());
        title.setFont(Font.font(null, FontWeight.BOLD, 16));
        title.setTextFill(Color.WHITE);

        VBox offeredBox = new VBox(5);
        Label offeredTitle = new Label("Angebotene Ressourcen:");
        offeredTitle.setTextFill(Color.WHITE);
        offeredBox.getChildren().add(offeredTitle);
        for (Map.Entry<ResourceType, Integer> entry : offer.getOfferedResources().entrySet()) {
            Label label = new Label(entry.getKey() + ": " + entry.getValue());
            label.setTextFill(Color.WHITE);
            offeredBox.getChildren().add(label);
        }

        VBox requestedBox = new VBox(5);
        Label requestedTitle = new Label("Angeforderte Ressourcen:");
        requestedTitle.setTextFill(Color.WHITE);
        requestedBox.getChildren().add(requestedTitle);
        for (Map.Entry<ResourceType, Integer> entry : offer.getRequestedResources().entrySet()) {
            Label label = new Label(entry.getKey() + ": " + entry.getValue());
            label.setTextFill(Color.WHITE);
            requestedBox.getChildren().add(label);
        }

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button acceptBtn = new Button("✅ Annehmen");
        Button counterBtn = new Button("🔁 Gegenangebot");
        Button declineBtn = new Button("❌ Ablehnen");

        acceptBtn.setOnAction(e -> {
            FXGL.getGameScene().removeUINode(FXGL.geto("tradeOverlay"));

            if (!targetPlayer.hasResources(offer.getRequestedResources()) ||
                !offer.getOfferer().hasResources(offer.getOfferedResources())) {
                FXGL.showMessage("Mindestens einer der Spieler hat nicht genügend Ressourcen.");
                tradeDialogOpen = false;
                return;
            }

            targetPlayer.removeResourcesMap(offer.getRequestedResources());
            targetPlayer.addResourcesMap(offer.getOfferedResources());

            offer.getOfferer().removeResourcesMap(offer.getOfferedResources());
            offer.getOfferer().addResourcesMap(offer.getRequestedResources());

            FXGL.showMessage("Handel erfolgreich durchgeführt.");
            tradeDialogOpen = false;
        });

        counterBtn.setOnAction(e -> {
            FXGL.getGameScene().removeUINode(FXGL.geto("tradeOverlay"));
            openTradeDialogWithTarget(targetPlayer, offer.getOfferer());
        });

        declineBtn.setOnAction(e -> {
            FXGL.getGameScene().removeUINode(FXGL.geto("tradeOverlay"));
            FXGL.showMessage("Du hast das Handelsangebot abgelehnt.");
            tradeDialogOpen = false;
        });

        buttonBox.getChildren().addAll(acceptBtn, counterBtn, declineBtn);

        VBox fullContent = new VBox(20, title, offeredBox, requestedBox, buttonBox);
        fullContent.setAlignment(Pos.CENTER);
        fullContent.setPadding(new Insets(20));
        fullContent.setBackground(new Background(
            new BackgroundFill(Color.rgb(30, 30, 30, 0.95), new CornerRadii(10), Insets.EMPTY)));
        StackPane overlay = new StackPane();
        Node closable = createCloseableContent(fullContent, () -> {
            FXGL.getGameScene().removeUINode(overlay);
            tradeDialogOpen = false;
        });

        overlay.getChildren().add(closable);
        FXGL.set("tradeOverlay", overlay);
        FXGL.getGameScene().addUINode(overlay);
        centerOverlay(overlay, content);
    }
}