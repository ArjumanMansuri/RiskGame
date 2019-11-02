package com.riskGame.models;

import java.util.HashMap;

public class Country {

	private static HashMap<String,Country> listOfCountries = new HashMap<>();

	private String countryName;
	private int numberOfArmies;
	private HashMap<String, Country> neighbours;
	private int owner;
	private String continent;

	public static HashMap<String, Country> getListOfCountries() {
		return listOfCountries;
	}

	public static void setListOfCountries(HashMap<String, Country> listOfCountries) {
		Country.listOfCountries = listOfCountries;
	}
	
	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public Country() {
		neighbours = new HashMap();
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}


	public int getNumberOfArmies() {
		return numberOfArmies;
	}
	public void setNumberOfArmies(int numberOfArmies) {
		this.numberOfArmies = numberOfArmies;
	}
	

	public HashMap<String, Country> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(HashMap<String, Country> neighbours) {
		this.neighbours = neighbours;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}
	

}
