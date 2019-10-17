package com.riskTest.controller;

import org.junit.Test;

import com.riskGame.controller.MapFileEdit;
import com.riskGame.controller.MapFileParser;
import com.riskGame.models.Game;
import com.riskGame.models.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;

public class MapFileEditTest {	
	MapFileParser mapParser;
	MapFileEdit mapEdit;
	
	@Before
	public void beforeTestMap() {
		mapParser = new MapFileParser();
		mapEdit = new MapFileEdit();
		Map map = mapParser.readFileData("maps/World.map");
		Game.setEditMap(map);		
	}
		
	@Test
	public void testMapConnected() {
		assertTrue(mapEdit.validateContinentConnections());
	}
	
	@Test
	public void testContinentConnection() {
		assertTrue(mapEdit.validateCountryConnections());
	}
	
	@Test
	public void testReadValidMapFile() {
		assertTrue(mapParser.validateValidMapFile("maps/World.map"));
	}
	
	@Test
	public void testReadInvalidMapFile() {
		assertFalse(mapParser.validateValidMapFile("maps/incorrectMapOne.map"));
	}
	
	@Test
	public void testValidNeighbors() {
		assertTrue(mapEdit.validateNeighbors());
	}
	
	@Test
	public void testEditContinent() {
		int previousContinentCount = Game.getEditMap().getContinents().size();		
		mapEdit.editContinent("editcontinent -add TeamRisk 10".split(" "));
		assertEquals(7, previousContinentCount + 1);
	}
	
	
}
