package com.riskGame.models;

import java.io.Serializable;							
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import com.riskGame.strategies.AttackType;
import com.riskGame.strategies.FortifyType;
import com.riskGame.strategies.ReinforceType;
import com.riskGame.controller.AttackPhase;
import com.riskGame.controller.FortificationPhase;
import com.riskGame.controller.MapFileEdit;
import com.riskGame.controller.ReinforcementPhase;

/**
 * This is a model class for Player that holds the properties for a player.
 * @author niralhlad
 *
 */

public class Player extends Observable implements Observer,Serializable {

	private int playerNumber;
    private String playerName;
    private int playerNumOfArmy;
    private ArrayList<String> ownedCountries;
    private ArrayList<Card> cards;
    private ArrayList<String> ownedContinents;
    protected AttackType attackType; 
    public ReinforceType reinforceType;
    protected FortifyType fortifyType;
    protected String playerType;

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
     * getter method to get the player number
     * @return playerNumber number of player
     */
    public int getPlayerNumber() {
		return playerNumber;
	}
    /**
     * setter method to get the player number
     * @param playerNumber number of player
     */
	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}
    /**
     * getter method to get the player type
     * @return playerType behavior of player
     */
    public String getPlayerType() {
		return playerType;
	}

    /**
     * setter method to set the player type
     * @param playerType behavior of player
     */
	public void setPlayerType(String playerType) {
		this.playerType = playerType;
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

    /**
     * getter method to get the type of attack
     * @return attackType type of attack
     */
    public AttackType getAttackType() {
		return attackType;
	}

    /**
     * getter method to get the type of reinforce
     * @return attackType type of reinforce
     */
	public ReinforceType getReinforceType() {
		return reinforceType;
	}
	
	/**
     * getter method to get the type of fortify
     * @return attackType type of fortify
     */
	public FortifyType getFortifyType() {
		return fortifyType;
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
	
	/**
     * getter method to get list of cards
     * @return cards list cards
     */
	public ArrayList<Card> getCards() {
		return this.cards;
	}
	
	public String computeDominationViewData() {
		float percentage = this.getOwnedCountries().size() * 100 / Country.getListOfCountries().size();
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
