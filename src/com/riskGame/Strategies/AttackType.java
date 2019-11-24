package com.riskGame.Strategies;

import java.util.*;

import com.riskGame.controller.AttackPhase;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Player;

public interface AttackType {
	String attack();
}

 class HumanAttack implements AttackType{

	/**
	 * This method implements the real attack between the attacker and the defender countries
	 * @return canConquer if the attacker wins or else canNotConquer
	 * 
	 */
	@Override
	public String attack() {
		// TODO Auto-generated method stub
		ArrayList<Integer> attackerDiceRolls = new ArrayList<Integer>();
		ArrayList<Integer> defenderDiceRolls = new ArrayList<Integer>();
    
		String tempString = "";
	AttackPhase ap = new AttackPhase();
	for(int i=0;i<AttackPhase.getAttackerDiceNum();i++) {
			int rollNum = ap.rollDice();
			attackerDiceRolls.add(rollNum);
			tempString =  tempString + rollNum + " ";
		}

		AttackPhase.setAttackerDiceRollsString(tempString);
		tempString="";

		for(int i=0;i<AttackPhase.getDefenderDiceNum();i++) {
			int rollNum = ap.rollDice();
			defenderDiceRolls.add(rollNum);
			tempString =  tempString + rollNum + " ";
		}

		AttackPhase.setDefenderDiceRollsString(tempString);
		

		while(attackerDiceRolls.size()!=0 && defenderDiceRolls.size()!=0){
			// check if attacker wins
			int attackerMax = Collections.max(attackerDiceRolls);
			int defenderMax = Collections.max(defenderDiceRolls);
			if(attackerMax > defenderMax) {

				ap.notifyObserver("Defender lost 1 army !!");
				Country.getListOfCountries().get(AttackPhase.getDefenderCountry()).setNumberOfArmies(Country.getListOfCountries().get(AttackPhase.getDefenderCountry()).getNumberOfArmies()-1);
			}
			else {
				ap.notifyObserver("Attacker1 lost 1 army !!");
				Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).setNumberOfArmies(Country.getListOfCountries().get(AttackPhase.getAttackerCountry()).getNumberOfArmies()-1);
			}
			attackerDiceRolls.remove((Integer)attackerMax);
			defenderDiceRolls.remove((Integer)defenderMax);
		}
		if(Country.getListOfCountries().get(AttackPhase.getDefenderCountry()).getNumberOfArmies()==0) {
			Player p = Game.getPlayersList().get(Country.getListOfCountries().get(AttackPhase.getDefenderCountry()).getOwner());
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
	
}

 class AggresiveAttack implements AttackType{

	@Override
	public String attack() {
		// TODO Auto-generated method stub
		return "";
	}
	
}

 class BenevolentAttack implements AttackType{

	@Override
	public String attack() {
		// TODO Auto-generated method stub
		return "";
	}
	
}

 class CheaterAttack implements AttackType{
	 protected int playerIndex;
	 protected ArrayList<String> conqueredList;

	 public CheaterAttack(int playerNumber) {
			playerIndex = playerNumber;
			conqueredList = new ArrayList<String>();
	 }

	 @Override
	public String attack() {
		Player p = Game.getPlayersList().get(playerIndex);
		Iterator ownCountryIter = p.getOwnedCountries().iterator();

		while(ownCountryIter.hasNext()){
			String ownCountry = (String) ownCountryIter.next();
			HashMap<String, Country> ownCountryNeighbors = Country.getListOfCountries().get(ownCountry).getNeighbours();

			for(Map.Entry<String, Country> neighbor : ownCountryNeighbors.entrySet()){
				if(!conqueredList.contains(neighbor.getKey())) {
					int neighborOwner = neighbor.getValue().getOwner();
					if (neighborOwner != playerIndex) {
						moveArmies(ownCountry, neighbor.getValue());
						break;
					}
				}
			}
		}
		return "";
	}

	 /**
	  *
	  * @param attackerCountry current player's owned country
	  * @param defenderCountry neighbor country of the owner country
	  */
	 private void moveArmies(String attackerCountry, Country defenderCountry) {
		 int numOfArmiesCanBeMoved = Country.getListOfCountries().get(attackerCountry).getNumberOfArmies() / 2;

		 // Set number of armies
		 Country.getListOfCountries().get(attackerCountry).setNumberOfArmies(Country.getListOfCountries().get(attackerCountry).getNumberOfArmies() - numOfArmiesCanBeMoved);
		 Country.getListOfCountries().get(defenderCountry).setNumberOfArmies(Country.getListOfCountries().get(defenderCountry.getCountryName()).getNumberOfArmies() + numOfArmiesCanBeMoved);

		 // change ownership of defender country
		 Country.getListOfCountries().get(defenderCountry).setOwner(playerIndex);

		 Game.getPlayersList().get(playerIndex).getOwnedCountries().add(defenderCountry.getCountryName());
		 Game.getPlayersList().get(defenderCountry.getOwner()).getOwnedCountries().remove(defenderCountry.getCountryName());

		 // add defender country to the conquered list (so that it is skipped in the next iteration)
		 conqueredList.add(defenderCountry.getCountryName());
	 }

 }

class RandomAttack implements AttackType{
	static final int MAX_LIMIT_ATTACK_RANDOM = 5;
	protected int playerIndex;
	protected ArrayList<String> ownedCountryLostList;

	public RandomAttack(int playerNumber) {
		playerIndex = playerNumber;
		ownedCountryLostList = new ArrayList<String>();
	}

	@Override
	public String attack() {
		Player p = Game.getPlayersList().get(playerIndex);
		Iterator ownCountryIter = p.getOwnedCountries().iterator();

		String attackerCountry = generateRandomCountry(p.getOwnedCountries()); // init value
		int randomAttackCount = generateRandomAttackCount(MAX_LIMIT_ATTACK_RANDOM);
		int attackCounter = 0;

		while(attackCounter != 0){
			// check attacker country still with us ( not lost ) && number of attack count is greater than 0
			if(ownedCountryLostList.contains(attackerCountry)){
				attackerCountry = generateRandomCountry(p.getOwnedCountries()); // init value
			}
			HashMap<String, Country> ownCountryNeighbors = Country.getListOfCountries().get(attackerCountry).getNeighbours();
			ArrayList<String> enemyNeighborCountries = new ArrayList<String>;

			// generate a country list of enemy neighbors
			for(Map.Entry<String, Country> neighbor : ownCountryNeighbors.entrySet()){
				int neighborOwner = neighbor.getValue().getOwner();
				if(neighborOwner != playerIndex){
					enemyNeighborCountries.add(neighbor.getKey());
				}
			}

			// generate random defender country
			String randomSelectedDefenderCountry = generateRandomCountry(enemyNeighborCountries);

			// Attack with attackerCountry and randomSelectedDefenderCountry
			int randomNumDice = generateRandomAttackCount(6);


			// when the attackerCountry loses, remove it from the p.getOwnedCountries() - remove - and setOwnedCountries()
			attackCounter--;
		}

		return "";
	}

	public int generateRandomAttackCount(int max) {
		Random randomArmy = new Random();
		return randomArmy.nextInt(max);
	}

	public String generateRandomCountry(ArrayList<String> countryList) {
		Random randomCountry = new Random();
//		if(ownedCountryLostList.size() > 0){
//			for (String lostCountry : ownedCountryLostList) {
//				countryList.removeIf(countryListElement -> (countryListElement.equalsIgnoreCase(lostCountry)));
//			}
//			return countryList.get(randomCountry.nextInt(countryList.size()));
//		}
		return countryList.get(randomCountry.nextInt(countryList.size()));
	}
	
}