package com.riskGame.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.riskGame.models.Continent;
import com.riskGame.models.Game;
import com.riskGame.models.Map;
import com.riskGame.models.Country;

/**
 * This class contains the business logic  to read Map file.
 * @author SaiChitta
 * 
 */
public class MapFileParser {

	public static final String MAP_FILE_NAME = "maps/World.map";
	String fileData;
	MapFileEdit fileEdit;

	public MapFileParser() {
		fileEdit = new MapFileEdit();
	}

	/**
	 * getter method for file data
	 * @return data of file
	 */
	public String getFileData() {
		return fileData;	
	}

	/**
	 * setter method for file data
	 * @param fileData data of file
	 */
	public void setFileData(String fileData) {
		this.fileData = fileData;
	}
	
	/**
	 * This method reads the map file data and create Map object.  	
	 * @return map - Map object containing territories and continents.
	 * 
	 */
	public Map readFileData(String fileName){
		if(!validateValidMapFile(fileName)) {
			return null;
		}
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
			    		currentCountry.setContinent(territoryContinent);
			    		Continent continentToUpdate = continents.get(territoryContinent);
			    		continentToUpdate.pushTerritory(currentCountry);
			    		// Add country to static map in Country
			    		Country.getListOfCountries().put(currentCountry.getCountryName(), currentCountry);
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
	 * This method validates if the given filename is a valid map file.
	 * @param fileName
	 * @return true if the file is a valid map file.
	 */
	public boolean validateValidMapFile(String fileName) {		
		boolean validResult = false;
		int findCount = 0;
		try {
			BufferedReader readMap = new BufferedReader(new FileReader(fileName));
			while(readMap.ready()) {
				String line = readMap.readLine().trim();								
				if(line.isEmpty()) {
					continue;
				}
				if(line.equals("[Continents]")) {
					findCount++;
				}
				if(line.equals("[Territories]")) {
					findCount++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(findCount == 2) {
			validResult = true;
		}
		return validResult;
	}

	/**
	 * This method converts territoryLine into a list of territory instances.
	 * @param territoryLine territory line from map file. 
	 * @return territories List of territories.
	 * 
	 */
	private HashMap<String,Country> convertToTerritories(String[] territoryLine) {
		HashMap<String,Country> countries = new HashMap<String, Country>();		
		for(int territoryLineIndex = 4;territoryLineIndex < territoryLine.length;territoryLineIndex++) {
			Country currentTerritory = new Country();
			currentTerritory.setCountryName(territoryLine[territoryLineIndex]);			
			countries.put(currentTerritory.getCountryName(), currentTerritory);
		}		
		return countries;		
	}


	/**
	 * This method saves the map object to the filename.
	 * @param commandInput - command fro the user.
	 * @return int value for the correct message.
	 *
	 */
	public int saveMap(String[] commandInput) {
		if(commandInput.length != 2) {
			return BaseMapFile.SAVE_MAP_COMMAND_ERROR;
		}

		if(!fileEdit.validateMap()) {
			MapFileEdit.printMapStatusMessage(false);
			return BaseMapFile.SAVE_MAP_NO_CONTINENTS;
		}

		if(Game.getEditMap().getContinents().isEmpty()) {
			return BaseMapFile.SAVE_MAP_NO_CONTINENTS;
		}

		String fileName = BaseMapFile.MAP_FILE_DIR + commandInput[1];
		HashMap<String, Continent> continents = Game.getEditMap().getContinents();
		String fileContent;

		try {
			File file = new File(fileName);
			if(!file.exists()) {
				file.createNewFile();
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			fileContent = getSaveMapFileContent();
			if(fileContent.length() < 1) {
				// error in creating the file content
				writer.close();
				return BaseMapFile.SAVE_MAP_INVALID;
			}
			writer.write(fileContent);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Game.setEditMapSet(false); // last statement in this method
		return BaseMapFile.SAVE_MAP_SUCCESS;
	}



	/**
	 * This method processes the edit Map object and convert to string to save to file.
	 * @return fileContent .map file content.
	 *
	 */
	public String getSaveMapFileContent() {
		String fileContent = "[Continents]\n";
		for(String printContinentKey : Game.getEditMap().getContinents().keySet()) {
			Continent continent = Game.getEditMap().getContinents().get(printContinentKey);
			fileContent += continent.getContinentName() + "=" + continent.getControlValue() + "\n";
		}
		fileContent += "\n";
		fileContent += "[Territories]\n";

		for(String printTerritoryContinentKey : Game.getEditMap().getContinents().keySet()) {
			Continent printContinent = Game.getEditMap().getContinents().get(printTerritoryContinentKey);

			for(Country printCountry : printContinent.getTerritories()) {
				String countryName = printCountry.getCountryName();
				String neighborCSV = getNeighborCSV(printCountry.getNeighbours());
				fileContent += countryName + ",0,0," +  printContinent.getContinentName() + "," + neighborCSV + "\n";
			}
			fileContent += "\n";
		}
		return fileContent;
	}

	/**
	 * Getter method to get a Comma Separated String of neighbor list.
	 * @return CSV list.
	 *
	 */
	public String getNeighborCSV(HashMap<String,Country> neighbours) {
		String neighborCSV = "";
		for(String neighbor : neighbours.keySet()) {
			neighborCSV += neighbor + ",";
		}
		neighborCSV = neighborCSV.replaceAll(",$", "");
		return neighborCSV;
	}

}
