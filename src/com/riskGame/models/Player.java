package com.riskGame.models;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * This is a model class for Player that holds the properties for a player.
 * @author niralhlad
 *
 */

public class Player extends Observable implements Observer {

    private String playerName;
    private int playerNumOfArmy;
    private ArrayList<String> ownedCountries;
    private ArrayList<Card> cards;
    private ArrayList<String> ownedContinents;
    
    

    /**
     * This is a default constructor which will create player object.
     *
     */
    public Player(){
        ownedCountries = new ArrayList<String>();
        cards = new ArrayList<Card>();
        ownedContinents = new ArrayList<>();
    }

    /**
     * getter method to get the list of continents owned by a player
     * @return ownedCountries List of continents owned by a player
     */
    public ArrayList<String> getOwnedContinents() {
		return ownedContinents;
	}
    
    /**
     * setter method to assign the list of continents owned by a player
     * @param ownedCountries list of continents owned by a player
     */
	public void setOwnedContinents(ArrayList<String> ownedContinents) {
		this.ownedContinents = ownedContinents;
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

	@Override
	public void update(Observable o, Object arg) {
		if(((String)arg).equals("success")) {
			cards.add((Card)o);
			setChanged();
			notifyObservers("added");
		}
		else if(((String)arg).equals("removed")) {
			cards.remove((Card)o);
			setChanged();
			notifyObservers("removed");
		}
	}
	
	public ArrayList<Card> getCards() {
		return this.cards;
	}
	
	public String computeDominationViewData() {
		int percentage = this.getOwnedCountries().size() * 100 / Country.getListOfCountries().size();
		String finalStr = "World Domination View of player: " +this.getPlayerName()+"\n";
		finalStr = finalStr + "Percentage of world controlled : "+percentage+"%\n";
		finalStr = finalStr + "Continents owned : "+this.getOwnedContinents()+"\n";
		
		int totalArmy = 0;
		for(String countryName:this.getOwnedCountries()) {
			totalArmy = totalArmy + Country.getListOfCountries().get(countryName).getNumberOfArmies();
		}
		finalStr = finalStr + "Total number of armies owned : "+totalArmy+"\n";
		return finalStr;
	}
}
