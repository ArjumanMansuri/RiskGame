package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import models.Continent;
import models.Country;
import models.Game;
import models.Map;

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

//	/**
//	 * @param file 
//	 * 
//	 */
//	private void writeMapIdentifiers(String fileName) {	    	    
//	    try {
//	    	BufferedWriter writeMapDefault = new BufferedWriter(new FileWriter(fileName, true));
//			writeMapDefault.append("[Map] \n");
//			writeMapDefault.append("[Continents] \n");
//		    writeMapDefault.append("[Territories] \n");
//		    writeMapDefault.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}	    
//	}

	/**
	 * Validate the command and parse. 
	 * @param command
	 * @param fileName
	 * @return error / success 
	 */
	public String commandParser(String command, String fileNameInput, boolean mapFileExists) {
		String fileName = fileNameInput.split(" ")[1];
		String[] commandInput = command.split(" ");		
		String[] commands = {"editcontinent", "editcountry", "editneighbor", "showmap", "validatemap"};
		Map editMap;
		
		// instantiate a map object
		if(mapFileExists) {
			MapFileParser mapParser = new MapFileParser();
			editMap = mapParser.readFileData(MAP_FILE_DIR + fileName);
		}else {
			editMap = new Map();			
		}
				
		// Set the edit map object to Game 
		Game.setEditMap(editMap);
						
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
		}
		return "error";		
	}
	
	private void editNeighbor(String[] commandInput) {
		
	}

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
			} else if(operation.equals("-remove")){
				for(String continentKey : editMapContinents.keySet()) {					
//					editMapContinents.get(continentKey).getTerritories().
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
	public Boolean validateMap() {
		
		return null;		
	}
	
	/**
	 * Save the map object to the filename
	 * @return boolean 
	 */
	public Boolean saveMap() {
		return null;
	}
}
