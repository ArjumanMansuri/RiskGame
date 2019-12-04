package com.riskGame.controller;

import com.riskGame.models.*;
import com.riskGame.models.Map;
import com.riskGame.strategies.*;
import com.riskGame.view.GameLaunch;

import java.util.*;
import java.util.Map.Entry;

public class TournamentMode {

    static HashMap<String,String> gameWinnerList;
    static HashMap<Integer,Boolean> removeList;

    // Take tournament command input and process
    public static void tournamentCommandInput() {
        gameWinnerList = new HashMap<String,String>();
        removeList = new HashMap<Integer,Boolean>(); // holds the numbers of the removed players

        Scanner sc = new Scanner(System.in);
        boolean response = false;

        do {
            System.out.println("Enter the command:  `tournament -M listofmapfiles -P listofplayerstrategies -G numberofgames -D maxnumberofturns`");
            String tournamentCmdInput = sc.nextLine().trim();
            response = checkTournamentCommandArgs(tournamentCmdInput);
        } while (!response);
    }

    //	List<String>
    private static boolean checkTournamentCommandArgs(String commandInput) {
        List<String> commandList = new ArrayList<String>(Arrays.asList(commandInput.split(" "))); // commands split by space
        List<String> processArgList = new ArrayList<String>();

        commandList.removeAll(Arrays.asList(null, ""));
        while (commandList.remove(" ")) ;

        int numMapFrequency = Collections.frequency(commandList, "-M");
        int numPlayersFrequency = Collections.frequency(commandList, "-P");
        int numGamesFrequency = Collections.frequency(commandList, "-G");
        int numTurnsFrequency = Collections.frequency(commandList, "-D");

        // Check if the user entered all the required command arguments
        if (numGamesFrequency + numPlayersFrequency + numMapFrequency + numTurnsFrequency == 4) {

//            try {
                // Check if the list of map files provided are legit
                int mapListArgStartIndex = commandList.indexOf("-M"); // index of -M
                int mapListEndIndex = getTournamentCmdListEndIndex(mapListArgStartIndex, commandList); // end index for the map list inclusive
                processArgList = getTournamentCmdValues(mapListArgStartIndex + 1, mapListEndIndex, commandList, processArgList, "-M");
                // Provided Map files are invalid
                if (processArgList == null) {
                    return false;
                }

                // Check if the list of players provided are legit
                int playerListArgStartIndex = commandList.indexOf("-P"); // index of -P
                int playerListEndIndex = getTournamentCmdListEndIndex(playerListArgStartIndex, commandList); // end index for the map list inclusive
                processArgList = getTournamentCmdValues(playerListArgStartIndex + 1, playerListEndIndex, commandList, processArgList, "-P");
                boolean strategyDuplicatesExist = checkPlayerStrategyDuplicates(processArgList.get(1));
                if (processArgList == null || strategyDuplicatesExist) {
                    return false;
                }

                // Check if the number of games input is valid
                int numGamesArgStartIndex = commandList.indexOf("-G"); // index of -G
                int numGamesEndIndex = getTournamentCmdListEndIndex(numGamesArgStartIndex, commandList); // end index for the map list inclusive
                processArgList = getTournamentCmdValues(numGamesArgStartIndex + 1, numGamesEndIndex - 1, commandList, processArgList, "-G");

                if (processArgList == null) {
                    return false;
                }

                // Check if the number of turns input is valid
                int numTurnsArgStartIndex = commandList.indexOf("-D"); // index of -G
                int numTurnsEndIndex = getTournamentCmdListEndIndex(numTurnsArgStartIndex, commandList); // end index for the map list inclusive
                processArgList = getTournamentCmdValues(numTurnsArgStartIndex + 1, numTurnsEndIndex - 1, commandList, processArgList, "-D");

                if (processArgList == null) {
                    return false;
                }

                // Process the command Values - at this stage all the command argument input values are valid
                processTournamentCommandArgs(processArgList);

//            } 
//            catch (IndexOutOfBoundsException e) {
//            	System.out.println(e.getMessage());
//            	return false;
//            }
            return true;
        }
        return false;
    }

    private static void processTournamentCommandArgs(List<String> processArgList) {
        processArgList.removeAll(Arrays.asList(null, ""));
        while (processArgList.remove(" ")) ;
        
        String[] tournamentMaps = processArgList.get(0).split("\\$");
        String[] tournamentStrategies = processArgList.get(1).split("\\$");
        int tournamentGamesNum = Integer.parseInt(processArgList.get(2).split("\\$")[0]);
        int tournamentTurnsNum = Integer.parseInt(processArgList.get(3).split("\\$")[0]);

        // Map Iterator
        for (String mapFileName : tournamentMaps) {
            for (int gameIndex = 1; gameIndex <= tournamentGamesNum; gameIndex++) {
            	System.out.println("Starting Game " + gameIndex);
            	System.out.println("-------------------------------");
                setupGameAndPlayers(mapFileName, tournamentStrategies, tournamentTurnsNum);
                startPlaying(tournamentStrategies, tournamentTurnsNum, gameIndex, mapFileName);
            }
        }
    }

    private static void startPlaying(String[] tournamentStrategies, int tournamentTurnsNum, int gameIndex, String map) {
        boolean stopGame = false;
        boolean playerWon = false;
        for (int turnIndex = 1; turnIndex <= tournamentTurnsNum; turnIndex++) {        	
            if(stopGame){
                break;
            }

            for (int playerNumber = 1; playerNumber <= Game.getPlayersList().size(); playerNumber++) {
            	
            	// If this player number is in the remove list - do not process it 
            	if(removeList.size() > 0) {            		
            		if(removeList.containsKey(playerNumber)) {
            			continue;
            		}
            	}
            	
                System.out.println("Player : " + Game.getPlayersList().get(playerNumber).getPlayerName());
                calculateReinforcementArmies(playerNumber);

                /* Reinforce for the current player */
                System.out.println("Reinforcement phase starts...");
                Game.getPlayersList().get(playerNumber).getReinforceType().reinforce(playerNumber);
                GameLaunch.printPlayerInformation(playerNumber);

                /* Attack phase for the current player */
                System.out.println("Attack phase starts...");
                Game.getPlayersList().get(playerNumber).getAttackType().attackSetup(playerNumber);
                GameLaunch.printPlayerInformation(playerNumber);
                
                if (hasPlayerWon(playerNumber)) {
                	System.out.println("Player "+Game.getPlayersList().get(playerNumber).getPlayerName()+" won..!!!");
                    setGameWinner(gameIndex, map, Game.getPlayersList().get(playerNumber).getPlayerName());
                    stopGame = true;
                    playerWon = true;
                    break;
                }
                
                checkPlayerStatus(); // Check if a player has 0 countries - if true - remove the player from the list 
                
                System.out.println("Player Number " + playerNumber);
                
        		if(removeList.containsKey(playerNumber)) {
        			System.out.println("Skipping fortification phase for this player..");
        			continue;	
        		}
        	    
                /* Fortify phase for the current player */
                System.out.println("Fortification phase starts...");
                Game.getPlayersList().get(playerNumber).getFortifyType().fortify(playerNumber);
                GameLaunch.printPlayerInformation(playerNumber);

                System.out.println("Player : " + tournamentStrategies[playerNumber-1] + "'s turn ends");
                System.out.println("------------------------------------------------------------------");
            }
        }
        
        if(!playerWon) {
            /* turns ended set the game to draw */
            gameWinnerList.put(map, "Game " + gameIndex + "$Draw");
        }        
        
        System.out.println("Game  " + gameIndex + " has ended.");
        System.out.println("-------------------------------");
    }

    /**
     * Remove player from the player list if he has 0 countries after an attack  
     */
    private static void checkPlayerStatus() {    	     	
    	for(Entry<Integer, Player> player : Game.getPlayersList().entrySet()) {
    		if(player.getValue().getOwnedCountries().size() == 0) {
    			System.out.println("------------------------------------------------------------------");
    			System.out.println("Removing player " + player.getValue().getPlayerName() + " due to insufficient owned nunmber countries.");
    			removeList.put(player.getKey(), true);
    			System.out.println("------------------------------------------------------------------");
    		}
    	}
    	
//    	for(int removePlayerNumber : removeList) {
//    		Game.getPlayersList().remove(removePlayerNumber);
//    	}
	}

	private static void setGameWinner(int gameIndex, String map, String playerName) {
        gameWinnerList.put(map, "Game " + gameIndex + "$" + playerName);
    }

    /**
     * This method checks if the player has won the game
     *
     * @param player number indicating player
     * @return true if the player has won else false
     */
    public static boolean hasPlayerWon(int player) {
        return Country.getListOfCountries().size() == Game.getPlayersList().get(player).getOwnedCountries().size();
    }

    private static void calculateReinforcementArmies(int playerNumber) {
        Player currentPlayer = Game.getPlayersList().get(playerNumber);
        int newArmies = currentPlayer.getOwnedCountries().size() / 3;

        for (java.util.Map.Entry<String, Continent> entry : Game.getMap().getContinents().entrySet()) {
            String key = entry.getKey();
            Continent value = entry.getValue();

            if (value.checkOwnership(currentPlayer) == true) {
                newArmies += value.getControlValue();
            }
        }

        currentPlayer.setPlayerNumOfArmy(newArmies);
    }

    private static void setupGameAndPlayers(String mapFileName, String[] tournamentStrategies, int tournamentTurnsNum) {
        loadTournamentMap(mapFileName);
        addTournamentPlayers(tournamentStrategies);
        populateCountries(tournamentStrategies.length);
        placeTournamentPlayerArmies(tournamentStrategies.length);
    }

    private static void placeTournamentPlayerArmies(int noOfPlayers) {
        for (int playerNumber = 1; playerNumber <= noOfPlayers; playerNumber++) {
            Player currentPlayer = Game.getPlayersList().get(playerNumber);
            ArrayList<String> countries = currentPlayer.getOwnedCountries();

            // get countries with zero armies
            for (String country : countries) {
                if (Country.getListOfCountries().get(country).getNumberOfArmies() == 0) {
                    Country.getListOfCountries().get(country).setNumberOfArmies(1);
                    currentPlayer.setPlayerNumOfArmy(currentPlayer.getPlayerNumOfArmy() - 1);
                }
            }
            while (currentPlayer.getPlayerNumOfArmy() != 0) {
                for (String country : countries) {
                    if (currentPlayer.getPlayerNumOfArmy() == 0) {
                        break;
                    }
                    Country.getListOfCountries().get(country).setNumberOfArmies(Country.getListOfCountries().get(country).getNumberOfArmies() + 1);
                    currentPlayer.setPlayerNumOfArmy(currentPlayer.getPlayerNumOfArmy() - 1);
                }
            }
            GameLaunch.printPlayerInformation(playerNumber);
        }
    }

    public boolean checkIfArmiesPlaced() {
        boolean allPlayerDone = true;
        HashMap<Integer, Player> playerList = Game.getPlayersList();

        for (int playerNumber = 1; playerNumber <= playerList.size(); playerNumber++) {
            if (playerList.get(playerNumber).getPlayerNumOfArmy() == 0) {
                allPlayerDone = true && allPlayerDone;
            } else {
                allPlayerDone = false && allPlayerDone;
            }
        }
        if (allPlayerDone) {
//            this.notifyObserver("All armies for all the players has been placed");
            return true;
        }
        return false;
    }

    private static void populateCountries(int noOfPlayers) {
        ArrayList<String> countries = new ArrayList<>();

        for (String country : Country.getListOfCountries().keySet()) {
            countries.add(country);
        }

        while (countries.size() > 0) {
            for (int index = 1; index <= noOfPlayers; index++) {
                if (countries.size() > 0) {
                    Country.getListOfCountries().get(countries.get(0)).setOwner(index);                    
                    Game.getPlayersList().get(index).getOwnedCountries().add(countries.get(0)); // assign country to players
                    countries.remove(0);
                }
            }
        }
    }

    private static void addTournamentPlayers(String[] tournamentStrategies) {
        HashMap<Integer, Player> playersData = new HashMap<Integer, Player>();
        int playerId = 1;
        int army[] = {60, 35, 30, 25, 20};
        int noOfPlayers = tournamentStrategies.length;

        for (int index = 1; index <= tournamentStrategies.length; index++) {
            Player currentPlayer;
            String playerStrategy = tournamentStrategies[index - 1];

            if (playerStrategy.equals("aggressive")) {
                currentPlayer = new AggressivePlayer();
            } else if (playerStrategy.equals("benevolent")) {
                currentPlayer = new BenevolentPlayer();
            } else if (playerStrategy.equals("random")) {
                currentPlayer = new RandomPlayer();
            } else {
                currentPlayer = new CheaterPlayer();
            }

            currentPlayer.setPlayerName(playerStrategy);            
            currentPlayer.setPlayerNumOfArmy(army[noOfPlayers - 2]); //
            
            currentPlayer.setPlayerType(playerStrategy);
            playersData.put(playerId, currentPlayer);
            playerId++;
        }

        Game.setPlayersList(playersData); // add the tournament players to the game class static variable
    }

    // Load the tournament map file and convert to Map object
    private static void loadTournamentMap(String mapFileName) {
        MapFileEdit mapEditor = new MapFileEdit();
        mapEditor.selectMapParser(mapFileName);
        Map mapObject = MapFileEdit.mapParser.read(BaseMapFile.MAP_FILE_DIR + mapFileName);
        Game.setMap(mapObject);
    }

    // check if there are duplicate player strategies input
    private static boolean checkPlayerStrategyDuplicates(String argList) {
        String[] playerStrategyArray = argList.split("\\$");
        List<String> playerList = Arrays.asList(playerStrategyArray);
        Set<String> set = new HashSet<String>(playerList);
        if (set.size() < playerList.size()) {
            return true;
        }
        return false;
    }

    // get the command list values and format for later use
    private static List<String> getTournamentCmdValues(int startIndex, int endIndex, List<String> commandList, List<String> processArgList, String type) {
        String commandItemValue = "";
        String commandItem = "";

        for (int index = startIndex; index <= endIndex; index++) {
            commandItem = commandList.get(index).trim();

            if (commandItem.isEmpty()) {
                continue;
            }

            if (!isValidTournamentCmdValue(type, commandItem)) {
                return null;
            }
            commandItemValue += commandItem + "$";
        }

        if (type.equalsIgnoreCase("-G")) {
            if (Integer.parseInt(commandList.get(startIndex).trim()) < 0 || Integer.parseInt(commandList.get(startIndex).trim()) > 5) {
                return null;
            }
            processArgList.add(commandList.get(startIndex).trim() + "$");
        }
        if (type.equalsIgnoreCase("-D")) {
            if (Integer.parseInt(commandList.get(startIndex).trim()) < 10 || Integer.parseInt(commandList.get(startIndex).trim()) > 50) {
                return null;
            }
            processArgList.add(commandList.get(startIndex).trim() + "$");
        } else {
            processArgList.add(commandItemValue);
        }
        return processArgList;
    }


    private static boolean isValidTournamentCmdValue(String type, String commandItem) {
        switch (type) {
            case "-M":
                MapFileEdit mapEditor = new MapFileEdit();
                String mapFileName = commandItem;
                if (mapEditor.selectMapParser(mapFileName) == null) {
                    return false;
                }
                break;

            case "-P":
                String playerStrategy = commandItem.toLowerCase();
                boolean isValidPlayerStrategyInput = Arrays.asList("aggressive", "benevolent", "random", "cheater").contains(playerStrategy);
                if (!isValidPlayerStrategyInput) {
                    return false;
                }
                break;

            case "G":

                break;
        }
        return true;
    }

    // get the end index of the command argument starting the parameter `start`
    private static int getTournamentCmdListEndIndex(int startIndex, List<String> commandList) {
        for (int index = startIndex + 1; index < commandList.size(); index++) {
            String commandItem = commandList.get(index).trim();

            if (commandItem.isEmpty()) {
                continue;
            }

            // Break out of the loop if the next command item is found => -G or -D or -P or -M
            if (commandItem.equalsIgnoreCase("-D") || commandItem.equalsIgnoreCase("-P") || commandItem.equalsIgnoreCase("-G") || commandItem.equalsIgnoreCase("-M")) {
                // process the values and return the index
                return index - 1;
            }
        }
        return -1;
    }

}
