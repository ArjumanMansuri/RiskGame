package com.riskGame.controller;

import com.riskGame.models.Player;
import com.riskGame.models.Country;
import com.riskGame.models.Map;
import com.riskGame.controller.MapFileEdit;
import com.riskGame.controller.MapFileParser;

import java.io.File;
import java.util.HashMap;
import java.util.*;

/**
 * This class controls the startup phase
 */
public class StartUpPhase {
	private HashMap<Integer,Player> playersData;
	private int noOfPlayers;

	public StartUpPhase(){
		playersData = new HashMap<Integer,Player>();
	}

	/**
	 * It parses the input string
	 * @param input the input from the console passed from the GameLaunch.java
	 * @return the string whether the input is processed or not. "exit" if processed and "error" if not.
	 */
	public String parser(String input){
		input = input.trim();
		Map mapObject;

		//Check if input is empty or blank
		if(input.isEmpty() || input.length()==0) {
			return "error";
		}

		//check if the input is number of player or any other command
		if(input.matches("\\d+")){
			noOfPlayers = Integer.parseInt(input);
			if(noOfPlayers<2 || noOfPlayers>6) return "error";
		}

		String thisInput = input.toLowerCase();

		//call the gameplayer function
		if(thisInput.contains("gameplayer")){
			if(inputValidator(thisInput)==0) return "error";
			else return gamePlayer(thisInput);
		}
		else if(thisInput.contains("loadmap")){
			String[] stringParsed = thisInput.split(" ");
			String filename= stringParsed[1];
			if(stringParsed[0].equals("loadmap")){
				File mapFileCheck = new File("maps/"+stringParsed[1]);
				if(mapFileCheck.exists()) {
					MapFileParser m = new MapFileParser();
					mapObject = m.readFileData(filename);
					return "mapLoaded";
				}
			}
		}
		else
			return "exit";

		return "exit";
	}

	//left
	public String placeArmy(int i,String abc){
		return "abc";
	}

	//left
	public String placeAll(int i){
		return "abc";
	}

	/**
	 * The function to process the input and add the data to the HASHMAP accordingly.
	 * @param thisInput the input to be parsed and based on which 'add' or 'remove' is calculated
	 * @return the string whether the process is successful or not. "exit" if successful and "error" if not.
	 */
	String gamePlayer(String thisInput){

		String[] parsedString = thisInput.split(" ");
		int playerId;
		if(playersData.size()==0)
			playerId=1;
		else
			playerId=playersData.size()+1;

		int army[]={60,35,30,25,20};

		for(int i=1;i<parsedString.length;i++){

			if(parsedString[i].equals("-add")){

				if(!ifContains(playersData,parsedString[i+1])){
					Player p = new Player();
					p.setPlayerName(parsedString[i+1]);
					p.setPlayerNumOfArmy(army[noOfPlayers-2]);
					playersData.put(playerId,p);
					System.out.println(playerId+" "+playersData.get(playerId).getPlayerName());
					i++;
					playerId++;
				}
				else i++;
			}

			else if(parsedString[i].equals("-remove")){

				if(ifContains(playersData,parsedString[i+1])){
					for(int x=1;x<=noOfPlayers;x++){
						Player temp;
						temp = playersData.get(x);

						if(temp.getPlayerName().equals(parsedString[i+1])){
							if(x==noOfPlayers){
								playersData.remove(x);
								noOfPlayers--;
								i++;
							}
							else
							{
								for(int y=x; y<playersData.size();y++){
									playersData.put(y,playersData.get(y+1));
									i++;
								}
								playersData.remove(noOfPlayers);
								noOfPlayers--;
								x=noOfPlayers+1;
							}
							System.out.println(noOfPlayers+" "+x);

						}
					}
				}
				else return "notfound";
			}
		}
		System.out.println(playersData);

		if(playersData.size()<noOfPlayers)
			return "addmore";
		else
			return "exit";
	}

	/**
	 * To validate the input string
	 * @param thisInput Input String
	 * @return 1 if valid and 0 if invalid
	 * @throws ArrayIndexOutOfBoundsException
	 */
	int inputValidator(String thisInput) throws ArrayIndexOutOfBoundsException{
		String[] parsedString = thisInput.split(" ");
		try{
			if(parsedString[0].equals("gameplayer")){
				for(int i=1;i<parsedString.length;i++){
					if(parsedString[i].equals("-add") || parsedString[i].equals("-remove")){
						if(!parsedString[i+1].equals("-add") && !parsedString[i+1].equals("-remove")){
							i=i+1;
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
	 *
	 * @param temp Hashmap with the data
	 * @param name String to be searched in temp
	 * @return True if found and False if not
	 */
	Boolean ifContains(HashMap<Integer,Player> temp,String name){
		for(int i=1;i<=temp.size();i++){
			if(temp.get(i).getPlayerName().equals(name))
				return true;
		}
		return false;
	}
}


