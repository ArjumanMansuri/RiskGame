package view;

import java.util.Scanner;

import controller.FortificationPhase;
import controller.ReinforcementPhase;
import controller.StartUpPhase;
import models.Game;

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

			switch(optionMain) {
			case 1:
				System.out.println("Enter number of players ranging from 2 to 6:");
				noOfPlayers = sc.nextLine().trim();

				String response ;
				StartUpPhase startUpPhase = new StartUpPhase();
				System.out.println(startUpPhase.parser(noOfPlayers));
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

				// Show what countries belong to which player

				// Army assignment starts
				System.out.println("Army assignment starts");
				while(!response.equals("done")) {
					for(int i=1;i<=Integer.parseInt(noOfPlayers);i++) {
						System.out.println("Player : "+Game.getPlayersList().get(i).getPlayerName());
						System.out.println("Selct your option");
						System.out.println("1.Place armies individually");
						System.out.println("2.Place All");
						int armyOption = Integer.parseInt(sc.nextLine());
						if(armyOption==1) {
							if(Game.getPlayersList().get(i).getPlayerNumOfArmy()!=0) {
								System.out.println("Enter command as : placearmy 'countryname'");
								response = startUpPhase.placeArmy(i, sc.nextLine().toString());
							}
							else {
								continue;
							}
						}
						else {
							if(Game.getPlayersList().get(i).getPlayerNumOfArmy()!=0) {
								response = startUpPhase.placeAll(i);
							}
						}
					}
				}

				System.out.println("Initial army assignment is done");

				// reinforcement phase starts
				
				ReinforcementPhase rp = new ReinforcementPhase();
				FortificationPhase fp = new FortificationPhase();
				
				for(int i=1;i<=Integer.parseInt(noOfPlayers);i++) {
					System.out.println("Player : "+Game.getPlayersList().get(i).getPlayerName());
					System.out.println("Reinforcement phase starts");
					while(Game.getPlayersList().get(i).getPlayerNumOfArmy()!=0) {
							System.out.println("Use command : reinforce 'countryname' 'num'");
							System.out.println("Player : "+Game.getPlayersList().get(i).getPlayerName());
							rp.reinforce(i,sc.nextLine());
				}
				
				// fortification phase starts
				System.out.println("Fortification phase starts");
					
				System.out.println("Use command : fortify 'fromcountry' 'tocountry' 'num'");
				System.out.println("Or use command : fortify none");
				System.out.println("Player : "+Game.getPlayersList().get(i).getPlayerName());

				if(Game.getPlayersList().get(i).getPlayerNumOfArmy()!=0) {
					response = fp.fortify(i,sc.nextLine());
				}
				
				System.out.println("Player "+i+"'s turn ends");
			}
		}
		}while(optionMain!=3);
	}

}
