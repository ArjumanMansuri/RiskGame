package com.riskGame.strategies;

import java.util.ArrayList;

import com.riskGame.controller.ReinforcementPhase;
import com.riskGame.models.Country;
import com.riskGame.models.Game;

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

	@Override
	public String reinforce(int player,String ...command) {
		// TODO Auto-generated method stub
		return "";
	}
}


class AggresiveReinforce implements ReinforceType{

	@Override
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
	public String reinforce(int player,String ...command)  {
		// TODO Auto-generated method stub
		return "";
	}
}


class CheaterReinforce implements ReinforceType{

	@Override
	public String reinforce(int player,String ...command)  {
		// TODO Auto-generated method stub
		return "";
	}
}

