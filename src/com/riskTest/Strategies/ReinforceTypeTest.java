package com.riskTest.Strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import com.riskGame.models.Country;
import com.riskGame.models.Game;
import org.junit.Before;
import org.junit.Test;

import com.riskGame.Strategies.RandomPlayer;
import com.riskGame.models.Player;

public class ReinforceTypeTest {
	Player randomPlayer;
	
	@Before
	public void before() {
		HashMap playersData = new HashMap<Integer,Player>();
		HashMap<String, Country> countriesMap = new HashMap<String, Country>();

		/* Create a new Random player and add to playerdata  */
		randomPlayer = new RandomPlayer();
		ArrayList<String> ownedCountries = new ArrayList();
		String[] countries = {"Iran", "Japan", "canada","Germany", "France", "Asia", "Siberia", "China"};
		for(String countryName: countries) {
			ownedCountries.add(countryName);
		}
		randomPlayer.setOwnedCountries(ownedCountries);
		randomPlayer.setPlayerName("Player 1");
		randomPlayer.setPlayerNumOfArmy(16);

		/* Create countries and add to Country object */
		for(String countryName: countries) {
			Country newCountry = new Country();
			newCountry.setCountryName(countryName);
			newCountry.setNumberOfArmies(7);
			newCountry.setOwner(1);
			countriesMap.put(countryName, newCountry);
		}
		Country.setListOfCountries(countriesMap);

		playersData.put(1,randomPlayer);
		Game.setPlayersList(playersData);
	}
	
	@Test
	public void testRandomReinforceType() {
		assertTrue(randomPlayer.reinforceType.reinforce(1));
	}
	
}
