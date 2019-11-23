package com.riskTest.Strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import com.riskGame.Strategies.CheaterPlayer;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import org.junit.Before;
import org.junit.Test;

import com.riskGame.Strategies.RandomPlayer;
import com.riskGame.models.Player;

public class ReinforceTypeTest {
	Player randomPlayer,cheaterPlayer;
	
	@Before
	public void before() {
		HashMap playersData = new HashMap<Integer,Player>();
		HashMap<String, Country> countriesMap = new HashMap<String, Country>();

		/* Create a new Random player and add to playerdata  */
		randomPlayer = new RandomPlayer();
		cheaterPlayer = new CheaterPlayer();

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

		ArrayList<String> ownedCountries2 = new ArrayList();
		String[] countries2 = {"Japan", "Afghanistan", "Ukraine", "Yatusk", "Kamchatka", "Mongolia"};
		for(String countryName: countries2) {
			Country newCountry = new Country();
			newCountry.setCountryName(countryName);
			newCountry.setNumberOfArmies(7);
			newCountry.setOwner(2);
			countriesMap.put(countryName, newCountry);
			ownedCountries2.add(countryName);
		}
		cheaterPlayer.setOwnedCountries(ownedCountries2);

		// set list of countries
		Country.setListOfCountries(countriesMap);

		// add Players
		playersData.put(1, randomPlayer);
		playersData.put(2, cheaterPlayer);
		Game.setPlayersList(playersData);
	}
	
	@Test
	public void testRandomReinforceType() {
		assertTrue(randomPlayer.reinforceType.reinforce(1));
	}

	@Test
	public void testCheaterReinforceType() {
		assertTrue(cheaterPlayer.reinforceType.reinforce(2));
	}

}
