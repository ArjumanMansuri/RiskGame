package com.riskTest.controller;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.*;
import com.riskGame.controller.*;
import com.riskGame.models.*;

public class CardTest {
	
	Player myPlayer;
	@Before
	public void before() {
		myPlayer = new Player();
	}
	
	@Test
	public void testCardType() {
		Card card1 = new Card(Card.ARTILLARY);
		assertEquals(Card.ARTILLARY,card1.getType());	
	}
	
	@Test
	public void testCardOwner() {
		Card card1 = new Card(Card.ARTILLARY);
		card1.changeOwner(myPlayer);		
		assertEquals(card1.getOwner(), myPlayer);			
	}
	
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
	
}
