package com.riskGame.Strategies;

import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Player;

import java.util.*;

public interface FortifyType {
	boolean fortify(int playerIndex);
}

class HumanFortify implements FortifyType{

	@Override
	public boolean fortify(int playerIndex) {
		return false;
	}
}

class AggresiveFortify implements FortifyType{

	@Override
	public boolean fortify(int playerIndex) {
		return false;
	}

}

class BenevolentFortify implements FortifyType{

	@Override
	public boolean fortify(int playerIndex) {
		return false;
	}

}

class CheaterFortify implements FortifyType{

	@Override
	public boolean fortify(int playerIndex) {
		Player p = Game.getPlayersList().get(playerIndex);
		Iterator ownCountryIter = p.getOwnedCountries().iterator();
		boolean fortifiedCheater = false;

		while(ownCountryIter.hasNext()){
			String ownCountry = (String) ownCountryIter.next();
			HashMap<String, Country> ownCountryNeighbors = Country.getListOfCountries().get(ownCountry).getNeighbours();
			boolean shouldDoubleFortify = false;

			for(Map.Entry<String, Country> neighbor : ownCountryNeighbors.entrySet()){
				int neighborOwner = neighbor.getValue().getOwner();
				if(neighborOwner != playerIndex){
					shouldDoubleFortify = true;
					break;
				}
			}

			// if eligible - Double the army count
			if(shouldDoubleFortify){
				Country.getListOfCountries().get(ownCountry).setNumberOfArmies( Country.getListOfCountries().get(ownCountry).getNumberOfArmies() * 2 );
				fortifiedCheater = true;
			}
		}
		return fortifiedCheater;
	}


}

class RandomFortify implements FortifyType{

	@Override
	public boolean fortify(int playerIndex) {
		Player p = Game.getPlayersList().get(playerIndex);

		// generate random from country and to country
		String fromCountry = generateFromCountry(p.getOwnedCountries());
		String toCountry = generateToCountry(Country.getListOfCountries().get(fromCountry).getNeighbours());
		int numArmiesToMove = generateRandomArmyCount(Country.getListOfCountries().get(fromCountry).getNumberOfArmies() - 1);

		// move armies
		int fromCountBeforeFortify = Country.getListOfCountries().get(fromCountry).getNumberOfArmies();
		int toCountBeforeFortify = Country.getListOfCountries().get(toCountry).getNumberOfArmies();
		Country.getListOfCountries().get(fromCountry).setNumberOfArmies(fromCountBeforeFortify - numArmiesToMove);
		Country.getListOfCountries().get(toCountry).setNumberOfArmies(toCountBeforeFortify + numArmiesToMove);

		// check if before fortify and after fortify values are same
		if(fromCountBeforeFortify == Country.getListOfCountries().get(fromCountry).getNumberOfArmies() && Country.getListOfCountries().get(toCountry).getNumberOfArmies() == toCountBeforeFortify){
			return false;
		}
		return true;
	}

	public int generateRandomArmyCount(int i) {
		Random randomArmy = new Random();
		return randomArmy.nextInt(i);
	}

	public String generateFromCountry(ArrayList<String> countryList) {
		Random randomCountry = new Random();
		return countryList.get(randomCountry.nextInt(countryList.size()));
	}

	public String generateToCountry(HashMap<String, Country> countryList) {
		Random randomCountry = new Random();
		Object[] values = countryList.values().toArray();
		Country toCountry = (Country) values[randomCountry.nextInt(values.length)];
		return toCountry.getCountryName();
	}
}
