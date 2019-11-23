package com.riskTest.Strategies;

import com.riskGame.Strategies.CheaterPlayer;
import com.riskGame.Strategies.RandomPlayer;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FortifyTypeTest {
	Player randomPlayer,cheaterPlayer;
	
	@Before
	public void before() {
		HashMap playersData = new HashMap<Integer,Player>();
		HashMap<String, Country> countriesMap = new HashMap<String, Country>();
		HashMap<String, Country> neighborCountryMap = new HashMap<String, Country>();

		/* Create a new Random player and add to playerdata  */
		randomPlayer = new RandomPlayer();
		cheaterPlayer = new CheaterPlayer();

		ArrayList<String> ownedCountries = new ArrayList();

		String[] countries = {"China", "Ural", "Afghanistan","India"};
		String[] neighborCountries = {"Ukraine","Afghanistan","India","Southern Europe","Egypt","East Africa","Siberia","China","Ural","Middle East","Siam","Mongolia","Yatusk","Irkutsk","Japan","Kamchatka"};
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

		for(String countryName: neighborCountries) {
			Country newCountry = new Country();
			newCountry.setCountryName(countryName);
			newCountry.setNumberOfArmies(7);
			newCountry.setOwner(1);
			neighborCountryMap.put(countryName, newCountry);
		}

		for(String countryName: countries) {
			HashMap<String, Country> neighbors = new HashMap<String, Country>();
			switch(countryName){
				case "China":
					neighbors.put("Siam",neighborCountryMap.get("Siam"));
					neighbors.put("Ural",neighborCountryMap.get("Ural"));
					neighbors.put("Siberia",neighborCountryMap.get("Siberia"));
					neighbors.put("Mongolia",neighborCountryMap.get("Mongolia"));
					neighbors.put("India",neighborCountryMap.get("India"));
					neighbors.put("Afghanistan",neighborCountryMap.get("Afghanistan"));
					break;
				case "Ural":
					neighbors.put("Siberia",neighborCountryMap.get("Siberia"));
					neighbors.put("China",neighborCountryMap.get("China"));
					neighbors.put("Afghanistan",neighborCountryMap.get("Afghanistan"));
					neighbors.put("Ukraine",neighborCountryMap.get("Ukraine"));
					break;
				case "Afghanistan":
					neighbors.put("Ural",neighborCountryMap.get("Ural"));
					neighbors.put("China",neighborCountryMap.get("China"));
					neighbors.put("India",neighborCountryMap.get("India"));
					neighbors.put("Ukraine",neighborCountryMap.get("Ukraine"));
					break;
				case "India":
					neighbors.put("China",neighborCountryMap.get("China"));
					neighbors.put("Afghanistan",neighborCountryMap.get("Afghanistan"));
					neighbors.put("Siam",neighborCountryMap.get("Siam"));
					break;
			}
			countriesMap.get(countryName).setNeighbours(neighbors);
		}

		ArrayList<String> ownedCountries2 = new ArrayList();
		String[] countries2 = {"Japan", "Yatusk", "Kamchatka"};
		for(String countryName: countries2) {
			Country newCountry = new Country();
			newCountry.setCountryName(countryName);
			newCountry.setNumberOfArmies(7);
			newCountry.setOwner(2);
			if(countryName == "Kamchatka"){
				newCountry.setOwner(1);
			}
			countriesMap.put(countryName, newCountry);
			ownedCountries2.add(countryName);
		}
		cheaterPlayer.setOwnedCountries(ownedCountries2);

		for(String countryName: countries2) {
			HashMap<String, Country> neighbors = new HashMap<String, Country>();
			switch(countryName){
				case "Japan":
					neighbors.put("Yatusk",neighborCountryMap.get("Yatusk"));
					neighbors.put("Kamchatka",neighborCountryMap.get("Kamchatka"));
					break;
				case "Yatusk":
					neighbors.put("Japan",neighborCountryMap.get("Japan"));
					neighbors.put("Kamchatka",neighborCountryMap.get("Kamchatka"));
					break;
				case "Kamchatka":
					neighbors.put("Yatusk",neighborCountryMap.get("Yatusk"));
					neighbors.put("Japan",neighborCountryMap.get("Japan"));
					neighbors.put("Egypt",neighborCountryMap.get("Egypt"));
					break;
			}
			countriesMap.get(countryName).setNeighbours(neighbors);
		}

		// set list of countries
		Country.setListOfCountries(countriesMap);

		// add Players
		playersData.put(1, randomPlayer);
		playersData.put(2, cheaterPlayer);
		Game.setPlayersList(playersData);
	}
	
	@Test
	public void testRandomFortifyType() {
		assertEquals("true", randomPlayer.getFortifyType().fortify(1, ""));
	}

	@Test
	public void testCheaterFortifyType() {
		assertEquals("true",cheaterPlayer.getFortifyType().fortify(2, ""));
	}

}
