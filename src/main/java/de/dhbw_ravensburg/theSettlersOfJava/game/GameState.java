package de.dhbw_ravensburg.theSettlersOfJava.game;

/**
 * Represents the different phases of the game.
 * These states are used by the GameController to manage the flow of gameplay.
 */
public enum GameState {
		
		/**
	     * The initial setup phase where players place their starting settlements and roads.
	     * This phase includes two placement rounds per player, one in normal and one in reverse order.
	     */
		SETUP_PHASE,        
		/**
	     * The phase where the current player rolls the dice to determine resource distribution.
	     * If a 7 is rolled, the game enters the ROBBER_PHASE.
	     */
		ROLL_DICE,
		/**
	     * The phase triggered by rolling a 7, during which players must discard resources
	     * and the active player moves the robber to block a hex and possibly steal resources.
	     */
		ROBBER_PHASE,
		/**
	     * The main action phase where players can build, trade, and perform other actions.
	     * Ends when the player presses the "End Turn" button.
	     */
		ACTION_PHASE,  
		/**
	     * The phase indicating the end of the current player's turn.
	     * Triggers transition to the next player's turn and resets the game state.
	     */
	    END_TURN;
}
