package de.dhbw_ravensburg.theSettlersOfJava.game;




import java.util.ArrayList;
import java.util.List;

import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;
import de.dhbw_ravensburg.theSettlersOfJava.units.Player;

public class GameController {
	
	
	private GameBoard board;
	private List<Player> players;
	private int currentPlayerIndex;
	
	public GameController() {
		players = new ArrayList<>();
		currentPlayerIndex = 0;
		players.add(new Player("Jonas"));
		board = new GameBoard(HexType.generateHexTypeList());
	}
	public GameBoard getGameBoard() {
		return board;
	}
	
	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}
	
}

