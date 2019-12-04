package com.riskGame.strategies;

import java.io.Serializable;							
import java.util.ArrayList;
import java.util.Random;

import com.riskGame.controller.ReinforcementPhase;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Player;
import com.riskGame.observer.StartupPhaseObserver;

/**
 * This interface helps implement strategy pattern for fortify type
 * @author Arjuman Mansuri
 *
 */
public interface ReinforceType {
	String reinforce(int player,String ...command);
	ReinforcementPhase rp = new ReinforcementPhase();
}

/**
 * This class contains the business logic for human reinforce type
 * @author Arjuman Mansuri
 *
 */
class HumanReinforce implements ReinforceType,Serializable{

	/**
	 * This method checks and calculates the number of reinforcement armies depending on the number of players.
	 * @param player - number of player.
	 * @param command - command from the user input.
	 * @return error if incorrect or saved if correct.
	 * 
	 */
	@Override
	public String reinforce(int player,String ...command) {
		// TODO Auto-generated method stub
		
		if(command[0].isEmpty() || command[0].trim().length()==0) {
			return "Error : Invalid Command";
		}
		//check if it is a reinforcement command
		String[] commandComponents = command[0].split(" ");
		if(commandComponents.length < 2 || commandComponents.length > 4) {
			return "Error : Number of arguments does not match";
		}
		
		String commandName = commandComponents[0];
		if(commandName.equalsIgnoreCase("reinforce")) {
			return rp.processReinforceCmd(player, commandComponents);
		}
		else if(commandName.equalsIgnoreCase("exchangecards")) {
			return rp.processExchangeCardCmd(player, commandComponents);
		}
		else {
			return "Error : Please enter reinforcement command";
		}
	}
}

/**
 * This class contains the business logic for benevolent reinforce type
 * @author Arjuman Mansuri
 *
 */
class BenevolentReinforce implements ReinforceType,Serializable{

	/**
	 * This method checks and calculates the number of reinforcement armies depending on the number of players.
	 * @param player - number of player.
	 * @param command - empty in this case
	 * @return error if incorrect or saved if correct.
	 * 
	 */
	@Override
	public String reinforce(int player,String ...command) {
		// TODO Auto-generated method stub
		// get the weakest countries
		ArrayList<String> ownedCountries = Game.getPlayersList().get(player).getOwnedCountries();
		int minArmies;
		String countryWithMinArmies = "";
		ArrayList<String> minArmyCountries = new ArrayList<String>();
		do {
			minArmies = Integer.MAX_VALUE;
			for(String country : ownedCountries) {
				if(Country.getListOfCountries().get(country).getNumberOfArmies() < minArmies && !minArmyCountries.contains(country)) {
					minArmies = Country.getListOfCountries().get(country).getNumberOfArmies();
					countryWithMinArmies = country;
				}
			}
			minArmyCountries.add(countryWithMinArmies);
		}while(minArmyCountries.size()<2);
		
		// reinforce weakest countries with approximately half armies on each
		int totalArmies = Game.getPlayersList().get(player).getPlayerNumOfArmy();
		String[] commandComponents = {"reinforce",minArmyCountries.get(0),String.valueOf(totalArmies/2)};
		String viewData = "Reinforced "+minArmyCountries.get(0)+" with "+String.valueOf(totalArmies/2)+" armies";
		System.out.println(viewData);
		StartupPhaseObserver.startupViewData = viewData;
		StartupPhaseObserver.view.display();
		rp.processReinforceCmd(player, commandComponents);
		
		String[] commandComponents2 = {"reinforce",minArmyCountries.get(1),String.valueOf(totalArmies - totalArmies/2)};
		
		viewData = "Reinforced "+minArmyCountries.get(1)+" with "+String.valueOf(totalArmies - totalArmies/2)+" armies";
		System.out.println(viewData);
		StartupPhaseObserver.startupViewData = viewData;
		StartupPhaseObserver.view.display();
		
		rp.processReinforceCmd(player, commandComponents2);
		return "";
	}
}

/**
 * This class contains the business logic for aggressive reinforce type
 * @author Arjuman Mansuri
 *
 */
class AggresiveReinforce implements ReinforceType,Serializable{

	/**
	 * This method checks and calculates the number of reinforcement armies depending on the number of players.
	 * @param player - number of player.
	 * @param command - empty in this case
	 * @return error if incorrect or saved if correct.
	 * 
	 */
	public String reinforce(int player,String ...command)  {
		// TODO Auto-generated method stub
		// get the strongest country
		ArrayList<String> ownedCountries = Game.getPlayersList().get(player).getOwnedCountries();
		int maxArmies = 0;
		String countryWithMaxArmies = "";
		for(String country : ownedCountries) {
			if(Country.getListOfCountries().get(country).getNumberOfArmies() > maxArmies) {
				maxArmies = Country.getListOfCountries().get(country).getNumberOfArmies();
				countryWithMaxArmies = country;
			}
		}
		// reinforce it
		String[] commandComponents = {"reinforce",countryWithMaxArmies,String.valueOf(Game.getPlayersList().get(player).getPlayerNumOfArmy())};
		String viewData = "Reinforced "+countryWithMaxArmies+" with "+String.valueOf(Game.getPlayersList().get(player).getPlayerNumOfArmy())+" armies";
		System.out.println(viewData);
		StartupPhaseObserver.startupViewData = viewData;
		StartupPhaseObserver.view.display();
		
		rp.processReinforceCmd(player, commandComponents);
		return "";
	}
}

/**
 * This class contains the business logic for random reinforce type
 * @author Arjuman Mansuri
 *
 */
class RandomReinforce implements ReinforceType,Serializable{

	/**
	 * This method checks and calculates the number of reinforcement armies depending on the number of players.
	 * @param player - number of player.
	 * @param command - empty in this case
	 * @return error if incorrect or saved if correct.
	 * 
	 */
	@Override
	public String reinforce(int playerIndex,String ...command)  {
		// generate a random country and random number
		Player p = Game.getPlayersList().get(playerIndex);
		String countryName = generateRandomCountry(p.getOwnedCountries());

		// generate a random number of army count for reinforcing
		int initialArmyCount = p.getPlayerNumOfArmy();
		
		if(initialArmyCount < 1) {
			return "false";
		}
		
		int randomArmyCount = generateRandomArmyCount(initialArmyCount);
		p.setPlayerNumOfArmy(initialArmyCount - randomArmyCount);

		// reinforce with new number of armies for the countrName
		int newNumberOfArmies = Country.getListOfCountries().get(countryName).getNumberOfArmies() + randomArmyCount;
		Country.getListOfCountries().get(countryName).setNumberOfArmies(newNumberOfArmies);

		String viewData = "Reinforced "+countryName+" with " + newNumberOfArmies +" armies";
		System.out.println(viewData);
		StartupPhaseObserver.startupViewData = viewData;
		StartupPhaseObserver.view.display();
		
		
		if(countryName.length() < 1 || newNumberOfArmies < 1){
			return "false";
		}
		return "true";
	}

	/**
	 * This method generates random country
	 * @param countryList list of countries owned by player
	 * @return random country
	 */
	public String generateRandomCountry(ArrayList<String> countryList) {
		Random randomCountry = new Random();
        return countryList.get(randomCountry.nextInt(countryList.size()));			
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
}

/**
 * This class contains the business logic for cheater reinforce type
 * @author Arjuman Mansuri
 *
 */
class CheaterReinforce implements ReinforceType,Serializable{

	/**
	 * This method checks and calculates the number of reinforcement armies depending on the number of players.
	 * @param player - number of player.
	 * @param command - empty in this case
	 * @return error if incorrect or saved if correct.
	 * 
	 */
	@Override
	public String reinforce(int playerIndex,String ...command)  {
		Player p = Game.getPlayersList().get(playerIndex);
		boolean reinforceValid = true;

		// double the country army count
		for(String countryName : p.getOwnedCountries()){
			int beforeDouble = Country.getListOfCountries().get(countryName).getNumberOfArmies();
			Country.getListOfCountries().get(countryName).setNumberOfArmies(beforeDouble * 2);

			String viewData = "Reinforced "+countryName+" with " + beforeDouble * 2 +" armies";
			System.out.println(viewData);
			StartupPhaseObserver.startupViewData = viewData;
			StartupPhaseObserver.view.display();
			
			if(Country.getListOfCountries().get(countryName).getNumberOfArmies() != (beforeDouble * 2)){
				reinforceValid = false;
			}
		}

		return String.valueOf(reinforceValid);
	}
}

