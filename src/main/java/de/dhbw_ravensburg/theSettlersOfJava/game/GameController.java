package de.dhbw_ravensburg.theSettlersOfJava.game;

import java.util.List;

import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;

import de.dhbw_ravensburg.theSettlersOfJava.resources.HexType;

public class GameController {
	
	private GameBoard board;
	private Dice dice;
	
	public GameController() {
		board = new GameBoard(HexType.generateHexTypeList());
		dice = new Dice();
	}
	
	public GameBoard getGameBoard() {
		return board;
	}
	
	/**
	 * Updates the dice position when the camera moves
	 */
	public void updateDicePosition() {
		if (dice != null) {
			dice.positionInBottomRight();
		}
	}
}