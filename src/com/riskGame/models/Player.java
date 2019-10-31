package com.riskGame.models;

import java.util.ArrayList;

/**
 * This is a model class for Player that holds the properties for a player.
 * @author niralhlad
 *
 */

public class Player {

    private String playerName;
    private int playerNumOfArmy;
    private ArrayList<String> ownedCountries;

    /**
     * This is a default constructor which will create player object.
     *
     */
    public Player(){
        ownedCountries = new ArrayList<String>();
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
     * getter method to get the list of countries owned by a player
     * @return ownedCountries List of countries owned by a player
     */
    public ArrayList<String> getOwnedCountries() {
		return ownedCountries;
	}
    
    /**
     * setter method to assign the list of countries owned by a player
     * @param ownedCountries list of countries owned by a player
     */
    public void setOwnedCountries(ArrayList<String> ownedCountries) {
		this.ownedCountries = ownedCountries;
	}

}
