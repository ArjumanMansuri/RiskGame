package com.riskGame.models;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains the data members for the player
 * @author Mudra-PC
 */

public class Player {

    private String playerName;
    private int playerNumOfArmy;
    private Map<String, Continent> playerContinent;
    private HashMap<String,Country> ownedCountries;
    
    public Player(){
        playerContinent = new HashMap<String, Continent>();
        ownedCountries = new HashMap();
    }
    
    public HashMap<String, Country> getOwnedCountries() {
		return ownedCountries;
	}




	public void setOwnedCountries(HashMap<String, Country> ownedCountries) {
		this.ownedCountries = ownedCountries;
	}




	/**
     * @return The player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     *
     * @param playerName The player name
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     *
     * @return The number of army/armies player possess
     */
    public int getPlayerNumOfArmy() {
        return playerNumOfArmy;
    }

    /**
     *
     * @param playerNumOfArmy The number of army/armies player possess
     */
    public void setPlayerNumOfArmy(int playerNumOfArmy) {
        this.playerNumOfArmy = playerNumOfArmy;
    }

    /**
     *
     * @return List of continent owned by player
     */
    public Map<String,Continent> getPlayerContinent() {
        return playerContinent;
    }

    /**
     *
     * @param playerContinent List of continent owned by player
     */
    public void setPlayerContinent(Map<String,Continent> playerContinent) {
        this.playerContinent = playerContinent;
    }

}
