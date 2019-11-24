package com.riskGame.strategies;

import java.util.ArrayList;
import java.util.HashMap;

import com.riskGame.controller.FortificationPhase;
import com.riskGame.controller.MapFileEdit;
import com.riskGame.controller.ReinforcementPhase;
import com.riskGame.models.Country;
import com.riskGame.models.Game;

public interface FortifyType {
	FortificationPhase fp = new FortificationPhase();
	String fortify(int player,String ...command);
}


class HumanFortify implements FortifyType{

	/**
	 * This method would help making the fortification move if it is valid
	 * @param fromCountry
	 * @param toCountry
	 * @param num
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

class AggresiveFortify implements FortifyType{

	@Override
	public String fortify(int player,String ...command) {
		// TODO Auto-generated method stub
		ArrayList<String> ownedCountries = Game.getPlayersList().get(player).getOwnedCountries();
		int maxArmies = 0;
		String toCountry = "";
		String fromCountry = "";
		ArrayList<String> maxTriedCountries = new ArrayList<String>();
		do {
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
		}while(fromCountry.length()==0);
		
		int armiesToMove = Country.getListOfCountries().get(fromCountry).getNumberOfArmies() - 1;
		// move armies
		Country.getListOfCountries().get(fromCountry).setNumberOfArmies(Country.getListOfCountries().get(fromCountry).getNumberOfArmies()-armiesToMove);
		Country.getListOfCountries().get(toCountry).setNumberOfArmies(Country.getListOfCountries().get(toCountry).getNumberOfArmies()+armiesToMove);
		System.out.println("Fortified from "+fromCountry+" to "+toCountry+" with "+String.valueOf(armiesToMove)+" armies");
		return "done";
	}
}

class BenevolentFortify implements FortifyType{

	@Override
	public String fortify(int player,String ...command) {
		// TODO Auto-generated method stub
		return "";
	}


}

class CheaterFortify implements FortifyType{

	@Override
	public String fortify(int player,String ...command) {
		// TODO Auto-generated method stub
		return "";
	}


}


class RandomFortify implements FortifyType{

	@Override
	public String fortify(int player,String ...command) {
		// TODO Auto-generated method stub
		return "";
	}


}
