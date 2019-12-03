package com.riskGame.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.riskGame.models.Continent;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Map;
import com.riskGame.models.Player;
import com.riskGame.observer.AttackPhaseObserver;
import com.riskGame.observer.PhaseViewObserver;
import com.riskGame.observer.PhaseViewPublisher;
import com.riskGame.observer.PlayerDominationViewObserver;
import com.riskGame.observer.PlayerDominationViewPublisher;
import com.riskGame.observer.StartupPhaseObserver;

/**
 * 
 * @author Arjuman Mansuri
 * This class has business logic of the attack phase
 *
 */
public class AttackPhase implements PhaseViewPublisher, PlayerDominationViewPublisher{

	private static int attackerDiceNum;
	private static int defenderDiceNum;
	private static String attackerCountry;
	private static String defenderCountry;
	private static int defenderPlayer;
	private static int attackerPlayer;
	private PhaseViewObserver newObserver;
	private static String attackerDiceRollsString;
	private static String defenderDiceRollsString;
	private PlayerDominationViewObserver newDomiantionObserver;
	/**
	 * getter method to get the attacker player number.
	 * @return defenderPlayer attacker player number.
	 *
	 */
	public static int getAttackerPlayer() {
		return AttackPhase.attackerPlayer;
	}

	/**
	 * setter method to assign the defender player number.
	 * @param defenderPlayer defender player number.
	 *
	 */
	public static void setAttackerPlayer(int attackerPlayer) {
		AttackPhase.attackerPlayer = attackerPlayer;
	}

	/**
	 * getter method to get attacker dice rolls
	 * @return attackerDiceRollsString String value of all attacker dice rolls
	 */
	public static String getAttackerDiceRollsString() {
		return AttackPhase.attackerDiceRollsString;
	}

	/**
	 * setter method to get attacker dice rolls
	 * @param attackerDiceRollsString String value of all attacker dice rolls
	 */
	public static void setAttackerDiceRollsString(String attackerDiceRollsString) {
		AttackPhase.attackerDiceRollsString = attackerDiceRollsString;
	}
	
	/**
	 * getter method to get defender dice rolls
	 * @return defenderDiceRollsString String value of all defender dice rolls
	 */
	public static String getDefenderDiceRollsString() {
		return AttackPhase.defenderDiceRollsString;
	}
	
	/**
	 * setter method to get defender dice rolls
	 * @param defenderDiceRollsString String value of all defender dice rolls
	 */
	public static void setDefenderDiceRollsString(String defenderDiceRollsString) {
		AttackPhase.defenderDiceRollsString = defenderDiceRollsString;
	}
	
	/**
	 * Default constructor which will create new observer object. 
	 */

	public AttackPhase() {
		newObserver = new AttackPhaseObserver();
		newDomiantionObserver = new PlayerDominationViewObserver();
	}

	/**
	 * getter method to get the attacker dice number.
	 * @return attackerDiceNum attacker dice number.
	 *
	 */
	public static int getAttackerDiceNum() {
		return AttackPhase.attackerDiceNum;
	}

	/**
	 * setter method to assign the attacker dice number.
	 * @param attackerDiceNum attacker dice number.
	 *
	 */
	public static void setAttackerDiceNum(int attackerDiceNum) {
		AttackPhase.attackerDiceNum = attackerDiceNum;
	}

	/**
	 * getter method to get the defender dice number.
	 * @return defenderDiceNum defender dice number.
	 *
	 */
	public static int getDefenderDiceNum() {
		return AttackPhase.defenderDiceNum;
	}

	/**
	 * setter method to assign the defender dice number.
	 * @param defenderDiceNum defender dice number.
	 *
	 */
	public static void setDefenderDiceNum(int defenderDiceNum) {
		AttackPhase.defenderDiceNum = defenderDiceNum;
	}

	/**
	 * getter method to get the attacker country name.
	 * @return attackerCountry attacker country name.
	 *
	 */
	public static String getAttackerCountry() {
		return AttackPhase.attackerCountry;
	}

	/**
	 * setter method to assign the attacker country name.
	 * @param attackerCountry attacker country name.
	 *
	 */
	public static void setAttackerCountry(String attackerCountry) {
		AttackPhase.attackerCountry = attackerCountry;
	}

	/**
	 * getter method to get the defender country name.
	 * @return defenderCountry defender country name.
	 *
	 */
	public static String getDefenderCountry() {
		return AttackPhase.defenderCountry;
	}

	/**
	 * setter method to assign the defender country name.
	 * @param defenderCountry defender country name.
	 *
	 */
	public static void setDefenderCountry(String defenderCountry) {
		AttackPhase.defenderCountry = defenderCountry;
	}

	/**
	 * getter method to get the defender player number.
	 * @return defenderPlayer defender player number.
	 *
	 */
	public static int getDefenderPlayer() {
		return AttackPhase.defenderPlayer;
	}

	/**
	 * setter method to assign the defender player number.
	 * @param defenderPlayer defender player number.
	 *
	 */
	public static void setDefenderPlayer(int defenderPlayer) {
		AttackPhase.defenderPlayer = defenderPlayer;
	}
	/**
	 * This method implements the real attack between the attacker and the defender countries
	 * @return canConquer if the attacker wins or else canNotConquer
	 * 
	 */
	public String attack() {
		// TODO Auto-generated method stub
		ArrayList<Integer> attackerDiceRolls = new ArrayList<Integer>();
		ArrayList<Integer> defenderDiceRolls = new ArrayList<Integer>();
    
		String tempString = "";
	
	for(int i=0;i<AttackPhase.getAttackerDiceNum();i++) {
			int rollNum = rollDice();
			attackerDiceRolls.add(rollNum);
			tempString =  tempString + rollNum + " ";
		}

		AttackPhase.setAttackerDiceRollsString(tempString);
		tempString="";

		for(int i=0;i<AttackPhase.getDefenderDiceNum();i++) {
			int rollNum = rollDice();
			defenderDiceRolls.add(rollNum);
			tempString =  tempString + rollNum + " ";
		}

		AttackPhase.setDefenderDiceRollsString(tempString);
		System.out.println("Attacker Dice Rolls: "+AttackPhase.getAttackerDiceRollsString()+"\n"+"Defender Dice Rolls: "+AttackPhase.getDefenderDiceRollsString());
		while(attackerDiceRolls.size()!=0 && defenderDiceRolls.size()!=0){
			// check if attacker wins
			int attackerMax = Collections.max(attackerDiceRolls);
			int defenderMax = Collections.max(defenderDiceRolls);
			if(attackerMax > defenderMax) {

				notifyObserver("Defender lost 1 army !!");
				System.out.println("Defender lost 1 army !!");
				Country.getListOfCountries().get(AttackPhase.getDefenderCountry()).setNumberOfArmies(Country.getListOfCountries().get(AttackPhase.getDefenderCountry()).getNumberOfArmies()-1);
			}
			else {
				notifyObserver("Attacker lost 1 army !!");
				System.out.println("Attacker lost 1 army !!");
				Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).setNumberOfArmies(Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).getNumberOfArmies()-1);
			}
			attackerDiceRolls.remove((Integer)attackerMax);
			defenderDiceRolls.remove((Integer)defenderMax);
		}
		if(Country.getListOfCountries().get(AttackPhase.getDefenderCountry()).getNumberOfArmies()==0) {
			Player p = Game.getPlayersList().get(Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).getOwner());
			Game.assignRandomCard(p);
			boolean controlsContinent = true;
			String continentOfConqueredCountry = Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).getContinent();
			ArrayList<Country> countries = (ArrayList<Country>) Game.getMap().getContinents().get(continentOfConqueredCountry).getTerritories();
			for(Country country : countries) {
				if(!p.getOwnedCountries().contains(country)) {
					controlsContinent = false;
					break;
				}
			}
			if(controlsContinent) {
				p.getOwnedContinents().add(continentOfConqueredCountry);
			}
			
			return "canConquer "+Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).getNumberOfArmies();
		}
		else {
			return "canNotConquer "+Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).getNumberOfArmies();
		}
	}
	

	/**
	 * This method helps to setup the defender dice number and calls the attack method
	 * @param player player number indicating the turn
	 * @param command operation to be performed by the player
	 * @return the return string from the attack method
	 * 
	 */
	public String setDefendDice(int player,String command) {
		if(command.isEmpty() || command.trim().length()==0) {
			return "Error : Invalid Command";
		}

		String[] commandComponents = command.split(" ");

		// check if it is a defend command
		String commandName = commandComponents[0];
		if(!commandName.equalsIgnoreCase("defend")) {
			return "Error : Please enter defend command";
		}
		// Arguments less than 2
		if(commandComponents.length < 2) {
			return "Error : Please include number of dice you want to roll in the command";
		}
		String numDice = commandComponents[1];
		if(!numDice.matches("\\d") || Integer.parseInt(numDice)>2) {
			return "Error : Please enter a valid number of dice (1 or 2) you want to roll";
		}

		if(Integer.parseInt(numDice) > Country.getListOfCountries().get(AttackPhase.defenderCountry).getNumberOfArmies()) {
			return "Error: You can roll a maximum of "+Country.getListOfCountries().get(AttackPhase.defenderCountry).getNumberOfArmies()+" dice.";
		}
		AttackPhase.defenderDiceNum = Integer.parseInt(numDice);
		
		return attack();
	}


	/**
	 * This methods checks if attack is possible by the attacker country
	 * @return true if possible else false
	 */
	public boolean isAttackPossible() {
		return Country.getListOfCountries().get(AttackPhase.attackerCountry).getNumberOfArmies() > 1;
	}

	/**
	 * This method checks if the player has won the game
	 * @param player number indicating player
	 * @return true if the player has won else false
	 */
	public boolean hasPlayerWon(int player) {
		return Country.getListOfCountries().size() == Game.getPlayersList().get(player).getOwnedCountries().size();
	}

	/**
	 * This method implements roll of a dice
	 * @return number between 1 to 6 inclusive
	 */
	public int rollDice() {
		return (int)(Math.random()*6)+1;
	}

	/**
	 * This method implements army moving if the attacker wins the battle
	 * @param player number indicating player
	 * @param command moveArmy command
	 * @return done if successful else error
	 */
	public String moveArmies(int player,String command) {
		if(command.isEmpty() || command.trim().length()==0) {
			return "Error : Invalid Command";
		}

		String[] commandComponents = command.split(" ");

		// check if it is a defend command
		String commandName = commandComponents[0];
		if(!commandName.equalsIgnoreCase("attackmove")) {
			return "Error : Please enter attackmove command";
		}
		// Arguments less than 2
		if(commandComponents.length < 2) {
			return "Error : Please include number of armies you want to move";
		}
		String strNumOfArmies = commandComponents[1];
		if(!strNumOfArmies.matches("\\d+")) {
			return "Error : Please enter a valid number of armies";
		}
		int numOfArmies = Integer.parseInt(strNumOfArmies);
		int numOfArmiesCanBeMoved = Country.getListOfCountries().get(AttackPhase.attackerCountry).getNumberOfArmies()-1;
		if(numOfArmies > numOfArmiesCanBeMoved) {
			return "Error : You should have at least 1 army on "+AttackPhase.attackerCountry+". Try moving "+numOfArmiesCanBeMoved+" armies";
		}
		Country.getListOfCountries().get(AttackPhase.attackerCountry).setNumberOfArmies(Country.getListOfCountries().get(AttackPhase.attackerCountry).getNumberOfArmies()-numOfArmies);
		Country.getListOfCountries().get(AttackPhase.defenderCountry).setNumberOfArmies(Country.getListOfCountries().get(AttackPhase.defenderCountry).getNumberOfArmies()+numOfArmies);
		// change ownership of defender country
		Country.getListOfCountries().get(AttackPhase.defenderCountry).setOwner(player);
		
		Game.getPlayersList().get(player).getOwnedCountries().add(defenderCountry);
		Game.getPlayersList().get(defenderPlayer).getOwnedCountries().remove(defenderCountry);

		this.notifyObserver("Ownership of country " + AttackPhase.defenderCountry + " is changed to " + Game.getPlayersList().get(player).getPlayerName());
		System.out.println("Ownership of country " + AttackPhase.defenderCountry + " is changed to " + Game.getPlayersList().get(player).getPlayerName());
		return "done";
	}

	/**
	 * This method checks if the countries provided as parameters exist
	 * @param countries List of countries present in the game
	 * @param fromCountry Name of the country from which player wants to move his army/armies
	 * @param toCountry Name of the country to which player wants to move his army/armies
	 * @return true if the countries exist else false
	 */
	public boolean doCountriesExist(ArrayList<String> countries,String fromCountry,String toCountry) {
		return (countries.contains(fromCountry)) && (countries.contains(toCountry));
	}

	/**
	 * This method checks if the countries provided as parameters are adjacent
	 * @param fromCountry Name of the country from which player wants to move his army/armies
	 * @param toCountry Name of the country to which player wants to move his army/armies
	 * @return true if the countries are adjacent else false
	 */

	public boolean areCountriesAdjacent(String fromCountry,String toCountry) {
		this.notifyObserver("Checking if the countries are adjacent for the battle");

		if(!(Country.getListOfCountries().get(fromCountry).getNeighbours().containsKey(toCountry))){
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * This method checks if the countries provided as parameters are not owned by the player
	 * @param ownedCountries List of countries owned by the player
	 * @param fromCountry Name of the country from which player wants to move his army/armies
	 * @return true if the countries are owned by the player else false
	 */
	public boolean isCountryNotOwnedByPlayer(ArrayList<String> ownedCountries,String fromCountry) {
		return !ownedCountries.contains(fromCountry);
	}

	/**
	 * This method checks if the 'fromCountry' has sufficient armies to attack
	 * @param fromCountry Name of the country from which player wants to move his army/armies
	 * @param diceNum number of armies to move
	 * @return true if sufficient armies available else false
	 */
	public boolean areArmiesSufficientToAttack(String fromCountry,int diceNum) {
		return Country.getListOfCountries().get(fromCountry).getNumberOfArmies()>diceNum;
	}

	/**
	 * This method is to notify the observer pattern
	 * @param action string to notify the observer 
	 */
	@Override
	public void notifyObserver(String action) {
		this.newObserver.update(action);

	}
	
	/**
	 * This method is to notify the Domination observer pattern
	 * @param action string to notify the observer
	 */
	public void notifyDominationObserver(String action) {
		this.newDomiantionObserver.updateDomination(action);
	}
}
