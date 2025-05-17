package de.dhbw_ravensburg.theSettlersOfJava.game;

public enum GameState {
		SETUP_PHASE,        // Startphase zum Platzieren der Anfangssiedlungen und Straßen
		SETUP,
		ROLL_DICE,
	    RESOURCE_DISTRIBUTION,
	    ROBBER_PHASE,
	    ACTION_PHASE,  // Kombinierte Handels- und Bauphase
	    END_TURN;
}
