package com.riskGame.Strategies;

import java.util.ArrayList;
import java.util.Collections;

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

	@Override
	public String attack() {
		// TODO Auto-generated method stub
		return "";
	}
	
}

class RandomAttack implements AttackType{

	@Override
	public String attack() {
		// TODO Auto-generated method stub
		return "";
	}
	
}