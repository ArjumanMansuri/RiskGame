package com.riskTest.controller;

import org.junit.Test;

import com.riskGame.controller.MapFileEdit;
import com.riskGame.controller.MapFileParser;
import com.riskGame.controller.ReinforcementPhase;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;

/**
 * {@link MapFileEdit} Test Class
 */
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
	
	/**
	 * This method is to test if no continent is unconnected.
	 */
	@Test
	public void testMapConnected() {
		assertTrue(mapEdit.validateContinentConnections());
	}
	
	/**
	 * This method is to test if no countries is unconnected.
	 */
	@Test
	public void testContinentConnection() {
		assertTrue(mapEdit.validateCountryConnections());
	}
	
	/**
	 * This method is to test the validity of map file which is being read.
	 */
	@Test
	public void testReadValidMapFile() {
		assertTrue(mapParser.validateValidMapFile("maps/World.map"));
	}
	
	/**
	 * This method is to test if a map file is invalid.
	 */
	@Test
	public void testReadInvalidMapFile() {
		assertFalse(mapParser.validateValidMapFile("maps/incorrectMapOne.map"));
	}
	
	/**
	 * This method is to test if there are neighbours with valid countries.
	 */
	@Test
	public void testValidNeighbors() {
		assertTrue(mapEdit.validateNeighbors());
	}
	
	/**
	 * This method is to test a continent is successfully edited.
	 */
	@Test
	public void testEditContinent() {
		int previousContinentCount = Game.getEditMap().getContinents().size();		
		mapEdit.editContinent("editcontinent -add TeamRisk 10".split(" "));
		assertEquals(7, previousContinentCount + 1);
	}
	
	/**
	 * This method is to test if removing a continent is successful.
	 */
	@Test
	public void testEditContinentRemove() {
		int previousContinentCount = Game.getEditMap().getContinents().size();		
		mapEdit.editContinent("editcontinent -remove Australia".split(" "));		
		assertEquals(5, previousContinentCount - 1);
	}
	
	/**
	 * This method is to test if adding a country to continent is successful.
	 */
	@Test
	public void testEditCountryAdd() {
		int australiaCountryCount = Game.getEditMap().getContinents().get("Australia").getTerritories().size();
		mapEdit.editCountry("editcountry -add TeamRisk Australia".split(" "));
		int australiaCountryCountAdd = Game.getEditMap().getContinents().get("Australia").getTerritories().size();
		assertEquals(australiaCountryCount + 1, australiaCountryCountAdd);
	}
	
	/**
	 * This method is to test if removing a country is successful.
	 */
	@Test
	public void testEditCountryRemove() {
		mapEdit.editCountry("editcountry -remove Alaska".split(" "));
		Country alaskaCountry = mapEdit.isCountryExists("Alaska");
		assertNull(alaskaCountry);
	}
	
	/**
	 * This method is to test adding a neighbor is successful.
	 */
	@Test
	public void testAddNeighbor() {
		mapEdit.editCountry("editcountry -add TeamRisk Australia".split(" "));
		mapEdit.editNeighbor("editneighbor -add Alaska TeamRisk".split(" "));				
		System.out.println(mapEdit.showMap());				
	}
}
