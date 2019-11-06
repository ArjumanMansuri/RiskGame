package com.riskTest.controller;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.*;
import com.riskGame.controller.*;
import com.riskGame.models.*;

public class CardTest {
	
	Player myPlayer;
	/**
	 * This method is called once before the test methods for CardTest class to setup the context
	 */
	@Before
	public void before() {
		myPlayer = new Player();
	}
	
	/**
	 * Test if the card type is correctly set. 
	 */
	@Test
	public void testCardType() {
		Card card1 = new Card(Card.ARTILLARY);
		assertEquals(Card.ARTILLARY,card1.getType());	
	}
	
	/**
	 * Test if the card owner is correctly set.
	 */
	@Test
	public void testCardOwner() {
		Card card1 = new Card(Card.ARTILLARY);
		card1.changeOwner(myPlayer);		
		assertEquals(card1.getOwner(), myPlayer);			
	}
	
	/**
	 * Test the number of cards owned by the player object.
	 */
	@Test
	public void testCardPlayerNums() {
		Card card1 = new Card(Card.ARTILLARY);
		Card card2 = new Card(Card.ARTILLARY);
		Card card3 = new Card(Card.ARTILLARY);
		card1.changeOwner(myPlayer);		
		card2.changeOwner(myPlayer);		
		card3.changeOwner(myPlayer);		
		assertEquals(3, myPlayer.getCards().size());			
	}
	
	/**
	 * Test if the card gets transferred
	 */
	@Test
	public void testCardTransfer() {
		Player myPlayer2 = new Player();
		myPlayer2.setPlayerName("Player2");
		
		Card card1 = new Card(Card.ARTILLARY);
		
		card1.changeOwner(myPlayer);		
		card1.changeOwner(myPlayer2); // change the owner of the card 
		
		assertEquals("Player2", myPlayer2.getPlayerName());			
	}
	
}
