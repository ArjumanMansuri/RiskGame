package com.riskGame.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Arjuman Mansuri
 * @author Mudra-PC
 * Model class for Game with map and playersList as its data members
 */
public class Game {

    private static Map map = new Map();
    private static HashMap<Integer,Player> playersList = new HashMap<>();
    private static Map editMap = new Map();
    private static boolean editMapSet = false;
    private static ArrayList<Card> gameCards;
    
    /**
     * This is a default constructor that creates game object.
     *
     */
    public Game() {
        playersList = new HashMap<Integer, Player>();
        map = new Map();
    }
    
    public void initCards() {
    	gameCards = new ArrayList<Card>();
    	
    	for(int i = 0; i < 14; i++) {
    		gameCards.add(new Card(Card.ARTILLARY));
    	}
    	
    	for(int i = 0; i < 14; i++) {
    		gameCards.add(new Card(Card.CAVALRY));
    	}
    	
    	for(int i = 0; i < 14; i++) {
    		gameCards.add(new Card(Card.INFANTRY));
    	}
    }

    /**
     * getter method to get static object of map from the map model.
     * @return map object.
     *
     */
    public static Map getMap() {
        return map;
    }

    /**
     * setter method to set static object of map from the map model.
     * @param map set map object.
     *
     */
    public static void setMap(Map map) {
        Game.map = map;
    }

    /**
     * getter method to get static object list of players.
     * @return playersList list of players.
     *
     */
    public static HashMap<Integer, Player> getPlayersList() {
        return playersList;
    }

    /**
     * setter method to set list of players.
     * @param playersList hashmap of players.
     *
     */
    public static void setPlayersList(HashMap<Integer, Player> playersList) {
        Game.playersList = playersList;
    }

    /**
     * getter method to get static object edited map.
     * @return map object of the edited map.
     *
     */
    public static Map getEditMap() {
        return editMap;
    }

    /**
     * setter method to set static object edited map.
     * @param editMap object for the edited map.
     *
     * */
    public static void setEditMap(Map editMap) {
        Game.editMap = editMap;
    }

    /**
     * checks if the edited map is set into file.
     * @return true if edited map is set correctly.
     *
     */
    public static boolean isEditMapSet() {
        return editMapSet;
    }

    /**
     * setter method to set the edited map to true if correct.
     * @param editMapSet passese the boolean value true if set correctly.
     *
     */
    public static void setEditMapSet(boolean editMapSet) {
        Game.editMapSet = editMapSet;
    }
}

