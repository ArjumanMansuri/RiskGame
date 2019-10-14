package controller;

import models.Player;
import java.util.HashMap;

/**
 * This class controls the startup phase
 */
public class StartUpPhase {
	private HashMap<Integer,Player> playersData;
	private int noOfPlayers;

	public StartUpPhase(){
		playersData = new HashMap<Integer,Player>();
	}

	public String parser(String input){
		// if the command is loadmap, check the validity of the map before loading
		
		// if populatecountries, call corresponding method

		//Check if input is empty or blank
		if(input.isEmpty() || input.isEmpty()) return "error";

		input = input.trim();

		//check if the input is number of player or any other command
		if(input.matches("\\d+")){
			int noOfPlayer = Integer.parseInt(input);
			if(noOfPlayer<2 || noOfPlayer>6) return "error";
		}

		String thisInput = input.toLowerCase();

		//call the gameplayer function
		if(thisInput.contains("gameplayer")){
			if(inputValidator(thisInput)==0) return "error";
			else return gamePlayer(thisInput);
		}
		else
			return "final return";
	}

	public String gamePlayer(String thisInput){

		String[] parsedString = thisInput.split(" ");
		int playerId=1,army[]={60,35,30,25,20};
		int check = noOfPlayers*2 + 1;

		for(int i=1;i<parsedString.length;i++){
			if(parsedString[1].equals("-add")){
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
			else if(parsedString[1].equals("-remove")){
				Player p = new Player();
				p.setPlayerName(parsedString[i+1]);
				if(playersData.containsValue(p)){

					playersData.put(playerId,p);
					i++;
					playerId++;
				}
				else i++;
			}
		}

		if(check<parsedString.length)
			return "addmore";

		return "exit";
	}

	//input validator
	public int inputValidator(String thisInput) throws ArrayIndexOutOfBoundsException{
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
	public String placeArmy(int player,String command) {
		return "";
	}
	
	public String placeAll(int player) {
		return "";
	}

	/*
	 * public static void main(String[] args){ Scanner sc = new Scanner(System.in);
	 * StartUpPhase s = new StartUpPhase();
	 * System.out.println(s.parser(sc.nextLine()));
	 * System.out.println(s.parser(sc.nextLine())); }
	 */
}


