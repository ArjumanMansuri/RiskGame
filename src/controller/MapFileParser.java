package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import models.Continent;
import models.Map;
import models.Country;

/**
 * Controller to read Map file.
 * @author SaiChitta
 * 
 */
public class MapFileParser {
	
	public static final String MAP_FILE_NAME = "maps/World.map";
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
	public Map readFileData(String fileName){		
		Map map = new Map();
		HashMap<String, Continent> continents = new HashMap<String, Continent>();		 		
		try {
			boolean territoryLineFound = false,continentsLineFound = false;
			BufferedReader readMap = new BufferedReader(new FileReader(fileName));			
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
			    		Country currentCountry = new Country(); // create new territory for the continent 
			    		currentCountry.setCountryName(territoryLine[0]); 
			    		currentCountry.setNeighbours(convertToTerritories(territoryLine));			    		
			    		
			    		Continent continentToUpdate = continents.get(territoryContinent);
			    		continentToUpdate.pushTerritory(currentCountry);
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
	private ArrayList<Country> convertToTerritories(String[] territoryLine) {
		ArrayList<Country> countries = new ArrayList<Country>();		
		for(int territoryLineIndex = 4;territoryLineIndex < territoryLine.length;territoryLineIndex++) {
			Country currentTerritory = new Country();
			currentTerritory.setCountryName(territoryLine[territoryLineIndex]);			
			countries.add(currentTerritory);
		}		
		return countries;		
	}

}
