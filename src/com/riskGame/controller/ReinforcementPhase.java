package com.riskGame.controller;


import com.riskGame.models.Game;
import com.riskGame.models.Player;

public class ReinforcementPhase {
	
	public void calculateReinforcementArmies(int i) {
		Player p = Game.getPlayersList().get(i);
		int newArmies =  calculateReinforcementArmies(p);
		p.setPlayerNumOfArmy(newArmies);
	}
	
	public String reinforce(int player,String command) {	
		
		if(command.isEmpty() || command.trim().length()==0) return "Error : Invalid Command";
		//check if it is a reinforcement command
		String[] commandComponents = command.split(" ");
		if(!(commandComponents.length == 3)) {
			return "Error : Number of arguments does not match";
		}
		
		String commandName = commandComponents[0];
		if(!commandName.equalsIgnoreCase("reinforce")) {
			return "Error : Please enter reinforcement command";
		}
		String countryName = commandComponents[1];
		int num = Integer.parseInt(commandComponents[2]);
		// reinforce 'countryName' 'num'
		
		
			Player p = Game.getPlayersList().get(player);
			if(!p.getOwnedCountries().containsKey(countryName)) {
				return "Error : Country not owned by "+p.getPlayerName()+" or invalid country ";
			}
			if(!(p.getPlayerNumOfArmy()>=num)) {
				return "Error : Insufficient armies to move";
			}
			p.setPlayerNumOfArmy(p.getPlayerNumOfArmy()-num);
			p.getOwnedCountries().get(countryName).setNumberOfArmies(p.getOwnedCountries().get(countryName).getNumberOfArmies()+num);
		
		
		return "";			
	}
	
	public int calculateReinforcementArmies(Player currentPlayer) {
		int reinformentArmies =  currentPlayer.getOwnedCountries().size() /3;
		return reinformentArmies;
	}
}
