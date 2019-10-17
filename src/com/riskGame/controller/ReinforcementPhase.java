package com.riskGame.controller;


import com.riskGame.models.Game;
import com.riskGame.models.Player;

public class ReinforcementPhase {
	public String reinforce(int player,String command) {		
		Player currentPlayer = Game.getPlayersList().get(player);
		
		// command validation 
		String[] commandValidate = command.split(" ");
		if(commandValidate.length != 3 || !commandValidate[0].equals("reinforce")) {
			return "error";
		}
		
		String countryName = commandValidate[1];
		int numberOfArmies = Integer.parseInt(commandValidate[2]);
		
		// country validation
		if(currentPlayer.getOwnedCountries().containsKey(countryName)) {
			int currentArmies = currentPlayer.getOwnedCountries().get(countryName).getNumberOfArmies();
			int newArmies = calculateReinforcementArmies(currentPlayer);		
			
			if(numberOfArmies <= newArmies) {
				currentArmies += newArmies;
				currentPlayer.getOwnedCountries().get(countryName).setNumberOfArmies(currentArmies);
				currentPlayer.setPlayerNumOfArmy(currentPlayer.getPlayerNumOfArmy() + newArmies);
				return "saved";
			}			
		} 
		return "error";				
	}

	public int calculateReinforcementArmies(Player currentPlayer) {
		int reinformentArmies =  currentPlayer.getOwnedCountries().size() /3;
		return reinformentArmies;
	}
}
