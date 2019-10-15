package controller;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import models.Country;
import models.Game;

/**
 * 
 * @author Arjuman Mansuri
 * This class has business logic of the fortification phase
 *
 */
public class FortificationPhase {

	/**
	 * This method would help making the fortification move if it is valid
	 * @param fromCountry
	 * @param toCountry
	 * @param num
	 */
	public String fortify(int player,String command) {
		if(command.isEmpty() || command.trim().length()==0) return "Error : Invalid Command";
		
		// check if it is a fortification command
		String[] commandComponents = command.split(" ");
		String commandName = commandComponents[0];
		if(!commandName.equalsIgnoreCase("fortify")) {
			return "Error : Please enter fortification command";
		}
		// fortify none
		if(commandComponents.length==2) {
			if(commandComponents[1].equalsIgnoreCase("none")) {
				return "done";
			}
		}
		// fortify 'fromcountry' 'tocountry' 'num'
		if(!(commandComponents.length == 4)) {
			return "Error : Number of arguments does not match";
		}else {
			String fromCountry = commandComponents[1];
			String toCountry = commandComponents[2];
			int num = Integer.parseInt(commandComponents[3]);
			// check if those countries exist
			if(!(Country.getListOfCountries().contains(fromCountry)) || !(Country.getListOfCountries().contains(toCountry))){
				return "Error : Either one or both of the country names do not exist";
			}
			// check if they are owned by same player
			HashMap<String,Country> ownedCountries = Game.getPlayersList().get(player).getOwnedCountries();
			if(ownedCountries.get(fromCountry)==null || ownedCountries.get(toCountry)==null) {
				return "Error : Either one or both of the country names are not owned by you";
			}
			// check if they are adjacent
			if(!(ownedCountries.get(fromCountry).getNeighbours().contains(toCountry))){
				return "Error : Given countries are not adjacent";
			}
			// move armies
			if(!(ownedCountries.get(fromCountry).getNumberOfArmies()>=num)) {
				return "Error : Insufficient armies to move";
			}
			ownedCountries.get(fromCountry).setNumberOfArmies(ownedCountries.get(fromCountry).getNumberOfArmies()-num);
			ownedCountries.get(toCountry).setNumberOfArmies(ownedCountries.get(toCountry).getNumberOfArmies()+num);
		}
		return "done";
	}
// After fortification check if the player has at least one territory otherwise remove him from the game	
}
