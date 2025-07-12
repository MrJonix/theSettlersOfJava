package de.dhbw_ravensburg.theSettlersOfJava.game;

import java.util.ArrayList;
import java.util.List;

import de.dhbw_ravensburg.theSettlersOfJava.units.Player;

/**
 * Stores and manages the list of players participating in the current game.
 * 
 * <p>This class provides methods to access, modify, and reset the list of players.
 * Note: The internal list is declared static and thus shared across all instances.
 */
public class GameStatus {
	
	/**
     * Static list of all players in the current game session.
     */
    private static List<Player> playerNames = new ArrayList <>();

    /**
     * Returns the current list of registered players.
     *
     * @return the list of {@link Player} instances
     */
    public static List<Player> getPlayerNames() {
        return playerNames;
    }
    
    /**
     * Deletes the current list of players and resets it to an empty list.
     * This is typically used when starting a new game.
     */
    public void deletePlayersList() {
		playerNames = new ArrayList<>();
	}
   
    /**
     * Adds a new player to the current player list.
     *
     * @param p the {@link Player} instance to add
     */
	public void addPlayer(Player p) {
		playerNames.add(p);
	}
}
