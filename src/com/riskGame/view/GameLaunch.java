package com.riskGame.view;

import java.security.KeyStore.Entry;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Mudra-PC
 */

import com.riskGame.controller.FortificationPhase;
import com.riskGame.controller.MapFileEdit;
import com.riskGame.controller.ReinforcementPhase;
import com.riskGame.controller.StartUpPhase;
import com.riskGame.models.Country;
import com.riskGame.models.Game;

public class GameLaunch {

	public static void main(String[] args) {

		int optionMain;
		String noOfPlayers;
		do {

			System.out.println("Welcome to the Game");
			System.out.println("Select from the following options:");
			System.out.println("1.Play Game");
			System.out.println("2.Edit Map");
			System.out.println("3.Exit");

			Scanner sc = new Scanner(System.in);
			optionMain = Integer.parseInt(sc.nextLine());
			String response ="";
			switch(optionMain) {
			case 1:
				StartUpPhase startUpPhase = new StartUpPhase();
				do{
					System.out.println("Enter number of players ranging from 2 to 6:");
					noOfPlayers = sc.nextLine().trim();
					response=startUpPhase.parser(noOfPlayers);
					System.out.println(response);
				}while(response.equals("error"));


				// choose map phase
				System.out.println("Choose Map :");
				// List the map files to choose from from the maps folder in the project
				do {
					System.out.println("To load a map, use loadmap 'filename'.");
					String input = sc.nextLine();
					response = startUpPhase.parser(input);
					if(response.equals("error")) {
						System.out.println("Incorrect command format.");
					}
				}while(!response.equals("exit"));

				// add Player phase
				do {
					System.out.println("Enter gameplayer -add playername -remove playername");
					String input = sc.nextLine();
					response = startUpPhase.parser(input);
					if(response.equals("error")) {
						System.out.println("Incorrect command format.");
					}
				}while(!response.equals("exit"));

				System.out.println("populatecountries - to assign countries to players");
				startUpPhase.parser(sc.nextLine());
				// Show what countries belong to which player

				// Army assignment starts
				System.out.println("Army assignment starts");
				while(!response.equals("done")) {
					for(int i=1;i<=Integer.parseInt(noOfPlayers);i++) {
						System.out.println("Player : "+Game.getPlayersList().get(i).getPlayerName());
						if(Game.getPlayersList().get(i).getPlayerNumOfArmy()!=0) {
							System.out.println("Select your option");
							System.out.println("1.Place armies individually");
							System.out.println("2.Place All");
							int armyOption = Integer.parseInt(sc.nextLine());
							if(armyOption==1) {

								do {
									if(response.contains("Error")) {
										System.out.println(response);
									}
									System.out.println("Enter command as : placearmy 'countryname'");
									response = startUpPhase.placeArmy(i, sc.nextLine().toString());
								}while(!response.equals("donePlaceArmy"));


							}
							else {
								do {
									if(response.contains("Error")) {
										System.out.println(response);
									}
									System.out.println("Enter command as : placeall");
									String command = sc.nextLine().trim();

									response = startUpPhase.placeAll(i,command);


								}while(!response.equals("donePlaceall"));
							}

						}
						else {
							System.out.println("All your armies have been placed");
						}
					}
					response = startUpPhase.allPlayerArmies();
				}

				System.out.println("Initial army assignment is done");

				// reinforcement phase starts

				ReinforcementPhase rp = new ReinforcementPhase();
				FortificationPhase fp = new FortificationPhase();

				for(int i=1;i<=Integer.parseInt(noOfPlayers);i++) {
					System.out.println("Player : "+Game.getPlayersList().get(i).getPlayerName());
					System.out.println("Reinforcement phase starts");
					
					rp.calculateReinforcementArmies(i);
					
					while(Game.getPlayersList().get(i).getPlayerNumOfArmy()!=0) {
						if(response.contains("Error")) {
							System.out.println(response);
						}
						System.out.println("Use command : reinforce 'countryname' 'num'");
						System.out.println("Player : "+Game.getPlayersList().get(i).getPlayerName());
						GameLaunch.printPlayerInformation(i);
						System.out.println("Number of reinforcement armies available : "+Game.getPlayersList().get(i).getPlayerNumOfArmy());
						response = rp.reinforce(i,sc.nextLine());
					}
					System.out.println("Attack Phase for now is skipped.");

					// fortification phase starts
					System.out.println("Fortification phase starts");
					response = "";
					do {
						if(response.contains("Error")) {
							System.out.println(response);
						}

						System.out.println("Player : "+Game.getPlayersList().get(i).getPlayerName());

						GameLaunch.printPlayerInformation(i);

						System.out.println("Use command : fortify 'fromcountry' 'tocountry' 'num'");
						System.out.println("Or use command : fortify none");


						String command = sc.nextLine().trim();
						response = fp.fortify(i,command);

					}
					while(!response.equals("done"));
					System.out.println("Player "+i+"'s turn ends");
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

					if(fileExistsResponse.contains("error")) {
						System.out.println("Error in the -editmap command. Re-enter the command.");
						continue;
					}

					// Map file checked and created if not exist 
					if(!fileExistsResponse.equals("exists")) {
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
					} while(!response.equals("saved"));
				} while(fileExistsResponse.equals("error"));
				break;
			}
		}while(optionMain!=3);
	}
	public static void printPlayerInformation(int i) {
		// Printing players' countries with adjacent countries and number of armies
		HashMap<String, Country> countries = Game.getPlayersList().get(i).getOwnedCountries();
		for(java.util.Map.Entry<String, Country> k:countries.entrySet()) {
			System.out.print(k.getKey() +" : "+ k.getValue().getNumberOfArmies() + " : ");
			for(Country country : k.getValue().getNeighbours()) {
				System.out.print(country.getCountryName()+" ");
			}
			System.out.println();
		}
	}
}