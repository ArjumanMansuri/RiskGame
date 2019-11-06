package com.riskGame.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.riskGame.controller.ReinforcementPhase;
import com.riskGame.view.CardExchange;

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
    private static CardExchange cardExView = new CardExchange();
    public static int exchanges_made = 0;
    
    /**
     * This is a default constructor that creates game object.
     *
     */
    public Game() {
        playersList = new HashMap<Integer, Player>();
        map = new Map();
    }
    
    public static void initCards() {
    	gameCards = new ArrayList<Card>();
    	
    	for(int i = 0; i < 14; i++) {
    		Card c = new Card(Card.ARTILLARY);
    		gameCards.add(c);
    	}
    	
    	for(int i = 0; i < 14; i++) {
    		Card c = new Card(Card.CAVALRY);
    		gameCards.add(c);
    	}
    	
    	for(int i = 0; i < 14; i++) {
    		Card c = new Card(Card.INFANTRY);
    		gameCards.add(c);
    	}
    }
    
    public static ArrayList<Card> getCards() {
    	return gameCards;
    }
    
    public static void assignRandomCard(Player p) {
    	if(gameCards != null) {
        	Collections.shuffle(gameCards);    		
    	}
    	for(Card c : gameCards) {
    		if(c.getOwner() == null) {
    			c.changeOwner(p);
    			return;
    		}
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
        
        initCards();
        for(Integer key : Game.playersList.keySet()) {
        	if(cardExView == null) {
        		System.out.println("EX VIEW IS NULL");
        		continue;
        	}
        	Game.playersList.get(key).addObserver(cardExView);
        }
        
        /*
         * For Testing
        
        if(Game.playersList.size() > 1) {
        	// Bug the playerlist is not correct. First player is null. Second is the one u pass as first
	        Game.gameCards.get(0).changeOwner(Game.playersList.get(1));
	        Game.gameCards.get(15).changeOwner(Game.playersList.get(1));
	        Game.gameCards.get(30).changeOwner(Game.playersList.get(1));
	        
	        Game.gameCards.get(3).changeOwner(Game.playersList.get(2));
	        Game.gameCards.get(4).changeOwner(Game.playersList.get(2));
	        Game.gameCards.get(5).changeOwner(Game.playersList.get(2));
	        
	        //ReinforcementPhase rp = new ReinforcementPhase();
	        //rp.reinforce(1, "exchangecards -none");
	        //rp.reinforce(1, "reinforce ");
        }*/
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

