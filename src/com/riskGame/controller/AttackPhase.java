package com.riskGame.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.riskGame.models.Continent;
import com.riskGame.models.Country;
import com.riskGame.models.Game;

public class AttackPhase {
	
	private static int attackerDiceNum;
	private static int defenderDiceNum;
	private static String attackerCountry;
	private static String defenderCountry;
	private static int defenderPlayer;
	
	/**
	 * This method would help making the fortification move if it is valid
	 * @param fromCountry
	 * @param toCountry
	 * @param num
	 */
	public String attackSetup(int player,String command) {
		if(command.isEmpty() || command.trim().length()==0) {
			return "Error : Invalid Command";
		}
		
		String[] commandComponents = command.split(" ");
		
		// check if it is an attack command
		
		String commandName = commandComponents[0];
		if(!commandName.equalsIgnoreCase("attack")) {
			return "Error : Please enter attack command";
		}
		// attack none
		if(commandComponents.length == 2) {
			if(commandComponents[1].equalsIgnoreCase("none")) {
				return "done";
			}
		}
		
		if(!(commandComponents.length == 4)) {
			return "Error : Number of arguments does not match";
		}
		else {
			String fromCountry = commandComponents[1];
			String toCountry = commandComponents[2];
			
			 HashMap<String,Continent> continentList =  Game.getMap().getContinents();
	            ArrayList<String> countries = new ArrayList<>();
	            for (Continent continent: continentList.values()) {
	                for(Country country : continent.getTerritories()){
	                    countries.add(country.getCountryName());
	                }
	            }
			
			// check if those countries exist
			if(!doCountriesExist(countries, fromCountry, toCountry)){
				return "Error : Either one or both of the country names do not exist";
			}
			// check if attacking country owned by player
			ArrayList<String> ownedCountries = Game.getPlayersList().get(player).getOwnedCountries();
			if(isCountryNotOwnedByPlayer(ownedCountries,fromCountry)) {
				return "Error : Country from which you want to attack is not owned by you";
			}
			
			// check if attacked country owned by player
			if(!isCountryNotOwnedByPlayer(ownedCountries,toCountry)) {
				return "Error : You cannot attack a country owned by you";
			}
			
			// check if they are adjacent
			if(!(areCountriesAdjacent(fromCountry, toCountry))){
					return "Error : Given countries are not adjacent";
			}
			
			String numDice = commandComponents[3];
			if(!numDice.matches("\\d") || Integer.parseInt(numDice) > 3) {
				return "Error : Please enter a valid number of dice (1, 2 or 3) you want to roll";
			}
			int diceNum = Integer.parseInt(numDice);
			// check if sufficient armies to move
			if(!areArmiesSufficientToAttack(fromCountry, diceNum)) {
				int maxArmiesToBeMoved = Country.getListOfCountries().get(fromCountry).getNumberOfArmies()-1;
				if(maxArmiesToBeMoved!=0) {
					return "Error : Insufficient armies on "+fromCountry+" to roll "+ diceNum+" dice. Try rolling "+maxArmiesToBeMoved+" dice.";
				}
				else {
					return "Error : You cannot attack from "+fromCountry+" as it has 1 army. Try attacking from other country with more than 1 army";
				}
			}
			AttackPhase.attackerDiceNum = diceNum;
			AttackPhase.attackerCountry = fromCountry;
			AttackPhase.defenderCountry = toCountry;
			// get to know the defender player
			int defenderPlayer = Country.getListOfCountries().get(toCountry).getOwner();
			AttackPhase.defenderPlayer = defenderPlayer;
			return "DefenderPlayer "+defenderPlayer;
		}
	}
	
	public String setDefendDice(int player,String command) {
		if(command.isEmpty() || command.trim().length()==0) {
			return "Error : Invalid Command";
		}
		
		String[] commandComponents = command.split(" ");
		
		// check if it is a defend command
		String commandName = commandComponents[0];
		if(!commandName.equalsIgnoreCase("defend")) {
			return "Error : Please enter defend command";
		}
		// Arguments less than 2
		if(commandComponents.length < 2) {
			return "Error : Please include number of dice you want to roll in the command";
		}
		String numDice = commandComponents[1];
		if(!numDice.matches("\\d") || Integer.parseInt(numDice)>2) {
			return "Error : Please enter a valid number of dice (1 or 2) you want to roll";
		}
		
		if(Integer.parseInt(numDice) > Country.getListOfCountries().get(AttackPhase.defenderCountry).getNumberOfArmies()) {
			return "Error: You can roll a maximum of "+Country.getListOfCountries().get(AttackPhase.defenderCountry).getNumberOfArmies()+" dice.";
		}
		AttackPhase.defenderDiceNum = Integer.parseInt(numDice);
		
		return attack();
	}
	
	private String attack() {
		ArrayList<Integer> attackerDiceRolls = new ArrayList<Integer>();
		ArrayList<Integer> defenderDiceRolls = new ArrayList<Integer>();
		
		for(int i=0;i<AttackPhase.attackerDiceNum;i++) {
			attackerDiceRolls.add(rollDice());
		}
		for(int i=0;i<AttackPhase.defenderDiceNum;i++) {
			defenderDiceRolls.add(rollDice());
		}
		
		while(attackerDiceRolls.size()!=0 && defenderDiceRolls.size()!=0){
			// check if attacker wins
			int attackerMax = Collections.max(attackerDiceRolls);
			int defenderMax = Collections.max(defenderDiceRolls);
			if(attackerMax > defenderMax) {
				Country.getListOfCountries().get(AttackPhase.defenderCountry).setNumberOfArmies(Country.getListOfCountries().get(AttackPhase.defenderCountry).getNumberOfArmies()-1);
			}
			else {
				Country.getListOfCountries().get(AttackPhase.attackerCountry).setNumberOfArmies(Country.getListOfCountries().get(AttackPhase.attackerCountry).getNumberOfArmies()-1);
			}
			attackerDiceRolls.remove((Integer)attackerMax);
			defenderDiceRolls.remove((Integer)defenderMax);
		}
		if(Country.getListOfCountries().get(AttackPhase.defenderCountry).getNumberOfArmies()==0) {
			return "canConquer";
		}
		else {
			return "canNotConquer";
		}
		
	}
	
	private int rollDice() {
		return (int)(Math.random()*6)+1;
	}
	
	public String moveArmies(int player,String command) {
		if(command.isEmpty() || command.trim().length()==0) {
			return "Error : Invalid Command";
		}
		
		String[] commandComponents = command.split(" ");
		
		// check if it is a defend command
		String commandName = commandComponents[0];
		if(!commandName.equalsIgnoreCase("attackmove")) {
			return "Error : Please enter attackmove command";
		}
		// Arguments less than 2
		if(commandComponents.length < 2) {
			return "Error : Please include number of armies you want to move";
		}
		String strNumOfArmies = commandComponents[1];
		if(!strNumOfArmies.matches("\\d+")) {
			return "Error : Please enter a valid number of armies";
		}
		int numOfArmies = Integer.parseInt(strNumOfArmies);
		int numOfArmiesCanBeMoved = Country.getListOfCountries().get(AttackPhase.attackerCountry).getNumberOfArmies()-1;
		if(numOfArmies > numOfArmiesCanBeMoved) {
			return "Error : You should have at least 1 army on "+AttackPhase.attackerCountry+". Try moving "+numOfArmiesCanBeMoved+" armies";
		}
		Country.getListOfCountries().get(AttackPhase.attackerCountry).setNumberOfArmies(Country.getListOfCountries().get(AttackPhase.attackerCountry).getNumberOfArmies()-numOfArmies);
		Country.getListOfCountries().get(AttackPhase.defenderCountry).setNumberOfArmies(Country.getListOfCountries().get(AttackPhase.defenderCountry).getNumberOfArmies()+numOfArmies);
		// change ownership of defender country
		Country.getListOfCountries().get(AttackPhase.defenderCountry).setOwner(player);
		
		Game.getPlayersList().get(player).getOwnedCountries().add(defenderCountry);
		Game.getPlayersList().get(defenderPlayer).getOwnedCountries().remove(defenderCountry);
		return "done";
	}
	
	/**
	 * This method checks if the countries provided as parameters exist
	 * @param countries List of countries present in the game
	 * @param fromCountry Name of the country from which player wants to move his army/armies
	 * @param toCountry Name of the country to which player wants to move his army/armies
	 * @return true if the countries exist else false
	 */
	private boolean doCountriesExist(ArrayList<String> countries,String fromCountry,String toCountry) {
		return (countries.contains(fromCountry)) && (countries.contains(toCountry));
	}
	
	/**
	 * This method checks if the countries provided as parameters are adjacent
	 * @param ownedCountries List of countries owned by the player
	 * @param fromCountry Name of the country from which player wants to move his army/armies
	 * @param toCountry Name of the country to which player wants to move his army/armies
	 * @return true if the countries are adjacent else false
	 */
	private boolean areCountriesAdjacent(String fromCountry,String toCountry) {
		
			if(!(Country.getListOfCountries().get(fromCountry).getNeighbours().containsKey(toCountry))){
				return false;
			}
			else {
				return true;
			}
	}
	
	/**
	 * This method checks if the countries provided as parameters are owned by the player
	 * @param ownedCountries List of countries owned by the player
	 * @param fromCountry Name of the country from which player wants to move his army/armies
	 * @param toCountry Name of the country to which player wants to move his army/armies
	 * @return true if the countries are owned by the player else false
	 */
	private boolean isCountryNotOwnedByPlayer(ArrayList<String> ownedCountries,String fromCountry) {
		return !ownedCountries.contains(fromCountry);
	}
	
	/**
	 * This method checks if the 'fromCountry' has sufficient armies to move
	 * @param ownedCountries List of countries owned by the player
	 * @param fromCountry Name of the country from which player wants to move his army/armies
	 * @param num number of armies to move
	 * @return true if sufficient armies available else false
	 */
	private boolean areArmiesSufficientToAttack(String fromCountry,int diceNum) {
		return Country.getListOfCountries().get(fromCountry).getNumberOfArmies()>diceNum;
	}
}
