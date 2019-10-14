package models;

import java.util.HashMap;

public class Map {
	private HashMap<String, Continent> continents;

	public Map() {
		continents = new HashMap<String, Continent>();
	}
	
	public HashMap<String, Continent> getContinents() {
		return continents;
	}

	public void setContinents(HashMap<String, Continent> continents) {
		this.continents = continents;
	}	
}
