package models;

import java.util.HashMap;

/**
 * 
 * @author Arjuman Mansuri
 * Model class for Game with map and playersList as its data members
 */
public class Game {
	
	/**
	 * @param map Refers to the map used for the game play
	 * @param playersList Refers to the all the players of a particular game
	 */
	private Map map;
	private HashMap<Integer,Player> playersList;

	public Game() {
		playersList = new HashMap<Integer, Player>();
		map = new Map();
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public HashMap<Integer, Player> getPlayersList() {
		return playersList;
	}

	public void setPlayersList(HashMap<Integer, Player> playersList) {
		this.playersList = playersList;
	}
}
