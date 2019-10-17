package com.riskTest.controller;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.*;

import com.riskGame.controller.*;
import com.riskGame.models.*;


/**
 * {@link ReinforcementPhase} Test Class
 */
public class ReinforcementTest {

	ReinforcementPhase reinforcement;
	Continent myContinent;
	Player myPlayer;
	
	/**
	 * This method iscalled before every test method of ReinforcementTest class.
	 */
	@Before
	public void before() {
		
	myPlayer = new Player();
	HashMap<String, Country> ownedCountries = new HashMap<String, Country>();
	String[] countries = {"Iran", "Japan", "canada","Germany", "France", "Asia", "Siberia", "China", "Afghanistan", "Ukraine", "Yatusk", "Kamchatka", "Mongolia", "Egypt", "Indonesia"};
	for(String countryName: countries) {
		Country newCountry = new Country();
		newCountry.setCountryName(countryName);
		ownedCountries.put(countryName, newCountry);
	}
	myPlayer.setOwnedCountries(ownedCountries);
		
	}
	
	/**
	 * This method is to test number of reinforcement armies of Player.
	 * Calculation : reinforcement armies = Number of territory owned by Player / 3.
	 * e.g Number of Territory owned by Player : 15
	 * 	   Reinforcement Armies  = 15/3 = 5 new armies
	 */
	@Test
	public void testGenerateReinforcementArmy() {
		
		reinforcement = new ReinforcementPhase();
		assertEquals(5,reinforcement.calculateReinforcementArmies(myPlayer));	
	}
}
