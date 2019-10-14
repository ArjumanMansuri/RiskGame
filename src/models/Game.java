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
	private static Map map;
	private static HashMap<Integer,Player> playersList;

	public Game() {
		playersList = new HashMap<Integer, Player>();
		map = new Map();
	}

	public static Map getMap() {
		return map;
	}

	public static void setMap(Map map) {
		map = map;
	}

	public static HashMap<Integer, Player> getPlayersList() {
		return playersList;
	}

	public static void setPlayersList(HashMap<Integer, Player> playersList) {
		playersList = playersList;
	}
}
