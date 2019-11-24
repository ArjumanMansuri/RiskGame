package com.riskGame.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.riskGame.controller.AttackPhase;
import com.riskGame.controller.MapFileEdit;
import com.riskGame.controller.ReinforcementPhase;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Player;

public interface AttackType {
	String attackSetup(int player,String ...command);
	AttackPhase ap = new AttackPhase();
}

 class HumanAttack implements AttackType{
	 
	 	/**
		 * This method would help setting up the attack
		 * @param player player number indicating the turn
		 * @param command operation to be performed by the player
		 * @return Defender Player if successful else error
		 * 
		 */
	 	@Override
		public String attackSetup(int player,String ...command) {
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

			// check if it is an attack command
			String commandName = commandComponents[0];
			if(!commandName.equalsIgnoreCase("attack")) {
				return "Error : Please enter attack command";
			}
			// attack noattack
			if(commandComponents.length == 2) {
				if(commandComponents[1].equalsIgnoreCase("noattack")) {
					return "done";
				}
			}

			if(!(commandComponents.length == 4)) {
				return "Error : Number of arguments does not match";
			}
			else {
				String fromCountry = commandComponents[1];
				String toCountry = commandComponents[2];
				
				 
		            HashMap<String,Country> countriesMap = new HashMap<>();
		            countriesMap = Country.getListOfCountries();
		            ArrayList<String> countries = new ArrayList<String>(countriesMap.keySet());
		           
			
				// check if those countries exist
				if(!ap.doCountriesExist(countries, fromCountry, toCountry)){
					return "Error : Either one or both of the country names do not exist";
				}
				// check if attacking country owned by player
				ArrayList<String> ownedCountries = Game.getPlayersList().get(player).getOwnedCountries();
				if(ap.isCountryNotOwnedByPlayer(ownedCountries,fromCountry)) {
					return "Error : Country from which you want to attack is not owned by you";
				}

				// check if attacked country owned by player
				if(!ap.isCountryNotOwnedByPlayer(ownedCountries,toCountry)) {
					return "Error : You cannot attack a country owned by you";
				}

				// check if they are adjacent
				if(!(ap.areCountriesAdjacent(fromCountry, toCountry))){
					return "Error : Given countries are not adjacent";
				}

				//this.notifyObserver(fromCountry + " and " + toCountry + " exist in countries list which are owned by neighbouring country players" );


				String numDice = commandComponents[3];
				AttackPhase.setAttackerPlayer(player);
				if(numDice.equalsIgnoreCase("allout")) {
					AttackPhase.setAttackerCountry(fromCountry);
					AttackPhase.setDefenderCountry(toCountry);
					int attackerArmies = Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).getNumberOfArmies();

					if(attackerArmies == 1) {
						return "Error : You should have more than 1 army on "+AttackPhase.getAttackerCountry()+" to attack.";
					}

					int defenderArmies = Country.getListOfCountries().get(AttackPhase.getDefenderCountry()).getNumberOfArmies();
					if(defenderArmies == 0) {
						return "Error : You should have at least 1 army on "+AttackPhase.getDefenderCountry()+" to defend.";
					}



					String result = "";
					int defenderPlayer = Country.getListOfCountries().get(toCountry).getOwner();

					ap.notifyObserver("Attacker " + Game.getPlayersList().get(player).getPlayerName() + " has " + attackerArmies + " armies");
					ap.notifyObserver("Defender " + Game.getPlayersList().get(defenderPlayer).getPlayerName()+ " has " +defenderArmies+  " armies");

					AttackPhase.setDefenderPlayer(defenderPlayer);
					
					
					while(attackerArmies!=1 && defenderArmies!=0) {
						if(attackerArmies > 3) {
							AttackPhase.setAttackerDiceNum(3);
						}
						else {
							AttackPhase.setAttackerDiceNum(attackerArmies - 1);
						}
						if(defenderArmies > 1) {
							AttackPhase.setDefenderDiceNum(2);
						}
						else {
							AttackPhase.setDefenderDiceNum(defenderArmies);
						}
						result = ap.attack();
						attackerArmies = Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).getNumberOfArmies();
						defenderArmies = Country.getListOfCountries().get(AttackPhase.getDefenderCountry()).getNumberOfArmies();
					}
					return result +" "+AttackPhase.getDefenderPlayer();
				}
				else {
					if(command[0].endsWith("reattack")) {
						if(!AttackPhase.getAttackerCountry().equalsIgnoreCase(fromCountry)) {
							return "Error : You cannot change attacking country during reattack";
						}
						numDice = numDice.replace("reattack", "");
					}
					if(!numDice.matches("\\d") || Integer.parseInt(numDice) > 3) {
						return "Error : Please enter a valid number of dice (1, 2 or 3) you want to roll";
					}
					int diceNum = Integer.parseInt(numDice);
					// check if sufficient armies to move
					if(!ap.areArmiesSufficientToAttack(fromCountry, diceNum)) {
						int maxArmiesToBeMoved = Country.getListOfCountries().get(fromCountry).getNumberOfArmies()-1;

						if(maxArmiesToBeMoved!=0) {
							return "Error : Insufficient armies on "+fromCountry+" to roll "+ diceNum+" dice. Try rolling "+maxArmiesToBeMoved+" dice.";
						}
						else {
							return "Error : You cannot attack from "+fromCountry+" as it has 1 army. Try attacking from other country with more than 1 army";
						}
					}
					AttackPhase.setAttackerDiceNum(diceNum);
					AttackPhase.setAttackerCountry(fromCountry);
					AttackPhase.setDefenderCountry(toCountry);

					// get to know the defender player
					int defenderPlayer = Country.getListOfCountries().get(toCountry).getOwner();
					AttackPhase.setDefenderPlayer(defenderPlayer);

					ap.notifyObserver("Attacker is player : " + Game.getPlayersList().get(player).getPlayerName());
					ap.notifyObserver("Defender is player : " + Game.getPlayersList().get(Country.getListOfCountries().get(toCountry).getOwner()).getPlayerName());
					ap.notifyObserver("Attacker country name is " + fromCountry);
					ap.notifyObserver("Defender country name is " + toCountry);

					return "DefenderPlayer "+defenderPlayer;
				}
			}
		}
}

 class AggresiveAttack implements AttackType{

	@Override
	public String attackSetup(int player,String ...command) {
		// TODO Auto-generated method stub

		// select fromCountry
		ArrayList<String> ownedCountries = Game.getPlayersList().get(player).getOwnedCountries();
		int maxArmies = 0;
		String fromCountry = "";
		String toCountry = "";
		ArrayList<String> maxTriedCountries = new ArrayList<String>();
		do {
			maxArmies = 0;
			for(String country : ownedCountries) {
				if(Country.getListOfCountries().get(country).getNumberOfArmies() > maxArmies && !maxTriedCountries.contains(country)) {
					maxArmies = Country.getListOfCountries().get(country).getNumberOfArmies();
					fromCountry = country;
				}
			}
			maxTriedCountries.add(fromCountry);
			
			// select toCountry
			for(String country : Country.getListOfCountries().get(fromCountry).getNeighbours().keySet()) {
				if(ap.isCountryNotOwnedByPlayer(ownedCountries,country)) {
					toCountry = country;
					break;
				}
			}
		}while(toCountry.length()==0);
		System.out.println(fromCountry + " and " + toCountry + " exist in countries list which are owned by neighbouring country players");	
		ap.notifyObserver(fromCountry + " and " + toCountry + " exist in countries list which are owned by neighbouring country players" );

		AttackPhase.setAttackerPlayer(player);
		AttackPhase.setAttackerCountry(fromCountry);
		AttackPhase.setDefenderCountry(toCountry);
		int attackerArmies = Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).getNumberOfArmies();
		int defenderArmies = Country.getListOfCountries().get(AttackPhase.getDefenderCountry()).getNumberOfArmies();
		
		int defenderPlayer = Country.getListOfCountries().get(toCountry).getOwner();

		System.out.println("Attacker " + Game.getPlayersList().get(player).getPlayerName() + " has " + attackerArmies + " armies");
		ap.notifyObserver("Attacker " + Game.getPlayersList().get(player).getPlayerName() + " has " + attackerArmies + " armies");
		System.out.println("Defender " + Game.getPlayersList().get(defenderPlayer).getPlayerName()+ " has " +defenderArmies+  " armies");
		ap.notifyObserver("Defender " + Game.getPlayersList().get(defenderPlayer).getPlayerName()+ " has " +defenderArmies+  " armies");

		AttackPhase.setDefenderPlayer(defenderPlayer);
		
		while(attackerArmies!=1 && defenderArmies!=0) {
			if(attackerArmies > 3) {
				AttackPhase.setAttackerDiceNum(3);
			}
			else {
				AttackPhase.setAttackerDiceNum(attackerArmies - 1);
			}
			if(defenderArmies > 1) {
				AttackPhase.setDefenderDiceNum(2);
			}
			else {
				AttackPhase.setDefenderDiceNum(defenderArmies);
			}
			ap.attack();
			
			attackerArmies = Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).getNumberOfArmies();
			defenderArmies = Country.getListOfCountries().get(AttackPhase.getDefenderCountry()).getNumberOfArmies();
		}
		String moveArmiesCommand = "attackmove "+ String.valueOf(attackerArmies-1);
		return ap.moveArmies(AttackPhase.getAttackerPlayer(), moveArmiesCommand);
		}
	}
	


 class BenevolentAttack implements AttackType{

	@Override
	public String attackSetup(int player,String ...command) {
		// TODO Auto-generated method stub
		return "";
	}
	
}

 class CheaterAttack implements AttackType{

	@Override
	public String attackSetup(int player,String ...command){
		// TODO Auto-generated method stub
		return "";
	}
	
}

class RandomAttack implements AttackType{

	@Override
	public String attackSetup(int player,String ...command) {
		// TODO Auto-generated method stub
		return "";
	}
	
}