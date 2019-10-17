package com.riskGame.models;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class Country {
	
	private static Set<String> listOfCountries = new TreeSet<String>();
	
	public static Set<String> getListOfCountries() {
		return listOfCountries;
	}

	public static void setListOfCountries(Set<String> listOfCountries) {
		Country.listOfCountries = listOfCountries;
	}
	private String countryName;
	private int numberOfArmies;
	private ArrayList<Country> neighbours;
	private String owner;
	private String continent;
	
	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public Country() {
		neighbours = new ArrayList<Country>();
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
	public ArrayList<Country> getNeighbours() {
		return neighbours;
	}
	public void setNeighbours(ArrayList<Country> neighbours) {
		this.neighbours = neighbours;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
}
