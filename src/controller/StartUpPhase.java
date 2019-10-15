package controller;

import models.Player;
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

	public String placeArmy(int i,String abc){
		return "abc";
	}

	public String placeAll(int i){
		return "abc";
	}

	public String parser(String input){

		//Check if input is empty or blank
		if(input.isEmpty() || input.isBlank()) return "error";

		input = input.trim();

		//check if the input is number of player or any other command
		if(input.matches("\\d+")){
			noOfPlayers = Integer.parseInt(input);
			if(noOfPlayers<2 || noOfPlayers>6) return "error";
		}

		String thisInput = input.toLowerCase();

		//call the gameplayer function
		if(thisInput.contains("gameplayer")){
			System.out.println(noOfPlayers);
			if(inputValidator(thisInput)==0) return "error";
			else return gamePlayer(thisInput);
		}
		else
			return "final return";
	}

	String gamePlayer(String thisInput){

		String[] parsedString = thisInput.split(" ");
		int playerId=1,army[]={60,35,30,25,20};

		for(int i=1;i<parsedString.length;i++){
			if(parsedString[i].equals("-add")){
				Player p = new Player();
				p.setPlayerName(parsedString[i+1]);

				if(!playersData.containsValue(p)){
					p.setPlayerNumOfArmy(army[noOfPlayers-2]);
					playersData.put(playerId,p);
					i++;
					playerId++;
				}
				else i++;
			}
			else if(parsedString[i].equals("-remove")){
				Player p = new Player();
				System.out.println("Here 1");
				p.setPlayerName(parsedString[i+1]);
				System.out.println(p.getPlayerName());
				if(playersData.containsValue(p)){
					System.out.println("Here 2");
					for(int x=1;i<=noOfPlayers;i++){
						Player temp;
						temp = playersData.get(x);
						if(temp.getPlayerName().equals(parsedString[i+1])){
							System.out.println("Here 3");
							playersData.remove(x);
							noOfPlayers--;
						}
					}
				}
				else return "notfound";
			}
		}
		System.out.println(playersData);
		/*for(int a=1;a<playersData.size();a++){
			System.out.println();
		}*/
		if(playersData.size()<noOfPlayers)
			return "addmore";
		else
			return "exit";
	}

	//input validator
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

	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		StartUpPhase s = new StartUpPhase();
		System.out.println(s.parser(sc.nextLine()));
		System.out.println(s.parser(sc.nextLine()));
		System.out.println(s.parser(sc.nextLine()));
	}
}


