package com.riskGame.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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

public class MapFileEdit {

	public static final String MAP_FILE_DIR = "maps/";
	
	/**
	 * Check if the map file exists.
	 * @param command
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
				return "exists";
			}else {
				// create Map file with the name
				File file = new File(MAP_FILE_DIR + commandInput[1]);
				try {
					if(file.createNewFile()) {						
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
	 * @param command
	 * @param fileName
	 * @return error / success 
	 */
	public String commandParser(String command, String fileNameInput, boolean mapFileExists) {				
		String[] commands = {"editcontinent", "editcountry", "editneighbor"};
		String[] commandsNonArgs = {"showmap", "validatemap", "savemap"};
		Map editMap = Game.getEditMap();
		String[] commandInput = command.split(" ");
		
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
					validateMap();
					break;
				case "showmap":
					showMap();
					break;
				case "savemap":
					saveMap();
					break;
			}
		}		
		return "error";		
	}
	
	private void showMap() {
				
	}

	/**
	 * Edit the neighbor of a country.
	 * @param commandInput
	 */
	private void editNeighbor(String[] commandInput) {
		if(commandInput.length >= 4) {
			String operation = commandInput[1]; // add or remove
			String countryName = commandInput[2]; 
			String neightborCountryName = commandInput[3]; 
			HashMap<String, Continent> editMapContinents= Game.getEditMap().getContinents();
		
			if(operation.equals("-add")) {			
				Country addCountry = new Country();
				addCountry.setCountryName(countryName);
				
				for(String continentKey : editMapContinents.keySet()) {	
					Continent currentContinent = editMapContinents.get(continentKey);
					currentContinent.getTerritories().forEach(country -> {
						if(country.getCountryName().equals(countryName)) {
							System.out.println("Before adding : " + country.getNeighbours().size());
							country.getNeighbours().add(addCountry);
							System.out.println("after adding : " + country.getNeighbours().size());
						}
					});
				}				
			} else if(operation.equals("-remove")) {
				for(String continentKey : editMapContinents.keySet()) {										
					editMapContinents.get(continentKey).getTerritories().forEach(country -> {
						System.out.println("Before remove : " + country.getNeighbours().size());
						country.getNeighbours().removeIf(neighbor -> neighbor.getCountryName().equals(neightborCountryName));
						System.out.println("After remove : " + country.getNeighbours().size());
					}); 					
				}								
			}
		}
	}
	
	/** 
	 * Edit a country - add or remove.
	 * @param commandInput
	 */
	private void editCountry(String[] commandInput) {
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
				for(String continentKey : editMapContinents.keySet()) {										
					editMapContinents.get(continentKey).getTerritories().removeIf(country -> country.getCountryName().equals(countryName));					
				}
			}		
		}
	}

	/**
	 * Edit a continent in a map file. Add or remove continent.
	 * @param editMap map object to be edited.
	 * @param commandInput Full command entered by the user.
	 */
	private void editContinent(String[] commandInput) {
		String operation = commandInput[1]; // add or remove
		String name = commandInput[2]; // continent Name value
		int value = Integer.parseInt(commandInput[3]); // continent control value
		HashMap<String, Continent> editMapContinents= Game.getEditMap().getContinents();		
		
		if(operation.equals("-add")) {
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
	 * Checks the input command arguments. 
	 * 
	 * @param commandInput
	 */
	private boolean checkArguments(String[] commandInput) {		
		if(Arrays.asList(commandInput).contains("-add") || Arrays.asList(commandInput).contains("-remove")) {
			return true;
		}
		return false;		
	}

	/**
	 * Validate the loaded map.
	 * Run before loading a map/saving a map.
	 * @return validMap value holds true/false.
	 */
	public boolean validateMap() {
//		validateContinentConnections();
		System.out.println(validateCountryConnections());
//		validateNeighbors();
		return false;		
	}
	
	/**
	 * Validate 
	 * @return
	 */
	public boolean validateCountryConnections(){
		int continentsConnected = 0;
		for(String continentKey: Game.getEditMap().getContinents().keySet()) {
			Continent currentContinent = Game.getEditMap().getContinents().get(continentKey);
			
			/**
			 *  Iterate all the countries in the continent and check if either of them doesn't have a neighbor 
			 *  which is from the same continent
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
	 * Checks if neighbors are from the countriesInContinent
	 * @param neighbors
	 * @param countriesInContinent
	 * @return neighbourFromCountryFound true if neighbor is found from same continent 
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
	 * Get country names from Country array list
	 * @param continentCountries
	 * @return
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
	 * Validate if a given continent is connected to at least one other continent in the graph.
	 * 
	 */
	private boolean validateContinentConnections() {
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
	 * Check if the given checkCountry exists in all other continent's country's neighbor list. 
	 * @param ignoreContinentKey Ignores this continent while checking for the country check.
	 * @param checkCountry Checks this country in all other continents excepts the ignoreContinentKey  
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
//							System.out.println("Country " + checkCountry.getCountryName() + " found in " + otherContinentKey + " as a neighbor for " + otherContinentCountry.getCountryName());
							break;
						}
					}
				}			
			}			
		}	
		return countryExists;
	}	
	
	/**
	 * Save the map object to the filename
	 * @return boolean 
	 */
	public Boolean saveMap() {
		
		Game.setEditMapSet(false); // last statement in this method 
		return null;
	}
}




