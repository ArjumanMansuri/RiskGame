package com.riskGame.strategies;

import java.util.ArrayList;
import java.util.Random;

import com.riskGame.controller.ReinforcementPhase;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Player;

public interface ReinforceType {
	String reinforce(int player,String ...command);
	ReinforcementPhase rp = new ReinforcementPhase();
}

class HumanReinforce implements ReinforceType{

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


class BenevolentReinforce implements ReinforceType{


	public String reinforce(int player,String ...command) {
		// TODO Auto-generated method stub
		return "";
	}
}


class AggresiveReinforce implements ReinforceType{

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
		System.out.println("Reinforced "+countryWithMaxArmies+" with "+String.valueOf(Game.getPlayersList().get(player).getPlayerNumOfArmy())+" armies");
		rp.processReinforceCmd(player, commandComponents);
		return "";
	}
}


class RandomReinforce implements ReinforceType{

	@Override
  public String reinforce(int playerIndex,String ...command)  {
		// generate a random country and random number
		Player p = Game.getPlayersList().get(playerIndex);
		String countryName = generateRandomCountry(p.getOwnedCountries());

		// generate a random number of army count for reinforcing
		int initialArmyCount = p.getPlayerNumOfArmy();
		int randomArmyCount = generateRandomArmyCount(initialArmyCount);
		p.setPlayerNumOfArmy(initialArmyCount - randomArmyCount);

		// reinforce with new number of armies for the countrName
		int newNumberOfArmies = Country.getListOfCountries().get(countryName).getNumberOfArmies() + randomArmyCount;
		Country.getListOfCountries().get(countryName).setNumberOfArmies(newNumberOfArmies);

		if(countryName.length() < 1 || newNumberOfArmies < 1){
			return "false";
		}
		return "true";
	}

	public String generateRandomCountry(ArrayList<String> countryList) {
		Random randomCountry = new Random();
        return countryList.get(randomCountry.nextInt(countryList.size()));			
	}

	public int generateRandomArmyCount(int i) {
		Random randomArmy = new Random();
		return randomArmy.nextInt(i);
	}
}


class CheaterReinforce implements ReinforceType{

	@Override
	public String reinforce(int playerIndex,String ...command)  {
		Player p = Game.getPlayersList().get(playerIndex);
		boolean reinforceValid = true;

		// double the country army count
		for(String countryName : p.getOwnedCountries()){
			int beforeDouble = Country.getListOfCountries().get(countryName).getNumberOfArmies();
			Country.getListOfCountries().get(countryName).setNumberOfArmies(beforeDouble * 2);

			if(Country.getListOfCountries().get(countryName).getNumberOfArmies() != (beforeDouble * 2)){
				reinforceValid = false;
			}
		}

		return String.valueOf(reinforceValid);
	}
}

