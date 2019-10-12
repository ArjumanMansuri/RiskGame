package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

/**
 * @author SaiChitta
 * Model file to read Map file 
 * 
 */
public class MapFileParser {
	
	public static final String MAP_FILE_NAME = "default.map";
	String fileData;

	public String getFileData() {
		return fileData;	
	}

	public void setFileData(String fileData) {
		this.fileData = fileData;
	}
		
	public void readFileData(){
		try {
			boolean territoryLineFound = false,continentsLineFound = false;
			BufferedReader readMap = new BufferedReader(new FileReader(MAP_FILE_NAME));
			String line;
			while( (line = readMap.readLine()) != null) {
			    if(line.equals("[Territories]")) {
			    	territoryLineFound = true;
			    	continentsLineFound = false;
			    	continue;
			    }
			    if(line.equals("[Continents]")) {
			    	continentsLineFound = true;
			    	territoryLineFound = false;
			    	continue;
			    }
			    if(territoryLineFound) {
			    	String[] continentLine = line.split("=");
			    	String continentName = continentLine[0];
			    	int continentCtlVal = Integer.parseInt(continentLine[1]);
			    }
			    if(continentsLineFound) {
			    	String[] continentLine = line.split("=");
			    	String continentName = continentLine[0];
			    	int continentCtlVal = Integer.parseInt(continentLine[1]);
			    }
			}			
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
		
	public static void main(String[] args) {
		MapFileParser map = new MapFileParser();
		map.readFileData();
	}
}
