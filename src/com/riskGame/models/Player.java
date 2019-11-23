package com.riskGame.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import com.riskGame.Strategies.AttackType;
import com.riskGame.Strategies.FortifyType;
import com.riskGame.Strategies.ReinforceType;
import com.riskGame.controller.AttackPhase;
import com.riskGame.controller.FortificationPhase;
import com.riskGame.controller.MapFileEdit;
import com.riskGame.controller.ReinforcementPhase;

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
    protected AttackType attackType; 
    public ReinforceType reinforceType;
    protected FortifyType fortifyType;
    

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
	
	/**
	 * This method checks and calculates the number of reinforcement armies depending on the number of players.
	 * @param player - number of player.
	 * @param command - command from the user input.
	 * @return error if incorrect or saved if correct.
	 * 
	 */
	public String reinforce(int player,String command) {
		this.reinforceType.reinforce(player);
		return null;
//		ReinforcementPhase rp = new ReinforcementPhase();
//		if(command.isEmpty() || command.trim().length()==0) {
//			return "Error : Invalid Command";
//		}
//		//check if it is a reinforcement command
//		String[] commandComponents = command.split(" ");
//		if(commandComponents.length < 2 || commandComponents.length > 4) {
//			return "Error : Number of arguments does not match";
//		}
//		
//		String commandName = commandComponents[0];
//		if(commandName.equalsIgnoreCase("reinforce")) {
//			return rp.processReinforceCmd(player, commandComponents);
//		}
//		else if(commandName.equalsIgnoreCase("exchangecards")) {
//			return rp.processExchangeCardCmd(player, commandComponents);
//		}
//		else {
//			return "Error : Please enter reinforcement command";
//		}
	}
	
	/**
	 * This method implements the real attack between the attacker and the defender countries
	 * @return canConquer if the attacker wins or else canNotConquer
	 * 
	 */
	public String attack() {
		ArrayList<Integer> attackerDiceRolls = new ArrayList<Integer>();
		ArrayList<Integer> defenderDiceRolls = new ArrayList<Integer>();
    
		String tempString = "";
	AttackPhase ap = new AttackPhase();
  for(int i=0;i<AttackPhase.getAttackerDiceNum();i++) {
			int rollNum = ap.rollDice();
			attackerDiceRolls.add(rollNum);
			tempString =  tempString + rollNum + " ";
		}

		AttackPhase.setAttackerDiceRollsString(tempString);
		tempString="";

		for(int i=0;i<AttackPhase.getDefenderDiceNum();i++) {
			int rollNum = ap.rollDice();
			defenderDiceRolls.add(rollNum);
			tempString =  tempString + rollNum + " ";
		}

		AttackPhase.setDefenderDiceRollsString(tempString);
		

		while(attackerDiceRolls.size()!=0 && defenderDiceRolls.size()!=0){
			// check if attacker wins
			int attackerMax = Collections.max(attackerDiceRolls);
			int defenderMax = Collections.max(defenderDiceRolls);
			if(attackerMax > defenderMax) {

				ap.notifyObserver("Defender lost 1 army !!");
				Country.getListOfCountries().get(AttackPhase.getDefenderCountry()).setNumberOfArmies(Country.getListOfCountries().get(AttackPhase.getDefenderCountry()).getNumberOfArmies()-1);
			}
			else {
				ap.notifyObserver("Attacker1 lost 1 army !!");
				Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).setNumberOfArmies(Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).getNumberOfArmies()-1);
			}
			attackerDiceRolls.remove((Integer)attackerMax);
			defenderDiceRolls.remove((Integer)defenderMax);
		}
		if(Country.getListOfCountries().get(AttackPhase.getDefenderCountry()).getNumberOfArmies()==0) {
			Player p = Game.getPlayersList().get(Country.getListOfCountries().get(AttackPhase.getDefenderCountry()).getOwner());
			Game.assignRandomCard(p);
			boolean controlsContinent = true;
			String continentOfConqueredCountry = Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).getContinent();
			ArrayList<Country> countries = (ArrayList<Country>) Game.getMap().getContinents().get(continentOfConqueredCountry).getTerritories();
			for(Country country : countries) {
				if(!p.getOwnedCountries().contains(country)) {
					controlsContinent = false;
					break;
				}
			}
			if(controlsContinent) {
				p.getOwnedContinents().add(continentOfConqueredCountry);
			}
			
			return "canConquer "+Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).getNumberOfArmies();
		}
		else {
			return "canNotConquer "+Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).getNumberOfArmies();
		}

	}
	/**
	 * This method would help making the fortification move if it is valid
	 * @param fromCountry
	 * @param toCountry
	 * @param num
	 */
	public String fortify(int player,String command) {
		if(command.isEmpty() || command.trim().length()==0) {
			return "Error : Invalid Command";
		}

		String[] commandComponents = command.split(" ");

		// Call card exchange
		if(commandComponents[0].equalsIgnoreCase("exchangecards")) {
			ReinforcementPhase rp = new ReinforcementPhase();
			return Game.getPlayersList().get(player).reinforce(player, command);
		}
		
		if(commandComponents[0].equalsIgnoreCase("showmap")) {
			MapFileEdit.gamePlayShowMap();
			return "";
		}

		// check if it is a fortification command
		
		String commandName = commandComponents[0];
		if(!commandName.equalsIgnoreCase("fortify")) {
			return "Error : Please enter fortification command";
		}
		// fortify none
		if(commandComponents.length == 2) {
			if(commandComponents[1].equalsIgnoreCase("none")) {
				return "done";
			}
		}

		if(!(commandComponents.length == 4)) {
			return "Error : Number of arguments does not match";
		}

		if(commandComponents.length == 4 && !commandComponents[3].matches("\\d+")) {
			return "Error : Please enter a valid number of armies";
		}
		// fortify 'fromcountry' 'tocountry' 'num'
		else {
			String fromCountry = commandComponents[1];
			String toCountry = commandComponents[2];
			int num = Integer.parseInt(commandComponents[3]);
			FortificationPhase fp = new FortificationPhase();
			HashMap<String,Country> countriesMap = new HashMap<>();
            countriesMap = Country.getListOfCountries();
            ArrayList<String> countries = new ArrayList<String>(countriesMap.keySet());
			
			// check if those countries exist
			fp.notifyObserver("Checking for valid countries");
					// check if those countries exist

			if(!fp.doCountriesExist(countries, fromCountry, toCountry)){
				return "Error : Either one or both of the country names do not exist";
			}
			// check if they are owned by same player
			ArrayList<String> ownedCountries = Game.getPlayersList().get(player).getOwnedCountries();
			if(fp.areCountriesNotOwnedByPlayer(ownedCountries,fromCountry,toCountry)) {
				return "Error : Either one or both of the country names are not owned by you";
			}
			// check if they are adjacent
			if(!(fp.areCountriesAdjacent(fromCountry, toCountry))){
				return "Error : Given countries are not adjacent";
			}
			// check if sufficient armies to move
			if(!fp.areArmiesSufficientToMove(fromCountry, num)) {
				int maxArmiesToBeMoved = Country.getListOfCountries().get(fromCountry).getNumberOfArmies()-1;
				return "Error : Insufficient armies to move. Try moving "+maxArmiesToBeMoved+" armies";
			}
			// move armies
			Country.getListOfCountries().get(fromCountry).setNumberOfArmies(Country.getListOfCountries().get(fromCountry).getNumberOfArmies()-num);
			Country.getListOfCountries().get(toCountry).setNumberOfArmies(Country.getListOfCountries().get(toCountry).getNumberOfArmies()+num);
		}
		return "done";
	}
}
