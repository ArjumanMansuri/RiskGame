package com.riskGame.controller;

import java.util.ArrayList;
import java.util.HashMap;

import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.observer.FortificationPhaseObserver;
import com.riskGame.observer.PhaseViewObserver;
import com.riskGame.observer.PhaseViewPublisher;

/**
 * 
 * @author Arjuman Mansuri
 * This class has business logic of the fortification phase
 *
 */
public class FortificationPhase implements PhaseViewPublisher {

	private PhaseViewObserver newObserver;

	/**
	 * Default Constructor which will create new observer object
	 */
	public FortificationPhase() {
		newObserver = new FortificationPhaseObserver();
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
	 * @param ownedCountries List of countries owned by the player
	 * @param fromCountry Name of the country from which player wants to move his army/armies
	 * @param toCountry Name of the country to which player wants to move his army/armies
	 * @return true if the countries are adjacent else false
	 */
	public boolean areCountriesAdjacent(String fromCountry,String toCountry) {
		if(!(Country.getListOfCountries().get(fromCountry).getNeighbours().containsKey(toCountry))){		
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * This method checks if the countries provided as parameters are owned by the player
	 * @param ownedCountries List of countries owned by the player
	 * @param fromCountry Name of the country from which player wants to move his army/armies
	 * @param toCountry Name of the country to which player wants to move his army/armies
	 * @return true if the countries are owned by the player else false
	 */
	public boolean areCountriesNotOwnedByPlayer(ArrayList<String> ownedCountries,String fromCountry,String toCountry) {
		return !ownedCountries.contains(fromCountry) || !ownedCountries.contains(toCountry);
	}

	/**
	 * This method checks if the 'fromCountry' has sufficient armies to move
	 * @param ownedCountries List of countries owned by the player
	 * @param fromCountry Name of the country from which player wants to move his army/armies
	 * @param num number of armies to move
	 * @return true if sufficient armies available else false
	 */
	public boolean areArmiesSufficientToMove(String fromCountry,int num) {
		return Country.getListOfCountries().get(fromCountry).getNumberOfArmies()>num;
	}

	/**
	 * This method is to notify the observer pattern
	 * @param action string to notify the observer 
	 */
	@Override
	public void notifyObserver(String action) {
		this.newObserver.update(action);

	}
}
