package com.riskTest.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import com.riskGame.controller.AttackPhase;
import com.riskGame.controller.FortificationPhase;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Player;

/**
 * {@link FortificationPhase} Test Class
 */
public class FortificationTest {
	FortificationPhase fp = new FortificationPhase();
	static Player p1 = new Player();
	
	/**
	 * This method is called once before the test methods for AttackTest class to setup the context
	 */
	@BeforeClass
	public static void beforeClass() {
		
		HashMap<String, Country> countriesMap = new HashMap<String, Country>();
		ArrayList<String> p1OwnedCountries = new ArrayList<String>();
		String[] countries = {"Iran", "Japan", "canada","Germany", "France", "Siberia"};


		for(String countryName: countries) {
			Country newCountry = new Country();
			newCountry.setCountryName(countryName);
			newCountry.setNumberOfArmies(7);
			countriesMap.put(countryName, newCountry);
			p1OwnedCountries.add(countryName);
		}
		
		Country.setListOfCountries(countriesMap);
		HashMap<String, Country> p1Neighbours = new HashMap<String, Country>();
		p1Neighbours.put("Japan", countriesMap.get("Japan"));
		Country.getListOfCountries().get("Iran").setNeighbours(p1Neighbours);
		
		AttackPhase.setAttackerCountry("Iran");
		p1.setOwnedCountries(p1OwnedCountries);
		HashMap<Integer, Player> playerList = new HashMap<Integer, Player>();
		playerList.put(1, p1);
		Game.setPlayersList(playerList);
	}
	
	/**
	 * This method is to test if countries exist or not
	 */
	@Test
	public void testdoCountriesExist() {
		ArrayList<String> country = new ArrayList<String>();
		country.add("India");
		country.add("China");
		country.add("Iran");
		assertEquals(true,fp.doCountriesExist(country,"India","Iran"));
	}

	@Test
	public void testAreArmiesSufficiantToMove(){

		assertEquals(true, fp.areArmiesSufficientToMove("Japan",4));
	}

	@Test
	public void testAreCountriesNotOwnedByPlayer(){
		ArrayList<String> country = new ArrayList<String>();
		country.add("India");
		country.add("China");
		country.add("Iran");
		assertEquals(false,fp.areCountriesNotOwnedByPlayer(country,"India","China"));
	}

	@Test
	public void testAreCountriesAdjacent(){
		assertEquals(false, fp.areCountriesAdjacent("Japan","Iran"));
	}
}
