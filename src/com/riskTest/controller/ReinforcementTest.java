package com.riskTest.controller;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
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
	Player myPlayer,anotherPlayer;

	
	/**
	 * This method is called before every test method of ReinforcementTest class.
	 */
	@Before
	public void before() {
   		myPlayer = new Player();
		ArrayList<String> ownedCountries = new ArrayList();
		String[] countries = {"Iran", "Japan", "canada","Germany", "France", "Asia", "Siberia", "China", "Afghanistan", "Ukraine", "Yatusk", "Kamchatka", "Mongolia", "Egypt", "Indonesia"};
		for(String countryName: countries) {
			ownedCountries.add(countryName);
		}
		myPlayer.setOwnedCountries(ownedCountries);
		myPlayer.setPlayerName("Abc");
		myPlayer.setPlayerNumOfArmy(10);
		HashMap<Integer,Player> playerHashMap = new HashMap<Integer, Player>();
		playerHashMap.put(1,myPlayer);
		Game.setPlayersList(playerHashMap);

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

	@Test
	public void testprocessReinforceCmd(){
		reinforcement = new ReinforcementPhase();
		assertEquals("",reinforcement.processReinforceCmd(1, new String[]{"showmap", "Iran", "4"}));
	}

	@Test
	public void testProcessExchangeCardCmdValidation(){
		reinforcement = new ReinforcementPhase();
		assertEquals("No card exchange was made",reinforcement.processExchangeCardCmd(1,new String[]{"Exchange Card","none"}));
	}
}
