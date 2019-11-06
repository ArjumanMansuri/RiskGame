package com.riskGame.controller;

import java.util.ArrayList;
import java.util.HashMap;

import com.riskGame.models.Continent;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.observer.AttackPhaseObserver;
import com.riskGame.observer.FortificationPhaseObserver;
import com.riskGame.observer.PhaseViewObserver;
import com.riskGame.observer.PhaseViewPublisher;

/**
 * 
 * @author Arjuman Mansuri
 * This class has business logic of the fortification phase
 *
 */
public class FortificationPhase implements PhaseViewPublisher{

	private PhaseViewObserver newObserver;

	/**
	 * Default Constructor which will create new observer object
	 */
	public FortificationPhase() {
		newObserver = new FortificationPhaseObserver();
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
			return rp.reinforce(player, command);
		}

		// check if it is a fortification command
		this.notifyObserver("Checking for valid fortification command");
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

		// fortify 'fromcountry' 'tocountry' 'num'
		else {
			String fromCountry = commandComponents[1];
			String toCountry = commandComponents[2];
			int num = Integer.parseInt(commandComponents[3]);

			HashMap<String,Continent> continentList =  Game.getMap().getContinents();
			ArrayList<String> countries = new ArrayList<>();
			for (Continent continent: continentList.values()) {
				for(Country country : continent.getTerritories()){
					countries.add(country.getCountryName());
				}
			}
			this.notifyObserver("Checking for valid countries");
					// check if those countries exist
			if(!doCountriesExist(countries, fromCountry, toCountry)){
				return "Error : Either one or both of the country names do not exist";
			}
			// check if they are owned by same player
			this.notifyObserver("Checking for countries owned by same player");
			ArrayList<String> ownedCountries = Game.getPlayersList().get(player).getOwnedCountries();
			if(areCountriesNotOwnedByPlayer(ownedCountries,fromCountry,toCountry)) {
				return "Error : Either one or both of the country names are not owned by you";
			}
			// check if they are adjacent
			this.notifyObserver("Checking if countries are adjacent");
			if(!(areCountriesAdjacent(fromCountry, toCountry))){
				return "Error : Given countries are not adjacent";
			}
			// check if sufficient armies to move
			this.notifyObserver("Checking if sufficient armies are present to move");
			if(!areArmiesSufficientToMove(fromCountry, num)) {
				int maxArmiesToBeMoved = Country.getListOfCountries().get(fromCountry).getNumberOfArmies()-1;
				return "Error : Insufficient armies to move. Try moving "+maxArmiesToBeMoved+" armies";
			}
			// move armies
			this.notifyObserver("Moving armies...");
			this.notifyObserver(num + "armies from country" + fromCountry +" to country " + toCountry);
			Country.getListOfCountries().get(fromCountry).setNumberOfArmies(Country.getListOfCountries().get(fromCountry).getNumberOfArmies()-num);
			Country.getListOfCountries().get(toCountry).setNumberOfArmies(Country.getListOfCountries().get(toCountry).getNumberOfArmies()+num);
		}
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
	private boolean areCountriesNotOwnedByPlayer(ArrayList<String> ownedCountries,String fromCountry,String toCountry) {
		return !ownedCountries.contains(fromCountry) || !ownedCountries.contains(toCountry);
	}

	/**
	 * This method checks if the 'fromCountry' has sufficient armies to move
	 * @param ownedCountries List of countries owned by the player
	 * @param fromCountry Name of the country from which player wants to move his army/armies
	 * @param num number of armies to move
	 * @return true if sufficient armies available else false
	 */
	private boolean areArmiesSufficientToMove(String fromCountry,int num) {
		return Country.getListOfCountries().get(fromCountry).getNumberOfArmies()>num;
	}
	/**
	 * This method is to notify the observer pattern
	 * @param action string to notify the observer 
	 */
	@Override
	public void notifyObserver(String action) {
		this.newObserver.update(action);

	}
}
