package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import models.Continent;
import models.Map;
import models.Territory;

/**
 * Controller to read Map file.
 * @author SaiChitta
 * 
 */
public class MapFileParser {
	
	public static final String MAP_FILE_NAME = "Default.map";
	String fileData;

	public String getFileData() {
		return fileData;	
	}

	public void setFileData(String fileData) {
		this.fileData = fileData;
	}
	
	/**
	 * Reads the map file data and create Map object.  	
	 * @return map - Map object containing territories and continents
	 */
	public Map readFileData(){
		Map map = new Map();
		HashMap<String, Continent> continents = new HashMap<String, Continent>();
		 		
		try {
			boolean territoryLineFound = false,continentsLineFound = false;
			BufferedReader readMap = new BufferedReader(new FileReader(MAP_FILE_NAME));			
			while(readMap.ready()) {
				String line = readMap.readLine().trim();
				if(line.isEmpty()) {
					continue;
				}
				if(line.equals("[Continents]")) {
			    	continentsLineFound = true;
			    	territoryLineFound = false;
			    	continue;
			    }
				if(line.equals("[Territories]")) {
			    	territoryLineFound = true;
			    	continentsLineFound = false;
			    	continue;
			    }			    
				if(continentsLineFound) {					
			    	String[] continentLine = line.split("=");			    	
			    	Continent continent = new Continent();
			    	continent.setContinentName(continentLine[0]);			    	
			    	continent.setControlValue(Integer.parseInt(continentLine[1]));
			    	continents.put(continentLine[0], continent);
				}
			    if(territoryLineFound) {			    	
			    	String[] territoryLine = line.split(",");
			    	
			    	/**
			    	 *   Check 'if' the territory line data has a valid continent name value
			    	 */
			    	String territoryContinent = territoryLine[3];
			    	if(continents.containsKey(territoryContinent) == false) { 
			    		// Continent doesn't exist - throw invalid map error		    		
			    	}else {
			    		Territory currentTerritory = new Territory(); // create new territory for the continent 
			    		currentTerritory.setTerritoryName(territoryLine[0]); 
			    		currentTerritory.setNeighbours(convertToTerritories(territoryLine));			    		
			    		
			    		Continent continentToUpdate = continents.get(territoryContinent);
			    		continentToUpdate.pushTerritory(currentTerritory);
			    	}
			    }			   
			}	
			
			readMap.close();
			map.setContinents(continents);
			if(!map.getContinents().isEmpty()) {
				return map;	
			}			
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Convert territoryLine into a list of territory instances
	 * @param territoryLine territory line from map file. 
	 * @return territories List of territories.
	 */
	private ArrayList<Territory> convertToTerritories(String[] territoryLine) {
		ArrayList<Territory> territories = new ArrayList<Territory>();		
		for(int territoryLineIndex = 4;territoryLineIndex < territoryLine.length;territoryLineIndex++) {
			Territory currentTerritory = new Territory();
			currentTerritory.setTerritoryName(territoryLine[territoryLineIndex]);			
			territories.add(currentTerritory);
		}		
		return territories;		
	}	
}
