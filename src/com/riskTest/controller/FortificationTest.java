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
	 * This method is to test if fortification is done properly
	 */
	@Test
	public void testFortification() {
		System.out.println(fp.fortify(1, "fortify Iran Japan 3"));
		assertEquals(true,fp.fortify(1, "fortify Iran Japan 3").contains("done"));	
	}
}
