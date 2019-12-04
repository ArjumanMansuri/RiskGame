package com.riskTest.controller;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.*;
import com.riskGame.controller.*;
import com.riskGame.models.*;

/**
 * {@link StartUpTest} Test Class
 */
public class StartUpTest {
	StartUpPhase st =  new StartUpPhase();
	HashMap<Integer,Player> temp =  new HashMap<Integer, Player>();
	Player playerTest = new Player();

	/**
	 * This method is to check the name of Player is correct
	 */
	@Test
	public void testIfContains(){
		playerTest.setPlayerName("Abc");
		temp.put(1,playerTest);

		assertEquals(true, st.ifContains(temp,"Abc"));
	}

	/**
	 * This method is to check if the input command works correctly.
	 */
	@Test
	public void testInputValidator(){
		assertEquals(1, st.inputValidator("gameplayer -add a human -add b cheater"));
	}
	/**
	 * This method tests if the player gets the armies assigned correctly.
	 */
	@Test
	public void testAllPlayerArmies(){
		playerTest.setPlayerName("abc");
		Player p2 = new Player();
		p2.setPlayerName("def");
		temp.put(1,playerTest);
		temp.put(2,p2);

		assertEquals("",st.allPlayerArmies());
	}

	/**
	 * This method is to test the correct working of the parser.
	 */
	@Test
	public void testParser(){
		assertEquals("error",st.parser(""));
	}
	/**
	 * This method is to test the number of players passed in the parser.
	 */
	@Test
	public void testNumberOfPlayers() {
		st.setNoOfPlayers(6);
		assertEquals("done", st.parser("5"));
	}

	@Test
	public void testIsNumberOfArgumentsCorrect(){
		st.isNumberOfArgumentsCorrect(new String[]{"gameplayer -add a human -remove n"});
	}
}
