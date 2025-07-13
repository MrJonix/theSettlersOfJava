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
import de.dhbw_ravensburg.theSettlersOfJava.graphics.view.TradeUIController;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.view.WinView;
import de.dhbw_ravensburg.theSettlersOfJava.map.Hex;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Dice;
import de.dhbw_ravensburg.theSettlersOfJava.units.NextPlayerButton;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import de.dhbw_ravensburg.theSettlersOfJava.units.PlayerColor;
import javafx.animation.PauseTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Central controller that manages game logic, state transitions, UI updates,
 * and interactions between players, dice, board, and game phases.
 *
 * Responsibilities:
 * - Initialize players, game board, dice, and UI elements
 * - Manage setup phase and phase transitions
 * - Handle resource discarding, robber actions, trading and longest road evaluation
 * - Control current player and enforce game rules
 */
public class GameController {

	private GameBoard board;
	private Dice dice;
	private NextPlayerButton nextPlayerbutton;
	private List<Player> players = GameStatus.getPlayerNames();
	private final ObjectProperty<Player> currentPlayer = new SimpleObjectProperty<>();
	private GameState currentState = GameState.SETUP_PHASE;
	private boolean firstSetup = true;
	private Player currentLongestRoadPlayer = null;
	private TradeUIController tradeUI;
	private boolean isDiscardingResources = false;

	/**
	 * Initializes the game controller by setting up players, UI, board, dice,
	 * and trade functionality. Shuffles player order and gives debug resources.
	 */
	public GameController() {
	    initializePlayers();
	    currentPlayer.set(players.get(0)); // Set initial player after shuffling
	    initializeUI();
	    initializeBoard();
	    initializeDice();
	    initializeTrade();
	    //debugStartResources();
	}

	/* ------------------ Initialization Methods ------------------ */

	/**
	 * Highlights all possible start positions for a player's initial settlement
	 * during the setup phase.
	 */
	public void setupPhase() {
		board.getPossibleStartPositions().forEach(c -> c.highlight());
	}
	
	/**
	 * Proceeds to the next player in the setup phase. 
	 * Switches to reverse order after each player placed once.
	 * Ends setup when all players have placed two settlements and roads.
	 *
	 * @param owner the player who just finished their setup turn
	 */
	public void finishedPlayerSetup(Player owner) {
	    int currentIndex = players.indexOf(currentPlayer.get());
	    int nextIndex;

	    if (firstSetup) {
	        // Iterate forward through players
	        nextIndex = currentIndex + 1;
	    } else {
	        // Iterate backwards through players
	        nextIndex = currentIndex - 1;
	    }

	    // Switch to reserve order after first setup round ends
	    if (nextIndex >= players.size()) {
	        // Switch to moving backwards, after finalization of the first phase
	        firstSetup = false;
	        nextIndex = players.size() - 1;
	    }

	    // If last player has placed second settlement, finish setup
	    if (!firstSetup && nextIndex < 0) {
	        // Exit the setup phase
	        System.out.println("Setup-Phase abgeschlossen.");
	        nextPhase();
	        return;
	    }

	    // Place current player and begin the setup phase anew
	    currentPlayer.set(players.get(nextIndex));
	    setupPhase();
	}

	/**
	 * Shuffles the player order at the start of the game.
	 */
	private void initializePlayers() {
		Collections.shuffle(players);
	}

	/**
	 * Initializes and displays the player UI, current player indicator, and next player button.
	 */
	private void initializeUI() {
	    PlayerInfoUI playerInfoUI = new PlayerInfoUI();
	    Pane playerUIPanel = playerInfoUI.createPlayerListPanel(players, currentPlayer);
	    FXGL.addUINode(playerUIPanel, 0, FXGL.getAppHeight()-120);

	    CurrentPlayerInfoUI currentPlayerUI = new CurrentPlayerInfoUI(currentPlayer);
	    FXGL.addUINode(currentPlayerUI.getRoot(), 20, 20); // Adjust position as needed
	    
	    nextPlayerbutton = new NextPlayerButton( FXGL.getAppWidth() - Dice.SIZE - 20, 20);
	    nextPlayerbutton.getView().setVisible(false);
	}

	/**
	 * Initializes the game board with a randomized hex layout.
	 */
	private void initializeBoard() {
	    board = new GameBoard(HexType.generateHexTypeList());
	}

	/**
	 * Creates the dice UI and hides it initially.
	 */
	private void initializeDice() {
        double x = FXGL.getAppWidth() - Dice.SIZE - 20;
        double y = 20;
	    dice = new Dice(x,y);
	    dice.getView().setVisible(false);
	}
	
	/**
	 * Grants debug resources to all players for testing purposes.
	 */
	private void debugStartResources() {
	    for (Player player : players) {
	        player.addResources(ResourceType.WOOD, 20);
	        player.addResources(ResourceType.BRICK, 20);
	        player.addResources(ResourceType.WHEAT, 20);
	        player.addResources(ResourceType.WOOL, 20);
	        player.addResources(ResourceType.ORE, 20);
	    }
	}
	
	/**
	 * Initializes the trading system UI and logic.
	 */
	private void initializeTrade() {
		tradeUI = new TradeUIController(this::getCurrentPlayer, players);
		tradeUI.initTradeButton();

	}

	/* ------------------ Game Phase Control ------------------ */

	/**
	 * Controls the transition between different game states/phases.
	 * Calls appropriate methods depending on the current phase.
	 */
	public void nextPhase() {
	    if (currentState == GameState.ACTION_PHASE) {
	    	//trade();
	        System.out.println("Du kannst handeln oder bauen oder deinen Zug beenden.");
	        currentState = GameState.END_TURN;
			updateTradeUI();
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
	    		updateTradeUI();
	            break;
	        case ROBBER_PHASE:
	        	currentState = GameState.ACTION_PHASE;
	        	nextPlayerbutton.getView().setVisible(true);
	    		updateTradeUI();
	            break;
	        case END_TURN:
	        	currentState = GameState.ROLL_DICE;
	        	tradeUI.hideTradeButton();
	            rollDice();
	            break;
	        default:
	            System.out.println("Unbekannter Zustand: " + currentState);
	    }
	}

	/**
	 * Displays the dice UI to allow the current player to roll.
	 */
	private void rollDice() {
	    System.out.println("Würfeln...");
	    dice.getView().setVisible(true);
	    nextPlayerbutton.getView().setVisible(false);
	}

	/**
	 * Initiates the robber phase when a 7 is rolled.
	 * Handles player discards and prompts robber relocation.
	 */
	private void robberPhase() {
	    currentState = GameState.ROBBER_PHASE;
	    FXGL.getNotificationService().pushNotification("Du musst jetzt den Räuber versetzen");
	    FXGL.getNotificationService().pushNotification("Alle Spieler mit mehr als 7 Karten müssen die Hälfte abgeben."); // Added for clarity
	    nextPlayerbutton.getView().setVisible(false); // Hide next player button during discard/robber phase

	    // Liste der Spieler, die mehr als 7 Karten haben
	    List<Player> toDiscard = players.stream()
	            .filter(p -> p.getResourceSize() > 7)
	            .collect(Collectors.toList());
	    
	    if (toDiscard.size() > 0) {
	    	isDiscardingResources = true; // Set flag to true if discards are needed
	    	
	    	// Wait two seconds before opening discard dialog for better UX
	    	PauseTransition pause = new PauseTransition(Duration.seconds(2)); // two second pause
	        pause.setOnFinished(event -> processNextDiscard(toDiscard, 0));
	        pause.play();

	    } else {
	    	isDiscardingResources = false; // Set flag to false if no discards needed
	    	FXGL.getNotificationService().pushNotification("Kein Spieler muss Karten abgegeben.");
	    	// No nextPhase() call here, as robber still needs to be moved
	    }
	    
	}
	
	/**
	 * Moves the robber to a new valid hex tile, triggers resource stealing 
	 * if opponents are present, and transitions to the next phase.
	 *
	 * @param hex the hex to move the robber to
	 */
	public void moveRobber(Hex hex) {
	    if (isDiscardingResources) { // Prevent robber movement during resource discard
	        return; 
	    }

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
	            nextPlayerbutton.getView().setVisible(true); // Re-enable button after robber moves and no steal
	        } else if (victims.size() == 1) {
	            // Nur einen Spieler -> automatisch stehlen
	            board.getRobber().stealRandomResourceFromPlayer(activePlayer, victims.get(0));
	            nextPhase();
	            nextPlayerbutton.getView().setVisible(true); // Re-enable button after robber moves and steal
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
	            nextPlayerbutton.getView().setVisible(true); // Re-enable button after robber moves and steal
	        }
	    }
	}


	/**
	 * Recursively processes resource discarding for each player 
	 * who has more than 7 cards.
	 *
	 * @param playersToDiscard the list of players who must discard
	 * @param index the current index of player being processed
	 */
	private void processNextDiscard(List<Player> playersToDiscard, int index) {
	    if (index >= playersToDiscard.size()) {
	        isDiscardingResources = false; // All players have discarded
	        FXGL.getNotificationService().pushNotification("Alle Spieler haben Karten abgegeben.");
	        FXGL.getNotificationService().pushNotification("Ressourcenabgabe beendet. Bitte versetzen Sie den Räuber."); // Notify that robber can be moved
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

	/**
	 * Displays the trade UI during the action phase.
	 * Does nothing if called outside the action phase.
	 */
	public void trade() {
	    if (!isActionPhase()) return;
		tradeUI.showTradeButton();
	    System.out.println("Handeln...");
  }
  
	public void updateTradeUI() {
	    if (isActionPhase()) {
	        tradeUI.showTradeButton();
	    } else {
	        tradeUI.hideTradeButton();
	    }
	}

	/**
	 * Placeholder for build logic; currently only outputs a debug message.
	 */
	public void build() {
	    if (!isActionPhase())return;
	    System.out.println("Bauen...");
	}

	/**
	 * Ends the current player's turn within the action phase and initiates next phase.
	 */
	public void endTurnActionPhase() {
	    if (!isActionPhase()) return;
	    System.out.println("Zug wird beendet...");
	    currentState = GameState.END_TURN;
	    nextPhase();
	}

	/**
	 * Advances to the next player and resets the game state.
	 */
	public void endTurn() {
		if(!currentState.equals(GameState.ACTION_PHASE)) return;
	    System.out.println("Nächster Spieler ist dran.");
	    int currentIndex = players.indexOf(currentPlayer.get());
	    int nextIndex = (currentIndex + 1) % players.size();
	    currentPlayer.set(players.get(nextIndex));
	    currentState = GameState.END_TURN;
        nextPhase();
	}

	/**
	 * Handles the logic after dice are rolled. Initiates the robber phase on a 7,
	 * otherwise distributes resources and continues the game.
	 *
	 * @param total the total rolled from the dice
	 */
	public void onDiceRolled(int total) {
		dice.getView().setVisible(false);
		
	    if (total == 7) {
	    	robberPhase();
	    } else {
	        board.distributeResources(total);
	        nextPhase();
	    }
	}

	/**
	 * Checks if the game is currently in the action phase.
	 *
	 * @return true if in action phase, false otherwise
	 */
	private boolean isActionPhase() {
	    if (currentState != GameState.ACTION_PHASE) {
	        System.out.println("Diese Aktion ist nur in der ACTION_PHASE erlaubt.");
	        return false;
	    }
	    return true;
	}
	
	/**
	 * Calculates and assigns the longest road bonus to the correct player.
	 * Adds or removes victory points accordingly.
	 */
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

	/**
	 * Returns the current game board instance.
	 *
	 * @return the game board
	 */
	public GameBoard getGameBoard() {
	    return board;
	}

	/**
	 * Returns the current active player.
	 *
	 * @return current player
	 */
	public Player getCurrentPlayer() {
	    return currentPlayer.get();
	}

	/**
	 * Returns a property object for the current player.
	 * <this allows binding and UI updates.
	 *
	 * @return current player property
	 */
	public ObjectProperty<Player> currentPlayerProperty() {
	    return currentPlayer;
	}
	/**
	 * Returns the current game state (phase).
	 *
	 * @return current game state
	 */

	public GameState getCurrentGameState() {
	    return currentState;
	}
	
	/**
	 * Returns whether the game is still in the first round of the setup phase,
	 * during which players place their first settlements.
	 *
	 * @return true if first setup round is active, false otherwise
	 */
	public boolean getFirstSetup() {
		return firstSetup;
	}

	/**
	 * Returns the list of players in the game
	 *
	 * @return list of all players of the game 
	 */
	public List<Player> getPlayers() {
		return players;
	}
}