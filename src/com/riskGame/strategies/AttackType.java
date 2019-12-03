package com.riskGame.strategies;

import java.util.*;
import java.io.Serializable;
import java.security.KeyStore.Entry;

import com.riskGame.controller.AttackPhase;
import com.riskGame.controller.MapFileEdit;
import com.riskGame.controller.ReinforcementPhase;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Player;

/**
 * This interface helps implement strategy pattern for attack type
 * @author Arjuman Mansuri
 *
 */
public interface AttackType {
	String attackSetup(int player,String ...command);
	AttackPhase ap = new AttackPhase();
}

/**
 * This class contains the business logic for human attack type
 * @author Arjuman Mansuri
 *
 */
class HumanAttack implements AttackType,Serializable{
	 
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
				if(commandComponents[1].equalsIgnoreCase("-noattack")) {
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
/**
 * This class contains the business logic for aggressive attack type
 * @author Arjuman Mansuri
 *
 */
 class AggresiveAttack implements AttackType,Serializable{

	 /**
	  * This method would help setting up the attack
	  * @param player player number indicating the turn
	  * @param command in this case would be empty
	  * @return Defender Player if successful else error
	  * 
	  */
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
			if(ownedCountries.size() == maxTriedCountries.size()) {
				System.out.println("Attack skipped");
				break;
			}											  
		}while(toCountry.length()==0);
		System.out.println(fromCountry + " and " + toCountry + " exist in countries list which are owned by neighbouring country players");	
		ap.notifyObserver(fromCountry + " and " + toCountry + " exist in countries list which are owned by neighbouring country players" );

		AttackPhase.setAttackerPlayer(player);
		AttackPhase.setAttackerCountry(fromCountry);	// countries with max armies as fromCountry
		AttackPhase.setDefenderCountry(toCountry);
		int attackerArmies = Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).getNumberOfArmies();
		int defenderArmies = Country.getListOfCountries().get(AttackPhase.getDefenderCountry()).getNumberOfArmies();
		
		int defenderPlayer = Country.getListOfCountries().get(toCountry).getOwner();

		System.out.println("Attacker " + Game.getPlayersList().get(player).getPlayerName() + " has " + attackerArmies + " armies");
		ap.notifyObserver("Attacker " + Game.getPlayersList().get(player).getPlayerName() + " has " + attackerArmies + " armies");
		System.out.println("Defender " + Game.getPlayersList().get(defenderPlayer).getPlayerName()+ " has " +defenderArmies+  " armies");
		ap.notifyObserver("Defender " + Game.getPlayersList().get(defenderPlayer).getPlayerName()+ " has " +defenderArmies+  " armies");

		AttackPhase.setDefenderPlayer(defenderPlayer);
		
		String result = "";
		// allout attack implementation						 
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
		if(result.contains("canConquer")) {
			// conquer defender country by moving all armies except one to it
			String moveArmiesCommand = "attackmove "+ String.valueOf(attackerArmies-1);
			return ap.moveArmies(AttackPhase.getAttackerPlayer(), moveArmiesCommand);
		}
		else {
			return result;
		}
	}
 }
 /**
  * This class contains the business logic for benevolent attack type
  * @author Arjuman Mansuri
  *
  */
 class BenevolentAttack implements AttackType,Serializable{

	 /**
	  * This method would help setting up the attack
	  * @param player player number indicating the turn
	  * @param command in this case would be empty
	  * @return done if successful
	  * 
	  */
	@Override
	public String attackSetup(int player,String ...command) {
		// TODO Auto-generated method stub
		System.out.println("Attack skipped");
		return "done";
	}
 }

 /**
  * This class contains the business logic for cheater attack type
  * @author Arjuman Mansuri
  *
  */
 class CheaterAttack implements AttackType,Serializable{
	 protected ArrayList<String> conqueredList;

	 /**
	  * This method performs the move armies operation
	  * @param attackerCountry current player's owned country
	  * @param defenderCountry neighbor country of the owner country
	  */
	 private void moveArmies(String attackerCountry, Country defenderCountry, int player) {
		 int numOfArmiesCanBeMoved = Country.getListOfCountries().get(attackerCountry).getNumberOfArmies() / 2;
		 
		 // Set number of armies
		 Country.getListOfCountries().get(attackerCountry).setNumberOfArmies(Country.getListOfCountries().get(attackerCountry).getNumberOfArmies() - numOfArmiesCanBeMoved);
		 Country.getListOfCountries().get(defenderCountry.getCountryName()).setNumberOfArmies(Country.getListOfCountries().get(defenderCountry.getCountryName()).getNumberOfArmies() + numOfArmiesCanBeMoved);

		 // change ownership of defender country
		 Country.getListOfCountries().get(defenderCountry.getCountryName()).setOwner(player);

		 Game.getPlayersList().get(player).getOwnedCountries().add(defenderCountry.getCountryName());
		 Game.getPlayersList().get(defenderCountry.getOwner()).getOwnedCountries().remove(defenderCountry.getCountryName());

		 // add defender country to the conquered list (so that it is skipped in the next iteration)
		 conqueredList.add(defenderCountry.getCountryName());
	 }

	 /**
	  * This method would help setting up the attack
	  * @param player player number indicating the turn
	  * @param command in this case would be empty
	  * @return done if successful else error
	  * 
	  */
	 @Override
	 public String attackSetup(int player, String... command) {
		 conqueredList = new ArrayList<String>();
		 Player p = Game.getPlayersList().get(player);		 
		 HashMap<String,ArrayList> possibleNeighbors = new HashMap<String,ArrayList>();		 
		 Iterator ownCountryIter = p.getOwnedCountries().iterator();
		 
		 while(ownCountryIter.hasNext()){
			 ArrayList<Country> neighborCountriesList = new ArrayList<Country>();
			 String ownCountry = (String) ownCountryIter.next();
			 HashMap<String, Country> ownCountryNeighbors = Country.getListOfCountries().get(ownCountry).getNeighbours();

			 for(Map.Entry<String, Country> neighbor : ownCountryNeighbors.entrySet()){
				 if(!conqueredList.contains(neighbor.getKey())) {
					 Country neighborCountry = Country.getListOfCountries().get(neighbor.getValue().getCountryName());
					 if (neighborCountry.getOwner() != player) {
						 neighborCountriesList.add(neighborCountry);						 	 
						 break;
					 }
				 }
			 }
			 
			 possibleNeighbors.put(ownCountry, neighborCountriesList);
		 }
		 
		 for(java.util.Map.Entry<String, ArrayList> neighborEntry : possibleNeighbors.entrySet()) {
			 String ownCountry = neighborEntry.getKey();
			 ArrayList<Country> neighborCountries = neighborEntry.getValue();
			 
			 for(Country neighborCountry : neighborCountries) {
				 moveArmies(ownCountry, neighborCountry, player);
			 }			 
		 }	 
		 
		 return "done";
	 }

 }
 
 /**
  * This class contains the business logic for random attack type
  * @author Arjuman Mansuri
  *
  */
 class RandomAttack implements AttackType,Serializable {
	static final int MAX_LIMIT_ATTACK_RANDOM = 5;
	protected ArrayList<String> ownedCountryLostList;
	
	/**
	  * This method would help setting up the attack
	  * @param player player number indicating the turn
	  * @param command in this case would be empty
	  * @return done if successful else error
	  * 
	  */
	@Override
	public String attackSetup ( int player, String ...command){
		Player p = Game.getPlayersList().get(player);

		String attackCountries = "";
		int attackCounter = generateRandomCount(MAX_LIMIT_ATTACK_RANDOM);
		System.out.println("Attack will happen "+attackCounter+" times.");
		while (attackCounter > 0) {
			System.out.println("Main Attack "+attackCounter);
			attackCountries = generateRandomAttackerAndDefender(p.getOwnedCountries()); // generating a random attacker and defender country for attack
			if(attackCountries.trim().length()==0) {
				System.out.println("Main Attack "+attackCounter+" skipped");
				System.out.println();
				attackCounter--;
				continue;
			}

			String attackerCountry = attackCountries.split(",")[0];
			String defenderCountry = attackCountries.split(",")[1];
			// Attack with attackerCountry and randomSelectedDefenderCountry
			int attackerDiceNum = generateAttackerDiceNum(attackerCountry);
			int defenderDiceNum = generateDefenderDiceNum(defenderCountry);
			
			AttackPhase.setAttackerPlayer(player);
			AttackPhase.setAttackerCountry(attackerCountry);
			AttackPhase.setAttackerDiceNum(attackerDiceNum);
		
			int defenderPlayer = Country.getListOfCountries().get(defenderCountry).getOwner();
			
			AttackPhase.setDefenderPlayer(defenderPlayer);
			AttackPhase.setDefenderCountry(defenderCountry);
			AttackPhase.setDefenderDiceNum(defenderDiceNum);
			int singleAttackCounter = generateRandomCount(MAX_LIMIT_ATTACK_RANDOM);
			System.out.println("Will try to attack "+singleAttackCounter+" times each.");
			int attackerArmies = Country.getListOfCountries().get(attackerCountry).getNumberOfArmies();
			int defenderArmies = Country.getListOfCountries().get(defenderCountry).getNumberOfArmies();
			while(attackerArmies>1 && singleAttackCounter>0 && defenderArmies>0) {
				System.out.println("Sub Attack "+singleAttackCounter);
				System.out.println("Attacker Country : "+attackerCountry);
				System.out.println("Defender Country : "+defenderCountry);
				System.out.println("Attacker " + Game.getPlayersList().get(player).getPlayerName() + " has " + attackerArmies + " armies");
				ap.notifyObserver("Attacker " + Game.getPlayersList().get(player).getPlayerName() + " has " + attackerArmies + " armies");
				System.out.println("Defender " + Game.getPlayersList().get(defenderPlayer).getPlayerName()+ " has " +defenderArmies+  " armies");
				ap.notifyObserver("Defender " + Game.getPlayersList().get(defenderPlayer).getPlayerName()+ " has " +defenderArmies+  " armies");
				
				String result = ap.attack();
				
				attackerArmies = Country.getListOfCountries().get(attackerCountry).getNumberOfArmies();
				defenderArmies = Country.getListOfCountries().get(defenderCountry).getNumberOfArmies();
				if(result.contains("canConquer")) {
					// conquer defender country by moving all armies except one to it
					int moveArmies = generateRandomCount(attackerArmies-1);
					String moveArmiesCommand = "attackmove "+ String.valueOf(moveArmies);
					ap.moveArmies(AttackPhase.getAttackerPlayer(), moveArmiesCommand);
					
					System.out.println("Ending main attack "+attackCounter);
					attackCounter--;
					continue;
				}
				singleAttackCounter--;
			}
			attackCounter--;
			System.out.println();
		}
		return "";
	}
	
	/**
	 * This method generates a random number of dice to be rolled for attacker
	 * @param attackerCountry country from which attack is made
	 * @return number of dice
	 */
	public int generateAttackerDiceNum(String attackerCountry) {
		int numOfArmies = Country.getListOfCountries().get(attackerCountry).getNumberOfArmies();
		if(numOfArmies>3) {
			return generateRandomCount(3);
		}
		else if(numOfArmies>2) {
			return generateRandomCount(2);
		}
		else {
			return 1;
		}
	}
	
	/**
	 * This method generates a random number of dice to be rolled for defender
	 * @param defenderCountry country to which attack is made
	 * @return number of dice
	 */
	public int generateDefenderDiceNum(String defenderCountry) {
		int numOfArmies = Country.getListOfCountries().get(defenderCountry).getNumberOfArmies();
		if(numOfArmies>1) {
			return generateRandomCount(2);
		}
		else {
			return 1;
		}
	}
	
	/**
	 * This method generates a random integer between 1 and max
	 * @param max the upper bound to generate a random number
	 * @return randomly generated number
	 */
	public int generateRandomCount (int max){
		return (int)(Math.random()*max)+1;
	}

	/**
	 * This method generates random attacker and defender countries
	 * @param countryList list of attacker's countries
	 * @return attacker and defender country if possible else empty string
	 */
	public String generateRandomAttackerAndDefender (ArrayList <String> countryList) {
		Random randomAttackerCountry = new Random();
		String attackerCountry;
		String defenderCountry;
		
		ArrayList<String> triedCountries = new ArrayList<String>();
		
		do {
			attackerCountry = countryList.get(randomAttackerCountry.nextInt(countryList.size()));
			triedCountries.add(attackerCountry);
			if(triedCountries.size()==countryList.size()) {
				break;
			}
		}while(!(Country.getListOfCountries().get(attackerCountry).getNumberOfArmies()>1));
		// if a country with 1 army was selected
		if(!(Country.getListOfCountries().get(attackerCountry).getNumberOfArmies()>1)) {
			return "";
		}
		
		triedCountries.clear();
		ArrayList<String> potentialDefenderCountries = new ArrayList<String>(Country.getListOfCountries().get(attackerCountry).getNeighbours().keySet());
		while(true) {
			defenderCountry = potentialDefenderCountries.get(randomAttackerCountry.nextInt(potentialDefenderCountries.size()));
			triedCountries.add(defenderCountry);
			if(ap.isCountryNotOwnedByPlayer(countryList,defenderCountry) || triedCountries.size()==potentialDefenderCountries.size()) {
				break;
			}
		}
		if(!ap.isCountryNotOwnedByPlayer(countryList,defenderCountry)) {
			return "";
		}
		
		return attackerCountry +","+defenderCountry;
	}
}