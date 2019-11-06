package com.riskTest.controller;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.*;
import com.riskGame.controller.*;
import com.riskGame.models.*;

public class CardTest {
	
	@Before
	public void before() {
		
	}
	
	@Test
	public void testGenerateReinforcementArmy() {
		Card card1 = new Card(Card.ARTILLARY);
		assertEquals(Card.ARTILLARY,card1.getType());	
	}
}
