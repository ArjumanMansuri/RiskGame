package com.riskGame.Strategies;

import java.util.ArrayList;
import java.util.Random;
import com.riskGame.models.Game;
import com.riskGame.models.Player;

public interface ReinforceType {
	void reinforce(int player);
}

class HumanReinforce implements ReinforceType{

	@Override
	public void reinforce(int player) {
		// TODO Auto-generated method stub
		
	}


}


class BenevolentReinforce implements ReinforceType{


	@Override
	public void reinforce(int player) {
		// TODO Auto-generated method stub
		
	}


}


class AggresiveReinforce implements ReinforceType{

	@Override
	public void reinforce(int player) {
		// TODO Auto-generated method stub
		
	}



}


class RandomReinforce implements ReinforceType{

	@Override
	public void reinforce(int player) {
		// generate a random country and random number	
		Player p = Game.getPlayersList().get(player);
		String countryName = generateRandomCountry(p.getOwnedCountries());
		System.out.println(countryName);
//		int num = generateRandomArmyCount(p.getPlayerNumOfArmy());				
		
//		p.setPlayerNumOfArmy(p.getPlayerNumOfArmy() - num);		
//		Country.getListOfCountries().get(countryName).setNumberOfArmies(Country.getListOfCountries().get(countryName).getNumberOfArmies()+num);		
	}

	private String generateRandomCountry(ArrayList<String> countryList) {		
		Random randomCountry = new Random(); 
        return countryList.get(randomCountry.nextInt(countryList.size()));			
	}

	private int generateRandomArmyCount(int i) {
		
		return i;		
	}

	
}


class CheaterReinforce implements ReinforceType{

	@Override
	public void reinforce(int player) {
		// TODO Auto-generated method stub
		
	}


}

