package de.dhbw_ravensburg.theSettlersOfJava.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.almasb.fxgl.dsl.FXGL;

import de.dhbw_ravensburg.theSettlersOfJava.graphics.PlayerInfoUI;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GameController {

    private GameBoard board;
    private Dice dice;
    private final List<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private GameState currentState = GameState.ROLL_DICE;
    private boolean turnEnded = false;

    public GameController() {
        initializePlayers();
        initializeUI();
        initializeBoard();
        initializeDice();
        debugStartResources();
    }

    /* ------------------ Initialization Methods ------------------ */

    private void initializePlayers() {
        players.add(new Player("Jonas", Color.RED));
        players.add(new Player("Nico", Color.BLUE));
        players.add(new Player("Arthur", Color.GREEN));
        players.add(new Player("Kim", Color.AQUA));
        Collections.shuffle(players);
    }

    private void initializeUI() {
        PlayerInfoUI playerInfoUI = new PlayerInfoUI();
        Pane playerUIPanel = playerInfoUI.createPlayerListPanel(players);
        FXGL.addUINode(playerUIPanel, 20, 20);
    }

    private void initializeBoard() {
        board = new GameBoard(HexType.generateHexTypeList());
    }

    private void initializeDice() {
        dice = new Dice(); // übergebe GameController für State-Check
    }

    private void debugStartResources() {
        
        for (Player player : players) {
        	player.addResources(ResourceType.WOOD, 5);
            player.addResources(ResourceType.BRICK, 5);
            player.addResources(ResourceType.WHEAT, 5);
            player.addResources(ResourceType.WOOL, 2);
            player.addResources(ResourceType.ORE, 3);
            player.setVictoryPoints(2);
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
	        // Fowarding Logic in onDiceRolled(int total)
	        case ROBBER_PHASE:
	            robberPhase();
	            endTurn(); //DEBUG
	            break;
	        case END_TURN:
	            endTurn(); //DEBUG
	            break;
	        default:
	            System.out.println("Unbekannter Zustand: " + currentState);
	            break;
        }


    }

    private void rollDice() {
        System.out.println("Würfeln...");
        // Nur noch von Dice-Objekt ausgelöst
    }

    private void robberPhase() {
        System.out.println("Räuberphase...");
        // Räuber versetzen, Karten abwerfen
        currentState = GameState.ACTION_PHASE;
        turnEnded = false;
    }

    public void trade() {
        if (!isActionPhase()) return;
        System.out.println("Handeln...");
        // Handelslogik
    }

    public void build() {
        if (!isActionPhase()) return;
        System.out.println("Bauen...");
        // Baulogik
    }

    public void endTurnActionPhase() {
        if (!isActionPhase()) return;
        System.out.println("Zug wird beendet...");
        turnEnded = true;
        currentState = GameState.END_TURN;
        endTurn();
    }

    private void endTurn() {
        System.out.println("Nächster Spieler ist dran.");
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentState = GameState.ROLL_DICE;
    }

    public void onDiceRolled(int total) {
        System.out.println("Wurf: " + total);
        if (total == 7) {
            currentState = GameState.ROBBER_PHASE;
            
        } else {
            board.distributeResources(total); 
            currentState = GameState.ACTION_PHASE;
            endTurn();
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
        return players.get(currentPlayerIndex);
    }

    public GameState getCurrentGameState() {
        return currentState;
    }
}
