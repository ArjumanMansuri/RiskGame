package com.riskTest.controller;

import com.riskGame.controller.AttackPhase;
import com.riskGame.controller.TournamentMode;
import com.riskGame.models.Card;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class TournamentTest {
	static Player p1 = new Player();
	TournamentMode tm = new TournamentMode();

	/**
	 * This method is called once before the test methods for CardTest class to setup the context
	 */
	@Before
	public void before() {
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
	 * Test if the arguments are correct or not.
	 */
	@Test
	public void testCheckTournamentCommandArgs() {
		String cmd = "tournament -M Asia.map -P Cheater -G 2 -D 3";
		assertEquals(false, TournamentMode.checkTournamentCommandArgs(cmd));
	}

	@Test
	public void testHadPlayerWon() {
		assertEquals(true, TournamentMode.hasPlayerWon(1));
	}

	@Test
	public void testCheckIfArmiesPlaced() {
		assertEquals(true, tm.checkIfArmiesPlaced());
	}

}
