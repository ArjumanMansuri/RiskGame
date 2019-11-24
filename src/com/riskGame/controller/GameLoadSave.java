package com.riskGame.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.riskGame.models.Card;
import com.riskGame.models.Country;
import com.riskGame.models.Game;
import com.riskGame.models.Map;
import com.riskGame.models.Player;

public class GameLoadSave {
	
	public static String save(String command) throws IOException {
		
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
	
	public static String load(String command) throws IOException, ClassNotFoundException {
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
		Game.setCards((ArrayList<Card>) oi.readObject());
		Game.setMap((Map) oi.readObject());
		Game.setPhase((String) oi.readObject());
		Game.setPlayersList((HashMap<Integer, Player>) oi.readObject());
		Game.setPlayerTurn((int) oi.readObject());
		Country.setListOfCountries((HashMap<String, Country>) oi.readObject());

		oi.close();
		fi.close();
		
		System.out.println("Game has been successfully loaded from "+filename+".txt");
		
		return "done";
	}
}
