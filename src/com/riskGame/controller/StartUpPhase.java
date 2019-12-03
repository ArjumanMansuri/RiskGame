package com.riskGame.controller;

import com.riskGame.models.Player;
import com.riskGame.observer.PhaseViewObserver;
import com.riskGame.observer.PhaseViewPublisher;
import com.riskGame.observer.PlayerDominationViewObserver;
import com.riskGame.observer.PlayerDominationViewPublisher;
import com.riskGame.observer.StartupPhaseObserver;
import com.riskGame.strategies.AggressivePlayer;
import com.riskGame.strategies.BenevolentPlayer;
import com.riskGame.strategies.CheaterPlayer;
import com.riskGame.strategies.HumanPlayer;
import com.riskGame.strategies.RandomPlayer;
import com.riskGame.models.Country;
import com.riskGame.models.Map;
import com.riskGame.models.Game;
import com.riskGame.controller.MapFileParser;
import java.io.File;
import java.util.*;

/**
 * This class contains the business logic of the StartUpPhase
 * @author	niralhlad
 * 
 */
public class StartUpPhase implements PhaseViewPublisher, PlayerDominationViewPublisher{
	private HashMap<Integer,Player> playersData;
	private int noOfPlayers;
	private PhaseViewObserver newObserver;
	private PlayerDominationViewObserver newDomiantionObserver;
	
	/**
	 * Constructor of this class
	 */
	public StartUpPhase(){
		playersData = new HashMap<Integer,Player>();
		newObserver = new StartupPhaseObserver();
		newDomiantionObserver = new PlayerDominationViewObserver();
	}
	
	/**
	 * getter method for number of players
	 * @return number of players
	 */
	public int getNoOfPlayers() {
		return noOfPlayers;
	}
	
	/**
	 * setter method for number of players
	 * @param noOfPlayers
	 */
	public void setNoOfPlayers(int noOfPlayers) {
		this.noOfPlayers = noOfPlayers;
	}

	/**
	 * This method parses the input string.
	 * @param input the input from the console passed from the GameLaunch.java.
	 * @return the string whether the input is processed or not. "exit" if processed and "error" if not.
	 */
	public String parser(String input){
		input = input.trim();
		Map mapObject = null;

		//Check if input is empty or blank
		if(input.isEmpty() || input.length()==0) {
			return "error";
		}

		//check if the input is number of player or any other command
		if(input.matches("\\d+")){
			noOfPlayers = Integer.parseInt(input);
			if(noOfPlayers<2 || noOfPlayers>6) {
				return "error";
			}
			else {
				return "done";
			}
		}
		String thisInput;
		thisInput = input;

		if(!input.contains("loadmap")){
			thisInput = input.toLowerCase();
		}

		//call the gameplayer function
		if(thisInput.contains("gameplayer")){
			if(inputValidator(thisInput)==0) {
				return "error";
			}
			else {
				return gamePlayer(thisInput);
			}
		}

		else if(thisInput.contains("loadmap")){
			
			String[] stringParsed = thisInput.split(" ");
			String fileName= stringParsed[1];

			String[] fileNameParsed = fileName.split("\\.");

			if(stringParsed[0].equals("loadmap")){
				this.notifyObserver(fileName + " Map is been loaded...");
				File mapFileCheck = new File("maps/"+fileName);
				if(mapFileCheck.exists()) {
					MapFileEdit mapEditor = new MapFileEdit();
					mapEditor.selectMapParser(fileName);
					mapObject = MapFileEdit.mapParser.read(BaseMapFile.MAP_FILE_DIR + fileName);
					Game.setMap(mapObject);
					return "exit";
				}
				else {
					return "fileNotFound";
				}
			}
		}
		else if(thisInput.contains("populatecountries")) {
			this.notifyObserver("Populating countries...");
			ArrayList<String> countries = new ArrayList<>();
				for(String country : Country.getListOfCountries().keySet()){
					countries.add(country);
				}
			
			while(countries.size()>0){
				for(int i=1;i<=noOfPlayers;i++){
					if(countries.size()>0){
						Country.getListOfCountries().get(countries.get(0)).setOwner(i);
						Game.getPlayersList().get(i).getOwnedCountries().add(countries.get(0));
						countries.remove(0);
					}
				}
			}
		}
		//Final return if nothing works
		return "Error";
	}

	/**
	 * This method is used to place army into countries one by one.
	 * @param playerNumber - current player. 
	 * @param command - command from user.
	 * @return string value.
	 * 
	 */
	public String placeArmy(int playerNumber,String command){
		this.notifyObserver("Starting to place the armies for each countries of all players...");
		if(command.isEmpty() || command.trim().length()==0) {
			return "Error : Invalid Command";
		}

		String[] commandComponents = command.split(" ");
		String commandName = commandComponents[0];
		
		if(commandName.equalsIgnoreCase("showmap")) {
			MapFileEdit.gamePlayShowMap();
			return "";
		}

		if(!commandName.equals("placearmy")) {
			return "Error : Enter placearmy command";
		}
		String countryName = commandComponents[1];
		Player p = Game.getPlayersList().get(playerNumber);

		if(!p.getOwnedCountries().contains(countryName)) {
			return "Error : Country not owned by the player";
		}
		ArrayList<String> countries = p.getOwnedCountries();
		if(Country.getListOfCountries().get(countryName).getNumberOfArmies()==1) {
			for (String country : countries) {
				if(Country.getListOfCountries().get(country).getNumberOfArmies()==0) {
					return "Error : Country "+ country+" has 0 armies. Please put an army on it.";
				}
			}
		}
		Country.getListOfCountries().get(countryName).setNumberOfArmies(Country.getListOfCountries().get(countryName).getNumberOfArmies()+1);
		this.notifyObserver("Added " + countryName + "to" + p.getPlayerName());
		return "donePlaceArmy";
	}

	/**
	 * Thus method is used to placeall army into countries owned by the players. 
	 * @param playerNumber - current player.
	 * @param command - command from user input.
	 * @return string value.
	 * 
	 */
	public String placeAll(int playerNumber,String command){

		if(command.isEmpty() || command.trim().length()==0 || !command.equals("placeall")) return "Error : Invalid Command";

		Player p = Game.getPlayersList().get(playerNumber);
		ArrayList<String> countries = p.getOwnedCountries();

		this.notifyObserver("Starting to PlaceAll armies for " + p.getPlayerName());
		// get countries with zero armies
		for (String country : countries) {
			if(Country.getListOfCountries().get(country).getNumberOfArmies()==0) {
				Country.getListOfCountries().get(country).setNumberOfArmies(1);
				p.setPlayerNumOfArmy(p.getPlayerNumOfArmy()-1);
				this.notifyObserver("Placed 1 army to the country " + country + "for " + p.getPlayerName());
			}
		}
		while(p.getPlayerNumOfArmy()!=0) {
			for (String country : countries) {
				if(p.getPlayerNumOfArmy()==0) {
					break;
				}
				Country.getListOfCountries().get(country).setNumberOfArmies(Country.getListOfCountries().get(country).getNumberOfArmies()+1);
				this.notifyObserver("Placed 1 army to the country " + country + "for " + p.getPlayerName());
				p.setPlayerNumOfArmy(p.getPlayerNumOfArmy()-1);
				this.notifyObserver("Total number of armies for " + p.getPlayerName() + "is increased to " + p.getPlayerNumOfArmy());
			}
		}
		this.notifyDominationObserver(p.computeDominationViewData());
		return "donePlaceall";
	}
	/**
	 * This method checks if all armies for all the players are placed.
	 * @return string value.
	 * 
	 */
	public String allPlayerArmies() {
		boolean allPlayerDone = true; 
		HashMap<Integer, Player> playerList = Game.getPlayersList();
		for(int i=1;i<=playerList.size();i++) {
			if(playerList.get(i).getPlayerNumOfArmy()==0) {
				allPlayerDone = true && allPlayerDone;
			}
			else {
				allPlayerDone = false && allPlayerDone;
			}
		}
		if(allPlayerDone) {
			this.notifyObserver("All armies for all the players has been placed");
			return "done";
		}
		else {
			return "";
		}
	}

	/**
	 * The function to process the input and add the data to the HASHMAP accordingly.
	 * @param thisInput the input to be parsed and based on which 'add' or 'remove' is calculated
	 * @return the string whether the process is successful or not. "exit" if successful and "error" if not.
	 * 
	 */
	String gamePlayer(String thisInput){

		String[] parsedString = thisInput.split(" ");
		int playerId;
		if(playersData.size()==0)
			playerId=1;
		else
			playerId=playersData.size()+1;

		int army[]={60,35,30,25,20};

		for(int i=1;i<parsedString.length;i++) {

			if(parsedString[i].equals("-add")) {

				if(!ifContains(playersData,parsedString[i+1])) {
					Player p;
					String behaviour = parsedString[i+2].toLowerCase();
					if(behaviour.equals("human")) {
						p = new HumanPlayer();
					}
					else if(behaviour.equals("aggressive")) {
						p = new AggressivePlayer();
					}
					else if(behaviour.equals("benevolent")) {
						p = new BenevolentPlayer();		
					}
					else if(behaviour.equals("random")) {
						p = new RandomPlayer();					
					}
					else {
						p = new CheaterPlayer();
					}
					p.setPlayerName(parsedString[i+1]);
					p.setPlayerNumOfArmy(army[noOfPlayers-2]);
					p.setPlayerType(behaviour);
					playersData.put(playerId,p);
					i+=2;
					playerId++;
				}
				else i++;
			}

			else if(parsedString[i].equals("-remove")) {

				if(ifContains(playersData,parsedString[i+1])) {
					for(int x=1;x<=noOfPlayers;x++) {
						Player temp;
						temp = playersData.get(x);

						if(temp.getPlayerName().equals(parsedString[i+1])) {
							if(x==noOfPlayers) {
								playersData.remove(x);
								noOfPlayers--;
								i++;
							}
							else
							{
								for(int y=x; y<playersData.size();y++) {
									playersData.put(y,playersData.get(y+1));
									i++;
								}
								playersData.remove(noOfPlayers);
								noOfPlayers--;
								x=noOfPlayers+1;
							}
						}
					}
				}
				else {
					return "notfound";
				}
			}
		}

		if(playersData.size()<noOfPlayers)
			return "addmore";
		else{
			Game.setPlayersList(playersData);
			return "exit";
		}
	}

	/**
	 * To validate the input string.
	 * @param thisInput Input String.
	 * @return 1 if valid and 0 if invalid.
	 * @throws ArrayIndexOutOfBoundsException
	 * 
	 */
	public int inputValidator(String thisInput) throws ArrayIndexOutOfBoundsException{
		String[] validPlayerTypes = {"human","aggressive","benevolent","random","cheater"};
		String[] parsedString = thisInput.split(" ");
		try{
			if(parsedString[0].equals("gameplayer") && isNumberOfArgumentsCorrect(parsedString)){
				for(int i=1;i<parsedString.length;i++){
					if(parsedString[i].equals("-add") || parsedString[i].equals("-remove")){
						if(parsedString[i].equals("-add")){
							if(!Arrays.asList(validPlayerTypes).contains(parsedString[i+2].toLowerCase())) {
								return 0;
							}
							else {
								i = i+2;
							}
						}
						else {
							i = i+1;
						}
					}
					else{
						return 0;
					}
				}
				return 1;
			}
			else return 0;
		}
		catch (ArrayIndexOutOfBoundsException e){
			return 0;
		}
	}

	/**
	 * This method checks if the number of arguments is correct
	 * @param parsedString String containing arguments
	 * @return trues if number of arguments is correct else false
	 */
	public boolean isNumberOfArgumentsCorrect(String[] parsedString) {
		 List<String>parsedStringList = Arrays.asList(parsedString);
		int addFrequency = Collections.frequency(parsedStringList, "-add");
		int removeFrequency = Collections.frequency(parsedStringList, "-remove");
		if(parsedStringList.size()-1 == (addFrequency*3)+(removeFrequency*2)) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * This method checks if the hashmap contains the string or not.
	 * @param temp Hashmap with the data.
	 * @param name String to be searched in temp.
	 * @return True if found and False if not.
	 * 
	 */
	public Boolean ifContains(HashMap<Integer,Player> temp,String name){
		for(int i=1;i<=temp.size();i++){
			if(temp.get(i).getPlayerName().equals(name))
				return true;
		}
		return false;
	}
	
	/**
	 * This method is to notify the observer pattern
	 * @param action string to notify the observer
	 */
	@Override
	public void notifyObserver(String action) {
		this.newObserver.update(action);
		
	}

	/**
	 * This method is to notify the Domination observer pattern
	 * @param action string to notify the observer
	 */
	public void notifyDominationObserver(String action) {
		this.newDomiantionObserver.updateDomination(action);
	}
}


