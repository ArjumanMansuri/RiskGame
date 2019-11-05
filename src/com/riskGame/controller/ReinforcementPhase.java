package com.riskGame.controller;


import java.util.HashMap;
import java.util.Map;

import com.riskGame.models.Continent;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Player;
import com.riskGame.observer.FortificationPhaseObserver;
import com.riskGame.observer.PhaseViewObserver;
import com.riskGame.observer.PhaseViewPublisher;
import com.riskGame.observer.ReinforcementPhaseObserver;

/**
 * This class contains the business logic of the Reinforcement Phase.
 * @author goumis
 *
 */
public class ReinforcementPhase implements PhaseViewPublisher{
	
	private PhaseViewObserver newObserver;
	
	public ReinforcementPhase() {
		newObserver = new ReinforcementPhaseObserver();
	}
	
	/**
	 * This method is used to  calculate the number of Reinforcement armies and set to access globally.
	 * @param playerNumber turn of player.
	 * 
	 */
	public void calculateReinforcementArmies(int playerNumber) {
		Player p = Game.getPlayersList().get(playerNumber);
		this.notifyObserver("Calculating Reinforcement armies for the player " + p.getPlayerName());
		int newArmies =  p.getOwnedCountries().size() /3;

		for(Map.Entry<String, Continent> entry : Game.getMap().getContinents().entrySet()) {
		    String key = entry.getKey();
		    Continent value = entry.getValue();
		    if(value.checkOwnership(p) == true) {
		    	newArmies += value.getControlValue();
		    }
		}
		
		this.notifyObserver("Number of new armies is " + newArmies + " for player " + p.getPlayerName());
		p.setPlayerNumOfArmy(newArmies);
	}
	
	public String processReinforceCmd(int player, String[] commandComponents) {
		String countryName = commandComponents[1];
		int num = Integer.parseInt(commandComponents[2]);
		
		Player p = Game.getPlayersList().get(player);
		if(!p.getOwnedCountries().contains(countryName)) {
			return "Error : Country not owned by "+p.getPlayerName()+" or invalid country ";
		}
		if(!(p.getPlayerNumOfArmy()>=num)) {
			return "Error : Insufficient armies to move";
		}
		p.setPlayerNumOfArmy(p.getPlayerNumOfArmy()-num);
		this.notifyObserver("Calculating Reinforcement armies for the player " + p.getPlayerName());
		Country.getListOfCountries().get(countryName).setNumberOfArmies(Country.getListOfCountries().get(countryName).getNumberOfArmies()+num);
		this.notifyObserver("New armies for thr country " + countryName + " is " + Country.getListOfCountries().get(countryName).getNumberOfArmies()+num);
	
		return "";
	}
	
	public String processExchangeCardCmd(int player, String[] commandComponents) {
		return "";
	}
	
	/**
	 * This method checks and calculates the number of reinforcement armies depending on the number of players.
	 * @param player - number of player.
	 * @param command - command from the user input.
	 * @return error if incorrect or saved if correct.
	 * 
	 */
	public String reinforce(int player,String command) {	
		
		if(command.isEmpty() || command.trim().length()==0) {
			return "Error : Invalid Command";
		}
		//check if it is a reinforcement command
		String[] commandComponents = command.split(" ");
		if(commandComponents.length < 2 || commandComponents.length > 4) {
			return "Error : Number of arguments does not match";
		}
		
		String commandName = commandComponents[0];
		if(commandName.equalsIgnoreCase("reinforce")) {
			return processReinforceCmd(player, commandComponents);
		}
		else if(commandName.equalsIgnoreCase("exchangecards")) {
			return processExchangeCardCmd(player, commandComponents);
		}
		else {
			return "Error : Please enter reinforcement command";
		}
		
			
	}
	/**
	 * This method is used to calculate reinforcement armies for testing.
	 * @param currentPlayer turn of the current player.
	 * @return number of reinforced armies.
	 * 
	 */
	public int calculateReinforcementArmies(Player currentPlayer) {
		int reinformentArmies =  currentPlayer.getOwnedCountries().size() /3;
		return reinformentArmies;
	}

	@Override
	public void notifyObserver(String action) {
		this.newObserver.update(action);
		
	}
}
