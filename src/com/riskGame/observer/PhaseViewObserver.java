package com.riskGame.observer;

import java.util.ArrayList;

/**
 * This is a PhaseViewObserver abstract class implemented by all Phase View Observers. 
 * @author GouthamG
 *
 */
public abstract class PhaseViewObserver {
	
	/**
	 *String gamePhaseName that holds Phase name.
	 */
	String gamePhaseName;
	
	/**
	 *String currentPlayerName that holds current player name.
	 */
	String currentPlayerName;
	
	/**
	 *actions - ArrayList of Strings that holds the phase logs.
	 */
	ArrayList<String> actions;
	
	public PhaseViewObserver() {
		actions = new ArrayList<String>();
	}

	/**
	 *Abstract method for updating the log message.
	 */
	public abstract void update(String action);
	
	/**
	 *Abstract method to set the playerName.
	 */
	public abstract void setData(String playerName);

}
