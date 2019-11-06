package com.riskTest.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import com.riskGame.controller.AttackPhase;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Player;

/**
 * {@link AttackPhase} Test Class
 */
public class AttackTest {
	
	AttackPhase ap = new AttackPhase();
	static Player p1 = new Player();
	
	/**
	 * This method is called once before the test methods for AttackTest class to setup the context
	 */
	@BeforeClass
	public static void beforeClass() {
		
		HashMap<String, Country> countriesMap = new HashMap<String, Country>();
		ArrayList<String> p1OwnedCountries = new ArrayList<String>();
		String[] countries = {"Iran", "Japan", "canada","Germany", "France", "Siberia", "China", "Afghanistan", "Ukraine", "Yatusk", "Kamchatka", "Mongolia", "Egypt", "Indonesia"};
		for(String countryName: countries) {
			Country newCountry = new Country();
			newCountry.setCountryName(countryName);
			newCountry.setNumberOfArmies(7);
			countriesMap.put(countryName, newCountry);
			p1OwnedCountries.add(countryName);
		}
		Country newCountry = new Country();
		newCountry.setCountryName("Sri Lanka");
		newCountry.setNumberOfArmies(7);
		countriesMap.put("Sri Lanka", newCountry);
		
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
	 * This method is to test if attack is possible by the attacker country
	 */
	@Test
	public void testIsAttackPossible() {
		assertEquals(true,ap.isAttackPossible());	
	}
	
	/**
	 * This method is to test if a player has won the game
	 */
	@Test
	public void testHasPlayerWon() {
		assertEquals(false,ap.hasPlayerWon(1));	
	}
	
	/**
	 * This method is to test if roll of a dice is valid
	 */
	@Test
	public void testRollDice() {
		assertEquals(true,1<=ap.rollDice() && ap.rollDice()<=6);	
	}
	
	/**
	 * This method is to test if the countries provided as parameters to the doCountriesExist method exist
	 */
	@Test
	public void testDoCountriesExist() {
		assertEquals(true,ap.doCountriesExist(p1.getOwnedCountries(),"Iran", "Japan"));	
	}
	
	/**
	 * This method is to test if the countries provided as parameters to the areCountriesAdjacent are adjacent
	 */
	@Test
	public void testAreCountriesAdjacent() {
		assertEquals(true,ap.areCountriesAdjacent("Iran", "Japan"));	
	}
	
	/**
	 * This method is to test if the countries provided as parameters to the isCountryNotOwnedByPlayer method are not owned by the player
	 */
	@Test
	public void testIsCountryNotOwnedByPlayer() {
		assertEquals(false,ap.isCountryNotOwnedByPlayer(p1.getOwnedCountries(), "Iran"));	
	}
	
	/**
	 * This method is to test if the country provided as parameter to areArmiesSufficientToAttack method has sufficient armies to attack
	 */
	@Test
	public void testAreArmiesSufficientToAttack() {
		assertEquals(true,ap.areArmiesSufficientToAttack("Iran", 3));	
	}
}
