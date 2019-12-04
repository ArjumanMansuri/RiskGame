package com.riskGame.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.riskGame.models.Country;
import com.riskGame.models.Game;

/**
 * 
 * @author Arjuman Mansuri
 * class for handling game save operation
 */
public class SaveGame extends ProductBuilder{
	/**
	 * This method performs the save game operation
	 * @param command command to save game
	 * @return done or error depending on the command
	 * @throws IOException 
	 */
	public String buildGame(String command) throws IOException {
		
		if(!command.contains("savegame")) {
			return "Error : Invalid command";
		}
		if(command.split(" ").length!=2) {
			return "Error : Invalid number of arguments";
		}
		
		String filename = command.split(" ")[1].trim();
		
		boolean fileExists = new File(filename+".txt").exists();
		
		if(fileExists) {
			return "Error : "+filename+".txt already exists. Please try a different name.";
		}
		
		FileOutputStream f = new FileOutputStream(new File(filename+".txt"));
		ObjectOutputStream o = new ObjectOutputStream(f);

		// Write objects to file
		o.writeObject(Game.getCards());
		o.writeObject(Game.getMap());
		o.writeObject(Game.getPhase());
		o.writeObject(Game.getPlayersList());
		o.writeObject(Game.getPlayerTurn());
		o.writeObject(Country.getListOfCountries());
		
		System.out.println("Game has been successfully saved to "+filename+".txt");
		System.out.println("Exiting the game for now...");
		o.close();
		f.close();
		return "done";
	}
}
