package com.riskGame.controller;

import java.util.*;

public class TournamentMode {

//    static HashMap<String, >

    // Take tournament command input and process
    public static void tournamentCommandInput() {

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

            try {
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
                processArgList = getTournamentCmdValues(playerListArgStartIndex + 1, playerListEndIndex - 1, commandList, processArgList, "-P");
//                boolean strategyDuplicatesExist = checkPlayerStrategyDuplicates(processArgList.get(1)); // ## check later
                if (processArgList == null) {
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

            } catch (IndexOutOfBoundsException e) {

            }
            return true;
        }
        return false;
    }

    private static void processTournamentCommandArgs(List<String> processArgList) {
        // outer loop : maps - each map
        // inner loop : game - eachg game
        // until number of turns all the  player complete


        //

    }

    private static boolean checkPlayerStrategyDuplicates(String argList) {
        String[] playerStrategyArray = argList.split("$");
        List<String> playerList = Arrays.asList(playerStrategyArray);
        Set<String> set = new HashSet<String>(playerList);

        if (set.size() < playerList.size()) {
            return true;
        }
        return false;
    }

    // get the command list values and format for later use
    private static List<String> getTournamentCmdValues(int startIndex, int endIndex, List<String> commandList, List<String> processArgList, String type) {
        String commandItemValue = type + "$";
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
            processArgList.add(type + "$" + commandList.get(startIndex).trim() + "$");
        }
        if (type.equalsIgnoreCase("-D")) {
            if (Integer.parseInt(commandList.get(startIndex).trim()) < 10 || Integer.parseInt(commandList.get(startIndex).trim()) > 50) {
                return null;
            }
            processArgList.add(type + "$" + commandList.get(startIndex).trim() + "$");
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
