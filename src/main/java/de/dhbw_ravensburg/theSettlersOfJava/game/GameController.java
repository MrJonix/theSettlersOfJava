package de.dhbw_ravensburg.theSettlersOfJava.game;

import java.util.List;

import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	
	public GameController() {
		
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
		
	}
	
	public GameBoard getGameBoard() {
		return board;
	}
	
	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}
	
}