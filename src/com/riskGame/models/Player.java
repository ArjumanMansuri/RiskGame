package com.riskGame.models;

import java.util.HashMap;

/**
 * This is a model class for Player that holds the properties for a player.
 * @author niralhlad
 *
 */

public class Player {

    private String playerName;
    private int playerNumOfArmy;
    private HashMap<String, Continent> playerContinent;
    private HashMap<String,Country> ownedCountries;

    /**
     * This is a default constructor which will create player object.
     *
     */
    public Player(){
        playerContinent = new HashMap<String, Continent>();
        ownedCountries = new HashMap();
    }

	/**
     * getter method to get the name of the player.
     * @return The player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * setter method to assign the player name.
     * @param playerName The player name
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * getter method to get the number of army/armies player possess
     * @return The number of army/armies player possess
     */
    public int getPlayerNumOfArmy() {
        return playerNumOfArmy;
    }

    /**
     * setter method to assign the number of army/armies player possess.
     * @param playerNumOfArmy The number of army/armies player possess
     */
    public void setPlayerNumOfArmy(int playerNumOfArmy) {
        this.playerNumOfArmy = playerNumOfArmy;
    }

    /**
     * getter method to get the name of the player.
     * @return List of continent owned by player
     */
    public HashMap<String,Continent> getPlayerContinent() {
        return playerContinent;
    }

    /**
     * setter method to assign the list of continent owned by player.
     * @param playerContinent List of continent owned by player
     */
    public void setPlayerContinent(HashMap<String,Continent> playerContinent) {
        this.playerContinent = playerContinent;
    }

    /**
     * getter method to get the list of countries owned by a player
     * @return ownedCountries List of countries owned by a player
     */
    public HashMap<String, Country> getOwnedCountries() {
        return ownedCountries;
    }

    /**
     * setter method to assign the list of countries owned by a player
     * @param ownedCountries list of countries owned by a player
     */
    public void setOwnedCountries(HashMap<String, Country> ownedCountries) {
        this.ownedCountries = ownedCountries;
    }
}
