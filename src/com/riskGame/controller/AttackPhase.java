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
	private PlayerDominationViewObserver newDomiantionObserver;
	/**
	 * getter method to get the attacker player number.
	 * @return defenderPlayer attacker player number.
	 *
	 */
	public static int getAttackerPlayer() {
		return attackerPlayer;
	}

	/**
	 * setter method to assign the defender player number.
	 * @param defenderPlayer defender player number.
	 *
	 */
	public static void setAttackerPlayer(int attackerPlayer) {
		AttackPhase.attackerPlayer = attackerPlayer;
	}

	private PhaseViewObserver newObserver;

	private static String attackerDiceRollsString;
	private static String defenderDiceRollsString;

	public static String getAttackerDiceRollsString() {
		return attackerDiceRollsString;
	}

	public static void setAttackerDiceRollsString(String attackerDiceRollsString) {
		attackerDiceRollsString = attackerDiceRollsString;
	}

	public static String getDefenderDiceRollsString() {
		return defenderDiceRollsString;
	}

	public static void setDefenderDiceRollsString(String defenderDiceRollsString) {
		defenderDiceRollsString = defenderDiceRollsString;
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
		return attackerDiceNum;
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
		return defenderDiceNum;
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
		return attackerCountry;
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
		return defenderCountry;
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
		return defenderPlayer;
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
	 * This method would help setting up the attack
	 * @param player player number indicating the turn
	 * @param command operation to be performed by the player
	 * @return Defender Player if successful else error
	 * 
	 */
	public String attackSetup(int player,String command) {
		if(command.isEmpty() || command.trim().length()==0) {
			return "Error : Invalid Command";
		}

		String[] commandComponents = command.split(" ");

		// Call card exchange
		if(commandComponents[0].equalsIgnoreCase("exchangecards")) {
			ReinforcementPhase rp = new ReinforcementPhase();
			return rp.reinforce(player, command);
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
			if(!doCountriesExist(countries, fromCountry, toCountry)){
				return "Error : Either one or both of the country names do not exist";
			}
			// check if attacking country owned by player
			ArrayList<String> ownedCountries = Game.getPlayersList().get(player).getOwnedCountries();
			if(isCountryNotOwnedByPlayer(ownedCountries,fromCountry)) {
				return "Error : Country from which you want to attack is not owned by you";
			}

			// check if attacked country owned by player
			if(!isCountryNotOwnedByPlayer(ownedCountries,toCountry)) {
				return "Error : You cannot attack a country owned by you";
			}

			// check if they are adjacent
			if(!(areCountriesAdjacent(fromCountry, toCountry))){
				return "Error : Given countries are not adjacent";
			}

			this.notifyObserver(fromCountry + " and " + toCountry + " exist in countries list which are owned by neighbouring country players" );


			String numDice = commandComponents[3];

			if(numDice.equalsIgnoreCase("allout")) {
				AttackPhase.attackerCountry = fromCountry;
				AttackPhase.defenderCountry = toCountry;
				int attackerArmies = Country.getListOfCountries().get(AttackPhase.attackerCountry).getNumberOfArmies();

				if(attackerArmies == 1) {
					return "Error : You should have more than 1 army on "+AttackPhase.attackerCountry+" to attack.";
				}

				int defenderArmies = Country.getListOfCountries().get(AttackPhase.defenderCountry).getNumberOfArmies();
				if(defenderArmies == 0) {
					return "Error : You should have at least 1 army on "+AttackPhase.defenderCountry+" to defend.";
				}



				String result = "";
				int defenderPlayer = Country.getListOfCountries().get(toCountry).getOwner();

				this.notifyObserver("Attacker " + Game.getPlayersList().get(player).getPlayerName() + " has " + attackerArmies + " armies");
				this.notifyObserver("Defender " + Game.getPlayersList().get(defenderPlayer).getPlayerName()+ " has " +defenderArmies+  " armies");

				AttackPhase.defenderPlayer = defenderPlayer;
				AttackPhase.attackerPlayer = player;
				
				while(attackerArmies!=1 && defenderArmies!=0) {
					if(attackerArmies > 3) {
						AttackPhase.attackerDiceNum = 3;
					}
					else {
						AttackPhase.attackerDiceNum = attackerArmies - 1;
					}
					if(defenderArmies > 1) {
						AttackPhase.defenderDiceNum = 2;
					}
					else {
						AttackPhase.defenderDiceNum = defenderArmies;
					}
					result = attack();
					attackerArmies = Country.getListOfCountries().get(AttackPhase.attackerCountry).getNumberOfArmies();
					defenderArmies = Country.getListOfCountries().get(AttackPhase.defenderCountry).getNumberOfArmies();
				}
				return result +" "+AttackPhase.defenderPlayer;
			}
			else {
				if(command.endsWith("reattack")) {
					if(!AttackPhase.attackerCountry.equalsIgnoreCase(fromCountry)) {
						return "Error : You cannot change attacking country during reattack";
					}
					numDice = numDice.replace("reattack", "");
				}
				if(!numDice.matches("\\d") || Integer.parseInt(numDice) > 3) {
					return "Error : Please enter a valid number of dice (1, 2 or 3) you want to roll";
				}
				int diceNum = Integer.parseInt(numDice);
				// check if sufficient armies to move
				if(!areArmiesSufficientToAttack(fromCountry, diceNum)) {
					int maxArmiesToBeMoved = Country.getListOfCountries().get(fromCountry).getNumberOfArmies()-1;

					if(maxArmiesToBeMoved!=0) {
						return "Error : Insufficient armies on "+fromCountry+" to roll "+ diceNum+" dice. Try rolling "+maxArmiesToBeMoved+" dice.";
					}
					else {
						return "Error : You cannot attack from "+fromCountry+" as it has 1 army. Try attacking from other country with more than 1 army";
					}
				}
				AttackPhase.attackerDiceNum = diceNum;
				AttackPhase.attackerCountry = fromCountry;
				AttackPhase.defenderCountry = toCountry;

				// get to know the defender player
				int defenderPlayer = Country.getListOfCountries().get(toCountry).getOwner();
				AttackPhase.defenderPlayer = defenderPlayer;


				this.notifyObserver("Attacker is player : " + Game.getPlayersList().get(player).getPlayerName());
				this.notifyObserver("Defender is player : " + Game.getPlayersList().get(Country.getListOfCountries().get(toCountry).getOwner()).getPlayerName());
				this.notifyObserver("Attacker country name is " + fromCountry);
				this.notifyObserver("Defender country name is " + toCountry);

				// get to know the defender player
				int defenderPlayer = Country.getListOfCountries().get(toCountry).getOwner();
				AttackPhase.defenderPlayer = defenderPlayer;

				return "DefenderPlayer "+defenderPlayer;
			}
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
	 * This method implements the real attack between the attacker and the defender countries
	 * @return canConquer if the attacker wins or else canNotConquer
	 * 
	 */
	public String attack() {
		ArrayList<Integer> attackerDiceRolls = new ArrayList<Integer>();
		ArrayList<Integer> defenderDiceRolls = new ArrayList<Integer>();
    
		String tempString = "";
	
  for(int i=0;i<AttackPhase.attackerDiceNum;i++) {
			int rollNum = rollDice();
			attackerDiceRolls.add(rollNum);
			tempString =  tempString + rollNum + " ";
		}

		attackerDiceRollsString = tempString;
		tempString="";

		for(int i=0;i<AttackPhase.defenderDiceNum;i++) {
			int rollNum = rollDice();
			defenderDiceRolls.add(rollNum);
			tempString =  tempString + rollNum + " ";
		}

		defenderDiceRollsString = tempString;
		

		while(attackerDiceRolls.size()!=0 && defenderDiceRolls.size()!=0){
			// check if attacker wins
			int attackerMax = Collections.max(attackerDiceRolls);
			int defenderMax = Collections.max(defenderDiceRolls);
			if(attackerMax > defenderMax) {

				this.notifyObserver("Defender lost 1 army !!");
				Country.getListOfCountries().get(AttackPhase.defenderCountry).setNumberOfArmies(Country.getListOfCountries().get(AttackPhase.defenderCountry).getNumberOfArmies()-1);
			}
			else {
				this.notifyObserver("Attacker1 lost 1 army !!");
				Country.getListOfCountries().get(AttackPhase.attackerCountry).setNumberOfArmies(Country.getListOfCountries().get(AttackPhase.attackerCountry).getNumberOfArmies()-1);
			}
			attackerDiceRolls.remove((Integer)attackerMax);
			defenderDiceRolls.remove((Integer)defenderMax);
		}
		if(Country.getListOfCountries().get(AttackPhase.defenderCountry).getNumberOfArmies()==0) {
			Player p = Game.getPlayersList().get(Country.getListOfCountries().get(AttackPhase.attackerCountry).getOwner());
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
			
			return "canConquer "+Country.getListOfCountries().get(AttackPhase.attackerCountry).getNumberOfArmies();
		}
		else {
			return "canNotConquer "+Country.getListOfCountries().get(AttackPhase.attackerCountry).getNumberOfArmies();
		}

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

		this.notifyObserver("Ownership of country " + AttackPhase.defenderCountry + "is changed to " + Game.getPlayersList().get(player).getPlayerName());

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
	
	public void notifyDominationObserver(String action) {
		this.newDomiantionObserver.updateDomination(action);
	}
}
