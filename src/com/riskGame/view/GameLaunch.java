package com.riskGame.view;

import java.io.IOException;
import java.util.*;

import com.riskGame.controller.*;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Map;
import com.riskGame.models.Player;

/**
 * This class holds the method that gives the menu option to start game and edit map.
 *
 * @author Mudra-PC
 */
public class GameLaunch {

    /**
     * This main method creates an instance to start the game.
     *
     * @param args arguments  to run main method.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        int optionMain;
        String noOfPlayers;
        do {
            Game.setEditMap(new Map());
            Game.setPlayersList(new HashMap<Integer, Player>());
            System.out.println("Welcome to the Game");
            System.out.println("Select from the following options:");
            System.out.println("1.Play Game");
            System.out.println("2.Edit Map");
            System.out.println("3.Exit");

            Scanner sc = new Scanner(System.in);
            optionMain = Integer.parseInt(sc.nextLine());
            String response = "";
            switch (optionMain) {
                case 1:

                    System.out.println("Select from the following options:");
                    System.out.println("1.Play new game");
                    System.out.println("2.Load saved game");
                    System.out.println("3.Start a Tournament");

                    int gameOpt = Integer.parseInt(sc.nextLine().trim());
                    boolean loadGame = false;

                    if (gameOpt == 2) {
                        System.out.println("To load a saved game, use command : loadgame 'fileName'");
                        String command = sc.nextLine().trim();
                        String result = "";
                        do {
                            if (result.toLowerCase().contains("error")) {
                                System.out.println(result);
                                System.out.println("To load a saved game, use command : loadgame 'fileName'");
                                command = sc.nextLine().trim();
                            }
                            result = GameLoadSave.load(command);
                        } while (!result.equals("done"));
                        loadGame = true;
                    }

                    if (!loadGame) {

                        if (gameOpt == 3) {
                            TournamentMode.tournamentCommandInput();
                        }

                        StartUpPhase startUpPhase = new StartUpPhase();
                        startUpPhase.notifyObserver("Start up Phase started...");

                        do {
                            System.out.println("Enter number of players ranging from 2 to 6:");
                            noOfPlayers = sc.nextLine().trim();
                            response = startUpPhase.parser(noOfPlayers);
                            //System.out.println(response);
                        } while (response.equals("error"));

                        // choose map phase
                        System.out.println("Choose Map :");
                        // List the map files to choose from from the maps folder in the project
                        do {
                            System.out.println("To load a map, use loadmap 'filename'.");
                            String input = sc.nextLine();
                            response = startUpPhase.parser(input);
                            if (response.equals("error")) {
                                System.out.println("Incorrect command format.");
                            } else if (response.equals("fileNotFound")) {
                                System.out.println("File not found");
                            }
                        } while (!response.equals("exit"));

                        // add Player phase
                        do {
                            System.out.println("Enter gameplayer -add playername strategy -remove playername");
                            String input = sc.nextLine();
                            response = startUpPhase.parser(input);
                            if (response.equals("error")) {
                                System.out.println("Incorrect command format.");
                            }
                        } while (!response.equals("exit"));

                        System.out.println("populatecountries - to assign countries to players");
                        startUpPhase.parser(sc.nextLine());
                        // Show what countries belong to which player
                        for (int i = 1; i <= Integer.parseInt(noOfPlayers); i++) {
                            System.out.println("Country population :");
                            GameLaunch.printPlayerInformation(i);
                        }
                        // Army assignment starts
                        System.out.println("Army assignment starts");

                        while (!response.equals("done")) {
                            for (int i = 1; i <= Integer.parseInt(noOfPlayers); i++) {

                                //if a player is non-human, place all its armies automatically
                                if (!Game.getPlayersList().get(i).getPlayerType().toLowerCase().equals("human") && Game.getPlayersList().get(i).getPlayerNumOfArmy() != 0) {
                                    startUpPhase.placeAll(i, "placeall");
                                    GameLaunch.printPlayerInformation(i);
                                } else {
                                    if (Game.getPlayersList().get(i).getPlayerNumOfArmy() != 0) {
                                        System.out.println("Player : " + Game.getPlayersList().get(i).getPlayerName());
                                        System.out.println("Select your option");
                                        System.out.println("1.Place armies individually");
                                        System.out.println("2.Place All");
                                        int armyOption = Integer.parseInt(sc.nextLine());
                                        if (armyOption == 1) {
                                            do {
                                                if (response.contains("Error")) {
                                                    System.out.println(response);
                                                }
                                                System.out.println("Enter command as : placearmy 'countryname'");
                                                response = startUpPhase.placeArmy(i, sc.nextLine().toString());
                                                if (response.equals("donePlaceArmy")) {
                                                    GameLaunch.printPlayerInformation(i);
                                                    startUpPhase.notifyObserver(Game.getPlayersList().get(i).computeDominationViewData());
                                                }
                                            } while (!response.equals("donePlaceArmy"));
                                        } else {
                                            do {
                                                if (response.contains("Error")) {
                                                    System.out.println(response);
                                                }
                                                System.out.println("Enter command as : placeall");
                                                String command = sc.nextLine().trim();

                                                response = startUpPhase.placeAll(i, command);
                                                if (response.equals("donePlaceall")) {
                                                    GameLaunch.printPlayerInformation(i);
                                                    startUpPhase.notifyObserver(Game.getPlayersList().get(i).computeDominationViewData());
                                                }
                                            } while (!response.equals("donePlaceall"));
                                        }
                                    } else {
                                        System.out.println("All your armies have been placed");
                                    }
                                }
                            }
                            response = startUpPhase.allPlayerArmies();
                        }

                        System.out.println("Initial army assignment is done");
                        startUpPhase.notifyObserver("StartUp Phase has ended...");
                    }

                    // reinforcement phase starts
                    ReinforcementPhase rp = new ReinforcementPhase();
                    FortificationPhase fp = new FortificationPhase();
                    char continueGame = 'y';

                    String currentPhase = "";
                    int turn = 1;
                    boolean setTurn = false;
                    if (loadGame) {
                        turn = Game.getPlayerTurn();
                        currentPhase = Game.getPhase();
                    }

                    game:
                    while (continueGame == 'y') {
                        for (int i = 1; i <= Game.getPlayersList().size(); i++) {

                            if (!setTurn) {
                                i = turn;
                                setTurn = true;
                            }

                            Game.setPlayerTurn(i);    // saving player turn in case game is saved
                            System.out.println("Player : " + Game.getPlayersList().get(i).getPlayerName());

                            if (currentPhase.length() == 0 || currentPhase.equals("reinforce")) {
                                currentPhase = "";
                                Game.setPhase("reinforce");  // saving player phase in case game is saved
                                System.out.println("Reinforcement phase starts");
                                // calculating reinforcement armies
                                rp.calculateReinforcementArmies(i);

                                if (Game.getPlayersList().get(i).getPlayerType().equals("human")) {
                                    while (Game.getPlayersList().get(i).getPlayerNumOfArmy() != 0) {
                                        if (response.contains("Error")) {
                                            System.out.println(response);
                                        }
                                        GameLaunch.printPlayerInformation(i);
                                        System.out.println("Use command : reinforce 'countryname' 'num'");
                                        System.out.println("Number of reinforcement armies available : " + Game.getPlayersList().get(i).getPlayerNumOfArmy());
                                        System.out.println("To quit and save the game, use command : savegame");
                                        String command = sc.nextLine().trim();
                                        if (command.contains("savegame")) {
                                            String result = "";
                                            do {
                                                if (result.toLowerCase().contains("error")) {
                                                    System.out.println(result);
                                                    System.out.println("To quit and save the game, use command : savegame 'filename'");
                                                    command = sc.nextLine().trim();
                                                }
                                                result = GameLoadSave.save(command);
                                            } while (!result.equals("done"));
                                            System.exit(0);
                                        }

                                        response = Game.getPlayersList().get(i).getReinforceType().reinforce(i, command);
                                    }
                                } else {
                                    Game.getPlayersList().get(i).getReinforceType().reinforce(i);
                                    GameLaunch.printPlayerInformation(i);
                                }
                            }
                            // Attack Phase starts
                            if (currentPhase.length() == 0 || currentPhase.equals("attack")) {
                                currentPhase = "";
                                Game.setPhase("attack");  // saving player phase in case game is saved
                                System.out.println("Attack phase starts...");
                                AttackPhase ap = new AttackPhase();
                                response = "";
                                int defender = 0;

                                if (Game.getPlayersList().get(i).getPlayerType().equals("human")) {
                                    boolean reAttack = false;
                                    do {
                                        do {
                                            if (response.contains("Error")) {
                                                System.out.println(response);
                                            }

                                            GameLaunch.printPlayerInformation(i);

                                            System.out.println("Use command : attack 'countrynamefrom' 'countynameto' 'numdice'");
                                            String command = "";
                                            if (!reAttack) {
                                                System.out.println("Use command for allout mode : attack 'countrynamefrom' 'countynameto' allout");
                                                System.out.println("Or to skip attack use command : attack -noattack");
                                                System.out.println("To quit and save the game, use command : savegame 'filename'");
                                                String result = "";
                                                command = sc.nextLine().trim();
                                                if (command.contains("savegame")) {
                                                    do {
                                                        if (result.toLowerCase().contains("error")) {
                                                            System.out.println(result);
                                                            System.out.println("To quit and save the game, use command : savegame 'filename'");
                                                            command = sc.nextLine().trim();
                                                        }
                                                        result = GameLoadSave.save(command);
                                                    } while (!result.equals("done"));
                                                    System.exit(0);
                                                }
                                            }

                                            if (reAttack) {
                                                command = command + "reattack";
                                            }
                                            response = Game.getPlayersList().get(i).getAttackType().attackSetup(i, command);
                                            if (response.contains("Conquer")) {
                                                defender = Integer.parseInt(response.split(" ")[2]);
                                                break;
                                            } else if (response.equalsIgnoreCase("done")) {
                                                System.out.println("Attack phase skipped");
                                                response = "noAttack";
                                                break;
                                            }
                                        } while (!response.contains("DefenderPlayer"));

                                        if (response.contains("DefenderPlayer")) {
                                            // get defender's numDice
                                            defender = Integer.parseInt(response.split(" ")[1]);
                                            System.out.println("Defender player is player : " + Game.getPlayersList().get(defender).getPlayerName());
                                            response = "";
                                            do {
                                                if (response.contains("Error")) {
                                                    System.out.println(response);
                                                }
                                                System.out.println("Enter number of dice, you want to roll using 'defend 'numdice'' command");
                                                String command = sc.nextLine().trim();
                                                response = ap.setDefendDice(defender, command);
                                            } while (!response.contains("Conquer"));
                                        }
                                        // If defender country lost and has zero armies on it
                                        if (response.contains("canConquer")) {
                                            int noOfArmies = Integer.parseInt(response.split(" ")[1]);
                                            System.out.println("You can conquer the defender country by moving armies to it. You have " + noOfArmies + " armies left.");
                                            response = "";
                                            do {
                                                if (response.contains("Error")) {
                                                    System.out.println(response);
                                                }
                                                System.out.println("Move armies using the 'attackmove 'num'' command.");
                                                String command = sc.nextLine().trim();
                                                response = ap.moveArmies(i, command);
                                                reAttack = false;
                                            } while (!response.contains("done"));
                                            if (ap.hasPlayerWon(i)) {
                                            	System.out.println("Player "+Game.getPlayersList().get(i).getPlayerName()+" won..!!!");
                                                break game;
                                            }
                                        } else {
                                            if (!response.equalsIgnoreCase("noattack") && ap.isAttackPossible()) {
                                                int noOfArmies = Integer.parseInt(response.split(" ")[1]);
                                                System.out.println("Attacker still have " + noOfArmies + " left. Do you want to attack again y or n.?");
                                                if (sc.nextLine().equalsIgnoreCase("y")) {
                                                    reAttack = true;
                                                } else {
                                                    reAttack = false;
                                                    break;
                                                }
                                            } else {
                                                reAttack = false;
                                            }
                                        }
                                    } while (reAttack == true);

                                    if (!response.equalsIgnoreCase("noAttack")) {
                                        System.out.println("Attack Phase ends");
                                        GameLaunch.printPlayerInformation(i);
                                        GameLaunch.printPlayerInformation(defender);
                                    }
                                }
                                 else {
                                    Game.getPlayersList().get(i).getAttackType().attackSetup(i);
                                    GameLaunch.printPlayerInformation(i);
                                    if(!Game.getPlayersList().get(i).getPlayerType().equals("benevolent")) {
                                    	GameLaunch.printPlayerInformation(AttackPhase.getDefenderPlayer());
                                    }
                                    if (ap.hasPlayerWon(i)) {
                                    	System.out.println("Player "+Game.getPlayersList().get(i).getPlayerName()+" won..!!!");
                                        break game;
                                    }
                                }
                            
                            }

                            // fortification phase starts
                            if (currentPhase.length() == 0 || currentPhase.equals("fortify")) {
                                currentPhase = "";
                                Game.setPhase("fortify");  // saving player phase in case game is saved
                                System.out.println("Fortification phase starts");
                                if (Game.getPlayersList().get(i).getPlayerType().equals("human")) {
                                    response = "";

                                    do {
                                        if (response.contains("Error")) {
                                            System.out.println(response);
                                        }

                                        GameLaunch.printPlayerInformation(i);

                                        System.out.println("Use command : fortify 'fromcountry' 'tocountry' 'num'");
                                        System.out.println("Or use command : fortify -none");
                                        System.out.println("To quit and save the game, use command : savegame");
                                        String command = sc.nextLine().trim();
                                        if (command.contains("savegame")) {
                                            String result = "";
                                            do {
                                                if (result.toLowerCase().contains("error")) {
                                                    System.out.println(result);
                                                    System.out.println("To quit and save the game, use command : savegame 'filename'");
                                                    command = sc.nextLine().trim();
                                                }
                                                result = GameLoadSave.save(command);
                                            } while (!result.equals("done"));
                                            System.exit(0);
                                        }


                                        response = Game.getPlayersList().get(i).getFortifyType().fortify(i, command);
                                        if (response.equals("done")) {
                                            GameLaunch.printPlayerInformation(i);
                                        }

                                    } while (!response.equals("done"));
                                } else {
                                    Game.getPlayersList().get(i).getFortifyType().fortify(i);
                                    GameLaunch.printPlayerInformation(i);
                                }
                                System.out.println("Player " + i + "'s turn ends");
                            }
                        }
                        //System.out.println("Do you want to continue the game? 'y' or 'n'");
                        //continueGame = sc.nextLine().trim().charAt(0);
                        continueGame = 'y';
                    }
                    break;

                case 2:
                    String fileExistsResponse;
                    do {
                        boolean mapFileExists = true;
                        System.out.println("To Edit Map File - editmap 'filename'");
                        String command = sc.nextLine();
                        MapFileEdit mapFileEdit = new MapFileEdit();
                        fileExistsResponse = mapFileEdit.fileExists(command);

                        if (fileExistsResponse.contains("error")) {
                            System.out.println("Error in the -editmap command. Re-enter the command.");
                            continue;
                        }

                        // Map file checked and created if not exist
                        if (!fileExistsResponse.equals("exists")) {
                            mapFileExists = false;
                            System.out.println("Map file does not exist. New Map File created with name " + fileExistsResponse);
                        }

                        do {
                            String editMapFileNameCommand = command;
                            System.out.println("Map File edit commands:");
                            System.out.println("editcontinent -add continentname continentvalue -remove continentname \neditcountry -add countryname continentname -remove countryname \neditneighbor -add countryname neighborcountryname -remove countryname neighborcountryname \nshowmap (show all continents and countries and their neighbors)");
                            System.out.println("savemap 'filename' If done with editing file.");
                            System.out.println("validatemap - to check the validity of map");
                            command = sc.nextLine().trim();
                            response = mapFileEdit.commandParser(command, editMapFileNameCommand, mapFileExists);
                        } while (!response.equals("saved"));
                    } while (fileExistsResponse.equals("error"));
                    break;
            }
        } while (optionMain != 3);
    }

    /**
     * This method prints the countries and the neighboring countries
     * along with number of armies in each countries.
     *
     * @param player number of players.
     */
    public static void printPlayerInformation(int player) {
        // Printing players' countries with adjacent countries and number of armies
        System.out.println("Player : " + Game.getPlayersList().get(player).getPlayerName() + " : " + Game.getPlayersList().get(player).getPlayerType());
        ArrayList<String> countries = Game.getPlayersList().get(player).getOwnedCountries();
        for (String k : countries) {
            Country country = Country.getListOfCountries().get(k);
            System.out.print(country.getCountryName() + " : " + country.getNumberOfArmies() + " : ");
            for (String c : country.getNeighbours().keySet()) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

//tournament -M World.map -P aggressive random  -G 4 -D 10