package com.riskGame.controller;

import com.riskGame.models.Continent;
import com.riskGame.models.Player;
import com.riskGame.models.Country;
import com.riskGame.models.Map;
import com.riskGame.models.Game;
import com.riskGame.controller.MapFileEdit;
import com.riskGame.controller.MapFileParser;

import java.awt.*;
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
        Map mapObject = null;

        System.out.println(input);
        //Check if input is empty or blank
        if(input.isEmpty() || input.length()==0) {
            return "error";
        }

        //check if the input is number of player or any other command
        if(input.matches("\\d+")){
            noOfPlayers = Integer.parseInt(input);
            if(noOfPlayers<2 || noOfPlayers>6) return "error";
        }
        String thisInput;
        thisInput = input;

        if(!input.contains("loadmap")){
            thisInput = input.toLowerCase();
        }

        System.out.println(thisInput);
        //call the gameplayer function

        if(thisInput.contains("gameplayer")){
            if(inputValidator(thisInput)==0) return "error";
            else return gamePlayer(thisInput);
        }

        else if(thisInput.contains("loadmap")){
            String[] stringParsed = thisInput.split(" ");
            String fileName= stringParsed[1];

            String[] fileNameParsed = fileName.split("\\.");
            System.out.println(fileNameParsed[0]);

            if(stringParsed[0].equals("loadmap")){
                System.out.println("Loading map");
                File mapFileCheck = new File("maps/"+fileName);
                if(mapFileCheck.exists()) {
                    MapFileParser m = new MapFileParser();
                    mapObject = m.readFileData("maps/"+fileName);
                    Game.setMap(mapObject);
                    System.out.println("Here "+mapObject);
                    return "exit";
                }
                else
                    return "fileNotFound";
            }
        }

        else if(thisInput.contains("populatecountries")) {
            HashMap<String,Continent> continentList =  Game.getMap().getContinents();
            ArrayList<Country> countries = new ArrayList<>();
            for (Continent continent: continentList.values()) {
                for(Country country : continent.getTerritories()){
                    countries.add(country);
                }
            }

            int noOfCountries = countries.size();
            System.out.println(noOfCountries);
            System.out.println(countries);
            while(countries.size()>0){
                for(int i=1;i<=noOfPlayers;i++){
                    if(countries.size()>0){
                        Game.getPlayersList().get(i).getOwnedCountries().put(countries.get(0).getCountryName(),countries.get(0));
                        countries.remove(0);
                    }
                }
            }
            for(int i=1;i<=noOfPlayers;i++){
                System.out.println(Game.getPlayersList().get(i).getOwnedCountries());
            }
        }

        //Final return if nothing works
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
        else{
            Game.setPlayersList(playersData);
            return "exit";
        }

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


