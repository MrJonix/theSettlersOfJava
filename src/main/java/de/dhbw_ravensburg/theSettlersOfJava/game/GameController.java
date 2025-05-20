package de.dhbw_ravensburg.theSettlersOfJava.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.almasb.fxgl.dsl.FXGL;

import de.dhbw_ravensburg.theSettlersOfJava.App;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.CurrentPlayerInfoUI;
import de.dhbw_ravensburg.theSettlersOfJava.graphics.PlayerInfoUI;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import de.dhbw_ravensburg.theSettlersOfJava.units.PlayerColor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;

public class GameController {

	private GameBoard board;
	private Dice dice;
	private List<Player> players = GameStatus.getPlayerNames();
	private final ObjectProperty<Player> currentPlayer = new SimpleObjectProperty<>();
	private GameState currentState = GameState.ROLL_DICE;

	public GameController() {
	    initializePlayers();
	    currentPlayer.set(players.get(0)); // Set initial player after shuffling
	    initializeUI();
	    initializeBoard();
	    initializeDice();
	    debugStartResources();
	}

	/* ------------------ Initialization Methods ------------------ */

	private void initializePlayers() {
		Collections.shuffle(players);
	}

	private void initializeUI() {
	    PlayerInfoUI playerInfoUI = new PlayerInfoUI();
	    Pane playerUIPanel = playerInfoUI.createPlayerListPanel(players, currentPlayer);
	    FXGL.addUINode(playerUIPanel, 0, FXGL.getAppHeight()-150);

	    CurrentPlayerInfoUI currentPlayerUI = new CurrentPlayerInfoUI(currentPlayer);
	    FXGL.addUINode(currentPlayerUI.getRoot(), 20, 20); // Adjust position as needed
	}

	private void initializeBoard() {
	    board = new GameBoard(HexType.generateHexTypeList());
	}

	private void initializeDice() {
        double x = FXGL.getAppWidth() - Dice.SIZE - 20;
        double y = 20;
	    dice = new Dice(x,y); // Übergabe für State-Check
	}

	private void debugStartResources() {
	    for (Player player : players) {
	        player.addResources(ResourceType.WOOD, 5);
	        player.addResources(ResourceType.BRICK, 5);
	        player.addResources(ResourceType.WHEAT, 5);
	        player.addResources(ResourceType.WOOL, 2);
	        player.addResources(ResourceType.ORE, 3);
	    }
	}

	/* ------------------ Game Phase Control ------------------ */

	public void nextPhase() {
	    if (currentState == GameState.ACTION_PHASE) {
	        System.out.println("Du kannst handeln oder bauen oder deinen Zug beenden.");
	        return;
	    }

	    switch (currentState) {
	        case ROLL_DICE:
	            rollDice();
	            break;
	        case ROBBER_PHASE:
	            robberPhase();
	            endTurn(); // DEBUG
	            break;
	        case END_TURN:
	            endTurn(); // DEBUG
	            break;
	        default:
	            System.out.println("Unbekannter Zustand: " + currentState);
	    }
	}

	private void rollDice() {
	    System.out.println("Würfeln...");
	    // Würfeln wird durch Dice-Objekt ausgelöst
	}

	private void robberPhase() {
	    System.out.println("Räuberphase...");
	    for (Player p : players) {
	        if (p.getResourceSize() > 7) {
	            // TODO: Karten abwerfen Logik
	        }
	    }
	    currentState = GameState.ACTION_PHASE;
	}

	public void trade() {
	    if (!isActionPhase()) return;
	    System.out.println("Handeln...");
	}

	public void build() {
	    if (!isActionPhase()) return;
	    System.out.println("Bauen...");
	}

	public void endTurnActionPhase() {
	    if (!isActionPhase()) return;
	    System.out.println("Zug wird beendet...");
	    currentState = GameState.END_TURN;
	    endTurn();
	}

	private void endTurn() {
	    System.out.println("Nächster Spieler ist dran.");
	    int currentIndex = players.indexOf(currentPlayer.get());
	    int nextIndex = (currentIndex + 1) % players.size();
	    currentPlayer.set(players.get(nextIndex));
	    currentState = GameState.ROLL_DICE;
	}

	public void onDiceRolled(int total) {
	    if (total == 7) {
	        currentState = GameState.ROBBER_PHASE;
	    } else {
	        board.distributeResources(total);
	        currentState = GameState.ACTION_PHASE;
	        endTurn(); // DEBUG
	    }
	    FXGL.getNotificationService().pushNotification(currentState.toString());
	}

	private boolean isActionPhase() {
	    if (currentState != GameState.ACTION_PHASE) {
	        System.out.println("Diese Aktion ist nur in der ACTION_PHASE erlaubt.");
	        return false;
	    }
	    return true;
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
}