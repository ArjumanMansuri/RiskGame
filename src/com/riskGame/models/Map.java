package com.riskGame.models;

import java.util.HashMap;

/**
 * This is a model class for Map that holds the properties of map.
 * @author goumis
 *
 */
public class Map {
	private HashMap<String, Continent> continents;

	/**
	 * This is a default constructor that will create Map object.
	 *
	 */
	public Map() {
		continents = new HashMap<String, Continent>();
	}

	/**
	 * getter method to get list of continents.
	 * @return hashmap of continents.
	 *
	 */
	public HashMap<String, Continent> getContinents() {
		return continents;
	}

	/**
	 * setter method to set list of continents.
	 * @param continents hashmap of continents.
	 */
	public void setContinents(HashMap<String, Continent> continents) {
		this.continents = continents;
	}
}
