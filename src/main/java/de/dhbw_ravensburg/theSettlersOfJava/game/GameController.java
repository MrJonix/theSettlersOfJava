package de.dhbw_ravensburg.theSettlersOfJava.game;

import java.util.List;

import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;

import java.util.ArrayList;
import java.util.Collections;

import de.dhbw_ravensburg.theSettlersOfJava.graphics.PlayerInfoUI;
import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;
import de.dhbw_ravensburg.theSettlersOfJava.resources.ResourceType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GameController {
	
	private GameBoard board;
	private Dice dice;
	private List<Player> players;
	private int currentPlayerIndex;
    private GameState currentState;
    private boolean turnEnded = false;
	
	public GameController() {
		this.currentState = GameState.SETUP;
		players = new ArrayList<>();
		currentPlayerIndex = 0;
		
		players.add(new Player("Jonas", Color.RED));
		players.add(new Player("Nico", Color.BLUE));
		players.add(new Player("Arthur", Color.GREEN));
		players.add(new Player("Kim", Color.AQUA));
		Collections.shuffle(players);
		PlayerInfoUI playerInfoUI = new PlayerInfoUI();
		Pane playerUIPanel = playerInfoUI.createPlayerListPanel(players);
		FXGL.addUINode(playerUIPanel, 20, 20);
		
		board = new GameBoard(HexType.generateHexTypeList());
		
		dice = new Dice();
		
		
		getCurrentPlayer().setVictoryPoints(3);
		getCurrentPlayer().addResources(ResourceType.WOOD, 5);
		getCurrentPlayer().addResources(ResourceType.BRICK, 5);
		getCurrentPlayer().addResources(ResourceType.WHEAT, 5);
		getCurrentPlayer().addResources(ResourceType.WOOL, 2);
		getCurrentPlayer().addResources(ResourceType.ORE, 3);
		
	}
	
	public GameBoard getGameBoard() {
		return board;
	}
	
	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}


	    // Methode, um zur nächsten Phase zu wechseln (außer in ACTION_PHASE)
	    public void nextPhase() {
	        if (currentState == GameState.ACTION_PHASE) {
	            // In der ACTION_PHASE entscheidet der Spieler, wann er fertig ist
	            System.out.println("Du kannst handeln oder bauen, oder deinen Zug beenden.");
	            return;
	        }

	        switch (currentState) {
	            case ROLL_DICE:
	                rollDice();
	                break;
	            case RESOURCE_DISTRIBUTION:
	                distributeResources();
	                break;
	            case ROBBER_PHASE:
	                robberPhase();
	                break;
	            case END_TURN:
	                endTurn();
	                break;
	            default:
	                break;
	        }
	    }

	    private void rollDice() {
	        System.out.println("Würfeln...");
	        // Hier Würfelwurf und Ergebnisverarbeitung
	        currentState = GameState.RESOURCE_DISTRIBUTION;
	    }

	    private void distributeResources() {
	        System.out.println("Ressourcen verteilen...");
	        // Ressourcen verteilen nach Würfelergebnis
	        // Wenn gewürfelt == 7, Räuber aktivieren
	        // Sonst weiter zu ACTION_PHASE
	        currentState = GameState.ROBBER_PHASE;
	    }

	    private void robberPhase() {
	        System.out.println("Räuberphase...");
	        // Räuber versetzen, Karten abwerfen etc.
	        // Danach ACTION_PHASE starten
	        currentState = GameState.ACTION_PHASE;
	        turnEnded = false;
	    }

	    // Handel starten (kann mehrmals in ACTION_PHASE aufgerufen werden)
	    public void trade() {
	        if (currentState != GameState.ACTION_PHASE) {
	            System.out.println("Handeln ist nur in der ACTION_PHASE möglich.");
	            return;
	        }
	        System.out.println("Handeln...");
	        // Handelslogik hier
	    }

	    // Bauen starten (kann mehrmals in ACTION_PHASE aufgerufen werden)
	    public void build() {
	        if (currentState != GameState.ACTION_PHASE) {
	            System.out.println("Bauen ist nur in der ACTION_PHASE möglich.");
	            return;
	        }
	        System.out.println("Bauen...");
	        // Baulogik hier
	    }

	    // Zug beenden in ACTION_PHASE
	    public void endTurnActionPhase() {
	        if (currentState != GameState.ACTION_PHASE) {
	            System.out.println("Du bist nicht in der ACTION_PHASE.");
	            return;
	        }
	        System.out.println("Zug wird beendet...");
	        turnEnded = true;
	        currentState = GameState.END_TURN;
	        endTurn();
	    }

	    private void endTurn() {
	        System.out.println("Nächster Spieler ist dran.");
	        currentPlayerIndex++;
	        // Spielerwechsel, ggf. Sieg prüfen
	        currentState = GameState.ROLL_DICE;
	    }
}