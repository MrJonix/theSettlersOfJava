package de.dhbw_ravensburg.theSettlersOfJava.game;




import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;

public class GameController {
	
	
	private GameBoard board;
	
	public GameController() {
		board = new GameBoard(HexType.generateHexTypeList());
	}
	public GameBoard getGameBoard() {
		return board;
	}
	
}

