package com.riskTest.Strategies;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.riskGame.Strategies.RandomPlayer;
import com.riskGame.models.Player;

public class ReinforceTypeTest {
	Player myPlayer;
	
	@Before
	public void before() {
		myPlayer = new RandomPlayer();
		ArrayList<String> ownedCountries = new ArrayList();
		String[] countries = {"Iran", "Japan", "canada","Germany", "France", "Asia", "Siberia", "China", "Afghanistan", "Ukraine", "Yatusk", "Kamchatka", "Mongolia", "Egypt", "Indonesia"};
		for(String countryName: countries) {
			ownedCountries.add(countryName);
		}
		myPlayer.setOwnedCountries(ownedCountries);
	}
	
	@Test
	public void testGenerateReinforcementArmy() {		
		myPlayer.reinforce(1,"");
//		assertEquals(5,reinforcement.calculateReinforcementArmies(myPlayer));	
	}
	
}
