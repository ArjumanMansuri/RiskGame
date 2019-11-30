package com.riskGame.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

	// TEXT COLOR TYPES

	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";	
	public static final String ANSI_GREEN = "\u001B[32m";

	// TYPE OF MAP FILE CONSTANTS
	private static final int TYPE_CONQUEST_MAP = 1;
	private static final int TYPE_DOMINATION_MAP = 2;
	private static final int INVALID_MAP_FILE_TYPE = 0;

	private static final int CONTINENT_ADD = 1;
	private static final int CONTINENT_REMOVE = 2;
	private static final int COUNTRY_ADD = 3;
	private static final int COUNTRY_REMOVE = 4;

	private String editMapFileName;

	// Map parser object - DominationMapFileParser or MapFileParser
	public static BaseMapFile mapParser;


	/**
	 * Check if the given map file exists - else create
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
			File mapFileCheck = new File(BaseMapFile.MAP_FILE_DIR + commandInput[1]);
			if(mapFileCheck.exists()) {
				setEditMapFileName(BaseMapFile.MAP_FILE_DIR + commandInput[1]);
				if(validateMapOnLoadAndSave(BaseMapFile.MAP_FILE_DIR + commandInput[1])) {
					return "exists";					
				} else {
					return "error";
				}				
			}else {
				// create Map file with the name
				File file = new File(BaseMapFile.MAP_FILE_DIR + commandInput[1]);
				try {
					if(file.createNewFile()) {	
						setEditMapFileName(BaseMapFile.MAP_FILE_DIR + commandInput[1]);
						if(validateMapOnLoadAndSave(BaseMapFile.MAP_FILE_DIR + commandInput[1])) {
							return commandInput[1];
						}else {
							return "error";							
						}						
					}
				} catch (IOException e) {
					return "error" + e.getMessage();
				}				
			}
			return "error";
		}
	}
	/**
	 *  Validate map on loading and saving
	 * @param fileName Name of the map file
	 * @return true if file is valid else false
	 */
	private boolean validateMapOnLoadAndSave(String fileName) {
		selectMapParser(fileName);
		if(mapParser.validateValidMapFile(fileName)) {			
			Map editMap = Game.getEditMap();			
			editMap = mapParser.read(fileName);
			Game.setEditMapSet(true);
			Game.setEditMap(editMap);
			
			if(!validateMap()) {				
				printMapStatusMessage(false);
				return false;
			}else {				
				printMapStatusMessage(true);
				return true;
			}			
		}else {
			printMapStatusMessage(false);
			return false;
		}		
	}

	/**
	 * Validate the command and parse.
	 * @param command - command input from the user.
	 * @param fileNameInput - name of the map file.
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
				selectMapParser(fileName);
				editMap = mapParser.read(BaseMapFile.MAP_FILE_DIR + fileName);
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
					int saveMap = mapParser.write(commandInput);
					switch(saveMap) {
						case BaseMapFile.SAVE_MAP_COMMAND_ERROR:
							System.out.println("There is an error the save map command. Please try again!");
							break;
						case BaseMapFile.SAVE_MAP_NO_CONTINENTS:
							printMapStatusMessage(false);
							break;
						case BaseMapFile.SAVE_MAP_INVALID:
							printMapStatusMessage(false);
							break;
						case BaseMapFile.SAVE_MAP_SUCCESS:
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
	public static void printMapStatusMessage(boolean status) {
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
		mapContent = mapParser.getSaveMapFileContent();
		return mapContent;
	}
	
	/**
	 * Edit the neighbor of a country.
	 * @param commandInput - input command from the user.
	 * 
	 */
	public void editNeighbor(String[] commandInput) {		
		if(commandInput.length >= 4) {
			List<String> processArgs = checkNeighborCommandArgs(commandInput);			
			HashMap<String, Continent> editMapContinents= Game.getEditMap().getContinents();
			
			for(String arg : processArgs) {
				String[] argSplit = arg.split("\\$"); // Split using $ symbol				
				String countryName = argSplit[1];
				String neighborCountryName = argSplit[2];				
				Country neighbourCountry;
				
				if((neighbourCountry = isCountryExists(neighborCountryName)) != null) {						
					if(argSplit[0].equals("-add")) {			
						Country addCountry = new Country();
						addCountry.setCountryName(neighborCountryName);
						
						//  Add neighbor country to main country if the main country exists  
						Country country = isCountryExists(countryName);
						if(country != null) {
							country.getNeighbours().put(neighborCountryName, addCountry);
							neighbourCountry.getNeighbours().put(countryName, country);
						}					
					} else if(argSplit[0].equals("-remove")) {
						for(String continentKey : editMapContinents.keySet()) {
							
							for(Country country : editMapContinents.get(continentKey).getTerritories()) {
								if(country.getCountryName().equalsIgnoreCase(countryName)) {
									country = isCountryExists(country.getCountryName());								
									country.getNeighbours().entrySet().removeIf(neighbor -> neighbor.getKey().equals(neighborCountryName));
								}								
							}
							
							for(Country country1 : editMapContinents.get(continentKey).getTerritories()) {
								if(country1.getCountryName().equalsIgnoreCase(neighborCountryName.trim())) {
									country1 = isCountryExists(country1.getCountryName());
									country1.getNeighbours().entrySet().removeIf(n -> n.getKey().equals(countryName));
								}
							}																	
							
						}								
					}
				}						
			}			
		}
	}
	
	/**
	 * Parse the editneighbor command and get the command args as list elements.
	 * @param commandInput
	 * @return List with Commands - imploded by $ symbol.  
	 */	
	private List<String> checkNeighborCommandArgs(String[] commandInput) {
		return checkCommandArgs(commandInput, true);
	}
	
	/**
	 * Check if a given country exists in the map.
	 * @param countryName - name of the country to check existence.
	 * @return Country object.
	 * 
	 */
	public static Country isCountryExists(String countryName){
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
	public boolean editCountry(String[] commandInput) {
		if(commandInput.length > 2) {
			List<String> processArgs = checkCountryCommandArgs(commandInput);			
			if(processArgs == null) {
				return false;
			}
			HashMap<String, Continent> editMapContinents= Game.getEditMap().getContinents();		
			
			for(String arg : processArgs) {
				String[] argSplit = arg.split("\\$"); // Split using $ symbol
				
				if(argSplit[0].equals("-add")) {
					if(argSplit.length >= 3) {			
						String continentName = argSplit[2]; // Continent Name Value
						String countryName = argSplit[1]; // country Name Value
						
						if(editMapContinents.containsKey(continentName)) {  
							// Check if the continent exists to add the given country and check if the given country already exists
							if(isCountryExists(countryName) == null) {
								Country addCountry = new Country();
								addCountry.setContinent(continentName);
								addCountry.setCountryName(countryName);						
								editMapContinents.get(continentName).getTerritories().add(addCountry);
								checkParserAndUpdateIndexes(countryName, COUNTRY_ADD);
							}													
						}	
					} else {
						return false;
					}
				} else if(argSplit[0].equals("-remove")) {		
					String countryName = argSplit[1]; // country Name Value
					for(String continentKey : editMapContinents.keySet()) {
						int sizeBefore = editMapContinents.get(continentKey).getTerritories().size();
						editMapContinents.get(continentKey).getTerritories().removeIf(country -> country.getCountryName().equals(countryName));
						int sizeAfter = editMapContinents.get(continentKey).getTerritories().size();

						if((sizeAfter + 1) == sizeBefore){
							checkParserAndUpdateIndexes(countryName, COUNTRY_REMOVE);
						}

						// Remove country from neighbors list of other countries
						editMapContinents.get(continentKey).getTerritories().forEach(neighborcountry -> {
							neighborcountry.getNeighbours().entrySet().removeIf(n -> n.getKey().equals(countryName));
						});
					}
				}
				
			}			
			return true;
		}
		return false;
	}
	
	/**
	 * Parse the editcountry command and get the command args as list elements.
	 * @param commandInput
	 * @return List with Commands - imploded by $ symbol.  
	 */
	private List<String> checkCountryCommandArgs(String[] commandInput) {
		return checkCommandArgs(commandInput,false);		
	}

	/**
	 * This method edits a continent in a map file. Add or remove continent.
	 * @param commandInput Full command entered by the user.
	 */
	public boolean editContinent(String[] commandInput) {		
		List<String> processArgs = checkContinentCommandArgs(commandInput);		
		if(processArgs == null) {
			return false;
		}		
		HashMap<String, Continent> editMapContinents = Game.getEditMap().getContinents();		
		
		for(String arg : processArgs) {
			String[] argSplit = arg.split("\\$"); // Split using $ symbol			
			String name = argSplit[1]; // continent name 
			
			if(argSplit[0].equals("-add")) {
				try {				
					int value = Integer.parseInt(argSplit[2]); // continent control value 				
					Continent addContinent = new Continent();
					addContinent.setContinentName(name);
					addContinent.setControlValue(value);
					if(!editMapContinents.containsKey(name)) {
						editMapContinents.put(name, addContinent);
						checkParserAndUpdateIndexes(name, CONTINENT_ADD);
					}
				} catch(NumberFormatException e) {
					return false;
				}
			} else if(argSplit[0].equals("-remove")){			
				if(editMapContinents.containsKey(name)) {
					editMapContinents.remove(name);
					checkParserAndUpdateIndexes(name, CONTINENT_REMOVE);
				}
			}				
		}		
		return true;
	}

	private void checkParserAndUpdateIndexes(String name, int type) {
		switch(type){
			case CONTINENT_ADD:
				int newContinentIndex = DominationMapParser.continentsIndex.size() + 1;
				DominationMapParser.continentsIndex.put(name, newContinentIndex);
				break;

			case CONTINENT_REMOVE:
				// remove the entry in the continentsindex map
				DominationMapParser.continentsIndex.remove(name);

				// rearrange the continentsIndex indexes for each continent
				int continentNewIndexer = 1;
				HashMap<String, Integer> continentsIndexTemp = new HashMap<String, Integer>();
				for (java.util.Map.Entry<String, Integer> continent: DominationMapParser.continentsIndex.entrySet()) {
					continentsIndexTemp.put(continent.getKey(), continentNewIndexer);
					continentNewIndexer++;
				}

				DominationMapParser.continentsIndex = continentsIndexTemp;
				break;

			case COUNTRY_ADD:
				int newCountryIndex = DominationMapParser.countryIndexes.size() + 1;
				DominationMapParser.countryIndexes.put(newCountryIndex, name);
				break;

			case COUNTRY_REMOVE:
				// remove the entry in the countryindex map
				DominationMapParser.countryIndexes.remove(name);

				// rearrange the continentsIndex indexes for each continent
				int countryNewIndexer = 1;
				HashMap<String, Integer> countryIndexTemp = new HashMap<String, Integer>();

				for (java.util.Map.Entry<String, Integer> continent: DominationMapParser.reverseCountryIndexes().entrySet()) {
					countryIndexTemp.put(continent.getKey(), countryNewIndexer);
					countryNewIndexer++;
				}

				DominationMapParser.countryIndexes = DominationMapParser.reverseCountryIndexes(countryIndexTemp);
				break;
		}
	}

	/**
	 * Parse the editcontinent command and get the command args as list elements.
	 * @param commandInput
	 * @return List with Commands - imploded by $ symbol. 
	 */
	private List<String> checkContinentCommandArgs(String[] commandInput) {		
		return checkCommandArgs(commandInput,false);
	}

	/**
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
		
		// Make countries hashmap 
		HashMap<String, Country> countries = new HashMap<String, Country>();
		for(Continent continent : Game.getEditMap().getContinents().values()) {						
			for(Country currentCountry : continent.getTerritories()) {	
				countries.put(currentCountry.getCountryName(), currentCountry);	
			}			
		}		
		
		boolean validNeighbors  = validateNeighbors();
		
		MapConnected continentsConnected = new MapConnected(Game.getEditMap().getContinents(),countries);	
		boolean validContinents = continentsConnected.checkConnectedContinents();
				
		MapConnected countriesConnected = new MapConnected(countries);
		boolean validCountries  = countriesConnected.checkConnectedCountries();
				
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
	 */
	public boolean checkNeighborsHasCountry(Country country, HashMap<String,Country> neighbours) {
		int neighbourCount = 0;
		
		// neighborCountry doesn't have neighbors set in the object. 
		for(String neighborCountryName: neighbours.keySet()) {
			Country neighborCountry	= isCountryExists(neighborCountryName);
			if(neighborCountry == null) {				
				break;
			}
			
			for(String neighborOfNeighborName : neighborCountry.getNeighbours().keySet()) {	
				Country neighborOfNeighbor	= isCountryExists(neighborOfNeighborName);
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
	private boolean countrySameContinentNeighbor(HashMap<String,Country> neighbors, String[] countriesInContinent) {
		boolean neighbourFromCountryFound = false;
		for(String neighborCountryName : neighbors.keySet()) {
			if(Arrays.asList(countriesInContinent).contains(neighborCountryName)) {
				neighbourFromCountryFound = true;
				break;
			}
		}
		return neighbourFromCountryFound;
	}	
	
	/**
	 * Method to get country names from Country array list
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
	 * This method checks if the given checkCountry exists in all other continent's country's
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
					for(String neighborCountryName : otherContinentCountry.getNeighbours().keySet()) {
						Country neighbor = isCountryExists(neighborCountryName);
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
	 * Getter method to get edit map file name.
	 * @return editMapFileName.
	 * 
	 */
	public String getEditMapFileName() {
		return editMapFileName;
	}
	
	/**
	 * Setter method to set edit map file name.
	 * 
	 */
	public void setEditMapFileName(String editMapFileName) {
		this.editMapFileName = editMapFileName;
	}
		
	/**
	 * Method for checking command args for editcontinent and editcountry
	 * @param commandInput
	 * @return List - command parsed and imploded by $ 
	 */
	private List<String> checkCommandArgs(String[] commandInput, boolean isNeighborCheck) {
		List<String> commandList = new ArrayList<String>(Arrays.asList(commandInput));
		List<String> processArgList = new ArrayList<String>();
		int addFrequency = Collections.frequency(commandList, "-add");
		int removeFrequency = Collections.frequency(commandList, "-remove");
		if (addFrequency > 0) {
			for (int i = 0; i < addFrequency; i++) {
				int addIndex = commandList.indexOf("-add");
				try {
					processArgList.add(commandList.get(addIndex) + "$" + commandList.get(addIndex + 1) + "$" + commandList.get(addIndex + 2));
					commandList.remove(addIndex);
					commandList.remove(addIndex);
					commandList.remove(addIndex);	
				} catch(IndexOutOfBoundsException e) {
					return null;
				}
			}
		}
		if (removeFrequency > 0) {
			for (int i = 0; i < removeFrequency; i++) {
				try {
					int removeIndex = commandList.indexOf("-remove");
					
					// If the check and parsing is for editneighbor command - the commandlist processing is different, needs to consider 3 indexes as done below 
					if(isNeighborCheck) {						
						processArgList.add(commandList.get(removeIndex) + "$" + commandList.get(removeIndex + 1) + "$" + commandList.get(removeIndex + 2));
						commandList.remove(removeIndex);
						commandList.remove(removeIndex);
						commandList.remove(removeIndex);
					} else {
						processArgList.add(commandList.get(removeIndex) + "$" + commandList.get(removeIndex + 1));
						commandList.remove(removeIndex);
						commandList.remove(removeIndex);
					}
				} catch(IndexOutOfBoundsException e) {
					return null;
				}
			}
		}
		return processArgList;
	}	
	
	/**
	 * Show the map anytime during the game play.
	 */
	public static void gamePlayShowMap() {
		// Print all the continents
		System.out.println("------------------------------------------------\n");
		System.out.println("Continents : \n--------------");
		for(Continent showContinent : Game.getMap().getContinents().values()) {
			System.out.println(showContinent.getContinentName() + " = " + showContinent.getControlValue());
		}
			
		// Print countries and armies 
		for(Country showCountry : Country.getListOfCountries().values()) {
			System.out.println("Country Name : " + showCountry.getCountryName());
			System.out.println("Armies 	: " + showCountry.getNumberOfArmies());
			String ownerName = "Owner   : N/A";
			try {
				ownerName = "Owner  	: " + Game.getPlayersList().get(showCountry.getOwner()).getPlayerName();
			} catch(Exception e) {
				// 
			} finally {				
				System.out.println(ownerName);
			}
			
			System.out.println("-------------- \n Neighbors : \n--------------");			
			for(Country showCountryNeighbor : showCountry.getNeighbours().values()) {
				System.out.println(showCountryNeighbor.getCountryName());
			}						
			System.out.println("------------------------------------------------");
		}		
	}

	/**
	 * Select the map file parser between DominationMapParser and MapFileParser and set the static variable in the class
	 * @param fileName
	 * @return
	 */
	public BaseMapFile selectMapParser(String fileName){
		int identifyMap = identifyMapType(fileName);
		switch(identifyMap){
			case TYPE_CONQUEST_MAP:
				mapParser = new MapFileParserAdapter();
				break;
			case TYPE_DOMINATION_MAP:
				mapParser = new DominationMapParser();
				break;
		}
		return null;
	}

	public int identifyMapType(String fileName) {
		int MapFileType = INVALID_MAP_FILE_TYPE;
		try {
			FileReader  identifyMapFileReader =  new FileReader(fileName);
			BufferedReader readMap = new BufferedReader(identifyMapFileReader);
			while(readMap.ready()) {
				String line = readMap.readLine().trim();
				if(line.isEmpty()) {
					continue;
				}
				if(line.equals("[Territories]")) {
					 MapFileType = TYPE_CONQUEST_MAP;
					 break;
				}
				if(line.equals("[countries]")) {
					MapFileType = TYPE_DOMINATION_MAP;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return MapFileType;
	}
}
