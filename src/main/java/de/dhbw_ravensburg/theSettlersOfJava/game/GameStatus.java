package de.dhbw_ravensburg.theSettlersOfJava.game;

import java.util.ArrayList;
import java.util.List;

import de.dhbw_ravensburg.theSettlersOfJava.units.Player;

public class GameStatus {
    private static List<Player> playerNames = new ArrayList<>();


    public static List<Player> getPlayerNames() {
        return playerNames;
    }

	public void addPlayer(Player p) {
		playerNames.add(p);
	}
}
