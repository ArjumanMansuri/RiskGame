package com.riskGame.Strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Player;

public interface ReinforceType {
	boolean reinforce(int playerIndex);
}

class HumanReinforce implements ReinforceType{

	@Override
	public boolean reinforce(int playerIndex) {
		// TODO Auto-generated method stub

		return false;
	}


}


class BenevolentReinforce implements ReinforceType{


	@Override
	public boolean reinforce(int playerIndex) {
		// TODO Auto-generated method stub
		return false;
	}


}


class AggresiveReinforce implements ReinforceType{

	@Override
	public boolean reinforce(int playerIndex) {
		// TODO Auto-generated method stub
		return false;
	}

}


class RandomReinforce implements ReinforceType{

	@Override
	public boolean reinforce(int playerIndex) {
		// generate a random country and random number
		Player p = Game.getPlayersList().get(playerIndex);
		String countryName = generateRandomCountry(p.getOwnedCountries());

		// generate a random number of army count for reinforcing
		int initialArmyCount = p.getPlayerNumOfArmy();
		int randomArmyCount = generateRandomArmyCount(initialArmyCount);
		p.setPlayerNumOfArmy(initialArmyCount - randomArmyCount);

		// reinforce with new number of armies for the countrName
		int newNumberOfArmies = Country.getListOfCountries().get(countryName).getNumberOfArmies() + randomArmyCount;
		Country.getListOfCountries().get(countryName).setNumberOfArmies(newNumberOfArmies);

		if(countryName.length() < 1 || newNumberOfArmies < 1){
			return false;
		}
		return true;
	}

	public String generateRandomCountry(ArrayList<String> countryList) {
		Random randomCountry = new Random(); 
        return countryList.get(randomCountry.nextInt(countryList.size()));			
	}

	public int generateRandomArmyCount(int i) {
		Random randomArmy = new Random();
		return randomArmy.nextInt(i);
	}
}


class CheaterReinforce implements ReinforceType{

	@Override
	public boolean reinforce(int playerIndex) {
		Player p = Game.getPlayersList().get(playerIndex);
		boolean reinforceValid = true;

		// double the country army count
		for(String countryName : p.getOwnedCountries()){
			int beforeDouble = Country.getListOfCountries().get(countryName).getNumberOfArmies();
			Country.getListOfCountries().get(countryName).setNumberOfArmies(beforeDouble * 2);

			if(Country.getListOfCountries().get(countryName).getNumberOfArmies() != (beforeDouble * 2)){
				reinforceValid = false;
			}
		}

		return reinforceValid;
	}

}

