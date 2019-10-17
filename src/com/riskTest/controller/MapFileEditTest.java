package com.riskTest.controller;

import org.junit.Test;

import com.riskGame.controller.MapFileEdit;
import com.riskGame.controller.MapFileParser;
import com.riskGame.models.Game;
import com.riskGame.models.Map;

import static org.junit.Assert.assertEquals;
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
		assertTrue(mapEdit.validateMap());
	}
	
	@Test
	public void testContinentConnection() {
		assertTrue(mapEdit.validateContinentConnections());
	}
	
	@Test
	public void testReadValidMapFile() {
		
	}
}
