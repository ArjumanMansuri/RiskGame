package com.riskGame.strategies;

import java.util.*;
import java.io.Serializable;						   

import com.riskGame.controller.FortificationPhase;
import com.riskGame.controller.MapFileEdit;
import com.riskGame.controller.ReinforcementPhase;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Player;

/**
 * This interface helps implement strategy pattern for fortify type
 * @author Arjuman Mansuri
 *
 */
public interface FortifyType {
	FortificationPhase fp = new FortificationPhase();
	String fortify(int player,String ...command);
}

/**
 * This class contains the business logic for human fortify type
 * @author Arjuman Mansuri
 *
 */
class HumanFortify implements FortifyType,Serializable{

	/**
	 * This method would help making the fortification move if it is valid
	 * @param player number of player
	 * @param command command to perform fortification
	 * @return done if successful else error
	 */
	@Override
	public String fortify(int player,String ...command) {
		if(command[0].isEmpty() || command[0].trim().length()==0) {
			return "Error : Invalid Command";
		}

		String[] commandComponents = command[0].split(" ");

		// Call card exchange
		if(commandComponents[0].equalsIgnoreCase("exchangecards")) {
			ReinforcementPhase rp = new ReinforcementPhase();
			return Game.getPlayersList().get(player).getReinforceType().reinforce(player, command);
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
			if(commandComponents[1].equalsIgnoreCase("-none")) {
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

/**
 * This class contains the business logic for aggressive fortify type
 * @author Arjuman Mansuri
 *
 */
class AggresiveFortify implements FortifyType,Serializable{
	
	/**
	 * This method would help making the fortification move if it is valid
	 * @param player number of player
	 * @param command command to perform fortification
	 * @return done if successful else error
	 */
	@Override
	public String fortify(int player,String ...command) {
		// TODO Auto-generated method stub
		ArrayList<String> ownedCountries = Game.getPlayersList().get(player).getOwnedCountries();
		int maxArmies;
		String toCountry = "";
		String fromCountry = "";
		ArrayList<String> maxTriedCountries = new ArrayList<String>();
		do {
			maxArmies = 0;	 
			for(String country : ownedCountries) {
				if(Country.getListOfCountries().get(country).getNumberOfArmies() > maxArmies && !maxTriedCountries.contains(country)) {
					maxArmies = Country.getListOfCountries().get(country).getNumberOfArmies();
					toCountry = country;
				}
			}
			maxTriedCountries.add(toCountry);
			
			// select toCountry
			for(String country : Country.getListOfCountries().get(toCountry).getNeighbours().keySet()) {
				if(!fp.areCountriesNotOwnedByPlayer(ownedCountries,country,toCountry) && Country.getListOfCountries().get(country).getNumberOfArmies() > 1) {
					fromCountry = country;
					break;
				}
			}
			if(ownedCountries.size() == maxTriedCountries.size()) {
				System.out.println("Fortification skipped");
				return "done";
			}											  
		}while(fromCountry.length()==0);
		
		if(fromCountry.trim().length()==0 || toCountry.trim().length()==0){
			System.out.println("Fortification skipped");
			return "done";
		}
		int armiesToMove = Country.getListOfCountries().get(fromCountry).getNumberOfArmies() - 1;
		// move armies
		Country.getListOfCountries().get(fromCountry).setNumberOfArmies(Country.getListOfCountries().get(fromCountry).getNumberOfArmies()-armiesToMove);
		Country.getListOfCountries().get(toCountry).setNumberOfArmies(Country.getListOfCountries().get(toCountry).getNumberOfArmies()+armiesToMove);
		System.out.println("Fortified from "+fromCountry+" to "+toCountry+" with "+String.valueOf(armiesToMove)+" armies");
		return "done";
	}

	
}

/**
 * This class contains the business logic for benevolent fortify type
 * @author Arjuman Mansuri
 *
 */
class BenevolentFortify implements FortifyType,Serializable{

	/**
	 * This method would help making the fortification move if it is valid
	 * @param player number of player
	 * @param command command to perform fortification
	 * @return done if successful else error
	 */
	@Override
	public String fortify(int player,String ...command) {
		// TODO Auto-generated method stub
		ArrayList<String> ownedCountries = Game.getPlayersList().get(player).getOwnedCountries();
		int minArmies;
		String toCountry = "";
		String fromCountry = "";
		ArrayList<String> minTriedCountries = new ArrayList<String>();
		do {
			minArmies = Integer.MAX_VALUE;
			// select weakest country for fortification
			for(String country : ownedCountries) {
				if(Country.getListOfCountries().get(country).getNumberOfArmies() < minArmies && !minTriedCountries.contains(country)) {
					minArmies = Country.getListOfCountries().get(country).getNumberOfArmies();
					toCountry = country;
				}
			}
			minTriedCountries.add(toCountry);
			
			// select fromCountry
			for(String country : Country.getListOfCountries().get(toCountry).getNeighbours().keySet()) {
				if(!fp.areCountriesNotOwnedByPlayer(ownedCountries,country,toCountry) && Country.getListOfCountries().get(country).getNumberOfArmies() > 1) {
					fromCountry = country;
					break;
				}
			}
			if(ownedCountries.size() == minTriedCountries.size()) {
				System.out.println("Fortification skipped");
				return "done";
			}
		}while(fromCountry.length()==0);
		
		int armiesToMove = Country.getListOfCountries().get(fromCountry).getNumberOfArmies() - 1;
		// move all armies except one
		Country.getListOfCountries().get(fromCountry).setNumberOfArmies(Country.getListOfCountries().get(fromCountry).getNumberOfArmies()-armiesToMove);
		Country.getListOfCountries().get(toCountry).setNumberOfArmies(Country.getListOfCountries().get(toCountry).getNumberOfArmies()+armiesToMove);
		System.out.println("Fortified from "+fromCountry+" to "+toCountry+" with "+String.valueOf(armiesToMove)+" armies");
		return "done";
	}

}

/**
 * This class contains the business logic for cheater fortify type
 * @author Arjuman Mansuri
 *
 */
class CheaterFortify implements FortifyType,Serializable{

	/**
	 * This method would help making the fortification move if it is valid
	 * @param player number of player
	 * @param command command to perform fortification
	 * @return done if successful else error
	 */
	@Override
	public String fortify(int playerIndex,String ...command) {
		Player p = Game.getPlayersList().get(playerIndex);
		Iterator ownCountryIter = p.getOwnedCountries().iterator();
		boolean fortifiedCheater = false;

		while(ownCountryIter.hasNext()){
			String ownCountry = (String) ownCountryIter.next();
			HashMap<String, Country> ownCountryNeighbors = Country.getListOfCountries().get(ownCountry).getNeighbours();
			boolean shouldDoubleFortify = false;

			for(Map.Entry<String, Country> neighbor : ownCountryNeighbors.entrySet()){
				int neighborOwner = neighbor.getValue().getOwner();
				if(neighborOwner != playerIndex){
					shouldDoubleFortify = true;
					break;
				}
			}

			// if eligible - Double the army count
			if(shouldDoubleFortify){
				Country.getListOfCountries().get(ownCountry).setNumberOfArmies( Country.getListOfCountries().get(ownCountry).getNumberOfArmies() * 2 );
				fortifiedCheater = true;
			}
		}
		return String.valueOf(fortifiedCheater);
	}
}

/**
 * This class contains the business logic for random fortify type
 * @author Arjuman Mansuri
 *
 */
class RandomFortify implements FortifyType,Serializable{

	/**
	 * This method would help making the fortification move if it is valid
	 * @param player number of player
	 * @param command command to perform fortification
	 * @return done if successful else error
	 */
	@Override
	public String fortify(int playerIndex,String ...command) {
		Player p = Game.getPlayersList().get(playerIndex);

		// generate random from country and to country
		String fromCountry = generateFromCountry(p.getOwnedCountries());
		String toCountry = generateToCountry(Country.getListOfCountries().get(fromCountry).getNeighbours());
		
		int armiesToMoveCalc = Country.getListOfCountries().get(fromCountry).getNumberOfArmies() - 1;
		
		if(armiesToMoveCalc < 1) {
			return "false";
		}
		
		int numArmiesToMove = generateRandomArmyCount(armiesToMoveCalc);
		
		// move armies
		int fromCountBeforeFortify = Country.getListOfCountries().get(fromCountry).getNumberOfArmies();
		int toCountBeforeFortify = Country.getListOfCountries().get(toCountry).getNumberOfArmies();
		Country.getListOfCountries().get(fromCountry).setNumberOfArmies(fromCountBeforeFortify - numArmiesToMove);
		Country.getListOfCountries().get(toCountry).setNumberOfArmies(toCountBeforeFortify + numArmiesToMove);

		// check if before fortify and after fortify values are same
		if(fromCountBeforeFortify == Country.getListOfCountries().get(fromCountry).getNumberOfArmies() && Country.getListOfCountries().get(toCountry).getNumberOfArmies() == toCountBeforeFortify){
			return "false";
		}
		return "true";
	}

	/**
	 * This method generates random army count
	 * @param i bound for generating random army
	 * @return random number of armies
	 */
	public int generateRandomArmyCount(int i) {
		Random randomArmy = new Random();
		return randomArmy.nextInt(i);
	}

	/**
	 * This method generates fromCountry for fortification
	 * @param countryList list of countries owned by the player
	 * @return a suitable fromCountry
	 */
	public String generateFromCountry(ArrayList<String> countryList) {
		Random randomCountry = new Random();
		return countryList.get(randomCountry.nextInt(countryList.size()));
	}
	
	/**
	 * This method generates toCountry for fortification
	 * @param countryList list of countries owned by the player
	 * @return a suitable toCountry
	 */
	public String generateToCountry(HashMap<String, Country> countryList) {
		Random randomCountry = new Random();
		Object[] values = countryList.values().toArray();
		Country toCountry = (Country) values[randomCountry.nextInt(values.length)];
		return toCountry.getCountryName();
	}
}
