package com.riskGame.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import com.riskGame.models.Continent;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Map;

/**
 * This class contains the business logic to edit the map file.
 * @author SaiChitta
 *
 */
public class MapFileEdit {

	public static final String MAP_FILE_DIR = "maps/";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";	
	public static final String ANSI_GREEN = "\u001B[32m";
	private static final int SAVE_MAP_COMMAND_ERROR = 0;
	private static final int SAVE_MAP_NO_CONTINENTS = 1;
	private static final int SAVE_MAP_INVALID = 2;
	private static final int SAVE_MAP_SUCCESS = 3;
	private String editMapFileName;
	
	/**
	 * Check if the given map file exists.
	 * @param command - command to edit map file.
	 * 
	 */
	public String fileExists(String command) {		
		if(command.length() < 1) {
			return "error";
		}		
		String[] commandInput = command.split(" ");
		
		if(commandInput.length > 2 || !commandInput[0].equals("editmap")) {
			return "error";
		}
		else {
			File mapFileCheck = new File(MAP_FILE_DIR + commandInput[1]);
			if(mapFileCheck.exists()) {
				setEditMapFileName(MAP_FILE_DIR + commandInput[1] );
				return "exists";
			}else {
				// create Map file with the name
				File file = new File(MAP_FILE_DIR + commandInput[1]);
				try {
					if(file.createNewFile()) {	
						setEditMapFileName(MAP_FILE_DIR + commandInput[1]);
						return commandInput[1];
					}
				} catch (IOException e) {
					return "error" + e.getMessage();
				}				
			}
			return "error";
		}
	}

	/**
	 * Validate the command and parse.
	 * @param command - command input from the user.
	 * @param fileName - name of the map file.
	 * @return error if incorrect else success if correct.
	 * 
	 */
	public String commandParser(String command, String fileNameInput, boolean mapFileExists) {		
		String[] commands = {"editcontinent", "editcountry", "editneighbor"};
		String[] commandsNonArgs = {"showmap", "validatemap", "savemap"};
		Map editMap = Game.getEditMap();
		String[] commandInput = command.split(" ");
		String commandResult = "error";
		
		// instantiate a map object
		if(!Game.isEditMapSet()) {		 
			String fileName = fileNameInput.split(" ")[1];			
			if(mapFileExists) {
				MapFileParser mapParser = new MapFileParser();
				editMap = mapParser.readFileData(MAP_FILE_DIR + fileName);
				Game.setEditMapSet(true);
				Game.setEditMap(editMap);
			}			
		}
		
		// Check if the entered command is a valid command. 
		if(Arrays.asList(commands).contains(commandInput[0].trim().toLowerCase())) {
			if(checkArguments(commandInput)) {
				// valid arguments exist process the commnd
				switch(commandInput[0]) {
					case "editcontinent":
						editContinent(commandInput);
						break;
					case "editcountry":
						editCountry(commandInput);
						break;
					case "editneighbor":						
						editNeighbor(commandInput);
						break;					
				}
			}			
		}else if(Arrays.asList(commandsNonArgs).contains(commandInput[0].trim().toLowerCase())) {
			switch(commandInput[0]) {
				case "validatemap":					
					if(!validateMap()) {
						printMapStatusMessage(false);												
					}else {
						printMapStatusMessage(true);
					}
					break;
				case "showmap":
					String showContent = showMap();
					if(showContent.isEmpty()) {
						printMapStatusMessage(false);
					}else {
						System.out.println(showContent);
					}
					break;
				case "savemap":
					int saveMap = saveMap(commandInput);
					switch(saveMap) {
						case SAVE_MAP_COMMAND_ERROR:
							System.out.println("There is an error the save map command. Please try again!");
							break;
						case SAVE_MAP_NO_CONTINENTS:
							printMapStatusMessage(false);
							break;
						case SAVE_MAP_INVALID:
							printMapStatusMessage(false);
							break;
						case SAVE_MAP_SUCCESS:
							commandResult = "saved";
							break;					
					}
					break;
			}
		}		
		return commandResult;		
	}
	
	/**
	 * Outputs to console the status of the map.
	 * @param status - status of the map - valid/invalid.
	 * 
	 */
	private void printMapStatusMessage(boolean status) {
		if(status) {
			System.out.println(ANSI_GREEN + "The map you've provided is valid!" + ANSI_RESET);
		}else {
			System.out.println(ANSI_RED + "The map you've provided is invalid! Please correct the errors and try again." + ANSI_RESET);			
		}
	}
	
	/**
	 * This method is used to show the contents of the map file.
	 * @return contents from the map file.
	 * 
	 */
	public String showMap() {
		String mapContent = "";
		// Check if the current edit map object has any continents 
		if(Game.getEditMap().getContinents().isEmpty()) {
			return mapContent;
		}				
		mapContent = getSaveMapFileContent();
		return mapContent;
	}

	/**
	 * Edit the neighbor of a country.
	 * @param commandInput - input command from the user.
	 * 
	 */
	public void editNeighbor(String[] commandInput) {
		if(commandInput.length >= 4) {
			String operation = commandInput[1]; // add or remove
			String countryName = commandInput[2]; 
			String neighborCountryName = commandInput[3]; 
			HashMap<String, Continent> editMapContinents= Game.getEditMap().getContinents();
			
			Country neighbourCountry;
			if((neighbourCountry = isCountryExists(neighborCountryName)) != null) {						
				if(operation.equals("-add")) {			
					Country addCountry = new Country();
					addCountry.setCountryName(neighborCountryName);
					
					//  Add neighbor country to main country if the main country exists  
					Country country = isCountryExists(countryName);
					if(country != null) {
						country.getNeighbours().add(addCountry);
						neighbourCountry.getNeighbours().add(country);
					}					
				} else if(operation.equals("-remove")) {
					for(String continentKey : editMapContinents.keySet()) {										
						editMapContinents.get(continentKey).getTerritories().forEach(country -> {						
							country.getNeighbours().removeIf(neighbor -> neighbor.getCountryName().equals(neighborCountryName));						
						}); 	
						
						editMapContinents.get(continentKey).getTerritories().forEach(country -> {
							country.getNeighbours().removeIf(n -> n.getCountryName().equals(countryName));
						});
					}								
				}
			}			
		}
	}
	
	/**
	 * Check if a given country exists in the map.
	 * @param countryName - name of the country to check existence.
	 * @return Country object.
	 * 
	 */
	public Country isCountryExists(String countryName){
		HashMap<String, Continent> editMapContinents= Game.getEditMap().getContinents();
		Country countryFound = null;
		
		for(String continentKey : editMapContinents.keySet()) {
			if(countryFound != null) {
				break;
			}
			Continent currentContinent = editMapContinents.get(continentKey);			
			for(Country currentCountry : currentContinent.getTerritories()) {	
				if(currentCountry.getCountryName().equals(countryName)) {							
					countryFound = currentCountry; 		
					break;
				}
			}			
		}	
		return countryFound;		
	}
	
	/**
	 * This method allows to edit a country - add or remove.
	 * @param commandInput - command from the user.
	 * 
	 */
	public void editCountry(String[] commandInput) {
		if(commandInput.length > 2) {
			String operation = commandInput[1]; // add or remove
			String countryName = commandInput[2]; // country Name value		 
			HashMap<String, Continent> editMapContinents= Game.getEditMap().getContinents();		
	
			if(operation.equals("-add")) {
				if(commandInput.length == 4) {			
					String continentName = commandInput[3]; // continent Name value				
					// check if the continent exists to add the given country 
					if(editMapContinents.containsKey(continentName)) {  
						Country addCountry = new Country();
						addCountry.setContinent(continentName);
						addCountry.setCountryName(countryName);						
						editMapContinents.get(continentName).getTerritories().add(addCountry);						
					}	
				}
			} else if(operation.equals("-remove")) {
				System.out.println("Removing");
				for(String continentKey : editMapContinents.keySet()) {										
					editMapContinents.get(continentKey).getTerritories().removeIf(country -> country.getCountryName().equals(countryName));				
				}
			}		
		}
	}

	/**
	 * This method edits a continent in a map file. Add or remove continent.
	 * @param editMap map object to be edited.
	 * @param commandInput Full command entered by the user.
	 */
	public void editContinent(String[] commandInput) {		
		String operation = commandInput[1]; // add or remove
		String name = commandInput[2]; // continent Name value		
		HashMap<String, Continent> editMapContinents= Game.getEditMap().getContinents();		
		
		if(operation.equals("-add")) {
			int value = Integer.parseInt(commandInput[3]); // continent control value
			Continent addContinent = new Continent();
			addContinent.setContinentName(name);
			addContinent.setControlValue(value);
			if(!editMapContinents.containsKey(name)) {
				editMapContinents.put(name, addContinent);
			}						
		} else if(operation.equals("-remove")){			
			if(editMapContinents.containsKey(name)) {
				editMapContinents.remove(name);
			}
		}			
	}

	/**
	 * This method edits a continent in a map file. Add or remove continent.
	 * @param editMap  - map object to be edited.
	 * @param commandInput - Full command entered by the user.
	 * 
	 */
	private boolean checkArguments(String[] commandInput) {		
		if(Arrays.asList(commandInput).contains("-add") || Arrays.asList(commandInput).contains("-remove")) {
			return true;
		}
		return false;		
	}

	/**
	 * This method validates the loaded map. Run before loading a map/saving a map.
	 * @return true/false.
	 * 
	 */
	public boolean validateMap() {				
		// Check if the current edit map object has any continents 
		if(Game.getEditMap().getContinents().isEmpty()) {
			return false;
		}
		
		boolean validContinents = validateContinentConnections();
		boolean validCountries  = validateCountryConnections();
		boolean validNeighbors  = validateNeighbors();
		
		if(validContinents && validCountries && validNeighbors) {
			return true;
		}
		return false;		
	}
	
	/**
	 * This method validates a country's neighbor's list is synchronized.
	 * @return true/false.
	 * 
	 */
	public boolean validateNeighbors() {
		boolean validNeighbors = true;
		for(String continentKey: Game.getEditMap().getContinents().keySet()) {
			if(!validNeighbors) {
				return false;
			}			
			Continent currentContinent = Game.getEditMap().getContinents().get(continentKey);								
			for(Country currentCountry : currentContinent.getTerritories()) {	
				 if(!checkNeighborsHasCountry(currentCountry, currentCountry.getNeighbours())) {
					 validNeighbors = false;
				 }
			}			
		}		
		return validNeighbors;				
	}
		
	/**
	 *This method checks if the neighbor countries have the given country as their neighbors.
	 * @param country
	 * @param neighbours
	 * @return true/false
	 * 
	 */
	public boolean checkNeighborsHasCountry(Country country, ArrayList<Country> neighbours) {
		int neighbourCount = 0;
		
		// neighborCountry doesn't have neighbors set in the object. 
		for(Country neighborCountry: neighbours) {
			neighborCountry	= isCountryExists(neighborCountry.getCountryName());
			if(neighborCountry == null) {				
				break;
			}
			
			for(Country neighborOfNeighbor : neighborCountry.getNeighbours()) {	
				neighborOfNeighbor	= isCountryExists(neighborOfNeighbor.getCountryName());
				if(neighborOfNeighbor.getCountryName() == country.getCountryName()) {					
					neighbourCount++;
				}
			}		
		}		
		if(neighbourCount == neighbours.size()) {
			return true;
		}
		return false;
	}

	/**
	 * This method validates all the countries in a continent are connected.
	 * @return true - if all countries are connected / 
	 * 				  false if a country is not connected to any country.
	 * 
	 */
	public boolean validateCountryConnections(){
		int continentsConnected = 0;
		for(String continentKey: Game.getEditMap().getContinents().keySet()) {
			Continent currentContinent = Game.getEditMap().getContinents().get(continentKey);
			
			/**
			 * Iterate all the countries in the continent and check if either of them.
			 * doesn't have a neighbor which is from the same continent.
			 * 
			 */
			int countriesConnected = 0;
			for(Country currentCountry : currentContinent.getTerritories()) {				
				if(countrySameContinentNeighbor(currentCountry.getNeighbours(), getCountryNames(currentContinent.getTerritories()))) {
					countriesConnected++;
				}
			}			
			if(countriesConnected == currentContinent.getTerritories().size()) {
				continentsConnected++;
			}
		}		
		if(continentsConnected == Game.getEditMap().getContinents().size()) {
			return true;
		}
		return false;		
	}
	
	/**
	 * This method Checks if neighbors are from the countriesInContinent
	 * @param neighbors
	 * @param countriesInContinent
	 * @return neighbourFromCountryFound true if neighbor is found from same continent.
	 * 
	 */
	private boolean countrySameContinentNeighbor(ArrayList<Country> neighbors, String[] countriesInContinent) {
		boolean neighbourFromCountryFound = false;
		for(Country neighbor : neighbors) {
			if(Arrays.asList(countriesInContinent).contains(neighbor.getCountryName())) {
				neighbourFromCountryFound = true;
				break;
			}
		}
		return neighbourFromCountryFound;
	}	
	
	/**
	 * ter method to get country names from Country array list
	 * @param continentCountries
	 * @return countryNames
	 * 
	 */
	private String[] getCountryNames(List<Country> continentCountries) {
		String[] countryNames = new String[continentCountries.size()];
		int countryIndex = 0;		
		for(Country country : continentCountries) {
			countryNames[countryIndex] = country.getCountryName();			
			countryIndex++;
		}
		return countryNames;
	}

	/**
	 * This method validates if a given continent is connected to at least one other continent in
	 * the graph.
	 * @return true - if all continents are connected, else false.
	 * 
	 */
	public boolean validateContinentConnections() {
		int connectedContinents = 0;
				
		for(String currentContinentKey: Game.getEditMap().getContinents().keySet()) {
			boolean continentConnected = false;
			if(continentConnected) {				
				continue;
			}			
			Continent currentContinent = Game.getEditMap().getContinents().get(currentContinentKey);			
			for(Country currentContinentCountry : currentContinent.getTerritories()) {			
				if(isCountryExistInOtherContinents(currentContinentKey, currentContinentCountry)) {					
					continentConnected = true;
					connectedContinents++;
					break;
				}
			}
		}		
		if(connectedContinents != Game.getEditMap().getContinents().size()) {
			return false;
		}
		return true;
	}

	/**
	 * this method checks if the given checkCountry exists in all other continent's country's
	 * neighbor list.
	 * @param ignoreContinentKey - Ignores this continent while checking for the country check.
	 * @param checkCountry - Checks this country in all other continents excepts the ignoreContinentKey.
	 * 
	 */
	private boolean isCountryExistInOtherContinents(String ignoreContinentKey, Country checkCountry) {
		boolean countryExists = false;		
		for(String otherContinentKey : Game.getEditMap().getContinents().keySet()) {
			if(countryExists) {
				break;
			}			
			Continent otherContinent = Game.getEditMap().getContinents().get(otherContinentKey);			
			if(!otherContinentKey.equals(ignoreContinentKey)) {
				for(Country otherContinentCountry : otherContinent.getTerritories()) {
					for(Country neighbor : otherContinentCountry.getNeighbours()) {
						if(neighbor.getCountryName().equals(checkCountry.getCountryName())) {
							countryExists = true;
							break;
						}
					}
				}			
			}			
		}	
		return countryExists;
	}	
	
	/**
	 * this method saves the map object to the filename.
	 * @param commandInput - command fro the user.
	 * @return int value for the correct message.
	 * 
	 */
	public int saveMap(String[] commandInput) {		
		if(commandInput.length != 2) {
			return SAVE_MAP_COMMAND_ERROR;
		}
		
		if(Game.getEditMap().getContinents().isEmpty()) {
			return SAVE_MAP_NO_CONTINENTS;
		}
		
		String fileName = MAP_FILE_DIR + commandInput[1];		 
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
				return SAVE_MAP_INVALID;
			}
			writer.write(fileContent);
			writer.close();
		} catch (IOException e) {			
			e.printStackTrace();
		} 
	    
		Game.setEditMapSet(false); // last statement in this method 
		return SAVE_MAP_SUCCESS;
	}
	
	/**
	 * This method processes the edit Map object and convert to string to save to file.
	 * @return fileContent .map file content.
	 * 
	 */
	private String getSaveMapFileContent() {
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
	 * getter method to get a Comma Separated String of neighbor list.
	 * @param neighbours.
	 * @return CSV list.
	 * 
	 */
	private String getNeighborCSV(ArrayList<Country> neighbours) {
		String neighborCSV = "";
		for(Country neighbor : neighbours) {
			neighborCSV += neighbor.getCountryName() + ",";
		}
		neighborCSV = neighborCSV.replaceAll(",$", "");
		return neighborCSV;
	}
	
	/**
	 * Getter method to get edit map file name.
	 * @return editMapFileName.
	 * 
	 */
	public String getEditMapFileName() {
		return editMapFileName;
	}
	
	/**
	 * Setter method to set edit map file name.
	 * @param editMapFileName.
	 * 
	 */
	public void setEditMapFileName(String editMapFileName) {
		this.editMapFileName = editMapFileName;
	}	
}
