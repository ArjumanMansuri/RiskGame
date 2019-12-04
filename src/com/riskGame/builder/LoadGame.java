package com.riskGame.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.riskGame.models.Card;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Map;
import com.riskGame.models.Player;

/**
 * 
 * @author Arjuman Mansuri
 * class for handling game load operation
 */
public class LoadGame extends ProductBuilder{
	/**
	 * This method performs the load game operation
	 * @param command command to load game
	 * @return done or error depending on the command
	 * @throws IOException
	 */
	public String buildGame(String command) throws IOException {
		if(!command.contains("loadgame")) {
			return "Error : Invalid command";
		}
		if(command.split(" ").length!=2) {
			return "Error : Invalid number of arguments";
		}
		
		String filename = command.split(" ")[1].trim();
		
		boolean fileExists = new File(filename+".txt").exists();
		
		if(!fileExists) {
			return "Error : "+filename+".txt does not exist. Please try the name that you saved your game with.";
		}
		
		FileInputStream fi = new FileInputStream(new File(filename+".txt"));
		ObjectInputStream oi = new ObjectInputStream(fi);

		// Read objects
		
		try {
			Game.setCards((ArrayList<Card>) oi.readObject());
			Game.setMap((Map) oi.readObject());
			Game.setPhase((String) oi.readObject());
			Game.setPlayersList((HashMap<Integer, Player>) oi.readObject());
			Game.setPlayerTurn((int) oi.readObject());
			Country.setListOfCountries((HashMap<String, Country>) oi.readObject());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oi.close();
		fi.close();
		
		System.out.println("Game has been successfully loaded from "+filename+".txt");
		
		return "done";
	}
}
