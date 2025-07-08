package de.dhbw_ravensburg.theSettlersOfJava.game;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.almasb.fxgl.dsl.FXGL;

import de.dhbw_ravensburg.theSettlersOfJava.graphics.CurrentPlayerInfoUI;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.PlayerInfoUI;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.view.DiscardResourcesView;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.view.PlayerSelectionView;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.view.TradeView;
import de.dhbw_ravensburg.theSettlersOfJava.map.Hex;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Dice;
import de.dhbw_ravensburg.theSettlersOfJava.units.NextPlayerButton;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GameController {

	private GameBoard board;
	private Dice dice;
	private NextPlayerButton nextPlayerbutton;
	private List<Player> players = GameStatus.getPlayerNames();
	private final ObjectProperty<Player> currentPlayer = new SimpleObjectProperty<>();
	private GameState currentState = GameState.SETUP_PHASE;
	private boolean firstSetup = true;
	private Player currentLongestRoadPlayer = null;

	public GameController() {
	    initializePlayers();
	    currentPlayer.set(players.get(0)); // Set initial player after shuffling
	    initializeUI();
	    initializeBoard();
	    initializeDice();
	    //debugStartResources();
	}

	/* ------------------ Initialization Methods ------------------ */

	public void setupPhase() {
		board.getPossibleStartPositions().forEach(c -> c.highlight());
		
	}
	
	public void finishedPlayerSetup(Player owner) {
	    int currentIndex = players.indexOf(currentPlayer.get());
	    int nextIndex;

	    if (firstSetup) {
	        // Nach hinten durchgehen
	        nextIndex = currentIndex + 1;
	    } else {
	        // Vorwärts durchgehen
	        nextIndex = currentIndex - 1;
	    }

	    // Zyklisch von vorne starten, wenn am Ende
	    if (nextIndex >= players.size()) {
	        // Wechsel zum Rückwärtsgehen nach Abschluss der ersten Phase
	        firstSetup = false;
	        nextIndex = players.size() - 1;
	    }

	    // Wenn rückwärts gehen und am Anfang
	    if (!firstSetup && nextIndex < 0) {
	        // Beende die Setup-Phase
	        System.out.println("Setup-Phase abgeschlossen.");
	        nextPhase();
	        return;
	    }

	    // Setze den aktuellen Spieler und beginne die Setup-Phase erneut
	    currentPlayer.set(players.get(nextIndex));
	    setupPhase();
	}

	private void initializePlayers() {
		Collections.shuffle(players);
	}

	private void initializeUI() {
	    PlayerInfoUI playerInfoUI = new PlayerInfoUI();
	    Pane playerUIPanel = playerInfoUI.createPlayerListPanel(players, currentPlayer);
	    FXGL.addUINode(playerUIPanel, 0, FXGL.getAppHeight()-120);

	    CurrentPlayerInfoUI currentPlayerUI = new CurrentPlayerInfoUI(currentPlayer);
	    FXGL.addUINode(currentPlayerUI.getRoot(), 20, 20); // Adjust position as needed
	    
	    nextPlayerbutton = new NextPlayerButton( FXGL.getAppWidth() - Dice.SIZE - 20, 20);
	    nextPlayerbutton.getView().setVisible(false);
	}

	private void initializeBoard() {
	    board = new GameBoard(HexType.generateHexTypeList());
	}

	private void initializeDice() {
        double x = FXGL.getAppWidth() - Dice.SIZE - 20;
        double y = 20;
	    dice = new Dice(x,y);
	    dice.getView().setVisible(false);
	}

	private void debugStartResources() {
	    for (Player player : players) {
	        player.addResources(ResourceType.WOOD, 10);
	        player.addResources(ResourceType.BRICK, 10);
	        player.addResources(ResourceType.WHEAT, 5);
	        player.addResources(ResourceType.WOOL, 2);
	        player.addResources(ResourceType.ORE, 3);
	    }
	}

	/* ------------------ Game Phase Control ------------------ */

	public void nextPhase() {
	    if (currentState == GameState.ACTION_PHASE) {
	        System.out.println("Du kannst handeln oder bauen oder deinen Zug beenden.");
	        currentState = GameState.END_TURN;
        	nextPhase();
	        return;
	    }

	    switch (currentState) {
	    	case SETUP_PHASE:
	    		currentState = GameState.ROLL_DICE;
	    		rollDice();
	    		break;
	        case ROLL_DICE:
	        	currentState = GameState.ACTION_PHASE;
	        	nextPlayerbutton.getView().setVisible(true);
	        	trade();
	            break;
	        case ROBBER_PHASE:
	        	currentState = GameState.ACTION_PHASE;
	        	nextPlayerbutton.getView().setVisible(true);
	            break;
	        case END_TURN:
	        	currentState = GameState.ROLL_DICE;
	            rollDice();
	            break;
	        default:
	            System.out.println("Unbekannter Zustand: " + currentState);
	    }
	}

	private void rollDice() {
	    System.out.println("Würfeln...");
	    dice.getView().setVisible(true);
	    nextPlayerbutton.getView().setVisible(false);
	}

	private void robberPhase() {
	    currentState = GameState.ROBBER_PHASE;
	    FXGL.getNotificationService().pushNotification("Du musst jetzt den Räuber versetzen");

	    // Liste der Spieler, die mehr als 7 Karten haben
	    List<Player> toDiscard = players.stream()
	            .filter(p -> p.getResourceSize() > 7)
	            .collect(Collectors.toList());
	    
	    if (toDiscard.size() > 0) {
	    	processNextDiscard(toDiscard, 0);
	    } else {
	    	FXGL.getNotificationService().pushNotification("Kein Spieler muss Karten abgegeben.");
	    }
	    
	}
	
	public void moveRobber(Hex hex) {
	    if (getCurrentGameState().equals(GameState.ROBBER_PHASE) &&
	        !hex.getHexType().equals(HexType.WATER) &&
	        !hex.equals(board.getRobber().getLocation())) {
	        
	        board.getRobber().moveRobber(hex);
	        
	        Player activePlayer = currentPlayer.get();
	        List<Player> victims = board.getPlayersAdjacentToHex(hex).stream()
	            .filter(p -> !p.equals(activePlayer))
	            .filter(p -> p.getResourceSize() > 0) // Nur Spieler mit Ressourcen
	            .collect(Collectors.toList());

	        if (victims.isEmpty()) {
	            // Keine Spieler zum Stehlen -> direkt nächste Phase
	            nextPhase();
	        } else if (victims.size() == 1) {
	            // Nur einen Spieler -> automatisch stehlen
	            board.getRobber().stealRandomResourceFromPlayer(activePlayer, victims.get(0));
	            nextPhase();
	        } else {
	            // Mehrere Spieler -> Auswahl anzeigen
	        	PlayerSelectionView view = new PlayerSelectionView(victims);
	        	Optional<Player> chosen = view.showAndWait();
	        	chosen.ifPresent(victim -> {
	        		if (victim != null) {
		            	board.getRobber().stealRandomResourceFromPlayer(activePlayer, victim);
		            }
	        	});

	            nextPhase();
	        }
	    }
	}


	// Rekursive Verarbeitung der Spieler
	private void processNextDiscard(List<Player> playersToDiscard, int index) {
	    if (index >= playersToDiscard.size()) {
	        // Alle Spieler durch – du kannst z.B. den Räuber versetzen lassen oder das Spiel fortsetzen
	        FXGL.getNotificationService().pushNotification("Alle Spieler haben Karten abgegeben.");
	        return;
	    }

	    Player player = playersToDiscard.get(index);

	    DiscardResourcesView view = new DiscardResourcesView(player.getName(), player.getResources(), discarded -> {
	        discarded.forEach((type, amount) -> {
	            int current = player.getResources().getOrDefault(type, 0);
	            player.getResources().put(type, current - amount);
	        });

	        FXGL.getNotificationService().pushNotification(player.getName() + " hat Karten abgegeben.");

	        processNextDiscard(playersToDiscard, index + 1);
	    });

	    FXGL.getGameScene().getRoot().getChildren().add(view.createOverlay());

	 
	}



    // Assume this is called during the player's turn
    public void trade() {
        if (!isActionPhase()) 
            return; // Ensure it's the appropriate phase for trading
        
        TradeView tradeView = new TradeView();
        AtomicReference<VBox> tradeUIRef = new AtomicReference<>();

        VBox tradeUI = tradeView.createTradeUI(() -> {
            FXGL.getGameScene().removeUINode(tradeUIRef.get());
        });
        tradeUIRef.set(tradeUI);

        FXGL.getGameScene().addUINode(tradeUI);
        tradeUI.setLayoutX(100);
        tradeUI.setLayoutY(100);


    }

	public void build() {
	    if (!isActionPhase()) return;
	    System.out.println("Bauen...");
	}

	public void endTurnActionPhase() {
	    if (!isActionPhase()) return;
	    System.out.println("Zug wird beendet...");
	    currentState = GameState.END_TURN;
	    nextPhase();
	}

	public void endTurn() {
		if(!currentState.equals(GameState.ACTION_PHASE)) return;
	    System.out.println("Nächster Spieler ist dran.");
	    int currentIndex = players.indexOf(currentPlayer.get());
	    int nextIndex = (currentIndex + 1) % players.size();
	    currentPlayer.set(players.get(nextIndex));
	    currentState = GameState.END_TURN;
        nextPhase();
	}

	public void onDiceRolled(int total) {
		dice.getView().setVisible(false);
		
	    if (total == 7) {
	    	robberPhase();
	    } else {
	        board.distributeResources(total);
	        nextPhase();
	    }
	}

	private boolean isActionPhase() {
	    if (currentState != GameState.ACTION_PHASE) {
	        System.out.println("Diese Aktion ist nur in der ACTION_PHASE erlaubt.");
	        return false;
	    }
	    return true;
	}
	
	public void setAllPlayersLongestRoad() {
	    Player newLongestRoadPlayer = null;
	    int maxRoadLength = 0;

	    // Find the player with the longest road >= 5
	    for (Player p : players) {
	        int roadLength = board.getLongestRoadLength(p);
	        p.longestRoadProperty().set(roadLength);

	        if (roadLength >= 5 && roadLength > maxRoadLength) {
	            maxRoadLength = roadLength;
	            newLongestRoadPlayer = p;
	        }
	    }

	    // If the longest road player changed, update victory points accordingly
	    if (newLongestRoadPlayer != null && !newLongestRoadPlayer.equals(currentLongestRoadPlayer)) {
	        // Remove 2 points from old longest road player
	        if (currentLongestRoadPlayer != null) {
	            currentLongestRoadPlayer.setVictoryPoints(currentLongestRoadPlayer.getVictoryPoints() - 2);
	        }

	        // Add 2 points to new longest road player
	        newLongestRoadPlayer.setVictoryPoints(newLongestRoadPlayer.getVictoryPoints() + 2);

	        // Update the tracker
	        currentLongestRoadPlayer = newLongestRoadPlayer;
	    }

	    // If no player qualifies for longest road (road length < 5)
	    else if (newLongestRoadPlayer == null && currentLongestRoadPlayer != null) {
	        // Remove 2 points from old holder and clear the reference
	        currentLongestRoadPlayer.setVictoryPoints(currentLongestRoadPlayer.getVictoryPoints() - 2);
	        currentLongestRoadPlayer = null;
	    }
	}


	/* ------------------ Getters ------------------ */

	public GameBoard getGameBoard() {
	    return board;
	}

	public Player getCurrentPlayer() {
	    return currentPlayer.get();
	}

	public ObjectProperty<Player> currentPlayerProperty() {
	    return currentPlayer;
	}

	public GameState getCurrentGameState() {
	    return currentState;
	}
	
	public boolean getFirstSetup() {
		return firstSetup;
	}

	public List<Player> getPlayers() {
		return players;
	}
}